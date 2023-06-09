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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
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

    private final ArrayList<String> redisKeyList = new ArrayList<>();

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

        String jsonBooks = redisTemplate.opsForValue().get(QUERY_BOOK_KEY_PRE + key);

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
            redisTemplate.opsForValue().set(QUERY_BOOK_KEY_PRE + key, jsonMapper.writeValueAsString(map), QUERY_BOOK_TTL, TimeUnit.HOURS);
            //添加到列表
            redisKeyList.add(QUERY_BOOK_KEY_PRE + key);
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
        if (!collectBookService.deleteCollectBookByBookId(null, bookId)) {
            return Result.fail("删除失败");
        }

        //删除该书籍
        if (removeById(bookId)) {
            //删除缓存
            deleteRedisCache();
            return Result.succeed(null);
        }

        return Result.fail("删除失败");
    }

    @Override
    public Result addBook(Book book) {
        if (save(book)) {
            //删除缓存
            redisTemplate.delete(redisKeyList);
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
            redisTemplate.opsForValue().set(QUERY_LIST_KEY, jsonMapper.writeValueAsString(list),QUERY_LIST_TTL,TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            return Result.fail("服务器异常");
        }
        return Result.succeed(list);
    }

    private void deleteRedisCache(){
        redisTemplate.delete(redisKeyList);
        redisTemplate.delete(collectBookService.getRedisKeyList());
        redisTemplate.delete(QUERY_LIST_KEY);
    }
}
