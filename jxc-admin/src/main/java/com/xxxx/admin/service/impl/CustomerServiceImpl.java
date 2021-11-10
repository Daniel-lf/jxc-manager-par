package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.admin.dto.CustomerQuery;
import com.xxxx.admin.pojo.Customer;
import com.xxxx.admin.mapper.CustomerMapper;
import com.xxxx.admin.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        IPage<Customer> page = new Page<Customer>(customerQuery.getPage(), customerQuery.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotBlank(customerQuery.getCustomerName())) {
            queryWrapper.like("name", customerQuery.getCustomerName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageReusltUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void saveCustomer(Customer customer) {
        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        AssertUtil.isTrue(null != this.findCustomerByName(customer.getName()), "客户已存在！");
        customer.setIsDel(0);
        AssertUtil.isTrue(!(this.save(customer)), "记录添加失败！");
    }

    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入客户名称！");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入客户电话！");
    }

    @Override
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(null == this.getById(customer.getId()), "请选择客户ID！");
        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        Customer temp = this.findCustomerByName(customer.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(customer.getId())), "客户已存在！");
        AssertUtil.isTrue(!(this.updateById(customer)), "记录更新失败！");
    }

    @Override
    public Customer findCustomerByName(String name) {
        return this.baseMapper.selectOne(new QueryWrapper<Customer>().eq("is_del", 0).eq("name", name));
    }

    @Override
    public void deleteCustomer(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的客户记录！");
        List<Customer> customerList=new ArrayList<>();
        for (Integer id : ids) {
            Customer temp = this.getById(id);
            temp.setIsDel(1);
            customerList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(customerList)),"记录删除成功！");
    }


}
