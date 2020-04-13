package cn.edu.bistu.cs.ir;

import cn.edu.bistu.cs.ir.data.News;
import cn.edu.bistu.cs.ir.search.Indexer;
import cn.edu.bistu.cs.ir.search.Searcher;
import org.apache.log4j.Logger;

public class SearchMain {

    private static final Logger log = Logger.getLogger(SearchMain.class);


    public static void main(String[] args){
        //开始执行索引构建工作
        String dir = "/Users/ruoyuchen/Downloads/lucene";
        Indexer indexer = new Indexer(dir);
        News news = new News();
        news.setTitle("见证：42000多张照片 记住英雄面孔");
        news.setUrl("https://new.qq.com/omn/TWF20200/TWF2020041300303000.html");
        news.setContent("一场突如其来的疫情让医护人员成为最美逆行者。" +
                "为了让人们记住救治一线的生动面孔，2月21日，" +
                "中国摄影家协会主席、《人民日报》摄影记者李舸带领摄影团队奋战40多天，" +
                "终于在武汉解封前拍摄完成42000多名驰援湖北的医护人员脱下口罩瞬间的照片。" +
                "42000多张照片，是共和国抗击疫情的国家影像档案。" +
                "历史永远铭记英雄的面孔。");
        news.setHtml("<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\" dir=\"ltr\">\n" +
                "  <head>\n" +
                "    <title>见证：42000多张照片 记住英雄面孔_腾讯新闻</title>\n" +
                "    <meta name=\"keywords\" content=\"见证：42000多张照片 记住英雄面孔,见证\">\n" +
                "    <meta name=\"description\" content=\"一场突如其来的疫情让医护人员成为最美逆行者。为了让人们记住救治一线的生动面孔，2月21日，中国摄影家协会主席、《人民日报》摄影记者李舸带领摄影团队奋战40多天，终于在武汉解封前拍摄完成42000多名驰援湖北的医护人员脱下口罩瞬间的照片。42000多张照片，是共和国抗击疫情的国家影像档案。历史永远铭记英雄的面孔。\">\n" +
                "    <meta name=\"apub:time\" content=\"2020-4-13 12:22:15\">\n" +
                "    <meta name=\"apub:from\" content=\"default\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\" />\n" +
                "<link rel=\"stylesheet\" href=\"//mat1.gtimg.com/pingjs/ext2020/dcom-static/build/static/css/static.css\" />\n" +
                "<!--[if lte IE 8]><meta http-equiv=\"refresh\" content=\"0; url=/upgrade.htm\"><![endif]-->\n" +
                "<!-- <meta name=\"sogou_site_verification\" content=\"SYWy6ahy7s\"/> -->\n" +
                "<meta name=\"baidu-site-verification\" content=\"jJeIJ5X7pP\" />\n" +
                "<link rel=\"shortcut icon\" href=\"//mat1.gtimg.com/www/icon/favicon2.ico\" />\n" +
                "<link rel=\"stylesheet\" href=\"//vm.gtimg.cn/tencentvideo/txp/style/txp_desktop.css\" />\n" +
                "<script src=\"//js.aq.qq.com/js/aq_common.js\"></script>\n" +
                "<script>\n" +
                "    // 判断如果是动态底层不加载此JS逻辑 2020/1/19\n" +
                "    if(location.href.indexOf('rain') === -1){\n" +
                "        (function(){\n" +
                "            var bp = document.createElement('script');\n" +
                "            var curProtocol = window.location.protocol.split(':')[0];\n" +
                "            if (curProtocol === 'https') {\n" +
                "                bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        \n" +
                "            }\n" +
                "            else {\n" +
                "                bp.src = 'http://push.zhanzhang.baidu.com/push.js';\n" +
                "            }\n" +
                "            var s = document.getElementsByTagName(\"script\")[0];\n" +
                "            s.parentNode.insertBefore(bp, s);\n" +
                "        })();\n" +
                "    }\n" +
                "</script>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/configF2017/5df6e3b3.js\" charset=\"utf-8\"></script>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/configF2017/5a978a31.js\" charset=\"utf-8\"></script>\n" +
                "<script>window.conf_dcom = apub_5a978a31 </script><!--[if !IE]>|xGv00|b9dd434ff7daf1071dbd37c03c2cd4a9<![endif]-->\n" +
                "<!--[if !IE]>|xGv00|038d6e161753081e56c192d04873c65c<![endif]-->\n" +
                "    <script>\n" +
                "      window.DATA = {\n" +
                "\t\t\"article_id\": \"20200413003030\",\n" +
                "\t\t\"article_type\": \"0\",\n" +
                "\t\t\"title\": \"见证：42000多张照片 记住英雄面孔\",\n" +
                "\t\t\"abstract\": null,\n" +
                "\t\t\"catalog1\": \"photography\",\n" +
                "\t\t\"catalog2\": \"photography_zuopin\",\n" +
                "\t\t\"introduction\": \"\",\n" +
                "\t\t\"media\": \"长江云\",\n" +
                "\t\t\"media_id\": \"\",\n" +
                "\t\t\"pubtime\": \"2020-04-13 10:27:11\",\n" +
                "\t\t\"comment_id\": \"5070277016\",\n" +
                "\t\t\"tags\": \"见证\",\n" +
                "\t\t\"political\": 0,\n" +
                "\t\t\"artTemplate\": null,\n" +
                "\t\t\"FztCompetition\": null,\n" +
                "\t\t\"FCompetitionName\": null,\n" +
                "\t\t\"FisOriginal\": false,\n" +
                "\t\t\"cms_id\": \"TWF2020041300303000\",\n" +
                "\t\t\"videoArr\": []\n" +
                "}\n" +
                "      \n" +
                "    </script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"TopNav\"></div>\n" +
                "    <div id=\"topAd\"></div>\n" +
                "    <div class=\"qq_conent clearfix\">\n" +
                "      <div class=\"LEFT\">\n" +
                "        <h1>见证：42000多张照片 记住英雄面孔</h1>\n" +
                "        <div class=\"content clearfix\">\n" +
                "          <div id=\"LeftTool\" class=\"LeftTool\"></div>\n" +
                "          <!--内容-->\n" +
                "          <div class=\"content-article\">\n" +
                "            <!--导语-->\n" +
                "            <div class=\"videoPlayerWrap\"></div>\n" +
                "            <script>window.DATA.videoArr.push({\"title\":\"《见证》：42000多张照片 记住英雄面孔\",\"vid\":\"h0033y6yr5a\",\"img\":\"http://inews.gtimg.com/newsapp_ls/0/11578378634_640480/0\",\"desc\":\"视频：《见证》：42000多张照片 记住英雄面孔，时长约5分10秒\"})</script>\n" +
                "            <p class=\"one-p\">一场突如其来的疫情让医护人员成为最美逆行者。为了让人们记住救治一线的生动面孔，2月21日，中国摄影家协会主席、《人民日报》摄影记者李舸带领摄影团队奋战40多天，终于在武汉解封前拍摄完成42000多名驰援湖北的医护人员脱下口罩瞬间的照片。42000多张照片，是共和国抗击疫情的国家影像档案。历史永远铭记英雄的面孔。</p>\n" +
                "            <div id=\"Status\"></div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div id=\"Comment\"></div>\n" +
                "        <div id=\"Recomend\"></div>\n" +
                "      </div>\n" +
                "      <div id=\"RIGHT\" class=\"RIGHT\"></div>\n" +
                "    </div>\n" +
                "    <div id=\"bottomAd\"></div>\n" +
                "    <div id=\"Foot\" class=\"qq_footer\"></div>\n" +
                "    <div id=\"GoTop\"></div>\n" +
                "    <script src=\"//mat1.gtimg.com/libs/jquery/1.12.0/jquery.min.js\"></script>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/dc2017/dist/m_tips/tips.js\" async></script>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/dc2017/publicjs/m/ping.js\" charset=\"gbk\"></script>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/2018/js/check-https-content.js\"></script>\n" +
                "<script>\n" +
                "if(typeof(pgvMain) == 'function'){pgvMain();}\n" +
                "</script>\n" +
                "<script type=\"text/javascript\" src=\"//imgcache.qq.com/qzone/biz/comm/js/qbs.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "var TIME_BEFORE_LOAD_CRYSTAL = (new Date).getTime();\n" +
                "</script>\n" +
                "<script src=\"//ra.gtimg.com/web/crystal/v4.7Beta05Build050/crystal-min.js\" id=\"l_qq_com\" arguments=\"{'extension_js_src':'//ra.gtimg.com/web/crystal/v4.7Beta05Build050/crystal_ext-min.js', 'jsProfileOpen':'false', 'mo_page_ratio':'0.01', 'mo_ping_ratio':'0.01', 'mo_ping_script':'//ra.gtimg.com/sc/mo_ping-min.js'}\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "if(typeof crystal === 'undefined' && Math.random() <= 1) {\n" +
                "  (function() {\n" +
                "    var TIME_AFTER_LOAD_CRYSTAL = (new Date).getTime();\n" +
                "    var img = new Image(1,1);\n" +
                "    img.src = \"//dp3.qq.com/qqcom/?adb=1&dm=new&err=1002&blockjs=\"+(TIME_AFTER_LOAD_CRYSTAL-TIME_BEFORE_LOAD_CRYSTAL);\n" +
                "  })();\n" +
                "}\n" +
                "</script>\n" +
                "<style>.absolute{position:absolute;}</style>\n" +
                "<!--[if !IE]>|xGv00|bfa6be71716f6329ed6738978b6c1e2d<![endif]-->\n" +
                "\n" +
                "<script>\n" +
                "var _mtac = {};\n" +
                "(function() {\n" +
                "    var mta = document.createElement(\"script\");\n" +
                "    mta.src = \"//pingjs.qq.com/h5/stats.js?v2.0.2\";\n" +
                "    mta.setAttribute(\"name\", \"MTAH5\");\n" +
                "    mta.setAttribute(\"sid\", \"500651042\");\n" +
                "    var s = document.getElementsByTagName(\"script\")[0];\n" +
                "    s.parentNode.insertBefore(mta, s);\n" +
                "})();\n" +
                "</script><!--[if !IE]>|xGv00|3f33b9921201aaae8080bb9e9215804d<![endif]-->\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/dcom-static/build/static/js/static.js\"></script>\n" +
                "<!--[if !IE]>|xGv00|4ca6a9f2151663d1bc8e48d5aca0d199<![endif]-->\n" +
                "  </body>\n" +
                "</html><!--[if !IE]>|xGv00|d77e4425955cb7fae3e64e016b0a9196<![endif]-->");
        boolean result = indexer.addNews(news);
        if(result){
            log.info("成功添加文档到索引");
        }else{
            log.error("添加文档到索引失败");
        }
        indexer.destroy();
        Searcher searcher = new Searcher(dir);
        System.out.println("ID:"+searcher.getDocCounts("ID"));
        System.out.println("TITLE:"+searcher.getDocCounts("TITLE"));
        System.out.println("HTML:"+searcher.getDocCounts("HTML"));
        System.out.println("CONTENT:"+searcher.getDocCounts("CONTENT"));
        System.out.println("fieldname:"+searcher.getDocCounts("fieldname"));
        searcher.destroy();
    }
}
