package cn.edu.bistu.cs.ir.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 基于OkHttp设计的用于下载网络上指定页面对应的HTML代码的工具类
 * @author chenruoyu
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);


    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36";

    /**
     * 获取指定URL对应的页面内容
     * @param url 要访问的URL地址
     * @return 获取到的页面内容
     */
    public static String getPage(String url){
        if(StringUtil.isEmpty(url)){
            log.error("请求URL不可以为空");
            return null;
        }
        Request.Builder rBuilder = new Request.Builder().url(url);
        rBuilder.header("User-Agent", DEFAULT_USER_AGENT);
        //设置默认获取简体中文内容
        rBuilder.header("Accept-Language", "zh-CN,zh-Hans,en-US;q=0.9");
        Request request = rBuilder.build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        try (Response response = call.execute()){
            if(response.isSuccessful()){
                log.debug("请求URL[{}]成功", url);
                ResponseBody body = response.body();
                if(body!=null){
                    return body.string();
                }else{
                    log.error("请求URL[{}]成功，但响应体为空", url);
                    return null;
                }
            }else{
                log.error("请求URL[{}]失败", url);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("请求URL[{}]时出现错误：[{}]", url, e.getMessage());
            return null;
        }
    }

    public static String postJson(String url, String body, List<Map.Entry<String, String>> headers){
        if(StringUtil.isEmpty(url)){
            log.error("请求URL不可以为空");
            return null;
        }
        Request.Builder rBuilder = new Request.Builder().url(url);
        if(StringUtil.isEmpty(body)){
            log.warn("POST请求的请求体参数为空");
            rBuilder.post(RequestBody.create("", MediaType.parse("application/json")));
        }else{
            rBuilder.post(RequestBody.create(body, MediaType.parse("application/json")));
        }
        rBuilder.header("User-Agent", DEFAULT_USER_AGENT);
        //设置默认获取简体中文内容
        rBuilder.header("Accept-Language", "zh-CN,zh-Hans,en-US;q=0.9");
        if(headers!=null&&!headers.isEmpty()){
            for(Map.Entry<String, String> header: headers){
                rBuilder.header(header.getKey(), header.getValue());
            }
        }
        Request request = rBuilder.build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        try (Response response = call.execute()){
            if(response.isSuccessful()){
                log.debug("请求URL[{}]成功", url);
                ResponseBody b = response.body();
                if(b!=null){
                    return b.string();
                }else{
                    log.error("请求URL[{}]成功，但响应体为空", url);
                    return null;
                }
            }else{
                log.error("请求URL[{}]失败", url);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("请求URL[{}]时出现错误：[{}]", url, e.getMessage());
            return null;
        }
    }
}
