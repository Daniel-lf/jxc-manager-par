package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.admin.dto.SupplierQuery;
import com.xxxx.admin.pojo.Supplier;
import com.xxxx.admin.mapper.SupplierMapper;
import com.xxxx.admin.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Override
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        IPage<Supplier> page = new Page<Supplier>(supplierQuery.getPage(), supplierQuery.getLimit());
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotBlank(supplierQuery.getSupplierName())) {
            queryWrapper.like("name", supplierQuery.getSupplierName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageReusltUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void saveSupplier(Supplier supplier) {
        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        AssertUtil.isTrue(null != this.findSupplierByName(supplier.getName()), "供应商已存在！");
        supplier.setIsDel(0);
        AssertUtil.isTrue(!(this.save(supplier)), "记录添加失败！");
    }

    @Override
    public Supplier findSupplierByName(String name) {
        return this.getOne(new QueryWrapper<Supplier>().eq("is_del", 0).eq("name", name));
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        AssertUtil.isTrue(null == this.getById(supplier.getId()), "请选择供应商记录");
        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        Supplier temp = this.findSupplierByName(supplier.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(supplier.getId())), "供应商已存在！");
        AssertUtil.isTrue(!(this.updateById(supplier)), "供应商更新失败！");
    }

    @Override
    public void deleteSupplier(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的记录！");
        List<Supplier> supplierList = new ArrayList<>();
        for (Integer id : ids) {
            Supplier temp = this.getById(id);
            temp.setIsDel(1);
            supplierList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(supplierList)), "记录删除失败！");
    }

    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入供应商名称！");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入供应商联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入供应商电话！");
    }
}
