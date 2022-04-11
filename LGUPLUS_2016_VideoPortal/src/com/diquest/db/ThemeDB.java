package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ThemeVO;

public class ThemeDB {
	private final String[] ALL_COLUMN = new String[]{
			"THEME_ID",
			"THEME_NM_M",
			"THEME_NM_T",
			"THEME_NM_P",
			"THEME_IMG_M",
			"THEME_IMG_T",
			"THEME_IMG_P",
			"KEYWORD",
			"CONTENT_NM",
			"ACTOR",
			"DIRECTOR",
	};
	
	
	private final String[] INDEX = new String[]{
			"THEME_ID"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	
	public ThemeDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public ThemeDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, ALL_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	
	public List<ThemeVO> select(){
		List<ThemeVO> list = new ArrayList<ThemeVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				ThemeVO vo = new ThemeVO();
				vo.setKeyword(rs.getString(1));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(ThemeVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getThemeId());
			insertPstmt.setString(2, vo.getThemeNmM());
			insertPstmt.setString(3, vo.getThemeNmT());
			insertPstmt.setString(4, vo.getThemeNmP());
			insertPstmt.setString(5, vo.getThemeImgM());
			insertPstmt.setString(6, vo.getThemeImgT());
			insertPstmt.setString(7, vo.getThemeImgP());
			insertPstmt.setString(8, vo.getKeyword());
			insertPstmt.setString(9, vo.getContentNm());
			insertPstmt.setString(10, vo.getActor());
			insertPstmt.setString(11, vo.getDirector());
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
	
	public int update(ThemeVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getThemeId());
			updatePstmt.setString(2, vo.getThemeNmM());
			updatePstmt.setString(3, vo.getThemeNmT());
			updatePstmt.setString(4, vo.getThemeNmP());
			updatePstmt.setString(5, vo.getThemeImgM());
			updatePstmt.setString(6, vo.getThemeImgT());
			updatePstmt.setString(7, vo.getThemeImgP());
			updatePstmt.setString(8, vo.getKeyword());
			updatePstmt.setString(9, vo.getContentNm());
			updatePstmt.setString(10, vo.getActor());
			updatePstmt.setString(11, vo.getDirector());
		
		
			
			String[] whereToks = vo.getWhereIdx().split("_"); 
			for(int i=0,iSize = whereToks.length ; i<iSize ; i++){
				updatePstmt.setString(20 + i, whereToks[i]);
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
	
	public int delete (ThemeVO vo){
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
