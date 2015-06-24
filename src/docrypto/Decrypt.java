package docrypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.Base64;

/**
 * Main Decryption module
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class Decrypt 
{
	private static String extract_chars(int nrows, int ncols, char mat[][], boolean flag[][])
	{
		String s="";
		for(int i=0;i<nrows;++i)
			for(int j=0;j<ncols;++j)
				if(flag[i][j])
					s+=mat[i][j];		
		return s;
	}
	private static void init_matrices(String s, int nrows, char mat[][], boolean flag[][]) throws IOException
	{
		int k=0;	
		DataInputStream key=new DataInputStream(new FileInputStream("key.txt"));
		for(int i=0;i<Encrypt.num_cols();++i)
		{
			int pos=key.readInt();
			for(int j=0;j<nrows;++j)
				if(k<s.length() && flag[j][pos])
					mat[j][pos]=s.charAt(k++);				
		}		
		key.close();
	}
	/**
	 * Performs decryption operation
	 * @param cipher Path to the cipher file
	 * @param ext Extension of the plain text file
	 * @throws IOException
	 */
	public static void decrypt(String cipher, String ext) throws IOException
	{
		FileInputStream cis=new FileInputStream(cipher);
		byte[] b=new byte[cis.available()];
		cis.read(b);
		cis.close();			
		String bits=new String(Base64.getDecoder().decode(b));	
		bits=Encrypt.cipher_to_bits(bits);
		int ncols=Encrypt.num_cols(), nrows=bits.length()/ncols;
		if(bits.length()>nrows*ncols)
			nrows++;
		char mat[][]=new char[nrows][ncols];
		init_matrices(bits, nrows, mat, Encrypt.flag1);			
		String s=extract_chars(nrows, ncols, mat,Encrypt.flag1);			
		s=Encrypt.bits_to_ascii(s);			
		nrows=s.length()/ncols;
		if(s.length()>nrows*ncols)
			nrows++;
		char mat1[][]=new char[nrows][ncols];
		init_matrices(s, nrows, mat1, Encrypt.flag);
		String decrypted_text=extract_chars(nrows, ncols, mat1, Encrypt.flag);
		FileOutputStream dos=new FileOutputStream("decrypted_file"+ext);		
		dos.write(decrypted_text.getBytes("ISO-8859-1"));
		dos.close();				
	}
}
