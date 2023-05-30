package com.cbz.librarymanagementsystem.template;

import com.cbz.librarymanagementsystem.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询模板，封装查询页面的条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTemplate {

    private Book book;

    //页的大小
    private Integer pageSize;

    //起始索引
    private Integer beginIdx;

}
