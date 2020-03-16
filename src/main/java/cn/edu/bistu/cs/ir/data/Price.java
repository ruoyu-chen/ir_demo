package cn.edu.bistu.cs.ir.data;

/**
 * 用于对京东商品价格信息进行反序列化的Java Bean
 * @author ruoyuchen
 */
public class Price {
    //{"cbf":"0","id":"J_12353915","m":"79.80","op":"73.40","p":"73.40"}

    private String cbf;

    private String id;

    private String m;

    private String op;

    private String p;

    public String getCbf() {
        return cbf;
    }

    public void setCbf(String cbf) {
        this.cbf = cbf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
