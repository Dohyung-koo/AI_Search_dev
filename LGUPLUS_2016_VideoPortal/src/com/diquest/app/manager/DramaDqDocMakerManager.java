package com.diquest.app.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.app.common.Constant.DB_NAME;
import com.diquest.app.common.Constant.DISA;
import com.diquest.app.common.Constant.DRAMA;
import com.diquest.app.common.Method;
import com.diquest.db.EventDB;
import com.diquest.db.HighLightDB;
import com.diquest.db.HitUccDB;
import com.diquest.db.NaverNewsEntDB;
import com.diquest.db.UflixAutoCompleteDB;
import com.diquest.db.UflixPackageDB;
import com.diquest.db.UflixSeriesDB;
import com.diquest.db.VideoAutoCompleteDB;
import com.diquest.db.VodDB;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.EventVO;
import com.diquest.db.entity.HighLightVO;
import com.diquest.db.entity.HitUccVO;
import com.diquest.db.entity.NaverNewsEntVO;
import com.diquest.db.entity.UflixAutoCompleteVO;
import com.diquest.db.entity.UflixPackageVO;
import com.diquest.db.entity.UflixSeriesVO;
import com.diquest.db.entity.VideoAutoCompleteVO;
import com.diquest.db.entity.VodVO;
//import com.diquest.jiana.NLProcessConfiguration;
import com.diquest.plot.PLOT;
import com.diquest.plot.result.PLOTResult;
import com.diquest.util.FileUtil;
import com.diquest.util.StringUtil;

/**
 * Drama 색인시 필요한 DQDOC 을 만든다
 * 연관어 키워드 추출시 PLOT 사용 ---> 변경 20160608 사전을 만들어서 키워드를 추출한다 사전 시드는 제목및 감독 
 * 
 * @author hoho
 *
 */

public class DramaDqDocMakerManager {
	private final static int IDX_PLOT_DIC_DIR = 0;
	private final static int IDX_JIANA_DIC_DIR = 1;
	private final static int IDX_LANGUAGE = 2;
	private final static int IDX_KEYWORD_DIR = 3;
	
	private final static String PLOT_HOME = "/resources/plot/dic/korean/dcd";
	private final static String JIANA_HOME = "/resources/jiana/dic/korean/dcd";
	private final static String KEYWORD_DIR = "/resources/plot/dic/korean/dcd/keyword.txt";
	
//	private List<String> srcFile;
	private Connection srcConn;
	private DBConnector srcConnector;
	private NaverNewsEntDB naverNewsEnt;
	
	private VodDB vod;
	private HitUccDB hitUcc;
	private HighLightDB highLight;
	private EventDB event;
	private UflixPackageDB uflixPackage;
	private UflixSeriesDB uflixSeries;
	
	//DIC
	private BufferedWriter videoDicBw;
	private BufferedWriter uflixMobileDicBw;
	private BufferedWriter uflixTvgDicBw;
	private BufferedReader videoDicBr;
	private BufferedReader uflixMobileDicBr;
	private BufferedReader uflixTvgDicBr;
	
	//DQDOC
	private BufferedWriter videoBw;
	private BufferedWriter videoNaverBw;
	private BufferedWriter uflixMobileBw;
	private BufferedWriter uflixMobileNaverBw;
	private BufferedWriter uflixTvgBw;
	private BufferedWriter uflixTvgNaverBw;
	PLOT plot=null;
	
	private List<VodVO> vodList;
	private List<HitUccVO> hitUccList;
	private List<HighLightVO> highLightList;
	private List<EventVO> eventList;
	private List<UflixPackageVO> uflixPackageList;
	private List<UflixSeriesVO> uflixSeriesList;
	
	// 자동완성
	private VideoAutoCompleteDB videoAuto;
	private UflixAutoCompleteDB uflixMobileAuto;
	private UflixAutoCompleteDB uflixTvgAuto;
	
	private List<VideoAutoCompleteVO> videoAutoList;
	private List<UflixAutoCompleteVO> uflixMobileAutoList;
	private List<UflixAutoCompleteVO> uflixTvgAutoList;
	
	// 연관어 맵
	// 띄어쓰기 제거용(key : 공백 제거, value : 원본)
	private HashMap<String, String> relTermMap = new HashMap<String, String>(); 
	
	
//	private DicManager dic;
	
