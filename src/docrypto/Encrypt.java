package docrypto;

import java.io.FileOutputStream;
import java.util.Random;
public class Encrypt 
{
	private static int ncols=8;
	private static void col_transpose(String s, int nrows, char mat[][], boolean flag[][])
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
	private static String generate_key(int ncols)
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
	private static void disp_mat(char mat[][], boolean flag[][], int nrows)
	{
		for(int i=0;i<nrows;++i)
		{
			for(int j=0;j<ncols;++j)
				System.out.print(mat[i][j]+" ");
			System.out.println();
		}
		System.out.println("\n");

		for(int i=0;i<nrows;++i)
		{
			for(int j=0;j<ncols;++j)
				System.out.print(flag[i][j]+" ");
			System.out.println();
		}
	}
	private static String cipher_to_bits(String cipher)
	{
		String bits="";
		for(int i=0;i<cipher.length();++i)
			bits+=Integer.toBinaryString((int)cipher.charAt(i));			
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
	private static String bits_to_ascii(String bits)
	{			
		String ascii="";
		for(int i=0;i<bits.length();)
		{
			int asc=0,j,power=6;
			for(j=i;j<i+7 && j<bits.length();++j,--power)
			{
				int c=(int)bits.charAt(j)-48;
				asc+=c*Math.pow(2, power);
			}
			i=j;
			ascii+=(char)asc+" ";
		}
		return ascii;
	}
	public static void encrypt_file(String s)
	{
		try
		{
			int nrows=s.length()/8;
			if(s.length()>(nrows*ncols))
				nrows++;
			char mat[][]=new char[nrows][ncols];
			boolean flag[][]=new boolean[nrows][ncols];
			col_transpose(s, nrows, mat, flag);
			String key=generate_key(ncols);	
			FileOutputStream keyfos=new FileOutputStream("key.txt");
			keyfos.write(key.getBytes());
			keyfos.close();
			String cipher=generate_cipher(key,nrows,flag,mat);
			FileOutputStream cos=new FileOutputStream("cipher_text1.txt");
			cos.write(cipher.getBytes());		
			cos.close();
			String bits=cipher_to_bits(cipher);
			FileOutputStream bitstream=new FileOutputStream("cipher_bits.txt");			
			bitstream.write(bits.getBytes());
			bitstream.close();				
			nrows=bits.length()/ncols;
			if(bits.length()>(nrows*ncols))
				nrows++;
			char mat1[][]=new char[nrows][ncols];
			boolean flag1[][]=new boolean[nrows][ncols];
			col_transpose(bits, nrows, mat1, flag1);
			cipher=generate_cipher(key,nrows,flag1,mat1);
			cipher=bits_to_ascii(cipher);
			cos=new FileOutputStream("cipher_text2.txt");
			cos.write(cipher.getBytes());		
			cos.close();	
			disp_mat(mat1,flag1,nrows);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}
