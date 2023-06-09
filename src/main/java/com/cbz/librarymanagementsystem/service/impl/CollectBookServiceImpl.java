package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.entity.CollectBook;
import com.cbz.librarymanagementsystem.mapper.CollectBookMapper;
import com.cbz.librarymanagementsystem.service.ICollectBookService;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cbz.librarymanagementsystem.utils.RedisConst.QUERY_COLLECT_BOOK_KEY_PRE;
import static com.cbz.librarymanagementsystem.utils.RedisConst.QUERY_COLLECT_BOOK_TTL;

@Service
public class CollectBookServiceImpl extends ServiceImpl<CollectBookMapper, CollectBook> implements ICollectBookService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

    public ArrayList<String> getRedisKeyList() {
        return redisKeyList;
    }

    private final ArrayList<String> redisKeyList = new ArrayList<>();


    @Override
    public Result addBookToCollect(Integer bookId) {

        Integer id = UserHolder.getUser().getId();

        //查询书籍是否已经收藏
        QueryWrapper<CollectBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id).eq("book_id", bookId);
        CollectBook collectBook = getOne(queryWrapper);

        if (collectBook != null) {
            return Result.fail("该书籍已经收藏");
        }

        collectBook = new CollectBook();
        collectBook.setBookId(bookId);
        collectBook.setUserId(id);

        if (save(collectBook)) {
            //删除缓存
            redisTemplate.delete(QUERY_COLLECT_BOOK_KEY_PRE + id);
            return Result.succeed(null);
        }

        return Result.fail("添加失败");
    }

    @Override
    public Result queryMyCollectBooks() {

        Integer id = UserHolder.getUser().getId();
        String key = QUERY_COLLECT_BOOK_KEY_PRE + id;

        //查询redis缓存
        String strBooks = redisTemplate.opsForValue().get(key);
        List<Book> books;

        if (strBooks != null) {
            try {
                //返回
                books = jsonMapper.readValue(strBooks, List.class);
                return Result.succeed(books);
            } catch (JsonProcessingException e) {
                return Result.fail("服务器异常");
            }
        }

        //从数据库查询
        books = baseMapper.queryAllByUserId(id);

        try {
            String jsonBooks = jsonMapper.writeValueAsString(books);
            //存入redis
            redisTemplate.opsForValue().set(key, jsonBooks, QUERY_COLLECT_BOOK_TTL, TimeUnit.HOURS);
            //key列表追加
            redisKeyList.add(key);
        } catch (JsonProcessingException e) {
            return Result.fail("服务器异常");
        }

        return Result.succeed(books);
    }

    @Override
    public Result deleteBookToCollect(Integer bookId) {
        //删除
        if (deleteCollectBookByBookId(bookId)) {
            //删除缓存
            redisTemplate.delete(QUERY_COLLECT_BOOK_KEY_PRE + UserHolder.getUser().getId());
            return Result.succeed(null);
        }

        return Result.fail("取消收藏失败！");
    }


    public boolean deleteCollectBookByBookId(Integer userId, Integer bookId) {

        HashMap<String, Object> hashMap = new HashMap<>();

        if (userId != null) {
            hashMap.put("user_id", userId);
        }
        hashMap.put("book_id", bookId);

        return removeByMap(hashMap);
    }

    private boolean deleteCollectBookByBookId(Integer bookId) {
        return deleteCollectBookByBookId(UserHolder.getUser().getId(), bookId);
    }
}
