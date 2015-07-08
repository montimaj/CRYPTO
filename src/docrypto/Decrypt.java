package docrypto;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

import docrypto.utilities.Log;

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
		for(int i=0;i<nrows ;++i)		
			for(int j=0;j<ncols;++j)				
				s+=mat[i][j];			
		return s;
	}
	private static void init_matrices(String s, int nrows, int ncols, int num[], char mat[][], String keyfile)
	{
		int k=0;		
		for(int i=0;i<ncols;++i)
			for(int j=0;j<nrows;++j)				
				mat[j][num[i]]=s.charAt(k++);		
	}	
	private static int get_numcols(String keyfile, int num[]) throws IOException
	{
		DataInputStream key=new DataInputStream(new FileInputStream(keyfile));
		int i=0, max=-1;
		boolean eof=false;
		while(!eof)
		{			
			try
			{
				num[i]=key.readInt();
				if(i==0)
					max=num[i];
				else
					if(max<num[i])
						max=num[i];
				++i;
					
			}
			catch(EOFException e)
			{
				eof=true;
				key.close();
			}
		}		
		return max+1;		
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
	private static void decrypt(String cipher, String keyfile, String dir) throws IOException
	{		
		String bits=Encrypt.read_from_file(cipher);		
		bits=Encrypt.String_to_bits(bits);		
		int num[]=new int[256],ncols=get_numcols(keyfile, num), nrows=bits.length()/ncols;			
		char mat[][]=new char[nrows][ncols];
		init_matrices(bits, nrows, ncols, num, mat, keyfile);			
		String s=extract_chars(nrows, ncols, mat);		
		char mat1[][]=new char[nrows][ncols];
		init_matrices(s, nrows, ncols, num, mat1, keyfile);
		String dt=extract_chars(nrows, ncols, mat1);		
		dt=right_trim(Encrypt.bits_to_ascii(dt));		
		FileOutputStream dos=new FileOutputStream(dir+"/decrypted"+cipher.substring(cipher.lastIndexOf('_'), cipher.length()));
		String ext=cipher.substring(cipher.lastIndexOf('.'), cipher.length());
		if(ext.equalsIgnoreCase(".zip"))
			dos.write(dt.getBytes("ISO-8859-1"));
		else
			for(int i=0;i<dt.length();++i)
				dos.write(dt.charAt(i));
		dos.close();				
	}
	public static void main(String[] args) throws Exception
	{
		Process p1=null;
		try
		{
			if(args.length<3 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty())
				throw new IOException("Invalid input");			
			String[] x={"zenity","--progress","--pulsate","--no-cancel","--text=Decrypting..."};
			p1=new ProcessBuilder(x).start();
			long st=System.nanoTime();		
			decrypt(args[0], args[1], args[2]);
			long et=System.nanoTime();	
			p1.destroy();
			String time="Decryption time= "+UserInput.getExecutionTime(st,et), x1[]={"zenity","--info","--title=Result","--text="+time};
			p1=new ProcessBuilder(x1).start();
			p1.waitFor();						
		}
		catch(Exception e)
		{
			if(p1!=null)
		    	p1.destroy();
		    String s=Log.create_log(args[2],e), x[]={"zenity","--error","--text="+s};
		    e.printStackTrace();
		    p1=new ProcessBuilder(x).start(); 
		    p1.waitFor(); 
		}
	}
}
