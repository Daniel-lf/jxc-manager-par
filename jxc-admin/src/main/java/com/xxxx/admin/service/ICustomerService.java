package com.xxxx.admin.service;

import com.xxxx.admin.dto.CustomerQuery;
import com.xxxx.admin.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface ICustomerService extends IService<Customer> {

    Map<String, Object> customerList(CustomerQuery customerQuery);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    Customer findCustomerByName(String name);

    void deleteCustomer(Integer[] ids);
}
