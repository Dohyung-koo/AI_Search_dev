package com.diquest.app.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.diquest.util.FileUtil;

public class Constant {
	
	public static String builderHome;
	public static String confPath;
	public static void init(){
		FileInputStream fis;
		try {
			confPath = FileUtil.appendEndsPath(confPath);
			fis = new FileInputStream(confPath + "config.properties");
			Properties properties = new Properties();
			properties.load(fis);
			
			//dhkim 20220112 html postgre 정보추가
			DB_INFO.HTML_POSTGRE_URL = properties.getProperty("HTML_POSTGRE_URL");
			DB_INFO.HTML_POSTGRE_USER = properties.getProperty("HTML_POSTGRE_USER");
			DB_INFO.HTML_POSTGRE_PASSWORD = properties.getProperty("HTML_POSTGRE_PASSWORD");
			DB_INFO.HTML_POSTGR_DB_NAME = properties.getProperty("HTML_POSTGR_DB_NAME");
			DB_INFO.HTML_POSTGRE_HOST = properties.getProperty("HTML_POSTGRE_HOST");
			DB_INFO.HTML_POSTGRE_PORT = properties.getProperty("HTML_POSTGRE_PORT");
						
			//Choihu 20181128 AR 오라클 정보추가
			DB_INFO.AR_ORACLE_URL = properties.getProperty("AR_ORACLE_DB_URL");
			DB_INFO.AR_ORACLE_DRIVER = properties.getProperty("AR_ORACLE_DB_DRIVER");
			DB_INFO.AR_ORACLE_USER = properties.getProperty("AR_ORACLE_DB_USER");
			DB_INFO.AR_ORACLE_PASSWORD = properties.getProperty("AR_ORACLE_DB_PASSWORD");
					
			//Choihu 20180627 오라클 정보추가
			DB_INFO.ORACLE_URL = properties.getProperty("ORACLE_DB_URL");
			DB_INFO.ORACLE_DRIVER = properties.getProperty("ORACLE_DB_DRIVER");
			DB_INFO.ORACLE_USER = properties.getProperty("ORACLE_DB_USER");
			DB_INFO.ORACLE_PASSWORD = properties.getProperty("ORACLE_DB_PASSWORD");
			
			DB_INFO.DB_URL = properties.getProperty("DB_URL");
			DB_INFO.DB_DRIVER = properties.getProperty("DB_DRIVER");
			DB_INFO.DB_USER = properties.getProperty("DB_USER");
			DB_INFO.DB_PASSWORD = properties.getProperty("DB_PASSWORD");
			
			DB_INFO.URL = properties.getProperty("DB_URL");
			DB_INFO.URL = properties.getProperty("DB_URL");
			DB_INFO.DRIVER = properties.getProperty("DB_DRIVER");
			DB_INFO.USER = properties.getProperty("DB_USER");
			DB_INFO.PASSWORD = properties.getProperty("DB_PASSWORD");
			DB_INFO.USE_SUB_DB = Boolean.parseBoolean(properties.getProperty("USE_SUB_DB"));
			
			if(DB_INFO.USE_SUB_DB){
				DB_INFO.KOFIC_URL = properties.getProperty("KOFIC_DB_URL");
				DB_INFO.KOFIC_DRIVER = properties.getProperty("KOFIC_DB_DRIVER");
				DB_INFO.KOFIC_USER = properties.getProperty("KOFIC_DB_USER");
				DB_INFO.KOFIC_PASSWORD = properties.getProperty("KOFIC_DB_PASSWORD");
			}
			
			DB_INFO.MARINER_DB_URL = properties.getProperty("MARINER_DB_URL");
			DB_INFO.MARINER_DB_DRIVER = properties.getProperty("MARINER_DB_DRIVER");
			DB_INFO.MARINER_DB_USER = properties.getProperty("MARINER_DB_USER");
			DB_INFO.MARINER_DB_PASSWORD = properties.getProperty("MARINER_DB_PASSWORD");
			DB_INFO.MARINER_QUERY_TABLE = properties.getProperty("MARINER_QUERY_TABLE");
			
			DB_NAME.DRAMA_TERM_RELATION = properties.getProperty("DRAMA_TERM_RELATION_TABLE");
			
			DB_INFO.PASSWORD = properties.getProperty("DB_PASSWORD");
			DISA.HOME = properties.getProperty("DISA_HOME");
			DISA.CATEGORY = properties.getProperty("DISA_CATEGORY");
			
			OPTION.DELETEOPTION=properties.getProperty("DELETEOPTION");
			OPTION.SELECTOPTION=properties.getProperty("SELECTOPTION");
			OPTION.SECTIONOPTION=properties.getProperty("SECTIONOPTION");
//			DATA.VOD_DATA = properties.getProperty("VOD_DATA");
//			DATA.CHANNEL_DATA = properties.getProperty("CHANNEL_DATA");
//			DATA.HIT_UCC_DATA = properties.getProperty("HIT_UCC_DATA");
//			DATA.EVENT_DATA = properties.getProperty("EVENT_DATA");
//			DATA.HIGHLIGHT_DATA = properties.getProperty("HIGHLIGHT_DATA");
//			DATA.UFLIX_PACKAGE_DATA = properties.getProperty("UFLIX_PACKAGE_DATA");
//			DATA.UFLIX_SERIES_DATA = properties.getProperty("UFLIX_SERIES_DATA");
			
			DRAMA.HOME_DIR = properties.getProperty("DRAMA_HOME_DIR");
			
			DRAMA.VIDEO_DST_DIR = properties.getProperty("VIDEO_DST_DIR");
			DRAMA.UFLIX_MOBILE_DST_DIR = properties.getProperty("UFLIX_MOBILE_DST_DIR");
			DRAMA.UFLIX_TVG_DST_DIR = properties.getProperty("UFLIX_TVG_DST_DIR");
			
			DIC.HOME = properties.getProperty("DIC_HOME");
			
			DRAMA.SIZE = properties.getProperty("DRAMA_SIZE");
			DATA.WORKDIRS = properties.getProperty("WORKDIRS");
			
			LOG.IS_LOG_DIR = properties.getProperty("IS_LOG_DIR");
			LOG.MARINER_LOG_DIR = properties.getProperty("MARINER_LOG_DIR");
			LOG.MARINER_LOG_SER=properties.getProperty("MARINER_LOG_SER");
			LOG.IS_LOG_SER=properties.getProperty("IS_LOG_SER");
			LOG.ADD_LOG_DIR = properties.getProperty("ADD_LOG_DIR");
			LOG.MARINERADD_LOG_DIR=properties.getProperty("MARINERADD_LOG_DIR");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * LGUPLUS 에서 제공하는 File 의 지정된 구분문자 문자 와 데이터 구축 타입 
	 * @author hoho
	 *
	 */
	public static class DELIMS{
		public final static String ENDLINE = "<<<EOL>>>";
		public final static String WORD_DELIM = "^|";
//		public final static String I30INSERT="C";
		public final static String INSERT = "I";
		public final static String UPDATE = "U";
		public final static String DELETE = "D";

	}
	/**
	 * FILE 구분 타입
	 * @author hoho
	 *
	 */
	public static class FILETYPE{
		public final static String VOD_ALL = "VIDEO_VOD_ALL";
		public final static String CHANNEL_ALL = "VIDEO_CHANNEL_ALL";
		public final static String HIT_UCC_ALL = "VIDEO_HIT_UCC_ALL";
		public final static String HIGHLIGHT_ALL = "VIDEO_HIGHLIGHT_ALL";
		public final static String EVENT_ALL = "VIDEO_EVENT_ALL";
		public final static String UFLIX_PACKAGE_ALL = "UFLIX_PACKAGE_ALL";
		public final static String UFLIX_SERIES_ALL = "UFLIX_SERIES_ALL";
		public final static String THEME_ALL = "THEME_ALL";
		public final static String VOD_APPEND = "VIDEO_VOD_APPEND";
		public final static String CHANNEL_APPEND = "VIDEO_CHANNEL_APPEND";
		public final static String HIT_UCC_APPEND = "VIDEO_HIT_UCC_APPEND";
		public final static String HIGHLIGHT_APPEND = "VIDEO_HIGHLIGHT_APPEND";
		public final static String EVENT_APPEND = "VIDEO_EVENT_APPEND";
		public final static String UFLIX_PACKAGE_APPEND = "UFLIX_PACKAGE_APPEND";
		public final static String UFLIX_SERIES_APPEND = "UFLIX_SERIES_APPEND";
		public final static String WATCH_RECORD ="WATCH_RECORD";
		public final static String I30_ALL = "I30_ALL";
		public final static String I30_APPEND="I30_APPEND";
		public final static String I30_CHANNEL="I30_CHANNEL";
		public final static String VR_CONTENTS = "VR_CONTENTS";
		public final static String CJ_CONTENTS = "CJ_CONTENTS";
		public final static String KIDS_SPC = "KIDS_SPC";
		public final static String KIDS_EXP = "KIDS_EXP";
//		public final static String I30_CHANNEL_APPEND="I30_CHANNEL_APPEND";
	}
	/**
	 * 데이터 구분 타입
	 * @author hoho
	 *
	 */
	public static class TYPE{
		public final static String FILTERING_CODE="9"; //실시간 TV , schedule.txt
		public final static String FILTERING_CODE2="1"; //
//		public final static String FILTERRING_CODE3="10"; //
		public final static String SVC_TYPE = "HDTV"; // 대박영상,하이라이트,이벤트
		public final static String SVC_TYPE2 = "MUSI"; // 대박영상,하이라이트,이벤트
		
		public final static String VIDEO_PORTAL_CATGB = "NSC"; // VOD
		public final static String VIDOE_PORTAL_MFX="MFX";
		public final static String VIDOE_PORTAL_UFX = "UFX"; //Choihu 20180806 추가
		////////////////미사용
		public final static String UFLIX_CATGB_MOBILE = "MFX"; // UFLIX 모바일 데이터
		public final static String UFLIX_CATGB_TV = "UFX"; // UFLIX TV 데이터
		////////////////
		public final static String IPTV = "I20"; // 테스트 TYPE
		public final static String IPTV_30="I30";
		public final static String IPTV_UFLIX="UFX";
		public final static String SECTION_TYPE ="LTE_HVD";
	}
	
	public static class Excpect{
		public final static String any="애니"; 
		public final static String  kids= "키즈";
		public final static String foreign= "해외";
		public final static String tvreplay = "TV다시보기"; // UFLIX 모바일 데이터
		public final static String Result_type="CAT_GB";
	}
	
	
	/**
	 * LGUPLUS 에서 제공하는 File의 이름
	 * @author hoho
	 *
	 */
	public static class FILE_NAME{
/*		Choihu 20180803 VIDEO_VOD로 통합 
 * 		public final static String UFLIX_PACKAGE = "package";
		public final static String UFLIX_SERIES = "contents";*/
		public final static String VOD = "package_contents";
		public final static String CHANNEL = "schedule";
		public final static String HIT_UCC = "contents";
		public final static String HIGHLIGHT = "hlt_contents";
		public final static String EVENT = "banner_event";
		public final static String THEME = "theme";
		public final static String WATCH_RECORD="watch_stat_day";
		public final static String VOD_APPEND = "package_contents_1hour";
		public final static String CHANNEL_APPEND = "schedule_1hour";
		public final static String I30="package_contents_I30";
		public final static String I30_CHANNEL="schedule_I30";
		public final static String I30_APPEND = "package_contents_1hour_I30"; 
		public final static String VR_CONTENTS ="VR_CONTENTS";
		public final static String CJ_CONTENTS ="cj_contents";
		public final static String KIDS_SPC ="kinder";
		public final static String KIDS_EPX ="recommand";
	}
	
	/**
	 * LGUPLUS 에서 제공하는 데이터에 1ROW 당 데이터 개수 (전체 색인데이터 인경우에만 해당)
	 * @author hoho
	 */
	public static class SPLIT_SIZE{
		public final static int UFLIX_PACKAGE = 72;
		public final static int UFLIX_SERIES = 61;
		//public final static int VOD = 79;//Choihu Size 증가 2018.08.08 VIDEO_TYPE추가
		//public final static int VOD = 81;//Choihu Size 증가 2018.12.04 VRTYPE추가
		//public final static int VOD = 89;//Choihu Size 증가 20200122 아이들나라추가
		/*20200603 Choihu 아이돌LIVE 메타 추가작업 MAIN_PROPERTY, SUB_PROPERTY */
		public final static int VOD = 91;
		
		public final static int CHANNEL = 29; 
		//public final static int HIT_UCC = 19;//Choihu Size 증가 2018.07.27공연 
		//public final static int HIT_UCC = 21;//Choihu Size 증가 20190425 공연고도화
		public final static int HIT_UCC = 24;//Choihu Size 증가  공연2차고도화
		//public final static int HIGHLIGHT= 24; //Choihu Size 증가 20190425 공연고도화
		//public final static int HIGHLIGHT= 27; //Choihu Size 증가  공연2차고도화
		/*20200603 Choihu 아이돌LIVE 메타 추가작업 MAIN_PROPERTY, SUB_PROPERTY */
		public final static int HIGHLIGHT= 29;
		
		public final static int EVENT = 15;
		public final static int THEME = 11;
		//public final static int VOD_APPEND = 77;//Choihu Size 증가 2018.08.08 VIDEO_TYPE추가
		//public final static int VOD_APPEND = 79;//Choihu Size 증가 2018.12.04 VRTYPE추가
		//public final static int VOD_APPEND = 87;//Choihu Size 증가 20200122 아이들나라추가
		/*20200603 Choihu 아이돌LIVE 메타 추가작업 MAIN_PROPERTY, SUB_PROPERTY */
		public final static int VOD_APPEND = 89;
		
		public final static int WATCH_RECORD=3;
		//public final static int I30=73;//Choihu 20200720 SUPER_ID 갯수증가 +2 EPI_KEYWORD 갯수증가 +2
		public final static int I30=77;
		//public final static int I30_APPEND=74;//Choihu 20200720 SUPER_ID 갯수증가 +2 EPI_KEYWORD 갯수증가 +2
		public final static int I30_APPEND=78;
		public final static int I30_CHANNEL=29;
		public final static int VR_CONTENTS=34;//Choihu Size 증가 2019.03.15
		//public final static int CJ_CONTENTS=30;//Choihu 공연2차고도화
		/*20200603 Choihu 아이돌LIVE 메타 추가작업 MAIN_PROPERTY, SUB_PROPERTY */
		public final static int CJ_CONTENTS=32;
		public final static int KIDS_SPC=9;
		public final static int KIDS_EXP=4;
	}
	
	/**
	 * DB TABLE명 정보
	 * @author hoho
	 */
	public static class DB_NAME{
		public final static String UFLIX_PACKAGE = "UFLIX_PACKAGE";
		public final static String UFLIX_SERIES = "UFLIX_SERIES";
		public final static String VOD = "VIDEO_VOD";
		public final static String CHANNEL = "VIDEO_CHANNEL";
		public final static String HIT_UCC = "VIDEO_HIT_UCC";
		public final static String HIGHLIGHT = "VIDEO_HIGHLIGHT";
		public final static String EVENT = "VIDEO_EVENT";
		public final static String VIDEO_AUTO_COMPLETE = "VIDEO_AUTO_COMPLETE";
		public final static String UFLIX_TVG_AUTO_COMPLETE = "UFLIX_TVG_AUTO_COMPLETE";
		public final static String UFLIX_MOBILE_AUTO_COMPLETE = "UFLIX_MOBILE_AUTO_COMPLETE";
		public final static String MOVIE_KOFIC = "movie_kofic";
		public final static String MOVIE_SUM_INFO = "movie_sum_info";
		public final static String NAVER_NEWS_ENT = "naver_news_ent";
		public final static String THEME = "UFLIX_THEME";
		public final static String RLEVEL = "RLEVEL";
		public final static String IPTV_VOD_AUTO_COMPLETE = "IPTV_VOD_AUTO_COMPLETE";
		public final static String IPTV_UFLIX_AUTO_COMPLETE = "IPTV_UFLIX_AUTO_COMPLETE";
		public final static String WATCH_RECORD = "WATCH_RECORD";
		public final static String WATCH_RECORD_SECTION="WATCH_RECORD_SECTION";
		public final static String I30="I30";
		public final static String I30_AUTO_COMPLETE = "I30_AUTO_COMPLETE";
		public final static String I30_CHANNEL = "I30_CHANNEL";
		
		//Choihu 20180626 공연테이블추가
		public final static String SHOW = "SHOW_VOD";
		public final static String SHOW_AUTO_COMPLETE = "SHOW_AUTO_COMPLETE";
		
		//Choihu 20181130 AR,VR테이블 추가
		public final static String AR = "AR_CONTENTS";
		public final static String AR_AUTO_COMPLETE = "AR_AUTO_COMPLETE";
		public final static String VR = "VR_VOD";
		public final static String VR_AUTO_COMPLETE = "VR_AUTO_COMPLETE";
		public final static String VR_CONTENTS = "VR_CONTENTS";
		
		//Choihu 20190114 골프2테이블 추가
		public final static String GOLF2 = "GOLF2_VOD";
		public final static String GOLF2_AUTO_COMPLETE = "GOLF2_AUTO_COMPLETE";
		
		//Choihu 20200122 아이들 나라 추가
		public final static String KIDS = "KIDS_VOD";
		public final static String KIDS_AUTO_COMPLETE = "KIDS_AUTO_COMPLETE";
		
		//dhkim 20210917 아이들 나라클래스 추가
		public final static String KIDSCLASS = "KIDSCLASS_VOD";
		public final static String KIDSCLASSSPC = "KIDSCLASSSPC_VOD";
		public final static String KIDSCLASSEXP = "KIDSCLASSEXP_VOD";
		public final static String KIDSCLASS_AUTO_COMPLETE = "KIDSCLASS_AUTO_COMPLETE";
		
		public final static String CJ_CONTENTS = "CJ_CONTENTS";
		
		
		public final static String DRAMA_MGMT = "DRAMA_MGMT";
		public final static String DRAMA_DATA = "DRAMA_DATA";
		public static String DRAMA_TERM_RELATION = "";
		
		public final static String  MARINER_INDEX_LOG = "IR01_INDEX_LOG";
		public final static String IS_LOG = "IS_LOG";
	}
	
	/**
	 * DB 작업 옵션
	 * @author hoho
	 *
	 */
	public static class OPTION{
		public final static byte INSERT = 1;
		public final static byte UPDATE = 2;
		public final static byte DELETE = 3;
		public static String SELECTOPTION = "";
		public static String DELETEOPTION = "";
		public static String SECTIONOPTION ="";
	}
	
	/**
	 * DB정보
	 * @author hoho
	 */
	public static class DB_INFO{
		
		public static String URL="";
		public static String DRIVER="";
		public static String USER="";
		public static String PASSWORD="";
		
		public static boolean USE_SUB_DB = false;
		
		public static String KOFIC_URL="";
		public static String KOFIC_DRIVER="";
		public static String KOFIC_USER="";
		public static String KOFIC_PASSWORD="";
		public final static int BATCH_SIZE = 5000;
		
		public static String MARINER_DB_URL="";
		public static String MARINER_DB_DRIVER="";
		public static String MARINER_DB_USER="";
		public static String MARINER_DB_PASSWORD="";
		public static String MARINER_QUERY_TABLE="";
		
		public static String DRAMA_URL="";
		public static String DRAMA_DRIVER="";
		public static String DRAMA_USER="";
		public static String DRAMA_PASSWORD="";
		
		public static String DB_URL="";
		public static String DB_DRIVER="";
		public static String DB_USER="";
		public static String DB_PASSWORD="";
		
		//Choihu 20180627 공연 오라클정보추가
		public static String ORACLE_URL="";
		public static String ORACLE_DRIVER="";
		public static String ORACLE_USER="";
		public static String ORACLE_PASSWORD="";
		
		//Choihu 20181128 AR 오라클정보추가
		public static String AR_ORACLE_URL="";
		public static String AR_ORACLE_DRIVER="";
		public static String AR_ORACLE_USER="";
		public static String AR_ORACLE_PASSWORD="";
		
		//dhkim 20220110 아이들나라 HTML DB 정보추가
		public static String HTML_POSTGRE_URL="";
		public static String HTML_POSTGR_DB_NAME="";
		public static String HTML_POSTGRE_USER="";
		public static String HTML_POSTGRE_PASSWORD="";
		public static String HTML_POSTGRE_HOST="";
		public static String HTML_POSTGRE_PORT="";
		

	}
	
	/**
	 * 사전정보
	 * @author hoho
	 *
	 */
	public static class DIC{
		public static String HOME = "";
		public static final String VIDEO = "video";
		public static final String UFLIX_MOBILE = "uflixmobile";
		public static final String UFLIX_TVG = "uflixtvg";
	}
	
	/**
	 * Disa 정보
	 * @author hoho
	 */
	public static class DISA{
		public static String HOME = "";
		public static String CATEGORY = "";
	}
	
	/**
	 * 데이터 PATH 정보
	 * @author hoho
	 */
	public static class DATA{
		public static String WORKDIRS = "";
		
//		public static String VOD_DATA = "";
//		public static String CHANNEL_DATA = "";
//		public static String HIT_UCC_DATA = "";
//		public static String EVENT_DATA = "";
//		public static String HIGHLIGHT_DATA = "";
//		public static String UFLIX_PACKAGE_DATA = "";
//		public static String UFLIX_SERIES_DATA = "";
	}
	
	/**
	 * DQDOC 이 생성될 드라마 정보
	 * @author hoho
	 */
	public static class DRAMA{
		public static String HOME_DIR = "";
		public static String TERM_MAP_FILE = "termMap.map";
		public static String SIZE = "";
		public static String VIDEO_DST_DIR = "";
		public static String UFLIX_MOBILE_DST_DIR = "";
		public static String UFLIX_TVG_DST_DIR = "";
	}
	/**
	 *	FilToDB 작업 옵션 
	 * @author hoho
	 */
	public static class  FILE_TO_DB_TYPE{
		public static final String ALL = "ALL";  
		public static final String VIDEO = "V"; 
		public static final String VIDEO_AUTO_KEYWORD = "VA"; 
		public static final String UFLIX = "U"; 
		public static final String UFLIX_AUTO_KEYWORD = "UA"; 
	}
	
	/**
	 * Collection 정보
	 */
	public static class COLLECTION{
		
		public final static String UFLIX_THEME = "UFLIX_THEME";
		public final static String UFLIX_VOD = "UFLIX_VOD";
		public final static String UFLIX_TVG_AUTO_COMPLETE = "UFLIX_TVG_AUTO_COMPLETE";
		public final static String UFLIX_MOBILE_AUTO_COMPLETE = "UFLIX_MOBILE_AUTO_COMPLETE";
		
		public final static String VIDEO_VOD = "VIDEO_VOD";
		public final static String VIDEO_CHANNEL = "VIDEO_CHANNEL";
		public final static String VIDEO_HIT_UCC = "VIDEO_HIT_UCC";
		public final static String VIDEO_HIGHLIGHT = "VIDEO_HIGHLIGHT";
		public final static String VIDEO_EVENT = "VIDEO_EVENT";
		public final static String VIDEO_AUTO_COMPLETE = "VIDEO_AUTO_COMPLETE";
		
		
		public final static String DRAMA = "DRAMA";
		public final static String DRAMA_DUMMY = "DRAMA_DUMMY";
		public final static String LGUPLUS_DUMMY = "LGUPLUS_DUMMY";
		public final static String WATCH_RECORD = "WATCH_RECORD";
		
		public final static String I30="I30";
		public final static String I30_AUTO_COMPLETE="I30_AUTO_COMPLETE";
		public final static String I30_CHANNEL="I30_CHANNEL";
		
		//Choihu 20180626 공연테이블추가
		public final static String SHOW = "SHOW_VOD";
		public final static String SHOW_AUTO_COMPLETE = "SHOW_AUTO_COMPLETE";
		
		//Choihu 20180806 골프 로그 추가
		public final static String GOLF = "VOD_GOLF";
		public final static String GOLF_AUTO_COMPLETE = "VOD_GOLF_AUTO_COMPLETE";
		
		//Choihu 20181213 로그 추가
		public final static String VR = "VR_VOD";
		public final static String VR_CONTENTS = "VR_CONTENTS";
		public final static String VR_AUTO_COMPLETE = "VR_AUTO_COMPLETE";
			
		//Choihu 20180806 골프 로그 추가
		public final static String GOLF2 = "GOLF2_VOD";
		public final static String GOLF2_AUTO_COMPLETE = "GOLF2_AUTO_COMPLETE";	
		
		public final static String AR = "AR_CONTENTS";
		public final static String AR_AUTO_COMPLETE = "AR_AUTO_COMPLETE";
						
		
		public static String VIDEO_DRAMA = "VIDEO_KEYWORD";
		public static String UFLIX_MOBILE_DRAMA = "UFLIX_MOBILE_KEYWORD";
		public static String UFLIX_TVG_DRAMA = "UFLIX_TVG_KEYWORD";
	}
	
	/**
	 * 자동완성 필드 목록 
	 * @author hoho
	 *
	 */
	public static class AUTO_COMPLETE_FIELD{
		public static final String CAT_NAME = "CAT_NAME"; 
		public static final String ALBUM_NAME = "ALBUM_NAME"; 
		public static final String ACTOR = "ACTOR"; 
		public static final String OVERSEER_NAME = "OVERSEER_NAME"; 
		public static final String STARRING_ACTOR = "STARRING_ACTOR"; 
		public static final String KEYWORD = "KEYWORD";
		
		public static final String TITLE_ENG = "TITLE_ENG"; 
		public static final String DIRECTOR_ENG = "DIRECTOR_ENG"; 
		public static final String PLAYER_ENG = "PLAYER_ENG"; 
		public static final String CAST_NAME_ENG = "CAST_NAME_ENG"; 
		public static final String CAST_NAME = "CAST_NAME"; 
		public static final String TITLE_ORIGIN = "TITLE_ORIGIN"; 
		public static final String WRITER_ORIGIN = "WRITER_ORIGIN"; 
		
		public static final String CAST_NAME_MAX = "CAST_NAME_MAX"; 
		public static final String CAST_NAME_MAX_ENG = "CAST_NAME_MAX_ENG"; 
		public static final String ACT_DISP_MAX = "ACT_DISP_MAX"; 
		public static final String ACT_DISP_MAX_ENG = "ACT_DISP_MAX_ENG"; 
		
	
		
	}
	
	public static class LOG{
		

		public static final String delims = "|";

		public static String IS_LOG_DIR = ""; 
		public static String MARINER_LOG_DIR = "";
		public static String MARINERADD_LOG_DIR = "";
		
		public static final String IS_KOFIC_CATEGORY_ID = "11";
		public static final String[] IS_KOFIC_BBS_ID = {"186", "187", "188"};
		
		public static String IS_NAVER_CATEGORY_ID = "44";
		public static final String[] IS_NAVER_BBS_ID = {"299", "300", "301"};
		
		public static String IS_SUCCESS_RESULT_CODE = "20000000";
		public static String IS_FAIL_DB_RESULT_CODE = "40000001";
		public static String IS_FAIL_KOFIC_INFO_RESULT_CODE = "50000003";
		public static String IS_FAIL_KOFIC_SUM_RESULT_CODE = "50000004";
		public static String IS_FAIL_NAVER_TV_RESULT_CODE = "50000005";
		public static String IS_FAIL_NAVER_DRAMA_RESULT_CODE = "50000006";
		public static String IS_FAIL_NAVER_MOVIE_RESULT_CODE = "50000007";
		
		public static String IS_LOG_SER ="";
		public static String MARINER_LOG_SER ="";
		public static String ADD_LOG_DIR="";
		public static String ADD_LOG_DIR2="";
		public static String[] IS_LOG_FIELD = {
			"SEQ_ID",
			"LOG_TIME",
			"LOG_TYPE",
			"SID",
			"RESULT_CODE",
			"REQ_TIME",
			"RSP_TIME",
			"CLIENT_IP",
			"DEV_INFO",
			"OS_INFO",
			"NW_INFO",
			"SVC_NAME",
			"DEV_MODEL",
			"CARRIER_TYPE",
			"SESSION_ID",
			"ONEID_EMAIL",
			"CTN",
			"APP_EXE_PATH",
			"SVC_TYPE",
			"COLLECT_COUNT",
		};
		
		public static String[] MARINER_LOG_FIELD = {
			"SEQ_ID",
			"LOG_TIME",
			"LOG_TYPE",
			"SID",
			"RESULT_CODE",
			"REQ_TIME",
			"RSP_TIME",
			"CLIENT_IP",
			"DEV_INFO",
			"OS_INFO",
			"NW_INFO",
			"SVC_NAME",
			"DEV_MODEL",
			"CARRIER_TYPE",
			"SESSION_ID",
			"ONEID_EMAIL",
			"CTN",
			"APP_EXE_PATH",
			"INDEX_TYPE",
			"INDEX_NAME",
			"INDEX_RESULT",
			"INDEX_DESC",
			"INDEX_SIZE",
			"INDEX_START_TIME",
			"INDEX_END_TIME",
			"SVC_TYPE",
			"COLLECT_COUNT",
		};
	}
	
	public static class DB2DB {

		public static String LOOKUP_TABLE ="PERSON_GROUP_LOOKUP";
		public static String AR_LOOKUP_TABLE ="ARENT.TB_ARENT_SEARCH_LOOKUP";
		
		public static String all_Select(String tableName) {
			StringBuffer buf = new StringBuffer();
			String[] columns = null;
			if ("VW_PERSON_GROUP_LINK_INFO".equals(tableName) || "SHOW_PERSON_GROUP_LINK_INFO".equals(tableName)) {
				columns = LINK_COLUMN;
			} else if ("VW_PERSON_INFO".equals(tableName) || "SHOW_PERSON_INFO".equals(tableName)) {
				columns = PERSON_COLUMN;
			} else if ("VW_GROUP_INFO".equals(tableName) || "SHOW_GROUP_INFO".equals(tableName)) {
				columns = GROUP_COLUMN;
			} else if("PERSON_GROUP_LOOKUP".equals(tableName)) {
				columns = LOOKUP_COLUMN;
			} else if("ARENT.TB_ARENT_SEARCH_LOOKUP".equals(tableName)) {
				columns = AR_LOOKUP_COLUMN;
			} else if("AR_CONTENTS".equals(tableName)) {
				columns = AR_CONTENTS_COLUMN;
			} else if("ARENT.VW_CONTENTS_INFO".equals(tableName)) {
				columns = AR_ORACLE_CONTENTS_COLUMN;
			}else if("i_contentsprogramming_schema.vw_contents".equals(tableName) || "KIDS_HTML".equals(tableName)) {
				columns = HTML_COLUMN;
			}
			
			for(int j = 0; j < columns.length; j++) {
				buf.append(columns[j]);
				if(j!=columns.length-1) {
					buf.append(",");
				}
			}
			//System.out.println("ALL_SELEST : "+"SELECT "+buf.toString()+" FROM " + tableName);
			return "SELECT "+buf.toString()+" FROM " + tableName;
		}

		public static String whereSet(String table, String pg_Id, String revision_Code) {
			String whereSet = "";
			if ("VW_PERSON_GROUP_LINK_INFO".equals(table)) {
				whereSet = " WHERE GROUP_ID= '"+pg_Id+"' AND G_RV_CODE = '"+revision_Code+"'";//여기 수정해야함
			} else if ("VW_PERSON_INFO".equals(table)) {
				whereSet = " WHERE PERSON_ID= '" + pg_Id + "' AND REVISION_CODE = '" + revision_Code + "'";
			} else if ("VW_GROUP_INFO".equals(table)) {
				whereSet = " WHERE GROUP_ID= '" + pg_Id + "' AND REVISION_CODE = '" + revision_Code + "'";
			} else if ("PERSON_GROUP_LOOKUP".equals(table)) {
				whereSet = " WHERE SYNC_STATUS IS NULL OR SYNC_STATUS <> '2'";
			} else if ("ARENT.TB_ARENT_SEARCH_LOOKUP".equals(table)) {
				whereSet = " WHERE SYNC_STATUS IS NULL OR ( SYNC_STATUS = '0' OR  SYNC_STATUS = '2') ORDER BY LOOKUP_KEY";
			} else if ("ARENT.VW_CONTENTS_INFO".equals(table)) {
				whereSet = " WHERE CONTENTS_KEY = '"+pg_Id+"'";
			}
			return whereSet;

		}
		public static void generateInsertSql(StringBuffer buf, String tableName, String[] columns) {
			buf.append("insert into ");
			buf.append(tableName);
			buf.append("( ");
			//buf.append(StringUtils.join(columns, "(", ", ", ")"));
			
			for(int j = 0; j < columns.length; j++) {
				buf.append(columns[j]);
				if(j!=columns.length-1) {
					buf.append(",");
				}
			}
			
			buf.append(" ) values (");
			if (columns.length > 0) {
				buf.append("?");
				for (int a = 1; a < columns.length; a++) {
					buf.append(",?");
				}
			}
			buf.append(")");
		}
		
		public static void generateUpdateSql(StringBuffer buf, String tableName, String[] columns) {
			if (columns.length == 0) {
				throw new IllegalArgumentException();
			}

			buf.append("update ");
			buf.append(tableName);
			buf.append(" set ");
			format(buf, columns, "", " = ?", "", ", ", "");
			if("VW_GROUP_INFO".equals(tableName) || "SHOW_GROUP_INFO".equals(tableName)) {
				buf.append(" WHERE GROUP_ID = ? AND REVISION_CODE = ? ");
			}else if("VW_PERSON_INFO".equals(tableName) || "SHOW_PERSON_INFO".equals(tableName)) {
				buf.append(" WHERE PERSON_ID = ? AND REVISION_CODE = ? ");
			}else if("PERSON_GROUP_LOOKUP".equals(tableName) || "TB_ARENT_SEARCH_LOOKUP".equals(tableName)) {
				buf.append(" WHERE SEQ = ?");
			} else if("AR_CONTENTS".equals(tableName)) {
				buf.append(" WHERE CONTENTS_KEY = ? ");
			}  else if("ARENT.TB_ARENT_SEARCH_LOOKUP".equals(tableName)) {
				buf.append(" WHERE LOOKUP_KEY = ? ");
			} 
			
		}
		
		public static void generateAllUpdateSql(StringBuffer buf, String tableName, String[] columns) {
			if (columns.length == 0) {
				throw new IllegalArgumentException();
			}

			buf.append("update ");
			buf.append(tableName);
			buf.append(" set ");
			format(buf, columns, "", " = ?", "", ", ", "");
		
		}
		
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
		
		public static void format(StringBuffer trans, String[] source, String everyLeft, String everyRight, String left, String separator, String right) {
			trans.append(left);
			if (source.length > 0) {
				trans.append(everyLeft);
				trans.append(source[0]);
				trans.append(everyRight);
			}
			for (int i = 1; i < source.length; i++) {
				trans.append(separator);
				trans.append(everyLeft);
				trans.append(source[i]);
				trans.append(everyRight);
			}
			trans.append(right);
		}

		public static String[] AR_ORACLE_TABLE = new String[] {  "ARENT.VW_CONTENTS_INFO", };
		
		public static String[] HTML_POSTGRE_TABLE = new String[] {  "i_contentsprogramming_schema.vw_contents" };
		
		public static String[] AR_MYSQL_TABLE = new String[] { "AR_CONTENTS" };
		
		public static String[] HTML_MYSQL_TABLE = new String[] { "KIDS_HTML" };

		
		public static String[] ORACLE_TABLE = new String[] { "VW_PERSON_GROUP_LINK_INFO", "VW_PERSON_INFO",
				"VW_GROUP_INFO", };

		public static String[] MYSQL_TABLE = new String[] { "SHOW_PERSON_GROUP_LINK_INFO", "SHOW_PERSON_INFO",
				"SHOW_GROUP_INFO", };

		public static String[] PERSON_COLUMN = new String[] { "PERSON_ID", "REVISION_CODE", "COMPANY", "BIRTH_DT", "NAME",
				"SEX_CODE", "DEBUT_DT", "PRIZE1", "PRIZE2", "PRIZE3", "PRIZE4", "PRIZE5", "FACEBOOK", "INSTAGRAM",
				"TWITTER", "HOMEPAGE", "UTUBE", /*"MIMS_IMG_URL", */};

		public static String[] GROUP_COLUMN = new String[] { "GROUP_ID", "REVISION_CODE", "COMPANY", "NAME", "DEBUT_DT",
				"PRIZE1", "PRIZE2", "PRIZE3", "PRIZE4", "PRIZE5", "FACEBOOK", "INSTAGRAM", "TWITTER", "HOMEPAGE", "UTUBE",
				/*"MIMS_IMG_URL",*/ };

		public static String[] LINK_COLUMN = new String[] { "GROUP_ID", "G_RV_CODE", "PERSON_ID", "P_RV_CODE", };

		public static String[] LOOKUP_COLUMN = new String[] { "SEQ", "PG_ID", "REVISION_CODE", "VIEW_NAME", "LINK_JOB", "CREATE_DT", "UPDATE_DT", "SYNC_STATUS", };
		
		public static String[] AR_LOOKUP_COLUMN = new String[] { "LOOKUP_KEY", "CONTENTS_KEY", "REVISION_CODE", "VIEW_NAME", "LINK_JOB", "CREATE_DT", "UPDATE_DT", "SYNC_STATUS", };
		
		public static String[] AR_CONTENTS_COLUMN = new String[] { 
				"CONTENTS_KEY",
				"REVISION_CODE",
				"CONTENTS_NAME",
				"CONTENTS_TYPE",
				"CONTENTS_DESC",
				"CONTENTS_DETAIL_DESC",
				"CONTENTS_ARTIST",
/*				"CONTENTS_EXTRA1",
				"CONTENTS_EXTRA2",
				"CONTENTS_EXTRA3",
				"CONTENTS_EXTRA4",
				"CONTENTS_EXTRA5",*/
				"CONTENTS_TAG",
				"THUMB_URL",
				"MAIN_THUMB_URL",
				"MYLIST_THUMB_URL",
				"IMAGE1_URL",
				"IMAGE2_URL",
				"IMAGE3_URL",
				"IMAGE4_URL",
				"IMAGE5_URL",
				"PLAY_TIME",
				"VIEW_CNT",
				"START_DATE",
				"END_DATE",
				"REG_DATE",
				"ADULT_YN",
				"CONTENTS_START_DT",
				"CONTENTS_END_DT",
				"CONTENTS_REG_DT",
				"CONTENTS_NAME_CHOSUNG"
		};
		
		public static String[] AR_ORACLE_CONTENTS_COLUMN = new String[] { 
				"CONTENTS_KEY",
				"REVISION_CODE",
				"CONTENTS_NAME",
				"CONTENTS_TYPE",
				"CONTENTS_DESC",
				"CONTENTS_DETAIL_DESC",
				"CONTENTS_ARTIST",
/*				"CONTENTS_EXTRA1",
				"CONTENTS_EXTRA2",
				"CONTENTS_EXTRA3",
				"CONTENTS_EXTRA4",
				"CONTENTS_EXTRA5",*/
				"CONTENTS_TAG",
				"THUMB_URL",
				"MAIN_THUMB_URL",
				"MYLIST_THUMB_URL",
				"IMAGE1_URL",
				"IMAGE2_URL",
				"IMAGE3_URL",
				"IMAGE4_URL",
				"IMAGE5_URL",
				"PLAY_TIME",
				"VIEW_CNT",
				"START_DATE",
				"END_DATE",
				"REG_DATE",
				"ADULT_YN",
				"CONTENTS_START_DT",
				"CONTENTS_END_DT",
				"CONTENTS_REG_DT",

		};
		
		public static String[] HTML_COLUMN = new String[] { 
				"CONTENTS_ID",
				"CONTENTS_TYPE",
				"CONTENTS_NAME",
				"CONTENTS_WRITER",
				"CONTENTS_ILLUSTRATOR",
				"CONTENTS_PUBLISHER",
				"CONTENTS_SUMMARY",
				"CONTENTS_PAGE",
				"CONTENTS_PLAY_TIME",
				"CONTENTS_RECOMMEND_AGE",
				"CONTENTS_FREE_YN",
				"CONTENTS_PRICE",
				"CONTENTS_LEVEL",
				"CONTENTS_THEME",
				"CONTENTS_AR_POINTS",
				"CONTENTS_COVER_IMG_URL",
				"CONTENTS_AOS_URL",
				"CONTENTS_IOS_URL",
				"CONTENTS_TAG",
				"CONTENTS_HIGHLIGHT",
				"FILE_ID",
				"CONTENTS_PLATFORM",
				"CONTENTS_START_DT",
				"CONTENTS_DESC",
				"CONTENTS_END_DT",
				"CONTENTS_STATUS",
				"CATEGORY_ID",
				"CATEGORY_NAME",
				"DATA_SOURCE",
				"VOD_QUIZ_FLAG",
				"INT_QUIZ_FLAG",
				"CONTENTS_DELETE_YN",
				"REG_DATE",
				"PROD_DATE",


		};
		
		public static String[] HTMLAUTO_COLUMN = new String[] { 
				"CONTENTS_ID",
				"CONTENTS_NAME",
		};
		
		
		public static String[] KIDSAUTO_COLUMN = new String[] { 
				"ORG_ID",
				"KEYWORD",
				"KEYWORD_CHOSUNG",
				"TYPE",
				"FIELD",
		};
		
		
		public static String[] column(String a) {
			String[] s = null;
			if ("VW_PERSON_GROUP_LINK_INFO".equals(a) || "SHOW_PERSON_GROUP_LINK_INFO".equals(a)) {
				s = LINK_COLUMN;
			} else if ("VW_PERSON_INFO".equals(a) || "SHOW_PERSON_INFO".equals(a)) {
				s = PERSON_COLUMN;
			} else if ("VW_GROUP_INFO".equals(a) || "SHOW_GROUP_INFO".equals(a)) {
				s = GROUP_COLUMN;
			} else if("PERSON_GROUP_LOOKUP".equals(a)) {
				s = LOOKUP_COLUMN;
			} else if("ARENT.TB_ARENT_SEARCH_LOOKUP".equals(a)) {
				s = AR_LOOKUP_COLUMN;
			} else if("AR_CONTENTS".equals(a)) {
				s = AR_CONTENTS_COLUMN;
			} else if("ARENT.VW_CONTENTS_INFO".equals(a)) {
				s = AR_ORACLE_CONTENTS_COLUMN;
			}else if("i_contentsprogramming_schema.vw_contents".equals(a)|| "KIDS_HTML".equals(a)) {
				s = HTML_COLUMN;
			}else if("AUTO".equals(a)){
				s =  HTMLAUTO_COLUMN;
			}else if ("KIDS_AUTO_COMPLETE".equals(a)) {
				s = KIDSAUTO_COLUMN;
			}
			return s;
		}
	}
	
}
