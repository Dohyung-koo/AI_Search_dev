package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ChannelVO;
import com.diquest.db.entity.MovieSumInfoVO;

public class MovieSumInfoDB {

	private final String[] ALL_COLUMN = new String[]{
			"CONTS_SEQ ",
			"REG_DATE ",
			"CATEGORY_ID ",
			"CATEGORY_NAME ",
			"BBS_ID ",
			"BBS_NAME ",
			"SUM_SALE ",
			"SUM_AUDIENCE ",
			"CODE ",
			"URL",
	};
	private final String[] SELECT_COLUMN = new String[]{
			"SUM_SALE ",
			"SUM_AUDIENCE ",
			"CODE ",
	};
	
	
	
	private final String[] INDEX = new String[]{
			"CONTS_SEQ"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public MovieSumInfoDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public MovieSumInfoDB(String tableName, boolean batch) {
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
	
	public Map<String, MovieSumInfoVO> select(){
		Map<String, MovieSumInfoVO> map = new HashMap<String, MovieSumInfoVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				MovieSumInfoVO vo = new MovieSumInfoVO();
				vo.setSumSale (rs.getString(1));
				vo.setSumAudience (rs.getString(2));
				map.put(rs.getString(3), vo);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public int insertAll(MovieSumInfoVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getContsSeq ());
			insertPstmt.setString(2, vo.getRegDate ());
			insertPstmt.setString(3, vo.getCategoryId ());
			insertPstmt.setString(4, vo.getCategoryName ());
			insertPstmt.setString(5, vo.getBbsId ());
			insertPstmt.setString(6, vo.getBbsName ());
			insertPstmt.setString(7, vo.getSumSale ());
			insertPstmt.setString(8, vo.getSumAudience ());
			insertPstmt.setString(9, vo.getCode ());
			insertPstmt.setString(10, vo.getUrl());
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
	
	public int update(MovieSumInfoVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getContsSeq ());
			updatePstmt.setString(2, vo.getRegDate ());
			updatePstmt.setString(3, vo.getCategoryId ());
			updatePstmt.setString(4, vo.getCategoryName ());
			updatePstmt.setString(5, vo.getBbsId ());
			updatePstmt.setString(6, vo.getBbsName ());
			updatePstmt.setString(7, vo.getSumSale ());
			updatePstmt.setString(8, vo.getSumAudience ());
			updatePstmt.setString(9, vo.getCode ());
			updatePstmt.setString(10, vo.getUrl());
			
			String[] whereToks = vo.getWhereIdx().split("_"); 
			for(int i=0,iSize = whereToks.length ; i<iSize ; i++){
				updatePstmt.setString(30 + i, whereToks[i]);
			}
			
			if(batch) {
				updatePstmt.addBatch();
			} else {
				resultCnt = updatePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}

	
	public int delete (ChannelVO vo){
		int resultCnt = -1;
		try {
			deletePstmt.setString(1, vo.getWhereIdx());
			if(batch) {
				deletePstmt.addBatch();
			} else {
				resultCnt = deletePstmt.executeUpdate();
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
