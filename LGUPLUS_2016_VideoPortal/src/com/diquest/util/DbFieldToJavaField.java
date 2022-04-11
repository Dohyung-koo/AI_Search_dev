package com.diquest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CaseFormat;

/**
 * db 컬럼 명이 row 단위로 있는 txt 파일을 읽어 
 * public String 카멜케이스변수; 형태로 바꾸어준다
 * @author hoho
 *
 */
public class DbFieldToJavaField {

	String inDir;
	
	public DbFieldToJavaField(String inDir){
		this.inDir = inDir;
	}
	
	public List<String> read(){
		BufferedReader br = FileUtil.getBufferedReader(inDir);
		String line = "";
		ArrayList<String> list = new ArrayList<String>();
		try {
			while((line = br.readLine()) !=null){
				if("".equals(line))
					continue;
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public void printDoubleQutoStatement(){
		List<String> field = read();
		for(String s : field){
			StringBuilder sb = new StringBuilder();
			sb.append("\"");
			sb.append(s);
			sb.append("\"");
			sb.append(",");
			System.out.println(sb.toString());
		}
	}
	
	public void printValueStatement(){
		List<String> field = read();
		for(String s : field){
			StringBuilder sb = new StringBuilder();
			sb.append("private String ");
			
			String camel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s);
			String front = camel.substring(0,1);
			front = front.toLowerCase();
			camel = camel.substring(1, camel.length());
			sb.append(front);
			sb.append(camel);
			
			sb.append(";");
			System.out.println(sb.toString());
		}
	}
	
	public void printVoGetStatement(){
		List<String> field = read();
		int i =0;
		for(String s : field){
			StringBuilder sb = new StringBuilder();
			sb.append("insertPstmt.setString(");
			sb.append(++i);
			sb.append(", ");
			sb.append("vo.get");
			sb.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s));
			sb.append("());");
			System.out.println(sb.toString());
		}
	}
	
	public void printVoSetStatement(){
		List<String> field = read();
		int i =0;
		for(String s : field){
			//vo.setServiceId(toks[0]);
			StringBuilder sb = new StringBuilder();
			sb.append("vo.set");
			sb.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s));
			sb.append("(");
			sb.append("list.get(");
			sb.append(i++);
			sb.append("));");
			System.out.println(sb.toString());
		}
	}
	
	public void printVoSetRsStatement(){
		List<String> field = read();
		int i =0;
		for(String s : field){
			//vo.setServiceId(toks[0]);
			StringBuilder sb = new StringBuilder();
			sb.append("vo.set");
			sb.append(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s));
			sb.append("(");
			sb.append("rs.getString(");
			sb.append(++i);
			sb.append("));");
			System.out.println(sb.toString());
		}
	}
	
	public static void main(String[] args) {
		
		String inDir = "d:/in.txt";
		DbFieldToJavaField app = new DbFieldToJavaField(inDir);
		app.printValueStatement();
		app.printVoGetStatement();
		app.printVoSetStatement();
		app.printDoubleQutoStatement();
		app.printVoSetRsStatement();
		
	}

}
