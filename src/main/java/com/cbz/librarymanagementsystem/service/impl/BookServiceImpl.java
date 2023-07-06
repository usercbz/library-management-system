package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.mapper.BookMapper;
import com.cbz.librarymanagementsystem.service.IBookService;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cbz.librarymanagementsystem.utils.RedisConst.*;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Autowired
    private CollectBookServiceImpl collectBookService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

    private final Set<String> redisKeySet = new HashSet<>();

    @Override
    public Result queryTableData(QueryTemplate queryTemplate) {

        Book book = queryTemplate.getBook();
        //查询缓存
        String key;
        if (book != null) {
            StringJoiner stringJoiner = new StringJoiner(",");
            key = stringJoiner.add(book.getAuthor()).add(book.getName()).add(book.getType()).add(String.valueOf(book.getStatus())).toString();
        } else {
            key = "null";
        }
        key = key + "," + queryTemplate.getBeginIdx();

        String jsonBooks = redisTemplate.opsForValue().get(QUERY_BOOKS_KEY_PRE + key);

        HashMap<String, Object> map;

        if (jsonBooks != null) {
            try {
                map = jsonMapper.readValue(jsonBooks, HashMap.class);
                return Result.succeed(map);
            } catch (JsonProcessingException e) {
                return Result.fail("服务器异常");
            }
        }
        //获取总条数
        Integer total = baseMapper.queryTotal(book);

        //获取数据
        List<Book> books = baseMapper.queryBookAndLimit(queryTemplate);

        //封装结果集
        map = new HashMap<>();
        map.put("total", total);
        map.put("books", books);

        try {
            redisTemplate.opsForValue().set(QUERY_BOOKS_KEY_PRE + key, jsonMapper.writeValueAsString(map), QUERY_BOOKS_TTL, TimeUnit.HOURS);
            //添加到列表
            redisKeySet.add(QUERY_BOOKS_KEY_PRE + key);
        } catch (JsonProcessingException e) {
            return Result.fail("服务器异常");
        }
        return Result.succeed(map);

    }

    @Override
    public Result deleteBooksById(Integer bookId) {

        //判断用户权限
        if (UserHolder.getUser().getPermission() != 1) {
            return Result.fail("权限不足");
        }

        //删除用户收藏表中的数据
        collectBookService.deleteCollectBookByBookId(null, bookId);

        //删除该书籍
        if (removeById(bookId)) {
            //删除缓存
            deleteRedisCache();
            redisTemplate.delete(QUERY_BOOK_KEY_PRE + bookId);
            return Result.succeed(null);
        }

        return Result.fail("删除失败");
    }

    @Override
    public Result addBook(Book book) {

        if (save(book)) {
            //删除缓存
            redisTemplate.delete(redisKeySet);
            redisTemplate.delete(QUERY_LIST_KEY);
            return Result.succeed(null);
        }
        return Result.fail("添加失败");
    }

    @Override
    public Result updateBook(Book book) {
        if (updateById(book)) {
            //删除缓存
            deleteRedisCache();
            redisTemplate.delete(QUERY_BOOK_KEY_PRE + book.getId());
            return Result.succeed(null);
        }
        return Result.fail("修改失败");
    }

    @Override
    public Result getAllBooks() {
        //查询缓存
        String listJson = redisTemplate.opsForValue().get(QUERY_LIST_KEY);

        List<Book> list;
        if (listJson != null) {
            try {
                list = jsonMapper.readValue(listJson, List.class);
                return Result.succeed(list);
            } catch (JsonProcessingException e) {
                return Result.fail("服务器异常");
            }
        }

        list = list();
        try {
            redisTemplate.opsForValue().set(QUERY_LIST_KEY, jsonMapper.writeValueAsString(list), QUERY_LIST_TTL, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            return Result.fail("服务器异常");
        }
        return Result.succeed(list);
    }

    @Override
    public Result getBookById(Integer bookId) {
        //先查询redis  key = query:book:id
        String key = QUERY_BOOK_KEY_PRE + bookId;
        String bookJson = redisTemplate.opsForValue().get(key);
        Book book;
        //查询成功有数据， 直接返回
        if (bookJson != null) {
            try {
                book = jsonMapper.readValue(bookJson, Book.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Result.fail("服务器异常");
            }
            return Result.succeed(book);
        }

        //未查询到数据
        //根据id查询数据库
        book = getById(bookId);
        if (book == null) {
            //未查询到数据
            return Result.fail("该书籍不存在！");
        }
        //成功 查询到数据，存入redis并返回数据
        //缓存
        String jsonStr;
        try {
            jsonStr = jsonMapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Result.fail("服务器异常");
        }
        redisTemplate.opsForValue().set(key, jsonStr, QUERY_BOOK_TTL, TimeUnit.HOURS);
        //返回
        return Result.succeed(book);
    }

    private void deleteRedisCache() {
        redisTemplate.delete(redisKeySet);
        redisTemplate.delete(collectBookService.getRedisKeyList());
        redisTemplate.delete(QUERY_LIST_KEY);
    }
}
