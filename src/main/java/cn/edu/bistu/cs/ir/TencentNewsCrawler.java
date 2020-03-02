package cn.edu.bistu.cs.ir;

import cn.edu.bistu.cs.ir.data.News;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 面向腾讯新闻(https://new.qq.com/ch/antip/)的网络爬虫示例
 */
public class TencentNewsCrawler implements PageProcessor {

    private static final Logger log = Logger.getLogger(TencentNewsCrawler.class);

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        if(url.contains("https://new.qq.com/ch/antip/")){
            log.info("解析新闻列表");
            List<Selectable> lis = page.getHtml().xpath("//div[@class='main fl']/div[@id='List']//ul[@class='list']/li").nodes();
            log.info("找到首页新闻["+lis.size()+"]条");
            for(Selectable li: lis){
                String news_url = li.xpath("//div[@class='detail']/h3/a/@href").get();
                page.addTargetRequest(news_url);
                String news_title = li.xpath("//div[@class='detail']/h3/a/text()").get();
                log.info("新闻标题:"+news_title+", 新闻URL:"+news_url);
            }
            page.setSkip(true);
        }else if(url.startsWith("https://new.qq.com/omn/")){
            log.info("解析新闻内容");
            String title = page.getHtml().xpath("//div[@class='qq_conent clearfix']/div[@class='LEFT']/h1/text()").get();
            String content = page.getHtml().xpath("//div[@class='qq_conent clearfix']/div[@class='LEFT']/div[@class='content clearfix']/div[@class='content-article']/tidyText()").get();
            String html = page.getHtml().xpath("//div[@class='qq_conent clearfix']/div[@class='LEFT']/div[@class='content clearfix']/div[@class='content-article']/html()").get();
            if(title==null||"".equalsIgnoreCase(title.trim())){
                log.warn("无法提取页面内容，请检查页面格式:"+url);
                page.putField("SUCCESS", "FALSE");
                page.putField("URL", url);
            }else{
                log.info("完成新闻内容抓取："+title);
                News news = new News();
                news.setUrl(url);
                news.setTitle(title);
                news.setHtml(html);
                news.setContent(content);
                page.putField("NEWS", news);
                page.putField("SUCCESS", "TRUE");
            }
        }else if(url.startsWith("https://new.qq.com/zt/")){
            log.warn("新闻专题，暂不支持:"+url);
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args){
        log.info("爬虫启动...");
        String startPage = "https://new.qq.com/ch/antip/";
        TencentNewsCrawler crawler = new TencentNewsCrawler();
        Spider spider = Spider.create(crawler);
        spider.addPipeline(new JsonFilePipeline("/Users/ruoyuchen/Downloads/qqnews"));
        spider.thread(1);
        spider.addUrl(startPage);
        spider.run();
    }
}
