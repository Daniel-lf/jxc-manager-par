package com.xxxx.admin.mapper;

import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-11-07
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<TreeDto> queryAllMenus(Integer roleId);

}
