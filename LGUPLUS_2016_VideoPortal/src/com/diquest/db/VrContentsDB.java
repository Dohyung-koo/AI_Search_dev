package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ShowVO;
import com.diquest.db.entity.VodVO;
import com.diquest.db.entity.VrContentsVO;
import com.diquest.db.entity.VrVO;
import com.diquest.util.SqlUtil;

public class VrContentsDB {

	private final String[] ALL_COLUMN = new String[]{
			"CTGRY_ID",
			"CTGRY_NM",
			"CNTNTS_GROUP_ID",
			"CNTNTS_GROUP_NM",
			"GENRE",
			"SERS_CNTNTS_AT",
			"SERS_DFK",
			"SERS_COMPT_AT",
			"MAKR",
			"USE_GRAD_CODE",
			"USE_GRAD_NAME",
			"USE_GRAD_AGE",
			"CONT_ID",
			"CONT_NM",
			"CONT_TME",
			"CONT_DC",
			"SVC_BEGIN_DT",
			"REPR_THUMB_URL",
			"CONT_IMAGE_URL",
			"GAME_VOD_URL",
			"CONT_URL",
			"CONT_CPCTY",
			"CONT_PRC",
			"GAME_HMD",
			"GAME_CNTLR",
			"GAME_CMT_DE",
			"MULT_GAME_AT",
			"NTWRK_USE_AT",
			"CNTRL_USE_AT",
			"PNRM_PLACE",
			"PNRM_SCRIN_CO",
			"PNRM_PLAY_URL",
			"CNTNTS_GROUP_NM_CHOSUNG",
			"CONT_NM_CHOSUNG",
			"REPR_THUMB_V_URL",
			"CONT_IMAGE_V_URL",

	};
	
	private final String[] INDEX = new String[]{
			"CTGRY_ID",
			"CNTNTS_GROUP_ID",
			"CONT_ID",
	};
	

	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
/*	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;*/
	AtomicInteger seq;
	
	public VrContentsDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public VrContentsDB(String tableName, boolean batch) {
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
	/*public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	*/
	/*public List<VodVO> select(){
		List<VodVO> list = new ArrayList<VodVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				VodVO vo = new VodVO();
				vo.setCatGb(rs.getString(1));
//				vo.setCatName(rs.getString(2));
				vo.setAlbumName(rs.getString(3));
				vo.setActor(rs.getString(4));
				vo.setOverseerName(rs.getString(5));
				vo.setStarringActor(rs.getString(6));
				vo.setKeyword(rs.getString(7));
				vo.setTitleEng(rs.getString(8));
				vo.setDirectorEng(rs.getString(9));
				vo.setPlayerEng(rs.getString(10));
				vo.setCastNameEng(rs.getString(11));
				vo.setCastName(rs.getString(12));
				vo.setTitleOrigin(rs.getString(13));
				vo.setWriterOrigin(rs.getString(14));
//				vo.setCastNameMax(rs.getString(15));
//				vo.setCastNameMaxEng(rs.getString(16));
				vo.setActDispMax(rs.getString(17));
				vo.setActDispMaxEng(rs.getString(18));
				// 신규 추가
				vo.setDescription(rs.getString(19));
				vo.setVoiceActor(rs.getString(20));
				vo.setDes(rs.getString(21));
				vo.setKoficSynopsis(rs.getString(22));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}*/
	
	public boolean isExist(Connection conn, String resultType, String catGb, String catId, String albumId){
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, new String[]{"Count(*)"});
		SqlUtil.appendWhere(sb, INDEX, true, false);
		
		try {
			PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sb.toString());
			pstmt.setString(1 , resultType);
			pstmt.setString(2 , catGb);
			pstmt.setString(3 , catId);
			pstmt.setString(4 , albumId);
			
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
	
	public int insertExistCheck(VrVO vo, Connection conn) {
		if(isExist(conn,vo.getResultType() , vo.getCatGb(), vo.getCatId(), vo.getAlbumId())){
			return 0;
		}
		
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getResultType());
			insertPstmt.setString(2, vo.getCatGb());
			insertPstmt.setString(3, vo.getCatId());
			insertPstmt.setString(4, vo.getCatName());
			insertPstmt.setString(5, vo.getAlbumId());
			insertPstmt.setString(6, vo.getAlbumName());
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
			insertPstmt.setString(28, vo.getActor());
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
			insertPstmt.setString(39, vo.getStarringActor());
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
			insertPstmt.setString(53, vo.getCastNameEng());
			insertPstmt.setString(54, vo.getCastName());
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
			//Choihu 20181012 RUN_TIME 추가
			insertPstmt.setString(92, vo.getRun_time());
			insertPstmt.setString(93, vo.getVr_type());
			
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
	
	public int insertAll(VrContentsVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getCtgryId());
			insertPstmt.setString(2, vo.getCtgryNm());
			insertPstmt.setString(3, vo.getCntntsGroupId());
			insertPstmt.setString(4, vo.getCntntsGroupNm());
			insertPstmt.setString(5, vo.getGenre());
			insertPstmt.setString(6, vo.getSersCntntsAt());
			insertPstmt.setString(7, vo.getSersDfk());
			insertPstmt.setString(8, vo.getSersComptAt());
			insertPstmt.setString(9, vo.getMakr());
			insertPstmt.setString(10, vo.getUseGradCode());
			insertPstmt.setString(11, vo.getUseGradName());
			insertPstmt.setString(12, vo.getUseGradAge());
			insertPstmt.setString(13, vo.getContId());
			insertPstmt.setString(14, vo.getContNm());
			insertPstmt.setString(15, vo.getContTme());
			insertPstmt.setString(16, vo.getContDc());
			insertPstmt.setString(17, vo.getSvcBeginDt());
			insertPstmt.setString(18, vo.getReprThumbUrl());
			insertPstmt.setString(19, vo.getContImageUrl());
			insertPstmt.setString(20, vo.getGameVodUrl());
			insertPstmt.setString(21, vo.getContUrl());
			insertPstmt.setString(22, vo.getContCpcty());
			insertPstmt.setString(23, vo.getContPrc());
			insertPstmt.setString(24, vo.getGameHmd());
			insertPstmt.setString(25, vo.getGameCntlr());
			insertPstmt.setString(26, vo.getGameCmtDe());
			insertPstmt.setString(27, vo.getMultGameAt());
			insertPstmt.setString(28, vo.getNtwrkUseAt());
			insertPstmt.setString(29, vo.getCntrlUseAt());
			insertPstmt.setString(30, vo.getPnrmPlace());
			insertPstmt.setString(31, vo.getPnrmScrinCo());
			insertPstmt.setString(32, vo.getPnrmPlayUrl());
			insertPstmt.setString(33, vo.getCntntsGroupNmChosung());
			insertPstmt.setString(34, vo.getContNmChosung());
			insertPstmt.setString(35, vo.getThumbVUrl());
			insertPstmt.setString(36, vo.getImageVUrl());


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
