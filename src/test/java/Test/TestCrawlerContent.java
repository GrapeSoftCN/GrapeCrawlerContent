package Test;

import common.java.httpServer.booter;
import common.java.nlogger.nlogger;

public class TestCrawlerContent {
    public static void main(String[] args) {
        booter booter = new booter();
        try {
            System.out.println("GrapeCrawlerContent");
            System.setProperty("AppName", "GrapeCrawlerContent");
            booter.start(1006);
        } catch (Exception e) {
            nlogger.logout(e);
        }
//        String string = "{\"time_0\":{\"url\":\"http:\\/\\/www.tljw.gov.cn\\/article.php?MsgId=414668\",\"content\":\"2017-12-14 09:16 作者：铜陵市纪委\"},\"mainName_0\":{\"url\":\"http:\\/\\/www.tljw.gov.cn\\/article.php?MsgId=414668\",\"content\":\"关于报送2017年度\\u201C三创\\u201D参评项目的相关文件\"},\"content_0\":{\"url\":\"http:\\/\\/www.tljw.gov.cn\\/article.php?MsgId=414668\",\"content\":\"<td> <p> <a class=\\\"ke-insertfile\\\" href=\\\"javascript:;\\\" target=\\\"_blank\\\"> 铜纪办6号 <\\/a> <\\/p> <p> <a class=\\\"ke-insertfile\\\" href=\\\"javascript:;\\\" target=\\\"_blank\\\"> 铜纪办通报第5期 <\\/a> <\\/p> <a class=\\\"ke-insertfile\\\" href=\\\"javascript:;\\\" target=\\\"_blank\\\"> 铜纪办通报第5期附件2 <\\/a> <p> &nbsp; <\\/p> <p> &nbsp; <\\/p> <\\/td>\"},\"time_1\":{\"url\":\"http:\\/\\/www.tljw.gov.cn\\/article.php?MsgId=414668\",\"content\":\"2017-12-14 09:16 作者：铜陵市纪委\"}}";
//        String data = codec.encodeFastJSON(string);
//        System.out.println(data);
//        System.out.println(codec.DecodeFastJSON(data));
    }
}
