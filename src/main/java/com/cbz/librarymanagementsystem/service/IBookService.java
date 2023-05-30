package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.template.QueryTemplate;

public interface IBookService extends IService<Book> {
    Result queryTableData(QueryTemplate queryTemplate);
}
