package interfaceApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.apache.bcel.generic.RETURN;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import com.alibaba.druid.util.StringUtils;

import Test.Shuju;
import common.java.JGrapeSystem.rMsg;
import common.java.apps.appIns;
import common.java.apps.appsProxy;
import common.java.check.checkHelper;
import common.java.httpClient.request;
import common.java.httpServer.grapeHttpUnit;
import common.java.nlogger.nlogger;
import common.java.rpc.execRequest;
import common.java.security.codec;
import common.java.string.StringHelper;
import common.java.time.TimeHelper;
import unit.Ceshi;
import unit.FileTypeHelper;
import unit.HtmlUtils;
import unit.Print_nlogger;
import unit.urlContent;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 * 爬虫回调函数
 * 
 *
 */
public class CrawlerContent {
	private String wbName;
	private String ogName;

	public void testSetInfo1(String jsonObject) throws IOException {
		// teshu_lanmu_chuli_wailian(new JSONObject(),
		// "https://mp.weixin.qq.com/s/FPO79FOW8852AvdAH-R2NQ");
		// String jsonObject1 = Ceshi.ceshiDUQU2("c:\\c3.txt");
		// String encodeFastJSON = codec.DecodeFastJSON(jsonObject1);
		// byte[] uriFile = request
		// .getUriFile("http://img.ahjjjc.gov.cn/uploadfiles/340700/image/20180104095407_90523.jpg");
		// String fileType = FileTypeHelper.getFileTypeByStream(uriFile);

	}

	/**
	 * 将爬虫获取到的数据添加至数据库,测试用
	 * 
	 * @param wbName
	 * @param ogName
	 * @param infos
	 * @return //
	 */
	public String testSetInfo(String infos) {
		this.wbName = "企务公开";
		this.ogName = "今日关注";
		String tsa="[     {         \"task\": 0,         \"author\": {             \"exist\": 0,             \"n\": \"\",             \"p\": \"\"         },         \"souce\": {             \"exist\": 1,             \"n\": \"发布时间：\",             \"p\": \"来源：\"         },         \"time\": {             \"exist\": 1,             \"n\": \"分享\",             \"p\": \"发布时间：\"         }     } ]";
		// appIns apps = appsProxy.getCurrentAppInfo();
		// JSONObject postParam = new JSONObject("param",codec.encodeFastJSON( infos));

		JSONObject json = JSONObject.toJSON(infos);
		infos = json.getString("param");
		String ogid = "", wbid = "";
		// 获取网站id
		wbid = getWbid(wbName);
		if (!StringHelper.InvaildString(wbid) || wbid.contains("errorcode")) {
			return rMsg.netMSG(1, "无效网站id");
		}
		// // 获取栏目id
		ogid = getOgid(wbid, ogName);
		if (!StringHelper.InvaildString(ogName) || wbid.contains("errorcode")) {
			return rMsg.netMSG(2, "无效栏目id");
		}
		return chuli_Info(wbid, ogid, tsa, infos);
	}

	/**
	 * 将爬虫获取到的数据添加至数据库
	 * 
	 * @param wbName
	 * @param ogName
	 * @return
	 */
	public String SetInfo(String wbName, String ogName, String tsa) {
		Print_nlogger.Print_SYSO("setinfo3个参数33333333333333333333333333333333333333333333333333333333333333333");
		this.wbName = wbName;
		this.ogName = ogName;
		String info = null;
		String ogid = "", wbid = "";
		JSONObject object = JSONObject.toJSON(execRequest.getChannelValue(grapeHttpUnit.formdata).toString());
		info = object.getString("param");
		// 获取网站id
		wbid = getWbid(wbName);
		if (!StringHelper.InvaildString(wbid) || wbid.contains("errorcode")) {
			return rMsg.netMSG(1, "无效网站id");
		}
		// 获取栏目id
		ogid = getOgid(wbid, ogName);
		if (!StringHelper.InvaildString(ogName) || wbid.contains("errorcode")) {
			return rMsg.netMSG(2, "无效栏目id");
		}
		return chuli_Info(wbid, ogid, tsa, info);
	}

