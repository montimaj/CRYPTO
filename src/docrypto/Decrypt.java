package docrypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

/**
 * Main Decryption module
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class Decrypt 
{
	private static String extract_chars(int nrows, int ncols, char mat[][])
	{
		String s="";
		for(int i=0;i<nrows;++i)
			for(int j=0;j<ncols;++j)
				s+=mat[i][j];		
		return s;
	}
	private static void init_matrices(String s, int nrows, char mat[][]) throws IOException
	{
		int k=0;	
		DataInputStream key=new DataInputStream(new FileInputStream("key.txt"));
		for(int i=0;i<Encrypt.num_cols();++i)
		{
			int pos=key.readInt();
			for(int j=0;j<nrows;++j)
				mat[j][pos]=s.charAt(k++);				
		}		
		key.close();
	}
	/**
	 * Trims extra spaces 
	 * @param s Input String
	 * @return Right trimmed string
	 */
	public static String right_trim(String s)
	{
		int i=s.length()-1;
		while(s.charAt(i--)==' ');
		String s1="";
		for(int j=0;j<=i+1;++j)
			s1+=s.charAt(j);
		return s1;
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
		String bits=new String(b);	
		bits=Encrypt.cipher_to_bits(bits);
		int ncols=Encrypt.num_cols(), nrows=bits.length()/ncols;		
		char mat[][]=new char[nrows][ncols];
		init_matrices(bits, nrows, mat);			
		String s=extract_chars(nrows, ncols, mat);			
		s=Encrypt.bits_to_ascii(s);			
		nrows=s.length()/ncols;		
		char mat1[][]=new char[nrows][ncols];
		init_matrices(s, nrows, mat1);
		String decrypted_text=extract_chars(nrows, ncols, mat1);
		FileOutputStream dos=new FileOutputStream("decrypted_file"+ext);	
		decrypted_text=right_trim(decrypted_text);
		dos.write(decrypted_text.getBytes("ISO-8859-1"));
		dos.close();				
	}
}
