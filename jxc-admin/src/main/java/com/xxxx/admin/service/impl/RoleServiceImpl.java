package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xxxx.admin.dto.RoleQuery;
import com.xxxx.admin.pojo.Role;
import com.xxxx.admin.mapper.RoleMapper;
import com.xxxx.admin.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Override
    public Map<String, Object> roleList(RoleQuery roleQuery) {
        IPage<Role> page = new Page<>(roleQuery.getPage(), roleQuery.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotBlank(roleQuery.getRoleName())) {
            queryWrapper.like("name", roleQuery.getRoleName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageReusltUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()), "角色名不能为空!");
        /**
         * 添加角色名  查询不到  即可添加新的角色
         */
        AssertUtil.isTrue(null != this.findRoleByRoleName(role.getName()), "角色名已存在");
        role.setIsDel(0);
        AssertUtil.isTrue(!(this.save(role)), "角色添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()), "请输入角色名称！");
        Role temp = this.findRoleByRoleName(role.getName());
        /**
         * 更新角色  先查询该角色  如果存在 并且  该要更新的角色的id与已查询到的角色id不同  必须不同才能是别的角色 如果相同就是自己
         */
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getId())), "角色名已存在！");
        AssertUtil.isTrue(!(this.updateById(role)), "角色更新失败!");
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return this.baseMapper.selectOne(new QueryWrapper<Role>().eq("is_del", 0).eq("name", roleName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void delete(Integer id) {
        AssertUtil.isTrue(null == id, "请选择待删除的记录！");
        Role role = this.getById(id);
        AssertUtil.isTrue(null == role, "待删除的记录不存在!");
        role.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(role)), "角色记录删除失败!");
    }
}
