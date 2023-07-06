package com.cbz.librarymanagementsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @TableId
    private Integer id;
    private String description;
    private String name;
    private String author;
    private Integer status;
    private String type;
    //图片
    private String imageUrl;
}
