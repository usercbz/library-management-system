package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.template.QueryTemplate;

public interface IBookService extends IService<Book> {
    Result queryTableData(QueryTemplate queryTemplate);

    Result deleteBooksById(Integer bookId);

    Result addBook(Book book);

    Result updateBook(Book book);

    Result getAllBooks();

}
