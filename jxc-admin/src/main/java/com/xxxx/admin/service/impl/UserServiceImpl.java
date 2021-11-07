package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.dto.UserQuery;
import com.xxxx.admin.mapper.UserMapper;
import com.xxxx.admin.pojo.User;
import com.xxxx.admin.service.IUserService;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import com.xxxx.admin.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private PasswordEncoder passwordEncoder;
    /**
     * 用户登录方法
     *
     * @param userName
     * @param password
     * @return
     *//*
    @Override
    public User login(String userName, String password) {
        AssertUtil.isTrue(StringUtil.isEmpty(userName), "用户名不能为空!");
        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空!");
        User user = findUserByUserName(userName);
        AssertUtil.isTrue(null == user, "该用户记录不存在或已注销!");
        AssertUtil.isTrue(!(user.getPassword().equals(password)), "密码错误!");
        return user;
    }*/

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
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()), "用户名不能为空!");
        User tempUser = findUserByUserName(user.getUsername());
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
        AssertUtil.isTrue(!(passwordEncoder.matches(oldPassword, user.getPassword())), "原始密码输入错误！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码输入不一致!");
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码与原始密码不能一致!");
        user.setPassword(passwordEncoder.encode(newPassword));
        AssertUtil.isTrue(!(this.updateById(user)), "用户密码更新失败!");
    }

    @Override
    public Map<String, Object> userList(UserQuery userQuery) {
        IPage<User> page = new Page<User>(userQuery.getPage(), userQuery.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotBlank(userQuery.getUserName())) {
            queryWrapper.like("user_name", userQuery.getUserName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        return PageReusltUtil.getResult(page.getTotal(),page.getRecords());
    }

    /**
     * 用户添加
     *
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()), "用户名不能为空！");
        AssertUtil.isTrue(null != this.findUserByUserName(user.getUsername()), "用户名已存在！");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setIsDel(0);
        AssertUtil.isTrue(!(this.save(user)), "用户记录添加失败！");
    }

    /**
     * 用户更新··
     *
     * @param user
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()), "用户名不能为空!");
        User temp = this.findUserByUserName(user.getUsername());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(user.getId())), "用户名已存在！");
        AssertUtil.isTrue(!(this.updateById(user)), "用户记录更新失败！");
    }

    /**
     * 用户删除
     *
     * @param ids
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(String[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的记录id!");
        ArrayList<User> users = new ArrayList<>();
        for (String id : ids) {
            User temp = this.getById(id);
            temp.setIsDel(1);
            users.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(users)), "用户记录删除失败！");
    }


}
