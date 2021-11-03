package com.xxxx.admin.service;

import com.xxxx.admin.dto.UserQuery;
import com.xxxx.admin.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-01
 */
public interface IUserService extends IService<User> {

    /**
     * 用户登录方法
     *
     * @param userName
     * @param password
     * @return
     */
    /*User login(String userName, String password);*/

    /**
     * 通过用户名查询User对象
     *
     * @param userName
     * @return
     */
    User findUserByUserName(String userName);

    /**
     * 更新用户信息
     * @param user
     */
    void updateUserInfo(User user);


    /**
     * 更改密码
     * @param userName
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword);

    /**
     *
     * @param userQuery
     * @return
     */
    Map<String, Object> userList(UserQuery userQuery);
}
