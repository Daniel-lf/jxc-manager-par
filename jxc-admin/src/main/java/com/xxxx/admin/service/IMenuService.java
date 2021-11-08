package com.xxxx.admin.service;

import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
public interface IMenuService extends IService<Menu> {

    List<TreeDto> queryAllMenus(Integer roleId);

}
