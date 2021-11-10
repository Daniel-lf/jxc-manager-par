package com.xxxx.admin.service;

import com.xxxx.admin.dto.GoodsQuery;
import com.xxxx.admin.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface IGoodsService extends IService<Goods> {

    Map<String, Object> goodsList(GoodsQuery goodsQuery);

    void saveGoods(Goods goods);

    String genGoodsCode();

    void updateGoods(Goods goods);

    void deleteGoods(Integer id);

    /**
     * 更新商品的库存
     * @param goods
     */
    void updateStock(Goods goods);

    void deleteStock(Integer id);
}
