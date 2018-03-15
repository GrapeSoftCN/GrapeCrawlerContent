package unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ss {
	  public static String timeStamp2Date(String seconds,String format) {  
	        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
	            return "";  
	        }  
	        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";  
	        SimpleDateFormat sdf = new SimpleDateFormat(format);  
	        return sdf.format(new Date(Long.valueOf(seconds)));  
	    }  
	  public static void main(String[] args) {
		String timeStamp2Date = timeStamp2Date("1517451240000", null);
		System.out.println(timeStamp2Date);
	}
}
