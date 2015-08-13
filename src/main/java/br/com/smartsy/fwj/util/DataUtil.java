package br.com.smartsy.fwj.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;

/**
 * Utility for data information
 * @author Vagner
 *
 */
public class DataUtil {

	/**
	 * 
	 * @param handler
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertDataHandler(DataHandler handler) throws IOException {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    handler.writeTo(output);
	    return output.toByteArray();
	}
	
}
