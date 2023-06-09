package com.example.springboot.service.impl;

import com.example.springboot.entity.Sign;
import com.example.springboot.mapper.SignMapper;
import com.example.springboot.service.ISignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-04-12
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

}
