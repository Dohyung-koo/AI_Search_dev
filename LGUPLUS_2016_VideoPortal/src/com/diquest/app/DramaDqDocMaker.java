package com.diquest.app;

import java.io.BufferedWriter;
import java.io.IOException;

import com.diquest.app.common.Constant;
import com.diquest.app.manager.DramaDqDocMakerManager;
import com.diquest.util.FileUtil;

public class DramaDqDocMaker {

	DramaDqDocMakerManager manager;
	
	DramaDqDocMaker(String[] args){
		Constant.confPath = args[0];
		manager = DramaDqDocMakerManager.getInstance();
	}
	
	private void build(){
		double start = System.nanoTime();
		
		try {
			manager.selectWorkList();
			manager.makeVodKS();
			manager.makeUflixKs();
			manager.makeDic(); 
			manager.makeDqDoc();
			manager.makeTermData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		manager.close();
		
		double end = System.nanoTime();			
		double elapsed = (end - start) / 1000000000;
		
		System.out.println("ELAPSED TIME:" + Double.toString(elapsed) + "s");
		
		// LOG 파일 남기기
		BufferedWriter bw =  FileUtil.getBufferedWriter(Constant.confPath + "/time.UTF-8");
		try {
			bw.write("ELAPSED TIME:" + Double.toString(elapsed) + "s");
			bw.newLine();
			bw.write("WORK TYPE: DramaDqDocMaker");
			bw.newLine();
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(bw);
	}
	
	
	public static void main(String[] args) {
		if(args.length == 0){ // LOCAL TEST 
			String[] arr ={
					"./conf"
			};
			args = arr;
		}
		
		if(args.length < 1){
			System.err.println("args Usage: confPath");
			System.err.println("ex1) args = [./conf]");
			System.exit(0);
		}
		
		DramaDqDocMaker app = new DramaDqDocMaker(args);
		app.build();
		
	}
}
