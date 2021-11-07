package com.xxxx.admin.service;

import com.xxxx.admin.dto.RoleQuery;
import com.xxxx.admin.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询角色列表
     * @param roleQuery
     * @return
     */
    Map<String, Object> roleList(RoleQuery roleQuery);

    void saveRole(Role role);

    void updateRole(Role role);

    Role findRoleByRoleName(String roleName);

    void delete(Integer id);
}
