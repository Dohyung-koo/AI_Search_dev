package com.diquest.test;

import java.util.ArrayList;

public class TestGetChosung {

	private final char[] FIRST_CHAR = new char[]{
		'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
	};
	private final String TAB = "\t";
	private final String WHITESPACE = "\t";
	
	
	public TestGetChosung() {
	}
	private static class Singleton {
		private static final TestGetChosung instance = new TestGetChosung();
	}
	public static TestGetChosung getInstance(){
		return Singleton.instance;
	}
	
	public String getFKEYCho (String keyword) {
		StringBuilder data_fkey = new StringBuilder();
		StringBuilder F_KEY = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>();
		int intch, first;
		char ch;
		int keyword_len = keyword.length();
		String cur_fkey = "";
		for(int i = 0; i < keyword_len; i++) {
			ch = keyword.charAt(i);
			intch = (int) ch;
			cur_fkey = F_KEY.toString();
			if(Character.isWhitespace(ch)) {
				F_KEY = new StringBuilder();
			} else {
				if(intch < 44032 || intch > 55203) {
					list.add(cur_fkey + ch);
					F_KEY.append(ch);
				} else {
					intch = intch - 44032;
					first = intch / 588;
					list.add(cur_fkey + FIRST_CHAR[first]);
					F_KEY.append(FIRST_CHAR[first]);
				}
			}
		}
		
		if(list.size() > 0)
			data_fkey.append(list.get(0));
		for(int i = 1; i < list.size(); i++) {
			data_fkey.append(TAB);
			data_fkey.append(list.get(i));
		}
		return data_fkey.toString();
	}

	public String getFCho(String keyword) {
		StringBuilder data_fkey = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>();
		int intch, first;
		char ch;
		int keyword_len = keyword.length();
		String cur_fkey = "";
		for(int i = 0; i < keyword_len; i++) {
			ch = keyword.charAt(i);
			intch = (int) ch;
			if(Character.isWhitespace(ch)) {
				list.add(WHITESPACE);
				continue;
			}
			if(intch < 44032 || intch > 55203) {
				list.add(cur_fkey + ch);
			} else {
				intch = intch - 44032;
				first = intch / 588;
				list.add(cur_fkey + FIRST_CHAR[first]);
			}
		}
		for(int i = 0; i < list.size(); i++) {
			data_fkey.append(list.get(i));
		}
		return data_fkey.toString();
	}
	public String getFKEY(String keyword) {
		StringBuilder data_fkey = new StringBuilder();
		StringBuilder F_KEY = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>();
		int intch, first, second, third, temp, sum;
		char ch;
		int keyword_len = keyword.length();
		String cur_fkey = "";
		for(int i = 0; i < keyword_len; i++) {
			ch = keyword.charAt(i);
			intch = (int) ch;
			cur_fkey = F_KEY.toString();
			if(Character.isWhitespace(ch)) {
				F_KEY = new StringBuilder();
			} else {
				if(intch < 44032 || intch > 55203) {
					list.add(cur_fkey + ch);
				} else {
					intch = intch - 44032;
					first = intch / 588;
					list.add(cur_fkey + FIRST_CHAR[first]);
					temp = intch % 588;
					second = temp / 28;
					sum = 44032 + (first * 588) + (second * 28);
					list.add(cur_fkey + (char) sum);
					third = temp % 28;
					if(third != 0) {
						sum = 44032 + (first * 588) + (second * 28) + third;
						list.add(cur_fkey + (char) sum);
					}
				}
				F_KEY.append(ch);
			}
		}
		if(list.size() > 0)
			data_fkey.append(list.get(0));
		for(int i = 1; i < list.size(); i++) {
			data_fkey.append(TAB);
			data_fkey.append(list.get(i));
		}
		return data_fkey.toString();
	}

	// 키워드를 뒤에서부터 음절단위로 분리한 후 재조합한다.
	public String getBKEY(String keyword) {
		StringBuilder data_bkey = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>();
		char ch;
		String B_KEY = new String();
		int keyword_length = keyword.length();
		
		for(int i = (keyword_length - 1); i >= 0; i--) {
			ch = keyword.charAt(i);
			if(Character.isWhitespace(ch)) {
				B_KEY = "";
			} else {
				B_KEY = ch + B_KEY;
				list.add(B_KEY);
			}
		}
		int listSize = list.size();
		for(int i = 0; i < listSize; i++) {
			if(i == 0) {
				data_bkey.append(list.get(i));
			} else {
				data_bkey.append(TAB);
				data_bkey.append(list.get(i));
			}
		}
		return data_bkey.toString();
	}

	public String getBKEYCho(String keyword) {
		StringBuilder data_bkey = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>();
		char ch;
		int first;
		String B_KEY = new String();
		int keyword_length = keyword.length();
		
		for(int i = (keyword_length - 1); i >= 0; i--) {
			ch = keyword.charAt(i);
			if(Character.isWhitespace(ch)) {
				B_KEY = "";
			} else {
				int intch = (int) ch;
				if (intch > 44032 && intch < 55203) {
					intch = intch - 44032;
					first = intch / 588;
					ch = FIRST_CHAR[first];
				}
				B_KEY = ch + B_KEY;
				list.add(B_KEY);
			}
		}
		int listSize = list.size();
		for(int i = 0; i < listSize; i++) {
			if(i == 0) {
				data_bkey.append(list.get(i));
			} else {
				data_bkey.append(TAB);
				data_bkey.append(list.get(i));
			}
		}
		return data_bkey.toString();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "테이블 밑에 손을 숨기지 마라, 엄지를 주머니에 넣는";
		
		System.out.println(TestGetChosung.getInstance().getBKEY(s));
		System.out.println(TestGetChosung.getInstance().getBKEYCho(s));
		System.out.println(TestGetChosung.getInstance().getFCho(s));
		System.out.println(TestGetChosung.getInstance().getFKEY(s));
		System.out.println(TestGetChosung.getInstance().getFKEYCho(s));
	}

}
