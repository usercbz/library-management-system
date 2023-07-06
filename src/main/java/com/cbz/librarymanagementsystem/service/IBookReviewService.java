package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.BookReview;

public interface IBookReviewService extends IService<BookReview> {
    Result queryReviewsByBookId(Integer bookId);

    Result addReview(BookReview bookReview);
}
