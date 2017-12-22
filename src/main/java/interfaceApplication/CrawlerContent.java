package interfaceApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import JGrapeSystem.rMsg;
import apps.appIns;
import apps.appsProxy;
import check.checkHelper;
import httpServer.grapeHttpUnit;
import nlogger.nlogger;
import rpc.execRequest;
import security.codec;
import string.StringHelper;
import time.TimeHelper;

/**
 * 爬虫回调函数
 * 
 *
 */
public class CrawlerContent {
    /**
     * 将爬虫获取到的数据添加至数据库
     * 
     * @param wbName
     * @param ogName
     * @return
     */
    public String SetInfo(String wbName, String ogName) {
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
        return SetInfo(wbid, ogid, info);
    }

    /**
     * 将爬虫获取到的数据添加至数据库
     * 
     * @param wbName
     * @param ogName
     * @return
     */
    @SuppressWarnings("unchecked")
    public String SetInfo(String wbid, String ogid, String info) {
        String url = "";
        String result = rMsg.netMSG(0, "添加成功");
        JSONObject tempJson;
        JSONObject ArticleInfo = getArticle(info); // 获取封装后的文章数据
        if (ArticleInfo != null && ArticleInfo.size() > 0) {
            for (Object obj : ArticleInfo.keySet()) {
                url = obj.toString();
                tempJson = ArticleInfo.getJson(url);
                tempJson.put("url", url);
                tempJson.puts("wbid", wbid).puts("ogid", ogid);// 网站id,栏目id
                result = AddContent(wbid, ogid, codec.encodeFastJSON(tempJson.toJSONString()));
                System.out.println("tips: " + result);
            }
        } else {
            result = rMsg.netMSG(100, "添加失败");
        }
        return result;
    }

