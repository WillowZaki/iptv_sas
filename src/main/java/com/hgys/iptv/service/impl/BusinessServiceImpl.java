package com.hgys.iptv.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.hgys.iptv.controller.assemlber.BusinessControllerAssemlber;
import com.hgys.iptv.controller.vm.BusinessAddVM;
import com.hgys.iptv.controller.vm.BusinessVM;
import com.hgys.iptv.model.*;
import com.hgys.iptv.model.enums.ResultEnum;
import com.hgys.iptv.model.vo.ResultVO;
import com.hgys.iptv.repository.*;
import com.hgys.iptv.service.BusinessService;
import com.hgys.iptv.util.AbstractBaseServiceImpl;
import com.hgys.iptv.util.CodeUtil;
import com.hgys.iptv.util.ResultVOUtil;
import com.hgys.iptv.util.UpdateTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Auther: wangz
 * @Date: 2019/5/5 18:05
 * @Description:
 */
@Service
public class BusinessServiceImpl extends AbstractBaseServiceImpl implements BusinessService {
    @Autowired
    private CpRepository cpRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    CpProductRepository cpProductRepository;
    @Autowired
    CpBusinessRepository cpBusinessRepository;
    @Autowired
    ProductBusinessRepository productBusinessRepository;

    @Autowired
    BusinessControllerAssemlber assemlber;
//    @Autowired
//    private Logger logger;
    //操作对象
    private static final String menuName = "业务管理";
    /**
     * 新增
     * @param vm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<?> save(BusinessAddVM vm){
        try {
            //校验名称是否已经存在
//        Business byName = businessRepository.findByName(business.getName());
//        if (null != byName){
//            return ResultVOUtil.error("1",byName + "名称已经存在");
//        }
            //必填字段：业务名臣，业务类型，结算类型，状态
            if(StringUtils.isBlank(vm.getName()))
                return ResultVOUtil.error("1","业务名称不能为空！");
            if(vm.getBizType()==null)
                return ResultVOUtil.error("1","业务类型不能为空！");
            if(vm.getStatus()==null)
                return ResultVOUtil.error("1","状态不能为空！");
            //1.存cp主表并返回
            Business business = new Business();
            BeanUtils.copyProperties(vm, business);
            business.setModifyTime(new Timestamp(System.currentTimeMillis()));//最后修改时间
            business.setInputTime(new Timestamp(System.currentTimeMillis()));//注册时间
            business.setCode(CodeUtil.getOnlyCode("BIZ",5));//cp编码
            business.setIsdelete(0);//删除状态
            Business biz_add = businessRepository.save(business);
            //处理 business关联的中间表的映射关系
            handleRelation(vm,biz_add.getId());
//            logger.log_add_success(menuName,"BusinessServiceImpl.save");
        }catch (Exception e){
            e.printStackTrace();
//            logger.log_add_fail(menuName,"BusinessServiceImpl.save");
            return ResultVOUtil.error("1","新增失败！");
        }
        return ResultVOUtil.success(Boolean.TRUE);
    }
    /**
     * 处理 business关联的中间表的映射关系
     * @param vm--维护数据来源
     * @param id--要维护的业务id
     */
    @Transactional(rollbackFor = Exception.class)
    protected void handleRelation(BusinessAddVM vm, Integer id){
        try {
            //------------------------处理关系
            List<String> pidLists = Arrays.asList(StringUtils.split(vm.getPids(), ","));
            //2.插product_business中间表
            if(pidLists.size()>0){
                List<ProductBusiness> pbs =new ArrayList<>();
                pidLists.forEach(pid->{
                    ProductBusiness pb = new ProductBusiness();
                    pb.setBid(id);
                    pb.setPid(Integer.parseInt(pid));
                    pbs.add(pb);
                });
                productBusinessRepository.saveAll(pbs);
            }
            //------------------------------------------
            //3.插cp-business中间表
            List<CpBusiness> cpBizs =new ArrayList<>();
            List<String> cpidLists = Arrays.asList(StringUtils.split(vm.getCpids(), ","));
            if(cpidLists.size()>0){
                cpidLists.forEach(cpid->{
                    CpBusiness cpBusiness = new CpBusiness();
                    cpBusiness.setBid(id);
                    cpBusiness.setCpid(Integer.parseInt(cpid));
                    cpBizs.add(cpBusiness);
                });
                cpBusinessRepository.saveAll(cpBizs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 修改
     * @param vm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<?> update(BusinessAddVM vm) {
        if (null == vm.getId()){
            ResultVOUtil.error("1","主键不能为空");
        }
        try{
            Business business = new Business();
            BeanUtils.copyProperties(vm,business);
//            //验证名称是否已经存在
//            if (StringUtils.isNotBlank(business.getName())){
//                Business byName = businessRepository.findByName(business.getName().trim());
//                if (null != byName && !byName.getId().equals(business.getId()) ){
//                    ResultVOUtil.error("1","名称已经存在");
//                }
//            }
            Business byId = businessRepository.findById(business.getId()).orElseThrow(()-> new IllegalArgumentException("为查询到ID为:" + business.getId() + "业务信息"));
            business.setModifyTime(new Timestamp(System.currentTimeMillis()));
            UpdateTool.copyNullProperties(byId,business);
            businessRepository.saveAndFlush(business);
            //先删除后插入
                productBusinessRepository.deleteAllByBid(business.getId());
                cpBusinessRepository.deleteAllByBid(business.getId());
            //处理 business关联的中间表的映射关系
            handleRelation(vm,vm.getId());

//            logger.log_up_success(menuName,"BusinessServiceImpl.update");

        }catch (Exception e){
            e.printStackTrace();
//            logger.log_up_fail(menuName,"BusinessServiceImpl.update");
            return ResultVOUtil.error(ResultEnum.SYSTEM_INTERNAL_ERROR);
        }
        return ResultVOUtil.success(Boolean.TRUE);
    }

//    /**
//     * 逻辑删除，只更新对象的isdelete字段值 0：未删除 1：已删除
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ResultVO<?> logicDelete(Integer id){
//        try {
//            System.out.println("in");
//            businessRepository.logicDelete(id);
//            System.out.println("");
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResultVOUtil.error(ResultEnum.SYSTEM_INTERNAL_ERROR);
//        }
//        return ResultVOUtil.success(Boolean.TRUE);
//    }

    /**
     * 批量逻辑删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<?> batchLogicDelete(String ids){
        try {
            List<String>  idLists = Arrays.asList(StringUtils.split(ids, ","));
            if(idLists.size()>0){
                Set<Integer> idSets = new HashSet<>();
                idLists.forEach(cpid->{
                    idSets.add(Integer.parseInt(cpid));
                });
                for (Integer id : idSets){
                    businessRepository.logicDelete(id);
                    //删除cp_business关系映射
                    cpBusinessRepository.deleteAllByBid(id);
                    //删除product_business关系映射
                    productBusinessRepository.deleteAllByBid(id);
                }

//                logger.log_rm_success(menuName,"BusinessServiceImpl.batchLogicDelete");
            }
        }catch (Exception e){
//            logger.log_rm_fail(menuName,"BusinessServiceImpl.batchLogicDelete");
            return ResultVOUtil.error(ResultEnum.SYSTEM_INTERNAL_ERROR);
        }
        return ResultVOUtil.success(Boolean.TRUE);
    }

    /**
     * 单查询--根据id
     * @param id
     * @return
     */
    @Override
    public ResultVO<?> findById(Integer id) {
        try {
            Business business = businessRepository.findById(id).orElse(null);
            if(business==null)
                return ResultVOUtil.error("1","所查业务不存在");
            BusinessVM vm = new BusinessVM();
            BeanUtils.copyProperties(business,vm);
            //查关联的产品--先按cpid查cp_product中间表查出pid集合-->按pid去 findAllById
            Set<Integer> pidSet = productBusinessRepository.findAllPid(id);
            List<Product> pList = productRepository.findAllById(pidSet);
            ArrayList<Product> PList = new ArrayList<>();
            //筛除已停用、删除的产品
            pList.forEach(p->{
                if(p.getIsdelete()==0&&p.getStatus()==0)
                    PList.add(p);
            });
            vm.setpList(PList);

            //查关联的cp
            Set<Integer> cpidSet = cpBusinessRepository.findAllCpid(id);
            List<Cp> cpList = cpRepository.findAllById(cpidSet);
            ArrayList<Cp> CPList = new ArrayList<>();
            //筛除已停用、删除的
            cpList.forEach(b->{
                if(b.getIsdelete()==0&&(b.getStatus()==1||b.getStatus()==2))
                    CPList.add(b);
            });
            vm.setCpList(CPList);
           return ResultVOUtil.success(vm);
        }catch (Exception e){
            return ResultVOUtil.error("1","所查业务不存在");
        }
    }

    /**
     * 单查询--根据code
     * @param code
     * @return
     */
    @Override
    public ResultVO<?> findByCode(String code) {
        Business business = businessRepository.findByCode(code);
        if(business!=null)
            return ResultVOUtil.success(business);
        return ResultVOUtil.error("1","所查询的产品不存在!");
    }

    /**
     * 列表查询所有未删除、未停用的业务
     * @return
     */
    @Override
    public ResultVO<?> findAll() {
        Map<String,Object> vm = new HashMap<>();
        vm.put("isdelete",0);
        vm.put("status",0);
        List<?> buss =findByCriteria(Business.class,vm);
        if(buss!=null&&buss.size()>0)
            return ResultVOUtil.success(buss);
        return ResultVOUtil.error("1","所查询的产品列表不存在!");
    }


    @Override
    public Page<BusinessVM> findByConditions(String name, String code, Integer bizType, Integer settleType, Integer status, Pageable pageable) {


        return businessRepository.findAll(((root, query,builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(name)){
                Predicate condition = builder.like(root.get("name").as(String.class), "%"+name+"%");
                predicates.add(condition);
            }
            if (StringUtils.isNotBlank(code)){
                Predicate condition = builder.like(root.get("code").as(String.class), "%"+code+"%");
                predicates.add(condition);
            }
            if (status!=null && bizType>0){
                Predicate condition = builder.equal(root.get("bizType").as(Integer.class), bizType);
                predicates.add(condition);
            }
            if (status!=null && status>0){
                Predicate condition = builder.equal(root.get("status").as(Integer.class), status);
                predicates.add(condition);
            }
            if (status!=null && settleType>0){
                Predicate condition = builder.equal(root.get("settleType").as(Integer.class), settleType);
                predicates.add(condition);
            }
            Predicate condition = builder.equal(root.get("isdelete").as(Integer.class), 0);
            predicates.add(condition);
            if (!predicates.isEmpty()){
                return builder.and(predicates.toArray(new Predicate[0]));
            }
//            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            return builder.conjunction();
        }),pageable).map(assemlber::getListVM);
    }

}