	public DramaDqDocMakerManager(){
		Constant.init();

		
//		srcFile = new ArrayList<String>();
//		srcFile.add(DATA.VOD_DATA);
//		srcFile.add(DATA.HIT_UCC_DATA);
//		srcFile.add(DATA.HIGHLIGHT_DATA);
//		srcFile.add(DATA.EVENT_DATA);
//		
//		srcFile.add(DATA.UFLIX_PACKAGE_DATA);
//		srcFile.add(DATA.UFLIX_SERIES_DATA);
		
		FileUtil.makeFolder(Constant.DIC.HOME);
		FileUtil.makeFolder(DRAMA.VIDEO_DST_DIR);
		FileUtil.makeFolder(DRAMA.UFLIX_MOBILE_DST_DIR);
		FileUtil.makeFolder(DRAMA.UFLIX_TVG_DST_DIR);
		FileUtil.deleteFolderContents(Constant.DIC.HOME);
		FileUtil.deleteFolderContents(DRAMA.VIDEO_DST_DIR);
		FileUtil.deleteFolderContents(DRAMA.UFLIX_MOBILE_DST_DIR);
		FileUtil.deleteFolderContents(DRAMA.UFLIX_TVG_DST_DIR);
		
		videoDicBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.VIDEO + ".ks");
		uflixMobileDicBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.UFLIX_MOBILE + ".ks");
		uflixTvgDicBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.UFLIX_TVG + ".ks");
		videoDicBr = FileUtil.getBufferedReader(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.VIDEO + ".ks");
		uflixMobileDicBr = FileUtil.getBufferedReader(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.UFLIX_MOBILE + ".ks");
		uflixTvgDicBr = FileUtil.getBufferedReader(FileUtil.appendEndsPath(Constant.DIC.HOME) + Constant.DIC.UFLIX_TVG + ".ks");
		
		videoBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.VIDEO_DST_DIR) + "video.UTF-8");
		videoNaverBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.VIDEO_DST_DIR) + "naver.UTF-8");
		uflixMobileBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.UFLIX_MOBILE_DST_DIR) + "uflixMobile.UTF-8");
		uflixTvgBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.UFLIX_TVG_DST_DIR) + "uflixTvg.UTF-8");
		uflixMobileNaverBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.UFLIX_MOBILE_DST_DIR) + "naver.UTF-8");
		uflixTvgNaverBw = FileUtil.getBufferedWriter(FileUtil.appendEndsPath(DRAMA.UFLIX_TVG_DST_DIR) + "naver.UTF-8");
		
		openDB();
	}
	
	private static class Singleton {
		private static final DramaDqDocMakerManager instance = new DramaDqDocMakerManager();
	}
	
	public static DramaDqDocMakerManager getInstance() {
		return Singleton.instance;
	}
	
	public void openDB(){
		System.out.println("OPEN DB [" + DB_INFO.URL + "]");
		String url = DB_INFO.URL;
		String user = DB_INFO.USER;
		String password = DB_INFO.PASSWORD;
		String driver = DB_INFO.DRIVER;
		srcConnector = new DBConnector(url,user,password,driver);
		srcConn = srcConnector.getConnection();
		naverNewsEnt = new NaverNewsEntDB(DB_NAME.NAVER_NEWS_ENT, true);
		naverNewsEnt.createSelectPstmt(srcConn);
		vod = new VodDB(DB_NAME.VOD, true);
		vod.createSelectPstmt(srcConn);
		hitUcc = new HitUccDB(DB_NAME.HIT_UCC, true);
		hitUcc.createSelectPstmt(srcConn);
		highLight = new HighLightDB(DB_NAME.HIGHLIGHT, true);
		highLight.createSelectPstmt(srcConn);
		event = new EventDB(DB_NAME.EVENT, true);
		event.createSelectPstmt(srcConn);
		uflixPackage = new UflixPackageDB(DB_NAME.UFLIX_PACKAGE, true);
		uflixPackage.createSelectPstmt(srcConn);
		uflixSeries = new UflixSeriesDB(DB_NAME.UFLIX_SERIES, true);
		uflixSeries.createSelectPstmt(srcConn);
		
		videoAuto = new VideoAutoCompleteDB(DB_NAME.VIDEO_AUTO_COMPLETE, true);
		videoAuto.createSelectPstmt(srcConn);
		uflixMobileAuto = new UflixAutoCompleteDB(DB_NAME.UFLIX_MOBILE_AUTO_COMPLETE, true);
		uflixMobileAuto.createSelectPstmt(srcConn);
		uflixTvgAuto = new UflixAutoCompleteDB(DB_NAME.UFLIX_TVG_AUTO_COMPLETE, true);
		uflixTvgAuto.createSelectPstmt(srcConn);
		
	}
	
	/**
	 * PLOT 을 초기화한다. 
	 * 
	 * @param	plotDicFolder  : 
	 * @param	jianaDicFolder : 
	 */
	public void plotInit(List<String> keywordList) {
		String disaHomeDir = FileUtil.appendEndsPath(DISA.HOME);
		String[] params = new String[]{disaHomeDir+PLOT_HOME, disaHomeDir+JIANA_HOME, "KOREAN", disaHomeDir+KEYWORD_DIR};
		
		if (params.length < 3){
			String[] newParams = new String[3];
			System.arraycopy(params, 0, newParams, 0, 2);
			newParams[IDX_LANGUAGE] = "KOREAN";
			params = newParams;
		}
		plot = PLOT.getInstance(params[IDX_LANGUAGE]); 
		plot.init(params[IDX_PLOT_DIC_DIR], params[IDX_JIANA_DIC_DIR]);
		plot.setDisaCategory(Constant.DISA.CATEGORY);
		
		if(params.length == 4){
			try {
				plot.keywordCompile(keywordList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void selectWorkList(){
		System.out.println("Select Work List ...");
		vodList = vod.select();
		hitUccList = hitUcc.select();
		highLightList = highLight.select();
		eventList = event.select();
		uflixPackageList = uflixPackage.select();
		uflixSeriesList = uflixSeries.select();
		
		videoAutoList = videoAuto.select();
		uflixMobileAutoList = uflixMobileAuto.select();
		uflixTvgAutoList = uflixTvgAuto.select();
	}
	
	/**
	 * PLOT 사전에 필요한 ks 파일을 만든다.
	 */
	public void makeVodKS(){
		System.out.println("MAKE VOD KS ...");
		Set<String> dicSet = new HashSet<String>();
		/*for(int i=0,iSize = vodList.size() ; i<iSize ; i++){
			VodVO vo = vodList.get(i);
//			dicSet.add(Method.normalizeKeywordDic(vo.getCastName()));
			dicSet.add(Method.normalizeKeywordDic(vo.getAlbumName()));
			addSplitResult(dicSet, vo.getActor());
			addSplitResult(dicSet, vo.getOverseerName());
			addSplitResult(dicSet, vo.getStarringActor());
			addSplitResult(dicSet, vo.getKeyword());
//			addSplitResult(dicSet, vo.getCastNameMax());
//			addSplitResult(dicSet, vo.getCastNameMaxEng());
//			addSplitResult(dicSet, vo.getActDispMax());
//			addSplitResult(dicSet, vo.getActDispMaxEng());
		}
		
		for(int i=0,iSize = hitUccList.size() ; i<iSize ; i++){
			HitUccVO vo = hitUccList.get(i);
			dicSet.add(Method.normalizeKeywordDic(vo.getContentsName()));
		}
		
		for(int i=0,iSize = highLightList.size() ; i<iSize ; i++){
			HighLightVO vo = highLightList.get(i);
			dicSet.add(Method.normalizeKeywordDic(vo.getContentsName()));
		}
		
		for(int i=0,iSize = eventList.size() ; i<iSize ; i++){
			EventVO vo = eventList.get(i);
			dicSet.add(Method.normalizeKeywordDic(vo.getTitle()));
		}*/
		for(int i=0,iSize = videoAutoList.size() ; i<iSize ; i++){
			VideoAutoCompleteVO vo = videoAutoList.get(i);
			dicSet.add(Method.normalizeKeywordDic(vo.getKeyword()));
		}
		
		//KS 기록
		try {
			for(String s : dicSet){
				if("".equals(s) || s == null)
					continue;
				videoDicBw.write(s);
				videoDicBw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(videoDicBw);
	}
	
	/**
	 * PLOT 사전에 필요한 ks 파일을 만든다.
	 */
	public void makeUflixKs(){
		System.out.println("MAKE UFLIX KS ...");
		Set<String> mobileDicSet = new HashSet<String>();
		Set<String> tvgDicSet = new HashSet<String>();
		
		/*for(int i=0,iSize = uflixPackageList.size() ; i<iSize ; i++){
			UflixPackageVO vo = uflixPackageList.get(i);
			Set<String> set = null;
			String type = vo.getCatGb();
			if(type == null)
				continue;
			
			if(type.equals(Constant.TYPE.UFLIX_CATGB_MOBILE))
				set = mobileDicSet; 
			else if(type.equals(Constant.TYPE.UFLIX_CATGB_TV))
				set = tvgDicSet;
			
//			set.add(Method.normalizeKeywordDic(vo.getCatName()));
			set.add(Method.normalizeKeywordDic(vo.getAlbumName()));
			addSplitResult(set, vo.getActor());
			addSplitResult(set, vo.getOverseerName());
			addSplitResult(set, vo.getStarringActor());
			addSplitResult(set, vo.getKeyword());
			addSplitResult(set, vo.getTitleEng());
			addSplitResult(set, vo.getDirectorEng());
			addSplitResult(set, vo.getPlayerEng());
//			addSplitResult(set, vo.getCastNameEng());
//			addSplitResult(set, vo.getCastName());
			addSplitResult(set, vo.getTitleOrigin());
			addSplitResult(set, vo.getWriterOrigin());
//			addSplitResult(set, vo.getCastNameMax());
//			addSplitResult(set, vo.getCastNameMaxEng());
//			addSplitResult(set, vo.getActDispMax());
//			addSplitResult(set, vo.getActDispMaxEng());
		}
		
		for(int i=0,iSize = uflixSeriesList.size() ; i<iSize ; i++){
			UflixSeriesVO vo = uflixSeriesList.get(i);
			Set<String> set = null;
			String type = vo.getCatGb();
			
			if(type == null)
				continue;
			
			if(type.equals(Constant.TYPE.UFLIX_CATGB_MOBILE))
				set = mobileDicSet; 
			else if(type.equals(Constant.TYPE.UFLIX_CATGB_TV))
				set = tvgDicSet;
			
			set.add(Method.normalizeKeywordDic(vo.getName()));
			addSplitResult(set, vo.getActor());
			addSplitResult(set, vo.getOverseerName());
			addSplitResult(set, vo.getStarringActor());
			addSplitResult(set, vo.getKeyword());
			addSplitResult(set, vo.getTitleEng());
			addSplitResult(set, vo.getDirectorEng());
			addSplitResult(set, vo.getPlayerEng());
//			addSplitResult(set, vo.getCastNameEng());
//			addSplitResult(set, vo.getCastName());
			addSplitResult(set, vo.getTitleOrigin());
			addSplitResult(set, vo.getWriterOrigin());
//			addSplitResult(set, vo.getCastNameMax());
//			addSplitResult(set, vo.getCastNameMaxEng());
			addSplitResult(set, vo.getActDispMax());
			addSplitResult(set, vo.getActDispMaxEng());
		}*/
		
		for(int i=0,iSize = uflixMobileAutoList.size() ; i<iSize ; i++){
			UflixAutoCompleteVO vo = uflixMobileAutoList.get(i);
			Set<String> set = mobileDicSet;
			set.add(Method.normalizeKeywordDic(vo.getKeyword()));
		}
		
		for(int i=0,iSize = uflixTvgAutoList.size() ; i<iSize ; i++){
			UflixAutoCompleteVO vo = uflixTvgAutoList.get(i);
			Set<String> set = tvgDicSet;
			set.add(Method.normalizeKeywordDic(vo.getKeyword()));
		}
		
		try {
			for(String s : mobileDicSet){
				if("".equals(s) || s == null)
					continue;
				uflixMobileDicBw.write(s);
				uflixMobileDicBw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(uflixMobileDicBw);
		try {
			for(String s : tvgDicSet){
				if("".equals(s) || s == null)
					continue;
				uflixTvgDicBw.write(s);
				uflixTvgDicBw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedWriter(uflixTvgDicBw);
	}
	
	/**
	 * 단순 SET 형식 사전은 사용안함
	 */
	
	@Deprecated
	public void makeDic(){
//		System.out.println("MAKE DIC ...");
//		NormalizeDic maker = new NormalizeDic();
//		maker.makeDic(Constant.DIC.HOME, Constant.DIC.VIDEO);
//		maker.makeDic(Constant.DIC.HOME, Constant.DIC.UFLIX_MOBILE);
//		maker.makeDic(Constant.DIC.HOME, Constant.DIC.UFLIX_TVG);
//		
//		dic = DicManager.getInstance();
//		dic.setDicDir(Constant.DIC.HOME);
	}
	
	public void makeDqDoc() throws IOException {
		int total = 0;
		int videoDramaIdx = 0;
		int uflixDramaIdx = 0;
		
//FILE 을 읽어서 만드는 모드		
//		for (int i = 0; i < srcFile.size(); i++){
//			BufferedReader br = FileUtil.getBufferedReader(srcFile.get(i), "euc-kr");
//			File f = new File(srcFile.get(i));
//			String fileName = f.getAbsoluteFile().getName();
//			fileName = fileName.substring(0, fileName.indexOf("."));
//			
//			String line = "";
//			int splitSize = 0;
//			StringBuffer sb =  new StringBuffer();
//			System.out.println("WORK [" + f.getAbsoluteFile() + "]...");
//			while(( line = br.readLine())!= null){
//				total++;
//				line = normalize(line);
//				//마지막을 의미하는 문자가 있을경우
//				if(line.contains(STRING.ENDLINE)){
//					
//					sb.append(line);
//					line = sb.toString();
//					
//					// 마지막을 의미하는 문자 기준으로 앞부분(a) 만 처리후 뒷부분(b) 은 다음 문장에서 이용
//					int idx = line.indexOf(STRING.ENDLINE);
//					String a = line.substring(0, idx);
//					String b = line.substring(idx + STRING.ENDLINE.length() , line.length());
//					List<String> result = customSplit(a, STRING.DELIM);
//					splitSize = result.size();
//					
//					if(FILE_NAME.VOD.equals(fileName) && splitSize == SPLIT_SIZE.VOD){
//						writeVod(result, ++videoDramaIdx);
//					} else if(FILE_NAME.HIT_UCC.equals(fileName) && splitSize == SPLIT_SIZE.HIT_UCC){
//						writeHitUcc(result, ++videoDramaIdx);
//					} else if(FILE_NAME.HIGHLIGHT.equals(fileName) && splitSize == SPLIT_SIZE.HIGHLIGHT){
//						writeHighLight(result, ++videoDramaIdx);
//					} else if(FILE_NAME.EVENT.equals(fileName) && splitSize == SPLIT_SIZE.EVENT){
//						writeEvnet(result, ++videoDramaIdx);
//					} else if(FILE_NAME.UFLIX_PACKAGE.equals(fileName) && splitSize == SPLIT_SIZE.UFLIX_PACKAGE ){
//						writeUflixPackage(result, ++uflixDramaIdx);
//					} else if(FILE_NAME.UFLIX_SERIES.equals(fileName) && splitSize == SPLIT_SIZE.UFLIX_SERIES){
//						writeUflixSeries(result, ++uflixDramaIdx);
//					}
//					//처리가 끝난후 뒷부분 버퍼에 담음 
//					sb = new StringBuffer();
//					sb.append(b);
//					
//				//마지막을 의미하는 문자가 없을 경우
//				} else {	
//					sb.append(line);
//				}
//			}
//			
//			FileUtil.closeBufferedReader(br);
//			System.out.println("COMPLETE [" + srcFile.get(i) + "] TOTAL CNT:" + total);
//			System.out.println();
//			total = 0;
//		}
		
		plotInit(getKeywordDicList(Constant.DIC.VIDEO));
		
		//DB를 읽어서 만드는 모드		
		System.out.println("WRITE VOD...");
		for(int i=0,iSize = vodList.size() ; i<iSize ; i++){
			total++;
			VodVO vo = vodList.get(i);
			// 내부에서 dq_doc을 2개 만듦
			videoDramaIdx += 2;
			writeVod(vo, videoDramaIdx);
		}
		
		System.out.println("WRITE HITUCC...");
		for(int i=0,iSize = hitUccList.size() ; i<iSize ; i++){
			total++;
			HitUccVO vo = hitUccList.get(i);
			writeHitUcc(vo, ++videoDramaIdx);
		}
		
		System.out.println("WRITE HIGHLIGHT...");
		for(int i=0,iSize = highLightList.size() ; i<iSize ; i++){
			total++;
			HighLightVO vo = highLightList.get(i);
			writeHighLight(vo, ++videoDramaIdx);
		}
		
		System.out.println("WRITE EVENT...");
		for(int i=0,iSize = eventList.size() ; i<iSize ; i++){
			total++;
			EventVO vo = eventList.get(i);
			writeEvent(vo, ++videoDramaIdx);
		}
		
//		//NAVER 뉴스는 항상 DB
//		List<NaverNewsEntVO> newsList = naverNewsEnt.selectWeek(srcConn);
//		
//		//각해당키워드로 plot 사전을 구축한다
//		//사전이 무엇으로 구성되는지 관리용으로 파일을 만들어넣고 사용
////				plotInit(getKeywordDicList(Constant.DIC.VIDEO));
//		System.out.println("WRITE VIDEO NAVERNEWS...");
//		for(int i=0,iSize = newsList.size() ; i<iSize ; i++){
//			total++;
//			NaverNewsEntVO vo = newsList.get(i);
//			writeNaverNews(vo, ++videoDramaIdx, Constant.DIC.VIDEO);
//			if(i%10000==0){
//				System.out.print(".");
//			}
//		}
//		System.out.println();
		plot.fine();//plot 종료
		
		plotInit(getKeywordDicList(Constant.DIC.UFLIX_MOBILE));
		System.out.println("WRITE UFLIXPACKAGE...");
		for(int i=0,iSize = uflixPackageList.size() ; i<iSize ; i++){
			total++;
			UflixPackageVO vo = uflixPackageList.get(i);
			// 내부에서 dq_doc을 2개 만듦
			uflixDramaIdx += 2;
			writeUflixPackage(vo, uflixDramaIdx);
		}
		
//		plotInit(getKeywordDicList(Constant.DIC.UFLIX_MOBILE)); 
//		System.out.println("WRITE UFLIX_MOBILE NAVERNEWS...");
//		for(int i=0,iSize = newsList.size() ; i<iSize ; i++){
//			total++;
//			NaverNewsEntVO vo = newsList.get(i);
//			writeNaverNews(vo, ++uflixDramaIdx, Constant.DIC.UFLIX_MOBILE);
//			if(i%10000==0){
//				System.out.print(".");
//			}
//		}
//		System.out.println();
		plot.fine();
		
		plotInit(getKeywordDicList(Constant.DIC.UFLIX_TVG));
		System.out.println("WRITE UFLIXSERIES...");
		for(int i=0,iSize = uflixSeriesList.size() ; i<iSize ; i++){
			total++;
			UflixSeriesVO vo = uflixSeriesList.get(i);
			// 내부에서 DQ_DOC을 2개 생성함
			uflixDramaIdx += 2;
			writeUflixSeries(vo, uflixDramaIdx);
			
		}
		
//		plotInit(getKeywordDicList(Constant.DIC.UFLIX_TVG));
//		System.out.println("WRITE UFLIX_TVG NAVERNEWS...");
//		for(int i=0,iSize = newsList.size() ; i<iSize ; i++){
//			total++;
//			NaverNewsEntVO vo = newsList.get(i);
//			writeNaverNews(vo, ++uflixDramaIdx, Constant.DIC.UFLIX_TVG);
//			if(i%10000==0){
//				System.out.print(".");
//			}
//		}
//		System.out.println();
		plot.fine();
		
		
		System.out.println("COMPLETE MAKE DRAMA DOC TOTAL DOC CNT:" + total);
	}
	
	// termMap을 모두 만든 이후 해당 정보를 저장하는 메서드
	// 저장은 File로 함
	public void makeTermData() {
		try {
			BufferedWriter bw = FileUtil.getBufferedWriter(Constant.DRAMA.HOME_DIR+Constant.DRAMA.TERM_MAP_FILE, "UTF8");
			Iterator<String> iter = relTermMap.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = relTermMap.get(key);
				bw.write(key+"\t"+value+"\n");
			}
			bw.flush();
			bw.close();
		}
		// 맵 파일 저장 실패. 실패만 알리고 계속 진행
		catch(IOException e) {
			System.out.println("DramaDqDocMakerManager : makeTermData : Fail to store relation term map!");
			e.printStackTrace();
		}
	}
	
	/**
	 * ks 파일로 만들어놓은 사전리스트를 PLOT 구축을 위해 가져온다.
	 * @param type
	 * @return
	 */
	private List<String> getKeywordDicList(String type) {
		List<String> list = new ArrayList<String>();
		String line = "";
		try {
			if(type.equals(Constant.DIC.VIDEO)){
				while((line = videoDicBr.readLine())!= null){
					list.add(line);
				}
				FileUtil.closeBufferedReader(videoDicBr);
				
			} else if(type.equals(Constant.DIC.UFLIX_MOBILE)){
				while((line = uflixMobileDicBr.readLine())!= null){
					list.add(line);
				}
				FileUtil.closeBufferedReader(uflixMobileDicBr);
				
			} else if(type.equals(Constant.DIC.UFLIX_TVG)){
				while((line = uflixTvgDicBr.readLine())!= null){
					list.add(line);
				}
				FileUtil.closeBufferedReader(uflixTvgDicBr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 20160530 뉴스 수집기사에서 CONTS 는 제외 TITLE 만 사용한다
	 *	PLOT 명사 추출 문자길이 한도는 약 2000
	 * 뉴스타이틀에서만 PLOT 추출을 이용한다   
	 * @param vo
	 * @param total
	 * @param bw
	 */
	private void writeNaverNews(NaverNewsEntVO vo, int total, String type){
		List<String> list = new ArrayList<String>();
//		List<String> contsList = null;
//		String conts = StringUtil.removeBigBrace(vo.getContsConts());
		String title = StringUtil.removeBigBrace(vo.getContsTitle());
//		int plotLimit = 2000;
		
		try {
//		if(conts!= null){
//			if(conts.length() > plotLimit){
//				contsList = new ArrayList<String>();
//				while(conts.indexOf(".") > 0){
//					String sFront = conts.substring(0, conts.indexOf("."));
//					contsList.add(sFront);
//					conts = conts.substring(conts.indexOf(".")+1, conts.length());
//				}
//				
//				for(String s : contsList){
//					addAnalyzeResult(list, s);
//				}
//			} else {
//				addAnalyzeResult(list, conts);
//			}
//		}
		
		addAnalyzeResult(list, title, type);
		if(type.equals(Constant.DIC.VIDEO)){
			writeDqDocBlock(list, total, videoNaverBw); 
		} else if(type.equals(Constant.DIC.UFLIX_MOBILE)){
			writeDqDocBlock(list, total, uflixMobileNaverBw); 
		} else if(type.equals(Constant.DIC.UFLIX_TVG)){
			writeDqDocBlock(list, total, uflixTvgNaverBw); 
		}
		
		} catch (Exception e){
			e.printStackTrace();
			System.out.println();
		}
	}
	
	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeVod(VodVO vo, int total) {
		List<String> list = new ArrayList<String>();
/*
 * //		list.add(Method.normalizeKeywordDic(vo.getCatName()));
		list.add(Method.normalizeKeywordDic(vo.getAlbumName()));
		addSplitResult(list, vo.getActor());
		addSplitResult(list, vo.getOverseerName());
		addSplitResult(list, vo.getStarringActor());
		addSplitResult(list, vo.getKeyword());
//		addSplitResult(list, vo.getCastNameMax());
//		addSplitResult(list, vo.getCastNameMaxEng());
		addSplitResult(list, vo.getActDispMax());
		addSplitResult(list, vo.getActDispMaxEng());
		
//		list.add(vo.getActor());
//		list.add(vo.getOverseerName());
//		list.add(vo.getStarringActor());
//		list.add(vo.getKeyword());
//		list.add(vo.getCastNameMax());
//		list.add(vo.getCastNameMaxEng());
//		list.add(vo.getActDispMax());
//		list.add(vo.getActDispMaxEng());
*/		
//		addAnalyzeResult(list, vo.getAlbumName(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getActor(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getOverseerName(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getStarringActor(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getKeyword(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getActDispMax(), Constant.DIC.VIDEO);
//		addAnalyzeResult(list, vo.getActDispMaxEng(), Constant.DIC.VIDEO);
//		writeDqDocBlock(list, total, videoBw);
		
		// 공백을 제거하면서 맵을 만드는 메서드 사용
		addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getAlbumName()).replace(':', ','));
		addSplitResult(list, vo.getActor());
		addSplitResult(list, vo.getOverseerName());
		addSplitResult(list, vo.getStarringActor());
		addSplitResult(list, vo.getKeyword());
		
		addSplitResult(list, vo.getTitleEng());
		addSplitResult(list, vo.getDirectorEng());
		addSplitResult(list, vo.getPlayerEng());
		addSplitResult(list, vo.getCastNameEng());
		addSplitResult(list, vo.getCastName());
		addSplitResult(list, vo.getTitleOrigin());
		addSplitResult(list, vo.getWriterOrigin());
		
//		addSplitResult(list, vo.getCastNameMax());
//		addSplitResult(list, vo.getCastNameMaxEng());
		addSplitResult(list, vo.getActDispMax());
		addSplitResult(list, vo.getActDispMaxEng());
		
		// 신규 추가
		addSplitResult(list, vo.getDescription());
		addSplitResult(list, vo.getVoiceActor());

		addAnalyzeResult(list, vo.getDescription(), Constant.DIC.VIDEO);
		addAnalyzeResult(list, vo.getKoficSynopsis(), Constant.DIC.VIDEO);
		
		writeDqDocBlock(list, total, videoBw);
		
		// 제목, 주연, 감독 DQ_DOC 추가
		list.clear();
		addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getAlbumName()).replace(':', ','));
		addSplitResult(list, vo.getStarringActor());
		addSplitResult(list, vo.getOverseerName());
		writeDqDocBlock(list, ++total, videoBw);
	}
	
	// term의 공백을 제거한 후 retern, 공백을 제거하기 전 원본을 Map에 저장
	private String removeNStoreTerm(String term) {
		String rmvTerm = term.replaceAll(" ", "");
		if(relTermMap.containsKey(rmvTerm)) {
			String prevTerm = relTermMap.get(rmvTerm);
			// 먼저 들어있던 것보다 새로 온게 더 길면 바꾼다.
			if(term.length() > prevTerm.length())
				relTermMap.put(rmvTerm, term);
		}
		// 처음 들어온 놈이면 그냥 넣는다.
		else
			relTermMap.put(rmvTerm, term);
		
		return rmvTerm;
	}
	
	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeHitUcc(HitUccVO vo, int total) {
		List<String> list = new ArrayList<String>();
//		list.add(Method.normalizeKeywordDic(vo.getContentsName()));
		addAnalyzeResult(list, vo.getContentsName(), Constant.DIC.VIDEO);
		writeDqDocBlock(list, total, videoBw);
	}

	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeHighLight(HighLightVO vo , int total) {
		List<String> list = new ArrayList<String>();
//		list.add(Method.normalizeKeywordDic(vo.getContentsName()));
		addAnalyzeResult(list, vo.getContentsName(), Constant.DIC.VIDEO);
		writeDqDocBlock(list, total, videoBw);
		
	}

	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeEvent(EventVO vo, int total) {
		List<String> list = new ArrayList<String>();
//		list.add(Method.normalizeKeywordDic(vo.getTitle()));
		addAnalyzeResult(list, vo.getTitle(), Constant.DIC.VIDEO);
		writeDqDocBlock(list, total, videoBw);
	}

	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeUflixPackage(UflixPackageVO vo, int total) {
		List<String> list = new ArrayList<String>();
		String type = vo.getCatGb();
		if(type == null)
			return;

//		list.add(Method.normalizeKeywordDic(vo.getCatName()));
//		list.add(Method.normalizeKeywordDic(vo.getAlbumName()));
//		list.add(Method.normalizeAutoCompleteTitle(vo.getAlbumName()));
		addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getAlbumName()).replace(':', ','));
		addSplitResult(list, vo.getActor());
		addSplitResult(list, vo.getOverseerName());
		addSplitResult(list, vo.getStarringActor());
		addSplitResult(list, vo.getKeyword());
		
		addSplitResult(list, vo.getTitleEng());
		addSplitResult(list, vo.getDirectorEng());
		addSplitResult(list, vo.getPlayerEng());
		addSplitResult(list, vo.getCastNameEng());
		addSplitResult(list, vo.getCastName());
		addSplitResult(list, vo.getTitleOrigin());
		addSplitResult(list, vo.getWriterOrigin());
		
//		addSplitResult(list, vo.getCastNameMax());
//		addSplitResult(list, vo.getCastNameMaxEng());
		addSplitResult(list, vo.getActDispMax());
		addSplitResult(list, vo.getActDispMaxEng());
		
		// 신규 추가
		addSplitResult(list, vo.getDescription());
		addSplitResult(list, vo.getVoiceActor());

		addAnalyzeResult(list, vo.getDescription(), type);
		addAnalyzeResult(list, vo.getKoficSynopsis(), type);
		
//		addAnalyzeResult(list, vo.getAlbumName(), type);
//		addAnalyzeResult(list, vo.getActor(), type);
//		addAnalyzeResult(list, vo.getOverseerName(), type);
//		addAnalyzeResult(list, vo.getStarringActor(), type);
//		addAnalyzeResult(list, vo.getKeyword(), type);
//		addAnalyzeResult(list, vo.getTitleEng(), type);
//		addAnalyzeResult(list, vo.getDirectorEng(), type);
//		addAnalyzeResult(list, vo.getPlayerEng(), type);
//		addAnalyzeResult(list, vo.getTitleOrigin(), type);
//		addAnalyzeResult(list, vo.getWriterOrigin(), type);
//		addAnalyzeResult(list, vo.getActDispMax(), type);
//		addAnalyzeResult(list, vo.getActDispMaxEng(), type);
		
		if(type.equals(Constant.TYPE.UFLIX_CATGB_MOBILE)) {
			writeDqDocBlock(list, total, uflixMobileBw);
			
			// 제목, 주연, 감독 DQ_DOC 추가
			list.clear();
			addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getAlbumName()).replace(':', ','));
			addSplitResult(list, vo.getStarringActor());
			addSplitResult(list, vo.getOverseerName());
			writeDqDocBlock(list, ++total, uflixMobileBw);
		}
		else if(type.equals(Constant.TYPE.UFLIX_CATGB_TV)) {
			writeDqDocBlock(list, total, uflixTvgBw);

			// 제목, 주연, 감독 DQ_DOC 추가
			list.clear();
			addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getAlbumName()).replace(':', ','));
			addSplitResult(list, vo.getStarringActor());
			addSplitResult(list, vo.getOverseerName());
			writeDqDocBlock(list, ++total, uflixTvgBw);
		}
	}
	/**
	 * 해당버퍼에 Dqdoc 내용 기록
	 * @param vo
	 * @param total
	 */
	private void writeUflixSeries(UflixSeriesVO vo, int total) {
		List<String> list = new ArrayList<String>();
		String type = vo.getCatGb();
		if(type == null)
			return;
	
		list.add(Method.normalizeKeywordDic(vo.getName()));
		addSplitResult(list, vo.getActor());
		addSplitResult(list, vo.getOverseerName());
		addSplitResult(list, vo.getStarringActor());
		addSplitResult(list, vo.getKeyword());
		
		addSplitResult(list, vo.getTitleEng());
		addSplitResult(list, vo.getDirectorEng());
		addSplitResult(list, vo.getPlayerEng());
		addSplitResult(list, vo.getCastNameEng());
		addSplitResult(list, vo.getCastName());
		addSplitResult(list, vo.getTitleOrigin());
		addSplitResult(list, vo.getWriterOrigin());
		
//		addSplitResult(list, vo.getCastNameMax());
//		addSplitResult(list, vo.getCastNameMaxEng());
		addSplitResult(list, vo.getActDispMax());
		addSplitResult(list, vo.getActDispMaxEng());
		
		// 신규 추가
		addSplitResult(list, vo.getSummary());
		addSplitResult(list, vo.getVoiceActor());
		
		addAnalyzeResult(list, vo.getDes(), type);
		addAnalyzeResult(list, vo.getKoficSynopsis(), type);
		
//		addAnalyzeResult(list, vo.getName(), type);
//		addAnalyzeResult(list, vo.getActor(), type);
//		addAnalyzeResult(list, vo.getOverseerName(), type);
//		addAnalyzeResult(list, vo.getStarringActor(), type);
//		addAnalyzeResult(list, vo.getKeyword(), type);
//		addAnalyzeResult(list, vo.getTitleEng(), type);
//		addAnalyzeResult(list, vo.getDirectorEng(), type);
//		addAnalyzeResult(list, vo.getPlayerEng(), type);
//		addAnalyzeResult(list, vo.getTitleOrigin(), type);
//		addAnalyzeResult(list, vo.getWriterOrigin(), type);
//		addAnalyzeResult(list, vo.getActDispMax(), type);
//		addAnalyzeResult(list, vo.getActDispMaxEng(), type);
		
		
		if(type.equals(Constant.TYPE.UFLIX_CATGB_MOBILE)) {
			writeDqDocBlock(list, total, uflixMobileBw);
			
			// 제목, 주연, 감독 DQ_DOC 추가
			list.clear();
			addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getName()).replace(':', ','));
			addSplitResult(list, vo.getStarringActor());
			addSplitResult(list, vo.getOverseerName());
			writeDqDocBlock(list, ++total, uflixMobileBw);
		}
		else if(type.equals(Constant.TYPE.UFLIX_CATGB_TV)) {
			writeDqDocBlock(list, total, uflixTvgBw);
			
			// 제목, 주연, 감독 DQ_DOC 추가
			list.clear();
			addSplitResult(list, Method.normalizeAutoCompleteTitle(vo.getName()).replace(':', ','));
			addSplitResult(list, vo.getStarringActor());
			addSplitResult(list, vo.getOverseerName());
			writeDqDocBlock(list, ++total, uflixTvgBw);
		}
	}

	/**
	 * PLOT 를 이용하여 사전에 등록된 키워드만 추출한다.
	 * @param list
	 * @param s
	 */
	private void addAnalyzeResult(List<String> list, String s, String type){
		if("".equals(s) || s==null)
			return;
		PLOTResult plotResult = plot.analyze(s);
		for(int i = 0; i < plotResult.getNENum(); i++) {
			if(plotResult.getCenterPatternPos(i) >= 0 && plotResult.getCenterPattern(i).equals("@keyword")){
				String term = plotResult.getNE(i);
				list.add(removeNStoreTerm(term));
			}
		}
	}
	
	private void addSplitResult(List<String> list, String s){
		if(s.contains(",")){
			String[] toks = s.split(",");
			for(String tok : toks){
				list.add(removeNStoreTerm(Method.normalizeDramaData(tok).trim()));
			}
		} else {
			list.add(removeNStoreTerm(Method.normalizeDramaData(s).trim()));
		}
	}
	
	private void addSplitResult(Set<String> set, String s){
		if(s.contains(",")){
			String[] toks = s.split(",");
			for(String tok : toks){
				set.add(removeNStoreTerm(Method.normalizeDramaData(tok)));
			}
		} else {
			set.add(removeNStoreTerm(Method.normalizeDramaData(s)));
		}
	}
	
	
	/**
	 * DRAMA 에서 색인을 할수 있는
	 *  DQ DOC 문서를 만든다.
	 * @param keywordList
	 * @param id
	 * @param bw
	 */
	private void writeDqDocBlock(List<String> keywordList, int id, BufferedWriter bw){
		StringBuilder sb = new StringBuilder();
		sb.append("(DQ_DOC\n");
		sb.append("(KEYWORD\n");
		for(int i=0 ,iSize=keywordList.size() ; i<iSize ; i++){
			if("".equals(keywordList.get(i)) || keywordList.get(i) == null)
				continue;
//			sb.append(" ");
			sb.append(keywordList.get(i));
			if(i < (iSize-1))
				sb.append(",");
		}
		sb.append("\n)KEYWORD\n");
		sb.append("(ID\n");
		sb.append(id);
		sb.append("\n)ID\n");
		sb.append(")DQ_DOC\n");
		try {
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		FileUtil.closeBufferedWriter(videoBw);
		FileUtil.closeBufferedWriter(videoNaverBw);
		FileUtil.closeBufferedWriter(uflixMobileBw);
		FileUtil.closeBufferedWriter(uflixTvgBw);
		FileUtil.closeBufferedWriter(uflixMobileNaverBw);
		FileUtil.closeBufferedWriter(uflixTvgNaverBw);
		naverNewsEnt.closeSelectPstmt();
		vod.closeSelectPstmt();
		hitUcc.closeSelectPstmt();
		highLight.closeSelectPstmt();
		event.closeSelectPstmt();
		uflixPackage.closeSelectPstmt();
		uflixSeries.closeSelectPstmt();
		srcConnector.closeConnection(srcConn);
		System.out.println("CLOSED DB [" + DB_INFO.URL + "]");
	}
	
}
