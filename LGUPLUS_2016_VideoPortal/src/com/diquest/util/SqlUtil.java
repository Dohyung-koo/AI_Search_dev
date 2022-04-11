package com.diquest.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.diquest.app.common.Constant;



// TODO preparedstatement util?
// TODO constant to top level?
/**
 * <ul>
 * <li>lower case
 * </ul>
 *
 * @author shlee
 * @since 1.0.0.0 (2008. 5. 22.)
 */
public class SqlUtil {
	/**
	 * @author shlee
	 * @since 1.0.0.0 (2008. 5. 28.)
	 */
	public static class Connectives {
		private static final String EXP_OR = "or";
		private static final String EXP_AND = "and";
		public static final boolean AND = true;
		public static final boolean OR = false;

		public static String toString(boolean connectives) {
			if (AND == connectives) {
				return EXP_AND;
			}
			else if (OR == connectives) {
				return EXP_OR;
			}
			else {
				throw new IllegalArgumentException("Unknown value: " + connectives);
			}
		}

		public static boolean parse(String connectives) throws ParseException {
			if (EXP_OR.equalsIgnoreCase(connectives)) {
				return OR;
			}
			else if (EXP_AND.equalsIgnoreCase(connectives)) {
				return AND;
			}
			else {
				throw new ParseException(connectives, 0);
			}
		}
	}

	/**
	 * @author shlee
	 * @since 1.0.0.0 (2008. 5. 28.)
	 */
	public static class Order {
		private static final String EXP_ASC = "asc";
		private static final String EXP_DESC = "desc";
		public static final boolean ASC = true;
		public static final boolean DESC = false;

		public static String toString(boolean order) {
			if (ASC == order) {
				return EXP_ASC;
			}
			else if (DESC == order) {
				return EXP_DESC;
			}
			else {
				throw new IllegalArgumentException("Unknown value: " + order);
			}
		}

		public static boolean parse(String order) throws ParseException {
			if (EXP_ASC.equalsIgnoreCase(order)) {
				return ASC;
			}
			else if (EXP_DESC.equalsIgnoreCase(order)) {
				return DESC;
			}
			else {
				throw new ParseException(order, 0);
			}
		}
	}

	/**
	 * <code>column</code><code>condition</code>?
	 *
	 * @param buf
	 * @param column
	 * @param condition
	 */
	public static void appendOperation(StringBuffer buf, String column, String condition) {
		ensureSpace(buf);
		buf.append(column).append(condition).append("? ");
	}

	/**
	 * @param buf
	 * @param from
	 * @param to
	 */
	public static void appendBetween(StringBuffer buf, String column) {
		ensureSpace(buf);
		buf.append(column).append(" between ? and ? ");
	}

	/**
	 * @param buf
	 * @param from
	 * @param to
	 */
	public static void appendInclusiveExclusiveBetween(StringBuffer buf, String column) {
		ensureSpace(buf);
		buf.append(" ( ");
		buf.append(column).append(" >= ? and ").append(column).append(" < ? ");
		buf.append(" ) ");
	}

	/**
	 * @param buf
	 * @param column
	 */
	public static void appendStringEquals(StringBuffer buf, String column) {
		ensureSpace(buf);
		buf.append(column).append("=? ");
	}

	/**
	 * @param buf
	 * @param column
	 */
	public static void appendStringLike(StringBuffer buf, String column) {
		ensureSpace(buf);
		buf.append(column).append(" like ? ");
	}

	/**
	 * @param buf
	 * @param columns
	 * @param and
	 */
	public static void appendStringEquals(StringBuffer buf, String[] columns, boolean conn) {
		ensureSpace(buf);
		if (columns.length > 0) {
			buf.append('(');
			buf.append(' ').append(columns[0]).append("=? ");
		}
		for (int i = 1; i < columns.length; i++) {
			buf.append(Connectives.toString(conn)).append(' ').append(columns[i]).append("=? ");
		}
		if (columns.length > 0) {
			buf.append(')');
		}
	}

