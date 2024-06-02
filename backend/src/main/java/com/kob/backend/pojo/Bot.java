package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {
    @TableId(type = IdType.AUTO) //主键自增
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Integer rating;

    //定义日期表示的格式
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private Date createtime;
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    private Date modifytime;
}
