package docrypto;

import java.io.FileInputStream;
import java.io.IOException;

import docrypto.utilities.*;

/**
 * Takes plain text file path as input and uses 
 * <p>
 * {@link docrypto.Encrypt} to generate key.txt and cipher_text.txt
 * {@link docrypto.utilities.QRCode} to generate qrcode image from result.zip containing the key and the cipher
 * {@link docrypto.Decrypt} to decrypt
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class UserInput 
{

	public static void main(String[] args) 
	{
		try
		{
			FileInputStream fis=new FileInputStream(args[0]);
			String ext=args[0].substring(args[0].lastIndexOf('.'),args[0].length());
			byte b[]=new byte[fis.available()];
			fis.read(b);
			fis.close();	
			String pt=new String(b);
			Encrypt.encrypt_file(pt);
			String files[]={"key.txt","cipher_text.txt"};
			//ZipCreator.create_zip("result.zip", files);
			//QRCode.gen_qrcode("result.zip");
			Decrypt.decrypt("cipher_text.txt",ext);			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
