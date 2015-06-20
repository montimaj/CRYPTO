package docrypto;

import java.io.FileInputStream;
import java.io.IOException;

import docrypto.utilities.*;
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
			Encrypt.encrypt_file(new String(b));
			String files[]={"key.txt","cipher_text.txt"};
			ZipCreator.create_zip("result.zip", files);
			QRCode.gen_qrcode("result.zip");
			Decrypt.decrypt("cipher_text.txt",ext);			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
