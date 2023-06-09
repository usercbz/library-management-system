package com.cbz.librarymanagementsystem.controller;


import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.service.impl.BookServiceImpl;
import com.cbz.librarymanagementsystem.service.impl.CollectBookServiceImpl;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CollectBookServiceImpl collectBookService;

    @GetMapping("all")
    public Result getAllBooks() {

        return bookService.getAllBooks();
    }

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

    @PutMapping("/add")
    public Result addBook(@RequestBody Book book) {
        //判断用户权限
        if (UserHolder.getUser().getPermission() != 1){
            return Result.fail("权限不足");
        }
        return bookService.addBook(book);
    }

    @DeleteMapping("/remove/{bookId}")
    public Result removeBook(@PathVariable Integer bookId) {
        return bookService.deleteBooksById(bookId);
    }

    @PostMapping("/update")
    public Result updateBookData(@RequestBody Book book) {
        //判断用户权限
        if (UserHolder.getUser().getPermission() != 1){
            return Result.fail("权限不足");
        }
        return bookService.updateBook(book);
    }
}
