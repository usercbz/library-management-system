package com.cbz.librarymanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cbz.librarymanagementsystem.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> getList();
}
