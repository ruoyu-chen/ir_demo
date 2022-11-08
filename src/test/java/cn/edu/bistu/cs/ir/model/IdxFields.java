package cn.edu.bistu.cs.ir.model;

/**
 * 学校信息索引的字段枚举类型
 * @author chenruoyu
 */
public enum IdxFields {
    /**
     * 学校的唯一ID
     */
    ID,
    /**
     * 学校名称
     */
    NAME,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 校长姓名
     */
    PRINCIPAL,
    /**
     * 学校网站
     */
    WEBSITE,
    /**
     * 城市
     */
    CITY,
    /**
     * 学校类型
     */
    TYPE,
    /**
     * 经纬度
     */
    LOCATION,
    /**
     * 纬度，用于存储
     */
    LAT,
    /**
     * 经度，用于存储
     */
    LON,
    /**
     * 学生总数
     */
    ROLL
}
