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
import com.diquest.db.entity.DramaMgmtVO;
import com.diquest.util.SqlUtil;

public class DramaMgmtDB {

	private final String[] ALL_COLUMN = new String[]{
			"COLLECTION_ID",
			"TERM_ID",
			"REL_TERM_ID",
			"IS_INSERT",
	};
	private final String[] UPDATE_COLUMN = new String[]{
			"IS_INSERT",
	};
	
	private final String[] SELECT_MAP_COLUMN = new String[]{
			"TERM_ID",
			"REL_TERM_ID",
	};
	private final String[] SELECT_COLUMN = new String[]{
			"REL_TERM_ID",
			"IS_INSERT",
	};
	
	private final String[] REL_TERM_ID_COLUMN = new String[]{
			"REL_TERM_ID"
	};
	private final String[] COLLECTION_ID_COLUMN = new String[]{
			"COLLECTION_ID"
	};
	private final String[] TERM_ID_COLUMN = new String[]{
			"TERM_ID"
	};
	private final String[] IS_INSERT_COLUMN = new String[]{
			"IS_INSERT"
	};
	
	private final String[] INDEX = new String[]{
			"COLLECTION_ID",
			"TERM_ID",
			"REL_TERM_ID",
	};
	
	private final String[] PAGE_INDEX = new String[]{
			"COLLECTION_ID",
			"TERM_ID",
			"IS_INSERT",
	};
	
	private final String[] DEL_INDEX = new String[]{
			"COLLECTION_ID",
			"TERM_ID",
	};
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public DramaMgmtDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public DramaMgmtDB(String tableName, boolean batch) {
		this.batch = batch;
		this.TABLE = tableName;
	}
	
	public int nextSeq() {
		return seq.incrementAndGet();
	}
	
	public void createSelectPstmt(Connection conn) {selectPstmt = DBConnector.createSelectPstmt(conn, TABLE, ALL_COLUMN);}
	public void createInsertPstmt(Connection conn) {insertPstmt = DBConnector.createInsertPstmt(conn, TABLE, ALL_COLUMN);}
	public void createUpdatePstmt(Connection conn) {updatePstmt = DBConnector.createUpdatePstmt(conn, TABLE, UPDATE_COLUMN, INDEX);}
	public void createDeletePstmt(Connection conn) {deletePstmt = DBConnector.createDeletePstmt(conn, TABLE, DEL_INDEX);}
	public void closeSelectPstmt(){		DBConnector.closePstmt(selectPstmt);		}
	public void closeInsertPstmt(){		DBConnector.closePstmt(insertPstmt);		}
	public void closeUpdatePstmt(){	DBConnector.closePstmt(updatePstmt);	}
	public void closeDeletePstmt(){		DBConnector.closePstmt(deletePstmt);	}
	
	
	public int getCount(Connection conn, DramaMgmtVO vo){
		int resultCnt=0;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(*) FROM " + TABLE+" ");
		SqlUtil.appendWhere(sb, INDEX, true, false);
		String sql = sb.toString();
		PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sql);
		try{
			pstmt.setString(1, vo.getCollectionId());
			pstmt.setString(2, vo.getTermId());
			pstmt.setString(3, vo.getRelTermId());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				resultCnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBConnector.closePstmt(pstmt);
		return resultCnt;
	}
	
