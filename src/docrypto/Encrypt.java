package docrypto;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Main Encryption module
 * @author Sayantan Majumdar 
 * @since 1.0
 */
public class Encrypt 
{
	private static final int ncols=new SecureRandom().nextInt(255)+1;	
	public static boolean flag[][], flag1[][];
	
	/**
	 * Method returns number of columns
	 * @return number of columns 
	 */
	public static int num_cols()
	{
		return ncols;
	}	
	private static void init_matrices(String s, int nrows, char mat[][], boolean flag[][])
	{
		int k=0;		
		for(int i=0;i<nrows;++i)
		{
			for(int j=0;j<ncols;++j)
			{				
				flag[i][j]=false;
				if(k<s.length())
				{
					mat[i][j]=s.charAt(k++);
					flag[i][j]=true;
				}				
			}
		}		
	}
	private static String generate_key() throws IOException
	{		
		SecureRandom srand=new SecureRandom();		
		DataOutputStream k=new DataOutputStream(new FileOutputStream("key.txt"));		
		boolean arr[]=new boolean[ncols];		
		String key="";
		for(int i=0;i<ncols;++i)
		{			
			int r=srand.nextInt(ncols);
			if(i==0)
			{
				k.writeInt(r);
				key+=r+" ";
			}
			else if(i>0 && !arr[r])
			{
				k.writeInt(r);
				key+=r+" ";
			}
			else if(arr[r])
				i--;
			arr[r]=true;
		}	
		k.close();
		return key;
	}	
	/**
	 * Converts ASCII to 8-bit binary string
	 * @param cipher String containing the cipher text
	 * @return Binary string
	 */
	public static String cipher_to_bits(String cipher)
	{
		String bits="";
		for(int i=0;i<cipher.length();++i)
		{
			int mask=1<<7, n=cipher.charAt(i);
			while(mask!=0)
			{
				bits+=(n&mask)!=0?1:0;
				mask>>=1;
			}
		}		
		return bits;		
	}
	private static String generate_cipher(int nrows, boolean flag[][], char mat[][]) throws IOException
	{
		String cipher_text="";
		DataInputStream key=new DataInputStream(new FileInputStream("key.txt"));
		for(int i=0;i<ncols;++i)
		{
			
			int pos=key.readInt();
			for(int j=0;j<nrows;++j)
				if(flag[j][pos])
					cipher_text+=mat[j][pos];		
		}
		key.close();
		return cipher_text;
	}
	/**
	 * Converts bits to ASCII
	 * @param bits Input bits in String format
	 * @return ASCII string
	 */
	public static String bits_to_ascii(String bits)
	{			
		String ascii="";
		for(int i=0;i<bits.length();)
		{
			int asc=0,j,power=7;
			for(j=i;j<i+8 && j<bits.length();++j,--power)
			{
				int c=bits.charAt(j)-48;
				asc+=c*(int)Math.pow(2, power);
			}
			i=j;
			ascii+=(char)asc;
		}
		return ascii;
	}
	/**
	 * Performs the encryption operation
	 * @param s String containing the plain text
	 * @throws IOException
	 */
	public static void encrypt_file(String s) throws IOException
	{
		System.out.println("Number of columns= "+ncols);
		int nrows=s.length()/ncols;
		if(s.length()>(nrows*ncols))
			nrows++;
		char mat[][]=new char[nrows][ncols];
		flag=new boolean[nrows][ncols];
		init_matrices(s, nrows, mat, flag);
		String key=generate_key();
		System.out.println("Randomly generated key= "+key);
		String cipher=generate_cipher(nrows,flag,mat);			
		String bits=cipher_to_bits(cipher);			
		nrows=bits.length()/ncols;
		if(bits.length()>(nrows*ncols))
			nrows++;
		char mat1[][]=new char[nrows][ncols];
		flag1=new boolean[nrows][ncols];
		init_matrices(bits, nrows, mat1, flag1);
		cipher=generate_cipher(nrows,flag1,mat1);				
		cipher=bits_to_ascii(cipher);		
		String bcipher=Base64.getEncoder().encodeToString(cipher.getBytes());			
		FileOutputStream cos=new FileOutputStream("cipher_text.txt");
		cos.write(bcipher.getBytes());		
		cos.close();				
	}
}
