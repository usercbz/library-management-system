package com.cbz.librarymanagementsystem.controller;


import com.cbz.librarymanagementsystem.service.impl.BookServiceImpl;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@CrossOrigin(originPatterns = "http://127.0.0.1:5500", allowCredentials = "true")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @PostMapping("/data")
    public Result getTableData(@RequestBody QueryTemplate queryTemplate){

        return bookService.queryTableData(queryTemplate);
    }
}
