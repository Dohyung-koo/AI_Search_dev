package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.Golf2VO;
import com.diquest.db.entity.KidsSpcVO;
import com.diquest.db.entity.KidsVO;
import com.diquest.util.SqlUtil;

public class KidsClassSpcDB {

	private final String[] ALL_COLUMN = new String[]{
			"ALBUM_ID",
			"FIELD_ID",
			"FIELD_NAME",
			"DEPTH1_ID",
			"DEPTH1_NAME",
			"DEPTH2_ID",
			"DEPTH2_NAME",
			"VIEWING_NAME",
			"SEARCH_GROUP",
			
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
	
	public KidsClassSpcDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public KidsClassSpcDB(String tableName, boolean batch) {
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
	
	public boolean isExist(Connection conn, String albumId, String fieldId){
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, new String[]{"Count(*)"});
		SqlUtil.appendWhere(sb, INDEX, true, false);
		
		try {
			PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sb.toString());
			pstmt.setString(1 , albumId);
			pstmt.setString(2 , fieldId);
			
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
	
	public int insertExistCheck(KidsSpcVO vo, Connection conn) {
		if(isExist(conn,vo.getAlbumId() , vo.getFieldId())){
			return 0;
		}
		
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getAlbumId());
			insertPstmt.setString(2, vo.getFieldId());
			insertPstmt.setString(3, vo.getFieldNm());
			insertPstmt.setString(4, vo.getDepth1Id());
			insertPstmt.setString(5, vo.getDepth1Nm());
			insertPstmt.setString(6, vo.getDepth2Id());
			insertPstmt.setString(7, vo.getDepth2Nm());
			insertPstmt.setString(8, vo.getViewingNm());
			insertPstmt.setString(9, vo.getSearchGroup());
			
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
	
	public int insertAll(KidsSpcVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getAlbumId());
			insertPstmt.setString(2, vo.getFieldId());
			insertPstmt.setString(3, vo.getFieldNm());
			insertPstmt.setString(4, vo.getDepth1Id());
			insertPstmt.setString(5, vo.getDepth1Nm());
			insertPstmt.setString(6, vo.getDepth2Id());
			insertPstmt.setString(7, vo.getDepth2Nm());
			insertPstmt.setString(8, vo.getViewingNm());
			insertPstmt.setString(9, vo.getSearchGroup());
		
			
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
	
	public int insertexcept(KidsSpcVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getAlbumId());
			insertPstmt.setString(2, vo.getFieldId());
			insertPstmt.setString(3, vo.getFieldNm());
			insertPstmt.setString(4, vo.getDepth1Id());
			insertPstmt.setString(5, vo.getDepth1Nm());
			insertPstmt.setString(6, vo.getDepth2Id());
			insertPstmt.setString(7, vo.getDepth2Nm());
			insertPstmt.setString(8, vo.getViewingNm());
			insertPstmt.setString(9, vo.getSearchGroup());
			
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
	
	
	
	public int update (KidsSpcVO vo){
		int resultCnt = -1;
		try {
			//System.out.println("vo.getCatName() : "+vo.getCatName());
			updatePstmt.setString(1, vo.getAlbumId());
			updatePstmt.setString(2, vo.getFieldId());
			updatePstmt.setString(3, vo.getFieldNm());
			updatePstmt.setString(4, vo.getDepth1Id());
			updatePstmt.setString(5, vo.getDepth1Nm());
			updatePstmt.setString(6, vo.getDepth2Id());
			updatePstmt.setString(7, vo.getDepth2Nm());
			updatePstmt.setString(8, vo.getViewingNm());
			updatePstmt.setString(9, vo.getSearchGroup());
	
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
	
	public int delete (KidsSpcVO vo){
		int resultCnt = -1;
		try {
			deletePstmt.setString(1, vo.getAlbumId());
			deletePstmt.setString(2, vo.getFieldId());
			deletePstmt.setString(3, vo.getFieldNm());
			deletePstmt.setString(4, vo.getDepth1Id());
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
	
	public void executeInsertBatch()	{
//		System.out.println("인서트 배치 시작");
		DBConnector.excuteBatch(insertPstmt);}
	public void executeUpdateBatch() {
//		System.out.println("업데이트 배치 시작");
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