    /**
     * 封装文章数据
     * 
     * @param info
     * @return {"url":{"mainName":"","content":""...}}
     */
    private JSONObject getArticle(String info) {
        JSONObject tempjson, tempobj = new JSONObject();
        JSONObject obj = JSONObject.toJSON(codec.DecodeFastJSON(info));
        String key, url, contents, keys = "";
        if (obj != null && obj.size() > 0) {
            for (Object object : obj.keySet()) {
                key = object.toString();
                tempjson = obj.getJson(key); // {"url":"","content":""}
                if (tempjson != null && tempjson.size() > 0) {
                    url = tempjson.getString("url"); // 获取url,
                    contents = tempjson.getString("content");// 获取内容
                    if (key.contains("mainName")) {
                        keys = "mainName";
                    }
                    if (key.contains("time")) {
                        keys = "time";
                    }
                    if (key.contains("souce")) {
                        keys = "souce";
                    }
                    if (key.contains("author")) {
                        keys = "author";
                    }
                    if (key.contains("content")) {
                        keys = "content";
                    }
                    if (key.contains("distinguish")) {
                        keys = "distinguish";
                    }
                    tempobj = setArticleInfo(tempobj, keys, contents, url);
                }
            }
        }
        return tempobj;
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
    private String AddContent(String wbid, String ogid, String ContentInfo) {
        int type = 1; // 0： 正常文章，1：外链文章
        String mainName = "", content = "", distinguish = "", contenturl = "";
        String result = rMsg.netMSG(100, "导入数据失败");
        ContentInfo = codec.DecodeFastJSON(ContentInfo);
        JSONObject object = JSONObject.toJSON(ContentInfo);
        // object = ClearData(object);
        // 获取时间及其他字段
        object = getTimeColumn(object);
        if (object != null && object.size() > 0) {
            // 验证文章是否已存在库中
            if (object.containsKey("mainName")) {
                mainName = object.getString("mainName");
            }
            if (object.containsKey("content")) {
                content = object.getString("content");
            }
            if (object.containsKey("distinguish")) {
                distinguish = object.getString("distinguish"); // 文章外链至微信，获取文章url
            }
            if (StringHelper.InvaildString(distinguish)) {
                contenturl = object.getString("url");
                object.put("contenturl", contenturl);
            }
            if (!object.containsKey("contenturl")) {
                if (!StringHelper.InvaildString(mainName) || !StringHelper.InvaildString(content)) {
                    return result;
                }
                type = 0; // 正常文章
            }
            // 验证文章是否已存在
            if (ContentIsExsist(ogid, mainName, content, contenturl, type) != 0) { // 文章已存在，返回错误提示信息
                return rMsg.netMSG(1, "文章已存在");
            }
            JSONObject postParam = new JSONObject("param", codec.encodeFastJSON(object.toJSONString()));
            appIns apps = appsProxy.getCurrentAppInfo();
            String temp = (String) appsProxy.proxyCall("/GrapeContent/Content/AddCrawlerContent/12", postParam, apps);
            if (StringHelper.InvaildString(temp) && temp.contains("errorcode")) {
                JSONObject rjJsonObject = JSONObject.toJSON(temp);
                if (rjJsonObject.getInt("errorcode") == 0) {
                    result = rMsg.netMSG(0, "数据存储成功");
                }
            }

        }
        return result;
    }

    /**
     * 将爬虫获取到的数据添加至数据库,测试用
     * 
     * @param wbName
     * @param ogName
     * @param infos
     * @return
     */
    private String SetInfoTest(String wbName, String ogName, String infos) {
        String ogid = "", wbid = "";
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
        return SetInfo(wbid, ogid, infos);
    }

    /**
     * 将爬虫获取到的数据添加至数据库,暂不用
     * 
     * @param wbName
     * @param ogName
     * @param infos
     * @return
     */
    private String SetInfos(String wbName, String ogName, String infso) {
        String info = null;
        String ogid = "", wbid = "";
        JSONObject object = JSONObject.toJSON(infso);
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
        return SetInfo(wbid, ogid, info);
    }

    /**
     * 获取栏目时间，作者
     * 
     * @param object
     * @return
     */
    private JSONObject getTimeColumn(JSONObject object) {
        String data = "";
        long times = TimeHelper.nowMillis();
        String time = "", souce = "", author = "", image = "";
        if (object != null && object.size() > 0) {
            if (object.containsKey("time")) {
                data = object.getString("time");
                if (data.contains("作者")) {
                    time = catchString("", "作者：", data);
                    author = data.substring(20, data.length());
                } else if (data.contains("发布日期")) {
                    // 捕获发布日期
                    time = catchString("发布日期：", "发布单位", data).replaceAll("\\u00A0", "");
                    // 捕获发布单位
                    author = catchString("作者：", " ", data);
                    // 捕获来源
                    souce = catchString("来源：", " ", data);
                } else {
                    time = data.trim().replaceAll("\\u00A0", ""); // 发布时间
                }
                times = StringHelper.InvaildString(time) ? getStamp(time) : TimeHelper.nowMillis();
            }
            if (object.containsKey("url")) {
                data = object.getString("url");
                String url = data.split("/")[0] + "//" + data.split("/")[2];
                if (object.containsKey("content")) {
                    data = object.getString("content");
                    if (data.contains("src=")) {
                        if (!data.contains("http://")) {
                            String[] conte = data.split("src=");
                            String content = "";
                            for (int i = 0; i < conte.length; i++) {
                                if (i <= 0) {
                                    content = conte[i];
                                } else {
                                    content += "src=" + '"' + url + conte[i].substring(1, conte[i].length());
                                }

                            }
                            object.puts("content", content);
                        }

                    }
                }

            }
            if (object.containsKey("content")) {
                data = object.getString("content");
                image = getImgaddress(data);
            }
            object.puts("time", times).puts("author", author).puts("souce", souce).puts("image", image);
        }
        return object;
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
    private int ContentIsExsist(String ogid, String mainName, String content, String contenturl, int type) {
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
        if (mainName.contains("%")) {
            return code = 1;
        } else {
            String temp = (String) appsProxy.proxyCall("/GrapeContent/Content/ContentIsExist/" + ogid + "/" + mainName + "/int:" + type + "/", postParam, apps);
            if (StringHelper.InvaildString(temp)) {
                code = Integer.parseInt(temp);
            }
        }
        return code;
    }

    /**
     * 整理获取的爬虫数据
     * 
     * @param object
     *            {key:{"url":"","content":""},key:{"url":"","content":""}}
     * @return {key:content,key:content}
     */
    @SuppressWarnings("unchecked")
    private JSONObject ClearData(JSONObject object) {
        JSONObject tempObj, rsObject = new JSONObject();
        String key, value = "";
        if (object != null && object.size() > 0) {
            for (Object obj : object.keySet()) {
                key = obj.toString();
                tempObj = object.getJson(key);
                if (tempObj != null && tempObj.size() > 0) {
                    if (tempObj.containsKey("content")) {
                        value = tempObj.getString("content");
                    }
                    if (key.equals("content")) { // 对于文章内容进行编码
                        rsObject.escapeHtmlPut("content", value);
                    } else {
                        rsObject.put(key, value);
                    }
                }
            }
        }
        return rsObject;
    }

    /**
     * 封装时间，作者，来源
     * 
     * @param object
     * @param ogName
     * @return
     */
    private JSONObject getTime(JSONObject object, String ogName) {
        switch (ogName) {
        case "今日关注":
            object = getTimeByJRGZ(object); // 获取今日关注时间，作者，来源
            break;
        case "国企党建":
            object = getTimeByGQDJ(object); // 获取国企党建时间，来源
            break;
        case "国企改革":
            object = getTimeByGQDJ(object); // 获取国企改革时间，来源
            break;
        case "纪检要闻":
            object = getTimeByJJYW(object); // 获取纪检要闻时间，来源
            break;
        case "政策法规":
            object = getTimeByzcfg(object); // 获取纪检监察时间，来源
            break;
        case "学思践悟":
            object = getTimeByJJYW(object); // 获取纪检监察时间，来源
            break;
        // case "图片视频":
        // object = getTimeByzcfg(object); // 获取图片视频时间，来源
        // break;
        // case "警示案例":
        // object = getTimeByzcfg(object); // 获取警示案例时间，来源
        // break;
        case "工作动态": // 铜官区五务公开-居务公开-工作动态

            object = getTimeByJwgk(object); // 获取铜官区五务公开-居务公开-工作动态时间，来源
            break;
        }
        return object;
    }

    /**
     * 获取居务公开下工作动态栏目的时间，作者，来源，阅读次数
     * 
     * @project SYJJContent
     * @package interfaceApplication
     * @file Content.java
     * 
     * @param object
     * @return
     *
     */
    private JSONObject getTimeByJwgk(JSONObject object) {
        if (object != null && object.size() > 0) {
            String data = object.getString("time").trim();
            // 捕获发布日期
            String tempTime = catchString("发布日期：", " ", data);
            // 捕获发布单位
            String tempAuthor = catchString("发布单位：", " ", data);
            // 捕获来源
            String tempSource = catchString("来源：", " ", data);

            object.puts("time", tempTime).puts("author", tempAuthor).puts("souce", tempSource);
        }
        return object;
    }

    /**
     * 获取政策法规栏目时间，来源
     * 
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject getTimeByzcfg(JSONObject object) {
        String data = "";
        String time = "", souce = "";
        if (object != null && object.size() > 0) {
            if (object.containsKey("time")) {
                data = object.getString("time");
                time = catchString("发布日期", "  ", data); // 发布时间
            }
            if (object.containsKey("souce")) {
                data = object.getString("souce");
                souce = catchString("信息来源", "  ", data); // 文章来源
            }
        }
        if (StringHelper.InvaildString(souce)) {
            object.put("souce", souce);
        }
        object.put("time", StringHelper.InvaildString(time) ? getStamp(time) : TimeHelper.nowMillis());
        return object;
    }

    /**
     * 获取纪检要闻栏目时间，来源
     * 
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject getTimeByJJYW(JSONObject object) {
        String data = "";
        String time = "", souce = "";
        if (object != null && object.size() > 0) {
            if (object.containsKey("time")) {
                data = object.getString("time");
                time = catchString("发布时间", "  ", data); // 发布时间
            }
            if (object.containsKey("souce")) {
                data = object.getString("souce");
                souce = catchString("来源", "  ", data); // 文章来源
            }
        }
        if (StringHelper.InvaildString(souce)) {
            object.put("souce", souce);
        }
        object.put("time", StringHelper.InvaildString(time) ? getStamp(time) : TimeHelper.nowMillis());
        return object;
    }

    /**
     * 获取国企党建栏目时间，来源
     * 
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject getTimeByGQDJ(JSONObject object) {
        String data = "";
        String time = "", souce = "";
        if (object != null && object.size() > 0) {
            if (object.containsKey("time")) {
                data = object.getString("time");
            }
            time = catchString("发布时间", "  ", data); // 发布时间
            souce = catchString("文章来源", "  ", data); // 文章来源
        }
        if (StringHelper.InvaildString(souce)) {
            object.put("souce", souce);
        }
        object.put("time", StringHelper.InvaildString(time) ? getStamp(time) : TimeHelper.nowMillis());
        return object;
    }

    /**
     * 获取今日关注栏目文章发布时间，作者，来源
     * 
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    private JSONObject getTimeByJRGZ(JSONObject object) {
        String data = "";
        String time = "", author = "", souce = "";
        if (object != null && object.size() > 0) {
            if (object.containsKey("time")) {
                data = object.getString("time");
            }
            if (StringHelper.InvaildString(data)) {
                if (data.startsWith("作者/来自")) {
                    author = catchString("作者/来自", " ", data);
                    souce = author;
                    time = catchString("发表时间", " ", data);
                } else if (data.startsWith("发布时间")) {
                    time = catchString("发布时间", " ", data);
                } else {
                    time = data.split("责任编辑:")[0].trim();
                    author = time.split("责任编辑:")[1].trim();
                }
                if (StringHelper.InvaildString(time)) {
                    object.put("time", getStamp(time));
                }
                if (StringHelper.InvaildString(author)) {
                    object.put("author", author);
                }
                if (StringHelper.InvaildString(souce)) {
                    object.put("souce", souce);
                }
            } else {
                object.put("time", TimeHelper.nowMillis());
            }
        }
        return object;
    }

    /**
     * 根据不同的显示时间类型转换为时间戳
     * 
     * @param time
     * @return
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
            if (length > 0 && length <= 10) {
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
            nlogger.logout(e);
            timeStamp = TimeHelper.nowMillis();
        }
        return timeStamp;
    }

    /**
     * 捕获发布日期，作者，来源
     * 
     * @param caption
     * @param endChr
     * @param data
     * @return
     */
    private static String catchString(String caption, String endChr, String data) {
        int l = caption.length();
        int end, i = data.toLowerCase().indexOf(caption);
        String temp = "", tempResult = "";
        if (i >= 0) {
            i += l;
            temp = data.substring(i).trim();
            end = temp.indexOf(endChr);
            if (end >= 0) {
                tempResult = temp.substring(0, end).trim();
            }
        }
        return tempResult;
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
