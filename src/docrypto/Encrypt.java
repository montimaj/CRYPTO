package docrypto;

import java.io.FileOutputStream;
import java.util.Random;
import java.util.Base64;
public class Encrypt 
{
	private static int ncols=8;	
	public static boolean flag[][], flag1[][];
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
	private static String generate_key()
	{
		Random random=new Random();	
		StringBuilder k=new StringBuilder();
		boolean arr[]=new boolean[ncols];	
		for(int i=0;i<ncols;++i)
		{			
			int r=random.nextInt(ncols);
			if(i==0)
				k.append(r);
			else if(i>0 && !arr[r])
				k.append(r);
			else if(arr[r])
				i--;
			arr[r]=true;
		}
		return k.toString();
	}	
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
	private static String generate_cipher(String key, int nrows, boolean flag[][], char mat[][])
	{
		StringBuilder cipher_text=new StringBuilder();
		for(int i=0;i<key.length();++i)
		{
			int pos=(int)key.charAt(i)-48;
			for(int j=0;j<nrows;++j)
			{					
				if(flag[j][pos])
					cipher_text.append(mat[j][pos]);
			}
		}
		return cipher_text.toString();
	}
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
	public static void encrypt_file(String s)
	{
		try
		{
			System.out.println("Plain text=\n"+s);
			int nrows=s.length()/8;
			if(s.length()>(nrows*ncols))
				nrows++;
			char mat[][]=new char[nrows][ncols];
			flag=new boolean[nrows][ncols];
			init_matrices(s, nrows, mat, flag);
			String key=generate_key();	
			System.out.println("Randomly generated key= "+key);
			FileOutputStream keyfos=new FileOutputStream("key.txt");
			keyfos.write(key.getBytes());
			keyfos.close();
			String cipher=generate_cipher(key,nrows,flag,mat);			
			String bits=cipher_to_bits(cipher);			
			nrows=bits.length()/ncols;
			if(bits.length()>(nrows*ncols))
				nrows++;
			char mat1[][]=new char[nrows][ncols];
			flag1=new boolean[nrows][ncols];
			init_matrices(bits, nrows, mat1, flag1);
			cipher=generate_cipher(key,nrows,flag1,mat1);				
			cipher=bits_to_ascii(cipher);		
			String bcipher=Base64.getEncoder().encodeToString(cipher.getBytes());		
			System.out.println("Base64 encoded Cipher="+bcipher);
			FileOutputStream cos=new FileOutputStream("cipher_text.txt");
			cos.write(bcipher.getBytes());		
			cos.close();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}
