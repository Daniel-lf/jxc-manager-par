package com.xxxx.admin.controller;


import com.xxxx.admin.dto.GoodsQuery;
import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.pojo.Goods;
import com.xxxx.admin.service.IGoodsService;
import com.xxxx.admin.service.IGoodsTypeService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService
            goodsService;
    @Autowired
    private IGoodsTypeService goodsTypeService;

    @RequestMapping("index")
    public String index() {
        return "goods/goods";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("addOrUpdateGoodsPage")
    public String addOrUpdateGoodsPage(Integer id, Integer typeId, Model model) {
        if (null != id) {
            Goods goods = goodsService.getById(id);
            //更新处理
            model.addAttribute("goods", goods);
            model.addAttribute("goodsType", goodsTypeService.getById(goods.getTypeId()));
        } else {
            //添加处理
            if (null != typeId) {
                model.addAttribute("goodsType", goodsTypeService.getById(typeId));
            }
        }
        return "goods/add_update";
    }

    /**
     * 商品类别选择页
     * @param typeId
     * @param model
     * @return
     */
    @RequestMapping("toGoodsTypePage")
    public String toGoodsTypePage(Integer typeId, Model model) {
        if (null != typeId) {
            model.addAttribute("typeId", typeId);
        }
        return "goods/goods_type";
    }

    /**
     * 添加商品接口
     * @param goods
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveGoods(Goods goods){
        goodsService.saveGoods(goods);
        return RespBean.success("商品记录添加成功！");
    }

    /**
     * 更新商品接口
     * @param goods
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean udpateGoods(Goods goods){
        goodsService.updateGoods(goods);
        return RespBean.success("商品记录更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteGoods(Integer id){
        goodsService.deleteGoods(id);
        return RespBean.success("商品记录更新成功！");
    }

}
