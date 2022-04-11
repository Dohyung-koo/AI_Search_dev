package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.I30AutoCompleteVO;

public class I30AutoCompleteDB {

	private final String[] ALL_COLUMN = new String[]{
			"KEYWORD",
			"CNT",
			"SEARCH_CNT",
			"IS_CAT_NAME",
			"IS_ALBUM_NAME",
			"IS_ACTOR",
			"IS_OVERSEER_NAME",
			"IS_STARRING_ACTOR",
			"IS_KEYWORD",
			"IS_CAST_NAME_MAX",
			"IS_CAST_NAME_MAX_ENG",
			"IS_ACT_DISP_MAX",
			"IS_ACT_DISP_MAX_ENG",
			"KEYWORD_CHOSUNG",
			"KIDS",
			"PR_INFO",
			"SECTION"
	};
	
	private final String[] INDEX = new String[]{
			"ID"
	};
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public I30AutoCompleteDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public I30AutoCompleteDB(String tableName, boolean batch) {
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
	
	public List<I30AutoCompleteVO> select(){
		List<I30AutoCompleteVO> list = new ArrayList<I30AutoCompleteVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				I30AutoCompleteVO vo = new I30AutoCompleteVO();
				vo.setKeyword(rs.getString("KEYWORD"));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(I30AutoCompleteVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getKeyword());
			insertPstmt.setString(2, vo.getCnt());
			insertPstmt.setString(3, vo.getSearchCnt());
			insertPstmt.setString(4, vo.getIsCatName());
			insertPstmt.setString(5, vo.getIsAlbumName());
			insertPstmt.setString(6, vo.getIsActor());
			insertPstmt.setString(7, vo.getIsOverseerName());
			insertPstmt.setString(8, vo.getIsStarringActor());
			insertPstmt.setString(9, vo.getIsKeyword());
			insertPstmt.setString(10, vo.getIsCastNameMax());
			insertPstmt.setString(11, vo.getIsCastNameMaxEng());
			insertPstmt.setString(12, vo.getIsActDispMax());
			insertPstmt.setString(13, vo.getIsActDispMaxEng());
			insertPstmt.setString(14, vo.getKeywordChosung());
			insertPstmt.setString(15, vo.getIsNscGb());
			insertPstmt.setString(16, vo.getPrInfo());
			insertPstmt.setString(17, vo.getSection());
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
