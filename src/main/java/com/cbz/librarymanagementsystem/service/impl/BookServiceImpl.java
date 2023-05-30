package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.Book;
import com.cbz.librarymanagementsystem.mapper.BookMapper;
import com.cbz.librarymanagementsystem.service.IBookService;
import com.cbz.librarymanagementsystem.template.QueryTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    @Override
    public Result queryTableData(QueryTemplate queryTemplate) {
        //获取总条数
        Integer total = baseMapper.queryTotal(queryTemplate.getBook());

        //获取数据
        List<Book> books = baseMapper.queryBookAndLimit(queryTemplate);

        //封装结果集
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("books",books);

        return Result.succeed(map);
    }
}
