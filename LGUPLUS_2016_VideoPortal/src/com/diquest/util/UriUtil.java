package com.diquest.util;

/**
 * Uri 생성에 필요한 Util 
 * 20150923
 * @author 이호준
 *
 */
public class UriUtil {

	/**
	 * 	<p>
	 * ttl 에 맞는 uri 를 생성 한다.
	 * </P>
	 * 
	 *<pre>
	 * UriUtil.makeUri("Human", "_", "홍길동")    = Human_홍길동
	 * UriUtil.makeUri("Keyword", "-", "한국")      = Keyword-한국
	 * UriUtil.makeUri("Keyword", "_", "한@국@K-O-R-E-A")    = Keyword_한국KOREA
	 *</pre>
	 * @param type
	 * @param delimiter
	 * @param s
	 * @return type + delimiter + s 
	 */
	
	public static String makeUri(String type, String delimiter, String s){
		s = removeInvalidLetter(s);
		if("".equals(s)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append(delimiter);
		sb.append(s);
		return sb.toString();
	}
	
	/**
	 * 
	 * 	<p>
	 *  2단어에 대한 정렬된 uri 를 생성 한다.
	 * </P>
	 *<pre>
	 * UriUtil.makeCoUri("Human", "_", "홍길동", "가길동")    = Human_가길동_홍길동
	 * UriUtil.makeCoUri("Human", "_", "홍길동", "가길동")    = Human_가길동_홍길동
	 * UriUtil.makeCoUri("Human", "_", "가길동", "Tom")    = Human_Tom_가길동
	 * UriUtil.makeCoUri("Human", "_", "가)_길*동", "T*o+m")    = Human_Tom_가길동
	 *</pre>
	 * @param type
	 * @param delimiter
	 * @param s
	 * @param s2
	 * @return
	 */
	public static String makeCoUri(String type, String delimiter, String s, String s2) {
		return makeCoUri(type, delimiter, s, s2, false);
	}
	public static String makeCoUri(String type, String delimiter, String s, String s2, boolean sort) {
		s = removeInvalidLetter(s);
		if("".equals(s)){
			return null;
		}
		s2 = removeInvalidLetter(s2);
		if("".equals(s2)){
			return null;
		}
		
		if(sort){
			if(s.compareToIgnoreCase(s2) < 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(type);
				sb.append(delimiter);
				sb.append(s);
				sb.append(delimiter);
				sb.append(s2);
				return sb.toString();
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(type);
				sb.append(delimiter);
				sb.append(s2);
				sb.append(delimiter);
				sb.append(s);
				return sb.toString();
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(type);
			sb.append(delimiter);
			sb.append(s);
			sb.append(delimiter);
			sb.append(s2);
			return sb.toString();
		}
	}
	
	/**
	 * Salinas Jiménez MªdelMar -->> SalinasJimnezMdelMar
	 * 영어, 한글, 숫자가 아니면 모두삭제
	 */
	public static String removeInvalidLetter(String s){
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = s.length(); i < j; i++) {
			char c = s.charAt(i);
			if (StringUtil.isNumber(c) || StringUtil.isKorean(c) || StringUtil.isEnglish(c) || StringUtil.isHanja(c)){
				sb.append(c);
			} 
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		System.out.println(makeUri("HUMAN", "_", "ªªª"));
//		System.out.println(isValidURI("dfdsdf"));
//		String s = "xxÌºíõäîëãîðª";
//		String x = removeInvalidLetter(s);
//		if("".equals(x)){
//			System.out.println("null");
//		} else {
//			System.out.println("s=" + s + ", x=" + x);
//		}
	}
	
}
