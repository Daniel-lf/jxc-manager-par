package com.xxxx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.admin.dto.TreeDto;
import com.xxxx.admin.mapper.GoodsTypeMapper;
import com.xxxx.admin.pojo.GoodsType;
import com.xxxx.admin.service.IGoodsTypeService;
import com.xxxx.admin.utils.AssertUtil;
import com.xxxx.admin.utils.PageReusltUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类别表 服务实现类
 * </p>
 *
 * @author 老李
 * @since 2021-11-09
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Override
    public List<TreeDto> queryAllGoodsTypes(Integer typeId) {
        List<TreeDto> treeDtos = this.baseMapper.queryAllGoodsTypes();
        if (null != typeId) {
            for (TreeDto treeDto : treeDtos) {
                if (treeDto.getId().equals(typeId)) {
                    treeDto.setChecked(true);
                    break;
                }
            }
        }
        return treeDtos;
    }

    @Override
    public List<Integer> queryAllSubTypeByTypeId(Integer typeId) {
        GoodsType goodsType = this.getById(typeId);
        if (goodsType.getPId() == -1) {
            //所有类别
            return this.list().stream().map(GoodsType::getId).collect(Collectors.toList());
        }
        List<Integer> result = new ArrayList<>();
        result.add(typeId);
        return getSubTypeIds(typeId, result);
    }

    @Override
    public Map<String, Object> goodsTypeList() {
        List<GoodsType> menus = this.list();
        return PageReusltUtil.getResult((long) menus.size(), menus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoodsType(GoodsType goodsType) {
        /**
         * 1.商品名称不能为空
         * 2.商品类别上级id 非空
         * 3.考虑父类别（父类别本身 state=0）
         *  设置父类别state=1
         */
        AssertUtil.isTrue(StringUtils.isBlank(goodsType.getName()), "商品类别名称不能为空！");
        AssertUtil.isTrue(null == goodsType.getPId(), "请指定父类别！");
        goodsType.setState(0);
        AssertUtil.isTrue(!(this.save(goodsType)), "记录添加失败！");
        GoodsType parent = this.getById(goodsType.getPId());
        if (parent.getState() == 0) {
            parent.setState(1);
        }
        AssertUtil.isTrue(!(this.updateById(parent)), "记录添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoodsType(Integer id) {
        GoodsType temp = this.getById(id);
        AssertUtil.isTrue(null == temp, "待删除的记录不存在！");
        int count = this.count(new QueryWrapper<GoodsType>().eq("p_id", id));
        AssertUtil.isTrue(count > 0, "存在子类别，暂不支持级联删除！");
        count = this.count(new QueryWrapper<GoodsType>().eq("p_id", temp.getPId()));
        //todo: 为什们查询的count要等于1
        if (count == 1) {
            AssertUtil.isTrue(!(this.update(new UpdateWrapper<GoodsType>().set("state", 0).eq("id", temp.getPId()))), "类别删除失败！");
        }
        AssertUtil.isTrue(!(this.removeById(id)), "类别删除失败！");
    }

    private List<Integer> getSubTypeIds(Integer typeId, List<Integer> result) {
        List<GoodsType> goodsTypes = this.baseMapper.selectList(new QueryWrapper<GoodsType>().eq("p_id", typeId));
        if (CollectionUtils.isNotEmpty(goodsTypes)) {
            goodsTypes.forEach(gt -> {
                result.add(gt.getId());
                getSubTypeIds(gt.getId(), result);
            });
        }
        return result;
    }
}
