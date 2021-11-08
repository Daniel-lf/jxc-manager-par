package com.xxxx.admin.controller;


import com.xxxx.admin.dto.RoleQuery;
import com.xxxx.admin.pojo.Role;
import com.xxxx.admin.pojo.User;
import com.xxxx.admin.service.IRoleService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @RequestMapping("index")
    public String index() {
        return "role/role";
    }

    /**
     * 查询角色列表
     *
     * @param roleQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> roleList(RoleQuery roleQuery) {
        return roleService.roleList(roleQuery);
    }

    /**
     * 添加和更新角色页面
     * todo:和用户添加同样的问题，这里角色的id是怎么从页面获取到的
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Model model, Integer id) {
        if (null != id) {
            model.addAttribute("role", roleService.getById(id));
        }
        return "role/add_update";
    }

    /**
     * 用户角色记录添加
     *
     * @param role
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveRole(Role role) {
        roleService.saveRole(role);
        return RespBean.success("用户记录添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean udpateRole(Role role) {
        roleService.updateRole(role);
        return RespBean.success("用户记录更新成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteRole(Integer id){
        roleService.delete(id);
        return RespBean.success("角色记录删除成功！");
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public RespBean addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return RespBean.success("角色记录授权成功！");
    }

}
