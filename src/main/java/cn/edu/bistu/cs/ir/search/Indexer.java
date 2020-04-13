package cn.edu.bistu.cs.ir.search;

import cn.edu.bistu.cs.ir.data.News;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import org.apache.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 基于Lucene构建的索引类
 * https://lucene.apache.org/
 * @author ruoyuchen
 */
public class Indexer {

    private static final Logger log = Logger.getLogger(Indexer.class);

    private IndexWriter writer;
    /**
     * 构造函数，负责初始化IndexWriter
     * @param dir 索引目录
     */
    public Indexer(String dir){
        HanLPIndexAnalyzer analyzer = new HanLPIndexAnalyzer();
        Directory index;
        try {
            index = FSDirectory.open(Paths.get(dir));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(index, writerConfig);
            log.info("索引初始化完成，索引目录为:"+dir);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("无法初始化索引，请检查提供的索引目录是否可用:"+dir);
            writer = null;
        }
    }

    public boolean addNews(News news){
        if(writer==null||news==null){
            return false;
        }
        Document doc = new Document();
        //News 对象包括 title，url，content，html
        doc.add(new TextField("TITLE", news.getTitle(), Field.Store.YES));
        doc.add(new TextField("CONTENT", news.getContent(), Field.Store.YES));
        doc.add(new StringField("ID", news.getUrl(), Field.Store.YES));
        doc.add(new StoredField("HTML", news.getHtml()));
        try {
            writer.updateDocument(new Term("ID", news.getUrl()), doc);
            writer.commit();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("构建索引失败");
            return false;
        }
    }

    public void destroy(){
        if(writer==null)
            return;
        try {
            log.info("索引关闭");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("尝试关闭索引失败");
        }
    }
}
