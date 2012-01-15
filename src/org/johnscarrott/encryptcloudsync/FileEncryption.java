package org.johnscarrott.encryptcloudsync;
import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.*;
import java.util.ArrayList;



public class FileEncryption {
	
	Cipher ciph;
	SecretKey k;
	int keyLength;
	String AESCiphType;
	String rootIn;
	String rootOut;
	private byte[] salt;
	// Initialise fileEncryption object with user password
	public FileEncryption(String passwordInput, String in, String out) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException{
		AESCiphType = "AES/CBC/PKCS5PADDING";
		rootIn = in;
		rootOut = out;		
		PBEKeySpec password = new PBEKeySpec(passwordInput.toCharArray(), saltCheck(), 1000, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKey key = (PBEKey) factory.generateSecret(password);
		k = new SecretKeySpec(key.getEncoded(), "AES");
		FileOutputStream salted = new FileOutputStream("salt.txt");
		salted.write(salt);
		salted.close();
		ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		//ciph = Cipher.getInstance("AES");
		
	}
	private static void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[1024];
		while((i=is.read(b))!=-1) {
			os.write(b, 0, i);
		}
	}
		
	public byte[] saltCheck() throws IOException, NoSuchAlgorithmException{
		File saltCheck = new File("salt.txt");
		boolean exists = saltCheck.exists();
		
		if(exists){
			FileInputStream in = new FileInputStream(saltCheck);
			byte[] salty = new byte[(int) saltCheck.length()];
			in.read(salty);
			salt = salty;
			return salt;
		}
		else{
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
			byte[] salty = new byte[16];
			rand.nextBytes(salty);
			salt = salty;
			return salt;
		}
	}
		
	
	public void encryptFile(File fileToEncrypt, File encryptedFileLocation) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException{
		//Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		File ivread = new File("ivout.txt");
		boolean exists = ivread.exists();
		if(exists){
			FileInputStream in = new FileInputStream(ivread);
			byte[] iv = new byte[(int) ivread.length()];
			in.read(iv);
			ciph.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(iv));
			in.close();
		}
		else{
			ciph.init(Cipher.ENCRYPT_MODE, k);
			byte[] iv = ciph.getIV();
			FileOutputStream ivout = new FileOutputStream("ivout.txt");
			ivout.write(iv);
			ivout.close();
		}
		
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileToEncrypt));
		CipherOutputStream os = new CipherOutputStream(new FileOutputStream(encryptedFileLocation), ciph);
		copy(is,os);
		is.close();
		os.close();
		/*byte[] iv = ciph.getIV();
		FileOutputStream ivout = new FileOutputStream("ivout.txt");
		ivout.write(iv);
		ivout.close();
		*/
	}
	public void decryptFile(File fileToDecrypt, File DecryptedFileLocation) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException{
		//Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		File ivread = new File("ivout.txt");
		
		FileInputStream in = new FileInputStream(ivread);
		byte[] iv = new byte[(int) ivread.length()];
		in.read(iv);
		
		ciph.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileToDecrypt));
		CipherOutputStream os = new CipherOutputStream(new FileOutputStream(DecryptedFileLocation), ciph);
		copy(is,os);
		is.close();
		os.close();
	}
	public void encryptDir(ArrayList<File> Files, int mode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
		if(mode == 1){
			for(int counter = 0; counter < Files.size(); counter++){
				File out = new File(rootOut, Files.get(counter).getName());
				encryptFile(Files.get(counter), out) ;
			}
		}
		else{
			for(int counter = 0; counter < Files.size(); counter++){
				File out = new File(rootIn, Files.get(counter).getName());
				decryptFile(Files.get(counter), out) ;
			}
			
		}
	}
	public int checkPassword(String dirIn, String dirOut) throws InvalidKeyException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException{
		File decryptCheckRoot = new File("donotdelete.txt");
		int success = 0;
		File encryptCheckRoot = new File("donotdeleteencrypted.txt");
		File decryptCheckDecrypted = new File(dirIn + "\\" +"donotdelete.txt");
		File encryptCheckEncrypted = new File("donotdelete.txt");
		encryptFile(encryptCheckEncrypted, new File("donotdeleteencryptedbyold"));
		
		FileInputStream in = new FileInputStream(decryptCheckRoot);
		byte[] iv = new byte[(int) decryptCheckRoot.length()];
		in.read(iv);
		FileInputStream in2 = new FileInputStream(decryptCheckDecrypted);
		byte[] iv2 = new byte[(int) decryptCheckDecrypted.length()];
		in2.read(iv2);
		if(iv != iv2){
			success = 1;
		}
		in.close();
		in2.close();
		
		FileInputStream in3 = new FileInputStream(encryptCheckRoot);
		byte[] iv3 = new byte[(int) decryptCheckRoot.length()];
		in3.read(iv);
		FileInputStream in4 = new FileInputStream(new File("donotdeleteencryptedbyold"));
		byte[] iv4 = new byte[(int) encryptCheckEncrypted.length()];
		in4.read(iv4);
		if(iv3 != iv4){
			success = 2;
		}
		in3.close();
		in4.close();
		return success;
		
		
	}
	/*
	public void writeAESKey(File keyLocation, File encryptedKeyLocation) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException{
		byte[] encodedKey = new byte[(int)keyLocation.length()];//reads a  plain text key file then encrypts it using the RSA cipher, using a keyspec and factory to make sure the key stays in the correct format.
	    new FileInputStream(keyLocation).read(encodedKey);
	    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pk = kf.generatePublic(publicKeySpec);
	    Cipher pkCipher = Cipher.getInstance(RSACiphType);
		// write AES key
	    pkCipher.init(Cipher.ENCRYPT_MODE, pk);
		CipherOutputStream os = new CipherOutputStream(new FileOutputStream(encryptedKeyLocation), pkCipher);
	    os.write(encodedKey);
	    os.close();
	}
	public SecretKey readAESKey(File keyLocation) throws IOException, NoSuchAlgorithmException{
		byte[] encodedKey = new byte[(int)keyLocation.length()];
		new FileInputStream(keyLocation).read(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(encodedKey);
		
		k = kf.
		return k;
	}Potentially use for extra options instead of PBE
	*/
}