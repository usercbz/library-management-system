package com.cbz.librarymanagementsystem.controller;


import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.Book;
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

    @GetMapping("all")
    public Result getAllBooks() {
        return Result.succeed(bookService.list());
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
        return bookService.save(book) ? Result.succeed(null) : Result.fail("添加失败");
    }

    @DeleteMapping("/remove/{bookId}")
    public Result removeBook(@PathVariable Integer bookId) {
        return bookService.deleteBooksById(bookId);
    }

    @PostMapping("/update")
    public Result updateBookData(@RequestBody Book book) {

        return bookService.updateById(book) ? Result.succeed(null) : Result.fail("修改失败");
    }
}