	public String SetInfo(String wbName, String ogName, String tsa, String exdata) {
		Print_nlogger.Print_SYSO("SetInfo4个参数4444444444444444444444444444444444444444444444444444444444444444444");
		this.wbName = wbName;
		this.ogName = ogName;
		String info = null;
		String ogid = "", wbid = "";
		JSONObject object = JSONObject.toJSON(execRequest.getChannelValue(grapeHttpUnit.formdata).toString());
		info = object.getString("param");
		// 获取网站id
		wbid = getWbid(wbName);
		if (!StringHelper.InvaildString(wbid) || wbid.contains("errorcode")) {
			return rMsg.netMSG(1, "无效网站id");
		}
		// 获取栏目id
		ogid = getOgid(wbid, ogName);
		if (!StringHelper.InvaildString(ogName) || wbid.contains("errorcode")) {
			return rMsg.netMSG(2, "无效栏目id");
		}
		return chuli_Info(wbid, ogid, tsa, info);
	}

	/**
	 * 将爬虫获取到的数据添加至数据库
	 * 
	 * @param wbName
	 * @param ogName
	 * @return
	 */
	public String chuli_Info(String wbid, String ogid, String tsa, String info) {

		String result = rMsg.netMSG(0, "添加成功");
		JSONObject ArticleInfo = getArticle1(info); // 获取封装后的文章数据
		ArticleInfo = chuli_mainName(ArticleInfo);
		if (ArticleInfo != null && ArticleInfo.size() > 0) {
			for (Object obj : ArticleInfo.keySet()) {
				JSONObject Artinfo = ArticleInfo.getJson(obj);
				if (Artinfo != null && Artinfo.size() > 0) {
					Artinfo.puts("wbid", wbid).puts("ogid", ogid);// 网站id,栏目id
					Print_nlogger.Print_SYSO("ogid:" + ogid);
					Print_nlogger.Print_SYSO("wbid:" + wbid);
				}
				result = AddContent(wbid, ogid, codec.encodeFastJSON(Artinfo.toJSONString()), tsa);

			}
		} else {
			result = rMsg.netMSG(100, "添加失败");
		}
		return result;
	}

	public JSONObject chuli_mainName(JSONObject articleInfo) {
		for (Object a : articleInfo.keySet()) {
			JSONObject json = articleInfo.getJson(a);
			if (json != null && json.size() > 0) {
				String mainName = json.getString("mainName");
				if (StringHelper.InvaildString(mainName)) {
					String substring = "";
					if (mainName.contains("title=\"")) {
						String[] split = mainName.split("title=\"");
						String split1 = split[1];
						int indexOf = split1.indexOf("\"");
						substring = split1.substring(0, indexOf);
						String replaceAll = substring.replaceAll("―", "—").replaceAll("&quot;", "\"");

						json.puts("mainName", replaceAll);
					} else {
						String replaceAll = mainName.replaceAll("―", "—").replaceAll("&quot;", "\"");

						json.puts("mainName", replaceAll);
					}
				}
			}
		}
		return articleInfo;
	}

	private JSONObject appendInfo(JSONObject data, String key, Object val) {
		if (data == null) {
			data = new JSONObject();
		} else {
			String aa = "asd";
		}
		if (!data.containsKey(key)) {
			data.put(key, val);
		}
		return data;
	}

	/**
	 * 封装文章数据1
	 * 
	 * @param info
	 * @return {"url":{"mainName":"","content":""...}}
	 */
	private JSONObject getArticle1(String info) {
		String decodeFastJSON = codec.DecodeFastJSON(info);
		JSONObject obj = JSONObject.toJSON(codec.DecodeHtmlTag(decodeFastJSON));

		JSONObject rNewArray = new JSONObject();// 新信息数组
		int i = 0;
		for (Object _obj : obj.keySet()) {// _obj = mainName_XXX
			JSONObject json = obj.getJson(_obj);
			if (json != null && json.size() > 0) {
				String[] item = ((String) _obj).split("_");
				String url = json.getString("url");
				String key = (item.length == 2) ? item[1] : "0";
				Object val = json.getString("content");
				String mainName = item[0];
				JSONObject dataJson = (JSONObject) rNewArray.get(key);
				if (dataJson != null && dataJson.size() > 1) {
					i = 100;
				}
				dataJson = appendInfo(dataJson, item[0], val);
				if (!dataJson.containsKey("url")) {
					if ("mainName".equals(mainName)) {
						dataJson.puts("url", url);
					}
				}
				rNewArray.put(key, dataJson);
			}
		}

		// 构造有序结构组
		return rNewArray;
	}

