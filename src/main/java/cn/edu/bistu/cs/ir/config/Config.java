package cn.edu.bistu.cs.ir.config;

import cn.edu.bistu.cs.ir.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 系统配置信息类
 * @author chenruoyu
 */
@ConfigurationProperties(prefix = "irdemo.dir")
@Component
@Getter
@Setter
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);


    /**
     * 系统工作的根目录
     */
    private String home;

    /**
     * 爬虫数据所在的目录
     */
    private String crawler;

    /**
     * 索引数据所在的目录
     */
    private String idx;

    /**
     * 网络爬虫：默认的UserAgent
     */
    private String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Safari/605.1.15";

    /**
     * 网络爬虫：重试次数，默认为3次
     */
    private int retryTimes = 3;

    /**
     * 网络爬虫：两次爬取之间的休眠间隔，以毫秒为单位，默认为5000，即5秒
     */
    private int sleepTime = 2000;

    /**
     * 是否在系统初始化完成后，启动爬虫的执行，
     * 默认为true
     */
    private boolean startCrawler = false;

    @PostConstruct
    public void init(){
        createDir(home);
        createDir(idx);
        createDir(crawler);
    }

    private void createDir(String dir){
        if(!FileUtils.isDirExists(dir)){
            log.info("系统工作目录[{}]不存在，尝试创建", dir);
            try {
                Files.createDirectories(Path.of(dir));
            } catch (IOException e) {
                log.error("无法创建工作目录:[{}]", dir);
                throw new RuntimeException(e);
            }
        }else{
            log.info("系统工作目录[{}]已经存在，可以直接使用", dir);
        }
    }
}
