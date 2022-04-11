package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.commons.db.SqlUtil;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.EventVO;
import com.diquest.db.entity.HighLightVO;
import com.diquest.db.entity.VodVO;

public class EventDB {

	private final String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"SVC_TYPE",
			"REG_NO",
			"TITLE",
			"BANNER_URL",
			"START_DT",
			"END_DT",
			"PRIZE",
			"ANNOUNCE_YN",
			"EV_TYPE",
			"IS_NEW",
			"EV_STATUS",
			"KEYWORD",
			"OS_TYPE",
			"MODEL",
	};
	
	private final String[] INDEX = new String[]{
			"REG_NO"
	};
	
	private final String[] DRAMA_SELECT = new String[]{
			"TITLE",
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public EventDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public EventDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, DRAMA_SELECT);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	
	public List<EventVO> select(){
		List<EventVO> list = new ArrayList<EventVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				EventVO vo = new EventVO();
				vo.setTitle(rs.getString(1));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(EventVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getSvcType());
			insertPstmt.setString(3, vo.getRegNo());
			insertPstmt.setString(4, vo.getTitle());
			insertPstmt.setString(5, vo.getBannerUrl());
			insertPstmt.setString(6, vo.getStartDt());
			insertPstmt.setString(7, vo.getEndDt());
			insertPstmt.setString(8, vo.getPrize());
			insertPstmt.setString(9, vo.getAnnounceYn());
			insertPstmt.setString(10, vo.getEvType());
			insertPstmt.setString(11, vo.getIsNew());
			insertPstmt.setString(12, vo.getEvStatus());
			insertPstmt.setString(13, vo.getKeyword());
			insertPstmt.setString(14, vo.getOsType());
			insertPstmt.setString(15, vo.getModel());
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
	
	public int update(EventVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getResultType());
			updatePstmt.setString(2, vo.getSvcType());
			updatePstmt.setString(3, vo.getRegNo());
			updatePstmt.setString(4, vo.getTitle());
			updatePstmt.setString(5, vo.getBannerUrl());
			updatePstmt.setString(6, vo.getStartDt());
			updatePstmt.setString(7, vo.getEndDt());
			updatePstmt.setString(8, vo.getPrize());
			updatePstmt.setString(9, vo.getAnnounceYn());
			updatePstmt.setString(10, vo.getEvType());
			updatePstmt.setString(11, vo.getIsNew());
			updatePstmt.setString(12, vo.getEvStatus());
			updatePstmt.setString(13, vo.getKeyword());
			updatePstmt.setString(14, vo.getOsType());
			updatePstmt.setString(15, vo.getModel());
			
			String[] whereToks = vo.getWhereIdx().split("_"); 
			for(int i=0,iSize = whereToks.length ; i<iSize ; i++){
				updatePstmt.setString(16 + i, whereToks[i]);
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

	
	public int delete (EventVO vo){
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
	
	
	public void printInsertSql(){
		DBConnector.printInsertSql(TABLE, ALL_COLUMN);
	}
	public void printUpdateSql(){
		DBConnector.printUpdateSql(TABLE, ALL_COLUMN, INDEX);
	}
	public void printDeleteSql(){
		DBConnector.printDeleteSql(TABLE, ALL_COLUMN, INDEX);
	}
}
