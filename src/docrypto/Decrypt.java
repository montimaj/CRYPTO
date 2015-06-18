package docrypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
public class Decrypt 
{
	private static String extract_chars(int nrows, int ncols, char mat[][], boolean flag[][])
	{
		String s="";
		for(int i=0;i<ncols;++i)
		{
			for(int j=0;j<nrows;++j)
				if(flag[j][i])
					s+=mat[j][i];
		}
		return s;
	}
	private static void init_matrices(String s, String key, int nrows, char mat[][], boolean flag[][])
	{
		int k=0;		
		for(int i=0;i<key.length();++i)
		{
			int pos=(int)key.charAt(i)-48;
			for(int j=0;j<nrows;++j)
			{				
				flag[j][pos]=false;
				if(k<s.length() && pos<7)
				{					
					mat[j][pos]=s.charAt(k++);
					flag[j][pos]=true;
				}				
			}
		}		
	}
	private static String pad_zeros(String bits)
	{
		int l=bits.length();
		if(l<7)
		{
			int num_zeros=7-l;
			String z=""; 
			for(int i=0;i<num_zeros;++i)
				z+="0";
			bits=z+bits;
		}
		return bits;
	}
	public static void decrypt(String cipher)
	{
		try
		{
			FileInputStream cis=new FileInputStream(cipher);
			byte[] b=new byte[cis.available()];
			cis.read(b);
			cis.close();
			FileInputStream kis=new FileInputStream("key.txt");
			byte[] b1=new byte[kis.available()];
			kis.read(b1);
			kis.close();
			String key=new String(b1);		
			String bits=Encrypt.bitstream;//Encrypt.cipher_to_bits(new String(Base64.getDecoder().decode(b)));	
			//bits=pad_zeros(bits);
			System.out.println("bits= "+bits);
			int ncols=Encrypt.num_cols(), nrows=bits.length()/ncols;
			if(bits.length()>nrows*ncols)
				nrows++;
			char mat[][]=new char[nrows][ncols];
			boolean flag[][]=new boolean[nrows][ncols];
			init_matrices(bits, key, nrows, mat, flag);			
			String s=extract_chars(nrows, ncols, mat,flag);			
			s=Encrypt.bits_to_ascii(s);
			System.out.println(s);
			nrows=s.length()/ncols;
			if(s.length()>nrows*ncols)
				nrows++;
			char mat1[][]=new char[nrows][ncols];
			boolean flag1[][]=new boolean[nrows][ncols];
			init_matrices(s, key, nrows, mat1, flag1);
			String decrypted_text=extract_chars(nrows, ncols, mat1, flag1);
			FileOutputStream dos=new FileOutputStream("decrypted_msg.txt");			
			dos.write(decrypted_text.getBytes());
			dos.close();
			System.out.println(decrypted_text);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
