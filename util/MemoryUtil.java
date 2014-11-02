package util;

import java.text.DecimalFormat;

/**
 * Another class for handling common things. In this case, it contains
 * a method to show a memory size in a human-readable format
 * @author Taken from the net
 *
 */

public class MemoryUtil {
	/**
	 * 
	 * @param long size the memory size in bytes
	 * @return String a human-readable representation of the input
	 */
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
