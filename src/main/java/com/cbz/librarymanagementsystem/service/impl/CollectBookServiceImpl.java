package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.entity.CollectBook;
import com.cbz.librarymanagementsystem.mapper.CollectBookMapper;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CollectBookServiceImpl extends ServiceImpl<CollectBookMapper, CollectBook> implements ICollectBookService {
    @Override
    public Result addBookToCollect(Integer bookId) {
        CollectBook collectBook = new CollectBook();
        collectBook.setBookId(bookId);
        collectBook.setUserId(UserHolder.getUser().getId());
        return save(collectBook) ? Result.succeed(null) : Result.fail("添加失败");
    }

    @Override
    public Result queryMyCollectBooks() {
        List<Book> books = baseMapper.queryAllByUserId(UserHolder.getUser().getId());
        return Result.succeed(books);
    }

    @Override
    public Result deleteBookToCollect(Integer bookId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", UserHolder.getUser().getId());
        hashMap.put("book_id", bookId);

        return removeByMap(hashMap) ? Result.succeed(null) : Result.fail("取消收藏失败！");
    }
}
