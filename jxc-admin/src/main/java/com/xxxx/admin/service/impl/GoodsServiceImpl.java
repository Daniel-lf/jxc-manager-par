package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.admin.dto.GoodsQuery;
import com.xxxx.admin.pojo.Goods;
import com.xxxx.admin.mapper.GoodsMapper;
import com.xxxx.admin.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.service.IGoodsTypeService;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private IGoodsTypeService goodsTypeService;

    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {


        IPage<Goods> page = new Page<Goods>(goodsQuery.getPage(), goodsQuery.getLimit());
        //保证类别有值
        if (null != goodsQuery.getTypeId()) {
            goodsQuery.setTypeIds(goodsTypeService.queryAllSubTypeByTypeId(goodsQuery.getTypeId()));
        }


        page = this.baseMapper.queryGoodsByParams(page, goodsQuery);
        return PageReusltUtil.getResult(page.getTotal(), page.getRecords());
    }


    /**
     * 添加商品接口
     *
     * @param goods
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()), "请指定商品名称！");
        AssertUtil.isTrue(null == goods.getTypeId(), "请指定商品的类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()), "请指定商品的单位！");
        goods.setCode(genGoodsCode());
        goods.setInventoryQuantity(0);
        goods.setState(0);
        goods.setLastPurchasingPrice(0F);
        goods.setIsDel(0);
        AssertUtil.isTrue(!(this.save(goods)), "记录添加失败！");
    }

    @Override
    public String genGoodsCode() {
        String maxGoodsCode = this.baseMapper.selectOne(new QueryWrapper<Goods>().select("max(code) as code")).getCode();
        if (StringUtils.isNotBlank(maxGoodsCode)) {
            Integer code = Integer.valueOf(maxGoodsCode) + 1;
            String codes = code.toString();
            int length = codes.length();
            for (int i = 4; i > length; i--) {
                codes = "0" + codes;
            }
            return codes;
        } else {
            return "0001";
        }
    }

    /**
     * 更新商品接口
     *
     * @param goods
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()), "请指定商品名称！");
        AssertUtil.isTrue(null == goods.getTypeId(), "请指定商品类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()), "请指定商品单位！");
        AssertUtil.isTrue(!(this.updateById(goods)), "记录更新失败！ ");
    }

    /**
     * 商品接口删除
     *
     * @param id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
        //todo: 如果商品已经入库 不可删除  商品已发生单据  不能删除
        Goods goods = this.getById(id);
        AssertUtil.isTrue(null == goods, "待删除的商品记录不存在！");
        AssertUtil.isTrue(goods.getState() == 1, "该商品已经期初入库，不能删除！");
        AssertUtil.isTrue(goods.getState() == 2, "该商品已经单据，不能删除！");
        goods.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(goods)), "商品删除失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateStock(Goods goods) {
        Goods temp = this.getById(goods.getId());
        AssertUtil.isTrue(null == goods, "待更新的商品记录不存在！");
        AssertUtil.isTrue(goods.getInventoryQuantity() <= 0, "库存量必须>0");
        AssertUtil.isTrue(goods.getPurchasingPrice() <= 0, "成本必须>0");
        AssertUtil.isTrue(!(this.updateById(goods)), "商品更新失败！");
    }

    @Override
    public void deleteStock(Integer id) {
        Goods temp = this.getById(id);
        AssertUtil.isTrue(null == temp, "待更新的商品记录不存在！");
        AssertUtil.isTrue(temp.getState() == 2, "该商品已发生单据，不可删除！");
        temp.setInventoryQuantity(0);
        AssertUtil.isTrue(!(this.updateById(temp)), "更新库存失败！");
    }
}
