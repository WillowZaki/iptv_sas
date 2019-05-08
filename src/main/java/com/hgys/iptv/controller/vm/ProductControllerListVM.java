package com.hgys.iptv.controller.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProductControllerListVM
 * @Auther: wangz
 * @Date: 2019/5/8 11:54
 * @Description: TODO
 */
@Data
@ApiModel("集合VM")
public class ProductControllerListVM {
    /** 名称*/
    @ApiModelProperty("名称")
    private String name;


    /** 名称*/
    @ApiModelProperty("价格")
    private Double price;

    /** 状态 */
    @ApiModelProperty("状态")
    private Integer status;
}
