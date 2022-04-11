package com.diquest.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class FileConverter {
	
	String inDir;
	String outDir;
	
	FileConverter (String inDir, String outDir){
		this.inDir = inDir;
		this.outDir = outDir;
	}
	
	public void convert(){
		BufferedReader br = FileUtil.getBufferedReader(inDir,  "euc-kr");
		BufferedWriter bw = FileUtil.getBufferedWriter(outDir, false,"euc-kr");
		
		String line = "";
		
		try {
			while((line = br.readLine()) != null){
				
				if(line.contains("\\\\")){
					line = line.replaceAll("\\\\", "\\\\\\\\");
				}
				
//				line = line.replaceAll("\\", "\\\\");
				
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileUtil.closeBufferedReader(br);
		FileUtil.closeBufferedWriter(bw);
		System.out.println("end App");
	}
	
	public static void main(String[] args) {
		String inDir = "d:/inFormat.txt";
		String outDir = "d:/outFormat.txt";
		FileConverter app = new FileConverter(inDir, outDir);
		app.convert();
		
//		String s = "^|^|^|온게임넷^|^|";
//		
//		String[] toks = s.split("\\^\\|");
//		for(String ss : toks){
//			System.out.println(ss);
//			
//		}
		
	}
}
