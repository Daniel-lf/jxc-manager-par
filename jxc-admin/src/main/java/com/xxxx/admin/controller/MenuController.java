package com.xxxx.admin.controller;


import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.Menu;
import com.xxxx.admin.service.IMenuService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @RequestMapping("queryAllMenus")
    @ResponseBody
    public List<TreeDto> queryAllMenus(Integer roleId){
        return menuService.queryAllMenus(roleId);
    }

    /**
     * 菜单主页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "menu/menu";
    }

    /**
     * 菜单列表查询接口
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> menuList(){
        return menuService.menuList();
    }

    @RequestMapping("addMenuPage")
    public String addMenuPage(Integer grade, Integer pId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("pId",pId);
        return "menu/add";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return RespBean.success("菜单记录添加成功！");
    }

    @RequestMapping("udpateMenuPage")
    public String updateMenuPage(Integer id,Model model){
        model.addAttribute("menu",menuService.getById(id));
        return "menu/update";
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return RespBean.success("菜单记录更新成功！");
    }

    /**
     * 菜单删除
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteMenu(Integer id){
        menuService.deleteMenuById(id);
        return RespBean.success("菜单删除成功！");
    }

}
