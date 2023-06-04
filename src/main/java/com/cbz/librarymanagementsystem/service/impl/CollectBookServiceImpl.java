package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.entity.CollectBook;
import com.cbz.librarymanagementsystem.mapper.CollectBookMapper;
import com.cbz.librarymanagementsystem.service.ICollectBookService;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CollectBookServiceImpl extends ServiceImpl<CollectBookMapper, CollectBook> implements ICollectBookService {
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
