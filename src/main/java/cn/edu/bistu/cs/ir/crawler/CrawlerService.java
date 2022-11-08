package cn.edu.bistu.cs.ir.crawler;

import cn.edu.bistu.cs.ir.config.Config;
import cn.edu.bistu.cs.ir.index.IdxService;
import cn.edu.bistu.cs.ir.index.LucenePipeline;
import cn.edu.bistu.cs.ir.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import javax.annotation.PostConstruct;

import static us.codecraft.webmagic.Spider.Status.Stopped;

/**
 * 面向爬虫的服务类
 * @author chenruoyu
 */
@Component
public class CrawlerService{

    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    private final Config config;

    private final IdxService idxService;

    public CrawlerService(@Autowired Config config,
                          @Autowired IdxService idxService){
        this.config = config;
        this.idxService = idxService;
    }

    private Spider spider = null;


    /**
     * 启动面向博客园的爬虫
     * @param blogger 待爬取的博主ID
     */
    public void startCnBlogCrawler(String blogger) {
        if(StringUtil.isEmpty(blogger)){
            log.error("博主的唯一ID不可以为空");
            return;
        }
        String startPage = String.format("https://www.cnblogs.com/%s/default.html?page=2", blogger);
        if(this.spider != null){
            if(!Stopped.equals(this.spider.getStatus())){
                //如果spider成员不为空，并且状态不是 Stopped，则不可以启动新的爬虫
                log.error("当前有正在运行的爬虫对象，不可以创建新的爬虫");
                return;
            }
        }
        Site site = Site
                .me()
                .setRetryTimes(config.getRetryTimes())
                .setSleepTime(config.getSleepTime())
                .setUserAgent(config.getAgent());
        this.spider = Spider.create(new CnBlogsCrawler(site, blogger));
        spider.addPipeline(new LucenePipeline(idxService));
        spider.addPipeline(new JsonFilePipeline(config.getCrawler()));
        spider.thread(1);
        spider.addUrl(startPage);
        spider.runAsync();
        log.info("启动面向博客园的爬虫，抓取博主ID为[{}]的作者的文章", blogger);
    }

    @PostConstruct
    public void init(){
        if(config.isStartCrawler()){
            log.info("系统配置信息中[startCrawler]配置项为true，启动爬虫的运行");
            startCnBlogCrawler("tencent-cloud-native");
        }
    }
}
