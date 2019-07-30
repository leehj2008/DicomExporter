package com.ge.si.dcmexport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	public static void main(String[] args) {
		new ReadExcel().readExcel();
	}

	private void readExcel() {

		try {
			// 1、获取文件输入流
			InputStream inputStream = new FileInputStream("/Users/lihaijiang/Documents/Files/export.xlsx");
			// 2、获取Excel工作簿对象
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			// 3、得到Excel工作表对象
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			// 4、循环读取表格数据
			for (Row row : sheetAt) {
				// 首行（即表头）不读取
				if (row.getRowNum() == 0) {
					continue;
				}
				// 读取当前行中单元格数据，索引从0开始
				String pid = row.getCell(0).getStringCellValue();
				String date = row.getCell(1).getStringCellValue();
				String type = row.getCell(2).getStringCellValue();
				System.out.println(pid + "|" + date + "|" + type);

			}
			// 5、关闭流
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
