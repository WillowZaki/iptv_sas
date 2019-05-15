package com.hgys.iptv.model.bean;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.math.BigDecimal;

@ExcelSheet(name = "订购量结算", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
@Data
public class CpOrderCpExcelDTO {
    @ExcelField(name = "Cp编码")
    private String cpcode;

    @ExcelField(name = "Cp名称", align = HorizontalAlignment.CENTER)
    private String cpname;

    @ExcelField(name = "订购量")
    private BigDecimal quantity;
}
