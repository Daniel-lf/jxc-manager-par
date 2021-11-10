package com.xxxx.admin.service;

import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品类别表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    List<TreeDto> queryAllGoodsTypes(Integer typeId);

    List<Integer> queryAllSubTypeByTypeId(Integer typeId);

    Map<String, Object> goodsTypeList();

    void saveGoodsType(GoodsType goodsType);

    void deleteGoodsType(Integer id);
}