	/**
	 * @param buf
	 * @param column
	 * @param count
	 * @param conn
	 */
	public static void appendStringEquals(StringBuffer buf, String column, int count, boolean conn) {
		ensureSpace(buf);
		if (count > 0) {
			buf.append('(');
			buf.append(' ').append(column).append("=? ");
		}
		for (int i = 1; i < count; i++) {
			buf.append(Connectives.toString(conn)).append(' ').append(column).append("=? ");
		}
		if (count > 0) {
			buf.append(')');
		}
	}

	/**
	 * @param buf
	 */
	public static void parenthesize(StringBuffer buf) {
		buf.insert(0, '(');
		buf.append(')');
	}

	/**
	 * @param buf
	 * @param columns
	 * @param orders
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static void appendOrderBy(StringBuffer buf, String[] columns, boolean[] orders) {
		if (columns.length != orders.length) {
			throw new IllegalArgumentException(/* TODO proper message */);
		}
		if (columns.length == 0) {
			return;
		}

		ensureSpace(buf);
		buf.append("order by ");
		Set used = new HashSet(columns.length);
		final int size = columns.length;

		buf.append(columns[0]);
		buf.append(' ');
		buf.append(Order.toString(orders[0]));
		used.add(columns[0]);

		for (int i = 0; i < size; i++) {
			if (used.add(columns[i])) {
				buf.append(", ").append(columns[i]).append(' ').append(Order.toString(orders[i]));
			}
		}
		buf.append(' ');
	}
	/**
	 *  WHERE (colum LIKE ?)<br>
	 *  WHERE (colum = ?)<br>
	 *  WHERE (colum LIKE ?  AND colum LIKE ?  AND ... colum LIKE ? )<br>	
	 *  WHERE (colum LIKE ?  OR colum LIKE ?  OR ... colum LIKE ? )<br>
	 *  WHERE (colum = ?  AND colum = ?  AND ... colum = ? )<br>
	 *  WHERE (colum = ?  OR colum = ?  OR ... colum = ? )<br>
	 *  <br>
	 * @param buf
	 * @param columns
	 * @param isAndOperator
	 * @param isLikeOperator
	 */
	public static void appendWhere(StringBuffer buf, String[] columns, boolean isAndOperator, boolean isLikeOperator){
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		buf.append("\n WHERE (");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
					buf.append(isAndOperator ? " AND " : " OR ");
			}
			buf.append(columns[i]).append(isLikeOperator ? " LIKE ? " : " = ? ");
		}
		buf.append(")");
	}

	/**
	 *  AND (colum LIKE ?)<br>
	 *  AND (colum = ?)<br>
	 *  AND (colum LIKE ?  AND colum LIKE ?  AND ... colum LIKE ? )<br>	
	 *  AND (colum LIKE ?  OR colum LIKE ?  OR ... colum LIKE ? )<br>
	 *  AND (colum = ?  AND colum = ?  AND ... colum = ? )<br>
	 *  AND (colum = ?  OR colum = ?  OR ... colum = ? )<br>
	 *  <br>
	 *  OR (colum LIKE ?)<br>	
	 *  OR (colum = ?)<br>
	 *  OR (colum LIKE ?  AND colum LIKE ?  AND ... colum LIKE ? )<br>	
	 *  OR (colum LIKE ?  OR colum LIKE ?  OR ... colum LIKE ? )	<br>
	 *  OR (colum = ?  AND colum = ?  AND ... colum = ? )<br>
	 *  OR (colum = ?  OR colum = ?  OR ... colum = ? )<br>
	 *  <br>
	 * @param buf
	 * @param columns
	 * @param isAndOperator
	 * @param isLikeOperator
	 * @param isAndOperatorJoin
	 */
	public static void appendWhereSub(StringBuffer buf, String[] columns, boolean isAndOperator, boolean isLikeOperator, boolean isAndOperatorJoin){
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		buf.append(isAndOperatorJoin ? "	\n AND (" : "	\n OR (");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				buf.append(isAndOperator ? " AND " : " OR ");
    		}
    		buf.append(columns[i]).append(isLikeOperator ? " LIKE ? " : " = ? ");
		}
		buf.append(")");
	}
	
	/**
	 * 
	 *  AND (colum != ?)<br>
	 *  AND (colum != ?  AND colum != ?  AND ... colum != ? )<br>
	 *  AND (colum != ?  OR colum != ?  OR ... colum != ? )<br>
	 *  <br>
	 *  OR (colum != ?)<br>
	 *  OR (colum != ?  AND colum != ?  AND ... colum != ? )<br>
	 *  OR (colum != ?  OR colum != ?  OR ... colum != ? )<br>
	 *  <br>
	 * @param buf
	 * @param columns
	 * @param isAndOperator
	 * @param isAndOperatorJoin
	 */
	public static void appendWhereSubNot(StringBuffer buf, String[] columns, boolean isAndOperator, boolean isAndOperatorJoin){
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		buf.append(isAndOperatorJoin ? "	\n AND (" : "	\n OR (");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				buf.append(isAndOperator ? " AND " : " OR ");
    		}
    		buf.append(columns[i]).append(" != ? ");
		}
		buf.append(")");
	}
	
	/**
	 * %value%<br>
	 * 
	 * @param value
	 * @param isLikeOperator
	 * @return
	 */
	public static String getValueChekedLike(String value, boolean isLikeOperator) {
		StringBuffer b = new StringBuffer();
		if (value == null)
			value = "";
		if (isLikeOperator) {
			b.append("%").append(value).append("%");
			return b.toString();
		} else {
			return value;
		}
	}
	
	private static void ensureSpace(StringBuffer buf) {
		if (buf.length() > 0) {
			if (Character.isWhitespace((buf.charAt(buf.length() - 1))) == false) {
				buf.append(' ');
			}
		}
		return;
	}

	/**
	 * insert into tableName (column, column, ..., column) values (?,?, ..., ?) 
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 */
	public static void generateInsertSql(StringBuffer buf, String tableName, String[] columns) {
		if (columns.length == 0) {
			buf.append("insert into ").append(tableName);
			return;
		}

		buf.append("insert into ");
		buf.append(tableName);
		buf.append(" ");
		buf.append(StringUtil.format(columns, "(", ", ", ")"));
		buf.append(" values (");
		if (columns.length > 0) {
			buf.append("?");
			for (int i = 1; i < columns.length; i++) {
				buf.append(",?");
			}
		}
		buf.append(")");
	}

	public static String generateSelectSql(String tableName, String[] columns) {
		StringBuffer b = new StringBuffer();
		generateSelectSql(b, tableName, columns);
		return b.toString();
	}

	/**
	 * select column, column, ... , column from <code>tableName</code>
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 */
	public static void generateSelectSql(StringBuffer buf, String tableName, String[] columns) {
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		buf.append("select ");
		buf.append(StringUtil.format(columns, ", "));
		buf.append(" from ");
		buf.append(tableName);
		
	}

	public static String generateSelectSq_watch(String tableName, String[] columns) {
		StringBuffer b = new StringBuffer();
		generateSelectSql_watch(b, tableName, columns);
		return b.toString();
	}

	/**
	 * select column, column, ... , column from <code>tableName</code>
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 */
	public static void generateSelectSql_watch(StringBuffer buf, String tableName, String[] columns) {
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		buf.append("select ");
		buf.append(StringUtil.format(columns, ", "));
		buf.append(" from ");
		buf.append(tableName);
		buf.append(" WHERE DATE >=(CURDATE() - INTERVAL ");
		buf.append(Constant.OPTION.SELECTOPTION);
		buf.append(" DAY)");
	}
	
	/**
	 * Generate prepared statement update sql with given columns and AND-connected conditions. update <code>tableName</code>
	 * set column=?, column=? ... , column=? where condition=? and condition=? and condition=?
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 * @param conditions
	 */
	public static void generateUpdateSql(StringBuffer buf, String tableName, String[] columns, String[] conditions) {
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}

		buf.append("update ");
		buf.append(tableName);
		buf.append(" set ");
		StringUtil.format(buf, columns, "", " = ?", "", ", ", "");
		if (conditions.length > 0) {
			buf.append(" where ");
			StringUtil.format(buf, conditions, "", " = ?", "", " and ", "");
		}
	}

	/**
	 * DELETE FROM tableName WHERE column = ? AND column = ? AND ... column = ?
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 */
	public static void generateDeleteSql(StringBuffer buf, String tableName, String[] columns) {
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		
		buf.append("DELETE FROM ");
		buf.append(tableName);
		buf.append(" WHERE ");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0)
				buf.append(" AND ");
			buf.append(columns[i]);
			buf.append(" =");
			buf.append(" ?");
		}
	}
		
	/**
	 * DELETE FROM tableName WHERE column = ? AND column = ? AND ... column = ?
	 *
	 * @param buf
	 * @param tableName
	 * @param columns
	 */
	public static void generateDeleteSql_watch(StringBuffer buf, String tableName, String[] columns) {
		if (columns.length == 0) {
			throw new IllegalArgumentException();
		}
		Calendar  cal  =  Calendar.getInstance();
		SimpleDateFormat  sdf  =  new SimpleDateFormat("yyyyMMdd");      // 데이터 출력 형식 지정
		String  toDate  =  sdf.format (cal.getTime());      // 오늘 날짜 변수에 저장
		
		cal.set (cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)- 30);     // 30일전 날짜 Set
		String  lastDate  =  sdf.format (cal.getTime());    // 7일전 날짜 변수에 저장
		buf.append("ALTER TABLE ");
		buf.append(tableName);
		buf.append(" DROP PARTITION");
		buf.append(" P"+lastDate);
		
		
