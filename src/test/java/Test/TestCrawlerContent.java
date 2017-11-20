package Test;

import httpServer.booter;
import nlogger.nlogger;

public class TestCrawlerContent {
    public static void main(String[] args) {
        booter booter = new booter();
        try {
            System.out.println("GrapeCrawlerContent");
            System.setProperty("AppName", "GrapeCrawlerContent");
            booter.start(1008);
        } catch (Exception e) {
            nlogger.logout(e);
        }
    }
}
