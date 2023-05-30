package com.cbz.librarymanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.template.QueryTemplate;

import java.util.List;

public interface BookMapper extends BaseMapper<Book> {

    List<Book> queryBookAndLimit(QueryTemplate queryTemplate);

    Integer queryTotal(Book book);
}
