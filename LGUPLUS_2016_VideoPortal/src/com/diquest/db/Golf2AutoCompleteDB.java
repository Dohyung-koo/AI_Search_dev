package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.app.common.Method;
import com.diquest.app.common.Constant.DB2DB;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.Golf2AutoCompleteVO;
import com.diquest.db.entity.ShowAutoCompleteVO;
import com.diquest.db.entity.VrAutoCompleteVO;

public class Golf2AutoCompleteDB {

	private final String[] ALL_COLUMN = new String[]{
			"ORG_ID",
			"KEYWORD",
			"KEYWORD_CHOSUNG",
			"TYPE",
			"FIELD",
			
	};
	
	private final String[] INDEX = new String[]{
			"ID"
	};
	

/*	public static String[] PERSON_COLUMN = new String[] { "PERSON_ID", "REVISION_CODE", "NAME",};

	public static String[] GROUP_COLUMN = new String[] { "GROUP_ID", "REVISION_CODE", "NAME",};

	public static String[] LINK_COLUMN = new String[] { "GROUP_ID", "G_RV_CODE", "PERSON_ID", "P_RV_CODE",};
*/
	
	private boolean batch;
	public String TABLE;
	PreparedStatement selectPstmt = null;
	PreparedStatement insertPstmt = null;
	PreparedStatement updatePstmt = null;
	PreparedStatement deletePstmt = null;
	AtomicInteger seq;
	
	public Golf2AutoCompleteDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public Golf2AutoCompleteDB(String tableName, boolean batch) {
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
	
	public List<VrAutoCompleteVO> select(){
		List<VrAutoCompleteVO> list = new ArrayList<VrAutoCompleteVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				VrAutoCompleteVO vo = new VrAutoCompleteVO();
				vo.setKeyword(rs.getString("KEYWORD"));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(Golf2AutoCompleteVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getOrgId());
			insertPstmt.setString(2, vo.getKeyword());
			insertPstmt.setString(3, vo.getKeywordChosung());
			insertPstmt.setString(4, vo.getType());
			insertPstmt.setString(5, vo.getField());

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

/*	public void selectShowData(Connection dstConn, String tableName, ArrayList<HashMap<String, String>> showLinkData, ArrayList<HashMap<String, String>> showPersonGroupData, ArrayList<ShowAutoCompleteVO> showAutoCompleteKeywordVO) {
		String[] col = null;
		if("SHOW_PERSON_INFO".equals(tableName)) {
			col = PERSON_COLUMN;
		}else if("SHOW_GROUP_INFO".equals(tableName)) {
			col = GROUP_COLUMN;
		}else if("SHOW_PERSON_GROUP_LINK_INFO".equals(tableName)) {
			col = LINK_COLUMN;
		}
		PreparedStatement pstmt = DBConnector.createSelectPstmt(dstConn, tableName, col);
		try {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if("SHOW_PERSON_GROUP_LINK_INFO".equals(tableName)) {
					HashMap<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < col.length; i++) {
						map.put(col[i], rs.getString(col[i]));
					}
					showLinkData.add(map);
				}else {
					
					//Map 사용
					HashMap<String, String> map = new HashMap<String, String>();
					ShowAutoCompleteVO vo = new ShowAutoCompleteVO();
					
					for (int i = 0; i < col.length; i++) {
						String val =rs.getString(col[i]);
					
						if(i == 0) {
							map.put("ID", val);
							vo.setOrgId(val);
							if(val.startsWith("P")) {
								vo.setField("P");
							}else if(val.startsWith("G")) {
								vo.setField("G");
							} 
						}else {
							map.put(col[i], val);
							if(col[i].equals("NAME")) {
								vo.setKeyword(val);
								vo.setKeywordChosung(Method.normalizeChosung(val));
							}
						}
					}
					showPersonGroupData.add(map);
					showAutoCompleteKeywordVO.add(vo);
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		
	}

	public void deleteShowData(PreparedStatement Pstmt, String val) {
		try {
			Pstmt.setString(1, val);
			Pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/


	
}
