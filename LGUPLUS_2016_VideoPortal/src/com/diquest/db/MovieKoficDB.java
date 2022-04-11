package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ChannelVO;
import com.diquest.db.entity.MovieKoficVO;

public class MovieKoficDB {

	private final String[] ALL_COLUMN = new String[]{
			"CONTS_SEQ",
			"REG_DATE",
			"CATEGORY_ID",
			"CATEGORY_NAME",
			"BBS_ID",
			"BBS_NAME",
			"GENRE",
			"SUM_AUDIENCE",
			"THUMBNAIL",
			"SUM_SALE",
			"FILM_FESTIVAL",
			"DIRECTOR",
			"ACTOR_01",
			"FILM_STUDIO",
			"FILM_DISTRIBUTORS",
			"ACTOR_02",
			"ACTOR_03",
			"SYNOPSIS",
			"MAKE_YEAR",
			"RELEASE_DATE",
			"MAKE_NATION",
			"MAKE_STATUS",
			"AKA",
			"NAME_EN",
			"SUMMARY",
			"DIRECTOR_CODE",
			"NAME_KO",
			"TYPE",
			"PROMOTE",
			"ACTOR_01_CODE",
			"URL",
			"ACTOR_02_CODE",
			"ACTOR_03_CODE",
			"CODE",
	};
	
	private final String[] SELECT_COLUMN = new String[]{
			"DIRECTOR",
			"FILM_STUDIO",
			"FILM_DISTRIBUTORS",
			"ACTOR_02",
			"ACTOR_03",
			"SYNOPSIS",
			"MAKE_YEAR",
			"MAKE_NATION",
			"NAME_KO",
			"CODE"
	};
	
	private final String[] INDEX = new String[]{
			"CONTS_SEQ"
	};
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public MovieKoficDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public MovieKoficDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, SELECT_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	
	public List<MovieKoficVO> select(){
		List<MovieKoficVO> list = new ArrayList<MovieKoficVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				MovieKoficVO vo = new MovieKoficVO();
				vo.setDirector(rs.getString(1));
				vo.setFilmStudio(rs.getString(2));
				vo.setFilmDistributors(rs.getString(3));
				vo.setActor02(rs.getString(4));
				vo.setActor03(rs.getString(5));
				vo.setSynopsis(rs.getString(6));
				vo.setMakeYear(rs.getString(7));
				vo.setMakeNation(rs.getString(8));
				vo.setNameKo(rs.getString(9));
				vo.setCode(rs.getString(10));
				list.add(vo);
				
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(MovieKoficVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getContsSeq());
			insertPstmt.setString(2, vo.getRegDate());
			insertPstmt.setString(3, vo.getCategoryId());
			insertPstmt.setString(4, vo.getCategoryName());
			insertPstmt.setString(5, vo.getBbsId());
			insertPstmt.setString(6, vo.getBbsName());
			insertPstmt.setString(7, vo.getGenre());
			insertPstmt.setString(8, vo.getSumAudience());
			insertPstmt.setString(9, vo.getThumbnail());
			insertPstmt.setString(10, vo.getSumSale());
			insertPstmt.setString(11, vo.getFilmFestival());
			insertPstmt.setString(12, vo.getDirector());
			insertPstmt.setString(13, vo.getActor01());
			insertPstmt.setString(14, vo.getFilmStudio());
			insertPstmt.setString(15, vo.getFilmDistributors());
			insertPstmt.setString(16, vo.getActor02());
			insertPstmt.setString(17, vo.getActor03());
			insertPstmt.setString(18, vo.getSynopsis());
			insertPstmt.setString(19, vo.getMakeYear());
			insertPstmt.setString(20, vo.getReleaseDate());
			insertPstmt.setString(21, vo.getMakeNation());
			insertPstmt.setString(22, vo.getMakeStatus());
			insertPstmt.setString(23, vo.getAka());
			insertPstmt.setString(24, vo.getNameEn());
			insertPstmt.setString(25, vo.getSummary());
			insertPstmt.setString(26, vo.getDirectorCode());
			insertPstmt.setString(27, vo.getNameKo());
			insertPstmt.setString(28, vo.getType());
			insertPstmt.setString(29, vo.getPromote());
			insertPstmt.setString(30, vo.getActor01Code());
			insertPstmt.setString(31, vo.getUrl());
			insertPstmt.setString(32, vo.getActor02Code());
			insertPstmt.setString(33, vo.getActor03Code());
			insertPstmt.setString(34, vo.getCode());
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
	
	public int update(MovieKoficVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getContsSeq());
			updatePstmt.setString(2, vo.getRegDate());
			updatePstmt.setString(3, vo.getCategoryId());
			updatePstmt.setString(4, vo.getCategoryName());
			updatePstmt.setString(5, vo.getBbsId());
			updatePstmt.setString(6, vo.getBbsName());
			updatePstmt.setString(7, vo.getGenre());
			updatePstmt.setString(8, vo.getSumAudience());
			updatePstmt.setString(9, vo.getThumbnail());
			updatePstmt.setString(10, vo.getSumSale());
			updatePstmt.setString(11, vo.getFilmFestival());
			updatePstmt.setString(12, vo.getDirector());
			updatePstmt.setString(13, vo.getActor01());
			updatePstmt.setString(14, vo.getFilmStudio());
			updatePstmt.setString(15, vo.getFilmDistributors());
			updatePstmt.setString(16, vo.getActor02());
			updatePstmt.setString(17, vo.getActor03());
			updatePstmt.setString(18, vo.getSynopsis());
			updatePstmt.setString(19, vo.getMakeYear());
			updatePstmt.setString(20, vo.getReleaseDate());
			updatePstmt.setString(21, vo.getMakeNation());
			updatePstmt.setString(22, vo.getMakeStatus());
			updatePstmt.setString(23, vo.getAka());
			updatePstmt.setString(24, vo.getNameEn());
			updatePstmt.setString(25, vo.getSummary());
			updatePstmt.setString(26, vo.getDirectorCode());
			updatePstmt.setString(27, vo.getNameKo());
			updatePstmt.setString(28, vo.getType());
			updatePstmt.setString(29, vo.getPromote());
			updatePstmt.setString(30, vo.getActor01Code());
			updatePstmt.setString(31, vo.getUrl());
			updatePstmt.setString(32, vo.getActor02Code());
			updatePstmt.setString(33, vo.getActor03Code());
			updatePstmt.setString(34, vo.getCode());
			
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
}
