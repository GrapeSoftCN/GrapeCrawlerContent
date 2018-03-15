package unit;

import common.java.nlogger.nlogger;

public class Print_nlogger {
	//1  开启打印
	//0 关闭打印
	private static int logout=1;
	private static int logoutException=0;
	public static void Print_SYSO(Object in) {
		if(logout==1) {
			System.out.println(in);
			
		}
	}
//	public static void Print(Object in) {
//		if(logout==1) {
//			System.out.print(in);
//			
//		}
//	}
	
	
	
	

}
