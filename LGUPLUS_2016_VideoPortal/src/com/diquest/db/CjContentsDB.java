package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.CjContentsVO;
import com.diquest.util.SqlUtil;

public class CjContentsDB {

	private final String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"SVC_TYPE",
			"CAT_ID",
			"CAT_NAME",
			"CONTENT_ID",
			"CONTENT_TYPE",
			"CONTENT_NAME",
			"CONTENT_INFO",
			"IMG_URL",
			"HIT_CNT",
			"CHANNEL_ID",
			"PROGRAM_ID",
			"CLIP_ID",
			"CLIP_TITLE",
			"CLIP_CONTENT_ID",
			"CLIP_CONTENT_TITLE",
			"CORNERID",
			"CLIPORDER",
			"MEDIAURL",
			"CLIP_CONTENT_IMG",
			"PLAYTIME",
			"MEZZOAD",
			"ISFULLVOD",
			"BROADDATE",
			"CONTENTNUMBER",
			"START_DT",
			"END_DT",
			"BADGE_DATA",
			"BADGE_DATA2",
			"DEVICE_DATA",
			"MAIN_PROPERTY",
			"SUB_PROPERTY",

	};
	
	private final String[] INDEX = new String[]{
			"CAT_ID",
			"CONTENT_ID",
			"CLIP_ID",
			"CLIP_CONTENT_ID",
	};
	

	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
