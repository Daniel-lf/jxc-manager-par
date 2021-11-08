package com.xxxx.admin.mapper;

import com.xxxx.admin.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Map<String, Object>> queryAllRole(Integer userId);
}
