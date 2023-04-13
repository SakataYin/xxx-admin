package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "Exam对象", description = "")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer courseId;

    private Integer duration;
    @ApiModelProperty("考试名称")
    private String name;
    @ApiModelProperty("开始时间")
    private String time;
    @ApiModelProperty("老师")
    private String teacher;
    @ApiModelProperty("考试状态")
    private String status;
    @TableField(exist = false)
    private Boolean enable;




}
