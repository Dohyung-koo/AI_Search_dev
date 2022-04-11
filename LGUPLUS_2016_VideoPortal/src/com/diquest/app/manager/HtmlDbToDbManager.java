package com.diquest.app.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Method;
import com.diquest.app.common.Constant.DB2DB;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.db.connector.DBConnector;


public class HtmlDbToDbManager {
	private DBConnector postgreConnector;
	private Connection htmlConn ;

	private Connection mySqlConn;
	private DBConnector mySqlConnector;
	
	PreparedStatement pstm = null; 
	ResultSet rs = null;

	public void Start() {
		System.out.println("HTML데이터 수집시작");
		openDB();
		if(htmlConn!=null) {
			htmldb2DbStart();
			closeDB();
		}
		
		System.out.println("HTML데이터 수집종료");
	}
	/**
	 * DB2DB DB연결
	 */
	public void openDB() {
		  PreparedStatement readStatement = null;
		  ResultSet resultSet = null;
		  String results = "";

		
	      System.out.println("OPEN DB [" + DB_INFO.URL + "]");
	      try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      String dbName = DB_INFO.HTML_POSTGR_DB_NAME;
	      String userName = DB_INFO.HTML_POSTGRE_USER;
	      String htmlPassword = DB_INFO.HTML_POSTGRE_PASSWORD;
	      String hostname = DB_INFO.HTML_POSTGRE_HOST;
	      String port = DB_INFO.HTML_POSTGRE_PORT;
	      String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + htmlPassword;
		  
		  // Load the JDBC driver
		  try {
		    System.out.println("Loading driver...");
		    Class.forName("org.postgresql.Driver");
		    System.out.println("Driver loaded!");
		  } catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		  }

//		  try {
			  try {
				htmlConn = DriverManager.getConnection(jdbcUrl);
				  System.out.println("OPEN DB [" + DB_INFO.URL + "]");
					String url = DB_INFO.DB_URL;
					String user = DB_INFO.DB_USER;
					String password = DB_INFO.DB_PASSWORD;
					String driver = DB_INFO.DB_DRIVER;
					mySqlConnector = new DBConnector(url, user, password, driver);
					mySqlConn = mySqlConnector.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	/**
	 * DB2DB DB끊기
	 */
	public void closeDB() {
		/*mySqlConnector.closeConnection(mySqlConn);
		oracleConnector.closeConnection(oracleConn);
		*/
		
		System.out.println("CLOSED DB [" + DB_INFO.HTML_POSTGRE_URL + "]");
		System.out.println("CLOSED DB [" + DB_INFO.URL + "]");
	
		DBConnector.closeConnection(htmlConn);
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
	 * HTML DB2DB 작업시작, HTML DB POSTGRE -> MySQL 데이터 전송
	 */
	private void htmldb2DbStart() {
		//	int my_Count = tableDataCount(mySqlConn, pstm, rs);
		//	System.out.println("테이블 카운트 체크 my_Count : "+my_Count);
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("HH");
			String str = dayTime.format(new Date(time));
				System.out.println(str);
			int hour = Integer.parseInt(str);
				System.out.println("hour" + hour);
			
				if(hour==2 ||hour==5 || hour==8 ||hour==11||hour==14||hour==17||hour==20||hour==23) {
					getPostGreData(htmlConn, mySqlConn, pstm, rs);	
				}
			//getOracleData(oracleConn, mySqlConn, pstm, rs);
		//		if(hour == 16 ) {
		//				getPostGreData(postgreConn, mySqlConn, pstm, rs);
		//			}else {
		//				if (my_Count == 1) {
							//System.out.println("데이터 추출 작업");
		//					dataExtractTask(postgreConn, mySqlConn, pstm, rs);
		//				} else {
							//System.out.println("오라클 데이터 모두 가져오기");

	}
	
	
	
	/**
	 * HTML 테이블 조회 및 데이터 MYSQL 적재
	 */
	private void getPostGreData(Connection htmlConn, Connection myConn, PreparedStatement pstm, ResultSet rs) {
		ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> autoMapList = new ArrayList<HashMap<String, String>>();
//		System.out.println("start oracle -> mysql");
		for (int i = 0; i < DB2DB.HTML_POSTGRE_TABLE.length; i++) {
//			DBConnector.truncateTable(myConn, DB2DB.HTML_MYSQL_TABLE[i]);
			HashMap<String, String> tableDataMap = new HashMap<String, String>();
			HashMap<String, String> tableAutoDataMap = new HashMap<String, String>();

			String tableName = DB2DB.HTML_POSTGRE_TABLE[i];
			String query = DB2DB.all_Select(tableName);
			
			System.out.println("HTML_DATA : "+query);
			try {
				rs = sendQuery(htmlConn, query, pstm);
				if(rs.getRow() > 1) {
					DBConnector.truncateTable(myConn, DB2DB.HTML_MYSQL_TABLE[i]);
					while (rs.next()) {
						tableDataMap = mapTask(tableName, rs);;
						tableAutoDataMap = autoMapTask("AUTO", rs);;
						mapList.add(tableDataMap);
						autoMapList.add(tableAutoDataMap);
					}
					
					insertSQL(myConn, DB2DB.HTML_MYSQL_TABLE[i], mapList, rs, pstm,null);
					insertAutoSQL(myConn, "KIDSHTML_AUTO_COMPLETE", autoMapList, rs, pstm,null);
					mapList.clear();
					autoMapList.clear();
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// MySQl에 Data Insert
			
//			insertSQL(myConn, DB2DB.HTML_MYSQL_TABLE[i], mapList, rs, pstm,null);
//			insertAutoSQL(myConn, "KIDS_AUTO_COMPLETE", autoMapList, rs, pstm,null);
//			mapList.clear();
//			autoMapList.clear();
		}
		
	}
//	
//	/**
//	 * DB2DB 데이터 추출 작업 Oracle VW_CONTENTS_INFO 데이터로 Mysql Update
//	 */
//	private void dataExtractTask(Connection postgreConn, Connection myConn, PreparedStatement pstm, ResultSet rs) {
//		int insertCnt = 0;
//		int updateCnt = 0;
//		int deleteCnt = 0;
//		String q = DB2DB.all_Select(DB2DB.AR_LOOKUP_TABLE) + DB2DB.whereSet(DB2DB.AR_LOOKUP_TABLE, null, null);
//		//System.out.println("LOOKUP_Q : "+q);
//		//변경될 쿼리 가져오기
//		try {
//			rs = sendQuery(postgreConn, q, pstm);
//
//			ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
//			// PERSON_GROUP_LOOKUP 테이블 정보 가져오기
//			while (rs.next()) {
//				HashMap<String, String> map = mapTask(DB2DB.AR_LOOKUP_TABLE, rs);
//				mapList.add(map);
//			}
//
//			System.out.println("ORACLE TB_ARENT_SEARCH_LOOKUP DATA TOTAL CNT:" + mapList.size());
//
//			// PERSON_GROUP_LOOKUP 테이블 정보로 작업하기
//			for (int i = 0; i < mapList.size(); i++) {
//				//System.out.println("mapList.size() for()");
//				
//				HashMap<String, String> map = mapList.get(i);
//				String seq = map.get("LOOKUP_KEY");
//				//System.out.println("SEQ : "+seq);
//				String key = map.get("CONTENTS_KEY");
//				String linkJob = map.get("LINK_JOB");
//				String tableName = "ARENT."+map.get("VIEW_NAME");
//
//				// 오라클에서 정보 가져오기
//				String searchquery = DB2DB.all_Select(tableName) + DB2DB.whereSet(tableName, key, null);
//				//System.out.println("LOOKUP QUERY : "+searchquery);
//				rs = sendQuery(postgreConn, searchquery, pstm);
//				ArrayList<HashMap<String, String>> searchMapList = new ArrayList<HashMap<String, String>>();
//				HashMap<String, String> dataMap = new HashMap<String, String>();
//				while (rs.next()) {
//					dataMap = mapTask(tableName, rs);
//					searchMapList.add(dataMap);
//				}
//				tableName = Constant.DB2DB.AR_MYSQL_TABLE[0];
//				// System.out.println("linkJob : "+linkJob);
//				// 인서트
//				//System.out.println(linkJob+ " : "+seq+" searchMapList.size() : "+searchMapList.size());
//				if(("I".equals(linkJob) || "U".equals(linkJob)) && searchMapList.size() == 0){
//					oracleTableUpdate(0, seq);
//				} else if ("I".equals(linkJob)) {
//					insertCnt += insertSQL(myConn, tableName, searchMapList, rs, pstm, seq);
//				} else if ("U".equals(linkJob)) {
//					updateCnt += updateSQL(myConn, tableName, searchMapList, rs, pstm, seq);
//				} else if ("D".equals(linkJob)) {
//					deleteCnt += deleteSQL(myConn, tableName, key, rs, pstm, seq);
//				}
//
//			}
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("INSERT MYSQL TOTAL CNT:" + insertCnt);
//		System.out.println("UPDATE MYSQL TOTAL CNT:" + updateCnt);
//		System.out.println("DELETE MYSQL TOTAL CNT:" + deleteCnt);
//		
//	}
//	
	/**
	 * MySql 데이터 확인
	 */
	private int tableDataCount(Connection myConn, PreparedStatement pstm, ResultSet rs)	throws SQLException {
		int a = 0;
		for (int i = 0; i < DB2DB.HTML_MYSQL_TABLE.length; i++) {
			String query = DB2DB.all_Select(DB2DB.HTML_MYSQL_TABLE[i]);
			System.out.println("ALL_SELEST : "+query);
			rs = sendQueryMysql(myConn, query, pstm);
			while (rs.next()) {
				if(rs.getRow()==1) {
					a++;
				}
			}
		}
		return a;
	}
	
	/**
	 * DB2DB 데이터 추출 작업 Oracle AR 데이터로 Mysql Update 후 AR 테이블 데이터 변경
	 */
	private void oracleTableUpdate(int cnt, String seq) {
		StringBuffer buf = new StringBuffer();
		try {
			if (1 == cnt) {
				//성공시
				/*DB2DB.generateDeleteSql(buf, DB2DB.LOOKUP_TABLE, new String[] { "SEQ" });
				pstm = oracleConn.prepareStatement(buf.toString());
				pstm.setString(1, seq);
				pstm.executeUpdate();*/
				DB2DB.generateUpdateSql(buf, DB2DB.AR_LOOKUP_TABLE, new String[] { "SYNC_STATUS" });
				pstm = htmlConn.prepareStatement(buf.toString());
				pstm.setString(1, "1");
				pstm.setString(2, seq);
				pstm.executeUpdate();
			} else {
				DB2DB.generateUpdateSql(buf, DB2DB.AR_LOOKUP_TABLE, new String[] { "SYNC_STATUS" });
				pstm = htmlConn.prepareStatement(buf.toString());
				pstm.setString(1, "3");
				pstm.setString(2, seq);
				pstm.executeUpdate();
				System.out.println("\t FAIL SEQ : "+seq);
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
	private int deleteSQL(Connection myConn, String tableName, String key, ResultSet rs,
			PreparedStatement pstm, String seq) {
		int totalCnt = 0;

		int cnt = 0;

		String[] columns = DB2DB.column(tableName);
		// 삭제필드
		columns = new String[] { columns[0] };
		StringBuffer buf = new StringBuffer();
		DB2DB.generateDeleteSql(buf, tableName, columns);
		//System.out.println("DELETE : " + buf.toString());

		try {
			pstm = myConn.prepareStatement(buf.toString());
			pstm.setString(1, key);
			/*
			 * for (int j = 0; j < columns.length; j++) { // 컬럼에 값 넣어주기 pstm.setString(j +
			 * 1, map.get(columns[j]));
			 * 
			 * if(j ==columns.length-1) { pstm.setString(columns.length,
			 * Method.normalizeChosung(map.get("CONTENTS_NAME"))); }else { //컬럼에 값 넣어주기
			 * 
			 * } }
			 */
			cnt = pstm.executeUpdate();
			totalCnt += cnt;
			oracleTableUpdate(cnt, seq);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			oracleTableUpdate(cnt, seq);
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
			//System.out.println("UPDATE : "+buf.toString());
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
					if(j ==columns.length-1) {
						pstm.setString(columns.length, Method.normalizeChosung(map.get("CONTENTS_NAME")));	
					}else {
						//컬럼에 값 넣어주기
						pstm.setString(j+1, map.get(columns[j]));
					}
				}
				pstm.setString(columns.length+1, id);
				//pstm.setString(columns.length+2, code);
				
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
			if(i == 0 ) {
				System.out.println("INSERT MYSQL : "+buf.toString());
			}
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = mapList.get(i);
			try {
				pstm = myConn.prepareStatement(buf.toString());
				
				for (int j = 0; j < columns.length; j++) {
					//System.out.print(columns[j]+",");
					if(j ==columns.length-1) {
						//System.out.print("마지막");
						pstm.setString(j+1, Method.normalizeChosung(map.get("CONTENTS_NAME")));	
					}else {
						//System.out.print("map.get(columns[j]) : "+map.get(columns[j]));
						//컬럼에 값 넣어주기
						pstm.setString(j+1, map.get(columns[j]));
					}
					/*if( i == 0) {
						
						if(j ==columns.length-1) {
							System.out.print(", "+Method.normalizeChosung(map.get("CONTENTS_NAME")));
							System.out.println("");
						}else {
							System.out.print(", "+map.get(columns[j]));
						}
					}*/
					
				}
				
				cnt = pstm.executeUpdate();
				totalCnt += cnt;
				if(seq!=null) {
					oracleTableUpdate(cnt,seq);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				if(seq!=null) {
					oracleTableUpdate(cnt,seq);
				}
				//e.printStackTrace();
			} 
		}
	
		return totalCnt;
	}
	
	
	/**
	 * DB2DB 데이터 INSERT 작업
	 * */
	public int insertAutoSQL(Connection myConn, String tableName, ArrayList<HashMap<String, String>> mapList, ResultSet rs, PreparedStatement pstm, String seq) {
		int totalCnt = 0;
		DBConnector.truncateTable(myConn, tableName);
		//System.out.println(1);
		for (int i = 0; i < mapList.size(); i++) {
			int cnt = 0;
			String[] columns = DB2DB.column("AUTO");
			String[] columnsAuto = DB2DB.column("KIDS_AUTO_COMPLETE");
			StringBuffer buf = new StringBuffer();
			DB2DB.generateInsertSql(buf, tableName, columnsAuto);
			if(i == 0 ) {
				System.out.println("INSERT KIDSHTML_AUTO MYSQL : "+buf.toString());
			}
			
			HashMap<String, String> map = new HashMap<String, String>();
			map = mapList.get(i);
			System.out.println("map size " + map.size());
			try {
				pstm = myConn.prepareStatement(buf.toString());
				
				for (int j = 0; j < columnsAuto.length; j++) {
					//System.out.print(columns[j]+",");
					if(j < map.size()-1) {
						pstm.setString(j+1, map.get(columns[j]));
						System.out.println("map.get(columns[j])" + map.get(columns[j]));
							
					}else if (j==map.size()-1) {
						pstm.setString(j+1, map.get("CONTENTS_CHOSUNG"));
						System.out.println("map.get(columns[j])" + map.get("CONTENTS_CHOSUNG"));
					}
					else {
						System.out.println("몇번 들어오나?");
						pstm.setString(j+1, "");
					}
					
				}
				
				cnt = pstm.executeUpdate();
				totalCnt += cnt;
				if(seq!=null) {
					oracleTableUpdate(cnt,seq);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				if(seq!=null) {
					oracleTableUpdate(cnt,seq);
				}
				//e.printStackTrace();
			} 
		}
	
		return totalCnt;
	}
	
	
	/**
	 * DB2DB 쿼리전송
	 * */
	public static ResultSet sendQuery(Connection conn, String query, PreparedStatement pstm) throws SQLException {
//		 readStatement = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		pstm = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return pstm.executeQuery();
	}
	
	public static ResultSet sendQueryMysql(Connection conn, String query, PreparedStatement pstm) throws SQLException {
		pstm = conn.prepareStatement(query);
		return pstm.executeQuery();
	}
	
	/**
	 * DB2DB 리턴된 데이터 맵 담기
	 * */
	private static HashMap<String, String> mapTask(String tableName, ResultSet rs) {
		HashMap<String,String> map = new HashMap<String, String>();
		String[] column = DB2DB.column(tableName);
		/*String contentsName = "";*/
		//System.out.println("mapTask");
		for (int i = 0; i < column.length; i++) {
			try {
				//System.out.println(column[i]);
				map.put(column[i], rs.getString(column[i]));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return map;
	}
	
	/**
	 * DB2DB 리턴된 데이터 맵 담기
	 * */
	private static HashMap<String, String> autoMapTask(String tableName, ResultSet rs) {
		HashMap<String,String> map = new HashMap<String, String>();
		String[] column = DB2DB.column(tableName);
		/*String contentsName = "";*/
		//System.out.println("mapTask");
		// CONTENTS_ID / CONTENTS_NAME 
		for (int i = 0; i < column.length+1; i++) {
			try {
				//System.out.println(column[i]);
				if(i == column.length) {
					System.out.println("column[1]" + column[1]);
					System.out.println("Method.normalizeChosung(rs.getString(column[1]))" + Method.normalizeChosung(rs.getString(column[1])));
					map.put("CONTENTS_CHOSUNG", Method.normalizeChosung(rs.getString(column[1])));
				}else {
					System.out.println("column[]" + column[i]);
					System.out.println("rs.getString(column[i])" + rs.getString(column[i]));
					map.put(column[i], rs.getString(column[i]));
					
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return map;
	}

}
