package com.diquest.app.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.SynchronousQueue;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import com.diquest.app.common.ConflictManager;
import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.AUTO_COMPLETE_FIELD;
import com.diquest.app.common.Constant.DB2DB;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.app.common.Constant.DB_NAME;
import com.diquest.app.common.Constant.DELIMS;
import com.diquest.app.common.Constant.FILETYPE;
import com.diquest.app.common.Constant.FILE_NAME;
import com.diquest.app.common.Constant.FILE_TO_DB_TYPE;
import com.diquest.app.common.Constant.SPLIT_SIZE;
import com.diquest.app.common.Method;
import com.diquest.db.ArAutoCompleteDB;
import com.diquest.db.ChannelDB;
import com.diquest.db.CjContentsDB;
import com.diquest.db.EventDB;
import com.diquest.db.Golf2AutoCompleteDB;
import com.diquest.db.Golf2DB;
import com.diquest.db.HighLightDB;
import com.diquest.db.HitUccDB;
import com.diquest.db.I30AutoCompleteDB;
import com.diquest.db.I30ChannelDB;
import com.diquest.db.I30DB;
import com.diquest.db.IptvUflixAutoCompleteDB;
import com.diquest.db.IptvVodAutoCompleteDB;
import com.diquest.db.KidsAutoCompleteDB;
import com.diquest.db.KidsClassAutoCompleteDB;
import com.diquest.db.KidsClassDB;
import com.diquest.db.KidsClassExpDB;
import com.diquest.db.KidsDB;
import com.diquest.db.KidsClassSpcDB;
import com.diquest.db.QueryLogFullDB;
import com.diquest.db.ShowAutoCompleteDB;
import com.diquest.db.ShowDB;
import com.diquest.db.ThemeDB;
import com.diquest.db.UflixAutoCompleteDB;
import com.diquest.db.UflixPackageDB;
import com.diquest.db.UflixSeriesDB;
import com.diquest.db.VideoAutoCompleteDB;
import com.diquest.db.VodDB;
import com.diquest.db.VrAutoCompleteDB;
import com.diquest.db.VrContentsDB;
import com.diquest.db.VrDB;
import com.diquest.db.WatchRecordDB;
import com.diquest.db.WatchRecordDBTotal;
//import com.diquest.db.kidsClassExpDB;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.ArAutoCompleteVO;
import com.diquest.db.entity.ChannelVO;
import com.diquest.db.entity.CjContentsVO;
import com.diquest.db.entity.EventVO;
import com.diquest.db.entity.Golf2AutoCompleteVO;
import com.diquest.db.entity.Golf2VO;
import com.diquest.db.entity.HighLightVO;
import com.diquest.db.entity.HitUccVO;
import com.diquest.db.entity.I30ChannelVO;
import com.diquest.db.entity.I30VO;
import com.diquest.db.entity.I30AutoCompleteVO;
import com.diquest.db.entity.IptvUflixAutoCompleteVO;
import com.diquest.db.entity.IptvVodAutoCompleteVO;
import com.diquest.db.entity.KidsAutoCompleteVO;
import com.diquest.db.entity.KidsClassAutoCompleteVO;
import com.diquest.db.entity.KidsClassVO;
import com.diquest.db.entity.KidsEpxVO;
import com.diquest.db.entity.KidsSpcVO;
import com.diquest.db.entity.KidsVO;
import com.diquest.db.entity.ShowAutoCompleteVO;
import com.diquest.db.entity.ShowVO;
import com.diquest.db.entity.ThemeVO;
import com.diquest.db.entity.UflixAutoCompleteVO;
import com.diquest.db.entity.UflixPackageVO;
import com.diquest.db.entity.UflixSeriesVO;
import com.diquest.db.entity.VideoAutoCompleteVO;
import com.diquest.db.entity.VodVO;
import com.diquest.db.entity.VrAutoCompleteVO;
import com.diquest.db.entity.VrContentsVO;
import com.diquest.db.entity.VrVO;
import com.diquest.db.entity.WatchRecordVO;
import com.diquest.db.entity.WatchRecordVOTotal;
import com.diquest.util.FileUtil;
import com.diquest.util.SqlUtil;
import com.diquest.util.StringUtil;

public class FileToDbManager {

	public String type;
	public String watch_date;
	private final List<String> srcFile;
	private Connection dstConn;
	private DBConnector dstConnector;

	private Connection koficConn;
	private DBConnector koficConnector;

	private Connection marinerConn;
	private DBConnector marinerConnector;
	private DBConnector dbConnector;

	private UflixPackageDB uflixPackage;
	private UflixSeriesDB uflixSeries;
	private VodDB vod;
	private ChannelDB channel;
	private HitUccDB hitUcc;
	private HighLightDB highLight;
	private EventDB event;

	private VideoAutoCompleteDB vodAutoComplete;
	private UflixAutoCompleteDB uflixTvgAutoComplete;
	private UflixAutoCompleteDB uflixMobileAutoComplete;
	private QueryLogFullDB QueryLogFull;
	private ThemeDB theme;
	private IptvVodAutoCompleteDB iptvVodAutoComplete;
	private IptvUflixAutoCompleteDB iptvUflixAutoComplete;
	private WatchRecordDB watchrecord;
	private WatchRecordDBTotal watchrecordtotal;
	private I30DB i30;
	private I30AutoCompleteDB i30AutoComplete;
	private I30ChannelDB i30channel;

	//Choihu 20180626 공연추가
	private ShowDB show;
	private ShowAutoCompleteDB showAutoComplete;
	
	//ChoiHu 20181130 VR추가
	private VrDB vr;
	private VrContentsDB vrContents;
	private VrAutoCompleteDB vrAutoComplete;
	
	//Choihu 공연2차 cj추가
	private CjContentsDB cjContents;
	
	//Choihu 20181204 AR 추가
	private ArAutoCompleteDB arAutoComplete;
	//Choihu 20190115 골프2추가
	private Golf2DB golf2;
	private Golf2AutoCompleteDB golf2AutoComplete;
	//Choihu 20190115 아이들나라
	private KidsDB kids;
	private KidsAutoCompleteDB kidsAutoComplete;
	//dhkim 20210917 아이들나라클래스
	private KidsClassDB kidsClass;
	private KidsClassSpcDB kidsClassSpc;
	private KidsClassExpDB kidsClassExp;
	private KidsClassAutoCompleteDB kidsClassAutoComplete;
	
	
	// 중복허용안함
	private Set<String> vodKeySet;
	//Choihu 20180628 공연추가
	private Set<String> showKeySet;
	//Choihu 20181203 공연추가
	private Set<String> vrKeySet;
	//Choihu 20190115 골프2추가
	private Set<String> golf2KeySet;
	private Set<String> vrContKeySet;
	
	//Choihu 공연2차 cj추가
	private Set<String> cjContKeySet;
	
	//Choihu 20200122 아이들나라추가
	private Set<String> kidsKeySet;
	//Choihu 20200122 아이들나라 자동완성 추가(test)
	private Set<String> kidsAutoKeySet;
	
	//dhkim 20210917 아이들나라클래스추가
	private Set<String> kidsClassKeySet;
	private Set<String> kidsClassSpcKeySet;
	private Set<String> kidsClassExpKeySet;
	private Set<String> kidsClassAutoKeySet;
	
	private Set<String> uflixKeySet;
	private Set<String> watchKeySet;
	private Set<String> i30KeySet;

	// 자동완성키워드 필드추가 (어느DB필드 데이터인지, 몇번검색요청이 있었는지)
	private final Map<String, List<String>> vodAutoCompleteKeywordMap;
	//private final Map<String, List<String>> showAutoCompleteKeywordMap;
	private final Map<String, List<String>> uflixTvgAutoCompleteKeywordMap;
	private final Map<String, List<String>> uflixMobileAutoCompleteKeywordMap;
	private final Map<String, List<String>> iptvVodAutoCompleteKeywordMap;
	private final Map<String, List<String>> iptvUflixAutoCompleteKeywordMap;
	private final Map<String, List<String>> i30AutoCompleteKeywordMap;
	private final Map<String, List<String>> iptv30UflixAutoCompleteKeywordMap;
	
	
	//Choihu 20180628 공연추가
	private ArrayList<HashMap<String,String>> showPersonGroupData;
	private ArrayList<HashMap<String,String>> showLinkData;
	private ArrayList<ShowAutoCompleteVO> showAutoCompleteKeywordVO;
	//Choihu 20181203 VR추가
	private ArrayList<VrAutoCompleteVO> vrAutoCompleteKeywordVO;
	//Choihu 20181204 AR 추가
	private ArrayList<ArAutoCompleteVO> arAutoCompleteKeywordVO;
	//Choihu 20190114 golf2추가
	private ArrayList<Golf2AutoCompleteVO> golf2AutoCompleteKeywordVO;
	//Choihu 20200122 아이들나라추가
	private ArrayList<KidsAutoCompleteVO> kidsAutoCompleteKeywordVO;
	//dhkim 20210917 아이들나라클래스추가
	private ArrayList<KidsClassAutoCompleteVO> kidsClassAutoCompleteKeywordVO;
	
	
	private Map<String, Integer> searchCntMap;

	MovieDataMergerManager mergerManager; // 영화진흥원

	public double totalInsertTime;

	public FileToDbManager(String type) {
		srcFile = new ArrayList<String>();
		this.type = type;
		if (type.equalsIgnoreCase(FILE_TO_DB_TYPE.ALL)) {
			vodKeySet = new HashSet<String>();
			uflixKeySet = new HashSet<String>();
			watchKeySet = new HashSet<String>();
			i30KeySet = new HashSet<String>();
			//Choihu 20180626 공연추가
			showKeySet = new HashSet<String>();
			vrKeySet = new HashSet<String>();
			vrContKeySet = new HashSet<String>(); 
			//Choihu 20190115 골프2 추가
			golf2KeySet = new HashSet<String>();
			//Choihu 공연2차 cj추가
			cjContKeySet = new HashSet<String>();
			//Choihu 20200122 아이들나라 추가
			kidsKeySet = new HashSet<String>();
			kidsAutoKeySet = new HashSet<String>();
			//dhkim 20200122 아이들나라 클래스 추가
			kidsClassKeySet = new HashSet<String>();
			kidsClassAutoKeySet = new HashSet<String>();
			
			
		} else if (type.equalsIgnoreCase(FILE_TO_DB_TYPE.VIDEO)) {
			vodKeySet = new HashSet<String>();
			showKeySet = new HashSet<String>();
			vrContKeySet = new HashSet<String>();
			//Choihu 20200122 아이들나라 추가
			kidsKeySet = new HashSet<String>();
			kidsAutoKeySet = new HashSet<String>();
		} else if (type.equalsIgnoreCase(FILE_TO_DB_TYPE.VIDEO_AUTO_KEYWORD)) {
		} else if (type.equalsIgnoreCase(FILE_TO_DB_TYPE.UFLIX)) {
			uflixKeySet = new HashSet<String>();
		} else if (type.equalsIgnoreCase(FILE_TO_DB_TYPE.UFLIX_AUTO_KEYWORD)) {
		}
		vodAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		//showAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		uflixTvgAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		uflixMobileAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		iptvVodAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		iptvUflixAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		i30AutoCompleteKeywordMap = new HashMap<String, List<String>>();
		iptv30UflixAutoCompleteKeywordMap = new HashMap<String, List<String>>();
		//Choihu 20180626 공연추가
		showAutoCompleteKeywordVO = new ArrayList<ShowAutoCompleteVO>();
		//Choihu 20181203 VR추가
		vrAutoCompleteKeywordVO = new ArrayList<VrAutoCompleteVO>();
		//Choihu 20181204 AR 추가
		arAutoCompleteKeywordVO = new ArrayList<ArAutoCompleteVO>();
		//Choihu 20190114 Golf2 추가
		golf2AutoCompleteKeywordVO = new ArrayList<Golf2AutoCompleteVO>();
		//Choihu 20200122 아이들나라 추가
		kidsAutoCompleteKeywordVO = new ArrayList<KidsAutoCompleteVO>();
		//dhkim 20210917 아이들나라 추가
		kidsClassAutoCompleteKeywordVO = new ArrayList<KidsClassAutoCompleteVO>();
		
		showPersonGroupData = new ArrayList<HashMap<String,String>>();
		showLinkData = new ArrayList<HashMap<String,String>>();
	}

