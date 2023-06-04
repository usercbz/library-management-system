package com.cbz.librarymanagementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cbz.librarymanagementsystem.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    List<User> getList();

    boolean updateUsernameById(@Param("id") Integer id, @Param("username") String username);

    boolean updatePasswordById(@Param("id") Integer id, @Param("password") String password);
}
