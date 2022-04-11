package com.diquest.app.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NormalizeManager {
	INSTANCE;
	
	private static List<Pattern> REMAIN_PATTERN_LIST = new ArrayList<Pattern>() {{
		this.add(Pattern.compile("[A-Za-zㄱ-ㅎ가-힣]+\\s*(vs|VS)\\s*[A-Za-zㄱ-ㅎ가-힣]+"));
	}};
	
	private static List<String> NOMALIZED_PATTERN_LIST = new ArrayList<String>() {{
		this.add("(제|Day|day|DAY|Day)?\\s*[0-9]+?\\s*(화|장|부|부분|절|주의|편|회|년대)");
		this.add("([0-9]+강)|([0-9]+주차)");
		this.add("외$");
	}};
	
	private static Pattern numCheck = Pattern.compile("^[0-9]+$");
	private static Pattern numBetweenSpaceCheck = Pattern.compile("[0-9]+(\\s*)[0-9]+");
	
	private static Pattern lastNumCheck = Pattern.compile("[^시즌].\\s*[0-9]+$");
	private static String lastNumRemovePattern = "[0-9]+$";
	
	private static Pattern muchSpaceCheck = Pattern.compile("[ ][ ]+");
	
	private NormalizeManager() {
		
	}
	
	public static String regexNomalized_Contents(String input) {
		if(input == null) {
			return null;
		}
				
		String result = input.trim();
		
		for(Pattern remainPattern : REMAIN_PATTERN_LIST) {
			Matcher matcher = remainPattern.matcher(result);
			
			if(matcher.find()) {
				result = matcher.group();
			}
			
			result = result.trim();			
			Matcher numCheckMatcher = numCheck.matcher(result);			
			if(numCheckMatcher.find() == true) {
				return null;
			}
		}
		
		for(String normalizedPattern : NOMALIZED_PATTERN_LIST) {
			result = result.replaceAll(normalizedPattern, "");			
			result = result.trim();
			Matcher numCheckMatcher = numCheck.matcher(result);
			if(numCheckMatcher.find() == true) {
				return null;
			}
		}
		
		Matcher lastNumMatcher = lastNumCheck.matcher(result);
		if(lastNumMatcher.find() == true) {
			result = result.replaceAll(lastNumRemovePattern, "");
		}
		
		Matcher numBetweenSpaceMatcher = numBetweenSpaceCheck.matcher(result);
		if(numBetweenSpaceMatcher.find() == true) {
			String prefix = result.substring(0, numBetweenSpaceMatcher.start());
			String postfix = result.substring(numBetweenSpaceMatcher.end(), result.length());
			String group = numBetweenSpaceMatcher.group().replaceAll(" ", "");
			result = prefix + group + postfix;
			result = result.trim();
		}
		
		Matcher muchSpaceMatcher = muchSpaceCheck.matcher(result);
		if(muchSpaceMatcher.find() == true) {
			String prefix = result.substring(0, muchSpaceMatcher.start());
			String postfix = result.substring(muchSpaceMatcher.end(), result.length());
			String group = muchSpaceMatcher.group().replaceAll("[ ][ ]+", " ");
			result = prefix + group + postfix;
			result = result.trim();
		}
		
		return result;
	}
}
