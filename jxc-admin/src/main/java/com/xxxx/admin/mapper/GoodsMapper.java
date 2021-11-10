package com.xxxx.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xxxx.admin.dto.GoodsQuery;
import com.xxxx.admin.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    IPage<Goods> queryGoodsByParams(IPage<Goods> page, @Param("goodsQuery") GoodsQuery goodsQuery);
}
