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
 * 面向博客园（cnblogs.com）的爬虫
 * 例如：<a href="https://www.cnblogs.com/tencent-cloud-native/">腾讯云原生</a>
 * @author chenruoyu
 */
public class CnBlogsCrawler implements PageProcessor {


    private final Site site;

    /**
     * 博主的ID
     */
    private final String bloggerId;

    private static final Logger log = LoggerFactory.getLogger(CnBlogsCrawler.class);

    public static final String RESULT_ITEM_KEY = "BLOG_INFO";

    /**
     * 当前博主的博文目录页URL前缀
     */
    private final String list_prefix;

    /**
     * 当前博主的博文内容页URL前缀
     */
    private final String blog_prefix;


    public CnBlogsCrawler(Site site, String bloggerId){
        this.site = site;
        this.bloggerId = bloggerId;
        //https://www.cnblogs.com/tencent-cloud-native/default.html?page=1
        //https://www.cnblogs.com/tencent-cloud-native/p/14913423.html
        this.list_prefix = String.format("https://www.cnblogs.com/%s/default.html", bloggerId);
        this.blog_prefix = String.format("https://www.cnblogs.com/%s/p/", bloggerId);
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("uuuu-MM-dd HH:mm");

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        if(url.startsWith(list_prefix)){
            log.info("解析博客目录页[{}]", url);
            List<String> blogs = page.getHtml().xpath("//div[@id='home']//div[@class='forFlow']//div[@class='postTitle']/a/@href").all();
            log.info("获取博文内容页地址[{}]条", blogs.size());
            page.addTargetRequests(blogs);
            //TODO 请大家思考如何添加其他博客目录页的地址?需要解析博客目录页最底部的导航栏
            //setSkip方法可以跳过后续的Pipeline的处理
            page.setSkip(true);
        }else if(url.startsWith(blog_prefix)){
            log.info("解析博客内容页[{}]", url);
            //博文的ID，设置为页面URL删去前缀和 .html后缀后的字符串
            String id = url.replace(blog_prefix, "").replace(".html", "");
            String title = page.getHtml().xpath("//div[@class='forFlow']//div[@class='post']/h1[@class='postTitle']/a/span/text()").get();
            String time = page.getHtml().xpath("//div[@class='forFlow']//div[@class='post']/div[@class='postDesc']/span[@id='post-date']/text()").get();
            String content = page.getHtml().xpath("//div[@class='forFlow']//div[@class='post']//div[@id='cnblogs_post_body']/tidyText()").get();
            String blogger = page.getHtml().xpath("//div[@id='blogTitle']/div[@class='title']/a/text()").get();
            //TODO 请大家思考如何抓取页面中的标签、阅读数、评论数等数据?
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
