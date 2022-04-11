package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.VideoAutoCompleteVO;
import com.diquest.db.entity.ChannelVO;

public class ChannelDB {

	private final String[] ALL_COLUMN = new String[]{
			"SERVICE_ID",
			"SERVICE_NAME",
			"SERVICE_ENG_NAME",
			"CHANNEL_NO",
			"PROGRAM_ID",
			"PROGRAM_NAME",
			"THM_IMG_URL",
			"THM_IMG_FILE_NAME",
			"RATING",
			"BROAD_TIME",
			"DAY",
			"OVERSEER_NAME",
			"ACTOR",
			"P_NAME",
			"GENRE1",
			"GENRE2",
			"GENRE3",
			"SERIES_NO",
			"SUB_NAME",
			"BROAD_DATE",
			"LOCAL_AREA",
			"AV_RESOLUTION",
			"Caption_Flag",
			"Dvs_Flag",
			"IS_51_CH",
			"FILTERING_CODE",
			"CHNL_KEYWORD",
			"CHNL_ICON_URL",
			"CHNL_ICON_FILE_NAME",
			"PROGRAM_NAME_CHOSUNG",
	};
	
	private final String[] INDEX = new String[]{
			"SERVICE_ID",
			"PROGRAM_ID"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public ChannelDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public ChannelDB(String tableName, boolean batch) {
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
	
	public int insertAll(ChannelVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getServiceId());
			insertPstmt.setString(2, vo.getServiceName());
			insertPstmt.setString(3, vo.getServiceEngName());
			insertPstmt.setString(4, vo.getChannelNo());
			insertPstmt.setString(5, vo.getProgramId());
			insertPstmt.setString(6, vo.getProgramName());
			insertPstmt.setString(7, vo.getThmImgUrl());
			insertPstmt.setString(8, vo.getThmImgFileName());
			insertPstmt.setString(9, vo.getRating());
			insertPstmt.setString(10, vo.getBroadTime());
			insertPstmt.setString(11, vo.getDay());
			insertPstmt.setString(12, vo.getOverseerName());
			insertPstmt.setString(13, vo.getActor());
			insertPstmt.setString(14, vo.getPName());
			insertPstmt.setString(15, vo.getGenre1());
			insertPstmt.setString(16, vo.getGenre2());
			insertPstmt.setString(17, vo.getGenre3());
			insertPstmt.setString(18, vo.getSeriesNo());
			insertPstmt.setString(19, vo.getSubName());
			insertPstmt.setString(20, vo.getBroadDate());
			insertPstmt.setString(21, vo.getLocalArea());
			insertPstmt.setString(22, vo.getAvResolution());
			insertPstmt.setString(23, vo.getCaptionFlag());
			insertPstmt.setString(24, vo.getDvsFlag());
			insertPstmt.setString(25, vo.getIs51Ch());
			insertPstmt.setString(26, vo.getFilteringCode());
			insertPstmt.setString(27, vo.getChnlKeyword());
			insertPstmt.setString(28, vo.getChnlIconUrl());
			insertPstmt.setString(29, vo.getChnlIconFileName());
			insertPstmt.setString(30, vo.getProgramNameChosung());
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
	
	public int update(ChannelVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getServiceId());
			updatePstmt.setString(2, vo.getServiceName());
			updatePstmt.setString(3, vo.getServiceEngName());
			updatePstmt.setString(4, vo.getChannelNo());
			updatePstmt.setString(5, vo.getProgramId());
			updatePstmt.setString(6, vo.getProgramName());
			updatePstmt.setString(7, vo.getThmImgUrl());
			updatePstmt.setString(8, vo.getThmImgFileName());
			updatePstmt.setString(9, vo.getRating());
			updatePstmt.setString(10, vo.getBroadTime());
			updatePstmt.setString(11, vo.getDay());
			updatePstmt.setString(12, vo.getOverseerName());
			updatePstmt.setString(13, vo.getActor());
			updatePstmt.setString(14, vo.getPName());
			updatePstmt.setString(15, vo.getGenre1());
			updatePstmt.setString(16, vo.getGenre2());
			updatePstmt.setString(17, vo.getGenre3());
			updatePstmt.setString(18, vo.getSeriesNo());
			updatePstmt.setString(19, vo.getSubName());
			updatePstmt.setString(20, vo.getBroadDate());
			updatePstmt.setString(21, vo.getLocalArea());
			updatePstmt.setString(22, vo.getAvResolution());
			updatePstmt.setString(23, vo.getCaptionFlag());
			updatePstmt.setString(24, vo.getDvsFlag());
			updatePstmt.setString(25, vo.getIs51Ch());
			updatePstmt.setString(26, vo.getFilteringCode());
			updatePstmt.setString(27, vo.getChnlKeyword());
			updatePstmt.setString(28, vo.getChnlIconUrl());
			updatePstmt.setString(29, vo.getChnlIconFileName());
			
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

	
	public int delete (ChannelVO vo){
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
