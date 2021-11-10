package com.xxxx.admin.mapper;

import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.GoodsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品类别表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    List<TreeDto> queryAllGoodsTypes();

}
