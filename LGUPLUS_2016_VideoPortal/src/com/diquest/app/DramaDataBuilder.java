package com.diquest.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.COLLECTION;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.app.common.Constant.DB_NAME;
import com.diquest.app.common.Constant.DRAMA;
import com.diquest.db.DramaDataDB;
import com.diquest.db.DramaMgmtDB;
import com.diquest.db.DramaTermRelationDB;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.DramaDataVO;
import com.diquest.util.FileUtil;
import com.diquest.util.StringUtil;

/**
 * 
 * 마리너에서 색인하여 바로 사용할수 있도록 
 *  계산된 drama 데이터를 DB에 적재한다.
 *   
 * @author hoho
 *
 */
public class DramaDataBuilder {
	DBConnector Connector;
	Connection conn;
	DramaTermRelationDB dramaDB;
	DramaMgmtDB dramaMgmtDB;
	DramaDataDB dramaDataDB;
	
	public DramaDataBuilder(String[] arr){
		Constant.builderHome = arr[0];
		Constant.confPath = Constant.builderHome + "conf/";
		Constant.init();
	}
	
	private void build(){
		double start = System.nanoTime();
		
		openDB();
		work(COLLECTION.VIDEO_DRAMA);
		work(COLLECTION.UFLIX_MOBILE_DRAMA);
		work(COLLECTION.UFLIX_TVG_DRAMA);
		closeDB();
		
		double end = System.nanoTime();			
		double elapsed = (end - start) / 1000000000;
		
		System.out.println("ELAPSED TIME:" + Double.toString(elapsed) + "s");
		
		// LOG 파일 남기기
		BufferedWriter bw =  FileUtil.getBufferedWriter(Constant.confPath + "/time.UTF-8");
		try {
			bw.write("ELAPSED TIME:" + Double.toString(elapsed) + "s");
			bw.newLine();
			bw.write("WORK TYPE: DramaDataBuilder");
			bw.newLine();
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(bw);
	}
	
	private void work(String collection){
		System.out.println("WORK DRAMA DATA collection [" + collection+"]");
//		List<String> list = dramaDB.select(dramaConn, collection);

		Map<String, List<String>> dramaMap = dramaDB.selectMap(conn, collection);
		Map<String, List<String>> dramaMgmtInsertMap = dramaMgmtDB.selectMap(conn, collection, "Y");
		Map<String, List<String>> dramaMgmtDeleteMap = dramaMgmtDB.selectMap(conn, collection, "N");
		
		System.out.println("SELECT KEYWORD SIZE :"+dramaMap.size());
		
		System.out.println("MERGE DRAMA DATA ... (MGMT, TERM RELATION DB) LIMIT:"+DRAMA.SIZE);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		int mergeCnt = 0;
		for(String s : dramaMap.keySet()){
			mergeCnt ++;
			List<String> dramaList = dramaMap.get(s);
			List<String> dramaMgmtInsertList = dramaMgmtInsertMap.get(s);
			List<String> dramaMgmtDeleteList = dramaMgmtDeleteMap.get(s);
			
			setDramaData(map, dramaList, dramaMgmtInsertList, dramaMgmtDeleteList, s);
			if(mergeCnt % 10000 ==0)
				System.out.println(mergeCnt + "keyword merge....");
		}
		System.out.println(mergeCnt + "keyword merged");
		
		System.out.print("INSERT");
		int insertCnt = 0;
		for(String key : map.keySet()){
			List<String> value = map.get(key);
			StringBuilder relTermBuilder = new StringBuilder();
			for(int i=0 , iSize=value.size() ; i<iSize ; i++){
				relTermBuilder.append(value.get(i));
				//마지막 , 빼주는 작업
				if(i < iSize-1)
					relTermBuilder.append(",");
			}
			DramaDataVO vo = new DramaDataVO();
			vo.setCollection(collection);
			vo.setTerm(key);
			vo.setRelTerm(relTermBuilder.toString());
			
			insertCnt++;
			dramaDataDB.insert(conn, vo);
			
			if(insertCnt%5000==0){
				System.out.print(".");
				dramaDataDB.executeInsertBatch();
			}
		}
		dramaDataDB.executeInsertBatch();
		System.out.println();
	}
	
	private void openDB(){
		System.out.println("OPEN DB ["+DB_INFO.URL+"]");
		Connector = new DBConnector(DB_INFO.URL, DB_INFO.USER,DB_INFO.PASSWORD ,DB_INFO.DRIVER);
		conn = Connector.getConnection();
		dramaDB = new DramaTermRelationDB(DB_NAME.DRAMA_TERM_RELATION ,false);
		dramaMgmtDB = new DramaMgmtDB(DB_NAME.DRAMA_MGMT ,false);
		dramaDataDB = new DramaDataDB(DB_NAME.DRAMA_DATA ,false);
		dramaDataDB.createInsertPstmt(conn);
		initTable(DB_NAME.DRAMA_DATA);
		
	}
	
	private void initTable(String tableName){
		System.out.println("TRUNCATE TABLE [" + tableName + "]");
		DBConnector.truncateTable(conn, tableName);
	}
	
	private void closeDB(){
		System.out.println("CLOSE DB");
		Connector.closeConnection(conn);
		dramaDataDB.closeInsertPstmt();
	}
	
	private void setDramaData(Map<String, List<String>> map, List<String> dramaList, List<String> dramaMgmtInsertList, List<String> dramaMgmtDeleteList, String where){
		
		List<String> relTerm = new ArrayList<String>();
		dramaMgmtInsertList = dramaMgmtInsertList==null ? new ArrayList<String>(): dramaMgmtInsertList;
		dramaMgmtDeleteList = dramaMgmtDeleteList==null ? new ArrayList<String>(): dramaMgmtDeleteList;
		
		int roopCnt=0;
		//수동반복
		for(int i=0, iSize = dramaMgmtInsertList.size(); i<iSize ; i++){
			relTerm.add(dramaMgmtInsertList.get(i));
			roopCnt++;
			if(roopCnt == Integer.parseInt(DRAMA.SIZE)){
//				map.put(where, relTerm);
				break;
			}
		}
		
		//자동반복
		for(int i=0, iSize = dramaList.size(); i<iSize ; i++){
			if(dramaMgmtDeleteList.contains(dramaList.get(i)))
				continue;
			// 5자 이상, 영문, 공백, 하이픈 조합 제거(인명 형태)
			if(dramaList.get(i).length() > 5 && StringUtil.isEnglishHyphen(dramaList.get(i)))
				continue;
			relTerm.add(dramaList.get(i));
			roopCnt++;
			
			if(roopCnt == Integer.parseInt(DRAMA.SIZE)){
//				map.put(where, relTerm);
				break;
			}
		}
		// DRAMA.SIZE를 다 못채웠을 수도 있으니 여기서 map에 넣자
		map.put(where, relTerm);
	}
	
	
	public static void main(String[] args) {
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
		
		DramaDataBuilder app = new DramaDataBuilder(args);
		app.build();
	}
}
