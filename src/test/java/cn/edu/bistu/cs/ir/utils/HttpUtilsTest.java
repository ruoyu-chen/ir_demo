package cn.edu.bistu.cs.ir.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {HttpUtils.class})
@ExtendWith(SpringExtension.class)
public class HttpUtilsTest {

    @Test
    public void getPageTest(){
        String url = "https://www.cnblogs.com/tencent-cloud-native/ajax/blogStats";
        HttpUtils httpUtils = new HttpUtils();
        String page = httpUtils.getPage(url, null);
        Assertions.assertNotNull(page);
        System.out.println(page);
    }

    @Test
    public void postJsonTest(){
        String url = "https://www.cnblogs.com/tencent-cloud-native/ajax/GetPostStat";
        HttpUtils httpUtils = new HttpUtils();
        String page = httpUtils.postJson(url, "[16276201]", null);
        Assertions.assertNotNull(page);
        System.out.println(page);
    }


    @Test
    public void getJDPriceTest(){
        String skuid = "100033901461";
        String url = String.format("https://fts.jd.com/prices/mgets?skuIds=J_%s&source=pc-item", skuid);
        HttpUtils httpUtils = new HttpUtils(false);
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer","https://item.jd.com/");
        String page = httpUtils.getPage(url, headers);
        Assertions.assertNotNull(page);
        System.out.println(page);
    }
}