package com.cbz.librarymanagementsystem;

import com.cbz.librarymanagementsystem.dto.UserDTO;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.mapper.BookMapper;
import com.cbz.librarymanagementsystem.mapper.CollectBookMapper;
import com.cbz.librarymanagementsystem.mapper.UserMapper;
import com.cbz.librarymanagementsystem.service.impl.UserServiceImpl;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import com.cbz.librarymanagementsystem.utils.BeanUtils;
import com.cbz.librarymanagementsystem.utils.DefaultMailMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

@SpringBootTest
class LibraryManagementSystemApplicationTests {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectBookMapper collectBookMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
        QueryTemplate temp = new QueryTemplate();
        Book book = new Book();
        book.setStatus(0);
        temp.setBook(book);
        temp.setBeginIdx(1);
        temp.setPageSize(1);
        System.out.println(bookMapper.queryBookAndLimit(temp));
    }


    @Test
    void testUUID() {
        System.out.println(UUID.randomUUID());
    }

    @Test
    void testJson() {
        System.out.println(objectMapper);
    }

    @Test
    void testCollectMapper() {
        System.out.println(collectBookMapper.queryAllByUserId(1));
    }

    @Test
    void testSendMail() {

        javaMailSender.send(new DefaultMailMessage("3288316494@qq.com", "Java发送邮件2", "你好，这是一条用于测试Spring Boot邮件发送功能的邮件2！")
        );
    }

    @Test
    void testMapToJson() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        ArrayList<User> users = new ArrayList<>();
        Collections.addAll(users,new User(),new User(),new User());
        hashMap.put("total","1");
        hashMap.put("users",users);

        String json = objectMapper.writeValueAsString(hashMap);
        System.out.println(json);

        HashMap hashMap1 = objectMapper.readValue(json, HashMap.class);

        String total = (String) hashMap.get("total");

        List<User> users1 = (List<User>) hashMap.get("users");

        System.out.println(total);
        System.out.println(users1);
    }
}
