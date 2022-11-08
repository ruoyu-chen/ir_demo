package cn.edu.bistu.cs.ir.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogStats {

    private int postId;

    /**
     * 阅读数
     */
    private int viewCount;

    /**
     * 评论数
     */
    private int feedbackCount;

    /**
     * 点赞数
     */
    private int diggCount;

    /**
     * 踩
     */
    private int buryCount;
}
