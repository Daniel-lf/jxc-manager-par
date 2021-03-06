package com.xxxx.admin.controller;


import com.xxxx.admin.dto.UserQuery;
import com.xxxx.admin.exception.ParamsException;
import com.xxxx.admin.pojo.User;
import com.xxxx.admin.service.IUserService;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-01
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

   /* @RequestMapping("/login")
    @ResponseBody
    public RespBean login(String userName, String password, HttpSession session) {
        try {
            User user = userService.login(userName, password);
            session.setAttribute("user", user);
            return RespBean.success("用户登录成功!");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("用户登录失败!");
        }
    }*/

   /* @RequestMapping("/login")
    @ResponseBody
    public RespBean login(String userName, String password, HttpSession session) {
        User user = userService.login(userName, password);
        session.setAttribute("user", user);
        return RespBean.success("用户登录成功!");
    }*/

    /**
     * 用户信息设置页面
     *
     * @return
     */
    @RequestMapping("setting")
    public String setting(Principal principal, Model model) {
       /* User user = (User) session.getAttribute("user");
        session.setAttribute("user", userService.getById(user.getId()));*/
        User user = userService.findUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "user/setting";
    }


    @RequestMapping("/updateUserInfo")
    @ResponseBody
    public RespBean updateUserInfo(User user) {
        userService.updateUserInfo(user);
        return RespBean.success("用户信息更新成功!");
    }

    @RequestMapping("password")
    public String password() {
        return "user/password";
    }

    @RequestMapping("updateUserPassword")
    @ResponseBody
    public RespBean updateUserPassword(Principal principal, String oldPassword, String newPassword, String confirmPassword) {
        /*User user = (User) session.getAttribute("user");*/
        userService.updateUserPassword(principal.getName(), oldPassword, newPassword, confirmPassword);
        return RespBean.success("用户密码更新成功!");
    }

    /**
     * 用户管理主页
     *
     * @return
     */
    @RequestMapping("index")
    @PreAuthorize("hasAnyAuthority('1010')")
    public String index() {
        return "user/user";
    }


    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('101003')")
    public Map<String, Object> userList(UserQuery userQuery) {
        return userService.userList(userQuery);
    }


    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdatePage(Integer id, Model model) {
        if (null != id) {
            model.addAttribute("user", userService.getById(id));
        }
        return "user/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveUser(User user) {
        userService.saveUser(user);
        return RespBean.success("用户记录添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean udpateUser(User user) {
        userService.updateUser(user);
        return RespBean.success("用户记录更新成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(String[] ids){
        userService.delete(ids);
        return RespBean.success("用户记录删除成功!");
    }

}
