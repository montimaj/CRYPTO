package docrypto;

import java.io.FileInputStream;

public class UserInput {

	public static void main(String[] args) 
	{
		try
		{
			FileInputStream fis=new FileInputStream("foo.txt");
			byte b[]=new byte[fis.available()];
			fis.read(b);
			fis.close();	
			Encrypt.encrypt_file("AB");
			Decrypt.decrypt("cipher_text.txt");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
