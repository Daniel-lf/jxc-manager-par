package com.xxxx.admin.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.admin.dto.CustomerQuery;
import com.xxxx.admin.dto.SupplierQuery;
import com.xxxx.admin.pojo.Customer;
import com.xxxx.admin.service.ICustomerService;
import com.xxxx.admin.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @RequestMapping("index")
    public String index() {
        return "customer/customer";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        return customerService.customerList(customerQuery);
    }

    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, Model model) {
        if (id != null) {
            model.addAttribute("customer", customerService.getById(id));
        }
        return "customer/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Customer customer) {
        customerService.saveCustomer(customer);
        return RespBean.success("记录添加成功！");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Customer customer){
        customerService.updateCustomer(customer);
        return RespBean.success("记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer[] ids){
        customerService.deleteCustomer(ids);
        return RespBean.success("客户记录删除成功！");
    }

}
