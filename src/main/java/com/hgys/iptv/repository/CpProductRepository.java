package com.hgys.iptv.repository;

import com.hgys.iptv.model.CpProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public interface CpProductRepository extends JpaRepository<CpProduct,Integer>, JpaSpecificationExecutor<CpProduct> {

    /**
     * 通过cpid查询中间表其下属产品pid集合
     */
    @Query(value = "select cpp.pid from CpProduct cpp where cpp.cpid=?1")
    Set<Integer> findAllPid(Integer cpid);

    @Query(value = "select cpid from CpProduct where pid=?1")
    Set<Integer> findAllCpid(Integer pid);


    //    按cpid删中间表
    @Query("delete from CpProduct cpp where cpp.cpid=?1")
    @Modifying
    @Transactional
    void deleteAllByCpid(Integer cpid);

    //    按pid删中间表
    @Query("delete from CpProduct where pid=?1")
    @Modifying
    @Transactional
    void deleteAllByPid(Integer cpid);

    /**校验cpid-pid组合是否已存在*/
    Long countByCpidAndPid(Integer cpid,Integer pid);


    @Query(value = "SELECT COUNT(1) FROM cp_product WHERE cpid=?1 AND pid=?1 limit 1",nativeQuery = true)
    Integer isExist(Integer id1,Integer id2);


    /**
     * 通过id查询名字
     * @param Code
     * @return
     */
    @Query(value = "select cpid from cp_product o where pid = ?1  limit 1 ",nativeQuery = true)
   Integer findByMastercpid(Integer Code);



    @Query(value = "select o from CpProduct o where o.pid = ?1")
    List<CpProduct> findByMastercplists(Integer code);


}
