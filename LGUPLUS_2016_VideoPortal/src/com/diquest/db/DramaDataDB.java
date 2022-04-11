package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.DramaDataVO;

public class DramaDataDB {

	private final String[] ALL_COLUMN = new String[]{
			"COLLECTION",
			"TERM",
			"REL_TERM",
	};
	
	private final String[] INDEX = new String[]{
			"ID",
	};
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public DramaDataDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public DramaDataDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, ALL_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, ALL_COLUMN);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}

	public int insert(Connection conn, DramaDataVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getCollection());
			insertPstmt.setString(2, vo.getTerm());
			insertPstmt.setString(3, vo.getRelTerm());
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
