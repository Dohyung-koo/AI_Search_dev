package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.Golf2VO;
import com.diquest.db.entity.KidsVO;
import com.diquest.util.SqlUtil;

public class KidsDB {

	private final String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"CAT_GB",
			"CAT_ID",
			"CAT_NAME",
			"ALBUM_ID",
			"ALBUM_NAME",
			"IMG_URL",
			"IMG_FILE_NAME",
			"DESCRIPTION",
			"TYPE",
			"FLASH_YN",
			"APPL_ID",
			"SERVICE_GB",
			"APPL_URL",
			"CREATE_DATE",
			"UPDATE_DATE",
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
			"FILTER_GB",
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
			"SER_CAT_ID",
			"MULTI_MAPPING_FLAG",
			"POSTER_FILE_URL",
			"POSTER_FILE_NAME_10",
			"POSTER_FILE_NAME_30",
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
			"SECTION",
/*			"KOFIC_SUPPORTING_ACTOR",
			"KOFIC_EXTRA_ACTOR",
			"KOFIC_SYNOPSIS",
			"KOFIC_KEYWORD",
			"KOFIC_FILM_STUDIO",
			"KOFIC_FILM_DISTRIBUTOR",
			"KOFIC_MAKE_NATION",
			"KOFIC_MAKE_YEAR",
			"KOFIC_SUM_SALE",
			"KOFIC_SUM_AUDIENCE",*/
			"CAT_NAME_CHOSUNG",
			"ALBUM_NAME_CHOSUNG",
			"ACTOR_CHOSUNG",
			"OVERSEER_NAME_CHOSUNG",
			"STARRING_ACTOR_CHOSUNG",
			"VOICE_ACTOR_CHOSUNG",
			"CAST_NAME_CHOSUNG",
			"KEYWORD_CHOSUNG",
			"TITLE_CHOSUNG",
			"CAST_NAME_MAX_CHOSUNG",
			"ACT_DISP_MAX_CHOSUNG",
			"BROAD_DATE_SORT",//Choihu 2018.06.12 컬럼추가
			/*//공연 추가 컬럼 2018.06.26
			"CUESHEET_TYPE",
			"ACTOR_ID",
			"CONCERT_IMG_URL",
			"CONCERT_IMG_FILE_NAME",
			"CUESHEET_VIDEO_TYPE",
			//Choihu 20180808 VIDEO_TYPE 추가*/
			"VIDEO_TYPE",
			"RUN_TIME",
			"YEAR",
			"SUB_TITLE",
			"ROUND",
			"STUDIO",
			"PRODUCER",
			"ACTORS_DISPLAY",
			"NSC_GB",
			"KIDS_GRADE",
	};
	
	private final String[] INDEX = new String[]{
			"RESULT_TYPE",
			"CAT_GB",
			"CAT_ID",
			"ALBUM_ID",
	};
	
	private final String[] DRAMA_SELECT = new String[]{
			"CAT_GB",
			"CAT_NAME",
			"ALBUM_NAME",
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
			// 신규 추가
			"DESCRIPTION",
			"VOICE_ACTOR",
			"DES",
			"KOFIC_SYNOPSIS",
	};
	
	
	private boolean batch;
	private String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public KidsDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public KidsDB(String tableName, boolean batch) {
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
	
	public int insertExistCheck(KidsVO vo, Connection conn) {
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
			insertPstmt.setString(86, vo.getVideoType());
			//Choihu 2018.06.25 공연 컬럼추가
			//Choihu 20181012 RUN_TIME 추가
			insertPstmt.setString(87, vo.getRun_time());
			insertPstmt.setString(88, vo.getYear());
			insertPstmt.setString(89, vo.getSubTitle());
			insertPstmt.setString(90, vo.getRound());
			insertPstmt.setString(91, vo.getStudio());
			insertPstmt.setString(92, vo.getProducer());
			insertPstmt.setString(93, vo.getActorDisplay());
			insertPstmt.setString(94, vo.getNscGb());
			insertPstmt.setString(95, vo.getKidsGrade());
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
	
	public int insertAll(KidsVO vo) {
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
			insertPstmt.setString(86, vo.getVideoType());
			//Choihu 20180808 VIDEO_TYPE 추가
			insertPstmt.setString(87, vo.getRun_time());
			insertPstmt.setString(88, vo.getYear());
			insertPstmt.setString(89, vo.getSubTitle());
			insertPstmt.setString(90, vo.getRound());
			insertPstmt.setString(91, vo.getStudio());
			insertPstmt.setString(92, vo.getProducer());
			insertPstmt.setString(93, vo.getActorDisplay());
			insertPstmt.setString(94, vo.getNscGb());
			insertPstmt.setString(95, vo.getKidsGrade());
			
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
	
	public int insertexcept(Golf2VO vo) {
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
			insertPstmt.setString(86, vo.getVideoType());
			//Choihu 2018.06.25 공연 컬럼추가
			insertPstmt.setString(87, vo.getRun_time());
			insertPstmt.setString(88, vo.getYear());
			insertPstmt.setString(89, vo.getSubTitle());
			insertPstmt.setString(90, vo.getRound());
			insertPstmt.setString(91, vo.getStudio());
			insertPstmt.setString(92, vo.getProducer());
			insertPstmt.setString(93, vo.getActorDisplay());
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
	
	
	
	public int update (KidsVO vo){
		int resultCnt = -1;
		try {
			//System.out.println("vo.getCatName() : "+vo.getCatName());
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
			insertPstmt.setString(86, vo.getVideoType());
			
			updatePstmt.setString(87, vo.getRun_time());
			updatePstmt.setString(88, vo.getYear());
			updatePstmt.setString(89, vo.getSubTitle());
			updatePstmt.setString(90, vo.getRound());
			updatePstmt.setString(91, vo.getStudio());
			updatePstmt.setString(92, vo.getProducer());
			updatePstmt.setString(93, vo.getActorDisplay());
			updatePstmt.setString(94, vo.getNscGb());
			updatePstmt.setString(95, vo.getKidsGrade());
			
			
			updatePstmt.setString(96 , vo.getResultType());
			updatePstmt.setString(97 , vo.getCatGb());
			updatePstmt.setString(98 , vo.getCatId());
			updatePstmt.setString(99 , vo.getAlbumId());
	
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
	
	public int delete (KidsVO vo){
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
	}
	
	public void executeInsertBatch()	{DBConnector.excuteBatch(insertPstmt);}
	public void executeUpdateBatch() {
		System.out.println("업데이트 배치 시작");
		DBConnector.excuteBatch(updatePstmt);}
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
