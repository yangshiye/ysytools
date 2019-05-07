package com.ysy.basetools.util;

import org.apache.poi.hssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 导出Excel表工具类
 */
public class ExcelUtil {

    public static byte[] exportExcel(String sheetName, List<List<String>> list) throws IOException {
        HSSFWorkbook wb = getHSSFWorkbook(sheetName, list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512 * 1024);
        wb.write(baos);
        return baos.toByteArray();
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, List<List<String>> list) {
        HSSFWorkbook wb = new HSSFWorkbook();// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFSheet sheet = wb.createSheet(sheetName);// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet

        if (ListUtil.empty(list)) {
            return wb;
        }

        HSSFRow titleRow = sheet.createRow(0);// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制


        HSSFCellStyle style = wb.createCellStyle();// 第四步，创建单元格，并设置值表头 设置表头居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //创建标题
        Iterator<List<String>> it = list.iterator();
        List<String> titleList = it.next();
        int i = 0;
        for (String title : titleList) {
            HSSFCell cell = titleRow.createCell(i++);
            cell.setCellValue(title);
            cell.setCellStyle(style);
        }

        //创建内容
        i = 1;
        for (; it.hasNext(); ) {
            List<String> dataList = it.next();
            HSSFRow row = sheet.createRow(i++);
            int j = 0;
            for (String value : dataList) {
                row.createCell(j++).setCellValue(value);//将内容按顺序赋给对应的列对象
            }
        }
        return wb;
    }
}