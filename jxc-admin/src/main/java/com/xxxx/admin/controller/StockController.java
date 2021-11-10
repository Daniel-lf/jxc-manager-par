package com.xxxx.admin.controller;

import com.xxxx.admin.dto.GoodsQuery;
import com.xxxx.admin.pojo.Goods;
import com.xxxx.admin.service.IGoodsService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("stock")
public class StockController {

    @Autowired
    private IGoodsService goodsService;

    /**
     * 期初库存主页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "stock/stock";
    }

    /**
     * 库存量为0的商品
     * @param goodsQuery
     * @return
     */
    @RequestMapping("listNoInventoryQuantity")
    @ResponseBody
    public Map<String, Object> listNoInventoryQuantity(GoodsQuery goodsQuery) {
        goodsQuery.setType(1);
        return goodsService.goodsList(goodsQuery);
    }


    /**
     * 商品库存>0的商品
     * @param goodsQuery
     * @return
     */
    @RequestMapping("listHasInventoryQuantity")
    @ResponseBody
    public Map<String, Object> listHasInventoryQuantity(GoodsQuery goodsQuery) {
        goodsQuery.setType(2);
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("toUpdateGoodsInfoPage")
    public String toUpdateGoodsInfoPage(Integer gid, Model model){
        model.addAttribute("goods",goodsService.getById(gid));
        return "stock/goods_update";
    }

    @RequestMapping("updateStock")
    @ResponseBody
    public RespBean udpateGoods(Goods goods){
        /**
         * 商品的库存量>0
         */
        goodsService.updateStock(goods);
        return RespBean.success("商品记录更新成功！");
    }

    @RequestMapping("deleteStock")
    @ResponseBody
    public RespBean deleteStock(Integer id){
        /**
         * 商品的库存量>0
         */
        goodsService.deleteStock(id);
        return RespBean.success("商品记录删除成功！");
    }
}
