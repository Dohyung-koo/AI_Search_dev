package com.diquest.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import com.diquest.app.checker.FileChecker;
import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.FILE_TO_DB_TYPE;
import com.diquest.app.manager.ArDbToDbManager;
import com.diquest.app.manager.DbToDbManager;
import com.diquest.app.manager.FileToDbManager;
import com.diquest.app.manager.HtmlDbToDbManager;
import com.diquest.util.FileUtil;

public class FileToDb {
	
	private String type;
	FileToDbManager manager;
	DbToDbManager db2dbManager;
	ArDbToDbManager arDb2dbManager;
	HtmlDbToDbManager htmlDb2dbManager;
	FileChecker checker;
	
	public FileToDb(String[] arr){
		Constant.builderHome = arr[0];
		Constant.confPath = Constant.builderHome + "conf/";
		Constant.init();
		this.checker = FileChecker.getInstance();
		this.db2dbManager = new DbToDbManager();
		this.arDb2dbManager = new ArDbToDbManager();
		this.htmlDb2dbManager = new HtmlDbToDbManager();
		this.manager = new FileToDbManager(arr[1]);
		type = arr[1];
	}
	
	private void build() throws IOException{
		double start = System.nanoTime();
		String workDirs = Constant.DATA.WORKDIRS;
		//TODO 먼저온파일 먼저처리
		List<String> completeFileList = checker.getCompleteFileList(workDirs);
		List<String> copiedFileList = checker.copyBackDir(completeFileList, Constant.builderHome + "data/bak/");
		List<String> workFileList = checker.unTarList(copiedFileList, Constant.builderHome + "data/work/");
		
		//AR DB
		try {
			arDb2dbManager.Start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//공연_DB
		try {
			db2dbManager.Start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//아이들나라 HTML_DB
		try {
			htmlDb2dbManager.Start();	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		checker.checkOldFile(Constant.builderHome + "data/bak/");
		checker.checkOldFiletxt(Constant.LOG.ADD_LOG_DIR);
		checker.logmaker(workFileList, completeFileList , workDirs);
		
		manager.openDB();
	
		
		try {
			for(String workFile : workFileList){
				String fileType = manager.getFileType(workFile);
				System.out.println("[WORK " +workFile +" TYPE:" + fileType+ " ...");
				int total = 0;
				if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.ALL)){
					total = manager.workAll(fileType, workFile);
//				} else if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.VIDEO)){
//					total = manager.workVideo();
//				} else if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.VIDEO_AUTO_KEYWORD)){
//					manager.workVideoAutoComplete();
//				} else if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.UFLIX)){
//					manager.workUflix();
//				} else if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.UFLIX_AUTO_KEYWORD)){
//					manager.workUflixAutoComplete();
				}
				if(total == 1){
					System.out.println("[UNKOWN FILE:" + workFile+ "]" );
				} else {
					System.out.println("[COMPLETE WORK " + fileType + " TOTAL LINE:" + total );
					
				}
			}
			
			if(type.equalsIgnoreCase(FILE_TO_DB_TYPE.ALL)){
				manager.workVideoAutoComplete();
				//Choihu 20180629 공연 자동완성
				manager.workShowAutoComplete();
				//Choihu 20181204 VR 자동완성
				manager.workVrAutoComplete();
				//Choihu 20181204 AR 자동완성
				manager.workArAutoComplete();
				manager.workUflixAutoComplete();
//				manager.workIptvAutoComplete();
				manager.workIptv30AutoComplete();
				manager.workGolf2AutoComplete();
				manager.workKidsAutoComplete();
				manager.workKidsClassAutoComplete();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		manager.closeDB();
		double end = System.nanoTime();			
		double elapsed = (end - start) / 1000000000;
		
		System.out.println("TOTAL INSERT TIME:" + Double.toString(manager.totalInsertTime / 1000000000) + "s");
		System.out.println("ELAPSED TIME:" + Double.toString(elapsed) + "s");
		
		// LOG 파일 남기기
		BufferedWriter bw =  FileUtil.getBufferedWriter(Constant.confPath + "/time.UTF-8");
		try {
			bw.write("TOTAL INSERT TIME:"+ Double.toString(manager.totalInsertTime / 1000000000) + "s");
			bw.newLine();
			bw.write("ELAPSED TIME:" + Double.toString(elapsed) + "s");
			bw.newLine();
			bw.write("WORK TYPE: FileToDb");
			bw.newLine();
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(bw);
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0){ // LOCAL TEST 
			String[] arr ={
					"./",
					"ALL"
					
			};
			args = arr;
		}
		
		if(args.length < 2 ||
					!(Constant.FILE_TO_DB_TYPE.ALL.equalsIgnoreCase(args[args.length-1])
					|| Constant.FILE_TO_DB_TYPE.VIDEO.equalsIgnoreCase(args[args.length-1])
					|| Constant.FILE_TO_DB_TYPE.VIDEO_AUTO_KEYWORD.equalsIgnoreCase(args[args.length-1])
					|| Constant.FILE_TO_DB_TYPE.UFLIX.equalsIgnoreCase(args[args.length-1])
					|| Constant.FILE_TO_DB_TYPE.UFLIX_AUTO_KEYWORD.equalsIgnoreCase(args[args.length-1]))){
			
			System.err.println("args Usage: confPath , workOption(ALL - All work, V - Video, VA - Video AutoComplete, U - Uflix, UA - Uflix AutoComplete) ");
			System.err.println("ex1) args = [./conf, V] ");
			System.err.println("ex2) args = [./conf, VA]");
			System.err.println("ex3) args = [./conf, U]");
			System.exit(0);
		}
		
		
		FileToDb app = new FileToDb(args);
		app.build();
		
	}

}

