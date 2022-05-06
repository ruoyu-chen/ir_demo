package cn.edu.bistu.cs.ir.crawler;

import cn.edu.bistu.cs.ir.model.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 面向新浪博客
 * （例如<a href="http://blog.sina.com.cn/kaifulee">李开复的新浪博客</a>）
 * 的网络爬虫示例
 * @author ruoyuchen
 */
public class SinaBlogCrawler implements PageProcessor {

    private static final String URL_PREFIX_LIST = "http://blog.sina.com.cn/s/articlelist_";

    private static final String URL_PREFIX_BLOG = "http://blog.sina.com.cn/s/blog_";

    public static final String RESULT_ITEM_KEY = "BLOG_INFO";

    private final Site site;

    /**
     * 博主的ID
     */
    private final String blogger;

    public SinaBlogCrawler(Site site, String blogger){
        this.site = site;
        this.blogger = blogger;
    }

    private static final Logger log = LoggerFactory.getLogger(SinaBlogCrawler.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("(uuuu-MM-dd HH:mm:ss)");

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        if(url.startsWith(URL_PREFIX_LIST)){
            log.info("解析博客目录页[{}]", url);
            List<String> blogs = page.getHtml().xpath("//div[@class='SG_connBody']//div[@class='articleList']//a/@href").all();
            log.info("获取博文内容页地址[{}]条", blogs.size());
            page.addTargetRequests(blogs);
            List<String> lists = page.getHtml().xpath("//div[@class='SG_connBody']//div[@class='SG_page']//li/a/@href").all();
            log.info("获取博文目录页地址[{}]条", lists.size());
            page.addTargetRequests(lists);
            //setSkip方法可以跳过后续的Pipeline的处理
            page.setSkip(true);
        }else if(url.startsWith(URL_PREFIX_BLOG)){
            log.info("解析博客内容页[{}]", url);
            //博文的ID，设置为页面URL删去前缀和 .html后缀后的字符串
            String id = url.replace(URL_PREFIX_BLOG, "").replace(".html", "");
            String title = page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalTitle']/h2/text()").get();
            String time = page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalTitle']/span/text()").get();
            String content = page.getHtml().xpath("//div[@id='articlebody']//div[@id='sina_keyword_ad_area2']/tidyText()").get();
            //TODO 请大家思考如何抓取页面中的标签、分类，阅读数、收藏数等数据
            Blog blog = new Blog();
            blog.setId(id);
            blog.setTitle(title);
            blog.setContent(content);
            try {
                blog.setDate(sdf.parse(time).getTime());
            } catch (ParseException e) {
                log.error("无法识别的日期时间格式:[{}]", time);
                e.printStackTrace();
                blog.setDate(0);
            }
            blog.setAuthor(blogger);
            page.putField(RESULT_ITEM_KEY, blog);
        }else{
            log.warn("暂不支持的URL地址:[{}]", url);
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}