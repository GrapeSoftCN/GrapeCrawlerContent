package Test;



import org.json.simple.JSONObject;

import common.java.security.codec;
import interfaceApplication.CrawlerContent;
import unit.TXTUtils;

public class Test {
	
	
	public static void main(String[] args) {
	//test2();
		test1();
	}

	public static void test2() {
		 JSONObject json = JSONObject.toJSON(Shuju.sj);
		 String encodeFastJSON = codec.encodeFastJSON(json.toJSONString());
		 System.out.println(encodeFastJSON);
		 TXTUtils.string2Txt("C:\\Shuju.txt", encodeFastJSON, "UTF-8");
	}
	public static void test1() {
		String s="http://mmbiz.qpic.cn/mmbiz_jpg/Bic8edlgthJGx9dGPFAAaOMAY8gC4duMZR221DmTiazybiaia2965rUGibw0OKJmcY1mBfsDYpZorWDqaq0ssU7o8kQ/0?wx_fmt=jpeg";
		if(s.endsWith("jpeg")) {
			System.out.println("1234");
			
		}
	}

}
