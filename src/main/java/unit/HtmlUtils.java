package unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import Test.Shuju;
import common.java.nlogger.nlogger;
import common.java.security.codec;
import common.java.string.StringHelper;

public class HtmlUtils {
	// public static List<String> getLink(final String html) {
	// String regex;
	// final List<String> list = new ArrayList<String>();
	// regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
	// final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
	// final Matcher ma = pa.matcher(html);
	// while (ma.find()) {
	// list.add(ma.group());
	// }
	// return list;
	// }
	//
	// public static void main(String[] args) {
	// String html_ExcludeHref = getHtml_ExcludeHref(Shuju.sj);
	// System.out.println(html_ExcludeHref);
	//
	// // html2Sting("D:\\StartPageForaa.html");
	//
	// // List<String> link = HtmlUtils.getLink("<tbody> \r\n" +
	// // " <tr> \r\n" +
	// // " <td> <a class=\"ke-insertfile\"
	// //
	// href=\"http://img.ahjjjc.gov.cn/uploadfiles/340700/file/20171227085946_29584.doc\"
	// // target=\"_blank\"> 三轮巡察进驻信息表（汇总） </a> &nbsp; </td> \r\n" +
	// // " </tr> \r\n" +
	// // "</tbody>");
	// // getHref(link);
	//
	// }
	//
	// private static List<String> getHref(List<String> links) {
	// List<String> href_list = new ArrayList<String>();
	// for (String link : links) {
	//
	// String[] split1 = link.split("href@m\\\\\\\"");
	// String[] split2 = split1[1].split("\\\\\"");
	// //String[] split2 = split1[1].split("\"");
	// href_list.add(split2[0]);
	//
	// }
	// return href_list;
	//
	// }
	//
	// public static List<String> getLink_then_getHref(final String html) {
	// List<String> link = getLink(html);
	// for (int i = 0; i < link.size(); i++) {
	// String a1 = codec.EncodeHtmlTag(link.get(i));
	// link.set(i, a1);
	//
	// }
	// List<String> href_list = getHref(link);
	// for (int i = 0; i < href_list.size(); i++) {
	// String a1 = codec.DecodeHtmlTag(href_list.get(i));
	// href_list.set(i, a1);
	//
	// }
	// return href_list;
	//
	// }
	// // public static String html2Str(String html) {
	// // try {
	// // html = nvl(html);
	// // Parser parser = Parser.createParser(html, "utf-8");
	// // TextExtractingVisitor visitor = new TextExtractingVisitor();
	// // parser.visitAllNodesWith(visitor);
	// // return visitor.getExtractedText();
	// // } catch (Exception ex) {
	// // return null;
	// // }
	// // }
	//
	// public static String html2Sting(String html_path_name) {
	// Document parse = null;
	// String html = null;
	// try {
	// String codeString = FileUtils.get_File_charset(html_path_name);
	// parse = Jsoup.parse(new File(html_path_name), codeString);
	// html = parse.html();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// nlogger.logout(e);
	// }
	//
	// return html;
	//
	// }
	//
	// public static void html2Txt(String html_path_name, String txt_path_name,
	// String txt_charset) {
	// String string = html2Sting(html_path_name);
	// TXTUtils.string2Txt(txt_path_name, string, txt_charset);
	//
	// }
	//
	// public static String getHtml_ExcludeHref(String html) {
	// String encodeHtml = codec.EncodeHtmlTag(html);
	// List<String> link_then_getHref = getLink_then_getHref(html);
	// for (int i = 0; i < link_then_getHref.size(); i++) {
	// String a1 = codec.EncodeHtmlTag(link_then_getHref.get(i));
	// link_then_getHref.set(i, a1);
	//
	// }
	// for (int i = 0; i < link_then_getHref.size(); i++) {
	// String url = link_then_getHref.get(i);
	// encodeHtml = encodeHtml.replace(url, "");
	// }
	// String decodeHtml = codec.DecodeHtmlTag(html);
	// return decodeHtml;
	//
	// }

