package com.example.springboot.controller;

import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Sign;
import com.example.springboot.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.xml.soap.Detail;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IExamService;
import com.example.springboot.entity.Exam;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {

    @Resource
    private IExamService examService;
    @Resource
    private ISignService signService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Exam exam) {
        if (exam.getId() == null) {
            //exam.setTime(DateUtil.now());
            //exam.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        examService.saveOrUpdate(exam);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        examService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        examService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(examService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(examService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        Page<Exam> page = examService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Exam exam : page.getRecords()) {
            setState(exam);
            //examService.updateById(exam);
        }

        return Result.success(page);
    }

    @GetMapping("/page/front")
    public Result findPageFront(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        Page<Exam> page = examService.page(new Page<>(pageNum, pageSize), queryWrapper);
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("user", currentUser.getUsername());
        }
        //查出当前用户所有报名考试
        List<Sign> signLsit = signService.list(new QueryWrapper<Sign>().eq("user_id", currentUser.getId()));
        for (Exam exam : page.getRecords()) {
            exam.setEnable(false);
            Integer examId = exam.getId();
            //当且仅当，当前用户的报名列表里面有这个考试，而且是审核通过的，我们才能设置开始考试的按钮状态
            signLsit.stream().filter(sign -> sign.getExamId().equals(examId)).findFirst().ifPresent(sign -> {
                exam.setEnable("审核通过".equals(sign.getState()));
            });
            setState(exam);

        }
        return Result.success(page);
    }

    //抽取方法
    private  void setState(Exam exam) {

        String time = exam.getTime();
        DateTime dateTime = DateUtil.parse(time,"yyyy-MM-dd HH:mm");
        Date now = new Date();

        if (DateUtil.compare(now,dateTime)<0){
            exam.setStatus("未开始");
        }else {
            if (DateUtil.compare(now,DateUtil.offsetMinute(dateTime,exam.getDuration()))<=0){
                exam.setStatus("进行中");
            }
            else if (DateUtil.compare(DateUtil.offsetMinute(dateTime,2),now)<0) {
                exam.setStatus("已结束");

            }
        }
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Exam> list = examService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Exam信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Exam> list = reader.readAll(Exam.class);

        examService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

