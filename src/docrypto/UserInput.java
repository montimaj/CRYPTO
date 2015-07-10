package docrypto;

import java.io.IOException;

import docrypto.utilities.Log;

/**
 * Takes plain text file path as input and uses 
 * <p>
 * {@link docrypto.Encrypt} to generate key.txt and cipher_text.txt 
 * @author Sayantan Majumdar
 * @since 1.0
 */
public class UserInput 
{

	/**
	 * Get execution time in millisecond or second
	 * @param st Start time
	 * @param et End Time
	 * @return Execution Time as a String
	 */
	public static String getExecutionTime(long st, long et)
	{
		double time=(et-st)/1e+6;
		String t=time+" ms";			
		if(time>=1000.)
		{
			time/=1000.;
			t=time+" s"; 
		}					
		return t;
	}
	/**
	 * Main module 
	 * @param args Input file path
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Process p1=null;
		try
		{			
			if(args.length<3 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty())
				throw new IOException("Invalid Input");
			else if(args[0].length()>16 )
				throw new IOException("Secret key length must be between 1-16");			
			String[] x={"zenity","--progress","--pulsate","--no-cancel","--text=Encrypting..."};
			p1=new ProcessBuilder(x).start();			
			long st=System.nanoTime();		
			String msg=Encrypt.encrypt_file(args[0],args[1],args[2]);
			long et=System.nanoTime();
			p1.destroy();
			msg+="\nTotal time= "+getExecutionTime(st,et);
			String x1[]={"zenity","--info","--title=Result","--text="+msg};
			p1=new ProcessBuilder(x1).start();
			p1.waitFor();			
		}
		catch(IOException e)
		{
			if(p1!=null)
		    	p1.destroy();
		    String s=Log.create_log(args[2],e), x[]={"zenity","--error","--text="+s};
		    p1=new ProcessBuilder(x).start(); 
		    p1.waitFor();     
		}		
	}
}
