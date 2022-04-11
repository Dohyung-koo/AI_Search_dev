package com.diquest.app.common;

import java.util.HashSet;
import java.util.Set;

import com.diquest.app.common.Constant.AUTO_COMPLETE_FIELD;
import com.diquest.util.StringUtil;

public enum ConflictManager {
	INSTANCE;
	
	private static Set<String> catNames = new HashSet<String>();
	private static Set<String> albumNames = new HashSet<String>();
	private static Set<String> actors = new HashSet<String>();
	private static Set<String> overseerNames = new HashSet<String>();
	private static Set<String> starringActors = new HashSet<String>();
	private static Set<String> keywords = new HashSet<String>();
	private static Set<String> titleEngs = new HashSet<String>();
	private static Set<String> playerEngs = new HashSet<String>();
	private static Set<String> directorEngs = new HashSet<String>();
	private static Set<String> castNameEngs = new HashSet<String>();
	private static Set<String> castNames = new HashSet<String>();
	private static Set<String> titleOrigins = new HashSet<String>();
	private static Set<String> writerOrigins = new HashSet<String>();
	private static Set<String> castNameMaxs = new HashSet<String>();
	private static Set<String> castNameMaxEngs = new HashSet<String>();
	private static Set<String> actDistMaxs = new HashSet<String>();
	private static Set<String> actDistMaxEngs = new HashSet<String>();
	
	private static Set<String> tv_catNames = new HashSet<String>();
	private static Set<String> tv_albumNames = new HashSet<String>();
	private static Set<String> tv_actors = new HashSet<String>();
	private static Set<String> tv_overseerNames = new HashSet<String>();
	private static Set<String> tv_starringActors = new HashSet<String>();
	private static Set<String> tv_keywords = new HashSet<String>();
	private static Set<String> tv_titleEngs = new HashSet<String>();
	private static Set<String> tv_playerEngs = new HashSet<String>();
	private static Set<String> tv_directorEngs = new HashSet<String>();
	private static Set<String> tv_castNameEngs = new HashSet<String>();
	private static Set<String> tv_castNames = new HashSet<String>();
	private static Set<String> tv_titleOrigins = new HashSet<String>();
	private static Set<String> tv_writerOrigins = new HashSet<String>();
	private static Set<String> tv_castNameMaxs = new HashSet<String>();
	private static Set<String> tv_castNameMaxEngs = new HashSet<String>();
	private static Set<String> tv_actDistMaxs = new HashSet<String>();
	private static Set<String> tv_actDistMaxEngs = new HashSet<String>();
	
	private ConflictManager() {
		
	}
	
	public static void init() {
		catNames.clear();
		albumNames.clear();
		actors.clear();
		overseerNames.clear();
		starringActors.clear();
		keywords.clear();
		titleEngs.clear();
		playerEngs.clear();
		directorEngs.clear();
		castNameEngs.clear();
		castNames.clear();
		titleOrigins.clear();
		writerOrigins.clear();
		castNameMaxs.clear();
		castNameMaxEngs.clear();
		actDistMaxs.clear();
		actDistMaxEngs.clear();
		
		tv_catNames.clear();
		tv_albumNames.clear();
		tv_actors.clear();
		tv_overseerNames.clear();
		tv_starringActors.clear();
		tv_keywords.clear();
		tv_titleEngs.clear();
		tv_playerEngs.clear();
		tv_directorEngs.clear();
		tv_castNameEngs.clear();
		tv_castNames.clear();
		tv_titleOrigins.clear();
		tv_writerOrigins.clear();
		tv_castNameMaxs.clear();
		tv_castNameMaxEngs.clear();
		tv_actDistMaxs.clear();
		tv_actDistMaxEngs.clear();
	}
	
	public static boolean isConflicted(String value, String type) {
		return isConflicted(value, type, false);
	}
	
	public static boolean isConflicted(String value, String type, boolean isTV) {
		if(value == null) {
			return true;
		}
		String input = StringUtil.removeWhitepace(value);
		
		if(input != null && input.length() == 0) {
			return true;
		}
		
		input = input.toUpperCase();
		
		Set<String> checkSet = null;
		
		if(isTV == false) {
			if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAT_NAME)) { checkSet = catNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ALBUM_NAME)) { checkSet = albumNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACTOR)) { checkSet = actors; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.OVERSEER_NAME)) { checkSet = overseerNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.STARRING_ACTOR)) { checkSet = starringActors; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.KEYWORD)) { checkSet = keywords; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.TITLE_ENG)) { checkSet = titleEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.PLAYER_ENG)) { checkSet = playerEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.DIRECTOR_ENG)) { checkSet = directorEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_ENG)) { checkSet = castNameEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME)) { checkSet = castNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.TITLE_ORIGIN)) { checkSet = titleOrigins; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.WRITER_ORIGIN)) { checkSet = writerOrigins; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_MAX)) { checkSet = castNameMaxs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG)) { checkSet = castNameMaxEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACT_DISP_MAX)) { checkSet = actDistMaxs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG)) { checkSet = actDistMaxEngs; }
		}
		else {
			if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAT_NAME)) { 	checkSet = tv_catNames; 	}
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ALBUM_NAME)) { checkSet = tv_albumNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACTOR)) { checkSet = tv_actors; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.OVERSEER_NAME)) { checkSet = tv_overseerNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.STARRING_ACTOR)) { checkSet = tv_starringActors; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.KEYWORD)) { checkSet = tv_keywords; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.TITLE_ENG)) { checkSet = tv_titleEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.DIRECTOR_ENG)) { checkSet = tv_directorEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.PLAYER_ENG)) { checkSet = tv_playerEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_ENG)) { checkSet = tv_castNameEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME)) { checkSet = tv_castNames; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.TITLE_ORIGIN)) { checkSet = tv_titleOrigins; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.WRITER_ORIGIN)) { checkSet = tv_writerOrigins; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_MAX)) { checkSet = tv_castNameMaxs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG)) { checkSet = tv_castNameMaxEngs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACT_DISP_MAX)) { checkSet = tv_actDistMaxs; }
			else if(type.equalsIgnoreCase(AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG)) { checkSet = tv_actDistMaxEngs; }
		}
		
		if(checkSet.contains(input) == false) {
			checkSet.add(input);
			return false;
		}
		else {
			return true;
		}
	}
}
