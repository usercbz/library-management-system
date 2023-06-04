package com.cbz.librarymanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.entity.CollectBook;

import java.util.List;

public interface CollectBookMapper extends BaseMapper<CollectBook> {

    List<Book> queryAllByUserId(Integer userId);

    boolean deleteByUserId(Integer userId);
}
