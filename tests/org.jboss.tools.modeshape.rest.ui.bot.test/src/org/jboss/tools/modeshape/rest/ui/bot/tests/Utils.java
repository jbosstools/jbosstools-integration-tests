package org.jboss.tools.modeshape.rest.ui.bot.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	   public static byte[] read(File file) throws IOException {
		   
	        InputStream is = new FileInputStream(file);
	        if (file.length() > Integer.MAX_VALUE) {
	            // File is too large
	        	throw new IOException("File " + file.toString() + " is too large!");
	        }
	        
	        byte[] bytes = read(is, (int) file.length());
	        
	        is.close();
	        return bytes;
	   }
	  
	   public static byte[] read(InputStream is, int len) throws IOException{
		   
		   byte[] bytes = new byte[len];
		   int bytesRead = 0;
		   int result = 0;
		   
		   while (bytesRead < len) {
				
			   result = is.read(bytes, bytesRead, len - bytesRead);
			   if (result == -1) break;
			   bytesRead += result;
		   }
		   
		   is.close();
		   return bytes;
	   }
}
