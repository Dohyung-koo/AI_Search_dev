package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;

public class IsLogDB {

	private final String[] ALL_COLUMN = new String[]{
			"LOG_ID",
			"CATEGORY_ID",
			"BBS_ID",
			"BBS_NAME",
			"URL",
			"DEPTH",
			"LOG_MSG",
			"REG_DATE",
			"LOG_LEVEL",
			"ERROR_CODE",
			"COLLECT_MODE",
	};
	
	private final String[] INDEX = new String[]{
			"LOG_ID",
	};
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;

	public IsLogDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public IsLogDB(String tableName, boolean batch) {
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

	public List<String> selectLogMsg(Connection conn, String catId, String bbsId, String beforeTime, String nowTime){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT LOG_MSG FROM ");
		sb.append(TABLE);
		sb.append(" WHERE CATEGORY_ID='");sb.append(catId);sb.append("'");
		sb.append(" AND ");
		sb.append(" BBS_ID='");sb.append(bbsId);sb.append("'");
		sb.append(" AND ");
		sb.append(" REG_DATE > '");sb.append(beforeTime);sb.append("'");
		sb.append(" AND ");
		sb.append(" REG_DATE < '");sb.append(nowTime);sb.append("'");
//		sb.append(" AND ");
//		sb.append(" COLLECT_MODE='W'");
//		sb.append("SELECT LOG_MSG FROM IS_LOG WHERE CATEGORY_ID='11' AND  BBS_ID='187' AND  REG_DATE > '20160808110000' AND  REG_DATE < '20160808110100'"
//);
		List<String> list = new ArrayList<String>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sb.toString());
			while(rs.next()){
				list.add(rs.getString(1));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
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
