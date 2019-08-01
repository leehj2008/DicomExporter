package com.ge.si.dcmexport;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dcm4che2.data.DicomObject;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FTLString {
	Configuration cfg ;
    StringTemplateLoader stringLoader ;
	public FTLString (){
		 cfg = new Configuration();  
	     stringLoader = new StringTemplateLoader();  
	     cfg.setTemplateLoader(stringLoader);
	     cfg.setTemplateUpdateDelay(0);
	}
	public String processTag(String pathexp,DicomObject dicomObject) {
		Pattern pattern = Pattern.compile("([{](([0-9a-fA-F]{4})[,]([0-9a-fA-F]{4}))[}])");
		Matcher matcher = pattern.matcher(pathexp);
		while(matcher.find()){
			String tag_part1 = matcher.group(3);
			String tag_part2 = matcher.group(4);
			int tagInteger = Integer.valueOf(tag_part1+tag_part2, 16);
			String tagVal = dicomObject.getString(tagInteger);
			System.out.println(matcher.group(1));
			if(tagVal!=null&&!tagVal.equals("")){
				pathexp = pathexp.replace(matcher.group(1), tagVal);
			}
		}
		//System.out.println(pathexp);
		
		return pathexp;
	}
	public String compileString(String templateContent,Map<String,Object> data){
		stringLoader.putTemplate("strTemplate",templateContent);  
        StringWriter writer = new StringWriter();    
		try {  
	        Template template = cfg.getTemplate("strTemplate","utf-8");  
	        try {  
	            template.process(data, writer);  
	        } catch (TemplateException e) {  
	            e.printStackTrace();  
	        }   
	          
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
		return writer.toString();
	}
	
	public static void main(String[] args) {
		String template="hello,${name}";
		FTLString f = new FTLString();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("name","中文");
		String a = f.compileString(template, data);
		System.out.println(a);
		data.put("name","英文");
		String template1="haaaa,${name}";
		
		String b = f.compileString(template1, data);
		System.out.println(b);
	}

}
