package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.Menu;
import com.xxxx.admin.mapper.MenuMapper;
import com.xxxx.admin.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public List<TreeDto> queryAllMenus(Integer roleId) {
        //根据roleId查询所有菜单集合
        List<TreeDto> treeDtos= this.baseMapper.queryAllMenus(roleId);
        //根绝roleId查询当前角色所拥有的menuIds
        List<Integer> roleHasMenuIds=roleMenuService.queryRoleHasAllMenusByRoleId(roleId);
        if(CollectionUtils.isNotEmpty(roleHasMenuIds)){
            treeDtos.forEach(treeDto -> {
                if(roleHasMenuIds.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }
}
