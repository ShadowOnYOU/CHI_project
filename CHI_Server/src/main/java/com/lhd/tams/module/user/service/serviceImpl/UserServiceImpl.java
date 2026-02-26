package com.lhd.tams.module.user.service.serviceImpl;

import com.lhd.tams.module.user.domain.User;
import com.lhd.tams.module.user.repository.UserDao;
import com.lhd.tams.module.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User loginService(String teacherName, String password) {
        // 如果账号密码都对则返回登录的用户对象，若有一个错误则返回null
        User user = userDao.findByTeacherNameAndPassword(teacherName, password);
        // 重要信息置空
        if(user != null){
            user.setPassword("");
        }
        return user;
    }

    @Override
    public User registerService(User user) {
        //当新用户的用户名已存在时

        if(userDao.findByTeacherName(user.getTeacherName())!=null){
            // 无法注册
            return null;
        }else{
            //返回创建好的用户对象(带id)
            User newUser = userDao.save(user);
            if(newUser != null){
                newUser.setPassword("");
            }
            return newUser;
        }
    }
}
