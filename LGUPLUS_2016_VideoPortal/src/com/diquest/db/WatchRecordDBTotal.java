package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.WatchRecordVO;
import com.diquest.db.entity.WatchRecordVOTotal;
import com.mysql.jdbc.ResultSetMetaData;

public class WatchRecordDBTotal {
	private final String[] ALL_COLUMN = new String[] { "CAT_GB", "ALBUM_ID", "VIEW_COUNT", "VIEW_POINT" };

	private final String[] SEL_COLUMN = new String[] { "CAT_GB", "ALBUM_ID", "VIEW_COUNT", "DATE" };

	private final String[] INDEX = new String[] { "STAT_DATE" };

	private boolean batch;
	private String TABLE = "WATCH_RECORD";
	private String TABLE_SECTION= "WATCH_RECORD_SECTION";
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;

	public WatchRecordDBTotal(String tableName) {
		this.TABLE = tableName;
	}

	public WatchRecordDBTotal(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}

	public int nextSeq() {
		return seq.incrementAndGet();
	}

	
	public void createSelectPstmt2(Connection conn) {selectPstmt = DBConnector.createSelectPstmt_watch(conn, TABLE, SEL_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE_SECTION, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){DBConnector.closePstmt(selectPstmt);}
	public void closeInsertPstmt(){DBConnector.closePstmt(insertPstmt);}
	public void closeUpdatePstmt(){DBConnector.closePstmt(updatePstmt);}
	public void closeDeletePstmt(){DBConnector.closePstmt(deletePstmt);	}
	

	public HashMap<String, Integer> select() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			int max = 0;
			int min = 10000000;
			System.out.println();
			while (rs.next()) {
				WatchRecordVOTotal vo = new WatchRecordVOTotal();
				vo.setCatGb(rs.getString(1));
				vo.setAlbumId(rs.getString(2));
				String watch_cnt = rs.getString(3);
				int watch_cntparse = Integer.parseInt(watch_cnt);
				if (max < watch_cntparse) {
					max = watch_cntparse;
				}
				if (min > watch_cntparse) {
					min = watch_cntparse;
				}
				vo.setViewCount(watch_cntparse);

				StringBuilder sb = new StringBuilder();
				sb.append(vo.getCatGb());
				sb.append("_");
				sb.append(vo.getAlbumId());
				String key = sb.toString();

				if (map.containsKey(key)) {
					int cnt = map.get(key);
					int cntquery = vo.getViewCount();
					int total = cnt + cntquery;
					map.put(key, total);
				} else {
					map.put(key, vo.getViewCount());
				}
			}
			
//			map.put("maxvalue",max);
//			map.put("minvalue",min);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public int insertAll(WatchRecordVOTotal vo) {
		int resultCnt = -1;
		String sTimenew = null;

		try {
			insertPstmt.setString(1, vo.getCatGb());
			insertPstmt.setString(2, vo.getAlbumId());
			insertPstmt.setInt(3, vo.getViewCount());
			insertPstmt.setString(4, vo.getViewPoint());

			if (batch) {
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
			for (int i = 0, iSize = whereToks.length; i < iSize; i++) {
				updatePstmt.setString(20 + i, whereToks[i]);
			}

			if (batch) {
				updatePstmt.addBatch();
			} else {
				resultCnt = updatePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}

	public int delete(WatchRecordVO vo) {
		int resultCnt = -1;
		try {
			if (batch) {
				deletePstmt.addBatch();
			} else {
				resultCnt = deletePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}

	public void executeInsertBatch() {
		DBConnector.excuteBatch(insertPstmt);
	}

	public void executeUpdateBatch() {
		DBConnector.excuteBatch(updatePstmt);
	}

	public void executeDeleteBatch() {
		DBConnector.excuteBatch(deletePstmt);
	}

	public double executeInsertBatchTime() {
		double start = System.nanoTime();
		DBConnector.excuteBatch(insertPstmt);
		double end = System.nanoTime();
		return end - start;
	}

	public void printInsertSql() {
		DBConnector.printInsertSql(TABLE, ALL_COLUMN);
	}

	public void printUpdateSql() {
		DBConnector.printUpdateSql(TABLE, ALL_COLUMN, INDEX);
	}

	public void printDeleteSql() {
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
