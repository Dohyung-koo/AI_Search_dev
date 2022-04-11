package com.diquest.app.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.diquest.app.common.Constant.DB2DB;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.db.connector.DBConnector;


public class DbToDbManager {
	private Connection oracleConn;
	private DBConnector oracleConnector;

	private Connection mySqlConn;
	private DBConnector mySqlConnector;
	
	PreparedStatement pstm = null; 
	ResultSet rs = null;

	public void Start() {
		openDB();
		closeDB();
	}
	
	/**
	 * DB2DB DB연결
	 */
	public void openDB() {
		
		System.out.println("OPEN ORACLE DB [" + DB_INFO.ORACLE_URL + "]");
		String oraUrl = DB_INFO.ORACLE_URL;
		String oraUser = DB_INFO.ORACLE_USER;
		String oraPassword = DB_INFO.ORACLE_PASSWORD;
		String oraDriver = DB_INFO.ORACLE_DRIVER;
		oracleConnector = new DBConnector(oraUrl, oraUser, oraPassword, oraDriver);
		oracleConn = oracleConnector.getConnection();

		System.out.println("OPEN DB [" + DB_INFO.URL + "]");
		String url = DB_INFO.DB_URL;
		String user = DB_INFO.DB_USER;
		String password = DB_INFO.DB_PASSWORD;
		String driver = DB_INFO.DB_DRIVER;
		mySqlConnector = new DBConnector(url, user, password, driver);
		mySqlConn = mySqlConnector.getConnection();

		db2DbStart();
	}
	
