package unit;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import common.java.nlogger.nlogger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class TXTUtils {
	public static void main(String args[]) {
		String pathname = "C:\\aaa.txt";
		try {
			String code = FileUtils.get_File_charset(pathname);
			String readTxt = Txt2String(pathname);
			string2Txt("C:\\bbb.txt", readTxt, "UTF-8");
			// Print_nlogger.Print_SYSO(readTxt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static String readtxt(String pathname,String code) {
	// /* 读入TXT文件 */
	// StringBuffer stringBuffer = new StringBuffer();
	// try {
	// File filename = new File(pathname); // 要读取以上路径的input。txt文件
	// InputStreamReader reader = new InputStreamReader(new
	// FileInputStream(filename),code); // 建立一个输入流对象reader
	// BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
	// String line = "";
	// line = br.readLine();
	// while (line != null) {
	// line = br.readLine(); // 一次读入一行数据
	// stringBuffer.append(line);
	// }
	// return stringBuffer.toString();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// nlogger.logout(e);
	// }
	// return stringBuffer.toString();
	//
	//
	// }

	public static void string2Txt(String pathname, String content, String charset) {
		/* 写入Txt文件 */
		OutputStreamWriter out = null;
		try {
			File writename = new File(pathname); // 相对路径，如果没有则要建立一个新的output。txt文件
			writename.createNewFile(); // 创建新文件
			out = new OutputStreamWriter(new FileOutputStream(writename), charset);
			// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(writename));
			out.write(content); // \r\n即为换行
			out.flush(); // 把缓存区内容压入文件

		} catch (IOException e) {
			nlogger.logout(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // 最后记得关闭文件
		}

	}

	public static String Txt2String(String filePath) {
		StringBuffer stringBuffer = new StringBuffer();
		String code = FileUtils.get_File_charset(filePath);
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;

		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis, code);
				br = new BufferedReader(isr);
				String lineTxt = "";

				while ((lineTxt = br.readLine()) != null) {

					stringBuffer.append(lineTxt).append("\r\n");

				}

			} else {
				// Print_nlogger.Print_SYSO("文件不存在!");
			}
		} catch (Exception e) {
			// Print_nlogger.Print_SYSO("文件读取错误!");
			nlogger.logout(e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return stringBuffer.toString();

	}

}
