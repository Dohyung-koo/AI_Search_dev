package com.diquest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.diquest.app.common.Constant.DB2DB;
import com.diquest.app.common.Constant.DB_NAME;
import com.diquest.app.common.Method;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ArAutoCompleteVO;
import com.diquest.db.entity.ShowAutoCompleteVO;

public class ArAutoCompleteDB {

	private final String[] ALL_COLUMN = new String[]{
			"ORG_ID",
			"KEYWORD",
			"KEYWORD_CHOSUNG",
			"TYPE",
			"FIELD",
			"ADULT_YN",
			
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
	
	public ArAutoCompleteDB(String tableName) {
		this.TABLE = tableName;
	}
	
	public ArAutoCompleteDB(String tableName, boolean batch) {
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
	
	public List<ArAutoCompleteVO> select(){
		List<ArAutoCompleteVO> list = new ArrayList<ArAutoCompleteVO>();
		try {
			ResultSet rs = selectPstmt.executeQuery();
			while(rs.next()){
				
				ArAutoCompleteVO vo = new ArAutoCompleteVO();
				vo.setKeyword(rs.getString("KEYWORD"));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int insertAll(ArAutoCompleteVO vo) {
		int resultCnt = -1;
		try {
			insertPstmt.setString(1, vo.getOrgId());
			insertPstmt.setString(2, vo.getKeyword());
			insertPstmt.setString(3, vo.getKeywordChosung());
			insertPstmt.setString(4, vo.getType());
			insertPstmt.setString(5, vo.getField());
			insertPstmt.setString(6, vo.getAdultYn());

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

	public void selectArData(Connection dstConn, String tableName, ArrayList<ArAutoCompleteVO> arAutoCompleteKeywordVO) {
		DBConnector.truncateTable(dstConn, DB_NAME.AR_AUTO_COMPLETE);
		String[] col = null;
		if("AR_CONTENTS".equals(tableName)) {
			col = DB2DB.AR_CONTENTS_COLUMN;
		}
		PreparedStatement pstmt = DBConnector.createSelectPstmt(dstConn, tableName, col);
		try {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				// Map 사용
				HashMap<String, String> map = new HashMap<String, String>();
				

				String key = "";
				String contentsType = "";
				String contentsName = "";
				String contentsArtist = "";
				String adultYn = "";
				for (int i = 0; i < col.length; i++) {
					if("CONTENTS_KEY".equals(col[i])) {
						key = rs.getString(col[i]);
					}else if("CONTENTS_TYPE".equals(col[i])) {
						contentsType = rs.getString(col[i]);
					}else if("CONTENTS_NAME".equals(col[i])) {
						contentsName = rs.getString(col[i]);
					}else if("CONTENTS_ARTIST".equals(col[i])) {
						contentsArtist = rs.getString(col[i]);
					}else if("ADULT_YN".equals(col[i])) {
						adultYn = rs.getString(col[i]);
					}
					if(col.length-1 == i) {
						if(!"".equals(contentsName) && null != contentsName) {
							ArAutoCompleteVO vo = new ArAutoCompleteVO();
							vo.setOrgId(key);
							vo.setKeyword(contentsName);
							vo.setKeywordChosung(Method.normalizeChosung(contentsName));
							vo.setType(contentsType);
							vo.setField("C");
							vo.setAdultYn(adultYn);
							arAutoCompleteKeywordVO.add(vo);
						}
						
						if(!"".equals(contentsArtist) && null != contentsArtist) {
							ArAutoCompleteVO vo = new ArAutoCompleteVO();
							vo.setOrgId(key);
							//System.out.println("contentsArtist : "+contentsArtist);
							vo.setKeyword(contentsArtist);
							vo.setKeywordChosung(Method.normalizeChosung(contentsArtist));
							vo.setType(contentsType);
							vo.setField("A");
							vo.setAdultYn(adultYn);
							arAutoCompleteKeywordVO.add(vo);
						}
					}
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		
	}

	
}
