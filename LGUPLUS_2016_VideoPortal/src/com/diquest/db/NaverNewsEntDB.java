package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.NaverNewsEntVO;
import com.diquest.util.SqlUtil;

public class NaverNewsEntDB {

	private final String[] ALL_COLUMN = new String[]{
			"CONTS_KEYWORD",
			"CONTS_SEQ",
			"REG_DATE",
			"CATEGORY_ID",
			"CATEGORY_NAME",
			"BBS_ID",
			"CONTS_CONTS",
			"CONTS_TITLE",
			"CONTS_URL",
			"CONTS_CAT",
	};
	private final String[] SELECT_COLUMN = new String[]{
			"CONTS_TITLE ",
			"CONTS_CONTS ",
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
	
	public NaverNewsEntDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public NaverNewsEntDB(String tableName, boolean batch) {
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
	
	public List<NaverNewsEntVO> selectWeek(Connection conn){
		List<NaverNewsEntVO> list = new ArrayList<NaverNewsEntVO>();
		
		try {
//			String sql = "SELECT CONTS_TITLE, CONTS_CONTS FROM  "+ TABLE + " WHERE TO_DAYS(NOW()) - TO_DAYS(REG_DATE) <= 14";
			String sql = "SELECT CONTS_TITLE FROM  "+ TABLE + " WHERE TO_DAYS(NOW()) - TO_DAYS(REG_DATE) <= 60";
			PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				NaverNewsEntVO vo = new NaverNewsEntVO();
				vo.setContsTitle(rs.getString(1));
//				vo.setContsConts(rs.getString(2));
				list.add(vo);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<NaverNewsEntVO> select(){
		List<NaverNewsEntVO> list = new ArrayList<NaverNewsEntVO>();
		try{
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				NaverNewsEntVO vo = new NaverNewsEntVO();
				vo.setContsTitle(rs.getString(1));
				vo.setContsConts(rs.getString(2));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(NaverNewsEntVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getContsKeyword());
			insertPstmt.setString(2, vo.getContsSeq());
			insertPstmt.setString(3, vo.getRegDate());
			insertPstmt.setString(4, vo.getCategoryId());
			insertPstmt.setString(5, vo.getCategoryName());
			insertPstmt.setString(6, vo.getBbsId());
			insertPstmt.setString(7, vo.getContsConts());
			insertPstmt.setString(8, vo.getContsTitle());
			insertPstmt.setString(9, vo.getContsUrl());
			insertPstmt.setString(10, vo.getContsCat());
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
	
	public int update(NaverNewsEntVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getContsKeyword());
			updatePstmt.setString(2, vo.getContsSeq());
			updatePstmt.setString(3, vo.getRegDate());
			updatePstmt.setString(4, vo.getCategoryId());
			updatePstmt.setString(5, vo.getCategoryName());
			updatePstmt.setString(6, vo.getBbsId());
			updatePstmt.setString(7, vo.getContsConts());
			updatePstmt.setString(8, vo.getContsTitle());
			updatePstmt.setString(9, vo.getContsUrl());
			updatePstmt.setString(10, vo.getContsCat());
			
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

	
	public int delete (NaverNewsEntVO vo){
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
