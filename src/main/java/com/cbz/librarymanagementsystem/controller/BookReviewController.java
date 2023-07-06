package com.cbz.librarymanagementsystem.controller;


import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.BookReview;
import com.cbz.librarymanagementsystem.service.impl.BookReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
public class BookReviewController {

    @Autowired
    private BookReviewServiceImpl bookReviewService;

    @GetMapping("/{bookId}")
    public Result getReviews(@PathVariable Integer bookId) {
        return bookReviewService.queryReviewsByBookId(bookId);
    }

    @PutMapping
    public Result addReview(@RequestBody BookReview bookReview){
        return bookReviewService.addReview(bookReview);
    }
}
