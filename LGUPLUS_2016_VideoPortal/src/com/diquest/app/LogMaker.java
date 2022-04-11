package com.diquest.app;

import java.io.IOException;
import java.text.ParseException;

import com.diquest.app.common.Constant;
import com.diquest.app.manager.LogMakerManager;

public class LogMaker {
	
	LogMakerManager maker;
	
	public LogMaker(String[] arr){
		Constant.builderHome = arr[0];
		Constant.confPath = Constant.builderHome + "conf/";
		Constant.init();
		maker = new LogMakerManager();
	}
	
	private void build() throws ParseException{
		System.out.println("[LGUPLUS LOG MAKER START]");
		
		maker.openDb();
		maker.getNowTime();
		try {
			if(!"".equals(Constant.LOG.IS_LOG_DIR))
				maker.writeIsLog();
			if(!"".equals(Constant.LOG.MARINER_LOG_DIR))
				maker.writeMarinerLog();
			if(!"".equals(Constant.LOG.MARINERADD_LOG_DIR))
				maker.writeMarinerLogAdd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		maker.closeDb();
		
		System.out.println("[LGUPLUS LOG MAKER END]");
	}
	
	public static void main(String[] args) throws ParseException {
		if(args.length == 0){ // LOCAL TEST 
			String[] arr ={
					"./"
			};
			args = arr;
		}
		
		if(args.length < 1){
			System.err.println("args Usage: BUILDER_HOME ");
			System.exit(0);
		}
		
		LogMaker app = new LogMaker(args);
		app.build();
	}	
}
