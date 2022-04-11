package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.QueryLogFullVO;
import com.diquest.util.SqlUtil;

public class QueryLogFullDB {

	private final String[] ALL_COLUMN = new String[]{
			"COLLECTION_ID",
			"PROFILE",
			"KEYWORDS",
			"ANALYZED_RESULTS",
			"USER_NAME",
			"EXTRA_DATA",
			"CACHED",
			"TOTAL_RESULT_SIZE",
			"ERROR_CODE",
			"RESPONSE_TIME",
			"REGISTERED",
			"NANO_TIME",

	};
	
	private final String[] SELECT_COLUMN = new String[]{
			"KEYWORDS"
	};
	
	private final String[] INDEX = new String[]{
			"COLLECTION_ID"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public QueryLogFullDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public QueryLogFullDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, SELECT_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	
	public Map<String, Integer> selectKeywords(Connection conn){
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		try {
//			String sql = "SELECT KEYWORDS FROM "+ TABLE + " WHERE TO_DAYS(NOW()) - TO_DAYS(REGISTERED) <= 30";
			String sql = "SELECT KEYWORD, SEARCH_CNT  FROM AUTO_COMPLETE_OCCURRENCES";
			PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sql);
			ResultSet rs = pstmt.executeQuery();
			int i=0;
			while(rs.next()){
				/*addCntMap(map, rs.getString(1));
				i++;
				if(i%1000==0){
//					System.out.println(i+"select logs works..");
				}*/
				map.put(rs.getString(1), rs.getInt(2));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	private void addCntMap(Map<String, Integer> map, String val){
		if(map.containsKey(val)){
			int cnt = map.get(val);
			map.put(val, ++cnt);
		} else {
			map.put(val, 1);
		}
	}
	
	public int insertAll(QueryLogFullVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getCollectionId());
			insertPstmt.setString(2, vo.getProfile());
			insertPstmt.setString(3, vo.getKeywords());
			insertPstmt.setString(4, vo.getAnalyzedResults());
			insertPstmt.setString(5, vo.getUserName());
			insertPstmt.setString(6, vo.getExtraData());
			insertPstmt.setString(7, vo.getCached());
			insertPstmt.setString(8, vo.getTotalResultSize());
			insertPstmt.setString(9, vo.getErrorCode());
			insertPstmt.setString(10, vo.getResponseTime());
			insertPstmt.setString(11, vo.getRegistered());
			insertPstmt.setString(12, vo.getNanoTime());
			if(batch) {
				insertPstmt.addBatch();
			} else {
				resultCnt = insertPstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}
	
	
	public void executeInsertBatch()	{DBConnector.excuteBatch(insertPstmt);}
	public void executeUpdateBatch() {DBConnector.excuteBatch(updatePstmt);}
	public void executeDeleteBatch()	{DBConnector.excuteBatch(deletePstmt);}
	
	public double executeInsertBatchTime()	{
		double start = System.nanoTime();
		DBConnector.excuteBatch(insertPstmt);
		double end = System.nanoTime();
		return end - start;
	}
}