	/**
	 * 封装文章数据
	 * 
	 * @param object
	 * @param info
	 * @return {"url":{"mainName":"","time":""}}
	 */
	@SuppressWarnings("unchecked")
	private JSONObject setArticleInfo(JSONObject object, String keys, String value, String url) {
		JSONObject tempjson = new JSONObject();
		if (StringHelper.InvaildString(keys)) {
			if (object != null && object.size() > 0) {
				if (object.containsKey(url)) {
					tempjson = object.getJson(url);
				}
			}
			tempjson.put(keys, value);
			object.put(url, tempjson);
		}
		return object;
	}

	private String chuliMainName1(String s) {
		char[] charArray = s.toCharArray();

		// for (int i = 0; i < charArray.length; i++) {
		// if
		// (checkHelper.checkChinese(String.valueOf(charArray[i]))||IsEnglish(String.valueOf(charArray[i])))
		// {
		// s = s.substring(i);
		// break;
		// }
		// }
		for (int i = 0; i < charArray.length; i++) {
			if ('・' == charArray[i]) {
				s = s.substring(i + 1);
				break;
			}
		}
		return s;
	}

	/**
	 * 添加文章到数据表中
	 * 
	 * @param wbid
	 * @param ogid
	 * @param ogName
	 * @param ContentInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String AddContent(String wbid, String ogid, String ContentInfo, String tsa) {
		int type = 1; // 0： 正常文章，1：外链文章
		String mainName = "", content = "", distinguish = "", contenturl = "", image = "";
		String result = rMsg.netMSG(100, "导入数据失败");
		ContentInfo = codec.DecodeFastJSON(ContentInfo);
		JSONObject object = JSONObject.toJSON(ContentInfo);
		JSONArray jsonArray = JSONArray.toJSONArray(tsa);
		JSONObject object2 = (JSONObject) jsonArray.get(0);
		int task = object2.getInt("task");
		// object = ClearData(object);
		// 获取时间及其他字段
		// object = getTime(object, "");
		object = getTime(object, tsa);
		if (object != null && object.size() > 0) {
			object.puts("image", "");
			// 验证文章是否已存在库中
			if (object.containsKey("mainName")) {
				mainName = object.getString("mainName");
				mainName = chuliMainName1(mainName);
				object.puts("mainName", mainName);
			}
			// if (object.containsKey("content")) {
			// content = object.getString("content");
			// if(object.containsKey("distinguish")) {//普通文章
			//
			// String chuLiContent = chuLiContent(content);
			// object.puts("content", chuLiContent);
			//
			// }
			// }
			if (object.containsKey("content")) {
				content = object.getString("content");
				content = HtmlUtils.chuli_xiangDui_href(content, object.getString("url"));
				object.puts("content", content);

			}
			if (task == 1) {// 外链任务

				if (!object.containsKey("distinguish")) {
					type = 1;
					contenturl = object.getString("url");
					object.put("contenturl", contenturl);
					teshu_lanmu_chuli_wailian(object);

				} else {

					type = 0;
					Print_nlogger.Print_SYSO(mainName + "          现在是外链任务,此文章为普通文章,不插入数据库");
					return rMsg.netMSG(200, "现在是外链任务,此文章为普通文章,不插入数据库");
				}
			}
			if (task == 0) {// 普通任务
				if (!object.containsKey("distinguish")) {
					type = 1;
					Print_nlogger.Print_SYSO(mainName + "          现在是普通任务,此文章为外链文章,不插入数据库");
					return rMsg.netMSG(200, "现在是普通任务,此文章为外链文章,不插入数据库");

				} else {
					if (!StringHelper.InvaildString(mainName) || !StringHelper.InvaildString(content)) {
						return result;
					}
					type = 0;
					teshu_lanmu_chuli_putong(object);
				}
			}
			JSONObject jj = new JSONObject().puts("ogid", ogid).puts("mainName", mainName).puts("content", content)
					.puts("contenturl", contenturl).puts("type", type).puts("image", image);
			Ceshi.ceshiZhuiJia1(jj.toJSONString());
			// 验证文章是否已存在
			if (ContentIsExsist(ogid, mainName, content, contenturl,String.valueOf(object.getLong("time")), type) != 0) { //

				// 文章已存在，返回错误提示信息
				Print_nlogger.Print_SYSO(mainName + "          文章已存在");
				return rMsg.netMSG(1, "文章已存在");
			}
			//铜官区轮播图
			tgqlbt(object);

			JSONObject postParam = new JSONObject("param", codec.encodeFastJSON(object.toJSONString()));
			appIns apps = appsProxy.getCurrentAppInfo();

			String temp = (String) appsProxy.proxyCall("/GrapeContent/Content/AddCrawlerContent/12", postParam, apps);
			if (StringHelper.InvaildString(temp) && temp.contains("errorcode")) {
				JSONObject rjJsonObject = JSONObject.toJSON(temp);
				if (rjJsonObject.getInt("errorcode") == 0) {
					Print_nlogger.Print_SYSO(mainName + "         数据存储成功");
					result = rMsg.netMSG(0, "数据存储成功");
				}
			}

		}
		return result;
	}

	private void tgqlbt(JSONObject object) {
		int appid = appsProxy.appid();
		if(appid==18&&"59892e12c6c2040edc1d5b81".equals(object.getString("ogid"))) {
			String content = object.getString("content");
			if(content.contains("<img src=\"")) {
				object.puts("attribute", 1);
			}
			
		}
	}

	/**
	 * 1:铜陵市纪律检查委员会-图片新闻
	 */
	private void teshu_lanmu_chuli_putong(JSONObject object) {
		if ("铜陵市纪律检查委员会".equals(wbName) && "图片新闻".equals(ogName)) {
			String wholeUrl = object.getString("url");
			if (!StringHelper.InvaildString(wholeUrl)) {
				return;
			}
			teshu_lanmu_chuli_putong_1(object, object.getString("content"), wholeUrl);
		}

	}

