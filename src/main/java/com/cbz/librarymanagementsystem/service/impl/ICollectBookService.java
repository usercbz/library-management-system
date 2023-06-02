package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.CollectBook;

public interface ICollectBookService extends IService<CollectBook> {
    Result addBookToCollect(Integer bookId);

    Result queryMyCollectBooks();

    Result deleteBookToCollect(Integer bookId);

}
