package com.xxxx.admin.controller;


import com.xxxx.admin.dto.SupplierQuery;
import com.xxxx.admin.pojo.Supplier;
import com.xxxx.admin.service.ISupplierService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <p>
 * 供应商表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private ISupplierService supplierService;

    @RequestMapping("index")
    public String index(){
        return "supplier/supplier";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> supplierList(SupplierQuery supplierQuery){
        return supplierService.supplierList(supplierQuery);
    }

    /**
     * 添加|更新供应商页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSupplierPage")
    public String addOrUpdateSupplierPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("supplier",supplierService.getById(id));
        }
        return "supplier/add_update";
    }

    /**
     * 添加供应商
     * @param supplier
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveSupplier(Supplier supplier){
        supplierService.saveSupplier(supplier);
        return RespBean.success("供应商记录添加成功!");
    }

    /**
     * 更新供应商
     * @param supplier
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateSupplier(Supplier supplier){
        supplierService.updateSupplier(supplier);
        return RespBean.success("供应商记录添加成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteSupplier(Integer[] ids){
        supplierService.deleteSupplier(ids);
        return RespBean.success("供应商删除成功！");
    }
}
