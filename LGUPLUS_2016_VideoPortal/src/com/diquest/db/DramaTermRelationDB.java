package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.DramaTermRelationVO;
import com.diquest.util.SqlUtil;

public class DramaTermRelationDB {

	//IR_DRAMA_TERM_RELATION DB
	private final String[] ALL_COLUMN = new String[]{
			"COLLECTION_ID",
			"INDEX_ID",
			"TERM_ID",
			"REL_TERM_ID",
			"REL_OPTION",
			"REL_TERM_ID_MOD",
			"SCORE",
			"FLAG_ACT",
	};
	private final String[] SELECT_COLUMN = new String[]{
			"REL_TERM_ID",
	};
	private final String[] SELECT_MAP_COLUMN = new String[]{
			"TERM_ID",
			"REL_TERM_ID"
	};
	
	private final String[] COLLECTION_ID_COL = new String[]{
			"COLLECTION_ID",
	};
	private final String[] TERM_ID_COL = new String[]{
			"TERM_ID",
	};
	
	private final String[] INDEX = new String[]{
			"COLLECTION_NAME",
			"TERM_ID"
	};

	
	private boolean batch;
	PreparedStatement selectPstmt = null;
	public String TABLE;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public DramaTermRelationDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public DramaTermRelationDB(String tableName, boolean batch) {
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
	
	/**
	 * 주제어 전체 페이징쿼리에 필요한 카운트
	 * @param conn
	 * @param collection
	 * @param where
	 * @return
	 */
	public int whereCount(Connection conn, String collection, String where){
		int result = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(DISTINCT TERM_ID) FROM " + TABLE+" ");
		SqlUtil.appendWhere(sb, COLLECTION_ID_COL, false, false);
		
		if(!"".equals(where)){
			SqlUtil.appendWhereSub(sb, TERM_ID_COL, false, true, true);
		}
		String sql = sb.toString();
		PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sql);
		try{
			pstmt.setString(1, collection);
			if(!"".equals(where)){
				pstmt.setString(2, "%"+where+"%");
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBConnector.closePstmt(pstmt);
		return result;
	}
	
	/**
	 * 주제어 전체 페이징쿼리
	 * @param conn
	 * @param collection
	 * @param where
	 * @param size
	 * @param offSet
	 * @return
	 */
	public List<DramaTermRelationVO> select(Connection conn, String collection, String where, int size, int offSet){
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" DISTINCT TERM_ID ");
		sb.append(" from ");
		sb.append(TABLE);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COL, false, false);
		if(!"".equals(where)){
			SqlUtil.appendWhereSub(sb, TERM_ID_COL, true, true,true);
		}
		sb.append(" LIMIT ");
		sb.append(size);
		sb.append(" OFFSET ");
		sb.append(offSet);
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		List<DramaTermRelationVO> list = new ArrayList<DramaTermRelationVO>();
		try{
			pstmt.setString(1, collection);
			if(!"".equals(where)){
				pstmt.setString(2, "%" + where + "%");
			}
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				DramaTermRelationVO vo = new DramaTermRelationVO();
				vo.setTermId(rs.getString(1));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBConnector.closePstmt(pstmt);
		return list;
	}
	
	/**
	 * 해당 주제어의 연관어 가져오기
	 * @param conn
	 * @param collection
	 * @param where
	 * @return
	 */
	public Map<String, List<String>> selectMap(Connection conn, String collection){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, SELECT_MAP_COLUMN);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COL, false, false);
		sb.append(" ORDER BY SCORE DESC ");
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		
		System.out.println(sb.toString());
		
		String tmp = null;
		System.err.println("### " + tmp);
		try{
			pstmt.setString(1, collection);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String term = rs.getString(1).trim();
				String relTerm = rs.getString(2).trim();
				
				if(map.containsKey(term)){
					List<String> list = map.get(term);
					list.add(relTerm);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(relTerm);
					map.put(term, list);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBConnector.closePstmt(pstmt);
		return map;
	}
	/**
	 * 해당 주제어의 연관어 가져오기
	 * @param conn
	 * @param collection
	 * @param where
	 * @return
	 */
	public List<DramaTermRelationVO> select(Connection conn, String collection, String where){
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, SELECT_COLUMN);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COL, false, false);
		SqlUtil.appendWhereSub(sb, TERM_ID_COL, true, false, true);
		sb.append(" ORDER BY SCORE DESC ");
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		List<DramaTermRelationVO> list = new ArrayList<DramaTermRelationVO>();
		try{
			//TODO 드라마 데이터가 공백이 포함된거랑 안포함된게 두가지로 들어가 있음..
			pstmt.setString(1, collection);
			pstmt.setString(2, " "+where );
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				DramaTermRelationVO vo = new DramaTermRelationVO();
				vo.setRelTermId(rs.getString(1));
				list.add(vo);
			}
			
			pstmt.setString(1, collection);
			pstmt.setString(2, where );
			rs = pstmt.executeQuery();
			while(rs.next()){
				DramaTermRelationVO vo = new DramaTermRelationVO();
				vo.setRelTermId(rs.getString(1));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBConnector.closePstmt(pstmt);
		return list;
	}
	
	/**
	 * 드라마 관리 주제어를 모두 가져온다.
	 * @param conn
	 * @param collection
	 * @param where
	 * @return
	 */
	public List<String> select(Connection conn, String collection){
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" DISTINCT TERM_ID ");
		sb.append(" from ");
		sb.append(TABLE);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COL, false, false);
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		List<String> list = new ArrayList<String>();
		try{
			pstmt.setString(1, collection);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1).trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DBConnector.closePstmt(pstmt);
		return list;
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