	private void teshu_lanmu_chuli_wailian(JSONObject object) {
		// if ("铜陵市纪律检查委员会".equals(wbName) && "图片新闻".equals(ogName)) {
		// String wholeUrl= object.getString("url");
		// if(!StringHelper.InvaildString(wholeUrl)) {
		// return;
		// }
		// teshu_lanmu_chuli_wailian_1(object, wholeUrl);
		// }

	}

	private void teshu_lanmu_chuli_putong_1(JSONObject obj, String content, String wholeUrl) {

		String url = getImageUrl_from_content_putong_1(content, wholeUrl);
		obj.puts("image", url);

	}

	private String getImageUrl_from_content_putong_1(String content, String wholeUrl) {
		List<String> link_then_getHref = HtmlUtils.getsrc(content);
		int flag = 0;
		String fileType = "";
		for (String url : link_then_getHref) {
			if (url.startsWith("data:image")) {
				continue;

			}
			int index = url.lastIndexOf(".");
			fileType = url.substring(index + 1);
			if (fileType_is_image(fileType)) {// 以图片类型结尾的超链接
				flag++;
				if (flag == 1) {// 第一张图片

					return url;
				}

			} else {// 不是以图片类型结尾的超链接

				byte[] uriFile = null;
				try {
					uriFile = request.getUriFile(url);
				} catch (Exception e) {
					Print_nlogger.Print_SYSO("url有问题,url:" + url);
				}
				if (uriFile != null && uriFile.length > 0) {
					fileType = FileTypeHelper.getFileTypeByStream(uriFile);

					if (fileType_is_image(fileType)) {
						flag++;
						if (flag == 2) {// 第一张图片不是,要第二张

							return url;
						}

					}

				}
			}

		}

		return "";

	}

	private void teshu_lanmu_chuli_wailian_1(JSONObject obj, String wholeUrl) {

		String html = request.page(wholeUrl);
		String url1 = getImageUrl_from_content_wailian_1(html, wholeUrl);
		obj.puts("image", url1);

	}

