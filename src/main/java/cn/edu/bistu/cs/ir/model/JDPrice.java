package cn.edu.bistu.cs.ir.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 面向京东商城中，动态渲染的价格信息的Java Bean
 */
@Getter
@Setter
public class JDPrice {
    private String cbf;
    private String id;
    private String m;
    private String op;
    private String p;
}
