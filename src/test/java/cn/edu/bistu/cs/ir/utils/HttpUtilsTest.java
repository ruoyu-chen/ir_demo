package cn.edu.bistu.cs.ir.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {HttpUtils.class})
@ExtendWith(SpringExtension.class)
public class HttpUtilsTest {

    @Test
    public void getPageTest(){
        String url = "https://www.cnblogs.com/tencent-cloud-native/ajax/blogStats";
        String page = HttpUtils.getPage(url);
        Assertions.assertNotNull(page);
        System.out.println(page);
    }

    @Test
    public void postJsonTest(){
        String url = "https://www.cnblogs.com/tencent-cloud-native/ajax/GetPostStat";
        String page = HttpUtils.postJson(url, "[15751705,14913423]", null);
        Assertions.assertNotNull(page);
        System.out.println(page);
    }
}
