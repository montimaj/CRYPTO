package docrypto;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

import docrypto.utilities.GenKey;
import docrypto.utilities.Log;

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
		for(int i=0;i<nrows ;++i)		
			for(int j=0;j<ncols;++j)	
				if(flag[i][j])
					s+=mat[i][j];		
		return s;
	}
	private static void init_matrix(String s, int nrows, int ncols, int num[], char mat[][], boolean flag[][]) throws IOException
	{
		try
		{
			int k=0;		
			for(int i=0;i<ncols;++i)
				for(int j=0;j<nrows;++j)
					if(flag[j][num[i]])
						mat[j][num[i]]=s.charAt(k++);	
		}	
		catch(StringIndexOutOfBoundsException e)
		{
			throw new IOException("Invalid Secret Key or input files");
		}
	}
	private static void init_key_array(String keyfile, int num[]) throws IOException
	{
		DataInputStream key=new DataInputStream(new FileInputStream(keyfile));
		int i=0;
		boolean eof=false;
		while(!eof)
		{			
			try
			{
				num[i++]=key.readInt();
			}				
			catch(EOFException e)
			{
				eof=true;
				key.close();
			}
			catch(ArrayIndexOutOfBoundsException e1)
			{
				throw new IOException("Invalid Secret Key or input files");
			}
		}
	}		
	/**
	 * Performs decryption operation
	 * @param cipher Path to the cipher file
	 * @param ext Extension of the plain text file
	 * @throws IOException
	 */
	private static void decrypt(String skey,String cipher, String keyfile, String dir) throws IOException
	{		
		String bits=Encrypt.read_from_file(cipher);		
		bits=Encrypt.String_to_bits(bits);
		GenKey gk=new GenKey(skey);		
		int ncols=gk.get_colsize(),num[]=new int[ncols], len=bits.length(),nrows=len/ncols, ndecrypt=gk.get_encryption_number();
		init_key_array(keyfile, num);
		if(len>nrows*ncols)
			nrows++;		
		boolean flag[][]=new boolean[nrows][ncols];
		Encrypt.init_matrix(bits, nrows, ncols, flag);
		char mat[][]=new char[nrows][ncols];
		for(int i=0;i<ndecrypt;++i)
		{			
			init_matrix(bits, nrows, ncols, num, mat,flag);			
			bits=extract_chars(nrows, ncols, mat, flag);		
		}		
		String dt=Encrypt.bits_to_ascii(bits);		
		FileOutputStream dos=new FileOutputStream(dir+"/decrypted"+cipher.substring(cipher.lastIndexOf('_'), cipher.length()));
		int c;
		String ext="";
		if((c=cipher.lastIndexOf('.'))!=-1)
			ext=cipher.substring(c, cipher.length());
		if(ext.equalsIgnoreCase(".zip"))
			dos.write(dt.getBytes("ISO-8859-1"));
		else
			for(int i=0;i<dt.length();++i)
				dos.write(dt.charAt(i));
		dos.close();		
	}
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Process p1=null;
		try
		{			
			if(args.length<4 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty())
				throw new IOException("Invalid input");	
			else if(args[0].length()>16)
				throw new IOException("Secret key length must be between 1-16");			
			String[] x={"zenity","--progress","--pulsate","--no-cancel","--text=Decrypting..."};
			p1=new ProcessBuilder(x).start();
			long st=System.nanoTime();		
			decrypt(args[0], args[1], args[2], args[3]);
			long et=System.nanoTime();	
			p1.destroy();
			String time="Decryption time= "+UserInput.getExecutionTime(st,et), x1[]={"zenity","--info","--title=Result","--text="+time};
			p1=new ProcessBuilder(x1).start();
			p1.waitFor();						
		}		
		catch(IOException e)
		{
			if(p1!=null)
		    	p1.destroy();
			String s=Log.create_log(args[3],e), x[]={"zenity","--error","--text="+s};
		    p1=new ProcessBuilder(x).start(); 
		    p1.waitFor(); 
		}
	}
}