	public int getPageCount(Connection conn, String collection, String whereKeyword, String whereSelectType){
		int resultCnt=0;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(DISTINCT TERM_ID) FROM " + TABLE+" ");
		SqlUtil.appendWhere(sb, COLLECTION_ID_COLUMN, false, false);
		SqlUtil.appendWhereSub(sb, IS_INSERT_COLUMN, true, false,true);
		if(!"".equals(whereKeyword)){
			SqlUtil.appendWhereSub(sb, TERM_ID_COLUMN, true, true,true);
		}
		String sql = sb.toString();
		PreparedStatement pstmt =  SqlUtil.generatePstmt(conn, sql);
		try{
			pstmt.setString(1, collection);
			if(whereSelectType.equals("insert")){
				pstmt.setString(2, "Y");
			} else {
				pstmt.setString(2, "N");
			}
			if(!"".equals(whereKeyword)){
				pstmt.setString(3, "%" + whereKeyword + "%");
			}
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				resultCnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBConnector.closePstmt(pstmt);
		return resultCnt;
	}
	
	public int insert(Connection conn, DramaMgmtVO vo) {
		return insert(conn, vo, true);
	}
	public int insert(Connection conn, DramaMgmtVO vo, boolean dataCheck) {
		int resultCnt = -1;
		if("".equals(vo.getRelTermId().trim()) || vo.getRelTermId() == null)
			return resultCnt;
		if(dataCheck)
			resultCnt = getCount(conn, vo);
		if(resultCnt > 0)
			return -1;
		
		try {
			insertPstmt.setString(1, vo.getCollectionId());
			insertPstmt.setString(2, vo.getTermId());
			insertPstmt.setString(3, vo.getRelTermId());
			insertPstmt.setString(4, vo.getIsInsert());
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
	
	public int update(Connection conn, DramaMgmtVO vo){
		int resultCnt = getCount(conn, vo);
		
		if(resultCnt>0){
			try {
				//field
				updatePstmt.setString(1,vo.getIsInsert());
				//where
				updatePstmt.setString(2, vo.getCollectionId());
				updatePstmt.setString(3, vo.getTermId());
				updatePstmt.setString(4, vo.getRelTermId());
				if(batch) {
					updatePstmt.addBatch();
				} else {
					resultCnt = updatePstmt.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			insert(conn, vo, false);
		}
		
		return resultCnt;
	}
	
	public void delete(Connection conn, DramaMgmtVO vo){
		try {
			deletePstmt.setString(1,vo.getCollectionId());
			deletePstmt.setString(2, vo.getTermId());
			if(batch) {
				deletePstmt.addBatch();
			} else {
				deletePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, List<String>> selectMap(Connection conn, String collection, String isInsert){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, SELECT_MAP_COLUMN);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COLUMN, true, false);
		SqlUtil.appendWhereSub(sb, IS_INSERT_COLUMN, true, false, true);
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		try{
			pstmt.setString(1, collection);
			pstmt.setString(2, isInsert);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				
				String term = rs.getString(1);
				String relTerm = rs.getString(2);
				
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
	
	public List<DramaMgmtVO> select(Connection conn, String collection, String where){
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, TABLE, SELECT_COLUMN);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COLUMN, true, false);
		SqlUtil.appendWhereSub(sb, TERM_ID_COLUMN, true, false, true);
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		List<DramaMgmtVO> list = new ArrayList<DramaMgmtVO>();
		try{
			pstmt.setString(1, collection);
			pstmt.setString(2, where);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				DramaMgmtVO vo = new DramaMgmtVO();
				vo.setRelTermId(rs.getString(1));
				vo.setIsInsert(rs.getString(2));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBConnector.closePstmt(pstmt);
		return list;
	}
	
	public List<DramaMgmtVO> pageSelect(Connection conn, String collection, String whereKeyword, String whereSelectType, int rowSize, int offSet){
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" DISTINCT TERM_ID ");
		sb.append(" from ");
		sb.append(TABLE);
		SqlUtil.appendWhere(sb, COLLECTION_ID_COLUMN, false, false);
		SqlUtil.appendWhereSub(sb, IS_INSERT_COLUMN, true, false,true);
		if(!"".equals(whereKeyword)){
			SqlUtil.appendWhereSub(sb, TERM_ID_COLUMN, true, true,true);
		}
		sb.append(" LIMIT ");
		sb.append(rowSize);
		sb.append(" OFFSET ");
		sb.append(offSet);
		
		PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sb.toString());
		List<DramaMgmtVO> list = new ArrayList<DramaMgmtVO>();
		try{
			pstmt.setString(1, collection);
			if(whereSelectType.equals("insert")){
				pstmt.setString(2, "Y");
			} else {
				pstmt.setString(2, "N");
			}
			if(!"".equals(whereKeyword)){
				pstmt.setString(3, "%" + whereKeyword + "%");
			}
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				DramaMgmtVO vo = new DramaMgmtVO();
				vo.setTermId(rs.getString(1));
				list.add(vo);
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
