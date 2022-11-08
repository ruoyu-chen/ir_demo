package cn.edu.bistu.cs.ir.utils;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于OkHttp设计的用于下载网络上指定页面对应的HTML代码的工具类
 * @author chenruoyu
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);


    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36";

    private final OkHttpClient client;


    public HttpUtils(){
        this(false);
    }

    public HttpUtils(boolean withCookie){
        if(withCookie){
            //需要Cookie管理
            Map<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
            client = new OkHttpClient.Builder()
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                            // 从响应头中保存 cookie
                            cookieStore.put(httpUrl.host(), list);
                        }

                        @NotNull
                        @Override
                        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                            // 添加 cookie
                            List<Cookie> cookies = cookieStore.get(httpUrl.host());
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
                    .build();
        }else{
            //不需要Cookie管理
            client = new OkHttpClient();
        }
    }
    /**
     * 获取指定URL对应的页面内容
     * @param url     要访问的URL地址
     * @param headers http请求头，以键值对的形式提供
     * @return 获取到的页面内容
     */
    public String getPage(String url, Map<String, String> headers){
        if(StringUtil.isEmpty(url)){
            log.error("请求URL不可以为空");
            return null;
        }
        Request.Builder rBuilder = new Request.Builder().url(url);
        addHeaders(rBuilder, headers);
        Request request = rBuilder.build();
        return getResponse(request, url);
    }

    public String postJson(String url, String body, Map<String, String> headers){
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
        addHeaders(rBuilder, headers);
        Request request = rBuilder.build();
        return getResponse(request, url);
    }

    private void addHeaders(Request.Builder rBuilder, Map<String, String> headers){
        //设置默认的 User-Agent
        rBuilder.header("User-Agent", DEFAULT_USER_AGENT);
        //设置默认获取简体中文内容
        rBuilder.header("Accept-Language", "zh-CN,zh-Hans,en-US;q=0.9");
        //添加额外的headers
        if(headers!=null&&!headers.isEmpty()){
            for(String header: headers.keySet()){
                rBuilder.header(header, headers.get(header));
            }
        }
    }

    private String getResponse(Request request, String url){
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
