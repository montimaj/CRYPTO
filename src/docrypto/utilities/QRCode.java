package docrypto.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/** 
* Generates the QRCode image
* @since 1.0
* @see <a href="http://zxing.github.io/zxing/apidocs/">Zxing</a>
*/

public class QRCode 
{
	/**
	  * @param input Input argument where 
	  * <p>
	  * input is the path to Result.zip and	    
	  */
	public static void gen_qrcode(String input, String dir, String name) throws IOException, WriterException
	{		
		String file = dir+"/QRCode_"+name+".png", data = input;		
		data=read_from_file(data);  //if this exceeds ~4.2KB for Low error correction level then Exception will occur    
		Map<EncodeHintType, ErrorCorrectionLevel> hint_map1 = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hint_map1.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); //hints used by QRCodeWriter.encode for efficient generation of the QRCode image
		createQRCode(data, file,hint_map1,500,500);			
	}
	/**
	  * Reads bytes from input file
	  * and encodes in {@link java.util.Base64} BarcodeFormat
	  * @param s Input file path
	  * @return ISO-8859-1 encoded String
	  * @throws IOException
	  */
	public static String read_from_file(String s) throws IOException
	{
	    FileInputStream fp=new FileInputStream(s);    
	    byte[] data=new byte[fp.available()];
	    fp.read(data);
	    s = new String(data, "ISO-8859-1");
	    fp.close();
	    return s;
	}
	
	/**
	  * Generates QRCode.png
	  * @param data String to be encoded in QRCode
	  * @param file Output file path
	  * @param hint_map hints used by {@link QRCodeWriter} for efficiency
	  * @param qrh Height of QRCode.png
	  * @param qrw Width of QRCode.png
	  * @throws WriterException generally if input data is too big
	  * @throws IOException
	  */
	public static void createQRCode(String data, String file, Map<EncodeHintType, ErrorCorrectionLevel> hint_map, int qrh, int qrw) throws WriterException, IOException
	{		
	    BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, qrw, qrh, hint_map); 
	    MatrixToImageWriter.writeToFile(matrix, "png",new File(file));
	}	
}