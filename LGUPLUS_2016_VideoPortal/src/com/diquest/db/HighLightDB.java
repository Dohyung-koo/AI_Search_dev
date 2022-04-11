package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.HighLightVO;

public class HighLightDB {

	private final String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"SVC_TYPE",
			"CAT_ID",
			"CAT_NAME",
			"CONTENTS_ID",
			"CONTENTS_TYPE",
			"CONTENTS_NAME",
			"CONTENTS_INFO",
			"IMG_URL",
			"DURATION",
			"HIT_CNT",
			"H_ALBUM_ID",
			"H_CATEGORY_ID",
			"START_TIME",
			"END_TIME",
			"H_CONTENT_NAME",
			"H_SERIES_DESC",
			"H_SERIES_NO",
			"H_SPONSOR_NAME",
			"H_STILL_IMG",
			"REG_DATE",
			"BADGE_DATA",//Choihu 2019.04.25 "BADGE_DATA"값추가
			"BADGE_DATA2",//Choihu 2019.04.25 "BADGE_DATA2"값추가
			"ONAIR_DATE",//Choihu 2019.04.25 "ONAIR_DATE"값추가
			"DEVICE_DATA",//Choihu 공연고도화2차
			"START_DT",//Choihu 공연고도화2차
			"END_DT",//Choihu 공연고도화2차
			"MAIN_PROPERTY",
			"SUB_PROPERTY",
			"CONTENTS_NAME_CHOSUNG",

	};
	
	private final String[] INDEX = new String[]{
			"CAT_ID",
			"CONTENTS_ID",
	};
	
	private final String[] DRAMA_SELECT = new String[]{
			"CONTENTS_NAME",
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public HighLightDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public HighLightDB(String tableName, boolean batch) {
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
	
	public List<HighLightVO> select(){
		List<HighLightVO> list = new ArrayList<HighLightVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				HighLightVO vo = new HighLightVO();
				vo.setContentsName(rs.getString(1));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(HighLightVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getSvcType());
			insertPstmt.setString(3, vo.getCatId());
			insertPstmt.setString(4, vo.getCatName());
			insertPstmt.setString(5, vo.getContentsId());
			insertPstmt.setString(6, vo.getContentsType());
			insertPstmt.setString(7, vo.getContentsName());
			insertPstmt.setString(8, vo.getContentsInfo());
			insertPstmt.setString(9, vo.getImgUrl());
			insertPstmt.setString(10, vo.getDuration());
			insertPstmt.setString(11, vo.getHitCnt());
			insertPstmt.setString(12, vo.gethAlbumId());
			insertPstmt.setString(13, vo.gethCategoryId());
			insertPstmt.setString(14, vo.getStartTime());
			insertPstmt.setString(15, vo.getEndTime());
			insertPstmt.setString(16, vo.gethContentName());
			insertPstmt.setString(17, vo.gethSeriesDesc());
			insertPstmt.setString(18, vo.gethSeriesNo());
			insertPstmt.setString(19, vo.gethSponsorName());
			insertPstmt.setString(20, vo.gethStillImg());
			insertPstmt.setString(21, vo.getRegDate());
			insertPstmt.setString(22, vo.getBadgeData());
			insertPstmt.setString(23, vo.getBadgeData2());
			insertPstmt.setString(24, vo.getOnairDate());
			insertPstmt.setString(25, vo.getDeviceData());
			insertPstmt.setString(26, vo.getStartDt());
			insertPstmt.setString(27, vo.getEndDt());
			insertPstmt.setString(28, vo.getMainProperty());
			insertPstmt.setString(29, vo.getSubProperty());
			insertPstmt.setString(30, vo.getContentsNameChosung());
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
	
	public int update(HighLightVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getResultType());
			updatePstmt.setString(2, vo.getSvcType());
			updatePstmt.setString(3, vo.getCatId());
			updatePstmt.setString(4, vo.getCatName());
			updatePstmt.setString(5, vo.getContentsId());
			updatePstmt.setString(6, vo.getContentsType());
			updatePstmt.setString(7, vo.getContentsName());
			updatePstmt.setString(8, vo.getContentsInfo());
			updatePstmt.setString(9, vo.getImgUrl());
			updatePstmt.setString(10, vo.getDuration());
			updatePstmt.setString(11, vo.getHitCnt());
			updatePstmt.setString(12, vo.gethAlbumId());
			updatePstmt.setString(13, vo.gethCategoryId());
			updatePstmt.setString(14, vo.getStartTime());
			updatePstmt.setString(15, vo.getEndTime());
			updatePstmt.setString(16, vo.gethContentName());
			updatePstmt.setString(17, vo.gethSeriesDesc());
			updatePstmt.setString(18, vo.gethSeriesNo());
			updatePstmt.setString(19, vo.gethSponsorName());
			updatePstmt.setString(20, vo.gethStillImg());
			updatePstmt.setString(21, vo.getRegDate());
			updatePstmt.setString(22, vo.getBadgeData());
			updatePstmt.setString(23, vo.getBadgeData2());
			updatePstmt.setString(24, vo.getOnairDate());
			updatePstmt.setString(25, vo.getDeviceData());
			updatePstmt.setString(26, vo.getStartDt());
			updatePstmt.setString(27, vo.getEndDt());
			updatePstmt.setString(28, vo.getMainProperty());
			updatePstmt.setString(29, vo.getSubProperty());
			updatePstmt.setString(30, vo.getContentsNameChosung());
			
			String[] whereToks = vo.getWhereIdx().split("_"); 
			for(int i=0,iSize = whereToks.length ; i<iSize ; i++){
				updatePstmt.setString(29 + i, whereToks[i]);
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

	
	public int delete (HighLightVO vo){
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
