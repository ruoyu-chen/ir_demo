package cn.edu.bistu.cs.ir.data;

/**
 * 面向新闻网站的新闻内容页的Java Bean
 * @author ruoyuchen
 */
public class News {
    //新闻标题
    private String title;
    //新闻页面URL
    private String url;
    //新闻正文文本
    private String content;
    //新闻正文部分的HTML代码
    private String html;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
