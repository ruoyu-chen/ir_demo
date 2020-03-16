package cn.edu.bistu.cs.ir.pps;

import cn.edu.bistu.cs.ir.data.Book;
import cn.edu.bistu.cs.ir.data.Price;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 面向京东图书类商品
 * （编程语言与程序设计 https://list.jd.com/list.html?cat=1713,3287,3797）
 * 的简单网络爬虫
 */
public class JDBooks implements PageProcessor {

    private static final Logger log = Logger.getLogger(JDBooks.class);

    //京东商城反爬虫比较严格, 需要进行浏览器伪装，即将UserAgent设置为 Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15
    // 此外，为了避免过度频繁抓取，将两次请求间隔设置为10秒（10000毫秒）
    private Site site = Site.me().setRetryTimes(3).setSleepTime(10000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15");

    @Override
    public void process(Page page) {
        String url = page.getRequest().getUrl();
        if(url.startsWith("https://list.jd.com/")){
            //列表页/目录页
            //https://list.jd.com/list.html?cat=1713,3287,3797
            log.info("处理列表页["+url+"]...");
            //目前仅提取当前列表页内部包含的信息页链接，不提取指向其他列表页的链接
            List<String> items = page.getHtml().xpath("//div[@id='plist']/" +
                    "ul[@class='gl-warp clearfix']/" +
                    "li[@class='gl-item']//div[@class='p-name']/" +
                    "a/@href").all();
            log.info("共提取到商品信息页链接"+items.size()+"个");
            log.info("作为演示，只抓取其中的第一个链接");
            page.addTargetRequest(items.get(0));
            page.setSkip(true);
        }else if(url.startsWith("https://item.jd.com/")){
            //商品信息页
            //https://item.jd.com/12353915.html
            log.info("处理商品信息页["+url+"]...");
            //从URL中提取商品的SKU
            String sku = url.replace("https://item.jd.com/", "").replace(".html", "");
            Book book = new Book();
            book.setId(sku);
            String bookTitle = page.getHtml().xpath("//div[@id='product-intro']//" +
                    "div[@id='itemInfo']//div[@class='sku-name']/text()").get();
            book.setTitle(bookTitle);
            List<String> authors = page.getHtml().xpath("//div[@id='product-intro']//" +
                    "div[@id='itemInfo']//div[@class='p-author']/a/text()").all();
            //作者信息中有重复的
            Set<String> authorset = new HashSet<>(authors);
            book.setAuthors(Arrays.asList(authorset.toArray(new String[]{})));
            String price = page.getHtml().xpath("//div[@id='product-intro']//" +
                    "div[@id='itemInfo']//div[@id='summary-price']//strong[@class='p-price']/text()").get();
            if(price==null||"".equalsIgnoreCase(price)){
                log.warn("在商品信息页中，不存在价格数据");
            }else{
                log.error("商品信息页中，存在价格数据");
                book.setPrice(price);
            }
            page.putField("BOOK_INFO", book);
            //价格信息来自于Ajax请求，通过网络流量分析，得知页面发出的请求URL如下：
            // https://p.3.cn/prices/mgets?type=1&area=1_2901_55560_0&pdtk=&pduid=158398828222991797434&pdpin=biore&pdbp=0&skuIds=J_12353915,J_12250414,J_12550531,J_12359944,J_12185501,J_12458548&ext=11100000&callback=jQuery6885614&_=1584329376829
            //经过反复试验，上述URL可以被简化为：
            // https://p.3.cn/prices/mgets?skuIds=J_12353915
            // 返回消息如下：[{"cbf":"0","id":"J_12353915","m":"79.80","op":"73.40","p":"73.40"}]
            page.addTargetRequest(String.format("https://p.3.cn/prices/mgets?skuIds=J_%s", sku));
        }else if(url.startsWith("https://p.3.cn/prices/mgets")){
            log.info("处理商品价格页["+url+"]...");
            String json = page.getRawText();
            List<Price> prices = JSONArray.parseArray(json, Price.class);
            if(prices ==null|| prices.size()!=1){
                log.error("无法解析价格信息:URL="+url+", 消息内容="+json);
                page.setSkip(true);
            }else{
                log.info("提取到价格信息:"+prices.get(0).getP());
                page.putField("PRICE_INFO", prices.get(0));
            }
        }
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
