package com.xxxx.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 系统主页面
     *
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    /**
     * 用户退出
     *
     * @return
     */
    @RequestMapping("signout")
    public String signout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:index";
    }
}
