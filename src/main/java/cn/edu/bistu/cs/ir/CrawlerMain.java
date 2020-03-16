package cn.edu.bistu.cs.ir;

import cn.edu.bistu.cs.ir.pps.JDBooks;
import cn.edu.bistu.cs.ir.pps.TencentNews;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
/**
 * @author ruoyuchen
 */
public class CrawlerMain {

    private static final Logger log = Logger.getLogger(CrawlerMain.class);

    public static void main(String[] args){
//        startTencentNewsCrawler();
        startJDBooksCrawler();
    }

    private static void startTencentNewsCrawler(){
        log.info("启动腾讯新闻爬虫...");
        String startPage = "https://new.qq.com/ch/antip/";
        TencentNews crawler = new TencentNews();
        Spider spider = Spider.create(crawler);
        spider.addPipeline(new JsonFilePipeline("/Users/ruoyuchen/Downloads/qqnews"));
        spider.thread(1);
        spider.addUrl(startPage);
        spider.run();
    }

    private static void startJDBooksCrawler(){
        log.info("启动京东商城爬虫...");
        String startPage = "https://list.jd.com/list.html?cat=1713,3287,3797";
        JDBooks crawler = new JDBooks();
        Spider spider = Spider.create(crawler);
        spider.addPipeline(new JsonFilePipeline("/Users/ruoyuchen/Downloads/jdbooks"));
        spider.thread(1);
        spider.addUrl(startPage);
        spider.run();
    }
}