	public String getImageUrl_from_content_wailian_1(String content, String wholeUrl) {
		List<String> link_then_getHref = HtmlUtils.getsrc(content);
		int flag = 0;
		String fileType = "";
		for (String url : link_then_getHref) {
			if (url.startsWith("data:image")) {
				continue;

			}

			if (url.endsWith("wx_fmt=jpeg")) {// 以wx_lazy=1结尾的超链接
				flag++;
				if (flag == 2) {// 第一张图片不是,要第二张

					return url;
				}

			} // webp图片无法识别类型
				// else {//不是以x_lazy=1结尾的超链接
				//
				// byte[] uriFile = request.getUriFile(url);
				// if (uriFile != null && uriFile.length > 0) {
				// fileType = FileTypeHelper.getFileTypeByStream(uriFile);
				//
				// if (fileType_is_image(fileType)) {
				// flag++;
				// if (flag == 1) {//第一张图片不是,要第二张
				//
				// return url;
				// }
				//
				// }
				//
				// }
				// }

		}

		return "";

	}

	public boolean fileType_is_image(String fileType) {
		if ("jpg".equals(fileType) || "bmp".equals(fileType) || "jpg".equals(fileType) || "jpeg".equals(fileType)
				|| "png".equals(fileType) || "gif".equals(fileType)) {

			return true;
		}
		return false;
	}

	private String chuLiContent(String content) {

		if (content.contains("<img src=\"") && content.contains("data:image")) {
			int beginIndex = content.indexOf("<img src=\"");
			int beginIndex1 = content.indexOf("data:image");
			if (beginIndex1 - beginIndex < 100) {

				String string = new String(content);
				int num = string.split("<img src=\"").length;
				for (int i = 0; i < num; i++) {
					beginIndex = content.indexOf("<img src=\"");
					beginIndex1 = content.indexOf("data:image");
					int len = "<img src=\"".length();
					String substring = content.substring(beginIndex + len, beginIndex1);
					content = content.replace(substring, "");

				}
			}

		}
		return content;

	}

	@Test
	public void test() {
		chuLiContent(
				".....<img src=\"http://60.173.0.46:9000\\data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA;;<img src=\"http://60.173.0.46:9000\\data:image");
	}