	public static List<String> getLink(final String html) {
		String regex;
		final List<String> list = new ArrayList<String>();
		regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(html);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	public static void main(String[] args) {
		String html_ExcludeHref = getHtml_Exclude_href(Shuju.sj);

		// html2Sting("D:\\StartPageForaa.html");

		// List<String> link = HtmlUtils.getLink("<tbody> \r\n" +
		// " <tr> \r\n" +
		// " <td> <a class=\"ke-insertfile\"
		// href=\"http://img.ahjjjc.gov.cn/uploadfiles/340700/file/20171227085946_29584.doc\"
		// target=\"_blank\"> 三轮巡察进驻信息表（汇总） </a> &nbsp; </td> \r\n" +
		// " </tr> \r\n" +
		// "</tbody>");
		// getHref(link);

	}

	public static String chuli_xiangDui_href(String content, String curhref) {
		ArrayList<String> yijing_replace = new ArrayList<String>();
		if (content.contains(" src=\"")) {
			String[] splits = content.split(" src=\\\"");
			for (int i = 1; i < splits.length; i++) {
				int indexOf = splits[i].indexOf("\"");
				String url = splits[i].substring(0, indexOf);
				if (!yijing_replace.contains(url)) {
					String filterURL = urlContent.filterURL(curhref, url);
					content = content.replace(url, filterURL);
					yijing_replace.add(url);
				}
			}

		}
		if (content.contains(" href=\"")) {
			String[] splits = content.split(" href=\\\"");
			for (int i = 1; i < splits.length; i++) {
				int indexOf = splits[i].indexOf("\"");
				String url = splits[i].substring(0, indexOf);
				if (!yijing_replace.contains(url)) {
					String filterURL = urlContent.filterURL(curhref, url);
					content = content.replace(url, filterURL);
					yijing_replace.add(url);
				}
			}

		}

		return content;
	}

	private static List<String> getHref(List<String> links) {
		List<String> href_list = new ArrayList<String>();
		for (String link : links) {

			String[] split1 = link.split("href=\\\"");
			String string = split1[1];
			String[] split2 = string.split("\\\"");
			// String[] split2 = split1[1].split("\"");
			String html_zhuanyi = html_zhuanyi(split2[0]);
			href_list.add(html_zhuanyi);

		}
		return href_list;

	}

	public static String html_zhuanyi(String content) {
		if (content == null)
			return "";
		String html = content;

		// html = html.replace( "'", "&apos;");
		html = html.replaceAll("&amp;", "&");
		// html = html.replace("\"", "&quot;"); // "
		// html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
		// html = html.replace(" ", "&nbsp;");// 替换空格
		// html = html.replace("<", "&lt;");
		//
		// html = html.replaceAll(">", "&gt;");

		return html;
	}

	public static List<String> getLink_then_getHref(final String html) {
		List<String> link = getLink(html);
		List<String> href_list = getHref(link);
		return href_list;

	}
	// public static String html2Str(String html) {
	// try {
	// html = nvl(html);
	// Parser parser = Parser.createParser(html, "utf-8");
	// TextExtractingVisitor visitor = new TextExtractingVisitor();
	// parser.visitAllNodesWith(visitor);
	// return visitor.getExtractedText();
	// } catch (Exception ex) {
	// return null;
	// }
	// }

	public static String html2Sting(String html_path_name) {
		Document parse = null;
		String html = null;
		try {
			String codeString = FileUtils.get_File_charset(html_path_name);
			parse = Jsoup.parse(new File(html_path_name), codeString);
			html = parse.html();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			nlogger.logout(e);
		}

		return html;

	}

	public static void html2Txt(String html_path_name, String txt_path_name, String txt_charset) {
		String string = html2Sting(html_path_name);
		TXTUtils.string2Txt(txt_path_name, string, txt_charset);

	}

	public static String getHtml_Exclude_href_src(String html) {
		String s1 = getHtml_Exclude_href(html);
		String s2 = getHtml_Exclude_src(s1);
		return s2;

	}

	public static String getHtml_Exclude_href(String html) {
		List<String> list = getLink_then_getHref(html);
		for (String url : list) {
			while (html.contains(url)) {
				html = html.replace(url, "");
			}
		}
		return html;

	}

	public static String getHtml_Exclude_src(String html) {
		List<String> list = getsrc(html);
		for (String url : list) {
			while (html.contains(url)) {
				html = html.replace(url, "");
			}
		}
		return html;

	}

	public static List<String> getsrc(final String html) {
		ArrayList<String> arrayList = new ArrayList<String>();
		if (html.contains("src=\"")) {
			String[] splits = html.split("src=\\\"");

			for (int i = 1; i < splits.length; i++) {
				int indexOf = splits[i].indexOf("\"");
				String url = splits[i].substring(0, indexOf);

				arrayList.add(url);
			}

		}

		return arrayList;
	}
}
