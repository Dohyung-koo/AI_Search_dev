package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.commons.db.SqlUtil;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.UflixPackageVO;
import com.diquest.db.entity.UflixSeriesVO;
import com.diquest.db.entity.VodVO;

public class UflixSeriesDB {

	private final String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"CAT_GB",
			"ID",
			"NAME",
			"CAT_ID",
			"IMG_URL",
			"IMG_FILE_NAME",
			"IMG_TYPE",
			"PRICE",
			"PR_INFO",
			"RUNTIME",
			"IS_51_CH",
			"IS_HOT",
			"IS_CAPTION",
			"IS_HD",
			"CLOSE_YN",
			"3D_YN",
			"SERVICE_GB",
			"FILTER_GB",
			"SUMMARY ",
			"ACTOR",
			"OVERSEER_NAME",
			"CONTS_SUBNAME",
			"GENRE1",
			"GENRE2",
			"GENRE3",
			"SERIES_NO",
			"PT",
			"DES",
			"PRICE_2",
			"BROAD_DATE",
			"STARRING_ACTOR",
			"VOICE_ACTOR",
			"BROADCASTER",
			"RELEASE_DATE",
			"IS_FH",
			"MULTI_MAPPING_FLAG",
			"KEYWORD_DESC",
			"TITLE_ENG",
			"DIRECTOR_ENG",
			"PLAYER_ENG",
			"CAST_NAME_ENG",
			"CAST_NAME",
			"TITLE_ORIGIN",
			"WRITER_ORIGIN",
			"PUBLIC_CNT",
			"POINT_WATCHA",
			"GENRE_UXTEN",
			"RETENTION_YN",
			"KEYWORD",
			"TITLE",
			"CAST_NAME_MAX",
			"CAST_NAME_MAX_ENG",
			"ACT_DISP_MAX",
			"ACT_DISP_MAX_ENG",
			"POINT_ORDER",
			"ALBUM_NO",
			"STILL_IMG_NAME",
			"CP_PROPERTY",
			"CP_PROPERTY_UFX",
			"THEME_YN",
			"KOFIC_SUPPORTING_ACTOR",
			"KOFIC_EXTRA_ACTOR",
			"KOFIC_SYNOPSIS",
			"KOFIC_KEYWORD",
			"KOFIC_FILM_STUDIO",
			"KOFIC_FILM_DISTRIBUTOR",
			"KOFIC_MAKE_NATION",
			"KOFIC_MAKE_YEAR",
			"KOFIC_SUM_SALE",
			"KOFIC_SUM_AUDIENCE",
			"NAME_CHOSUNG",
			"ACTOR_CHOSUNG",
			"STARRING_ACTOR_CHOSUNG",
			"VOICE_ACTOR_CHOSUNG",
			"CAST_NAME_CHOSUNG",
			"KEYWORD_CHOSUNG",
	};
	
	private final String[] INDEX = new String[]{
			"RESULT_TYPE",
			"CAT_GB",
			"CAT_ID",
			"NAME",
	};
	
	
	private final String[] DRAMA_SELECT = new String[]{
			"CAT_GB",
			"NAME",
			"ACTOR",
			"OVERSEER_NAME",
			"STARRING_ACTOR",
			"KEYWORD",
			"TITLE_ENG",
			"DIRECTOR_ENG",
			"PLAYER_ENG",
			"CAST_NAME_ENG",
			"CAST_NAME",
			"TITLE_ORIGIN",
			"WRITER_ORIGIN",
			"CAST_NAME_MAX",
			"CAST_NAME_MAX_ENG",
			"ACT_DISP_MAX",
			"ACT_DISP_MAX_ENG",
			"SUMMARY",
			"VOICE_ACTOR",
			"DES",
			"KOFIC_SYNOPSIS"
	};
	
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public UflixSeriesDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public UflixSeriesDB(String tableName, boolean batch) {
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
	
	public List<UflixSeriesVO> select(){
		List<UflixSeriesVO> list = new ArrayList<UflixSeriesVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				UflixSeriesVO vo = new UflixSeriesVO();
				vo.setCatGb(rs.getString(1));
				vo.setName(rs.getString(2));
				vo.setActor(rs.getString(3));
				vo.setOverseerName(rs.getString(4));
				vo.setStarringActor(rs.getString(5));
				vo.setKeyword(rs.getString(6));
				vo.setTitleEng(rs.getString(7));
				vo.setDirectorEng(rs.getString(8));
				vo.setPlayerEng(rs.getString(9));
				vo.setCastNameEng(rs.getString(10));
				vo.setCastName(rs.getString(11));
				vo.setTitleOrigin(rs.getString(12));
				vo.setWriterOrigin(rs.getString(13));
//				vo.setCastNameMax(rs.getString(14));
//				vo.setCastNameMaxEng(rs.getString(15));
				vo.setActDispMax(rs.getString(16));
				vo.setActDispMaxEng(rs.getString(17));
				
				// 신규 추가
				vo.setSummary(rs.getString(18));
				vo.setVoiceActor(rs.getString(19));
				vo.setDes(rs.getString(20));
				vo.setKoficSynopsis(rs.getString(21));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(UflixSeriesVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getCatGb());
			insertPstmt.setString(3, vo.getId());
			insertPstmt.setString(4, vo.getName());
			insertPstmt.setString(5, vo.getCatId());
			insertPstmt.setString(6, vo.getImgUrl());
			insertPstmt.setString(7, vo.getImgFileName());
			insertPstmt.setString(8, vo.getImgType());
			insertPstmt.setString(9, vo.getPrice());
			insertPstmt.setString(10, vo.getPrInfo());
			insertPstmt.setString(11, vo.getRuntime());
			insertPstmt.setString(12, vo.getIs51Ch());
			insertPstmt.setString(13, vo.getIsHot());
			insertPstmt.setString(14, vo.getIsCaption());
			insertPstmt.setString(15, vo.getIsHd());
			insertPstmt.setString(16, vo.getCloseYn());
			insertPstmt.setString(17, vo.getThreedYn());
			insertPstmt.setString(18, vo.getServiceGb());
			insertPstmt.setString(19, vo.getFilterGb());
			insertPstmt.setString(20, vo.getSummary ());
			insertPstmt.setString(21, vo.getActor());
			insertPstmt.setString(22, vo.getOverseerName());
			insertPstmt.setString(23, vo.getContsSubname());
			insertPstmt.setString(24, vo.getGenre1());
			insertPstmt.setString(25, vo.getGenre2());
			insertPstmt.setString(26, vo.getGenre3());
			insertPstmt.setString(27, vo.getSeriesNo());
			insertPstmt.setString(28, vo.getPoint());
			insertPstmt.setString(29, vo.getDes());
			insertPstmt.setString(30, vo.getPrice2());
			insertPstmt.setString(31, vo.getBroadDate());
			insertPstmt.setString(32, vo.getStarringActor());
			insertPstmt.setString(33, vo.getVoiceActor());
			insertPstmt.setString(34, vo.getBroadcaster());
			insertPstmt.setString(35, vo.getReleaseDate());
			insertPstmt.setString(36, vo.getIsFh());
			insertPstmt.setString(37, vo.getMultiMappingFlag());
			insertPstmt.setString(38, vo.getKeywordDesc());
			insertPstmt.setString(39, vo.getTitleEng());
			insertPstmt.setString(40, vo.getDirectorEng());
			insertPstmt.setString(41, vo.getPlayerEng());
			insertPstmt.setString(42, vo.getCastNameEng());
			insertPstmt.setString(43, vo.getCastName());
			insertPstmt.setString(44, vo.getTitleOrigin());
			insertPstmt.setString(45, vo.getWriterOrigin());
			insertPstmt.setString(46, vo.getPublicCnt());
			insertPstmt.setString(47, vo.getPointWatcha());
			insertPstmt.setString(48, vo.getGenreUxten());
			insertPstmt.setString(49, vo.getRetentionYn());
			insertPstmt.setString(50, vo.getKeyword());
			insertPstmt.setString(51, vo.getTitle());
			insertPstmt.setString(52, vo.getCastNameMax());
			insertPstmt.setString(53, vo.getCastNameMaxEng());
			insertPstmt.setString(54, vo.getActDispMax());
			insertPstmt.setString(55, vo.getActDispMaxEng());
			insertPstmt.setString(56, vo.getPointOrder());
			insertPstmt.setString(57, vo.getAlbumNo());
			insertPstmt.setString(58, vo.getStillImgName());
			insertPstmt.setString(59, vo.getCpProperty());
			insertPstmt.setString(60, vo.getCpPropertyUfx());
			insertPstmt.setString(61, vo.getThemeYn());
			insertPstmt.setString(62, vo.getKoficSupportingActor());
			insertPstmt.setString(63, vo.getKoficExtraActor());
			insertPstmt.setString(64, vo.getKoficSynopsis());
			insertPstmt.setString(65, vo.getKoficKeyword());
			insertPstmt.setString(66, vo.getKoficFilmStudio());
			insertPstmt.setString(67, vo.getKoficFilmDistributor());
			insertPstmt.setString(68, vo.getKoficMakeNation());
			insertPstmt.setString(69, vo.getKoficMakeYear());
			insertPstmt.setString(70, vo.getKoficSumSale());
			insertPstmt.setString(71, vo.getKoficSumAudience());
			insertPstmt.setString(72, vo.getNameChosung());
			insertPstmt.setString(73, vo.getActorChosung());
			insertPstmt.setString(74, vo.getStarringActorChosung());
			insertPstmt.setString(75, vo.getVoiceActorChosung());
			insertPstmt.setString(76, vo.getCastNameChosung());
			insertPstmt.setString(77, vo.getKeywordChosung());
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
	
	public int update(UflixSeriesVO vo) {
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getResultType());
			updatePstmt.setString(2, vo.getCatGb());
			updatePstmt.setString(3, vo.getId());
			updatePstmt.setString(4, vo.getName());
			updatePstmt.setString(5, vo.getCatId());
			updatePstmt.setString(6, vo.getImgUrl());
			updatePstmt.setString(7, vo.getImgFileName());
			updatePstmt.setString(8, vo.getImgType());
			updatePstmt.setString(9, vo.getPrice());
			updatePstmt.setString(10, vo.getPrInfo());
			updatePstmt.setString(11, vo.getRuntime());
			updatePstmt.setString(12, vo.getIs51Ch());
			updatePstmt.setString(13, vo.getIsHot());
			updatePstmt.setString(14, vo.getIsCaption());
			updatePstmt.setString(15, vo.getIsHd());
			updatePstmt.setString(16, vo.getCloseYn());
			updatePstmt.setString(17, vo.getThreedYn());
			updatePstmt.setString(18, vo.getServiceGb());
			updatePstmt.setString(19, vo.getFilterGb());
			updatePstmt.setString(20, vo.getSummary ());
			updatePstmt.setString(21, vo.getActor());
			updatePstmt.setString(22, vo.getOverseerName());
			updatePstmt.setString(23, vo.getContsSubname());
			updatePstmt.setString(24, vo.getGenre1());
			updatePstmt.setString(25, vo.getGenre2());
			updatePstmt.setString(26, vo.getGenre3());
			updatePstmt.setString(27, vo.getSeriesNo());
			updatePstmt.setString(28, vo.getPoint());
			updatePstmt.setString(29, vo.getDes());
			updatePstmt.setString(30, vo.getPrice2());
			updatePstmt.setString(31, vo.getBroadDate());
			updatePstmt.setString(32, vo.getStarringActor());
			updatePstmt.setString(33, vo.getVoiceActor());
			updatePstmt.setString(34, vo.getBroadcaster());
			updatePstmt.setString(35, vo.getReleaseDate());
			updatePstmt.setString(36, vo.getIsFh());
			updatePstmt.setString(37, vo.getMultiMappingFlag());
			updatePstmt.setString(38, vo.getKeywordDesc());
			updatePstmt.setString(39, vo.getTitleEng());
			updatePstmt.setString(40, vo.getDirectorEng());
			updatePstmt.setString(41, vo.getPlayerEng());
			updatePstmt.setString(42, vo.getCastNameEng());
			updatePstmt.setString(43, vo.getCastName());
			updatePstmt.setString(44, vo.getTitleOrigin());
			updatePstmt.setString(45, vo.getWriterOrigin());
			updatePstmt.setString(46, vo.getPublicCnt());
			updatePstmt.setString(47, vo.getPointWatcha());
			updatePstmt.setString(48, vo.getGenreUxten());
			updatePstmt.setString(49, vo.getRetentionYn());
//			updatePstmt.setString(50, vo.getKeyword()); //20160617 키워드 데이터 제외
			updatePstmt.setString(51, vo.getTitle());
			updatePstmt.setString(52, vo.getCastNameMax());
			updatePstmt.setString(53, vo.getCastNameMaxEng());
			updatePstmt.setString(54, vo.getActDispMax());
			updatePstmt.setString(55, vo.getActDispMaxEng());
			updatePstmt.setString(56, vo.getPointOrder());
			updatePstmt.setString(57, vo.getAlbumNo());
			updatePstmt.setString(58, vo.getStillImgName());
			updatePstmt.setString(59, vo.getCpProperty());
			updatePstmt.setString(60, vo.getCpPropertyUfx());
			updatePstmt.setString(61, vo.getThemeYn());
			
			String[] whereToks = vo.getWhereIdx().split("_"); 
			for(int i=0,iSize = whereToks.length ; i<iSize ; i++){
				updatePstmt.setString(62 + i, whereToks[i]);
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

	public int delete (UflixSeriesVO vo){
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
