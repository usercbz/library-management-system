package com.cbz.librarymanagementsystem;

import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.mapper.BookMapper;
import com.cbz.librarymanagementsystem.mapper.UserMapper;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LibraryManagementSystemApplicationTests {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        QueryTemplate temp =new QueryTemplate();
        Book book = new Book();
        book.setStatus(0);
        temp.setBook(book);
        temp.setBeginIdx(1);
        temp.setPageSize(1);
        System.out.println(bookMapper.queryBookAndLimit(temp));
    }


    @Test
    void testUUID(){
        System.out.println(UUID.randomUUID());
    }

    @Test
    void testJson(){
        System.out.println(objectMapper);
    }

}
