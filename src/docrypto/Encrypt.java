package docrypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.security.SecureRandom;

import com.google.zxing.WriterException;

import docrypto.utilities.QRCode;
import docrypto.utilities.ZipCreator;

/**
 * Main Encryption module
 * @author Sayantan Majumdar 
 * @since 1.0
 */
public class Encrypt 
{
	private static final int ncols=new SecureRandom().nextInt(255)+1;		
	private static void init_matrices(String s, int nrows, char mat[][])
	{
		int k=0;		
		for(int i=0;i<nrows;++i)		
			for(int j=0;j<ncols;++j)				
				mat[i][j]=s.charAt(k++);				
	}
	private static String generate_key(String s, String dir, int key_arr[]) throws IOException
	{		
		SecureRandom srand=new SecureRandom();		
		DataOutputStream k=new DataOutputStream(new FileOutputStream(dir+"/key_"+s+".txt"));	
		boolean arr[]=new boolean[ncols];		
		for(int i=0;i<ncols;++i)
		{			
			int r=srand.nextInt(ncols);
			if(i==0)
			{
				key_arr[i]=r;
				k.writeInt(r);				
			}
			else if(i>0 && !arr[r])
			{
				key_arr[i]=r;
				k.writeInt(r);				
			}
			else if(arr[r])
				i--;
			arr[r]=true;
		}	
		k.close();	
		String msg="Randomly generated..."+"\ncolumns="+ncols+"\n\nkey= ";
		for(int i=0;i<ncols;++i)
			msg+=key_arr[i]+" ";
		msg+="\n";
		return msg;		
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
	private static String generate_cipher(int nrows, int key[], char mat[][])
	{
		String cipher_text="";		
		for(int i=0;i<ncols;++i)
			for(int j=0;j<nrows;++j)				
				cipher_text+=mat[j][key[i]];		
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
	public static String encrypt_file(String s, String dir) throws IOException, WriterException
	{		
		long st=System.nanoTime();
		FileInputStream fis=new FileInputStream(s);
		String ext=s.substring(s.lastIndexOf('.'),s.length());
		byte b[]=new byte[fis.available()];
		fis.read(b);
		fis.close();	
		String pt=new String(b, "ISO-8859-1")+ext;		
		int len=pt.length(),nrows=len/ncols;
		if(len>(nrows*ncols))
			nrows++;
		int empty_cells=nrows*ncols-len;
		if(empty_cells>0)
			for(int i=0;i<empty_cells;++i)
				pt+=" ";			
		char mat[][]=new char[nrows][ncols];		
		init_matrices(pt, nrows, mat);
		int k[]=new int[ncols];
		String s1=s.substring(s.lastIndexOf(File.separatorChar)+1,s.lastIndexOf('.'));
		String msg=generate_key(s1,dir,k);		
		String cipher=generate_cipher(nrows,k,mat);			
		String bits=cipher_to_bits(cipher);			
		nrows=bits.length()/ncols;		
		char mat1[][]=new char[nrows][ncols];		
		init_matrices(bits, nrows, mat1);
		cipher=generate_cipher(nrows,k,mat1);				
		cipher=bits_to_ascii(cipher);				
		FileOutputStream cos=new FileOutputStream(dir+"/cipher_"+s1+".txt");
		cos.write(cipher.getBytes());		
		cos.close();		
		long et=System.nanoTime();		
		msg+="\nEncryption Time= "+UserInput.getExecutionTime(st, et);		
		st=System.nanoTime();
		String files[]={dir+"/key_"+s1+".txt", dir+"/cipher_"+s1+".txt"};
		String z=dir+"/zipped_"+s1+".zip";
		ZipCreator.create_zip(z, files);
		QRCode.gen_qrcode(z, dir, s1);
		et=System.nanoTime();
		msg+="\nQRCode generation time= "+UserInput.getExecutionTime(st, et);
		return msg;
	}
}