	/**
	 * DB2DB DB끊기
	 */
	public void closeDB() {
		/*mySqlConnector.closeConnection(mySqlConn);
		oracleConnector.closeConnection(oracleConn);
		*/
		
		System.out.println("CLOSED DB [" + DB_INFO.ORACLE_URL + "]");
		System.out.println("CLOSED DB [" + DB_INFO.URL + "]");
	
		DBConnector.closeConnection(oracleConn);
		DBConnector.closeConnection(mySqlConn);
	
		DBConnector.closePstmt(pstm);
		
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * DB2DB 작업시작, Oracle -> MySQL 데이터 전송
	 */
	private void db2DbStart() {
		try {
			int my_Count = tableDataCount(mySqlConn, pstm, rs);
			long time = System.currentTimeMillis(); 

			SimpleDateFormat dayTime = new SimpleDateFormat("hh");

			String str = dayTime.format(new Date(time));
			
			int hour = Integer.parseInt(str);
			if(hour == 10) {
				getOracleData(oracleConn, mySqlConn, pstm, rs);
			}else {
				if (my_Count == 3) {
					dataExtractTask(oracleConn, mySqlConn, pstm, rs);
				} else {
					getOracleData(oracleConn, mySqlConn, pstm, rs);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * DB2DB Oracle 테이블 3개 전송 VW_PERSON_GROUP_LINK_INFO,VW_PERSON_INFO,VW_GROUP_INFO
	 */
	private void getOracleData(Connection oraConn, Connection myConn, PreparedStatement pstm, ResultSet rs) {
		ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		//System.out.println("start oracle -> mysql");
		for (int i = 0; i < DB2DB.ORACLE_TABLE.length; i++) {
			DBConnector.truncateTable(myConn, DB2DB.MYSQL_TABLE[i]);
			
			HashMap<String, String> tableDataMap = new HashMap<String, String>();

			String tableName = DB2DB.ORACLE_TABLE[i];
			String query = DB2DB.all_Select(tableName);
			try {
				rs = sendQuery(oraConn, query, pstm);
				while (rs.next()) {
					tableDataMap = mapTask(tableName, rs);;
					mapList.add(tableDataMap);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// MySQl에 Data Insert
			insertSQL(myConn, DB2DB.MYSQL_TABLE[i], mapList, rs, pstm,null);
			mapList.clear();
		}

		//LOOKUP_TABLE 테이블 삭제
		try {
			System.out.println("ORACLE -> MYSQL DATA MOVEMENT SUCCESS");
			pstm = oraConn.prepareStatement("DELETE FROM "+DB2DB.LOOKUP_TABLE);
			pstm.executeUpdate();
			System.out.println("DELETE "+DB2DB.LOOKUP_TABLE+" TABLE");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//DBConnector.truncateTable(oraConn, DB2DB.LOOKUP_TABLE);
		
	}
	
	/**
	 * DB2DB 데이터 추출 작업 Oracle PERSON_GROUP_LOOKUP 데이터로 Mysql Update
	 */
	private void dataExtractTask(Connection oraConn, Connection myConn, PreparedStatement pstm, ResultSet rs) {
		int insertCnt = 0;
		int updateCnt = 0;
		int deleteCnt = 0;
		String q = DB2DB.all_Select(DB2DB.LOOKUP_TABLE) + DB2DB.whereSet(DB2DB.LOOKUP_TABLE, null, null);
		//System.out.println("LOOKUP_Q : "+q);
		//변경될 쿼리 가져오기
		try {
			rs = sendQuery(oraConn, q, pstm);

			ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
			// PERSON_GROUP_LOOKUP 테이블 정보 가져오기
			while (rs.next()) {
				HashMap<String, String> map = mapTask(DB2DB.LOOKUP_TABLE, rs);
				mapList.add(map);
			}

			System.out.println("ORACLE PERSON_GROUP_LOOKUP DATA TOTAL CNT:" + mapList.size());

			// PERSON_GROUP_LOOKUP 테이블 정보로 작업하기
			for (int i = 0; i < mapList.size(); i++) {
				//System.out.println("mapList.size() for()");
				
				HashMap<String, String> map = mapList.get(i);
				String seq = map.get("SEQ");
				String linkJob = map.get("LINK_JOB");
				String tableName = map.get("VIEW_NAME");
				String pgId = map.get("PG_ID");
				String revisionCode = map.get("REVISION_CODE");
				// 오라클에서 정보 가져오기
				String searchquery = DB2DB.all_Select(tableName) + DB2DB.whereSet(tableName, pgId, revisionCode);
		
				rs = sendQuery(oraConn, searchquery, pstm);
				ArrayList<HashMap<String, String>> searchMapList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> dataMap = new HashMap<String, String>();
				while (rs.next()) {
					dataMap = mapTask(tableName, rs);
					searchMapList.add(dataMap);
				}
				tableName = tableName.replaceAll("VW_", "SHOW_");
				// System.out.println("linkJob : "+linkJob);
				// 인서트
	
				if ("I".equals(linkJob)) {
					insertCnt += insertSQL(myConn, tableName, searchMapList, rs, pstm, seq);
				} else if ("U".equals(linkJob)) {
					updateCnt += updateSQL(myConn, tableName, searchMapList, rs, pstm, seq);
				} else if ("D".equals(linkJob)) {
					deleteCnt += deleteSQL(myConn, tableName, searchMapList, rs, pstm, seq);
				}

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("INSERT MYSQL TOTAL CNT:" + insertCnt);
		System.out.println("UPDATE MYSQL TOTAL CNT:" + updateCnt);
		System.out.println("DELETE MYSQL TOTAL CNT:" + deleteCnt);
		
	}
	
	/**
	 * MySql 데이터 확인
	 */
	private int tableDataCount(Connection myConn, PreparedStatement pstm, ResultSet rs)
			throws SQLException {
		int a = 0;
		for (int i = 0; i < DB2DB.MYSQL_TABLE.length; i++) {
			String query = DB2DB.all_Select(DB2DB.MYSQL_TABLE[i]);
			rs = sendQuery(myConn, query, pstm);
			while (rs.next()) {
				if(rs.getRow()==1) {
					a++;
				}
			}
		}
		return a;
	}
	
	/**
	 * DB2DB 데이터 추출 작업 Oracle PERSON_GROUP_LOOKUP 데이터로 Mysql Update 후 PERSON_GROUP_LOOKUP 테이블 데이터 제거
	 */
	private void oracleTableUpdate(int cnt, String seq) {
		StringBuffer buf = new StringBuffer();
		try {
			if (1 == cnt) {
				DB2DB.generateDeleteSql(buf, DB2DB.LOOKUP_TABLE, new String[] { "SEQ" });
				pstm = oracleConn.prepareStatement(buf.toString());
				pstm.setString(1, seq);
				pstm.executeUpdate();
			} else {
				DB2DB.generateUpdateSql(buf, DB2DB.LOOKUP_TABLE, new String[] { "SYNC_STATUS" });
				pstm = oracleConn.prepareStatement(buf.toString());
				pstm.setString(1, "2");
				pstm.setString(2, seq);
				pstm.executeUpdate();
				System.out.println("\tUPDATE FAIL SEQ : "+seq);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * DB2DB 데이터 DELETE 작업
	 * */
	private int deleteSQL(Connection myConn, String tableName, ArrayList<HashMap<String, String>> mapList, ResultSet rs,
			PreparedStatement pstm, String seq) {
		int totalCnt = 0;
		for (int i = 0; i < mapList.size(); i++) {
			int cnt = 0;
			
			String[] columns = DB2DB.column(tableName);
			StringBuffer buf = new StringBuffer();
			DB2DB.generateDeleteSql(buf, tableName, columns);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = mapList.get(i);
			
			try {
				pstm = myConn.prepareStatement(buf.toString());
				for (int j = 0; j < columns.length; j++) {
					// 컬럼에 값 넣어주기
					pstm.setString(j + 1, map.get(columns[j]));
				}
				cnt = pstm.executeUpdate();
				totalCnt += cnt;
				oracleTableUpdate(cnt,seq);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				oracleTableUpdate(cnt,seq);
			}
		}
		return totalCnt;
	}
	
	/**
	 * DB2DB 데이터 UPDATE 작업
	 * */
	public int updateSQL(Connection myConn, String tableName, ArrayList<HashMap<String, String>> mapList, ResultSet rs,
			PreparedStatement pstm, String seq) {
		int totalCnt = 0;
		for (int i = 0; i < mapList.size(); i++) {
			int cnt = 0;
			String[] columns = DB2DB.column(tableName);
			StringBuffer buf = new StringBuffer();
			DB2DB.generateUpdateSql(buf, tableName, columns);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = mapList.get(i);
			
			try {
				pstm = myConn.prepareStatement(buf.toString());
				String id = "";
				String code = "";
				for (int j = 0; j < columns.length; j++) {
					if(j==0) {
						id = map.get(columns[j]);
					}else if(j==1) {
						code = map.get(columns[j]);
					}
					pstm.setString(j + 1, map.get(columns[j]));
				}
				
				pstm.setString(columns.length+1, id);
				pstm.setString(columns.length+2, code);
				
				cnt = pstm.executeUpdate();
				totalCnt +=cnt;
				oracleTableUpdate(cnt,seq);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				oracleTableUpdate(cnt,seq);
			}
		}
		return totalCnt;
	}

	/**
	 * DB2DB 데이터 INSERT 작업
	 * */
	public int insertSQL(Connection myConn, String tableName, ArrayList<HashMap<String, String>> mapList, ResultSet rs, PreparedStatement pstm, String seq) {
		int totalCnt = 0;
		//System.out.println(1);
		for (int i = 0; i < mapList.size(); i++) {
			int cnt = 0;
			String[] columns = DB2DB.column(tableName);
			StringBuffer buf = new StringBuffer();
			DB2DB.generateInsertSql(buf, tableName, columns);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = mapList.get(i);
			try {
				pstm = myConn.prepareStatement(buf.toString());
				for (int j = 0; j < columns.length; j++) {
					//컬럼에 값 넣어주기
					pstm.setString(j+1, map.get(columns[j]));
				}
				cnt = pstm.executeUpdate();
				totalCnt += cnt;
				if(seq!=null) {
					oracleTableUpdate(cnt,seq);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				oracleTableUpdate(cnt,seq);
				//e.printStackTrace();
			} 
		}
	
		return totalCnt;
	}
	
	/**
	 * DB2DB 쿼리전송
	 * */
	public static ResultSet sendQuery(Connection conn, String query, PreparedStatement pstm) throws SQLException {
		pstm = conn.prepareStatement(query);
		return pstm.executeQuery();
	}
	
	/**
	 * DB2DB 리턴된 데이터 맵 담기
	 * */
	private static HashMap<String, String> mapTask(String tableName, ResultSet rs) {
		HashMap<String,String> map = new HashMap<String, String>();
		String[] column = DB2DB.column(tableName);
		for (int i = 0; i < column.length; i++) {
			try {
				map.put(column[i], rs.getString(column[i]));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