	/**
	 * 상수 초기화(config.properties 값 가져오기), DB 열기, 검색어 카운트, 영화진흥원 데이터 메모리에 들기
	 */
	public void openDB() {
		System.out.println("OPEN DB [" + DB_INFO.URL + "]");
		String url = DB_INFO.URL;
		String user = DB_INFO.USER;
		String password = DB_INFO.PASSWORD;
		String driver = DB_INFO.DRIVER;
		dstConnector = new DBConnector(url, user, password, driver);
		dstConn = dstConnector.getConnection();

		if (DB_INFO.USE_SUB_DB) {
			System.out.println("OPEN DB [" + DB_INFO.KOFIC_URL + "]");
			String koficUrl = DB_INFO.KOFIC_URL;
			String koficUser = DB_INFO.KOFIC_USER;
			String koficPassword = DB_INFO.KOFIC_PASSWORD;
			String koficDriver = DB_INFO.KOFIC_DRIVER;
			koficConnector = new DBConnector(koficUrl, koficUser, koficPassword, koficDriver);
			koficConn = koficConnector.getConnection();
		}

		String marinerUrl = DB_INFO.MARINER_DB_URL;
		String marinerUser = DB_INFO.MARINER_DB_USER;
		String marinerPassword = DB_INFO.MARINER_DB_PASSWORD;
		String marinerDriver = DB_INFO.MARINER_DB_DRIVER;
		marinerConnector = new DBConnector(marinerUrl, marinerUser, marinerPassword, marinerDriver);
		marinerConn = marinerConnector.getConnection();

		String dbUrl = DB_INFO.DB_URL;
		String dbUser = DB_INFO.DB_USER;
		String dbPassword = DB_INFO.DB_PASSWORD;
		String dbDriver = DB_INFO.DB_DRIVER;
		dbConnector = new DBConnector(dbUrl, dbUser, dbPassword, dbDriver);
		marinerConn = dbConnector.getConnection();
		
/*		this.uflixPackage = new UflixPackageDB(DB_NAME.UFLIX_PACKAGE, true);
		this.uflixSeries = new UflixSeriesDB(DB_NAME.UFLIX_SERIES, true);*/
		this.vod = new VodDB(DB_NAME.VOD, true);
		this.channel = new ChannelDB(DB_NAME.CHANNEL, true);
		this.hitUcc = new HitUccDB(DB_NAME.HIT_UCC, true);
		this.highLight = new HighLightDB(DB_NAME.HIGHLIGHT, true);
		this.event = new EventDB(DB_NAME.EVENT, true);
		this.vodAutoComplete = new VideoAutoCompleteDB(DB_NAME.VIDEO_AUTO_COMPLETE, true);
		this.uflixTvgAutoComplete = new UflixAutoCompleteDB(DB_NAME.UFLIX_TVG_AUTO_COMPLETE, true);
		this.uflixMobileAutoComplete = new UflixAutoCompleteDB(DB_NAME.UFLIX_MOBILE_AUTO_COMPLETE, true);
		this.theme = new ThemeDB(DB_NAME.THEME, true);
//		this.iptvVodAutoComplete = new IptvVodAutoCompleteDB(DB_NAME.IPTV_VOD_AUTO_COMPLETE, true);
//		this.iptvUflixAutoComplete = new IptvUflixAutoCompleteDB(DB_NAME.IPTV_UFLIX_AUTO_COMPLETE, true);
		this.watchrecord = new WatchRecordDB(DB_NAME.WATCH_RECORD, true);
		this.watchrecordtotal = new WatchRecordDBTotal(DB_NAME.WATCH_RECORD, true);
		this.i30 = new I30DB(DB_NAME.I30, true);
		this.i30AutoComplete = new I30AutoCompleteDB(DB_NAME.I30_AUTO_COMPLETE, true);
		this.i30channel = new I30ChannelDB(DB_NAME.I30_CHANNEL, true);

		//Choihu 20180626 공연테이블추가
		this.show = new ShowDB(DB_NAME.SHOW, true);
		this.showAutoComplete = new ShowAutoCompleteDB(DB_NAME.SHOW_AUTO_COMPLETE, true);
		
		//Choihu 20181130 Vr추가
		this.vr = new VrDB(DB_NAME.VR, true);
		this.vrContents = new VrContentsDB(DB_NAME.VR_CONTENTS, true);
		this.vrAutoComplete = new VrAutoCompleteDB(DB_NAME.VR_AUTO_COMPLETE, true);
		//Choihu 20181204 AR추가
		this.arAutoComplete = new ArAutoCompleteDB(DB_NAME.AR_AUTO_COMPLETE, true);
		//Choihu 20190115 골프2추가
		this.golf2 = new Golf2DB(DB_NAME.GOLF2, true);
		this.golf2AutoComplete = new Golf2AutoCompleteDB(DB_NAME.GOLF2_AUTO_COMPLETE, true);
		
		//Choihu 20200212 아이들나라추가
		this.kids = new KidsDB(DB_NAME.KIDS,true);
		this.kidsAutoComplete = new KidsAutoCompleteDB(DB_NAME.KIDS_AUTO_COMPLETE, true);
		
		//dhkim 20210917 아이들클래스추가
		this.kidsClass = new KidsClassDB(DB_NAME.KIDSCLASS,true);
		this.kidsClassSpc = new KidsClassSpcDB(DB_NAME.KIDSCLASSSPC,true);
		this.kidsClassExp = new KidsClassExpDB(DB_NAME.KIDSCLASSEXP,true);
		this.kidsClassAutoComplete = new KidsClassAutoCompleteDB(DB_NAME.KIDSCLASS_AUTO_COMPLETE, true);
		
		//Choihu 공연2차cj추가
		this.cjContents = new CjContentsDB(DB_NAME.CJ_CONTENTS, true);
		
		this.mergerManager = (DB_INFO.USE_SUB_DB) ? new MovieDataMergerManager(koficConn) : new MovieDataMergerManager(dstConn);

		// 검색어에 대한 Count 값 1달치를 작업전에 메모리에 들고있는다.
		QueryLogFull = new QueryLogFullDB(DB_INFO.MARINER_QUERY_TABLE, true);
		searchCntMap = QueryLogFull.selectKeywords(marinerConn);

		//show데이터가져오기
		getShowData();
		
		try {
			getArData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		openInsertPstmt();
		openUpdatePstmt();
		openDeletePstmt();
		openSelectPstmt();
	}

	private void getArData() {
		String[] arDataTable = DB2DB.AR_MYSQL_TABLE;
		if("AR_CONTENTS".equals(arDataTable[0])) {
			arAutoComplete.selectArData(dstConn, arDataTable[0], arAutoCompleteKeywordVO);		}
	}

	private void getShowData() {
		
		String[] showDataTable = DB2DB.MYSQL_TABLE;
		for (int i = 0; i < showDataTable.length; i++) {
			if("SHOW_PERSON_GROUP_LINK_INFO".equals(showDataTable[i])) {
				showAutoComplete.selectShowData(dstConn, showDataTable[i], showLinkData, null, null);	
			}else if("SHOW_PERSON_INFO".equals(showDataTable[i])) {
				showAutoComplete.selectShowData(dstConn, showDataTable[i], null, showPersonGroupData ,showAutoCompleteKeywordVO);	
			}else if("SHOW_GROUP_INFO".equals(showDataTable[i])) {
				showAutoComplete.selectShowData(dstConn, showDataTable[i], null, showPersonGroupData ,showAutoCompleteKeywordVO);	
			}
		}
		
	}

	public void initAllTable() {
		System.out.println("TRUNCATE ALL TABLE");
/*		DBConnector.truncateTable(dstConn, DB_NAME.UFLIX_PACKAGE);//Choihu 20180806 Video메타데이터 통합 주석처리 
		DBConnector.truncateTable(dstConn, DB_NAME.UFLIX_SERIES);*/
		DBConnector.truncateTable(dstConn, DB_NAME.VOD);
		DBConnector.truncateTable(dstConn, DB_NAME.CHANNEL);
		DBConnector.truncateTable(dstConn, DB_NAME.HIT_UCC);
		DBConnector.truncateTable(dstConn, DB_NAME.HIGHLIGHT);
		DBConnector.truncateTable(dstConn, DB_NAME.EVENT);
		DBConnector.truncateTable(dstConn, DB_NAME.THEME);
//		DBConnector.truncateTable(dstConn, DB_NAME.IPTV_VOD_AUTO_COMPLETE);
//		DBConnector.truncateTable(dstConn, DB_NAME.IPTV_UFLIX_AUTO_COMPLETE);
		DBConnector.truncateTable(dstConn, DB_NAME.WATCH_RECORD_SECTION);
		DBConnector.truncateTable(dstConn, DB_NAME.I30);
		DBConnector.truncateTable(dstConn, DB_NAME.I30_AUTO_COMPLETE);
		DBConnector.truncateTable(dstConn, DB_NAME.I30_CHANNEL);
		//Choihu 20180626 공연
		DBConnector.truncateTable(dstConn, DB_NAME.SHOW);
		DBConnector.truncateTable(dstConn, DB_NAME.SHOW_AUTO_COMPLETE);
		//Choihu 20181130 VR
		DBConnector.truncateTable(dstConn, DB_NAME.VR);
		DBConnector.truncateTable(dstConn, DB_NAME.VR_CONTENTS);
		DBConnector.truncateTable(dstConn, DB_NAME.VR_AUTO_COMPLETE);

		//Choihu 20190115 Golf2
		DBConnector.truncateTable(dstConn, DB_NAME.GOLF2);
		DBConnector.truncateTable(dstConn, DB_NAME.GOLF2_AUTO_COMPLETE);
		
		//Choihu 공연2차 cj추가
		DBConnector.truncateTable(dstConn, DB_NAME.CJ_CONTENTS);
		
		DBConnector.truncateTable(dstConn, DB_NAME.KIDS);
		DBConnector.truncateTable(dstConn, DB_NAME.KIDS_AUTO_COMPLETE);
		
		DBConnector.truncateTable(dstConn, DB_NAME.KIDSCLASS);
		DBConnector.truncateTable(dstConn, DB_NAME.KIDSCLASSSPC);
		DBConnector.truncateTable(dstConn, DB_NAME.KIDSCLASSEXP);
		DBConnector.truncateTable(dstConn, DB_NAME.KIDSCLASS_AUTO_COMPLETE);
	}

	public void initTable(String tableName) {
		System.out.println("TRUNCATE TABLE [" + tableName + "]");
		DBConnector.truncateTable(dstConn, tableName);
	}
	
	public void alterTable(String tableName){
		System.out.println("ALTER TABLE [" + tableName + "]");
		DBConnector.alterTable(dstConn, tableName, watch_date);
	}

	public void closeDB() {
		dstConnector.closeConnection(dstConn);
		marinerConnector.closeConnection(marinerConn);
		if (DB_INFO.USE_SUB_DB)
			koficConnector.closeConnection(koficConn);
		System.out.println("CLOSED DB [" + DB_INFO.URL + "]");
		closeInsertPstmt();
		closeUpdatePstmt();
		closeDeletePstmt();
	}

	private void openSelectPstmt() {
		watchrecordtotal.createSelectPstmt2(dstConn);
	}

	public void openInsertPstmt() {
		vod.createInsertPstmt(dstConn);
		channel.createInsertPstmt(dstConn);
		hitUcc.createInsertPstmt(dstConn);
		highLight.createInsertPstmt(dstConn);
		event.createInsertPstmt(dstConn);
/*		uflixPackage.createInsertPstmt(dstConn); //Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.createInsertPstmt(dstConn);*/
		vodAutoComplete.createInsertPstmt(dstConn);
		uflixTvgAutoComplete.createInsertPstmt(dstConn);
		uflixMobileAutoComplete.createInsertPstmt(dstConn);
		theme.createInsertPstmt(dstConn);
//		iptvUflixAutoComplete.createInsertPstmt(dstConn);
//		iptvVodAutoComplete.createInsertPstmt(dstConn);
		watchrecord.createInsertPstmt(dstConn);
		watchrecordtotal.createInsertPstmt(dstConn);
		i30.createInsertPstmt(dstConn);
		i30AutoComplete.createInsertPstmt(dstConn);
		i30channel.createInsertPstmt(dstConn);
		//Choihu 20180626 공연
		show.createInsertPstmt(dstConn);
		showAutoComplete.createInsertPstmt(dstConn);
		//Choihu 20181130 VR
		vr.createInsertPstmt(dstConn);
		vrContents.createInsertPstmt(dstConn);
		vrAutoComplete.createInsertPstmt(dstConn);
		arAutoComplete.createInsertPstmt(dstConn);
		
		//Choihu 20190115 골프2
		golf2.createInsertPstmt(dstConn);
		golf2AutoComplete.createInsertPstmt(dstConn);
		
		//Choihu 공연2차 cj추가
		cjContents.createInsertPstmt(dstConn);
		
		//Choihu 아이들나라
		kids.createInsertPstmt(dstConn);
		kidsAutoComplete.createInsertPstmt(dstConn);
		
		//dhkim 아이들나라클래스
		kidsClass.createInsertPstmt(dstConn);
		kidsClassSpc.createInsertPstmt(dstConn);
		kidsClassExp.createInsertPstmt(dstConn);
		kidsClassAutoComplete.createInsertPstmt(dstConn);
		
	}

	public void closeInsertPstmt() {
		vod.closeInsertPstmt();
		channel.closeInsertPstmt();
		hitUcc.closeInsertPstmt();
		highLight.closeInsertPstmt();
		event.closeInsertPstmt();
/*		uflixPackage.closeInsertPstmt();//Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.closeInsertPstmt();*/
		vodAutoComplete.closeInsertPstmt();
		uflixTvgAutoComplete.closeInsertPstmt();
		uflixMobileAutoComplete.closeInsertPstmt();
		theme.closeInsertPstmt();
//		iptvUflixAutoComplete.closeInsertPstmt();
//		iptvVodAutoComplete.closeInsertPstmt();
		watchrecord.closeInsertPstmt();
		watchrecordtotal.closeInsertPstmt();
		i30.closeInsertPstmt();
		i30AutoComplete.closeInsertPstmt();
		i30channel.closeInsertPstmt();
		//Choihu 20180626 공연
		show.closeInsertPstmt();
		showAutoComplete.closeInsertPstmt();
		//Choihu 20181130 VR
		vr.closeInsertPstmt();
		vrContents.closeInsertPstmt();
		
		//Choihu 20190115 골프2
		golf2.closeInsertPstmt();
		golf2AutoComplete.closeInsertPstmt();
		
		//Choihu 공연2차 cj추가
		cjContents.closeInsertPstmt();
		
		//Choihu 아이들나라 추가
		kids.closeInsertPstmt();
		kidsAutoComplete.closeInsertPstmt();
		
		//dhkim 아이들나라클래스 추가
		kidsClass.closeInsertPstmt();
		kidsClassSpc.closeInsertPstmt();
		kidsClassExp.closeInsertPstmt();
		kidsClassAutoComplete.closeInsertPstmt();
	}

	public void openUpdatePstmt() {
		vod.createUpdatePstmt(dstConn);
		channel.createUpdatePstmt(dstConn);
		hitUcc.createUpdatePstmt(dstConn);
		highLight.createUpdatePstmt(dstConn);
		event.createUpdatePstmt(dstConn);
/*		uflixPackage.createUpdatePstmt(dstConn);//Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.createUpdatePstmt(dstConn);*/
		theme.createUpdatePstmt(dstConn);
//		iptvUflixAutoComplete.createUpdatePstmt(dstConn);
//		iptvVodAutoComplete.createUpdatePstmt(dstConn);
		watchrecord.createUpdatePstmt(dstConn);
		watchrecordtotal.createUpdatePstmt(dstConn);
		i30.createUpdatePstmt(dstConn);
		i30AutoComplete.createUpdatePstmt(dstConn);
		i30channel.createUpdatePstmt(dstConn);
		//Choihu 20180626 공연
		show.createUpdatePstmt(dstConn);
		showAutoComplete.createUpdatePstmt(dstConn);
		//Choihu 20181130 VR
		vr.createUpdatePstmt(dstConn);
		//Choihu 20190115 골프2

		golf2.createUpdatePstmt(dstConn);
		golf2AutoComplete.createUpdatePstmt(dstConn);
		
		//Choihu 아이들나라 
		kids.createUpdatePstmt(dstConn);
		kidsAutoComplete.createUpdatePstmt(dstConn);
		
		//dhkim 아이들나라클래스
		kidsClass.createUpdatePstmt(dstConn);
		kidsClassSpc.createUpdatePstmt(dstConn);
		kidsClassExp.createUpdatePstmt(dstConn);
		kidsClassAutoComplete.createUpdatePstmt(dstConn);
	}

	public void closeUpdatePstmt() {
		vod.closeUpdatePstmt();
		channel.closeUpdatePstmt();
		hitUcc.closeUpdatePstmt();
		highLight.closeUpdatePstmt();
		event.closeUpdatePstmt();
/*		uflixPackage.closeUpdatePstmt();//Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.closeUpdatePstmt();*/
		theme.closeUpdatePstmt();
//		iptvUflixAutoComplete.closeUpdatePstmt();
//		iptvVodAutoComplete.closeUpdatePstmt();
		watchrecord.closeUpdatePstmt();
		watchrecordtotal.closeUpdatePstmt();
		i30.closeUpdatePstmt();
		i30AutoComplete.closeUpdatePstmt();
		i30channel.closeUpdatePstmt();
		//Choihu 20180626 공연
		show.closeUpdatePstmt();
		showAutoComplete.closeUpdatePstmt();
		//Choihu 20181130 VR
		vr.closeUpdatePstmt();
		//Choihu 20190115 골프2
		golf2.closeUpdatePstmt();
		golf2AutoComplete.closeUpdatePstmt();
		
		//Choihu 아이들나라
		kids.closeUpdatePstmt();
		kidsAutoComplete.closeUpdatePstmt();
		
		//dhkim 아이들나라클래스
		kidsClass.closeUpdatePstmt();
		kidsClassSpc.closeUpdatePstmt();
		kidsClassExp.closeUpdatePstmt();
		kidsClassAutoComplete.closeUpdatePstmt();
	}

	public void openDeletePstmt() {
		vod.createDeletePstmt(dstConn);
		channel.createDeletePstmt(dstConn);
		hitUcc.createDeletePstmt(dstConn);
		highLight.createDeletePstmt(dstConn);
		event.createDeletePstmt(dstConn);
/*		uflixPackage.createDeletePstmt(dstConn);//Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.createDeletePstmt(dstConn);*/
		theme.createDeletePstmt(dstConn);
//		iptvUflixAutoComplete.createDeletePstmt(dstConn);
//		iptvVodAutoComplete.createDeletePstmt(dstConn);
		watchrecord.createDeletePstmt(dstConn);
		watchrecordtotal.createDeletePstmt(dstConn);
		i30.createDeletePstmt(dstConn);
		i30AutoComplete.createDeletePstmt(dstConn);
		i30channel.createDeletePstmt(dstConn);
		//Choihu 20180626 공연
		show.createDeletePstmt(dstConn);
		showAutoComplete.createDeletePstmt(dstConn);
		//Choihu 20181130 VR
		vr.createDeletePstmt(dstConn);
		//Choihu 20190115 골프2
		golf2.createDeletePstmt(dstConn);
		golf2AutoComplete.createDeletePstmt(dstConn);
		//Choihu 아이들나라
		kids.createDeletePstmt(dstConn);
		kidsAutoComplete.createDeletePstmt(dstConn);
		//dhkim 아이들나라클래스
		kidsClass.createDeletePstmt(dstConn);
		kidsClassSpc.createDeletePstmt(dstConn);
		kidsClassExp.createDeletePstmt(dstConn);
		kidsClassAutoComplete.createDeletePstmt(dstConn);
	}

	public void closeDeletePstmt() {
		vod.closeDeletePstmt();
		channel.closeDeletePstmt();
		hitUcc.closeDeletePstmt();
		highLight.closeDeletePstmt();
		event.closeDeletePstmt();
/*		uflixPackage.closeDeletePstmt();//Choihu 20180806 Video메타데이터 통합 주석처리 
		uflixSeries.closeDeletePstmt();*/
		theme.closeDeletePstmt();
//		iptvUflixAutoComplete.closeDeletePstmt();
//		iptvVodAutoComplete.closeDeletePstmt();
		watchrecord.closeDeletePstmt();
		watchrecordtotal.closeDeletePstmt();
		i30.closeDeletePstmt();
		i30AutoComplete.closeDeletePstmt();
		i30channel.closeDeletePstmt();
		//Choihu 20180626 공연
		show.closeDeletePstmt();
		showAutoComplete.closeDeletePstmt();
		//Choihu 20181130 VR
		vr.closeDeletePstmt();
		//vrContents.closeDeletePstmt();
		golf2.closeDeletePstmt();
		golf2AutoComplete.closeDeletePstmt();
		//Choihu 아이들나라
		kids.closeDeletePstmt();
		kidsAutoComplete.closeDeletePstmt();
		//dhkim 아이들나라클래스
		kidsClass.closeDeletePstmt();
		kidsClassSpc.closeDeletePstmt();
		kidsClassExp.closeDeletePstmt();
		kidsClassAutoComplete.closeDeletePstmt();
		
	}

	public String getFileType(String srcFile) {
		String type = "";

		File f = new File(srcFile);
		String fileName = f.getAbsoluteFile().getName();
		fileName = fileName.substring(0, fileName.indexOf("."));

		BufferedReader br = null;
		br = (/*Choihu 20180626 공연*/ 
				FILE_NAME.VOD_APPEND.equals(fileName) || FILE_NAME.CHANNEL_APPEND.equals(fileName)
				|| FILE_NAME.I30_CHANNEL.equals(fileName) || FILE_NAME.I30.equals(fileName) || FILE_NAME.I30_APPEND.equals(fileName)
				|| fileName.contains(FILE_NAME.VR_CONTENTS) || fileName.contains(FILE_NAME.KIDS_SPC)|| fileName.contains(FILE_NAME.KIDS_EPX)
				) ? FileUtil.getBufferedReader(srcFile, "utf-8")	: FileUtil.getBufferedReader(srcFile, "euc-kr");
		// br = (FILE_NAME.THEME.equals(fileName) ||
		// FILE_NAME.VOD_APPEND.equals(fileName) ||
		// FILE_NAME.CHANNEL_APPEND.equals(fileName))
		// ? FileUtil.getBufferedReader(srcFile,"utf-8")
		// :FileUtil.getBufferedReader(srcFile, "euc-kr");

		String line = "";
		int splitSize = 0;
		int count = 0;
		StringBuffer sb = new StringBuffer();
		try {
			while ((line = br.readLine()) != null) {
				//System.out.println("line : "+line);
				line = normalizeFileLine(line);
				//System.out.println("라인 : "+line);
				// 마지막을 의미하는 문자가 있을경우
				if (line.contains(DELIMS.ENDLINE)) {
					count++;
					sb.append(line);
					line = sb.toString();

					// 마지막을 의미하는 문자 기준으로 앞부분(a) 만 처리후 뒷부분(b) 은 다음 문장에서 이용
					int idx = line.indexOf(DELIMS.ENDLINE);

					String a = line.substring(0, idx);
					String b = line.substring(idx + DELIMS.ENDLINE.length(), line.length());
					List<String> result = customSplit(a, DELIMS.WORD_DELIM);
					
					splitSize = result.size();
					//System.out.println("splitSize : "+splitSize);
					/*System.out.println("SPLIT_SIZE.VOD || SPLIT_SIZE.VOD -3 : "+SPLIT_SIZE.VOD);
					System.out.println("스플릿 싸이즈 : "+splitSize);*/
					if (FILE_NAME.VOD.equals(fileName) /*&& splitSize <= SPLIT_SIZE.VOD*/ ) {//Choihu 20181204 VOD SIZE 증가 
						//키 메모리 초기화
						vodKeySet = new HashSet<String>();
						uflixKeySet = new HashSet<String>();
						watchKeySet = new HashSet<String>();
						i30KeySet = new HashSet<String>();
					
						showKeySet = new HashSet<String>();
						vrKeySet = new HashSet<String>();
						vrContKeySet = new HashSet<String>();
						
						golf2KeySet = new HashSet<String>();
						
						cjContKeySet = new HashSet<String>();
						//Choihu 20200122 아이들나라 추가
						kidsKeySet = new HashSet<String>();
						kidsAutoKeySet = new HashSet<String>();
						//dhkim 202010923 아이들나라 추가						
//						kidsClassKeySet = new HashSet<String>();
//						kidsClassAutoKeySet = new HashSet<String>();
						type = FILETYPE.VOD_ALL;
					} else if (FILE_NAME.CHANNEL.equals(fileName) && splitSize == SPLIT_SIZE.CHANNEL) {
						type = FILETYPE.CHANNEL_ALL;
					} else if (FILE_NAME.HIT_UCC.equals(fileName) && (splitSize == SPLIT_SIZE.HIT_UCC || splitSize == SPLIT_SIZE.HIT_UCC-3)) {//Choihu 공연2차고도화
						type = FILETYPE.HIT_UCC_ALL;
					} else if (FILE_NAME.HIGHLIGHT.equals(
							fileName) /*
										 * && (splitSize == SPLIT_SIZE.HIGHLIGHT || splitSize == SPLIT_SIZE.HIGHLIGHT-3)
										 */) {//Choihu 공연2차고도화
						type = FILETYPE.HIGHLIGHT_ALL;
					} else if (FILE_NAME.EVENT.equals(fileName) && splitSize == SPLIT_SIZE.EVENT) {
						type = FILETYPE.EVENT_ALL;
					} /*//Choihu 20180806 Video메타데이터 통합 주석처리 
					else if (FILE_NAME.UFLIX_PACKAGE.equals(fileName) && (splitSize == SPLIT_SIZE.UFLIX_PACKAGE || splitSize == SPLIT_SIZE.UFLIX_PACKAGE+1)) {
						type = FILETYPE.UFLIX_PACKAGE_ALL;
					} else if (FILE_NAME.UFLIX_SERIES.equals(fileName) && splitSize == SPLIT_SIZE.UFLIX_SERIES) {
						type = FILETYPE.UFLIX_SERIES_ALL;
					} */else if (FILE_NAME.THEME.equals(fileName) && splitSize == SPLIT_SIZE.THEME) {
						type = FILETYPE.THEME_ALL;
					} else if (FILE_NAME.VOD_APPEND.equals(fileName) /*&& (splitSize <= SPLIT_SIZE.VOD_APPEND )*/) {//Choihu VOD SIZE 증가 2018.06.12
						vodKeySet = new HashSet<String>();
						showKeySet = new HashSet<String>();
						vrKeySet = new HashSet<String>();
						vrContKeySet = new HashSet<String>();
						golf2KeySet = new HashSet<String>();
						//Choihu 20200122 아이들나라 추가
						kidsKeySet = new HashSet<String>();
						//kidsAutoKeySet = new HashSet<String>();
						kidsClassKeySet = new HashSet<String>();
						type = FILETYPE.VOD_APPEND;
					} else if (FILE_NAME.CHANNEL_APPEND.equals(fileName) && splitSize == SPLIT_SIZE.CHANNEL) {
						type = FILETYPE.CHANNEL_ALL;
					} else if (fileName.contains(FILE_NAME.WATCH_RECORD) && splitSize == SPLIT_SIZE.WATCH_RECORD) {
						type = FILETYPE.WATCH_RECORD;
						watch_date = fileName.substring(15);
					} else if (FILE_NAME.I30
							.equals(fileName) /* && (splitSize == SPLIT_SIZE.I30|| splitSize == SPLIT_SIZE.I30-1) */) {//Choihu 20200720 I30 SUPER_ID 방어로직
						type = FILETYPE.I30_ALL;
					} else if (FILE_NAME.I30_APPEND.equals(fileName) /*&& (splitSize == SPLIT_SIZE.I30_APPEND|| splitSize == SPLIT_SIZE.I30_APPEND-1)*/) {//Choihu 20200720 I30 SUPER_ID 방어로직
						type = FILETYPE.I30_APPEND;
					} else if (FILE_NAME.I30_CHANNEL.equals(fileName) && splitSize == SPLIT_SIZE.I30_CHANNEL) {
						type = FILETYPE.I30_CHANNEL;
					} else if (fileName.contains(FILE_NAME.VR_CONTENTS)/* && (splitSize == SPLIT_SIZE.VR_CONTENTS ||splitSize == SPLIT_SIZE.VR_CONTENTS -2 )*/) {
						vrContKeySet = new HashSet<String>();
						type = FILETYPE.VR_CONTENTS;
					} else if (fileName.contains(FILE_NAME.CJ_CONTENTS) /* && splitSize == SPLIT_SIZE.CJ_CONTENTS */) {
						cjContKeySet = new HashSet<String>();
						type = FILETYPE.CJ_CONTENTS;
					} 
					else if (fileName.equals(FILE_NAME.KIDS_SPC) /* && splitSize == SPLIT_SIZE.CJ_CONTENTS */) {
						kidsClassSpcKeySet = new HashSet<String>();
						type = FILETYPE.KIDS_SPC;
					} else if (fileName.equals(FILE_NAME.KIDS_EPX) /* && splitSize == SPLIT_SIZE.CJ_CONTENTS */) {
						kidsClassExpKeySet = new HashSet<String>();
						type = FILETYPE.KIDS_EXP;
					}

					// 처리가 끝난후 뒷부분 버퍼에 담음
					sb = new StringBuffer();
					sb.append(b);

					// 마지막을 의미하는 문자가 없을 경우
				} else {
					sb.append(line);
				}

				// 1줄만 읽어도 타입을 알기때문에 break;
				if (count == 1)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedReader(br);

		return type;
	}

	public int workAll(String type, String workFile) throws IOException {
		// 작업타입이 전체일경우만 TRUNCATE를 시킨다.
		if (FILETYPE.VOD_ALL.equals(type)) {

			initTable(DB_NAME.VIDEO_AUTO_COMPLETE);
			initTable(DB_NAME.VOD);
			//Choihu 20180626 공연
			initTable(DB_NAME.SHOW);
			initTable(DB_NAME.SHOW_AUTO_COMPLETE);
			
			//Choihu 20190115 골프2
			initTable(DB_NAME.GOLF2);
			initTable(DB_NAME.GOLF2_AUTO_COMPLETE);
			//Choihu 20200122 아이들나라 추가
			initTable(DB_NAME.KIDS);
			initTable(DB_NAME.KIDS_AUTO_COMPLETE);
			//dhkim 20210923 아이들나라클래스 추가
			initTable(DB_NAME.KIDSCLASS);
			initTable(DB_NAME.KIDSCLASS_AUTO_COMPLETE);
			
			//Choihu 20181130 VR
			initTable(DB_NAME.VR);
			Method.deleteData(DBConnector.createDeletePstmt(dstConn, DB_NAME.VR_AUTO_COMPLETE, new String[] {"TYPE"}),"V");
			
			//Choihu 20180803 TVG,MOBILE 자동완성 VIDEO_VOD에 붙이기
			initTable(DB_NAME.UFLIX_TVG_AUTO_COMPLETE);
			initTable(DB_NAME.UFLIX_MOBILE_AUTO_COMPLETE);
//			initTable(DB_NAME.IPTV_VOD_AUTO_COMPLETE);
		} else if (FILETYPE.CHANNEL_ALL.equals(type) || FILETYPE.CHANNEL_APPEND.equals(type)) {
			initTable(DB_NAME.CHANNEL);
		} else if (FILETYPE.HIT_UCC_ALL.equals(type)) {
			initTable(DB_NAME.HIT_UCC);
			Method.deleteData(DBConnector.createDeletePstmt(dstConn, DB_NAME.VR_AUTO_COMPLETE, new String[] {"TYPE"}),"U");
		} else if (FILETYPE.HIGHLIGHT_ALL.equals(type)) {
			initTable(DB_NAME.HIGHLIGHT);
			//Method.deleteData(DBConnector.createDeletePstmt(dstConn, DB_NAME.SHOW_AUTO_COMPLETE, new String[] {"TYPE"}),"U");
		} else if (FILETYPE.EVENT_ALL.equals(type)) {
			initTable(DB_NAME.EVENT);
		}/* else if (FILETYPE.UFLIX_PACKAGE_ALL.equals(type)) {//Choihu 20180806 Video메타데이터 통합 주석처리 
			initTable(DB_NAME.UFLIX_TVG_AUTO_COMPLETE);
			initTable(DB_NAME.UFLIX_MOBILE_AUTO_COMPLETE);
			initTable(DB_NAME.UFLIX_PACKAGE);
//			initTable(DB_NAME.IPTV_UFLIX_AUTO_COMPLETE);
		} else if (FILETYPE.UFLIX_SERIES_ALL.equals(type)) {
			initTable(DB_NAME.UFLIX_SERIES);
		}*/ else if (FILETYPE.THEME_ALL.equals(type)) {
			initTable(DB_NAME.THEME);
		} else if (FILETYPE.WATCH_RECORD.equals(type)) {
			alterTable(DB_NAME.WATCH_RECORD);
		}
		else if (FILETYPE.I30_ALL.equals(type)) {
			initTable(DB_NAME.I30);
			initTable(DB_NAME.I30_AUTO_COMPLETE);
		}
		else if(FILETYPE.I30_CHANNEL.equals(type)) {
			initTable(DB_NAME.I30_CHANNEL);
		}
		else if (FILETYPE.VR_CONTENTS.equals(type)) {
			initTable(DB_NAME.VR_CONTENTS);
			Method.deleteData(DBConnector.createDeletePstmt(dstConn, DB_NAME.VR_AUTO_COMPLETE, new String[] { "TYPE" }),"C");
		} else if (FILETYPE.CJ_CONTENTS.equals(type)) {
			initTable(DB_NAME.CJ_CONTENTS);
			//Method.deleteData(DBConnector.createDeletePstmt(dstConn, DB_NAME.SHOW_AUTO_COMPLETE, new String[] { "TYPE" }),"C");
		}
		else if (FILETYPE.KIDS_SPC.equals(type)) {
			initTable(DB_NAME.KIDSCLASSSPC);
		}else if (FILETYPE.KIDS_EXP.equals(type)) {
			initTable(DB_NAME.KIDSCLASSEXP);
		}

		int workTotal = 0;
		File f = new File(workFile);
		String fileName = f.getAbsoluteFile().getName();
		fileName = fileName.substring(0, fileName.indexOf("."));
		BufferedReader br = null;
		br = (FILE_NAME.VOD_APPEND.equals(fileName) || FILE_NAME.CHANNEL_APPEND.equals(fileName) 
				|| FILE_NAME.I30_CHANNEL.equals(fileName) || FILE_NAME.I30.equals(fileName) || FILE_NAME.I30_APPEND.equals(fileName)
				|| fileName.contains(FILE_NAME.VR_CONTENTS) || fileName.contains(FILE_NAME.KIDS_SPC)|| fileName.contains(FILE_NAME.KIDS_EPX)
				) ? FileUtil.getBufferedReader(workFile, "utf-8") : FileUtil.getBufferedReader(workFile, "euc-kr");

		String line = "";
		StringBuffer sb = new StringBuffer();
		ConflictManager.init();
		boolean checkFirst = false;
		StringBuffer sb2 = new StringBuffer();
		BufferedWriter bw = null;
		boolean exist = false;
		//int i = 0;
		// System.out.println("WORK [" + f.getAbsoluteFile() + "]...");
		while ((line = br.readLine()) != null) {
			line = normalizeFileLine(line);
			// 마지막을 의미하는 문자가 있을경우
			if (line.contains(DELIMS.ENDLINE)) {
				sb.append(line);
				line = sb.toString();

				// 마지막을 의미하는 문자 기준으로 앞부분(a) 만 처리후 뒷부분(b) 은 다음 문장에서 이용
				int idx = line.indexOf(DELIMS.ENDLINE);
				String a = line.substring(0, idx);
				if(!checkFirst) {
					if(a.length() != 0) {
						if(!StringUtil.isNumber(a.substring(0, 1)) && !StringUtil.isEnglish(a.substring(0, 1))) {
							a =a.substring(1, a.length());
						}
					}
					checkFirst = true; 
				}
				String b = line.substring(idx + DELIMS.ENDLINE.length(), line.length());
				List<String> result = customSplit(a, DELIMS.WORD_DELIM);
				/*if(i == 0) {
					System.out.println(result.size());
					i++;
				}*/
				if (FILETYPE.VOD_ALL.equals(type)) {
					int d = Constant.SPLIT_SIZE.VOD-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					
					if(("라이프".equals(result.get(30))&&"스포츠".equals(result.get(31))&&"골프".equals(result.get(32)))
							||("스포츠".equals(result.get(30))&&"골프".equals(result.get(31)))) {
						workTotal = insertGolf2(result, workTotal, true, true);
						insertVod(result, workTotal, true, true);
					}
					else if("KID".equals(result.get(87)) && result.get(87)!=null) {
						//Choihu 20200122 아이들나라 추가
						workTotal = insertKids(result, workTotal, true, true);
					}
					else if("KDC".equals(result.get(87)) && result.get(87)!=null) {
						//dhkim	 20210923 아이들나라클래스 추가
						exist = true;
						workTotal = insertKidsClass(result, workTotal, true, true);
					}
					else if(!"".equals(result.get(80)) && result.get(80)!=null) {
/*						System.out.println("인서트 VR");
						System.out.println("result.get(80) : "+result.get(80));*/
						workTotal = insertVr(result, workTotal, true, true);
						//20200602 VR M_TV section 데이터 비디오포털 제공작업
						if("V3S".equals(result.get(80))) {
							workTotal = insertVod(result, workTotal, true, true);
						}
					}else if(!"".equals(result.get(73)) && result.get(73)!=null) {
						//Choihu 20180626 비디오포털,공연 분기
						workTotal = insertShow(result, workTotal, true, true);
					}else {
						workTotal = insertVod(result, workTotal, true, true);
					}
				} 
				else if (FILETYPE.KIDS_SPC.equals(type)) {
					int d = Constant.SPLIT_SIZE.KIDS_SPC-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertkidsSpc(result, workTotal);
				} else if (FILETYPE.KIDS_EXP.equals(type)) {
					int d = Constant.SPLIT_SIZE.KIDS_EXP-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertkidsExp(result, workTotal);
				} 
				else if (FILETYPE.CHANNEL_ALL.equals(type)) {
					workTotal = insertChannel(result, workTotal);
				} else if (FILETYPE.HIT_UCC_ALL.equals(type)) {
					//공연 고도화
					int d = Constant.SPLIT_SIZE.HIT_UCC-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertHitUcc(result, workTotal, true, true);
				} else if (FILETYPE.HIGHLIGHT_ALL.equals(type)) {
					//공연 고도화
					int d = Constant.SPLIT_SIZE.HIGHLIGHT-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertHighLight(result, workTotal, true, true);
				} else if (FILETYPE.EVENT_ALL.equals(type)) {
					workTotal = insertEvnet(result, workTotal, true, true);
				}/*Choihu 20180806 유플릭스메타 데이터 VOD통합 주석처리
				 else if (FILETYPE.UFLIX_PACKAGE_ALL.equals(type)) {
					workTotal = insertUflixPackage(result, workTotal, true, true);
				} else if (FILETYPE.UFLIX_SERIES_ALL.equals(type)) {
					workTotal = insertUflixSeries(result, workTotal, true, true);
				}*/ else if (FILETYPE.THEME_ALL.equals(type)) {
					workTotal = insertTheme(result, workTotal, true);
				} else if (FILETYPE.VOD_APPEND.equals(type)) {
					int d = Constant.SPLIT_SIZE.VOD_APPEND-result.size();
					//System.out.println("스포츠가 나와라 : "+result.get(28));
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					if(("라이프".equals(result.get(28))&&"스포츠".equals(result.get(29))&&"골프".equals(result.get(30)))
							||("스포츠".equals(result.get(28))&&"골프".equals(result.get(29)))) {
						workTotal = appendGolf2(result, workTotal);
						appendVod(result, workTotal);
					}else if("KID".equals(result.get(85)) && result.get(85)!=null) {
						workTotal = appendKids(result, workTotal);
					}
					else if("KDC".equals(result.get(85)) && result.get(85)!=null) {
						workTotal = appendKidsClass(result, workTotal);
					}
					else if(!"".equals(result.get(78)) && result.get(78)!=null) {
						workTotal = appendVr(result, workTotal);
						//20200602 VR M_TV section 데이터 비디오포털 제공작업
						if("V3S".equals(result.get(78))) {
							workTotal = appendVr(result, workTotal);
						}
					}else if(!"".equals(result.get(71)) && result.get(71)!=null) {
						//20180629 공연 추가데이터
						workTotal = appendShow(result, workTotal);
					}else {
						workTotal = appendVod(result, workTotal);
					}
				} else if (FILETYPE.CHANNEL_APPEND.equals(type)) {
					workTotal = insertChannel(result, workTotal);
				} else if (FILETYPE.WATCH_RECORD.equals(type)) {
					workTotal = insertWatchRecord(result, workTotal, true);
				} else if (FILETYPE.I30_ALL.equals(type)) {
					//I30 SUPER_ID 선반영 대비
					//DB 클래스에 컬럼 추가 아직안함
					int d = Constant.SPLIT_SIZE.I30-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					
//					if("KID".equals(result.get(69))) {
//						sb2.append("제목 : ");
//						sb2.append(result.get(5));
//						sb2.append("\n");
//						sb2.append("줄거리 :");
//						sb2.append(result.get(32));
//						sb2.append("\n");
//						System.out.println("들어온거 맞나 봅시다"+ result.get(5)+"&&" + result.get(32));
//					}
					workTotal = insertI30(result, workTotal, true, true);
					
					
				} else if (FILETYPE.I30_APPEND.equals(type)) {
					//I30 SUPER_ID 선반영 대비
					//DB 클래스에 컬럼 추가 아직안함
					int d = Constant.SPLIT_SIZE.I30_APPEND-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = appendI30(result, workTotal);
				} else if (FILETYPE.I30_CHANNEL.equals(type)) {
					workTotal = insertI30Channel(result, workTotal);
				} else if (FILETYPE.VR_CONTENTS.equals(type)) {
					int d = Constant.SPLIT_SIZE.VR_CONTENTS-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertVrContents(result, workTotal, true, true);
				} else if (FILETYPE.CJ_CONTENTS.equals(type)) {
					int d = Constant.SPLIT_SIZE.CJ_CONTENTS-result.size();
					for(int s = 0; s < d; s++) {
						result.add("");
					}
					workTotal = insertCjContents(result, workTotal, true, true);
				}

				// 처리가 끝난후 뒷부분 버퍼에 담음
				sb = new StringBuffer();
				sb.append(b);

				// 마지막을 의미하는 문자가 없을 경우
			} else {
				sb.append(line);
			}
		}
		
//		  System.out.println("파일 시작");
//		  String isWorkDir = "/svc/diquest/kids"; 
//		  isWorkDir = FileUtil.appendEndsPath(isWorkDir); FileUtil.makeFolder(isWorkDir); 
//		  bw = FileUtil.getBufferedWriter(isWorkDir + "showshow.txt"); 
//		  bw.write(sb2.toString());
//		  FileUtil.closeBufferedWriter(bw);
//		  System.out.println("파일 끝");
//		  
//		  System.out.println("파일2 시작");
//		  String isWorkDir2 = "/svc/diquest/kids"; 
//		  isWorkDir = FileUtil.appendEndsPath(isWorkDir); FileUtil.makeFolder(isWorkDir); 
//		  bw = FileUtil.getBufferedWriter(isWorkDir + "showdb.txt"); 
//		  bw.write(sb3.toString());
//		  FileUtil.closeBufferedWriter(bw);
//		  System.out.println("파일 끝2");
		
		if (FILETYPE.WATCH_RECORD.equals(type)) {
			initTable(DB_NAME.WATCH_RECORD_SECTION);
			insertWatchRecordTotal(true);
			deleteWatchRecordTotal();
		}

		if (FILETYPE.VOD_ALL.equals(type)) {
			if(exist) {
				totalInsertTime += vr.executeInsertBatchTime();
				totalInsertTime += vod.executeInsertBatchTime();
				totalInsertTime += show.executeInsertBatchTime();
				totalInsertTime += golf2.executeInsertBatchTime();
				totalInsertTime += kids.executeInsertBatchTime();
				totalInsertTime += kidsClass.executeInsertBatchTime();	
			}else {
				totalInsertTime += vr.executeInsertBatchTime();
				totalInsertTime += vod.executeInsertBatchTime();
				totalInsertTime += show.executeInsertBatchTime();
				totalInsertTime += golf2.executeInsertBatchTime();
				totalInsertTime += kids.executeInsertBatchTime();	
			}
			
		} else if (FILETYPE.CHANNEL_ALL.equals(type)) {
			totalInsertTime += channel.executeInsertBatchTime();
		} else if (FILETYPE.HIT_UCC_ALL.equals(type)) {
			totalInsertTime += hitUcc.executeInsertBatchTime();
		} else if (FILETYPE.HIGHLIGHT_ALL.equals(type)) {
			totalInsertTime += highLight.executeInsertBatchTime();
		} else if (FILETYPE.EVENT_ALL.equals(type)) {
			totalInsertTime += event.executeInsertBatchTime();
		} /*else if (FILETYPE.UFLIX_PACKAGE_ALL.equals(type)) {//Choihu 20180806 Video메타데이터 통합 주석처리 
			totalInsertTime += uflixPackage.executeInsertBatchTime();
		} else if (FILETYPE.UFLIX_SERIES_ALL.equals(type)) {
			totalInsertTime += uflixSeries.executeInsertBatchTime();
		}*/ else if (FILETYPE.THEME_ALL.equals(type)) {
			totalInsertTime += theme.executeInsertBatchTime();
		} else if (FILETYPE.VOD_APPEND.equals(type)) {
			vod.executeInsertBatch();
			vod.executeUpdateBatch();
			vod.executeDeleteBatch();
			
			//20180629 Choihu 공연 한시간 추가데이터
			show.executeInsertBatch();
			show.executeUpdateBatch();
			show.executeDeleteBatch();
			
			vr.executeInsertBatch();
			vr.executeUpdateBatch();
			vr.executeDeleteBatch();
			
			golf2.executeInsertBatch();
			golf2.executeUpdateBatch();
			golf2.executeDeleteBatch();
			
			kids.executeInsertBatch();
			kids.executeUpdateBatch();
			kids.executeDeleteBatch();
			
			kidsClass.executeInsertBatch();
			kidsClass.executeUpdateBatch();
			kidsClass.executeDeleteBatch();
		} else if (FILETYPE.CHANNEL_APPEND.equals(type)) {
			totalInsertTime += channel.executeInsertBatchTime();
		} else if (FILETYPE.WATCH_RECORD.equals(type)) {
			totalInsertTime += watchrecord.executeInsertBatchTime();
		} else if (FILETYPE.I30_ALL.equals(type)) {
			totalInsertTime += i30.executeInsertBatchTime();
		} else if (FILETYPE.I30_APPEND.equals(type)) {
			 i30.executeInsertBatch();
			 i30.executeUpdateBatch();
			 i30.executeDeleteBatch();
		} else if (FILETYPE.I30_CHANNEL.equals(type)) {
			totalInsertTime += i30channel.executeInsertBatchTime();
		} else if (FILETYPE.VR_CONTENTS.equals(type)) {
			totalInsertTime += vrContents.executeInsertBatchTime();
		} else if (FILETYPE.CJ_CONTENTS.equals(type)) {
			totalInsertTime += cjContents.executeInsertBatchTime();
		}
		else if (FILETYPE.KIDS_EXP.equals(type)) {
			totalInsertTime += kidsClassExp.executeInsertBatchTime();
		}else if (FILETYPE.KIDS_SPC.equals(type)) {
			totalInsertTime += kidsClassSpc.executeInsertBatchTime();
		}

		FileUtil.closeBufferedReader(br);
		return workTotal;
	}






	public void workVideoAutoComplete() {
		insertVideoAutoComplete(vodAutoCompleteKeywordMap, vodAutoComplete);
	}
	//Choihu 20180629 공연 자동완성
	public void workShowAutoComplete() {
		insertShowAutoComplete(showAutoCompleteKeywordVO, showAutoComplete);
	}

	//Choihu 20181203 VR 자동완성
	public void workVrAutoComplete() {
		insertVrAutoComplete(vrAutoCompleteKeywordVO, vrAutoComplete);
	}
	//Choihu 20181203 VR 자동완성
	public void workArAutoComplete() {
		insertArAutoComplete(arAutoCompleteKeywordVO, arAutoComplete);
	}	
	//Choihu 20190115 골프2 자동완성
	public void workGolf2AutoComplete() {
		insertGolf2AutoComplete(golf2AutoCompleteKeywordVO, golf2AutoComplete);
	}	
	//Choihu 20200122 아이들나라 자동완성
	public void workKidsAutoComplete() {
		insertKidsAutoComplete(kidsAutoCompleteKeywordVO, kidsAutoComplete);
	}
		//dhkim 20210923 아이들나라클래스 자동완성
	public void workKidsClassAutoComplete() {
		insertKidsClassAutoComplete(kidsClassAutoCompleteKeywordVO, kidsClassAutoComplete);
	}	
	public void workUflixAutoComplete() {
		insertUflixAutoComplete(uflixMobileAutoCompleteKeywordMap, uflixMobileAutoComplete);
		insertUflixAutoComplete(uflixTvgAutoCompleteKeywordMap, uflixTvgAutoComplete);
	}

	public void workIptv30AutoComplete() {
		insertI30Complete(i30AutoCompleteKeywordMap, i30AutoComplete);
	}
	
	/**
	 * INSERT VrContents 채널 작업
	 * 
	 * @param result
	 * @param total
	 * @param insert 
	 * @param collectAutoComplete 
	 * @return
	 */

	private int insertVrContents(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		VrContentsVO vo = new VrContentsVO();
		vo.setAll(vo, result);

		/*if (!(Constant.TYPE.FILTERING_CODE2.equalsIgnoreCase(vo.getFilteringCode())
				|| Constant.TYPE.FILTERING_CODE.equalsIgnoreCase(vo.getFilteringCode()))) {
			return total;
		}*/
		//
		if(insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getCtgryId());
			sb.append(vo.getCntntsGroupId());
			sb.append(vo.getContId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (vrContKeySet.contains(key)) {
				return total;
			} else {
				vrContents.insertAll(vo);
				total++;
				vrContKeySet.add(key);
			}
			
			if (collectAutoComplete){
				VrAutoCompleteVO vrAutoVo = new VrAutoCompleteVO();
				vrAutoVo.setOrgId(vo.getContId());
				vrAutoVo.setKeyword(vo.getContNm());
				vrAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getContNm()));
				vrAutoVo.setType("C");
				vrAutoVo.setField("A");
				vrAutoCompleteKeywordVO.add(vrAutoVo);
			}
		}
		
		
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			channel.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

/*	*//**
	 * INSERT CjContents 채널 작업
	 * 
	 * @param result
	 * @param total
	 * @param insert 
	 * @param collectAutoComplete 
	 * @return
	 */

	private int insertCjContents(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		CjContentsVO vo = new CjContentsVO();
		vo.setAll(vo, result);

/*		if (!(Constant.TYPE.FILTERING_CODE2.equalsIgnoreCase(vo.getFilteringCode())
				|| Constant.TYPE.FILTERING_CODE.equalsIgnoreCase(vo.getFilteringCode()))) {
			return total;
		}*/
		//
		if(insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getCatId());
			sb.append(vo.getContentId());
			sb.append(vo.getClipId());
			sb.append(vo.getClipContentId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (cjContKeySet.contains(key)) {
				return total;
			} else {
				cjContents.insertAll(vo);
				total++;
				cjContKeySet.add(key);
			}
			
			if (collectAutoComplete){
				ShowAutoCompleteVO showAutoVo = new ShowAutoCompleteVO();
				showAutoVo.setOrgId(vo.getClipId());
				showAutoVo.setKeyword(vo.getClipContentTitle());
				showAutoVo.setField("J");
				showAutoVo.setDeviceData(vo.getDeviceData());
				showAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getClipContentTitle()));	
				showAutoCompleteKeywordVO.add(showAutoVo);
			}
		}
		
		
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			cjContents.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	
	/**
	 * INSERT GOLF2 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertGolf2(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		// TODO Auto-generated method stub
		Golf2VO vo = new Golf2VO();
		vo.setAll(vo, result);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (golf2KeySet.contains(key)) {
				return total;
			} else {
				golf2.insertAll(vo);
				golf2KeySet.add(key);
			}
			//자동완성 모으기
			if (collectAutoComplete){
				
				
				if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
					Golf2AutoCompleteVO golfAutoVo = new Golf2AutoCompleteVO();
					golfAutoVo.setOrgId(vo.getAlbumId());
					golfAutoVo.setKeyword(vo.getAlbumName());
					golfAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
					golfAutoVo.setType("V");
					golfAutoVo.setField("A");
					golf2AutoCompleteKeywordVO.add(golfAutoVo);
				}
				
				if(!"".equals(vo.getActor()) ) {
					String[] actorArr = vo.getActor().split(",");
					for (String actor : actorArr) {
						Golf2AutoCompleteVO golfAutoVo = new Golf2AutoCompleteVO();
						golfAutoVo.setOrgId(vo.getAlbumId());
						golfAutoVo.setKeyword(actor.trim());
						golfAutoVo.setKeywordChosung(Method.normalizeChosung(actor.trim()));
						golfAutoVo.setType("V");
						golfAutoVo.setField("P");
						golf2AutoCompleteKeywordVO.add(golfAutoVo);
					}
				}
				
				if(!"".equals(vo.getSubTitle())) {
					Golf2AutoCompleteVO golfAutoVo = new Golf2AutoCompleteVO();
					golfAutoVo.setOrgId(vo.getAlbumId());
					golfAutoVo.setKeyword(vo.getSubTitle());
					golfAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getSubTitle()));
					golfAutoVo.setType("V");
					golfAutoVo.setField("T");
					golf2AutoCompleteKeywordVO.add(golfAutoVo);
				}
				
				if(!"".equals(vo.getProducer()) ) {
					String[] pdArr = vo.getProducer().split(",");
					for (String pd : pdArr) {
						Golf2AutoCompleteVO golfAutoVo = new Golf2AutoCompleteVO();
						golfAutoVo.setOrgId(vo.getAlbumId());
						golfAutoVo.setKeyword(pd.trim());
						golfAutoVo.setKeywordChosung(Method.normalizeChosung(pd.trim()));
						golfAutoVo.setType("V");
						golfAutoVo.setField("P");
						golf2AutoCompleteKeywordVO.add(golfAutoVo);
					}
				}
				
				
			}
			// vod.insertAll(vo);
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			golf2.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * INSERT KIDS 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertKids(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		// TODO Auto-generated method stub
		KidsVO vo = new KidsVO();
		vo.setAll(vo, result);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (kidsKeySet.contains(key)) {
				return total;
			} else {
				kids.insertAll(vo);
				kidsKeySet.add(key);
			}
			//자동완성 모으기
			if (collectAutoComplete){
				
				/* AlbumName 중복제거
				 * if (kidsAutoKeySet.contains(vo.getAlbumName())) {
				 * 
				 * } else { kidsAutoKeySet.add(vo.getAlbumName());
				 * 
				 * if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
				 * KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
				 * kidsAutoVo.setOrgId(vo.getAlbumId());
				 * kidsAutoVo.setKeyword(vo.getAlbumName());
				 * kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
				 * kidsAutoVo.setType("V"); kidsAutoVo.setField("A");
				 * kidsAutoCompleteKeywordVO.add(kidsAutoVo); }
				 * 
				 * }
				 */
				if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
					if ("PALB".equals(vo.getResultType())) {

						KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
						kidsAutoVo.setOrgId(vo.getAlbumId());
						kidsAutoVo.setKeyword(vo.getAlbumName());
						kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsAutoVo.setType("A");
						kidsAutoVo.setField("A");
						kidsAutoCompleteKeywordVO.add(kidsAutoVo);

					} else if ("PCAT".equals(vo.getResultType())) {

						KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
						kidsAutoVo.setOrgId(vo.getAlbumId());
						kidsAutoVo.setKeyword(vo.getAlbumName());
						kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsAutoVo.setType("C");
						kidsAutoVo.setField("A");
						kidsAutoCompleteKeywordVO.add(kidsAutoVo);

					} else if ("CALB".equals(vo.getResultType())) {

						KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
						kidsAutoVo.setOrgId(vo.getAlbumId());
						kidsAutoVo.setKeyword(vo.getAlbumName());
						kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsAutoVo.setType("S");
						kidsAutoVo.setField("A");
						kidsAutoCompleteKeywordVO.add(kidsAutoVo);

					} else {

						KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
						kidsAutoVo.setOrgId(vo.getAlbumId());
						kidsAutoVo.setKeyword(vo.getAlbumName());
						kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsAutoVo.setType("");
						kidsAutoVo.setField("A");
						kidsAutoCompleteKeywordVO.add(kidsAutoVo);

					}
				}
					
				
			}
			// vod.insertAll(vo);
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			kids.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	
	/**
	 * INSERT KIDS 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	
	private int insertKidsClass(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		// TODO Auto-generated method stub
		KidsClassVO vo = new KidsClassVO();
		vo.setAll(vo, result);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (kidsClassKeySet.contains(key)) {
				return total;
			} else {
				kidsClass.insertAll(vo);
				kidsClassKeySet.add(key);
			}
			//자동완성 모으기
			if (collectAutoComplete){
				
				/* AlbumName 중복제거
				 * if (kidsAutoKeySet.contains(vo.getAlbumName())) {
				 * 
				 * } else { kidsAutoKeySet.add(vo.getAlbumName());
				 * 
				 * if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
				 * KidsAutoCompleteVO kidsAutoVo = new KidsAutoCompleteVO();
				 * kidsAutoVo.setOrgId(vo.getAlbumId());
				 * kidsAutoVo.setKeyword(vo.getAlbumName());
				 * kidsAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
				 * kidsAutoVo.setType("V"); kidsAutoVo.setField("A");
				 * kidsAutoCompleteKeywordVO.add(kidsAutoVo); }
				 * 
				 * }
				 */
				if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
					if ("PALB".equals(vo.getResultType())) {

						KidsClassAutoCompleteVO kidsClassAutoVo = new KidsClassAutoCompleteVO();
						kidsClassAutoVo.setOrgId(vo.getAlbumId());
						kidsClassAutoVo.setKeyword(vo.getAlbumName());
						kidsClassAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsClassAutoVo.setType("A");
						kidsClassAutoVo.setField("A");
						kidsClassAutoCompleteKeywordVO.add(kidsClassAutoVo);

					} else if ("PCAT".equals(vo.getResultType())) {

						KidsClassAutoCompleteVO kidsClassAutoVo = new KidsClassAutoCompleteVO();
						kidsClassAutoVo.setOrgId(vo.getAlbumId());
						kidsClassAutoVo.setKeyword(vo.getAlbumName());
						kidsClassAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsClassAutoVo.setType("C");
						kidsClassAutoVo.setField("A");
						kidsClassAutoCompleteKeywordVO.add(kidsClassAutoVo);

					} else if ("CALB".equals(vo.getResultType())) {

						KidsClassAutoCompleteVO kidsClassAutoVo = new KidsClassAutoCompleteVO();
						kidsClassAutoVo.setOrgId(vo.getAlbumId());
						kidsClassAutoVo.setKeyword(vo.getAlbumName());
						kidsClassAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsClassAutoVo.setType("S");
						kidsClassAutoVo.setField("A");
						kidsClassAutoCompleteKeywordVO.add(kidsClassAutoVo);

					} else {

						KidsClassAutoCompleteVO kidsClassAutoVo = new KidsClassAutoCompleteVO();
						kidsClassAutoVo.setOrgId(vo.getAlbumId());
						kidsClassAutoVo.setKeyword(vo.getAlbumName());
						kidsClassAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
						kidsClassAutoVo.setType("");
						kidsClassAutoVo.setField("A");
						kidsClassAutoCompleteKeywordVO.add(kidsClassAutoVo);

					}
				}
					
				
			}
			// vod.insertAll(vo);
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			kidsClass.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	
	
	/**
	 * INSERT VR 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertVr(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		// TODO Auto-generated method stub
		VrVO vo = new VrVO();
		vo.setAll(vo, result);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (vrKeySet.contains(key)) {
				return total;
			} else {
				vr.insertAll(vo);
				vrKeySet.add(key);
			}
			//자동완성 모으기
			if (collectAutoComplete){
				
				
				if (!"".equals(vo.getAlbumId()) && !"".equals(vo.getAlbumName())) {
					VrAutoCompleteVO vrAutoVo = new VrAutoCompleteVO();
					vrAutoVo.setOrgId(vo.getAlbumId());
					vrAutoVo.setKeyword(vo.getAlbumName());
					vrAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));
					vrAutoVo.setType("V");
					vrAutoVo.setField("A");
					vrAutoCompleteKeywordVO.add(vrAutoVo);
				}
				
				if(!"".equals(vo.getActor()) ) {
					String[] actorArr = vo.getActor().split(",");
					for (String actor : actorArr) {
						VrAutoCompleteVO vrAutoVo = new VrAutoCompleteVO();
						vrAutoVo.setOrgId(vo.getAlbumId());
						vrAutoVo.setKeyword(actor.trim());
						vrAutoVo.setKeywordChosung(Method.normalizeChosung(actor.trim()));
						vrAutoVo.setType("V");
						vrAutoVo.setField("P");
						vrAutoCompleteKeywordVO.add(vrAutoVo);
					}
				}
				
				
				
				
			}
			// vod.insertAll(vo);
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			vr.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	/**
	 * INSERT SHOW 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertShow(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		ShowVO vo = new ShowVO();
		vo.setAll(vo, result, showPersonGroupData, showLinkData);
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
//			 sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (showKeySet.contains(key)) {
				return total;
			} else {
				show.insertAll(vo);
				showKeySet.add(key);
			}
			//자동완성 모으기
			if (collectAutoComplete){
				
				ShowAutoCompleteVO showAutoVo = new ShowAutoCompleteVO();
				if(!"".equals(vo.getCatId()) &&  !"".equals(vo.getCatName())) {
					showAutoVo.setOrgId(vo.getCatId());
					showAutoVo.setKeyword(vo.getCatName());
					showAutoVo.setField("C");
					showAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getCatName()));	
					showAutoCompleteKeywordVO.add(showAutoVo);
				}
				
				if(!"".equals(vo.getAlbumId()) &&  !"".equals(vo.getAlbumName())) {
					showAutoVo.setOrgId(vo.getAlbumId());
					showAutoVo.setKeyword(vo.getAlbumName());				
					showAutoVo.setField("M");
					showAutoVo.setKeywordChosung(Method.normalizeChosung(vo.getAlbumName()));	
					showAutoCompleteKeywordVO.add(showAutoVo);
				}
			}
			// vod.insertAll(vo);
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			double start = System.nanoTime();
			show.executeInsertBatch();
			
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	/**
	 * INSERT VOD 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertVod(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		VodVO vo = new VodVO();
		vo.setAll(vo, result);
		// 20160517 비디오포탈 데이터만 쓰고 나머진 버린다.
		//Choihu 20180806 NSC,MFX,UFX
		if (!(Constant.TYPE.VIDEO_PORTAL_CATGB.equalsIgnoreCase(vo.getCatGb())
//				|| Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())
				|| Constant.TYPE.VIDOE_PORTAL_MFX.equalsIgnoreCase(vo.getCatGb())
				|| Constant.TYPE.VIDOE_PORTAL_UFX.equalsIgnoreCase(vo.getCatGb()))) {
			return total;
		}
//		if(Constant.TYPE.SECTION_TYPE.equalsIgnoreCase(vo.getSection())){
//			return total;
//		}

		// 영화 진흥원 데이터를 추가해준다. 넣어준다.
		mergerManager.updateVod(vo);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			// vo.init();

			if (vodKeySet.contains(key)) {
				return total;
			} else {
				vod.insertAll(vo);
				vodKeySet.add(key);
			}

			// vod.insertAll(vo);
		}

		// boolean isTV = false;

		if (collectAutoComplete){
			if((vo.getResultType().equalsIgnoreCase("CALB") &&  
				(vo.getSection().equals("LTE_REP") || vo.getSection().equals("LTE_FOR") ||  vo.getSection().equals("LTE_ANI") || vo.getSection().equals("LTE_KIDS"))) 
					|| (vo.getGenre2().equals("뉴스")))  {
				
			}else{
//			System.out.println("getResultType : " + vo.getResultType());
//			System.out.println("section : " + vo.getSection());
				Map<String, List<String>> mapVP = null;
				Map<String, List<String>> mapMFX = null;
				Map<String, List<String>> mapUFX = null;
				//VOD자동완성
				if (Constant.TYPE.VIDEO_PORTAL_CATGB.equalsIgnoreCase(vo.getCatGb())) {
					mapVP = vodAutoCompleteKeywordMap;
					autoCompleteKeywordSet(mapVP, vo, "VP");
				}else if (Constant.TYPE.VIDOE_PORTAL_MFX.equalsIgnoreCase(vo.getCatGb())) {
					//mapVP = vodAutoCompleteKeywordMap;
					//autoCompleteKeywordSet(mapVP, vo, "VP");
					mapMFX =  uflixMobileAutoCompleteKeywordMap;
					autoCompleteKeywordSet(mapMFX, vo, "MFX");
				}else if (Constant.TYPE.VIDOE_PORTAL_UFX.equalsIgnoreCase(vo.getCatGb())) {
					mapUFX = uflixTvgAutoCompleteKeywordMap;
					autoCompleteKeywordSet(mapUFX, vo, "UFX");
				}
				
		
				
			/////////////////////////////////////////////////////////////////////////////////////////////////////////	
			//Choihu 20180806 기존 VIDEO자동완성로직 주석처리
/*			Map<String, List<String>> map = null;
			if ((Constant.TYPE.VIDEO_PORTAL_CATGB.equalsIgnoreCase(vo.getCatGb()) || Constant.TYPE.VIDOE_PORTAL_MFX.equalsIgnoreCase(vo.getCatGb()))) {
				map = vodAutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			} 
//			else if (Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())) {
//				map = iptvVodAutoCompleteKeywordMap;
//			}

			// 자동완성 키워드는 따로 메모리에 모아둔다.
			// if(collectAutoComplete){
			// if(ConflictManager.isConflicted(vo.getCatName().toUpperCase(),
			// AUTO_COMPLETE_FIELD.CAT_NAME) == false)
			
//			String catName=vo.getCatName().replace("\\n", "");
//			addCntMap(map,catName.toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);
			addCntMap(map, vo.getCatName().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);

			String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());
			// if(ConflictManager.isConflicted(normalizeAlbumName.toUpperCase(),
			// AUTO_COMPLETE_FIELD.ALBUM_NAME) == false)
			addCntMap(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

			String[] toks = vo.getActor().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACTOR) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
			}
			toks = vo.getOverseerName().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.OVERSEER_NAME) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
			}
			toks = vo.getStarringActor().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.STARRING_ACTOR) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
			}*/
