package com.cbz.librarymanagementsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user_collect")
public class CollectBook {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer bookId;
}
