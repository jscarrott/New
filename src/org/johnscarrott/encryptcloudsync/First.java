package org.johnscarrott.encryptcloudsync;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import java.awt.FlowLayout;

public class First extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static File dirIn;
	public static File dirOut;
	public String pass;
	 private JFileChooser fc = new JFileChooser();
	 private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 */
	public First() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblSelectInputFolder = new JLabel("Select Input Folder:");
		getContentPane().add(lblSelectInputFolder);
		
		JButton button = new JButton("...");
		button.addActionListener(new OpenListenerIn());
		
		getContentPane().add(button);
		
		JLabel lblSelectOutputFolder = new JLabel("Select Output Folder");
		getContentPane().add(lblSelectOutputFolder);
		
		JButton button_1 = new JButton("...");
		getContentPane().add(button_1);
		
		JLabel lblPassword = new JLabel("Password");
		getContentPane().add(lblPassword);
		button_1.addActionListener(new OpenListenerOut());
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		getContentPane().add(passwordField);
		
		JButton btnEncrypt = new JButton("Encrypt");
		getContentPane().add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Decrypt");
		getContentPane().add(btnDecrypt);
		
		JButton btnNewPassword = new JButton("New Password");
		getContentPane().add(btnNewPassword);
		pass = passwordField.toString();
		btnEncrypt.addActionListener(new EncryptListener());
		btnDecrypt.addActionListener(new DecryptListener());
		btnNewPassword.addActionListener(new ResetListener());
		//if(filesSelected == 1) {
		
		//}
		
	}
	private class OpenListenerIn implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(First.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //this is where a real application would open the file.
                dirIn = file;
        }
    }
        
}
	private class OpenListenerOut implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(First.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //this is where a real application would open the file.
                dirOut = file;
        }
    }
	}
	private class EncryptListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	FileEncryption test = null;
			try {
				test = new FileEncryption(pass, dirIn.toString(), dirOut.toString());
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
    		DirectoryCheck testy = new DirectoryCheck(dirIn.toString());
    		try {
				testy.pollDirectory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				test.encryptDir(testy.readDirectory(), 1);
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidAlgorithmParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				test.encryptFile(new File("donotdelete.txt"), new File("donotdeleteencrypted.txt"));
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidAlgorithmParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
	private class DecryptListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	FileEncryption test = null;
			try {
				test = new FileEncryption(pass, dirIn.toString(), dirOut.toString());
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(test.checkPassword(dirIn.toString(), dirOut.toString()) == 2){
					File ivread = new File("ivout.txt");
					boolean exists = ivread.exists();
					FileInputStream in = new FileInputStream(ivread);
					byte[] iv = new byte[(int) ivread.length()];
					in.read(iv);
					test.ciph.init(Cipher.ENCRYPT_MODE, test.k, new IvParameterSpec(iv));
					in.close();
				}
			} catch (InvalidKeyException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InvalidAlgorithmParameterException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} catch (NoSuchAlgorithmException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (NoSuchPaddingException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
    		DirectoryCheck testy = new DirectoryCheck(dirOut.toString());
    		try {
				testy.pollDirectory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				test.encryptDir(testy.readDirectory(), 0);
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidAlgorithmParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
        }
    }
	private class ResetListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        	FileEncryption test = null;
			try {
				test = new FileEncryption(pass, dirIn.toString(), dirOut.toString());
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(test.checkPassword(dirIn.toString(), dirOut.toString()) == 0){
					System.exit(1);
				}
			} catch (InvalidKeyException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (NoSuchAlgorithmException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (NoSuchPaddingException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (InvalidAlgorithmParameterException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
    		DirectoryCheck testy = new DirectoryCheck(dirIn.toString());
    		try {
				testy.pollDirectory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				test.encryptDir(testy.readDirectory(), 1);
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidAlgorithmParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        }
    
	
public static void main(String s[]) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
	try {
		  UIManager.setLookAndFeel(
		    UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	JFrame frame = new First();
	frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
}
}