/*			
			toks = vo.getKeyword().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.KEYWORD) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.KEYWORD);
			}
			toks = vo.getCastNameMax().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.CAST_NAME_MAX) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX);
			}
			toks = vo.getCastNameMaxEng().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG);
			}
			toks = vo.getActDispMax().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX);
			}
			toks = vo.getActDispMaxEng().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
				// addCntMap(vodAutoCompleteKeywordMap, s.trim().toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
			}
			*/
			}
		}

		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			vod.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	
	private void autoCompleteKeywordSet(Map<String, List<String>> map, VodVO vo, String type) {

	
		//UFLIX일때
		if("MFX".equals(type)||"UFX".equals(type)) {

			addCntMap(map, vo.getCatName().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);

			String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());

			addCntMap(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

			String[] toks = vo.getActor().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
			}
			toks = vo.getOverseerName().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
			}
			toks = vo.getStarringActor().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
			}

		
			toks = vo.getKeyword().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.KEYWORD);
			}

			addCntMap(map, vo.getTitleEng().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ENG);

			toks = vo.getDirectorEng().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.DIRECTOR_ENG);
			}
			toks = vo.getPlayerEng().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.PLAYER_ENG);
			}
			toks = vo.getCastNameEng().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_ENG);
			}
			toks = vo.getCastName().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME);
			}
			toks = vo.getTitleOrigin().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ORIGIN);
			}
			toks = vo.getWriterOrigin().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.WRITER_ORIGIN);
			}

			toks = vo.getCastNameMax().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX);
			}
			toks = vo.getCastNameMaxEng().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG);
			}
			toks = vo.getActDispMax().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX);
			}
			toks = vo.getActDispMaxEng().split(",");
			for (String s : toks) {
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
			}
		}else {
			if("TV다시보기".equals(vo.getGenreUxten())
				||"해외시리즈".equals(vo.getGenreUxten())
				||"애니".equals(vo.getGenreUxten())
				||"키즈".equals(vo.getGenreUxten())
				
					){
				
				if("PALB".equals(vo.getResultType())||"PCAT".equals(vo.getResultType())) {
					addCntMap(map, vo.getCatName().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);

					String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());

					addCntMap(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

					String[] toks = vo.getActor().split(",");
					for (String s : toks) {
						addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
					}
					toks = vo.getOverseerName().split(",");
					for (String s : toks) {
						addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
					}
					toks = vo.getStarringActor().split(",");
					for (String s : toks) {
						addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
					}
				}
				
			} else if("영화".equals(vo.getGenreUxten())
					//section 추가
					||"실시간TV".equals(vo.getGenreUxten())
					||"하이라이트".equals(vo.getGenreUxten())
					||"19금".equals(vo.getGenreUxten())
					||"외국어".equals(vo.getGenreUxten())
					||"경영/자격증".equals(vo.getGenreUxten())
					||"인문학특강".equals(vo.getGenreUxten())
					||"초중고교과".equals(vo.getGenreUxten())
					||"명품다큐".equals(vo.getGenreUxten())
					||"음악/공연".equals(vo.getGenreUxten())
					||"게임".equals(vo.getGenreUxten())
					||"스포츠".equals(vo.getGenreUxten())
					||"요리".equals(vo.getGenreUxten())
					||"맛집".equals(vo.getGenreUxten())
					||"여행".equals(vo.getGenreUxten())
					||"뷰티/건강/다이어트".equals(vo.getGenreUxten())
					||"취미".equals(vo.getGenreUxten())
					||"살림노하우".equals(vo.getGenreUxten())
					||"비디오쇼핑".equals(vo.getGenreUxten())
					||"360도".equals(vo.getGenreUxten())
					||"미분류".equals(vo.getGenreUxten())
					||"대박영상".equals(vo.getGenreUxten())
					||"배너형이벤트".equals(vo.getGenreUxten())
					||"알뜰생활정보".equals(vo.getGenreUxten())){
				addCntMap(map, vo.getCatName().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);

				String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());

				addCntMap(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

				String[] toks = vo.getActor().split(",");
				for (String s : toks) {
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
				}
				toks = vo.getOverseerName().split(",");
				for (String s : toks) {
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
				}
				toks = vo.getStarringActor().split(",");
				for (String s : toks) {
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
				}
			}
			

		}
		
		
		
	}
	
	/**
	 * 추가색인 데이터 Golf2 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendGolf2(List<String> result, int total) {
		Golf2VO vo = new Golf2VO();
		String dbWorkType = result.get(0);
		
		result.remove(0);
		vo.setAppend(vo, result);
		
		boolean ischeker = true ; 

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = golf2.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());

			if(ischeker == false){
				golf2.insertAll(vo);
			}else{

				golf2.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			golf2.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			golf2.delete(vo);
		}
	
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				golf2.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				golf2.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				golf2.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				golf2.executeDeleteBatch();
			}
		}
		return total;
	}
	
	/**
	 * 추가색인 데이터 Kids 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendKids(List<String> result, int total) {
		KidsVO vo = new KidsVO();
		String dbWorkType = result.get(0);
		
		result.remove(0);
		vo.setAppend(vo, result);
		
		boolean ischeker = true ; 

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = kids.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());

			if(ischeker == false){
				kids.insertAll(vo);
			}else{

				kids.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			kids.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			kids.delete(vo);
		}
	
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				kids.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				kids.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				kids.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				kids.executeDeleteBatch();
			}
		}
		return total;
	}
	
	/**
	 * 추가색인 데이터 Kids 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendKidsClass(List<String> result, int total) {
		KidsClassVO vo = new KidsClassVO();
		String dbWorkType = result.get(0);
		
		result.remove(0);
		vo.setAppend(vo, result);
		
		boolean ischeker = true ; 

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = kids.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());

			if(ischeker == false){
				kidsClass.insertAll(vo);
			}else{

				kidsClass.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			kidsClass.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			kidsClass.delete(vo);
		}
	
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				kidsClass.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				kidsClass.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				kidsClass.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				kidsClass.executeDeleteBatch();
			}
		}
		return total;
	}	
	
	
	/**
	 * 추가색인 데이터 VR 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendVr(List<String> result, int total) {
		VrVO vo = new VrVO();
		String dbWorkType = result.get(0);
		
		result.remove(0);
		vo.setAppend(vo, result);
		
		boolean ischeker = true ; 

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = vr.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());

			if(ischeker == false){
				vr.insertAll(vo);
			}else{

				vr.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			vr.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			vr.delete(vo);
		}
	
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				vr.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				vr.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				vr.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				vr.executeDeleteBatch();
			}
		}
		return total;
	}
	
	/**
	 * 추가색인 데이터 SHOW 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendShow(List<String> result, int total) {
		ShowVO vo = new ShowVO();
		String dbWorkType = result.get(0);
		result.remove(0);
		vo.setAppend(vo, result, showPersonGroupData, showLinkData);
		
		boolean ischeker = true ; 
		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = show.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());
			if(ischeker == false){
				show.insertAll(vo);
			}else{
				
				show.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			show.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			show.delete(vo);
		}
	
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				show.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				show.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				show.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				show.executeDeleteBatch();
			}
		}
		return total;
	}
	/**
	 * 추가색인 데이터 VOD 작업
	 * 
	 * @param result
	 * @param cnt
	 */
	private int appendVod(List<String> result, int total) {
		VodVO vo = new VodVO();
		String dbWorkType = result.get(0);
		result.remove(0);
		vo.setAppend(vo, result);
		boolean ischeker = true ; 

		if (!(Constant.TYPE.VIDEO_PORTAL_CATGB.equalsIgnoreCase(vo.getCatGb())
//				|| Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())
				|| Constant.TYPE.VIDOE_PORTAL_MFX.equalsIgnoreCase(vo.getCatGb())
				|| Constant.TYPE.VIDOE_PORTAL_UFX.equalsIgnoreCase(vo.getCatGb()))) {
			return total;
		}

		// 영화 진흥원 데이터를 추가해준다. 넣어준다.
		mergerManager.updateVod(vo);

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = vod.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());
			if(ischeker == false){
				vod.insertAll(vo);
			}else{
				vod.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			vod.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			vod.delete(vo);
		}

		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker==true) {
				vod.executeUpdateBatch();
			}else if(DELIMS.UPDATE.equals(dbWorkType)&& ischeker==false) {
				vod.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				vod.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				vod.executeDeleteBatch();
			}
		}
		return total;
	}

	/**
	 * INSERT CHANNEL 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */
	private int insertChannel(List<String> result, int total) {
		ChannelVO vo = new ChannelVO();
		vo.setAll(vo, result);

		if (!Constant.TYPE.FILTERING_CODE.equalsIgnoreCase(vo.getFilteringCode()))
			return total;

		channel.insertAll(vo);
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			channel.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * INSERT HITUCC 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertHitUcc(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		HitUccVO vo = new HitUccVO();
		vo.setAll(vo, result);
/*
		20181012 대박영상 제한 제거(기존 HDTV,MUSI만 INSERT)
		if (!Constant.TYPE.SVC_TYPE.equalsIgnoreCase(vo.getSvcType()) && !Constant.TYPE.SVC_TYPE2.equalsIgnoreCase(vo.getSvcType()))
			return total;

*/		
		if (insert)
			hitUcc.insertAll(vo);

		if (collectAutoComplete) {
			// if(ConflictManager.isConflicted(vo.getContentsName().toUpperCase(),
			// AUTO_COMPLETE_FIELD.ALBUM_NAME) == false)
			String normalizeName = Method.normalizeAutoCompleteTitle(vo.getContentsName());
			//20190618 비디오포털제거
			/*if("HDTV".equals(vo.getSvcType())||"MUSI".equals(vo.getSvcType())) {
				addCntMap(vodAutoCompleteKeywordMap, normalizeName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);
			}*/
			if("MUSI".equals(vo.getSvcType())||"AR".equals(vo.getSvcType())) {
				ShowAutoCompleteVO showAutoVo = new ShowAutoCompleteVO();
				showAutoVo.setOrgId(vo.getContentsId());
				showAutoVo.setKeyword(normalizeName.toUpperCase());
				showAutoVo.setField("C");
				showAutoVo.setKeywordChosung(Method.normalizeChosung(normalizeName.toUpperCase()));	
				showAutoCompleteKeywordVO.add(showAutoVo);
			}
			if("VR".equals(vo.getSvcType())) {
				//DBConnector.createDeletePstmt(dstConn, DB_NAME.VR_AUTO_COMPLETE, new String[] {"TYPE"}),"P");
				VrAutoCompleteVO vrAutoVo = new VrAutoCompleteVO();
				vrAutoVo.setOrgId(vo.getContentsId());
				vrAutoVo.setKeyword(normalizeName.toUpperCase());
				vrAutoVo.setKeywordChosung(Method.normalizeChosung(normalizeName.toUpperCase()));
				vrAutoVo.setType("U");
				vrAutoVo.setField("A");
				vrAutoCompleteKeywordVO.add(vrAutoVo);
			}
		}

		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			hitUcc.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * INSERT HIGHLIGHT 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertHighLight(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		HighLightVO vo = new HighLightVO();
		vo.setAll(vo, result);

/*		if (!Constant.TYPE.SVC_TYPE.equalsIgnoreCase(vo.getSvcType()) 
			&& !Constant.TYPE.SVC_TYPE2.equalsIgnoreCase(vo.getSvcType()))
			return total;*/

		if (insert)
			highLight.insertAll(vo);

		if (collectAutoComplete) {
			String normalizeName = Method.normalizeAutoCompleteTitle(vo.getContentsName());
			
			if("MUSI".equals(vo.getSvcType())/*&&"VOD".equals(vo.getResultType())*/) {
				ShowAutoCompleteVO showAutoVo = new ShowAutoCompleteVO();
				showAutoVo.setOrgId(vo.getContentsId());
				showAutoVo.setKeyword(normalizeName.toUpperCase());
				showAutoVo.setField("C");
				showAutoVo.setKeywordChosung(Method.normalizeChosung(normalizeName.toUpperCase()));	
				showAutoCompleteKeywordVO.add(showAutoVo);
			}//20190618 비디오포털제거
			/*else {
				addCntMap(vodAutoCompleteKeywordMap, normalizeName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);
			}*/
			
		}

		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			highLight.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * INSERT EVENT 작업
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 */
	private int insertEvnet(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		EventVO vo = new EventVO();
		vo.setAll(vo, result);

		if (!Constant.TYPE.SVC_TYPE.equalsIgnoreCase(vo.getSvcType()))
			return total;

		if (insert)
			event.insertAll(vo);

		if (collectAutoComplete) {
			String normalizeName = Method.normalizeAutoCompleteTitle(vo.getTitle());
			addCntMap(vodAutoCompleteKeywordMap, normalizeName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);
		}

		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			event.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

/*Choihu 20180806	UFILX VOD로 통합 주석처리*//**
	 * INSERT 유플릭스 패키지
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 *//*
	private int insertUflixPackage(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		UflixPackageVO vo = new UflixPackageVO();
		vo.setAll(vo, result);

		// 20160517 유플릭스 데이터만 쓰고 나머진 버린다.
		if (!(Constant.TYPE.UFLIX_CATGB_MOBILE.equalsIgnoreCase(vo.getCatGb()) || Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())
//				|| Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())
				)) {
			return total;
		}

		mergerManager.updateUflixPackage(vo);

		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			String key = sb.toString();

			if (uflixKeySet.contains(key)) {
				return total;
			} else {
				uflixPackage.insertAll(vo);
				uflixKeySet.add(key);
			}
		}

		// boolean isTV = false;

		// 자동완성 키워드는 따로 메모리에 모아둔다.
		if (collectAutoComplete) {
			Map<String, List<String>> map = null;
			if (Constant.TYPE.UFLIX_CATGB_MOBILE.equalsIgnoreCase(vo.getCatGb())) {
				map = uflixMobileAutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			} else if (Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())) {
				// isTV = true;
				map = uflixTvgAutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			} 
//			else if (Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())) {
//				map = iptvUflixAutoCompleteKeywordMap;
//			}

			if (map != null) {
				// if(ConflictManager.isConflicted(vo.getCatName().trim().toUpperCase(),
				// AUTO_COMPLETE_FIELD.CAT_NAME, isTV) == false)
				addCntMap(map, vo.getCatName().trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME);

				String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());
				// if(ConflictManager.isConflicted(normalizeAlbumName.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ALBUM_NAME, isTV) == false)
				addCntMap(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

				String[] toks = vo.getActor().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACTOR, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
				}
				toks = vo.getOverseerName().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.OVERSEER_NAME, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
				}
				toks = vo.getStarringActor().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.STARRING_ACTOR, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
				}
				toks = vo.getKeyword().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.KEYWORD, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.KEYWORD);
				}
				// if(ConflictManager.isConflicted(vo.getTitleEng().toUpperCase(),
				// AUTO_COMPLETE_FIELD.TITLE_ENG, isTV) == false)
				addCntMap(map, vo.getTitleEng().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ENG);

				toks = vo.getDirectorEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.DIRECTOR_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.DIRECTOR_ENG);
				}
				toks = vo.getPlayerEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.PLAYER_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.PLAYER_ENG);
				}
				toks = vo.getCastNameEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_ENG);
				}
				toks = vo.getCastName().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME);
				}
				toks = vo.getTitleOrigin().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.TITLE_ORIGIN, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ORIGIN);
				}
				toks = vo.getWriterOrigin().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.WRITER_ORIGIN, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.WRITER_ORIGIN);
				}

				toks = vo.getCastNameMax().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_MAX, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX);
				}
				toks = vo.getCastNameMaxEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG);
				}
				toks = vo.getActDispMax().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACT_DISP_MAX, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX);
				}
				toks = vo.getActDispMaxEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
				}
			}
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			uflixPackage.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	*//**
	 * INSERT 유플릭스 시리즈
	 * 
	 * @param result
	 * @param total
	 * @param collectAutoComplete
	 * @param insert
	 * @return
	 *//*
	private int insertUflixSeries(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		UflixSeriesVO vo = new UflixSeriesVO();
		vo.setAll(vo, result);

		// 20160517 유플릭스 데이터만 쓰고 나머진 버린다.
		if (!(Constant.TYPE.UFLIX_CATGB_MOBILE.equalsIgnoreCase(vo.getCatGb()) || Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())
//				|| Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())
				)) {
			return total;
		}

		// 20160615 유플릭스 series 경우 MULTI_MAPPING_FLAG =0 인것만 처리
		if (!vo.getMultiMappingFlag().equals("0")) {
			return total;
		}

		mergerManager.updateUflixSeries(vo);

		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getId());
			String key = sb.toString();

			if (uflixKeySet.contains(key)) {
				return total;
			} else {
				uflixSeries.insertAll(vo);
				uflixKeySet.add(key);
			}

		}

		// 자동완성 키워드는 따로 메모리에 모아둔다.
		if (collectAutoComplete) {
			Map<String, List<String>> map = null;

			// boolean isTV = false;

			if (Constant.TYPE.UFLIX_CATGB_MOBILE.equalsIgnoreCase(vo.getCatGb())) {
				map = uflixMobileAutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			} else if (Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())) {
				// isTV = true;
				map = uflixTvgAutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			} 
//			else if (Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())) {
//				map = iptvUflixAutoCompleteKeywordMap;
//			}

			if (map != null) {
				String normalizeName = Method.normalizeAutoCompleteTitle(vo.getName());
				// if(ConflictManager.isConflicted(vo.getCastName().toUpperCase(),
				// AUTO_COMPLETE_FIELD.ALBUM_NAME, isTV) == false)
				addCntMap(map, normalizeName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME);

				String[] toks = vo.getActor().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACTOR, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR);
				}
				toks = vo.getOverseerName().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.OVERSEER_NAME, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME);
				}
				toks = vo.getStarringActor().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.OVERSEER_NAME, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR);
				}
				toks = vo.getKeyword().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.KEYWORD, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.KEYWORD);
				}
				// if(ConflictManager.isConflicted(vo.getTitleEng().toUpperCase(),
				// AUTO_COMPLETE_FIELD.TITLE_ENG, isTV) == false)
				addCntMap(map, vo.getTitleEng().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ENG);

				toks = vo.getDirectorEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.DIRECTOR_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.DIRECTOR_ENG);
				}
				toks = vo.getPlayerEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.PLAYER_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.PLAYER_ENG);
				}
				toks = vo.getCastNameEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_ENG);
				}
				toks = vo.getCastName().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME);
				}
				toks = vo.getTitleOrigin().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.TITLE_ORIGIN, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.TITLE_ORIGIN);
				}
				toks = vo.getWriterOrigin().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.WRITER_ORIGIN, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.WRITER_ORIGIN);
				}

				toks = vo.getCastNameMax().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_MAX, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX);
				}
				toks = vo.getCastNameMaxEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG);
				}
				toks = vo.getActDispMax().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACT_DISP_MAX, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX);
				}
				toks = vo.getActDispMaxEng().split(",");
				for (String s : toks) {
					// if(ConflictManager.isConflicted(s.toUpperCase(),
					// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG, isTV) == false)
					addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
				}
			}
		}
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			uflixSeries.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}*/

	/**
	 * INSERT THEME 작업
	 * 
	 * @param result
	 * @param total
	 * @param insert
	 * @return
	 */
	private int insertTheme(List<String> result, int total, boolean insert) {
		ThemeVO vo = new ThemeVO();
		vo.setAll(vo, result);

		// if(!Constant.TYPE.SVC_TYPE.equalsIgnoreCase(vo.getSvcType()))
		// return total;

		if (insert)
			theme.insertAll(vo);

		// if(collectAutoComplete){
		// addCntMap(vodAutoCompleteKeywordMap, vo.getTitle(),
		// AUTO_COMPLETE_FIELD.ALBUM_NAME);
		// }

		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			double start = System.nanoTime();
			event.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * INSERT WATCH_RECORD 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */
	private int insertWatchRecord(List<String> result, int total, boolean insert) {
		WatchRecordVO vo = new WatchRecordVO();
		vo.setAll(vo, result);
		
		// I20 데이터 수집 안함
		/*
		if (Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())) {
			return total;
		}
		*/
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getCatGb());
			sb.append(vo.getAlbumId());
			String key = sb.toString();

			if (watchKeySet.contains(key)) {
				return total;
			} else {
				watchrecord.insertAll(vo, watch_date);
				watchKeySet.add(key);

			}
		}
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			double start = System.nanoTime();
			watchrecord.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	private void deleteWatchRecordTotal() {
		WatchRecordVO vo = new WatchRecordVO();
		watchrecord.delete(vo);
		for(int i=0;i<3;i++){
			watchrecord.executeDeleteBatch();
			System.out.println("WATHCH_RECORD DROP PARTITION 완료");
		}
	}

	/**
	 * INSERT WATCH_RECORD 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private void insertWatchRecordTotal(boolean insert) {
		HashMap<String, Integer> Totalmap = new HashMap<String, Integer>();
		List<Integer> maxmin = new ArrayList<Integer>();
		Totalmap = watchrecordtotal.select();
		if (Totalmap.size() != 0) {
			for (String key : Totalmap.keySet()) {
				maxmin.add(Totalmap.get(key));
			}

			int max = Collections.max(maxmin);
			int min = Collections.min(maxmin);
			int div = Integer.parseInt(Constant.OPTION.SECTIONOPTION);
			int section = (max - min) / div;
			// System.out.println("insertWatchRecordTotal max value:" + max);
			// System.out.println("insertWatchRecordTotal min value:" + min);
			int total = 0;
			for (String mapkey : Totalmap.keySet()) {
				String mapkeynew = mapkey;
				StringTokenizer st = new StringTokenizer(mapkeynew, "_");
				String catGb = "";
				String albumId = "";

				if (st.countTokens() == 2) {
					catGb = st.nextToken();
					albumId = st.nextToken();
					List<String> list = new ArrayList<String>();
					list.add(catGb);
					list.add(albumId);
					int wCnt = Totalmap.get(mapkey);
					int sectionint = wCnt / section + 1;
					if (sectionint == 101) {
						sectionint = sectionint - 1;
					}
					String sections = String.valueOf(sectionint);
					String watchCnt = String.valueOf(wCnt);
					list.add(watchCnt);
					list.add(sections);
					WatchRecordVOTotal vo = new WatchRecordVOTotal();
					vo.setAll(vo, list);
					watchrecordtotal.insertAll(vo);
					total++;
				} else {
					// System.out.println("tokenizer error: key:" + mapkey);
					// total++;
				}
			}
			watchrecordtotal.executeInsertBatch();
		}
	}

	/**
	 * INSERT I30 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private int insertI30(List<String> result, int total, boolean collectAutoComplete, boolean insert) {
		I30VO vo = new I30VO();
		vo.setAll(vo, result);

		// 20211005 보급형 TVG 데이터 UFX도 수집
		if (!(Constant.TYPE.IPTV_30.equalsIgnoreCase(vo.getCatGb()) || Constant.TYPE.IPTV_UFLIX.equalsIgnoreCase(vo.getCatGb()))) {
			return total;
		}

		mergerManager.updateI30(vo);
		
		if (insert) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.getResultType());
			sb.append(vo.getCatGb());
			sb.append(vo.getCatId());
			sb.append(vo.getAlbumId());
			// sb.append(vo.getThemeYn());
			String key = sb.toString();

			if (i30KeySet.contains(key)) {
				return total;
			} else {
				i30.insertAll(vo);
				i30KeySet.add(key);
			}

		}

		if (collectAutoComplete) {
			Map<String, List<String>> map = null;
			if (Constant.TYPE.IPTV_30.equalsIgnoreCase(vo.getCatGb())||Constant.TYPE.IPTV_UFLIX.equalsIgnoreCase(vo.getCatGb())) {
				map = i30AutoCompleteKeywordMap;
				// if(total%1000 == 0){
				// System.out.println(map.size() + "<<<" + vo.getCatGb());
				// }
			}
			// else if (Constant.TYPE.IPTV.equalsIgnoreCase(vo.getCatGb())) {
			// isTV = true;
			// map = iptvVodAutoCompleteKeywordMap;
			// if(total%1000 == 0){
			// System.out.println(map.size() + "<<<" + vo.getCatGb());
			// }
			// }
			// 자동완성 키워드는 따로 메모리에 모아둔다.
			// if(collectAutoComplete){
			// if(ConflictManager.isConflicted(vo.getCatName().toUpperCase(),
			// AUTO_COMPLETE_FIELD.CAT_NAME) == false)
			
			String nscGb = "";
			if(!StringUtil.isEmpty(vo.getNscGb()) && vo.getNscGb().equals("KID")) {
				nscGb = ","+vo.getNscGb();
			}
			String prInfor = "";
			if(!StringUtil.isEmpty(vo.getPrInfo())) {
				prInfor = vo.getPrInfo();
			}
			String section = "";
			if(!StringUtil.isEmpty(vo.getSection()) && vo.getSection().equals("LTE_PLUS")) {
				section = ","+vo.getSection();
			}
			addCntMapI30(map, vo.getCatName().toUpperCase(), AUTO_COMPLETE_FIELD.CAT_NAME, nscGb,prInfor,section);

			String normalizeAlbumName = Method.normalizeAutoCompleteTitle(vo.getAlbumName());
			// if(ConflictManager.isConflicted(normalizeAlbumName.toUpperCase(),
			// AUTO_COMPLETE_FIELD.ALBUM_NAME) == false)
			addCntMapI30(map, normalizeAlbumName.toUpperCase(), AUTO_COMPLETE_FIELD.ALBUM_NAME, nscGb,prInfor,section);

			String[] toks = vo.getActor().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACTOR) == false)
				addCntMapI30(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACTOR, nscGb,prInfor,section);
			}
			toks = vo.getOverseerName().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.OVERSEER_NAME) == false)
				addCntMapI30(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.OVERSEER_NAME, nscGb,prInfor,section);
			}
			toks = vo.getStarringActor().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.STARRING_ACTOR) == false)
				addCntMapI30(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.STARRING_ACTOR, nscGb,prInfor,section);
			}
/*			
			toks = vo.getKeyword().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.KEYWORD) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.KEYWORD);
			}
			toks = vo.getCastNameMax().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.CAST_NAME_MAX) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX);
			}
			toks = vo.getCastNameMaxEng().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG);
			}
			toks = vo.getActDispMax().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX);
			}
			toks = vo.getActDispMaxEng().split(",");
			for (String s : toks) {
				// if(ConflictManager.isConflicted(s.toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG) == false)
				addCntMap(map, s.trim().toUpperCase(), AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
				// addCntMap(vodAutoCompleteKeywordMap, s.trim().toUpperCase(),
				// AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG);
			}
			*/
		}

		
		total++;
		if (insert && total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			i30.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;

	}

	/**
	 * INSERT I30 추가 작업
	 * 
	 * @param result
	 * @param totalwl
	 * @return
	 */

	private int appendI30(List<String> result, int total) {
		I30VO vo = new I30VO();
		String dbWorkType = result.get(0);
		result.remove(0);
		vo.setAppend(vo, result);
		boolean ischeker = true;

		if (!(Constant.TYPE.IPTV_30.equalsIgnoreCase(vo.getCatGb()) || Constant.TYPE.IPTV_UFLIX.equalsIgnoreCase(vo.getCatGb()))) {
			return total;
		}

		if (DELIMS.UPDATE.equals(dbWorkType)) {
			ischeker = i30.isExist(dstConn, vo.getResultType(), vo.getCatGb(), vo.getCatId(), vo.getAlbumId());
			if(ischeker== false){
			i30.insertAll(vo);
			}else{
			i30.update(vo);
			}
		} else if (DELIMS.INSERT.equals(dbWorkType)) {
			i30.insertExistCheck(vo, dstConn);
		} else if (DELIMS.DELETE.equals(dbWorkType)) {
			i30.delete(vo);
		}

		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker== false) {
				i30.executeUpdateBatch();
			}else if (DELIMS.UPDATE.equals(dbWorkType)&& ischeker== true){
				i30.executeInsertBatch();
			}else if (DELIMS.INSERT.equals(dbWorkType)) {
				i30.executeInsertBatch();
			} else if (DELIMS.DELETE.equals(dbWorkType)) {
				i30.executeDeleteBatch();
			}
		}
		return total;
	}

	/**
	 * INSERT I30 채널 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private int insertI30Channel(List<String> result, int total) {
		I30ChannelVO vo = new I30ChannelVO();
		vo.setAll(vo, result);

		if (!(Constant.TYPE.FILTERING_CODE2.equalsIgnoreCase(vo.getFilteringCode())
				|| Constant.TYPE.FILTERING_CODE.equalsIgnoreCase(vo.getFilteringCode()))) {
			return total;
		}
		//

		i30channel.insertAll(vo);
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			channel.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}
	
	
	/**
	 * INSERT 아이들나라 특화메타 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private int insertkidsSpc(List<String> result, int total) {
		KidsSpcVO vo = new KidsSpcVO();
		vo.setAll(vo, result);

		//

		kidsClassSpc.insertAll(vo);
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			kidsClassSpc.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		
		return total;
	}
	
	
	/**
	 * INSERT 아이들나라 확장메타 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private int insertkidsExp(List<String> result, int total) {
		KidsEpxVO vo = new KidsEpxVO();
		vo.setAll(vo, result);

		kidsClassExp.insertAll(vo);
		total++;
		if (total % DB_INFO.BATCH_SIZE == 0) {
			// System.out.print(".");
			double start = System.nanoTime();
			kidsClassExp.executeInsertBatch();
			double end = System.nanoTime();
			totalInsertTime += (end - start);
		}
		return total;
	}

	/**
	 * 전체내용을 DB에 적재후 중복제거 처리된 자동완성 키워드를 적재 VIDEO 자동완성 작업
	 */
	private void insertVideoAutoComplete(Map<String, List<String>> map, VideoAutoCompleteDB db) {
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if ("".equals(key) || key == null)
				continue;

			List<String> list = map.get(key);
			String count = list.get(0);
			String keywordType = list.get(1);
			String val = list.get(2);
			String searchCnt = "0";
			if (searchCntMap.containsKey(key)) {
				searchCnt = String.valueOf(searchCntMap.get(key));
			}

			VideoAutoCompleteVO vo = new VideoAutoCompleteVO();
			vo.setKeyword(val);
			vo.setCnt(count);
			vo.setSearchCnt(searchCnt);

			String[] toks = keywordType.split(",");
			for (String s : toks) {
				if ("".equals(s) || s == null)
					continue;

				if (s.equals(AUTO_COMPLETE_FIELD.CAT_NAME)) {
					vo.setIsCatName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ALBUM_NAME)) {
					vo.setIsAlbumName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACTOR)) {
					vo.setIsActor("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.OVERSEER_NAME)) {
					vo.setIsOverseerName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.STARRING_ACTOR)) {
					vo.setIsStarringActor("Y");
				}
			
				else if (s.equals(AUTO_COMPLETE_FIELD.KEYWORD)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX)) {
					vo.setIsCastNameMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG)) {
					vo.setIsCastNameMaxEng("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX)) {
					vo.setIsActDispMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG)) {
					vo.setIsActDispMaxEng("Y");
				}
		
			}
			// 20160617 자동완성 초성 추가
			vo.setKeywordChosung(Method.normalizeChosung(val));

			db.insertAll(vo);
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		// System.out.println();
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
		// System.out.println();
	}

	//20180629 Choihu 공연 자동완성
	private void insertShowAutoComplete(ArrayList<ShowAutoCompleteVO> showAutoCompleteKeywordMap2,
			ShowAutoCompleteDB db) {
		System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < showAutoCompleteKeywordMap2.size(); i++) {
			db.insertAll(showAutoCompleteKeywordMap2.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	
	//20181203 Choihu VR 자동완성
	private void insertVrAutoComplete(ArrayList<VrAutoCompleteVO> VrAutoCompleteKeywordMap,
			VrAutoCompleteDB db) {
		/*System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		*/
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < VrAutoCompleteKeywordMap.size(); i++) {
			db.insertAll(VrAutoCompleteKeywordMap.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	//20181203 Choihu AR 자동완성
	private void insertArAutoComplete(ArrayList<ArAutoCompleteVO> ArAutoCompleteKeywordList,
			ArAutoCompleteDB db) {
		/*System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		*/
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < ArAutoCompleteKeywordList.size(); i++) {
			db.insertAll(ArAutoCompleteKeywordList.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	
	//20190115 Choihu 골프2 자동완성
	private void insertGolf2AutoComplete(ArrayList<Golf2AutoCompleteVO> AutoCompleteKeywordList,
			Golf2AutoCompleteDB db) {
		/*System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		*/
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < AutoCompleteKeywordList.size(); i++) {
			db.insertAll(AutoCompleteKeywordList.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	//20190115 Choihu 아이들나라 자동완성
	private void insertKidsAutoComplete(ArrayList<KidsAutoCompleteVO> AutoCompleteKeywordList,
			KidsAutoCompleteDB db) {
		/*System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		*/
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < AutoCompleteKeywordList.size(); i++) {
			db.insertAll(AutoCompleteKeywordList.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	
	//20210923 dhkim 아이들나라클래스 자동완성
	private void insertKidsClassAutoComplete(ArrayList<KidsClassAutoCompleteVO> AutoCompleteKeywordList,
			KidsClassAutoCompleteDB db) {
		/*System.out.println("DELETE " + db.TABLE + " COLUMN FIELD (P,G)...");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"P");
		showAutoComplete.deleteShowData(DBConnector.createDeletePstmt(dstConn, db.TABLE, new String[] {"FIELD"}),"G");
		*/
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		
		for (int i = 0; i < AutoCompleteKeywordList.size(); i++) {
			db.insertAll(AutoCompleteKeywordList.get(i));
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
	}
	
	/**
	 * 전체내용을 DB에 적재후 중복제거 처리된 자동완성 키워드를 적재 UFLIX 자동완성 추가
	 */
	private void insertUflixAutoComplete(Map<String, List<String>> map, UflixAutoCompleteDB db) {
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if ("".equals(key) || key == null)
				continue;

			List<String> list = map.get(key);
			String count = list.get(0);
			String keywordType = list.get(1);
			String val = list.get(2);
			String searchCnt = "0";
			if (searchCntMap.containsKey(key)) {
				searchCnt = String.valueOf(searchCntMap.get(key));
			}

			UflixAutoCompleteVO vo = new UflixAutoCompleteVO();
			vo.setKeyword(val);
			vo.setCnt(count);
			vo.setSearchCnt(searchCnt);

			String[] toks = keywordType.split(",");
			for (String s : toks) {
				if ("".equals(s) || s == null)
					continue;

				if (s.equals(AUTO_COMPLETE_FIELD.CAT_NAME)) {
					vo.setIsCatName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ALBUM_NAME)) {
					vo.setIsAlbumName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACTOR)) {
					vo.setIsActor("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.OVERSEER_NAME)) {
					vo.setIsOverseerName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.STARRING_ACTOR)) {
					vo.setIsStarringActor("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.KEYWORD)) {
					vo.setIsKeyword("Y");

				} else if (s.equals(AUTO_COMPLETE_FIELD.TITLE_ENG)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.DIRECTOR_ENG)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.PLAYER_ENG)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_ENG)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.TITLE_ORIGIN)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.WRITER_ORIGIN)) {
					vo.setIsKeyword("Y");

				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX)) {
					vo.setIsCastNameMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG)) {
					vo.setIsCastNameMaxEng("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX)) {
					vo.setIsActDispMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG)) {
					vo.setIsActDispMaxEng("Y");
				}
			}

			// 20160617 자동완성 초성 추가
			vo.setKeywordChosung(Method.normalizeChosung(val));

			db.insertAll(vo);
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		// System.out.println();
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
		// System.out.println();
	}

	/**
	 * INSERT I30 자동완성 작업
	 * 
	 * @param result
	 * @param total
	 * @return
	 */

	private void insertI30Complete(Map<String, List<String>> map, I30AutoCompleteDB db) {
		System.out.println("INSERT " + db.TABLE + "...");
		int total = 0;
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if ("".equals(key) || key == null)
				continue;

			List<String> list = map.get(key);
			String count = list.get(0);
			String keywordType = list.get(1);
			String val = list.get(2);
			String prInfor = list.get(3);
			String nscGb = list.get(4);
			String section = list.get(5);
			String searchCnt = "0";
			if (searchCntMap.containsKey(key)) {
				searchCnt = String.valueOf(searchCntMap.get(key));
			}

			I30AutoCompleteVO vo = new I30AutoCompleteVO();
			vo.setKeyword(val);
			vo.setCnt(count);
			vo.setSearchCnt(searchCnt);
			
			if(!StringUtil.isEmpty(nscGb)){
				nscGb = "Y" ;
			}

			if(!StringUtil.isEmpty(section)){
				section = "Y" ;
			}
			vo.setIsNscGb(nscGb);
			vo.setPrInfo(prInfor);
			vo.setSection(section);

			String[] toks = keywordType.split(",");
			for (String s : toks) {
				if ("".equals(s) || s == null)
					continue;

				if (s.equals(AUTO_COMPLETE_FIELD.CAT_NAME)) {
					vo.setIsCatName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ALBUM_NAME)) {
					vo.setIsAlbumName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACTOR)) {
					vo.setIsActor("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.OVERSEER_NAME)) {
					vo.setIsOverseerName("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.STARRING_ACTOR)) {
					vo.setIsStarringActor("Y");
				}
				
				else if (s.equals(AUTO_COMPLETE_FIELD.KEYWORD)) {
					vo.setIsKeyword("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX)) {
					vo.setIsCastNameMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.CAST_NAME_MAX_ENG)) {
					vo.setIsCastNameMaxEng("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX)) {
					vo.setIsActDispMax("Y");
				} else if (s.equals(AUTO_COMPLETE_FIELD.ACT_DISP_MAX_ENG)) {
					vo.setIsActDispMaxEng("Y");
				}
				
			}
			// 20160617 자동완성 초성 추가
			
			vo.setKeywordChosung(Method.normalizeChosung(val));

			db.insertAll(vo);
			if (total % DB_INFO.BATCH_SIZE == 0) {
				// System.out.print(".");
				double start = System.nanoTime();
				db.executeInsertBatch();
				double end = System.nanoTime();
				totalInsertTime += (end - start);
			}
			total++;
		}
		// System.out.println();
		System.out.println("COMPLETE [" + db.TABLE + "] TOTAL CNT:" + total);
		double start = System.nanoTime();
		db.executeInsertBatch();
		double end = System.nanoTime();
		totalInsertTime += (end - start);
		// System.out.println();
	}

	public void updateBuild() {
	}

	private void addCntMap(Map<String, List<String>> map, String val, String type) {
		addCntMap(map, val, type, false);
	}
	
	private void addCntMapI30(Map<String, List<String>> map, String val, String type, String nsc, String prInfor, String section) {
		// val = 무한도전 , type = ALBUM_NAME
		addCntMap(map, val, type, false);
		String key = "";
		if (val != null) {
			key = val.replaceAll(" ", "");
		}
		if(key != null && map !=null){
			if (key.length() > 1) {
				if (map.containsKey(key)) {
					List<String> list = map.get(key);
					// 무한도전 (1,cat_name,무한도전 )
					
					if (list.size() > 3)
					{
						if (!list.get(3).isEmpty())
						{
							list.set(3, prInfor);
						}
					}
					else
					{
						list.add(3, prInfor);
					}
					if (list.size() > 4)	// nsc값이 있다
					{
						if (nsc.equals(",KID"))
						{
							list.set(4, nsc);
						}
					}
					else
					{
						list.add(4, nsc);
					}
					
					if (list.size() > 5)	// nsc값이 있다
					{
						if (nsc.equals(",LTE_PLUS"))
						{
							list.set(5, section);
						}
					}
					else
					{
						list.add(5, section);
					}
					
				}
			}
		}
	}

	/**
	 * 자동완성 키워드 맵의 cnt 수를 증분시켜준다.
	 * 
	 * @param map
	 * @param val
	 * @param type
	 * @param searchCnt
	 */
	private void addCntMap(Map<String, List<String>> map, String val, String type, boolean isTV) {
		val = val.trim();

		String key = "";

		if (val != null) {
			key = val.replaceAll(" ", "");
		}
		
		if(key !=null && map !=null){
			if (key.length() > 1) {
	
				// if(ConflictManager.isConflicted(val, type, isTV) == true) {
				// List<String> list = map.get(val);
				// if(list != null) {
				// int cnt = Integer.parseInt(list.get(0));
				// list.set(0, String.valueOf(++cnt));
				// }
				// }
				// else {
				 // 2 album_name 무한도전
				
				if (map.containsKey(key)) {
					List<String> list = map.get(key);
					int cnt = Integer.parseInt(list.get(0));
					list.set(0, String.valueOf(++cnt));
	
					//cat_name
					String preTypes = list.get(1);
					StringBuffer sb = new StringBuffer();
					String[] toks = preTypes.split(",");
					boolean add = true;
					for (String s : toks) {
						if (type.equals(s)) {
							add = false;
						}
						sb.append(s);
						sb.append(",");
					}
					if (add) {
						sb.append(type);
						list.set(1, sb.toString());
					} else {
						String s = sb.toString();
						list.set(1, s.substring(0, s.length() - 1));
					}
	
					list.set(2, val);
				} else {
					List<String> list = new ArrayList<String>();
					list.add("1");
					list.add(type);
					list.add(val);
					map.put(key, list);
					// 무한도전 (1,cat_name,무한도전 )
					//무한도전 (2,cat_name,album_name,무한도전)
				}
				// }
			}
		}
}
	
	private String normalizeFileLine(String s) {
		return s.replaceAll("\\\\", "\\\\\\\\");
	}

	/**
	 * java 의 split 은 구분자와 공백이 여러개 있을때 한개로 쪼개기 때문에 새로 작성
	 * 
	 * java split a,b,,,c >>>> [a,b,c] customSplit a,b,,,c >>>>[a,b,,,c]
	 * 
	 * @param s
	 * @param delim
	 * @return
	 */
	private List<String> customSplit(String s, String delim) {
		// List<String> list = new ArrayList<String>();
		// while(s.length() != 0){
		// int idx = s.indexOf(delim);
		// String node = s.substring(0, idx);
		// list.add(node);
		// String tmp = s.substring(idx+delim.length(), s.length());
		// s = tmp;
		// }
		// return list;
		List<String> list = new ArrayList<String>();
		while (s.length() != 0) {
			int idx = s.indexOf(delim);
			if (idx == -1) {
				list.add(s);
				s = "";
			} else {
				String node = s.substring(0, idx);
				list.add(node);
				String tmp = s.substring(idx + delim.length(), s.length());
				s = tmp;
			}
		}
		return list;
	}



}
