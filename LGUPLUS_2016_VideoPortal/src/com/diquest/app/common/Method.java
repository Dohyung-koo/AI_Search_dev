package com.diquest.app.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.diquest.util.KoreanChoSungUtil;
import com.diquest.util.StringUtil;

public class Method {

	public static void deleteData(PreparedStatement Pstmt, String val) {
		try {
			Pstmt.setString(1, val);
			Pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 감독명을 비교하기위한 정제작업
	 * @param s
	 * @return
	 */
	public static String normalizeDirector(String s){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.removeBrace(s);
		s = StringUtil.removeWhitepace(s);
		s = StringUtil.removeSpChar(s);
		s = StringUtil.trim(s);
		return s;
	}
	
	/**
	 * 영화제목을 감독명을 비교하기위한 정제작업
	 * @param s
	 * @return
	 */
	public static String normalizeTitle(String s){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.removeBigBrace(s);
		s = StringUtil.removeBrace(s);
		s = StringUtil.removeWhitepace(s);
		s = StringUtil.removeSpChar(s);
		s = s.replaceAll("(무한시청|극장판|감독판|평생소장|무삭제판|무삭제)", "");
		s = StringUtil.trim(s);
		return s;
	}
	
	/**
	 * 영화진흥원 숫자데이터 정제작업
	 * @param s
	 * @return
	 */
	public static String normalizeMovieNumberData(String s){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.remainDigit(s);
		return s;
	}
	
	/**
	 * 영화진흥원 년도데이터 정제작업
	 * @param s
	 * @return
	 */
	public static String normalizeYear(String s){
		if("".equals(s) || null == s)
			return s;
		if("해당정보 없음".equals(s))
			return "";
		s = s.replaceAll("년", "");
		return s;
	}
	
	/**
	 * 제목을 유니크 하게 정제작업
	 * @param s
	 * @return
	 */
	public static String normalizeAutoCompleteTitle(String s){
		if("".equals(s) || null == s)
			return s;
		
		s = s.toUpperCase();
		
		s = NormalizeManager.regexNomalized_Contents(s);
		if(s == null) {
			s = "";
		}
		s = StringUtil.removeBigBrace(s);
		s = StringUtil.removeBrace(s);
		s = StringUtil.OnlyOneSpace(s);
		s = s.replaceAll("(무한시청|극장판|감독판|평생소장|무삭제판|무삭제)", "");
		s = s.replace("  ",  " ").trim();
		return s;
	}
	
	
	/**
	 * 사전만들기 전에 전처리
	 * @param s
	 * @return
	 */
	public static String normalizeKeywordDic(String s ){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.removeBigBrace(s);
		s = StringUtil.removeBrace(s);
		s = StringUtil.OnlyOneSpace(s);
		s = StringUtil.removeTag(s);
		s = s.replaceAll("(무한시청|극장판|감독판|평생소장|무삭제판|무삭제)", "");
		s = s.replace("  ",  " ").trim();
		return s;
	}
	
	public static String normalizeDramaData(String s){
		if("".equals(s) || null == s)
			return s;
		StringUtil.removeBrace(s);
		StringUtil.removeBigBrace(s);
		s = s.trim();
		return s;
	}
	
	/**
	 * 해당 문자열을 특수문자 ,공백 제거후 초성만 가져온다
	 * @param s
	 * @return
	 */
	public static String normalizeChosung(String s){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.removeSpChar(s);
		s = StringUtil.removeWhitepace(s);
		s = KoreanChoSungUtil.getInstance().getFCho(s);
		return s;
	}
	
	/**
	 * 해당 문자열을 특수문자 ,공백 제거후 초성만 가져온다
	 * @param s
	 * @return
	 */
	public static String normalizeHumanChosung(String s){
		if("".equals(s) || null == s)
			return s;
		s = StringUtil.removeWhitepace(s);
		s = s.replaceAll(",", " ");
		s = KoreanChoSungUtil.getInstance().getFCho(s);
		return s;
	}
	
	public static void main(String[] args) {
		
//		String s = "[평생소장]해리포터와 죽음의 성물2(Harry potter)[우리말제작](우리말)";
//		String s = "2016년";
//		System.out.println(normalizeYear(s));
		String s = "원피스 극장판 6기: 오마츠리 남작과 비밀의 섬(One Piece)";
		System.out.println(normalizeKeywordDic(s));
		System.out.println(normalizeAutoCompleteTitle(s));
		
	}
}
