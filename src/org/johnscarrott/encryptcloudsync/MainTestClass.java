package org.johnscarrott.encryptcloudsync;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;


public class MainTestClass {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InterruptedException 
	 */
	static String fileName;
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, InterruptedException {
		FileEncryption test = new FileEncryption("dadspurplesocks2331", "C:/TestingFolder", "C:/TestingFolder3");
		File root = new File("C:/TestingFolder");
		/*DirectoryCheck testy = new DirectoryCheck();
		in = getAbsoluteFile(root, testy.watchServ().toString());
		test.encryptFile(in, out);*/
		root.getAbsoluteFile();
		DirectoryCheck testy = new DirectoryCheck("C:/TestingFolder");
		testy.pollDirectory();
		test.encryptDir(testy.readDirectory(), 1);

	}
	public static File getAbsoluteFile(File root, String path) {
		  File file = new File(path);
		  if (file.isAbsolute())
		    return file;

		  if (root == null)
		    return null;

		  return new File(root, path);
		}

}
