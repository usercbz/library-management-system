package com.cbz.librarymanagementsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 评论实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookReview {
    //id
    @TableId
    private Integer id;
    //用户姓名
    private String username;
    //书籍id
    private Integer bookId;
    //信息
    private String info;

    //时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Timestamp time;
}
