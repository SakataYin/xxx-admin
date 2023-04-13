package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-04-10
 */
@Getter
@Setter
@TableName("sys_course")
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程名")
    private String courseName;

    private String img;

    @ApiModelProperty("总人数")
    private String courseNum;

    @ApiModelProperty("选课人数")
    private String selectNum;

    @ApiModelProperty("教师")
    private String tname;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("价格")
    private Float price;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("课程描述")
    private String description;


}
