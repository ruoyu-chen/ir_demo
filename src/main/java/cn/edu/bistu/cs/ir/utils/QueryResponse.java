package cn.edu.bistu.cs.ir.utils;

/**
 * Rest风格的知识库查询接口响应对象
 * @author chenruoyu
 */
public class QueryResponse<T> {

    /**
     * 工厂方法，用于快速生成访问出现错误时的响应消息
     * @param msg  成功/失败响应消息
     * @return 封装完成的响应对象
     */
    public static synchronized <T> QueryResponse<T> genErr(String msg){
        QueryResponse<T> response = new QueryResponse<>();
        response.setSuccess(false);
        response.setMsg(msg);
        return response;
    }

    /**
     * 工厂方法，用于快速生成访问成功时的响应消息
     * @param msg  成功/失败响应消息
     * @param data 数据对象
     * @return 返回封装好的对象
     */
    public static synchronized <T> QueryResponse<T> genSucc(String msg,
                                                            T data){
        QueryResponse<T> response = new QueryResponse<>();
        response.setSuccess(true);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    /**
     * 请求是否成功
     */
    private boolean success=true;

    /**
     * 请求成功/不成功，返回的提示性消息。
     */
    private String msg=null;

    /**
     *  返回结果中的数据对象，必须是可以被JSON序列化的
     */
    private T data=null;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}