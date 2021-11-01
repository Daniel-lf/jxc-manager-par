package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.mapper.UserMapper;
import com.xxxx.admin.pojo.User;
import com.xxxx.admin.service.IUserService;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录方法
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User login(String userName, String password) {
        AssertUtil.isTrue(StringUtil.isEmpty(userName), "用户名不能为空!");
        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空!");
        User user = findUserByUserName(userName);
        AssertUtil.isTrue(null == user, "该用户记录不存在或已注销!");
        AssertUtil.isTrue(!(user.getPassword().equals(password)), "密码错误!");
        return user;
    }

    /**
     * 通过用户名查询User对象
     *
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }


    /**
     * 更新用户信息
     *
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()), "用户名不能为空!");
        User tempUser = findUserByUserName(user.getUserName());
        AssertUtil.isTrue(null != tempUser && !(tempUser.getId().equals(user.getId())), "用户名已存在");
        AssertUtil.isTrue(!(this.updateById(user)), "用户信息更新失败!");
    }

    /**
     * 更改密码
     *
     * @param userName
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        User user = null;
        user = this.findUserByUserName(userName);
        AssertUtil.isTrue(null == user, "用户不存在或未登录!");
        AssertUtil.isTrue(StringUtil.isEmpty(oldPassword), "请输入原始密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword), "请输入新密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(confirmPassword), "请输入确认始密码!");
        AssertUtil.isTrue(!(user.getPassword().equals(oldPassword)), "原始密码输入错误！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码输入不一致!");
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码与原始密码不能一致!");
        user.setPassword(newPassword);
        AssertUtil.isTrue(!(this.updateById(user)), "用户密码更新失败!");
    }
}
