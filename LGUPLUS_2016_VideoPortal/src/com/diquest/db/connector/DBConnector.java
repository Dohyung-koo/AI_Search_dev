package com.diquest.db.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.diquest.util.SqlUtil;

public class DBConnector {

	private String url;
	private String user;
	private String password;
	private String driver;
	private Connection conn = null;

	public DBConnector(String url, String user, String password, String driver) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.driver = driver;
	}

	public DBConnector() {
		this.url = "jdbc:mysql://58.72.188.195:3306/video?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
		this.user = "diquest";
		this.password = "ek2znptm2";
		this.driver = "com.mysql.jdbc.Driver";
	}

	public Connection getConnection() {
		if (conn == null)
			conn = createConnection();
		return conn;
	}

	private Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static PreparedStatement createSelectPstmt(Connection conn, String table, String[] col) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql(sb, table, col);
		String sql = sb.toString();
		
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static PreparedStatement createSelectPstmt_watch(Connection conn, String table, String[] col) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateSelectSql_watch(sb, table, col);
		String sql = sb.toString();
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static PreparedStatement createInsertPstmt(Connection conn, String table, String[] col) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateInsertSql(sb, table, col);
		String sql = sb.toString();
		
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static PreparedStatement createUpdatePstmt(Connection conn, String table, String[] col, String[] where) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateUpdateSql(sb, table, col, where);
		String sql = sb.toString();
	
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static PreparedStatement createDeletePstmt(Connection conn, String table, String[] where) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateDeleteSql(sb, table, where);
		String sql = sb.toString();
		
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static PreparedStatement createDeletePstmt_watch(Connection conn, String table, String[] where) {
		PreparedStatement pstmt;
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateDeleteSql_watch(sb, table, where);
		String sql = sb.toString();
		pstmt = SqlUtil.generatePstmt(conn, sql);
		return pstmt;
	}

	public static void printInsertSql(String table, String[] col) {
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateInsertSql(sb, table, col);
		System.out.println(sb.toString());
	}

	public static void printUpdateSql(String table, String[] col, String[] where) {
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateUpdateSql(sb, table, col, where);
		System.out.println(sb.toString());
	}

	public static void printDeleteSql(String table, String[] col, String[] where) {
		StringBuffer sb = new StringBuffer();
		SqlUtil.generateDeleteSql(sb, table, where);
		System.out.println(sb.toString());
	}

	public static void truncateTable(Connection conn, String table) {
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		sb.append("TRUNCATE TABLE ");
		sb.append(table);
		String sql = sb.toString();
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void alterTable(Connection conn, String table, String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 데이터 출력 형식 지정
		String pdate = date;
		Date today = null;
		try {
			today = sdf.parse(pdate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		for (int i = 0; i < 30; i++) { // 10일치 미리 생성
			cal.add(Calendar.DATE, 1);
			String oneplusday = sdf.format(cal.getTime()); // 7일전 날짜 변수에 저장
			String watchdate = "";
			try {
				watchdate = exformat(oneplusday);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Statement stmt = null;
			StringBuffer sb = new StringBuffer();
			sb.append("ALTER TABLE ");
			sb.append(table);
			sb.append(" ADD PARTITION (");
			sb.append("PARTITION");
			sb.append(" P" + pdate);
			sb.append(" VALUES LESS THAN");
			sb.append(" (" + "'" + watchdate + "'" + "));");
			String sql = sb.toString();
			try {
				stmt = conn.createStatement();
				stmt.execute(sql);
				System.out.println("WATHCH_RECORD ADD PARTITION 완료 : P" + pdate);
				pdate = oneplusday;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("WATHCH_RECORD ADD PARTITION 이전 생성 완료 : P" + pdate);
				pdate = oneplusday;
			}
		}
	}

	public static void closePstmt(PreparedStatement pstmt) {
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ConnectionFactory.release(conn);
	}

	public static void excuteBatch(PreparedStatement pstmt) {
		try {
			pstmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String exformat(String date) throws ParseException {
		SimpleDateFormat origin_format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_data = origin_format.parse(date);
		String new_data = new_format.format(origin_data);

		return new_data;

	}
}
