package com.cbz.librarymanagementsystem.controller;


import com.cbz.librarymanagementsystem.service.impl.BookServiceImpl;
import com.cbz.librarymanagementsystem.service.impl.CollectBookServiceImpl;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CollectBookServiceImpl collectBookService;


    @PostMapping("/data")
    public Result getTableData(@RequestBody QueryTemplate queryTemplate) {

        return bookService.queryTableData(queryTemplate);
    }

    @PostMapping("/collect/add/{bookId}")
    public Result addBookToCollect(@PathVariable Integer bookId) {

        return collectBookService.addBookToCollect(bookId);
    }

    @DeleteMapping("/collect/remove/{bookId}")
    public Result removeBookToCollect(@PathVariable Integer bookId) {
        return collectBookService.deleteBookToCollect(bookId);
    }

    @GetMapping("/collect")
    public Result getMyCollectBooks() {
        return collectBookService.queryMyCollectBooks();
    }
}
