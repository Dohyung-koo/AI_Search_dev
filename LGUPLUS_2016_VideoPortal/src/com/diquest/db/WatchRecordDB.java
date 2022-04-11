package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ThemeVO;
import com.diquest.db.entity.WatchRecordVO;

public class WatchRecordDB {
	private final String[] ALL_COLUMN = new String[]{
			"CAT_GB",
			"ALBUM_ID",
			"VIEW_COUNT",
			"DATE"
			
	};
	
	
	private final String[] INDEX = new String[]{
			"STAT_DATE"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	
	public WatchRecordDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public WatchRecordDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, ALL_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt_watch(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){DBConnector.closePstmt(selectPstmt);}
	public void closeInsertPstmt(){DBConnector.closePstmt(insertPstmt);}
	public void closeUpdatePstmt(){DBConnector.closePstmt(updatePstmt);}
	public void closeDeletePstmt(){DBConnector.closePstmt(deletePstmt);	}
	
	public HashMap<String, String> select(){
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				WatchRecordVO vo = new WatchRecordVO();
				vo.setCatGb(rs.getString(1));
				vo.setAlbumId(rs.getString(2));
				vo.setViewcount(rs.getString(3));
				
				StringBuilder sb = new StringBuilder();
				sb.append(vo.getCatGb());
				sb.append("_");
				sb.append(vo.getAlbumId());
				String key = sb.toString();
			
				if(map.containsKey(key)){
					String cnt = map.get(key);
					int cntmap=Integer.parseInt(cnt);
					int cntquery = Integer.parseInt(vo.getViewcount());
					int total= cntmap+cntquery;
					String totals = String.valueOf(total);
					map.put(key,totals);
				}else{
					map.put(key, vo.getViewcount());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public int insertAll(WatchRecordVO vo, String watch_date) {
		int resultCnt = -1;
		String watchdate = "";
			try {
				 watchdate = exformat(watch_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				insertPstmt.setString(1, vo.getCatGb());
				insertPstmt.setString(2, vo.getAlbumId());
				insertPstmt.setString(3, vo.getViewcount());
				insertPstmt.setString(4, watchdate);
			
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
	
	public int update(WatchRecordVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getCatGb());
			updatePstmt.setString(2, vo.getAlbumId());
			updatePstmt.setString(3, vo.getViewcount());
			
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
	
	public int delete (WatchRecordVO vo){
		int resultCnt = -1;
		try {
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
	
	public String exformat(String date) throws ParseException {
		SimpleDateFormat origin_format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_data = origin_format.parse(date);
		String new_data = new_format.format(origin_data);

		return new_data;

	}
	
}
