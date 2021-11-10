package com.xxxx.admin.service;

import com.xxxx.admin.dto.SupplierQuery;
import com.xxxx.admin.pojo.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 供应商表 服务类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
public interface ISupplierService extends IService<Supplier> {

    Map<String, Object> supplierList(SupplierQuery supplierQuery);

    void saveSupplier(Supplier supplier);
    
    Supplier findSupplierByName(String name);

    void updateSupplier(Supplier supplier);

    void deleteSupplier(Integer[] ids);
}
