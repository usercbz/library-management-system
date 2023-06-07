package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.CollectBook;

public interface ICollectBookService extends IService<CollectBook> {
    Result addBookToCollect(Integer bookId);

    Result queryMyCollectBooks();

    Result deleteBookToCollect(Integer bookId);

}
