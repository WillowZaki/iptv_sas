package com.hgys.iptv.repository;

import com.hgys.iptv.model.OrderProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends BaseRepository<OrderProduct,Integer> {

    /**
     * 通过ID逻辑删除
     */
    @Modifying
    @Query(value = "update OrderProduct set isdelete = 1 WHERE id = ?1")
    void batchDeleteop(int id);


    /**
     * 通过id查询
     * @param id
     * @return
     */
    OrderProduct findById(String id);
}
