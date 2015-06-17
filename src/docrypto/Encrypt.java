package docrypto;

import java.io.FileOutputStream;
import java.util.Random;
public class Encrypt 
{
	private static final int ncols=8;
	private static void col_transpose(String s, int nrows, char mat[][], boolean flag[][])
	{
		int k=0;		
		System.out.println(s);
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
	public static void encrypt_file(String s)
	{
		try
		{
			int nrows=s.length()/8;
			char mat[][]=new char[nrows][ncols];
			boolean flag[][]=new boolean[nrows][ncols];
			col_transpose(s, nrows, mat, flag);
			disp_mat(mat, flag, nrows);
			String key=generate_key(ncols);	
			FileOutputStream keyfos=new FileOutputStream("key.txt");
			keyfos.write(key.getBytes());
			keyfos.close();
			StringBuilder cypher_text=new StringBuilder();
			for(int i=0;i<key.length();++i)
			{
				int pos=(int)key.charAt(i)-48;
				for(int j=0;j<nrows;++j)
				{					
					if(flag[j][pos])
						cypher_text.append(mat[j][pos]);
				}
			}
			FileOutputStream cos=new FileOutputStream("cypher_text.txt");
			cos.write(cypher_text.toString().getBytes());		
			cos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}
