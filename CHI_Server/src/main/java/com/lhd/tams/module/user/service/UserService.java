package com.lhd.tams.module.user.service;


import com.lhd.tams.module.user.domain.User;

public interface UserService {
    /**
     * 登录业务逻辑
     * @param teacherName 账户名
     * @param password 密码
     * @return
     */
    User loginService(String teacherName, String password);

    /**
     * 注册业务逻辑
     * @param user 要注册的User对象，属性中主键id要为空，若id不为空可能会覆盖已存在的user
     * @return
     */
    User registerService(User user);
}
