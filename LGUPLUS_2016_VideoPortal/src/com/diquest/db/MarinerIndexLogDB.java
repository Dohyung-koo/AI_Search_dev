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
import com.diquest.db.entity.DramaDataVO;
import com.diquest.db.entity.DramaMgmtVO;
import com.diquest.db.entity.MarinerIndexLogVO;
import com.diquest.util.SqlUtil;

public class MarinerIndexLogDB {

	private final String[] ALL_COLUMN = new String[]{
			"COLLECTION_ID",
			"INDEXING_TYPE",
			"EXECUTION_TYPE",
			"STATUS",
			"INSERT_COLLECTED",
			"UPDATE_COLLECTED",
			"DELETE_COLLECTED",
			"INSERTED",
			"UPDATED",
			"DELETED",
			"SERVICEABLE",
			"TOTAL_INDEXED",
			"STARTED",
			"ENDED",
			"COLLECTION_STARTED",
			"COLLECTION_ENDED",
			"REPOSITORY_STARTED",
			"REPOSITORY_ENDED",
			"INDEXING_STARTED",
			"INDEXING_ENDED",
			"NANO_TIME",
	};
	
	private final String[] INDEX = new String[]{
			"COLLECTION_ID",
	};
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public MarinerIndexLogDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public MarinerIndexLogDB(String tableName, boolean batch) {
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

	
	public List<MarinerIndexLogVO> selectLogMsg(Connection conn, String beforeTime, String nowTime){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(TABLE);
		sb.append(" WHERE ENDED > '");sb.append(beforeTime);sb.append("'");
		sb.append(" AND ");
		sb.append(" ENDED < '");sb.append(nowTime);sb.append("'");
		sb.append(" AND ");
		sb.append(" INDEXING_TYPE='F'");
		sb.append("AND");
		sb.append(" STATUS !='I'");
//		sb.append("SELECT * FROM IR01_INDEX_LOG WHERE ENDED > 20160810153000 AND ENDED < 20160810153900 AND INDEXING_TYPE='F' AND COLLECTION_STARTED='1970-01-01 09:00:00'");
		
		List<MarinerIndexLogVO> list = new ArrayList<MarinerIndexLogVO>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sb.toString());
			while(rs.next()){
				MarinerIndexLogVO vo = new MarinerIndexLogVO();
				vo.setCollectionId(rs.getString(1));
				vo.setIndexingType(rs.getString(2));
				vo.setExecutionType(rs.getString(3));
				vo.setStatus(rs.getString(4));
				vo.setInsertCollected(rs.getString(5));
				vo.setUpdateCollected(rs.getString(6));
				vo.setDeleteCollected(rs.getString(7));
				vo.setInserted(rs.getString(8));
				vo.setUpdated(rs.getString(9));
				vo.setDeleted(rs.getString(10));
				vo.setServiceable(rs.getString(11));
				vo.setTotalIndexed(rs.getString(12));
				vo.setStarted(rs.getString(13));
				vo.setEnded(rs.getString(14));
				vo.setCollectionStarted(rs.getString(15));
				vo.setCollectionEnded(rs.getString(16));
				vo.setRepositoryStarted(rs.getString(17));
				vo.setRepositoryEnded(rs.getString(18));
				vo.setIndexingStarted(rs.getString(19));
				vo.setIndexingEnded(rs.getString(20));
				vo.setNanoTime(rs.getString(21));
				list.add(vo);
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