	/**
	 * @param s
	 * @return 获得图片
	 */
	public static List<String> getImg(String s) {
		String regex;
		List<String> list = new ArrayList<String>();
		regex = "src=\"(.*?)\"";
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 返回存有图片地址
	 * 
	 * @param tar
	 * @return
	 */
	public static String getImgaddress(String tar) {
		List<String> imgList = getImg(tar);
		String res[] = new String[imgList.size()];
		String aa = "";
		if (imgList.size() > 0) {
			aa = imgList.get(0).substring(5);
			aa = aa.substring(0, aa.length() - 1);
		}
		return aa;
	}

	/**
	 * 验证文章是否已存在
	 * 
	 * @param ogid
	 * @param mainName
	 * @param content
	 * @return 0:表示文章不存在；1:表示文章已存在；2：远程调用API错误
	 */
	private int ContentIsExsist(String ogid, String mainName, String content, String contenturl,String time, int type) {
		int code = 2; // 远程调用API错误
		// 文章内容编码
		content = codec.encodebase64(content); // base64编码
		content = codec.EncodeHtmlTag(content); // 特殊格式编码

		// 外链url编码
		contenturl = codec.encodebase64(contenturl); // base64编码
		contenturl = codec.EncodeHtmlTag(contenturl); // 特殊格式编码
		if (type != 0) {
			content = contenturl;
		}
		JSONObject postParam = new JSONObject("param", content);
		appIns apps = appsProxy.getCurrentAppInfo();

		mainName = codec.encodeFastJSON(mainName); // 由于mainName可能含有特殊字符，需进行编码
		JSONObject jj = new JSONObject().puts("ogid", ogid).puts("mainName", mainName).puts("type", type)
				.puts("postParam", postParam);
		Ceshi.ceshiZhuiJia2(jj.toJSONString());
		String temp = (String) appsProxy.proxyCall(
				"/GrapeContent/Content/ContentIsExist/s:" + ogid + "/s:" + mainName + "/s:"+time+"/int:" + type + "/", postParam,
				apps);
		if (StringHelper.InvaildString(temp)) {
			code = Integer.parseInt(temp);
		}

		return code;
	}

	// @Test
	// public void test1() {
	// String[] split_data = "编辑日期：2017-11-07 作者：法规科 【字体： 大 中 小 】".split("：");
	// String[] a = new String[2];
	//
	// for (int i = 0; i < 2; i++) {
	// a[i] = chuliTimeOrAutherOrSource(split_data[i + 1]);
	//
	// }
	//
	// }

	// private String chuliTimeOrAutherOrSource(String a) {
	// char[] charArray = a.toCharArray();
	// int start = -1;
	// int end = 0;
	// int flag = 0;
	// int flag1 = 0;
	// for (int i = 0; i < charArray.length; i++) {
	//
	// boolean isNum = checkHelper.isNum(String.valueOf(charArray[i]));
	// boolean isChinese = checkHelper.checkChinese(String.valueOf(charArray[i]));
	// if (!isChinese && !isNum && (charArray[i] != '-')) {
	//
	// if (flag == 0) {
	// start = i;
	// flag = 1;
	// } else {
	// end = i;
	// break;
	// }
	//
	// } else if (flag1 == 0) {
	// start = 0;
	// flag = 1;
	// flag1 = 1;
	//
	// }
	//
	// }
	//
	// return a.substring(start == -1 ? 0 : start, end == 0 ? a.length() : end);
	//
	// }

	// @Test
	// public void testgetTime() {
	// getTime(new JSONObject("time", "编辑日期：2017-11-07 作者：法规科 【字体： 大 中 小 】"), "ta");
	//
	// }
	private void chuliTSA(JSONObject object, JSONObject authorORsouceORtime_JSONObject, String key, String data) {
		String n = authorORsouceORtime_JSONObject.getString("n");
		if (!StringHelper.InvaildString(n)) {
			n = null;
		}
		String p = authorORsouceORtime_JSONObject.getString("p");//
		if (!StringHelper.InvaildString(p)) {
			p = null;
		}
		// 想办法保证subString不为null
		String subString = StringUtils.subString(data, p, n);
		// if(subString==null) {
		// if(checkHelper.checkDate(data)) {
		// subString=data.trim();
		// }
		// }
		// " "
		if (subString == null) {
			throw new RuntimeException("下一个JSON");
		}

		String trimQGSPACE = trimQGSPACE(subString);

		if (key == "time") {
			try {
				trimQGSPACE = String.valueOf(getStamp(trimQGSPACE));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (e.getCause() instanceof ParseException) {
					throw new RuntimeException("下一个JSON");
				}

			}
			object.puts(key, Long.valueOf(trimQGSPACE));
		} else {
			object.puts(key, trimQGSPACE == null ? "" : trimQGSPACE);
		}

	}

	private JSONObject getTime1(JSONObject object, JSONObject tsa_JSONObject) {
		if (object != null && object.size() > 0) {
			String data = object.getString("time").trim();
			if (!StringHelper.InvaildString(data)) {
				// nlogger.logout(new RuntimeException(), "没有time字段");
				object.puts("time", TimeHelper.nowMillis());
				nlogger.logout("前台少写time_?字段");
				return object;

			}
			JSONObject author_JSONObject = (JSONObject) tsa_JSONObject.get("author");
			JSONObject souce_JSONObject = (JSONObject) tsa_JSONObject.get("souce");
			JSONObject time_JSONObject = (JSONObject) tsa_JSONObject.get("time");
			try {
				if (author_JSONObject.getInt("exist") == 1) {
					chuliTSA(object, author_JSONObject, "author", data);
				} else {
					object.puts("author", "");
				}

				if (souce_JSONObject.getInt("exist") == 1) {
					chuliTSA(object, souce_JSONObject, "souce", data);
				} else {
					object.puts("souce", "");
				}

				if (time_JSONObject.getInt("exist") == 1) {
					chuliTSA(object, time_JSONObject, "time", data);
				} else {
					object.puts("time", TimeHelper.nowMillis());
				}
			} catch (Exception e) {
				throw e;
			}

		}

		return null;
	}

	public static boolean IsEnglish(String str) {
		String regex = "[a-zA-Z]";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		boolean flag;
		try {
			Pattern reg = Pattern.compile(regex);
			Matcher matcher = reg.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	private String trimQGSPACE(String s) {

		char[] charArray = s.toCharArray();
		int start = 0;
		int end = 0;
		for (int i = 0; i < charArray.length; i++) {

			if (charArray[i] == ' ' || charArray[i] == ' ') {

			} else {
				start = i;
				break;
			}
		}
		for (int i = charArray.length - 1; i >= 0 && i < charArray.length; i--) {

			if (charArray[i] == ' ' || charArray[i] == ' ') {

			} else {
				end = i;
				break;
			}
		}
		String sum = "";
		for (int i = start; i <= end; i++) {
			sum = sum + charArray[i];

		}

		return sum;

	}

	/**
	 * 封装时间，作者，来源
	 * 
	 * @param object
	 * @param ogName
	 * @return
	 */
	private JSONObject getTime(JSONObject object, String tsa) {

		JSONArray jsonArray = JSONArray.toJSONArray(tsa);

		Exception e = null;
		int i = 0;

		do {
			try {
				e = null;
				JSONObject ob;
				try {
					ob = (JSONObject) jsonArray.get(i++);
				} catch (IndexOutOfBoundsException e2) {
					throw new RuntimeException("JSON参数写错了");
				}
				getTime1(object, ob);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e = e1;
			}
		} while (e != null && e.getMessage() == "下一个JSON");

		return object;

	}

	/**
	 * 根据不同的显示时间类型转换为时间戳
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	private long getStamp(String time) {
		long timeStamp = 0;
		SimpleDateFormat simpleDateFormat = null;
		Date date = null;
		if (!StringHelper.InvaildString(time)) {
			return TimeHelper.nowMillis();
		}
		int length = time.length();
		try {
			if (ifHavaChinese(time)) {

				simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				date = simpleDateFormat.parse(time);
				timeStamp = date.getTime();

			} else if (length > 0 && length <= 10) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				date = simpleDateFormat.parse(time);

				timeStamp = date.getTime();
			} else if (length > 10 && length < 19) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				date = simpleDateFormat.parse(time);

				timeStamp = date.getTime();
			} else {
				timeStamp = TimeHelper.dateToStamp(time);
			}
		} catch (Exception e) {
			// nlogger.logout(e);
			timeStamp = TimeHelper.nowMillis();
			if (e instanceof java.text.ParseException) {
				throw new RuntimeException(e);
			}
		}
		return timeStamp;
	}

	public boolean ifHavaChinese(String s) {
		char[] charArray = s.toCharArray();
		for (char c : charArray) {
			String valueOf = String.valueOf(c);
			if (checkHelper.checkChinese(valueOf)) {
				return true;
			}

		}
		return false;

	}

	/**
	 * 获取网站id
	 * 
	 * @param wbid
	 * @return
	 */
	private String getWbid(String wbid) {
		String result = null;
		if (StringHelper.InvaildString(wbid)) {
			if (checkHelper.checkChinese(wbid)) { // 判断wbid是否为纯中文,即网站名称
				result = (String) appsProxy.proxyCall("/GrapeWebInfo/WebInfo/getWbidByName/" + wbid);
			}
			if (!StringHelper.InvaildString(result)) {
				return rMsg.netMSG(1, "无效网站id");
			}
		}
		return result;
	}

	/**
	 * 获取栏目id
	 * 
	 * @param wbid
	 * @return
	 */
	private String getOgid(String wbid, String ogid) {
		String result = ogid;
		if (StringHelper.InvaildString(ogid)) {
			if (checkHelper.checkChinese(ogid)) { // 判断wbid是否为纯中文,即栏目名称
				result = (String) appsProxy.proxyCall("/GrapeContent/ContentGroup/getOgidByName/" + wbid + "/" + ogid);
			}
			if (!StringHelper.InvaildString(result)) {
				return rMsg.netMSG(1, "无效栏目id");
			}
		}
		return result;
	}
}
