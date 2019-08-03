package com.ge.si.dcmexport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.record.IterationRecord;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import joinery.DataFrame;
import joinery.DataFrame.NumberDefault;

public class ReadExcelJoiner {

	DecimalFormat nf = (DecimalFormat)DecimalFormat.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static void main(String[] args) {
		DataFrame df = new ReadExcelJoiner().readExcel("export.xls");
		System.out.println(df);
		df.show();
	}

	public DataFrame readExcel(String file) {
		nf.applyPattern("#");
		DataFrame<Object> dataframe = new DataFrame<Object>();
		try {
			dataframe = DataFrame.readXls(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataFrame.Function<Object, Object> function = new DataFrame.Function<Object, Object>(){

			@Override
			public Object apply(Object value) {
				System.out.println(value);
				if (value instanceof Double || value instanceof Long) {
					System.out.println("Doulble Or Long");
					value = nf.format(value);
				}
				if(value instanceof Date){
					value = sdf.format(value);

					System.out.println("Date");
				}
				System.out.println("class:"+value.getClass());
				return value;
			}};
		dataframe  = dataframe.apply(function);
		return dataframe;
	}
}
