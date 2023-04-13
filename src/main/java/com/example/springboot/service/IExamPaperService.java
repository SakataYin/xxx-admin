package com.example.springboot.service;

import com.example.springboot.entity.ExamPaper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-04-10
 */
public interface IExamPaperService extends IService<ExamPaper> {

    void addPaper(ExamPaper examPaper);
}
