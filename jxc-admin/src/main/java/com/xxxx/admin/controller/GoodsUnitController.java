package com.xxxx.admin.controller;


import com.xxxx.admin.pojo.Goods;
import com.xxxx.admin.pojo.GoodsUnit;
import com.xxxx.admin.service.IGoodsTypeService;
import com.xxxx.admin.service.IGoodsUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 商品单位表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {

    @Autowired
    private IGoodsUnitService goodsUnitService;

    @RequestMapping("allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> allGoodsUnits() {
        return goodsUnitService.list();
    }

}
