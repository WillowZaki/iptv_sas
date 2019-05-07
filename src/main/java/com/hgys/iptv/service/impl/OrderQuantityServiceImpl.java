package com.hgys.iptv.service.impl;


import com.hgys.iptv.controller.assemlber.OrderQuantityControllerAssemlber;
import com.hgys.iptv.controller.vm.OrderQuantityControllerListVM;
import com.hgys.iptv.model.OrderQuantity;
import com.hgys.iptv.model.enums.ResultEnum;
import com.hgys.iptv.model.vo.ResultVO;
import com.hgys.iptv.repository.OrderQuantityRepository;
import com.hgys.iptv.service.OrderQuantityService;
import com.hgys.iptv.util.CodeUtil;
import com.hgys.iptv.util.ResultVOUtil;
import com.hgys.iptv.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderQuantityServiceImpl  implements OrderQuantityService {

    @Autowired
    private OrderQuantityRepository orderquantityRepository;

    @Autowired
    private OrderQuantityControllerAssemlber orderQuantityControllerAssemlber;

    /**
     * 根据ID查询
     */
    @Override
    public OrderQuantity findById(Integer id) {
        //如果未查询到返回null
        return orderquantityRepository.findById(id).orElse(null);
    }

 /**
     * 添加
     */
    @Override
    public ResultVO<?> insterOrderQuantity(String name, String status, String note) {
        OrderQuantity oq = new OrderQuantity();
        oq.setCode(CodeUtil.getOnlyCode("SDS",5));
        oq.setInputTime(new Timestamp(System.currentTimeMillis()));
        oq.setIsdelete(0);
        oq.setName(name);
        oq.setNote(note);
        oq.setStatus(Integer.parseInt(status));
        orderquantityRepository.save(oq);
        return ResultVOUtil.success(Boolean.TRUE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVO<?> batchDelete(String ids) {
        try{
            List<String>  idLists = Arrays.asList(StringUtils.split(ids, ","));
            for (String s : idLists){
                orderquantityRepository.batchDelete(Integer.parseInt(s));
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.SYSTEM_INTERNAL_ERROR);
        }

        return ResultVOUtil.success(Boolean.TRUE);
    }

    @Override
    public Page<OrderQuantityControllerListVM> findByConditions(String name, String code, String status, Pageable pageable) {

        return orderquantityRepository.findAll(((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (org.apache.commons.lang3.StringUtils.isNotBlank(name)){
                Predicate condition = builder.equal(root.get("name"), name);
                predicates.add(condition);
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(code)){
                Predicate condition = builder.equal(root.get("code"), code);
                predicates.add(condition);
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(status)){
                Predicate condition = builder.equal(root.get("status"), status);
                predicates.add(condition);
            }
            if (!predicates.isEmpty()){
                return builder.and(predicates.toArray(new Predicate[0]));
            }
            return builder.conjunction();
        }),pageable).map(orderQuantityControllerAssemlber::getListVM);
    }
    @Override
    public ResultVO<?> updateOrderQuantity(OrderQuantity oq) {
        if (null == oq.getId()){
            ResultVOUtil.error("1","ID不能为空");
        }else if (org.apache.commons.lang3.StringUtils.isBlank(oq.getName())){
            ResultVOUtil.error("1","名称不能为空");
        }else if (null == oq.getStatus()){
            ResultVOUtil.error("1","状态不能为空");
        }
        try{
            OrderQuantity byId = orderquantityRepository.findById(oq.getId()).orElseThrow(()-> new IllegalArgumentException("为查询到ID为:" + oq.getId() + "信息"));
            oq.setModifyTime(new Timestamp(System.currentTimeMillis()));
            UpdateTool.copyNullProperties(byId,oq);
            orderquantityRepository.saveAndFlush(oq);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.SYSTEM_INTERNAL_ERROR);
        }
        return ResultVOUtil.success(Boolean.TRUE);
    }
}
