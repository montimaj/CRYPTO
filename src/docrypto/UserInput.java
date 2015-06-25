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

	/**
	 * Get execution time in millisecond or second
	 * @param st Start time
	 * @param et End Time
	 * @return Execution Time as a String
	 */
	public static String getExecutionTime(long st, long et)
	{
		double time=(et-st)/1e+6;
		String t=time+" ms";			
		if(time>=1000.)
		{
			time/=1000.;
			t=time+" s"; 
		}					
		return t;
	}
	/**
	 * Main module 
	 * @param args Input file path
	 */
	public static void main(String[] args) 
	{
		try
		{
			FileInputStream fis=new FileInputStream(args[0]);
			String ext=args[0].substring(args[0].lastIndexOf('.'),args[0].length());
			byte b[]=new byte[fis.available()];
			fis.read(b);
			fis.close();	
			String pt=new String(b, "ISO-8859-1");		
			long st=System.nanoTime();
			Encrypt.encrypt_file(pt);
			long et=System.nanoTime();
			System.out.println("Encryption time= "+getExecutionTime(st,et));
			//String files[]={"key.txt","cipher_text.txt"};
			//ZipCreator.create_zip("result.zip", files);
			//QRCode.gen_qrcode("result.zip");
			st=System.nanoTime();		
			Decrypt.decrypt("cipher_text.txt",ext);
			et=System.nanoTime();			
			System.out.println("Decryption time= "+getExecutionTime(st,et));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