/*	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;*/
	AtomicInteger seq;
	
	public CjContentsDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public CjContentsDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	/*public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, DRAMA_SELECT);}
	*/
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	/*public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, ALL_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, INDEX);}
	*/
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}

	
	public boolean isExist(Connection conn, String catId, String contentId){
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, new String[]{"Count(*)"});
		SqlUtil.appendWhere(sb, INDEX, true, false);
		
		try {
			PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sb.toString());
			pstmt.setString(1 , catId);
			pstmt.setString(2 , contentId);

			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				int result = rs.getInt(1);
				if(result > 0){
					return true;
				} else {
					return false;
				}
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int insertExistCheck(CjContentsVO vo, Connection conn) {
		if(isExist(conn,vo.getCatId(), vo.getContentId())){
			return 0;
		}
		
		int resultCnt = -1;
		try {

			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getSvcType());
			insertPstmt.setString(3, vo.getCatId());
			insertPstmt.setString(4, vo.getCatName());
			insertPstmt.setString(5, vo.getContentId());
			insertPstmt.setString(6, vo.getContentType());
			insertPstmt.setString(7, vo.getContentName());
			insertPstmt.setString(8, vo.getContentInfo());
			insertPstmt.setString(9, vo.getImgUrl());
			insertPstmt.setString(10, vo.getHitCnt());
			insertPstmt.setString(11, vo.getChannelId());
			insertPstmt.setString(12, vo.getProgramId());
			insertPstmt.setString(13, vo.getClipId());
			insertPstmt.setString(14, vo.getClipTitle());
			insertPstmt.setString(15, vo.getClipContentId());
			insertPstmt.setString(16, vo.getClipContentTitle());
			insertPstmt.setString(17, vo.getCornerid());
			insertPstmt.setString(18, vo.getCliporder());
			insertPstmt.setString(19, vo.getMediaurl());
			insertPstmt.setString(20, vo.getClipContentImg());
			insertPstmt.setString(21, vo.getPlaytime());
			insertPstmt.setString(22, vo.getMezzoad());
			insertPstmt.setString(23, vo.getIsfullvod());
			insertPstmt.setString(24, vo.getBroaddate());
			insertPstmt.setString(25, vo.getContentnumber());
			insertPstmt.setString(26, vo.getStartDt());
			insertPstmt.setString(27, vo.getEndDt());
			insertPstmt.setString(28, vo.getBadgeData());
			insertPstmt.setString(29, vo.getBadgeData2());
			insertPstmt.setString(30, vo.getDeviceData());
			insertPstmt.setString(31, vo.getMainProperty());
			insertPstmt.setString(32, vo.getDeviceData());
			

			
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
	
	public int insertAll(CjContentsVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getSvcType());
			insertPstmt.setString(3, vo.getCatId());
			insertPstmt.setString(4, vo.getCatName());
			insertPstmt.setString(5, vo.getContentId());
			insertPstmt.setString(6, vo.getContentType());
			insertPstmt.setString(7, vo.getContentName());
			insertPstmt.setString(8, vo.getContentInfo());
			insertPstmt.setString(9, vo.getImgUrl());
			insertPstmt.setString(10, vo.getHitCnt());
			insertPstmt.setString(11, vo.getChannelId());
			insertPstmt.setString(12, vo.getProgramId());
			insertPstmt.setString(13, vo.getClipId());
			insertPstmt.setString(14, vo.getClipTitle());
			insertPstmt.setString(15, vo.getClipContentId());
			insertPstmt.setString(16, vo.getClipContentTitle());
			insertPstmt.setString(17, vo.getCornerid());
			insertPstmt.setString(18, vo.getCliporder());
			insertPstmt.setString(19, vo.getMediaurl());
			insertPstmt.setString(20, vo.getClipContentImg());
			insertPstmt.setString(21, vo.getPlaytime());
			insertPstmt.setString(22, vo.getMezzoad());
			insertPstmt.setString(23, vo.getIsfullvod());
			insertPstmt.setString(24, vo.getBroaddate());
			insertPstmt.setString(25, vo.getContentnumber());
			insertPstmt.setString(26, vo.getStartDt());
			insertPstmt.setString(27, vo.getEndDt());
			insertPstmt.setString(28, vo.getBadgeData());
			insertPstmt.setString(29, vo.getBadgeData2());
			insertPstmt.setString(30, vo.getDeviceData());
			insertPstmt.setString(31, vo.getMainProperty());
			insertPstmt.setString(32, vo.getSubProperty());
			
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
	
/*	public int insertexcept(VrContentsVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getCatGb());
			insertPstmt.setString(3, vo.getCatId());
			insertPstmt.setString(4, null);
			insertPstmt.setString(5, vo.getAlbumId());
			insertPstmt.setString(6, null);
			insertPstmt.setString(7, vo.getImgUrl());
			insertPstmt.setString(8, vo.getImgFileName());
			insertPstmt.setString(9, vo.getDescription());
			insertPstmt.setString(10, vo.getType());
			insertPstmt.setString(11, vo.getFlashYn());
			insertPstmt.setString(12, vo.getApplId());
			insertPstmt.setString(13, vo.getServiceGb());
			insertPstmt.setString(14, vo.getApplUrl());
			insertPstmt.setString(15, vo.getCreateDate());
			insertPstmt.setString(16, vo.getUpdateDate());
			insertPstmt.setString(17, vo.getImgType());
			insertPstmt.setString(18, vo.getPrice());
			insertPstmt.setString(19, vo.getPrInfo());
			insertPstmt.setString(20, vo.getRuntime());
			insertPstmt.setString(21, vo.getIs51Ch());
			insertPstmt.setString(22, vo.getIsHot());
			insertPstmt.setString(23, vo.getIsCaption());
			insertPstmt.setString(24, vo.getIsHd());
			insertPstmt.setString(25, vo.getCloseYn());
			insertPstmt.setString(26, vo.getThreedYn());
			insertPstmt.setString(27, vo.getFilterGb());
			insertPstmt.setString(28, null);
			insertPstmt.setString(29, vo.getOverseerName());
			insertPstmt.setString(30, vo.getContsSubname());
			insertPstmt.setString(31, vo.getGenre1());
			insertPstmt.setString(32, vo.getGenre2());
			insertPstmt.setString(33, vo.getGenre3());
			insertPstmt.setString(34, vo.getSeriesNo());
			insertPstmt.setString(35, vo.getPt());
			insertPstmt.setString(36, vo.getDes());
			insertPstmt.setString(37, vo.getPrice2());
			insertPstmt.setString(38, vo.getBroadDate());
			insertPstmt.setString(39, null);
			insertPstmt.setString(40, vo.getVoiceActor());
			insertPstmt.setString(41, vo.getBroadcaster());
			insertPstmt.setString(42, vo.getReleaseDate());
			insertPstmt.setString(43, vo.getIsFh());
			insertPstmt.setString(44, vo.getSerCatId());
			insertPstmt.setString(45, vo.getMultiMappingFlag());
			insertPstmt.setString(46, vo.getPosterFileUrl());
			insertPstmt.setString(47, vo.getPosterFileName10());
			insertPstmt.setString(48, vo.getPosterFileName30());
			insertPstmt.setString(49, vo.getKeywordDesc());
			insertPstmt.setString(50, vo.getTitleEng());
			insertPstmt.setString(51, vo.getDirectorEng());
			insertPstmt.setString(52, vo.getPlayerEng());
			insertPstmt.setString(53, null);
			insertPstmt.setString(54, null);
			insertPstmt.setString(55, vo.getTitleOrigin());
			insertPstmt.setString(56, vo.getWriterOrigin());
			insertPstmt.setString(57, vo.getPublicCnt());
			insertPstmt.setString(58, vo.getPointWatcha());
			insertPstmt.setString(59, vo.getGenreUxten());
			insertPstmt.setString(60, vo.getRetentionYn());
			insertPstmt.setString(61, vo.getKeyword());
			insertPstmt.setString(62, vo.getTitle());
			insertPstmt.setString(63, vo.getCastNameMax());
			insertPstmt.setString(64, vo.getCastNameMaxEng());
			insertPstmt.setString(65, vo.getActDispMax());
			insertPstmt.setString(66, vo.getActDispMaxEng());
			insertPstmt.setString(67, vo.getPointOrder());
			insertPstmt.setString(68, vo.getAlbumNo());
			insertPstmt.setString(69, vo.getStillImgName());
			insertPstmt.setString(70, vo.getCpProperty());
			insertPstmt.setString(71, vo.getCpPropertyUfx());
			insertPstmt.setString(72, vo.getThemeYn());
			insertPstmt.setString(73, vo.getSection());
			insertPstmt.setString(74, vo.getCatNameChosung());
			insertPstmt.setString(75, vo.getAlbumNameChosung());
			insertPstmt.setString(76, vo.getActorChosung());
			insertPstmt.setString(77, vo.getOverseerNameChosung());
			insertPstmt.setString(78, vo.getStarringActorChosung());
			insertPstmt.setString(79, vo.getVoiceActorChosung());
			insertPstmt.setString(80, vo.getCastNameChosung());
			insertPstmt.setString(81, vo.getKeywordChosung());
			insertPstmt.setString(82, vo.getTitleChosung());
			insertPstmt.setString(83, vo.getCastNameMaxChosung());
			insertPstmt.setString(84, vo.getActDispMaxChosung());
			//Choihu 2018.06.12 컬럼추가
			insertPstmt.setString(85, vo.getBroadDateSort());
			//Choihu 2018.06.25 공연 컬럼추가
			insertPstmt.setString(86, vo.getCuesheetType());
			insertPstmt.setString(87, vo.getActorId());
			insertPstmt.setString(88, vo.getConcertImgUrl());
			insertPstmt.setString(89, vo.getConcertImgFileName());
			insertPstmt.setString(90, vo.getCuesheetVideoType());
			//Choihu 20180808 VIDEO_TYPE 추가
			insertPstmt.setString(91, vo.getVideoType());
			insertPstmt.setString(92, vo.getVr_type());
			if(batch) {
				insertPstmt.addBatch();
			} else {
				resultCnt = insertPstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}*/
	
	
/*	
	public int update (VrVO vo){
		int resultCnt = -1;
		try {
			updatePstmt.setString(1, vo.getResultType());
			updatePstmt.setString(2, vo.getCatGb());
			updatePstmt.setString(3, vo.getCatId());
			updatePstmt.setString(4, vo.getCatName());
			updatePstmt.setString(5, vo.getAlbumId());
			updatePstmt.setString(6, vo.getAlbumName());
			updatePstmt.setString(7, vo.getImgUrl());
			updatePstmt.setString(8, vo.getImgFileName());
			updatePstmt.setString(9, vo.getDescription());
			updatePstmt.setString(10, vo.getType());
			updatePstmt.setString(11, vo.getFlashYn());
			updatePstmt.setString(12, vo.getApplId());
			updatePstmt.setString(13, vo.getServiceGb());
			updatePstmt.setString(14, vo.getApplUrl());
			updatePstmt.setString(15, vo.getCreateDate());
			updatePstmt.setString(16, vo.getUpdateDate());
			updatePstmt.setString(17, vo.getImgType());
			updatePstmt.setString(18, vo.getPrice());
			updatePstmt.setString(19, vo.getPrInfo());
			updatePstmt.setString(20, vo.getRuntime());
			updatePstmt.setString(21, vo.getIs51Ch());
			updatePstmt.setString(22, vo.getIsHot());
			updatePstmt.setString(23, vo.getIsCaption());
			updatePstmt.setString(24, vo.getIsHd());
			updatePstmt.setString(25, vo.getCloseYn());
			updatePstmt.setString(26, vo.getThreedYn());
			updatePstmt.setString(27, vo.getFilterGb());
			updatePstmt.setString(28, vo.getActor());
			updatePstmt.setString(29, vo.getOverseerName());
			updatePstmt.setString(30, vo.getContsSubname());
			updatePstmt.setString(31, vo.getGenre1());
			updatePstmt.setString(32, vo.getGenre2());
			updatePstmt.setString(33, vo.getGenre3());
			updatePstmt.setString(34, vo.getSeriesNo());
			updatePstmt.setString(35, vo.getPt());
			updatePstmt.setString(36, vo.getDes());
			updatePstmt.setString(37, vo.getPrice2());
			updatePstmt.setString(38, vo.getBroadDate());
			updatePstmt.setString(39, vo.getStarringActor());
			updatePstmt.setString(40, vo.getVoiceActor());
			updatePstmt.setString(41, vo.getBroadcaster());
			updatePstmt.setString(42, vo.getReleaseDate());
			updatePstmt.setString(43, vo.getIsFh());
			updatePstmt.setString(44, vo.getSerCatId());
			updatePstmt.setString(45, vo.getMultiMappingFlag());
			updatePstmt.setString(46, vo.getPosterFileUrl());
			updatePstmt.setString(47, vo.getPosterFileName10());
			updatePstmt.setString(48, vo.getPosterFileName30());
			updatePstmt.setString(49, vo.getKeywordDesc());
			updatePstmt.setString(50, vo.getTitleEng());
			updatePstmt.setString(51, vo.getDirectorEng());
			updatePstmt.setString(52, vo.getPlayerEng());
			updatePstmt.setString(53, vo.getCastNameEng());
			updatePstmt.setString(54, vo.getCastName());
			updatePstmt.setString(55, vo.getTitleOrigin());
			updatePstmt.setString(56, vo.getWriterOrigin());
			updatePstmt.setString(57, vo.getPublicCnt());
			updatePstmt.setString(58, vo.getPointWatcha());
			updatePstmt.setString(59, vo.getGenreUxten());
			updatePstmt.setString(60, vo.getRetentionYn());
			updatePstmt.setString(61, vo.getKeyword());
			updatePstmt.setString(62, vo.getTitle());
			updatePstmt.setString(63, vo.getCastNameMax());
			updatePstmt.setString(64, vo.getCastNameMaxEng());
			updatePstmt.setString(65, vo.getActDispMax());
			updatePstmt.setString(66, vo.getActDispMaxEng());
			updatePstmt.setString(67, vo.getPointOrder());
			updatePstmt.setString(68, vo.getAlbumNo());
			updatePstmt.setString(69, vo.getStillImgName());
			updatePstmt.setString(70, vo.getCpProperty());
			updatePstmt.setString(71, vo.getCpPropertyUfx());
			updatePstmt.setString(72, vo.getThemeYn());
			updatePstmt.setString(73, vo.getSection());
			updatePstmt.setString(74, vo.getCatNameChosung());
			updatePstmt.setString(75, vo.getAlbumNameChosung());
			updatePstmt.setString(76, vo.getActorChosung());
			updatePstmt.setString(77, vo.getOverseerNameChosung());
			updatePstmt.setString(78, vo.getStarringActorChosung());
			updatePstmt.setString(79, vo.getVoiceActorChosung());
			updatePstmt.setString(80, vo.getCastNameChosung());
			updatePstmt.setString(81, vo.getKeywordChosung());
			updatePstmt.setString(82, vo.getTitleChosung());
			updatePstmt.setString(83, vo.getCastNameMaxChosung());
			updatePstmt.setString(84, vo.getActDispMaxChosung());
			updatePstmt.setString(85 , vo.getBroadDateSort());
			updatePstmt.setString(86, vo.getCuesheetType());
			updatePstmt.setString(87, vo.getActorId());
			updatePstmt.setString(88, vo.getConcertImgUrl());
			updatePstmt.setString(89, vo.getConcertImgFileName());
			updatePstmt.setString(90, vo.getCuesheetVideoType());
			//Choihu 20180808 VIDEO_TYPE 추가
			updatePstmt.setString(91, vo.getVideoType());
			updatePstmt.setString(92, vo.getRun_time());
			//Choihu 20181130 Vr 추가
			updatePstmt.setString(93, vo.getVr_type());

			updatePstmt.setString(94 , vo.getResultType());
			updatePstmt.setString(95 , vo.getCatGb());
			updatePstmt.setString(96 , vo.getCatId());
			updatePstmt.setString(97 , vo.getAlbumId());
	
			//Choihu 2018.06.25 공연 컬럼추가
	
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
	
	public int delete (ShowVO vo){
		int resultCnt = -1;
		try {
			deletePstmt.setString(1, vo.getResultType());
			deletePstmt.setString(2, vo.getCatGb());
			deletePstmt.setString(3, vo.getCatId());
			deletePstmt.setString(4, vo.getAlbumId());
			if(batch) {
				deletePstmt.addBatch();
			} else {
				resultCnt = deletePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultCnt;
	}*/
	
	public void executeInsertBatch()	{DBConnector.excuteBatch(insertPstmt);}
/*	public void executeUpdateBatch() {DBConnector.excuteBatch(updatePstmt);}
	public void executeDeleteBatch()	{DBConnector.excuteBatch(deletePstmt);}
	*/
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