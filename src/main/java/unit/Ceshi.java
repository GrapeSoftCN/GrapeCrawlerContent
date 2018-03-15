package unit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;

import common.java.security.codec;

public class Ceshi {
	private static boolean ifCeiShi = false;

	public static void ceshi_write_1(String infos) {
		if (ifCeiShi) {
			BufferedWriter pw = null;
			try {

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("c:\\ceshi_write_1.txt")), "UTF-8");
				pw = new BufferedWriter(osw);
				pw.write(infos);

				// 刷新流
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				if (pw != null) {
					try {
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public static void ceshi_write_2(String infos) {
		if (ifCeiShi) {
			BufferedWriter pw = null;
			try {

				// 写入数据

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("c:\\ceshi_write_2.txt")), "UTF-8");
				pw = new BufferedWriter(osw);
				pw.write(infos);

				// 刷新流
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				if (pw != null) {
					try {
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public static void ceshi_write_3(String infos) {
		if (ifCeiShi) {
			BufferedWriter pw = null;
			try {

				// 写入数据

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("c:\\ceshi_write_3.txt")), "UTF-8");
				pw = new BufferedWriter(osw);
				pw.write(infos);

				// 刷新流
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				if (pw != null) {
					try {
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void ceshi_write_N(String infos,int n) {
		if (ifCeiShi) {
			BufferedWriter pw = null;
			try {

				// 写入数据

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("c:\\ceshi_write_"+n+".txt")), "UTF-8");
				pw = new BufferedWriter(osw);
				pw.write(infos);

				// 刷新流
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				if (pw != null) {
					try {
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void ceshiZhuiJia1(String infos) {
		if (ifCeiShi) {
			BufferedWriter pw = null;
			try {

				// 写入数据

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("c:\\ZhuiJia1.txt")),
						"UTF-8");
				pw = new BufferedWriter(osw);
				pw.append(infos);

				// 刷新流
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭流
				if (pw != null) {
					try {
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public static void ceshiZhuiJia2(String infos) {
		if (ifCeiShi) {
			FileWriter writer = null;  
	        try {     
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
	            writer = new FileWriter("c:\\ZhuiJia.txt", true);     
	            writer.write(infos);       
	        } catch (IOException e) {     
	            e.printStackTrace();     
	        } finally {     
	            try {     
	                if(writer != null){  
	                    writer.close();     
	                }  
	            } catch (IOException e) {     
	                e.printStackTrace();     
	            }     
	        }   

		}
	}
	  /**   
     * 追加文件：使用FileWriter   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void method2(String fileName, String content) {   
        
    }     

}