//		buf.append("DELETE FROM ");
//		buf.append(tableName);
//		buf.append(" WHERE DATE <=(CURDATE()-INTERVAL ");
//		buf.append(Constant.OPTION.DELETEOPTION);
//		buf.append(" DAY)");
		
		
//		for (int i = 0; i < columns.length; i++) {
//			if (i > 0)
//				buf.append(" AND ");
//			buf.append(columns[i]);
//			buf.append(" =");
//			buf.append(" ?");
//		}
	}
	
	/**
	 * Generate prepared statement insert sql with given column names.
	 *
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public static String generateInsertSql(String tableName, String[] columns) {
		StringBuffer b = new StringBuffer();
		generateInsertSql(b, tableName, columns);
		return b.toString();
	}
	
	/**
	 * Generate delete sql with given column names.
	 *
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public static String generateDeleteSql(String tableName, String[] columns) {
		StringBuffer b = new StringBuffer();
		generateDeleteSql(b, tableName, columns);
		return b.toString();
	}

	/**
	 * Generate prepared statement update sql with given columns and AND-connected conditions. update <code>tableName</code>
	 * set column=?, column=? ... , column=? where condition=? and condition=? and condition=?
	 *
	 * @param tableName
	 * @param columns
	 * @param conditions
	 * @return
	 */
	public static String generateUpdateSql(String tableName, String[] columns, String[] conditions) {
		StringBuffer b = new StringBuffer();
		generateUpdateSql(b, tableName, columns, conditions);
		return b.toString();
	}
	
	/**
	 *	Generate PreparedStatement
	 * @param connection
	 * @param sql
	 * @return
	 */
	public static PreparedStatement generatePstmt(Connection connection, String sql){
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement)connection.prepareStatement(sql);
			return pstmt;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
