package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.BookReview;
import com.cbz.librarymanagementsystem.mapper.BookReviewMapper;
import com.cbz.librarymanagementsystem.service.IBookReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookReviewServiceImpl extends ServiceImpl<BookReviewMapper, BookReview> implements IBookReviewService {
    @Override
    public Result queryReviewsByBookId(Integer bookId) {
        QueryWrapper<BookReview> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId);
        List<BookReview> reviews = list(wrapper);
        return Result.succeed(reviews);
    }

    @Override
    public Result addReview(BookReview bookReview) {
        return save(bookReview) ? Result.succeed(null) : Result.fail("失败！");
    }
}
