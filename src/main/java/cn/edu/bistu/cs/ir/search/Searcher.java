package cn.edu.bistu.cs.ir.search;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 基于Lucene的索引检索类
 * @author ruoyuchen
 */
public class Searcher {

    private static final Logger log = Logger.getLogger(Searcher.class);

    private IndexSearcher searcher;

    private DirectoryReader reader;


    public Searcher(String dir){
        try {
            this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(dir)));
            this.searcher = new IndexSearcher(reader);
            log.info("IndexSearcher初始化成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IndexSearcher初始化失败");
        }
    }

    public int getDocCounts(String field){
        if(reader!=null){
            try {
                return reader.getDocCount(field);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("读取索引过程中出现异常");
                return -1;
            }
        }
        return -1;
    }

    public void destroy(){
        if(this.reader!=null){
            try {
                this.reader.close();
                log.info("关闭搜索服务");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
