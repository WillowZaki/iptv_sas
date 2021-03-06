package com.hgys.iptv.controller.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName BusinessControllerListVM
 * @Auther: wangz
 * @Date: 2019/5/8 11:54
 * @Description: TODO
 */
@ApiModel("集合VM")
@Data
public class BusinessControllerListVM {

    /** 主键*/
    @ApiModelProperty("主键")
    private Integer id;

    /** 名称*/
    @ApiModelProperty("名称")
    private String name;

    /** 名称*/
    @ApiModelProperty("编码")
    private String code;

    /** 业务类型*/
    @ApiModelProperty("业务类型")
    private Integer bizType;//1.视频类 2.非视频类

    @ApiModelProperty("结算类型")
    private Integer settleType;//1.比例结算 2.订购量结算

    /** 录入时间 */
    @ApiModelProperty("录入时间")
    private Timestamp inputTime;

    /** 修改时间 */
    @ApiModelProperty("修改时间")
    private Timestamp modifyTime;

    /** 状态 */
    @ApiModelProperty("状态")
    private Integer status;

    /** 逻辑删除(0:否；1:是) */
    @ApiModelProperty("逻辑删除(0:否；1:是)")
    private Integer isdelete;
}
