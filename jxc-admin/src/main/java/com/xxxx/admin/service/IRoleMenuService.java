package com.xxxx.admin.service;

import com.xxxx.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    List<Integer> queryRoleHasAllMenusByRoleId(Integer roleId);
}
