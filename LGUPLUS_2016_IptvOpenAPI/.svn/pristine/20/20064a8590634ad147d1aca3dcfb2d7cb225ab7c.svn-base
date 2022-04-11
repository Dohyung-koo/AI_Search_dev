package com.diquest.openapi.iptvpre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.DynamicAny._DynEnumStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QueryParser;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.openapi.relword.RelService;
import com.diquest.openapi.util.StringUtil;
import com.diquest.openapi.util.UtilManager;


@Service
public class IptvPreService {

	public Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	RelService relService;

	/**
	 * 검색 요청 변수 parse
	 * 
	 * @param input
	 * @param statusInfo
	 * @return
	 */
	
	public IptvPreVO parseInput(String input) throws Exception {
		IptvPreVO searchVO = new IptvPreVO();
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(input);

			searchVO.setCollection((String) obj.get("collection"));
			searchVO.setW((String) obj.get("w"));
			searchVO.setQ((String) obj.get("q"));
			searchVO.setSection((String) obj.get("section"));
			searchVO.setPg((String) obj.get("pg"));
			searchVO.setOutmax((String) obj.get("outmax"));
			searchVO.setSort((String) obj.get("sort"));
			searchVO.setP((String) obj.get("p"));
			searchVO.setD((String) obj.get("d"));
			searchVO.setCsq((String) obj.get("csq"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return searchVO;
	}

	/**
	 * 검색 쿼리 목록 만들기
	 * 
	 * @param searchVO
	 * @return
	 */
	public QuerySet makeQuerySet(IptvPreVO searchVO) throws Exception {
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String section_org = searchVO.getSection();
		// 통합검색 section 리스트 복사
		searchVO.setCloneSection(searchVO.getSection());
		String collectionName = "";
		String outmax = searchVO.getOutmax();

		// 통합검색 section 리스트
		String sectionList = searchVO.getSectionList();
		String[] sectionListArr = sectionList.split(",");
		
		
		// 순간검색 section 리스트
		String quickList = searchVO.getQuickList();
		String[] quickListArr = quickList.split(",");
		

		// 통합검색 여부 체크
		if (w != null && w.equals("IPTV_PRE") && section != null && !section.equals("")) {
			sectionListArr = new String[] { section };
		} else if (w != null && w.equals("IPTV_PRE_QUICK") && (section == null || section.equals(""))) {
			sectionListArr = quickListArr;
			// searchVO.setOutmax("20");
		}
		searchVO.setSectionListArr(sectionListArr);

		// sectionList 사이즈 만큼 쿼리 생성
		int queryCount = sectionListArr.length; // 세션수
				
		if(w != null && w.equals("IPTV_PRE_QUICK") && (section == null || section.equals("")) ) {
			queryCount = queryCount;
		
		}else {
			queryCount = queryCount + 1; // LTEVOD 는 연관검색어 쿼리 +1 , LTEVOD_QUICK는
			// 자동완성 쿼리 +1
		}
		
		QuerySet querySet = new QuerySet(queryCount);
		Query[] queryArr = new Query[queryCount];
		
		if (w.equals("IPTV_PRE")) {
			for (int i = 0; i < sectionListArr.length; i++) {
				section = sectionListArr[i];
				searchVO.setSection(section);
	
				// collection 구분
				switch (section) {
				case "IPTV_CHA": // 실시간
					collectionName = "IPTV_PRE_CHANNEL";
					break;
				default: // VOD
					collectionName = "IPTV_PRE";
					break;
				}
	
				searchVO.setCollection(collectionName);
	
				queryArr[i] = makeQuery(searchVO);
	
				if (w.equals("IPTV_PRE")) { // 통합검색일때
	
					if (section_org != null && !section_org.equals("")) { // section'파리미터가
																			// 있는 경우
																			// >>
																			// 로그남기기
						queryArr[i].setLoggable(true);
					} else {
						queryArr[i].setLoggable(true);
					}
	
				} else { // 자동완성일때
					queryArr[i].setLoggable(false);
				}
	
				querySet.addQuery(queryArr[i]);
			}
		}
		// 자동완성 쿼리 추가
		if (w.equals("IPTV_PRE_QUICK")) {
			searchVO.setCollection("IPTVPRE_AUTO_COMPLETE");
			searchVO.setOutmax(outmax);
			queryArr[queryCount - 1] = makeQuery(searchVO);
			queryArr[queryCount - 1].setValue("search_type", "auto");
			querySet.addQuery(queryArr[queryCount - 1]);
		} else {
			// 연관검색어 쿼리 추가
			queryArr[queryCount - 1] = relService.makeQuery(searchVO.getQ(), "video");
			querySet.addQuery(queryArr[queryCount - 1]);
		}

		return querySet;
		
	}
	
	
	/*
	*//**
	 * 검색 쿼리 목록 만들기
	 * 
	 * @param searchVO
	 * @return
	 *//*
	public QuerySet makeNewQuerySet(IptvPreVO searchVO) throws Exception {
		String w = searchVO.getW();
		String section = searchVO.getCloneSection();
		String section_org = "";
		String collectionName = "";
		String outmax = searchVO.getOutmax();

		// 통합검색 section 리스트
		String sectionList = searchVO.getSectionList();
		String[] sectionListArr = sectionList.split(",");

		// 순간검색 section 리스트
		String quickList = searchVO.getQuickList();
		String[] quickListArr = quickList.split(",");

		// 통합검색 여부 체크
		if (w != null && w.equals("IPTV_PRE") && section != null && !section.equals("")) {
			sectionListArr = new String[] { section };
		} else if (w != null && w.equals("IPTV_PRE_QUICK") && (section == null || section.equals(""))) {
			sectionListArr = quickListArr;
			// searchVO.setOutmax("20");
		}
		searchVO.setSectionListArr(sectionListArr);

		// sectionList 사이즈 만큼 쿼리 생성
		int queryCount = sectionListArr.length; // 세션수

		queryCount = queryCount + 1; // LTEVOD 는 연관검색어 쿼리 +1 , LTEVOD_QUICK는
										// 자동완성 쿼리 +1

		QuerySet querySet = new QuerySet(queryCount);
		Query[] queryArr = new Query[queryCount];
		for (int i = 0; i < sectionListArr.length; i++) {
			section = sectionListArr[i];
			searchVO.setSection(section);

			// collection 구분
			switch (section) {
			case "LTE_CHA": // 대박영상
				collectionName = "I30_CHANNEL";
				break;
			default: // VOD
				collectionName = "IPTV_PRE";
				break;
			}

			searchVO.setCollection(collectionName);

			queryArr[i] = makeNewQuery(searchVO);

			if (w.equals("IPTV_PRE")) { // 통합검색일때

				if (section_org != null && !section_org.equals("")) { // section'파리미터가
																		// 있는 경우
																		// >>
																		// 로그남기기
					queryArr[i].setLoggable(true);
				} else {
					queryArr[i].setLoggable(true);
				}

			} else { // 자동완성일때
				queryArr[i].setLoggable(false);
			}

			querySet.addQuery(queryArr[i]);
		}
		// 자동완성 쿼리 추가
		if (w.equals("IPTV_QUICK")) {
			searchVO.setCollection("I30_AUTO_COMPLETE");
			searchVO.setOutmax(outmax);
			queryArr[queryCount - 1] = makeQuery(searchVO);
			queryArr[queryCount - 1].setValue("search_type", "auto");
			querySet.addQuery(queryArr[queryCount - 1]);
		} else {
			// 연관검색어 쿼리 추가
			queryArr[queryCount - 1] = relService.makeQuery(searchVO.getQ(), "video");
			querySet.addQuery(queryArr[queryCount - 1]);
		}

		return querySet;
	}
*/
	/**
	 * 통합검색 처리 (json)
	 * 
	 * @param searchVO
	 * @return
	 */
	public OpenAPIVideoResponseJson searchJson(IptvPreVO searchVO, int keyword_byte) throws Exception {
		OpenAPIVideoResponseJson resultResponse = new OpenAPIVideoResponseJson();

		String q = searchVO.getQ();
		q = getByteLength(q, keyword_byte);
		searchVO.setQ(q);

		QuerySet querySet = makeQuerySet(searchVO);
		ResultSet resultSet = null; // ResultSet
		Result[] resultList = null; // Result[]
		logger.debug("[SEARCH KEYWORD] " + searchVO.getQ());
		// ResultSet
		// System.out.println("검색전::::::::"+getNowDate(17));
		CommandSearchRequest command = new CommandSearchRequest(searchVO.getHost(), searchVO.getPort());
		try {
			int rs = command.request(querySet);
			if (rs >= -1) {
				Query[] queryList = querySet.getQueryList();
				resultSet = command.getResultSet();
				resultList = resultSet.getResultList();
//				System.out.println("resultListsize = " + resultList.length);

//				 if((resultList.length==2 && resultList[0].getTotalSize()==0) || (resultList.length>=3 && resultList[0].getTotalSize()==0 && resultList[1].getTotalSize()==0)){
//					 System.out.println("여기 타고 있나?");
//					 QuerySet NewquerySet = makeNewQuerySet(searchVO);
//					 CommandSearchRequest Newcommand = new CommandSearchRequest(searchVO.getHost(), searchVO.getPort());
//					 int Newrs = Newcommand.request(NewquerySet);
//						 if (Newrs >= -1) {
//	
//						 queryList = NewquerySet.getQueryList();
//						 resultSet = Newcommand.getResultSet();
//						 resultList = resultSet.getResultList();
//						 }
//					 }
//				
				
				// System.out.println("검색후::::::::");
				resultResponse = makeResponseJson(searchVO, queryList, resultList, resultResponse, searchVO.getSectionListArr());
				logger.debug("[SEARCH SUCCESS] searchJson");
			} else {
				logger.debug("[SEARCH FAIL] searchJson : " + rs + " - " + command.getException());
				resultResponse.setErrorResponse(UtilManager.makeRsInfoJson(rs));
			}
		} catch (Exception e) {
			logger.error("[EXCEPTION START] " + e.toString());
			e.printStackTrace();
			resultResponse.setErrorResponse(UtilManager.makeErrorJson("1000"));
			// System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION END
			// =+=+=+=+=+=+=+=+=+=+=+=+=+=");
		}
		// System.out.println("가공후::::::::"+getNowDate(17));

		return resultResponse;
	}

	/**
	 * 자동완성/순간 검색 처리 (json)
	 * 
	 * @param searchVO
	 * @return
	 */
	public OpenAPIVideoQuickResponseJson searchQuickJson(IptvPreVO searchVO, int keyword_byte) throws Exception {
		OpenAPIVideoQuickResponseJson resultResponse = new OpenAPIVideoQuickResponseJson();

		String q = searchVO.getQ();
		q = getByteLength(q, keyword_byte);
		searchVO.setQ(q);

		QuerySet querySet = makeQuerySet(searchVO);

		ResultSet resultSet = null; // ResultSet
		Result[] resultList = null; // Result[]
		logger.debug("[SEARCH KEYWORD] " + searchVO.getQ());
		// ResultSet
		CommandSearchRequest command = new CommandSearchRequest(searchVO.getHost(), searchVO.getPort());
		try {
			int rs = command.request(querySet);

			if (rs >= -1) {
				Query[] queryList = querySet.getQueryList();
				resultSet = command.getResultSet();
				resultList = resultSet.getResultList();
//				System.out.println("result length" + resultList.length);

				resultResponse = makeQuickResponseJson(searchVO, queryList, resultList, resultResponse, searchVO.getSectionListArr());
				// System.out.println("### search success
				// VideoService.searchQuick");
				logger.debug("[SEARCH SUCCESS] searchQuickJson");
			} else {
				// System.out.println("### search fail
				// VideoService.searchQuickJson: " + rs + " - " +
				// command.getException());
				logger.debug("[SEARCH FAIL] searchQuickJson : " + rs + " - " + command.getException());

				resultResponse.setErrorResponse(UtilManager.makeRsInfoJson(rs));
			}
		} catch (Exception e) {
			logger.error("[EXCEPTION START] " + e.toString());
			e.printStackTrace();
			resultResponse.setErrorResponse(UtilManager.makeErrorJson("2000"));
			// System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION END
			// =+=+=+=+=+=+=+=+=+=+=+=+=+=");
		}

		return resultResponse;
	}

	/**
	 * 검색 쿼리 생성
	 * 
	 * @param searchVO
	 * @return
	 */
	private Query makeQuery(IptvPreVO searchVO) throws Exception {
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String collectionName = searchVO.getCollection();
		int pagenum = Integer.parseInt(searchVO.getPg()); // 페이지번호
		int outmax = Integer.parseInt(searchVO.getOutmax()); // 목록개수
		int startnum = (pagenum - 1) * outmax; // 시작번호
		int endnum = 0;
		endnum = startnum + outmax - 1;

		SelectSet[] selectSet = makeSelectSet(searchVO);
		WhereSet[] whereSet = makeWhereSet(searchVO);
		OrderBySet[] orderBySet = makeOrderBySet(searchVO);
		FilterSet[] filterSet = makeFilterSet(searchVO);
		GroupBySet[] groupbySet = getGroupBySet(searchVO);

		Query query = new Query("<strong>", "</strong>");
		query.setSearchOption(Protocol.SearchOption.CACHE);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		if (orderBySet != null) {
			query.setOrderby(orderBySet);
		}
		if (filterSet != null) {
			query.setFilter(filterSet);
		}
		if (groupbySet != null && (!collectionName.equals("IPTVPRE_AUTO_COMPLETE")) && (!collectionName.equals("IPTV_PRE_CHANNEL")))
			query.setGroupBy(groupbySet);

		query.setValue("synYn", "N");
		query.setValue("searchTerm", searchVO.getQ());
		query.setValue("section", searchVO.getSection());
		if (searchVO.getP() == null) {
			query.setValue("p", "");
		} else {
			query.setValue("p", searchVO.getP());
		}
		query.setFrom(collectionName);
		if (collectionName.equals("IPTV_PRE")) {

			// query.setProfile(section);
			query.setRankingOption((byte) Protocol.RankingOption.DOCUMENT_RANKING);
		}

		query.setResult(startnum, endnum);

		query.setDebug(true);
		query.setPrintQuery(false);
		query.setSearchKeyword(searchVO.getQ());
		query.setLogKeyword(searchVO.getQ().toCharArray());

		if (w.equals("IPTV_PRE_QUICK")) {
			// query.setLoggable(false);
			query.setValue("search_type", "quick");
		} else {
			// query.setLoggable(true);
			query.setValue("search_type", "total");
			// 금지어, 불용어 사용함
			query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			// 동의어, 유의어 확장
			query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		}

		// 오타보정 옵션s
		String typo = "typo";
		char[] modifier = query.getResultModifierList();
		if (modifier != null) {
			typo += "," + String.valueOf(modifier);
		}
		query.setResultModifierList(typo);
		// 알파벳 한글로 반환 | 한글오타(자소) 정정 후 반환 | | 한글오타(자소) 모든 자소를 제거 후 결과가 있으면 반환
		// query.setValue("typo-options","CORRECT_HANGUL_SPELL|ALPHABETS_TO_HANGUL|HANGUL_SYLLABLE_ONLY|HANGUL_TO_HANGUL");
		// query.setValue("typo-options","ALPHABETS_TO_HANGUL|HANGUL_SYLLABLE_ONLY|HANGUL_TO_HANGUL");
		query.setValue("typo-options", "ALPHABETS_TO_HANGUL|HANGUL_TO_ALPHABETS");
		// query.setValue("typo-correct-result-num","10");
		query.setValue("typo-parameters", searchVO.getQ());

		if (w != null && w.equals("IPTV_PRE")) {
			// 연관검색어 옵션
			// query.setValue("dramaCollection", "VIDEO_KEYWORD");
			// query.setValue("dramaField", "IDX_KEYWORD_RELATION");
			// query.setValue("dramaKeyword", searchVO.getQ());
			// query.setValue("dramaOption", "XQ");
			// query.setValue("dramaResultSize", "10");
			// String drama = "drama";
			// modifier = query.getResultModifierList();
			// if (modifier != null) {
			// drama += "," + String.valueOf(modifier);
			// }
			// query.setResultModifierList(drama);

			// 특수검색어(바로가기 url)
			if (collectionName.equals("IPTV_PRE")) {
				query.setRedirect(searchVO.getQ());
			}
		}
		 QueryParser queryParser = new QueryParser();
//		 System.out.println("### Query : " + 	 queryParser.queryToString(query));
		return query;
	}
	
	
/*	
	*//**
	 * 검색 쿼리 생성
	 * 
	 * @param searchVO
	 * @return
	 *//*
	private Query makeNewQuery(IptvPreVO searchVO) throws Exception {
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String collectionName = searchVO.getCollection();
		int pagenum = Integer.parseInt(searchVO.getPg()); // 페이지번호
		int outmax = Integer.parseInt(searchVO.getOutmax()); // 목록개수
		int startnum = (pagenum - 1) * outmax; // 시작번호
		int endnum = 0;
		endnum = startnum + outmax - 1;

		SelectSet[] selectSet = makeSelectSet(searchVO);
		WhereSet[] whereSet = makeNewWhereSet(searchVO);
		OrderBySet[] orderBySet = makeOrderBySet(searchVO);
		FilterSet[] filterSet = makeFilterSet(searchVO);
		GroupBySet[] groupbySet = getGroupBySet(searchVO);

		Query query = new Query("<strong>", "</strong>");
		query.setSearchOption(Protocol.SearchOption.CACHE);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		if (orderBySet != null) {
			query.setOrderby(orderBySet);
		}
		if (filterSet != null) {
			query.setFilter(filterSet);
		}
		if (groupbySet != null && (!collectionName.equals("IPTVPRE_AUTO_COMPLETE")) && (!collectionName.equals("I30_CHANNEL")))
			query.setGroupBy(groupbySet);

		query.setValue("synYn", "N");
		query.setValue("searchTerm", searchVO.getQ());
		query.setValue("section", searchVO.getSection());
		if (searchVO.getP() == null) {
			query.setValue("p", "");
		} else {
			query.setValue("p", searchVO.getP());
		}
		query.setFrom(collectionName);
		if (collectionName.equals("IPTV_PRE")) {

			// query.setProfile(section);
			query.setRankingOption((byte) Protocol.RankingOption.DOCUMENT_RANKING);
		}

		query.setResult(startnum, endnum);

		query.setDebug(true);
		query.setPrintQuery(false);
		query.setSearchKeyword(searchVO.getQ());
		query.setLogKeyword(searchVO.getQ().toCharArray());

		if (w.equals("IPTV_PRE_QUICK")) {
			// query.setLoggable(false);
			query.setValue("search_type", "quick");
		} else {
			// query.setLoggable(true);
			query.setValue("search_type", "total");
			// 금지어, 불용어 사용함
			query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			// 동의어, 유의어 확장
			query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		}
		//쿼리모디파이어
		String stcflag = "";
		if (w.equals("IPTV_PRE")) {
//			query.setQueryModifier("video");
//			stcflag = "Y";
//			query.setValue("stcflag", stcflag);
			query.setValue("stcflag", "N");
		} else {
			query.setValue("stcflag", "N");
			stcflag = "N";
		}
		query.setResultModifierList("video");
		query.setResultModifier("mixed");

		// 오타보정 옵션s
		String typo = "typo";
		char[] modifier = query.getResultModifierList();
		if (modifier != null) {
			typo += "," + String.valueOf(modifier);
		}
		query.setResultModifierList(typo);
		// 알파벳 한글로 반환 | 한글오타(자소) 정정 후 반환 | | 한글오타(자소) 모든 자소를 제거 후 결과가 있으면 반환
		// query.setValue("typo-options","CORRECT_HANGUL_SPELL|ALPHABETS_TO_HANGUL|HANGUL_SYLLABLE_ONLY|HANGUL_TO_HANGUL");
		// query.setValue("typo-options","ALPHABETS_TO_HANGUL|HANGUL_SYLLABLE_ONLY|HANGUL_TO_HANGUL");
		query.setValue("typo-options", "ALPHABETS_TO_HANGUL|HANGUL_TO_ALPHABETS");
		// query.setValue("typo-correct-result-num","10");
		query.setValue("typo-parameters", searchVO.getQ());

		if (w != null && w.equals("IPTV_PRE")) {

			// 특수검색어(바로가기 url)
			if (collectionName.equals("IPTV_PRE")) {
				query.setRedirect(searchVO.getQ());
			}
		}
//		 QueryParser queryParser = new QueryParser();
//		 System.out.println("### Query : " + queryParser.queryToString(query));

		
		return query;
	}
*/
	/**
	 * SelectSet 구성
	 * 
	 * @param searchInfo
	 * @return
	 */
	private SelectSet[] makeSelectSet(IptvPreVO searchVO) throws Exception {

		SelectSet[] selectSet = null;
		ArrayList selectList = new ArrayList();

		String collectionname = searchVO.getCollection();
		switch (collectionname) {
		case "IPTV_PRE_CHANNEL":
			selectList.add(new SelectSet("SERVICE_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SERVICE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SERVICE_ENG_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CHANNEL_NO", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PROGRAM_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PROGRAM_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("THM_IMG_URL", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("THM_IMG_FILE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("RATING", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("BROAD_TIME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("DAY", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("OVERSEER_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("ACTOR", Protocol.SelectSet.NONE));

			selectList.add(new SelectSet("P_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE1", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE2", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE3", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SERIES_NO", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SUB_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("BROAD_DATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("LOCAL_AREA", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("AV_RESOLUTION", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("Dvs_Flag", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("IS_51_CH", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("FILTERING_CODE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CHNL_KEYWORD", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CHNL_ICON_URL", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CHNL_ICON_FILE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));

			break;
		case "IPTV_PRE":
			selectList.add(new SelectSet("RESULT_TYPE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SECTION", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CAT_GB", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CAT_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CAT_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("ALBUM_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("ALBUM_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("IMG_URL", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("IMG_FILE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SERVICE_GB", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PRICE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PR_INFO", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("RUNTIME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("IS_CAPTION", Protocol.SelectSet.NONE));

			selectList.add(new SelectSet("ACTOR", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("STARRING_ACTOR", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("OVERSEER_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE1", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE2", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("GENRE3", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SERIES_NO", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("BROAD_DATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("RELEASE_DATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("BROADCASTER", Protocol.SelectSet.NONE));

			selectList.add(new SelectSet("SER_CAT_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("MULTI_MAPPING_FLAG", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("POSTER_FILE_URL", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("POSTER_FILE_NAME_10", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("POSTER_FILE_NAME_30", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE_ENG", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("DIRECTOR_ENG", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PLAYER_ENG", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CAST_NAME_ENG", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CAST_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE_ORIGIN", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WRITER_ORIGIN", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("PUBLIC_CNT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("POINT_WATCHA", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("RETENTION_YN", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("ALBUM_NO", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("STILL_IMG_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("THEME_YN", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CLOSE_YN", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("ORDER_DATE", Protocol.SelectSet.NONE));

			selectList.add(new SelectSet("NSC_GB", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CATEGORY_TYPE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("KIDS_GRADE", Protocol.SelectSet.NONE));

			selectList.add(new SelectSet("KOFIC_SUPPORTING_ACTOR", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("KOFIC_EXTRA_ACTOR", Protocol.SelectSet.NONE));
			
			selectList.add(new SelectSet("SUPER_ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("SUPER_NAME", Protocol.SelectSet.NONE));
			
			selectList.add(new SelectSet("SYN_KEYWORD", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("EPI_KEYWORD", Protocol.SelectSet.NONE));
			

			break;
		case "IPTVPRE_AUTO_COMPLETE":
			selectList.add(new SelectSet("KEYWORD", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("POINT", Protocol.SelectSet.NONE));

			break;
		default:
			break;
		}

		selectSet = new SelectSet[selectList.size()];
		for (int i = 0; i < selectList.size(); i++) {
			selectSet[i] = (SelectSet) selectList.get(i);
		}

		return selectSet;
	}

	/**
	 * WhereSet 구성
	 * 
	 * @param searchVO
	 * @return
	 */
	private WhereSet[] makeWhereSet(IptvPreVO searchVO) throws Exception {

		WhereSet[] whereSet = null;
		ArrayList whereList = new ArrayList();
		String section = "";
		if (searchVO.getSection() != null) {
			section = searchVO.getSection();
		}

		String collectionname = searchVO.getCollection();
		String p = searchVO.getP(); // p 파라미터 분야제한
		String option  = searchVO.getOption();
		
		if ((p == null || "".equals(p))|| "p".equalsIgnoreCase(p)) {
			p = "01,02,03,04,05";
		}else if("k".equalsIgnoreCase(p)){
			p = "01,02,03,04";
		}
		
		switch (collectionname) {
		case "IPTV_PRE_CHANNEL":
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

			whereList.add(new WhereSet("IDX_SERVICE_NAME_W", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SERVICE_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SERVICE_ENG_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_GENRE3_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SUB_NAME", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SUB_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SUB_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SUB_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_SUB_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_CHNL_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_PROGRAM_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			// 복합 인덱스
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_MULTI_FILED", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			break;
		case "IPTV_PRE":
//			String p = searchVO.getP(); // p 파라미터 분야제한
//			String option  = searchVO.getOption();
//			if ((p == null || "".equals(p))|| "p".equals(p)) {
//				p = "01,02,03,04,05";
//			}else if("k".equals(p)){
//				p = "01,02,03";
//			}
			
			if(!searchVO.getSection().equals("IPTV_PLUS") || !searchVO.getSection().equals("IPTV_UFPLUS")) {
				String[] p_arr = p.split(",");
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet("IDX_PR_INFO_F", Protocol.WhereSet.OP_HASANY, p_arr));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			}
			

			if (searchVO.getW().equals("IPTV_QUICK")) {
				if (searchVO.getSection().equals("") || searchVO.getSection() == null || searchVO.getSection().equals("IPTV_AUTO")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_LIFE"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_CHA"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_REP"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_MOV"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_KIDS"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_ANI"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_DOCU"));

					// 20170405-LTE_PLUS추가
					if (p != null && "01,02,03,04,05,06".equals(p)) {
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_PLUS"));
					}

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				} else {

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					if (searchVO.getSection().equals("LTE_LIFE")) {
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, searchVO.getSection()));
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_DOCU"));
					} else {
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, searchVO.getSection()));

					}

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));

				}

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

				whereList.add(new WhereSet("IDX_ALBUM_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_TITLE_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_OVERSEER_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PALB"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PCAT"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}

			if (searchVO.getW().equals("IPTV_PRE")) {
				if (searchVO.getSection().equals("") || searchVO.getSection() == null || searchVO.getSection().equals("IPTV_TOTAL") || searchVO.getSection().equals("IPTV_UF")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_LIFE"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_REP"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_MOV"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_KIDS"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_ANI"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_DOCU"));

//					// 20170405-LTE_PLUS추가
//					if (p != null && "01,02,03,04,05,06".equals(p)) {
//						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
//						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_PLUS"));
//					}

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				}else if(searchVO.getSection().equals("IPTV_PLUS") || searchVO.getSection().equals("IPTV_UFPLUS") ){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_PLUS"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				}
				else {

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					if (searchVO.getSection().equals("LTE_LIFE")) {
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, searchVO.getSection()));
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_DOCU"));
					} else {
						whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, searchVO.getSection()));

					}

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));

				}

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

				

				if(searchVO.getOption().equalsIgnoreCase("f") || searchVO.getOption().equals("")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_BROADCASTER_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_TITLE_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_DIRECTOR_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_TITLE_ORIGIN_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_WRITER_ORIGIN_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));

					whereList.add(new WhereSet("IDX_TITLE_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_TITLE_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_TITLE_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_TITLE_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_W", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_ENG_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					
					whereList.add(new WhereSet("IDX_ALBUM_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_SYN_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_EPI_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					
					

					// 복합 인덱스
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_MULTI_FILED", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					
				}else if(searchVO.getOption().equalsIgnoreCase("n")) {
					
					whereList.add(new WhereSet("IDX_ALBUM_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_K", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_TB", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_CAT_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}else if(searchVO.getOption().equalsIgnoreCase("a")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}else if(searchVO.getOption().equalsIgnoreCase("d")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PALB"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PCAT"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				if(searchVO.getOption().equalsIgnoreCase("f") || searchVO.getOption().equals("")) {

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

					whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_VOICE_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_BROADCASTER_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_DIRECTOR_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_WRITER_ORIGIN_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_W", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACT_DISP_MAX_ENG_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
//					
					whereList.add(new WhereSet("IDX_SYN_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_EPI_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					
					// 복합 인덱스
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_MULTI_FILED_NO_TITLE", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "CALB"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				}else if(searchVO.getOption().equalsIgnoreCase("a")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ().replaceAll("\\s", "")));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "CALB"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}else if(searchVO.getOption().equalsIgnoreCase("d")) {
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "CALB"));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
					
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}

				
			}
			break;
		case "IPTVPRE_AUTO_COMPLETE":
			
			String[] p_arr = p.split(",");
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("IDX_PR_INFO_F", Protocol.WhereSet.OP_HASANY, p_arr));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("IDX_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			break;
		default:
			break;
		}

		whereSet = new WhereSet[whereList.size()];
		for (int i = 0; i < whereList.size(); i++) {
			whereSet[i] = (WhereSet) whereList.get(i);
		}

		return whereSet;
	}

	/**
	 * 통합 검색 결과별 후 처리 (json)
	 * 
	 * @param searchVO
	 * @param queryList
	 * @param resultList
	 * @param response
	 * @return
	 */
	private OpenAPIVideoResponseJson makeResponseJson(IptvPreVO searchVO, Query[] queryList, Result[] resultList, OpenAPIVideoResponseJson response,
			String[] sectionListArr) throws Exception {
		String typoResult = "";
		String stcFlag = "";
		String synSearcher = "";
		int relWordSum = 0;
		int wordSum = 0;
		int totalSum = 0;

		ArrayList relWordList = new ArrayList();
		List wordList = new ArrayList();
		List JsonSectionList = new ArrayList(); // section 순위 정하기 전 리스트
		List JsonSectionRealList = new ArrayList(); // section 순위 정하기 후 리스트

		List sectionName = new ArrayList();
		List sectionWeight = new ArrayList();
		List sectionFirstWeight = new ArrayList();

		List<Json_Category> jsonCategoryList = new ArrayList();
		
//		System.out.println("resultlist길이" + resultList.length);
		
		for (int i = 0; i < resultList.length; i++) {
			Json_Section json_section = new Json_Section();
			Result result = resultList[i];

			if (i == 0) {

				typoResult = result.getValue("typo-result"); // 한영변환 결과
				stcFlag = result.getValue("stcflag");
				synSearcher = result.getValue("synsearCher");
			}
			if (searchVO.getW().equals("IPTV_PRE_QUICK") && i == resultList.length - 1) {
				wordList = makeAutoWord(result, (Query) queryList[i]);
				wordSum = wordSum + wordList.size();
			} else {
				if (i == 0) {
					jsonCategoryList = getJsonGroupResultMap(result);
				}

				if (i == 1 && resultList.length == 3) {
					List<Json_Category> cha_list_cnt = new ArrayList();
					Json_Category cate = new Json_Category();

					if (result.getTotalSize() > 0) {
						cate.setCat_name("IPTV_CHA");
						cate.setTotcnt(String.valueOf(result.getTotalSize()));
						jsonCategoryList.add(cate);

						if (Integer.parseInt(cate.getTotcnt()) > 0) {

							List<Json_Category> resultList_after = new ArrayList<Json_Category>();

							int num = jsonCategoryList.size();
							int[] score = new int[num];
							int[] rank = new int[num];

							for (int ck = 0; ck < num; ck++) {
								score[ck] = Integer.parseInt((String) jsonCategoryList.get(ck).getTotcnt());
							}

							// 각 섹션 정확도에 순위 정하기
							for (int c = 0; c < num; c++) {
								for (int j = 0; j < num; j++) {
									if (score[c] < score[j])
										rank[c] = rank[c] + 1;
								}
							}

							// 정한 순위 정확도순 내림차순 정렬하기
							for (int p = 0; p < num; p++) {
								for (int q = 0; q < num; q++) {
									if (p == rank[q]) {
										resultList_after.add(jsonCategoryList.get(q));
									}
								}
							}

							jsonCategoryList = resultList_after;

						}

					}

				}

				if (i == resultList.length - 1) {
					relWordList = relService.makeRelWord(result, (Query) queryList[i]);
					relWordSum = relWordSum + relWordList.size();
				} 
//				else if (i == 1 && resultList.length == 3) {
//				} 
				else {
					// 참조용 파라미터 셋팅
					if(StringUtils.equalsIgnoreCase("y",searchVO.getKidsyn()) && StringUtils.equals("IPTV_TOTAL",sectionListArr[i])) {
						json_section.setSection_name("IPTV_KIDS");
					}else {
						json_section.setSection_name(sectionListArr[i]); // 검색 섹션
					}
					
					json_section.setUsrspec(""); // 검색 정보파일명
					json_section.setIdxname(""); // 검색 색인 파일명
					json_section.setQryflag(""); // 검색 질의어 요청 방식
					json_section.setSrtflag(""); //

					if (result.getRedirect() != null) {

						response.setDirecturl(String.valueOf(result.getRedirect()));
					}
					
					if (result.getTotalSize() > 0  && StringUtils.equals("IPTV_TOTAL",sectionListArr[i]) && (StringUtils.equals("y",searchVO.getKidsyn()))||(StringUtils.equals("Y",searchVO.getKidsyn())) ) {
						
						logger.debug("[SEARCH SECTION_NAME] " + "IPTV_KIDS" + "(" + result.getTotalSize() + ")");
					}else if (result.getTotalSize() > 0) {
						
						logger.debug("[SEARCH SECTION_NAME] " + sectionListArr[i] + "(" + result.getTotalSize() + ")");
					}

					// 공통 파라미터 셋팅
					json_section.setTotcnt(result.getTotalSize() + ""); // 검색 결과
																		// 전체 건수
					totalSum = totalSum + result.getTotalSize();
					json_section.setMaxcnt(searchVO.getOutmax()); // 사용자 클릭 요청수
					json_section.setOutcnt(String.valueOf(result.getRealSize())); // xml
																					// 결과
																					// 출력
																					// 건수
					json_section.setPagenum(searchVO.getPg()); // xml 결과 출력 페이지
					String doc = ""; // id:문서번호, simc유사도
					String corqur = ""; // 오타보정 추천어
					String directurl = ""; // 바로가기 URL

					List attList = makeVideoJsonAtt(searchVO, result, (Query) queryList[i], sectionListArr[i]);

					if (attList.size() > 0) {

						switch (sectionListArr[i]) {
						case "IPTV_CHA": // 실시간TV
							sectionName.add(sectionListArr[i]);
							V_channel channel = (V_channel) attList.get(0);
							sectionWeight.add("2147483646"); // 실시간 티비가 검색된 것이
																// 있으면 무조건 최상위
							sectionFirstWeight.add("2147483646");
							break;
						default: // VOD
							sectionName.add(sectionListArr[i]);
							V_vod vod = (V_vod) attList.get(0);
							sectionWeight.add(vod.getWeight());
							sectionFirstWeight.add("0");

							for (int vodnum = 0; vodnum < attList.size(); vodnum++) {

								V_vod vodvod = (V_vod) attList.get(vodnum);

								if (vodvod.getSection().equals("LTE_MOV")) {
									String star = vodvod.getStarringActor(); // 주연
									String support = vodvod.getSupportingActor(); // 조연
									String extra = vodvod.getExtraActor(); // 단역
									String actor = vodvod.getActor(); // 출연

									if (star != null && !star.equals("") && star.contains(searchVO.getQ())) {
										vodvod.setActorGb("주연");
									} else if (support != null && !support.equals("") && support.contains(searchVO.getQ())) {
										vodvod.setActorGb("조연");
									} else if (extra != null && !extra.equals("") && extra.contains(searchVO.getQ())) {
										vodvod.setActorGb("단역");
									} else if (actor != null && !actor.equals("") && actor.contains(searchVO.getQ())) {
										vodvod.setActorGb("출연");
									} else {
										vodvod.setActorGb("");
									}
								} else {
									vodvod.setActorGb("");
								}

								attList.set(vodnum, vodvod);

							}

							break;
						}

					} else {
						sectionName.add(sectionListArr[i]);
						sectionWeight.add("0");
						sectionFirstWeight.add("0");
					}

					json_section.setJson_att_list(attList);
					Json_Section_List json_section_list = new Json_Section_List();
					json_section_list.setJson_section(json_section);
					JsonSectionList.add(json_section_list);
				}
			}
		}

		int num = sectionName.size();
		int[] score = new int[num];
		int[] rank = new int[num];

		for (int ck = 0; ck < num; ck++) {
			score[ck] = Integer.parseInt((String) sectionFirstWeight.get(ck));
		}

		// 순위정하고 담을

		for (int i = 0; i < num; i++) {
			for (int j = 0; j < num; j++) {
				if (score[i] < score[j])
					rank[i] = rank[i] + 1;
			}
		}

		for (int p = 0; p < num; p++) {
			for (int q = 0; q < num; q++) {
				if (p == rank[q]) {
					JsonSectionRealList.add(JsonSectionList.get(q));
				}
			}
		}

		logger.debug("[SEARCH Total CNT] " + totalSum);

		// 문장형검색 여부
//		response.setStcflag(stcFlag);
		if (!searchVO.getW().equals("IPTV_QUICK")) {
			response.setJson_section_list(JsonSectionRealList);
		}
		// 검색 결과 담기

//		response.setCategory_list(jsonCategoryList);

		/*
		 * // 연간검색어 담기 response.setRelWord(relWordList); logger.debug(
		 * "[SEARCH RelWord CNT] "+relWordList.size());
		 */
		if (totalSum == 0) {
			response.setErrorResponse(UtilManager.makeRsInfoJson(-1));
		}
		logger.debug("[SEARCH Corqry] " + response.getCorqry());
		return response;
	}

	/**
	 * 자동완성/순간 검색 결과별 후 처리 (json)
	 * 
	 * @param searchVO
	 * @param queryList
	 * @param resultList
	 * @param response
	 * @return
	 */
	private OpenAPIVideoQuickResponseJson makeQuickResponseJson(IptvPreVO searchVO, Query[] queryList, Result[] resultList,
			OpenAPIVideoQuickResponseJson response, String[] sectionListArr) throws Exception {
		String typoResult = "";
//		 String typoCorrectResult = "";
		String synSearcher = "";
		int relWordSum = 0;
		int wordSum = 0;
		int totalSum = 0;

		ArrayList relWordList = new ArrayList();
		List wordList = new ArrayList();
		List JsonSectionList = new ArrayList();
		for (int i = 0; i < resultList.length; i++) {
			Json_Section json_section = new Json_Section();
			Result result = resultList[i];

			if (i == 0) {
				typoResult = result.getValue("typo-result"); // 한영변환 결과
				// typoCorrectResult = result.getValue("typo-correct-result");
				// // 한영변환 결과
				synSearcher = result.getValue("synsearCher");
				// relWordList = getDramaResult(result); // 연관검색어 결과
				// relWordSum = relWordSum+relWordList.size();
			}

			if (searchVO.getW().equals("IPTV_PRE_QUICK") && i == resultList.length - 1) {
				wordList = makeAutoWord(result, (Query) queryList[i]);
				wordSum = wordSum + wordList.size();
				totalSum = totalSum + result.getTotalSize();
			} 
/*			
			else {
				if (i == resultList.length - 1) {
					relWordList = relService.makeRelWord(result, (Query) queryList[i]);
					relWordSum = relWordSum + relWordList.size();
				} else {
					// 참조용 파라미터 셋팅
					json_section.setSection_name(sectionListArr[i]); // 검색 섹션
					json_section.setUsrspec(""); // 검색 정보파일명
					json_section.setIdxname(""); // 검색 색인 파일명
					json_section.setQryflag(""); // 검색 질의어 요청 방식
					json_section.setSrtflag(""); //

					if (result.getTotalSize() > 0) {
						logger.debug("[SEARCH SECTION_NAME] " + sectionListArr[i] + "(" + result.getTotalSize() + ")");
					}

					// 공통 파라미터 셋팅
					json_section.setTotcnt(result.getTotalSize() + ""); // 검색 결과
																		// 전체 건수
					totalSum = totalSum + result.getTotalSize();
					json_section.setMaxcnt(searchVO.getOutmax()); // 사용자 클릭 요청수
					json_section.setOutcnt(String.valueOf(result.getRealSize())); // xml
																					// 결과
																					// 출력
																					// 건수
					json_section.setPagenum(searchVO.getPg()); // xml 결과 출력 페이지
					String doc = ""; // id:문서번호, simc유사도
					String corqur = ""; // 오타보정 추천어
					String directurl = ""; // 바로가기 URL

					List attList = makeVideoJsonAtt(searchVO, result, (Query) queryList[i], sectionListArr[i]);
					json_section.setJson_att_list(attList);
					Json_Section_List json_section_list = new Json_Section_List();
					json_section_list.setJson_section(json_section);
					JsonSectionList.add(json_section_list);
				}
			}
			*/
		}
/*
		// 오타보정
		if (synSearcher != null && !"".equals(synSearcher) && !synSearcher.equals(searchVO.getQ())) {
			response.setCorqry(synSearcher);
			// } else if(typoCorrectResult != null &&
			// !typoCorrectResult.equals("")) {
			// response.setCorqry(typoCorrectResult);
		} else {
			// typo의 영한 변환 결과가 이상할 경우 typo 결과를 사용하지 않음(정상적인 한글일 경우에만 typo 결과 사용)
			// 한영 변환도 추가되었으므로 검색 쿼리가 이상할 경우 typo 결과 사용(영한 변환의 result를 확인하는 것과
			// 동일한 방법 사용)
			// 생각을 바꿔서..
			// 입력이 영어고 출력이 바른 한글일 경우에 처리
			if (StringUtil.isEnglishHyphen(searchVO.getQ()) && StringUtil.isCorrecHangul(typoResult))
				response.setCorqry(typoResult);
			// 입력이 바른 한글이 아닌 경우에 대해서 처리
			else if (!StringUtil.isCorrecHangul(searchVO.getQ()))
				response.setCorqry(typoResult);
		}

		// 오타보정어를 자동완성어 리스트에 담기
		if (response.getCorqry() != null && (!response.getCorqry().equals(""))) {

			if (response.getCorqry().equals(searchVO.getQ())) {
				response.setCorqry(null);
			} else {
				List list = new ArrayList();
				int num = Integer.parseInt(searchVO.getOutmax());
				for (int i = 0; i < wordList.size(); i++) {
					String synword = (String) wordList.get(i);
					if (!synword.equals(synSearcher)) {
						if (!response.getCorqry().equals(searchVO.getQ())) {
							if (list.size() < 9 && list.size() + 1 < num)
								list.add(wordList.get(i));
						}
					}
				}

				list.add(response.getCorqry());
				wordList.clear();
				wordList = list;
			}

			logger.debug("[SEARCH Total CNT] " + totalSum);
		}
*/
		// 검색 결과 담기
		// if (!searchVO.getW().equals("IPTV_QUICK")) {
//		response.setJson_section_list(JsonSectionList);
		// }

		response.setAutoWord(wordList);
		logger.debug("[SEARCH AutoWord CNT] " + wordList.size());

		if (totalSum == 0) {
			response.setErrorResponse(UtilManager.makeRsInfoJson(-1));
		}
		logger.debug("[SEARCH Corqry] " + response.getCorqry());
		return response;
	}

	/**
	 * 자동완성 word 결과 담기
	 * 
	 * @param result
	 * @param query
	 * @return
	 */
	private List makeAutoWord(Result result, Query query) throws Exception {
		ArrayList wordList = new ArrayList();

		for (int i = 0; i < result.getRealSize(); i++) {

			// 수정후 1
			// String str = "";
			// 수정후 1

			for (int k = 0; k < result.getNumField(); k++) {

				String selectFieldName = new String((query.getSelectFields())[k].getField());
				String selectFieldValue = new String(result.getResult(i, k));

				// 수정전
				if (selectFieldName.equals("KEYWORD")) {
					wordList.add(selectFieldValue);
				}

			}
		}

		return wordList;
	}

	/**
	 * 검색 결과 json 담기
	 * 
	 * @param response
	 * @param searchVO,response,result,query
	 * @return OpenAPIVideoResponseresult
	 */
	private List makeVideoJsonAtt(IptvPreVO searchVO, Result result, Query query, String section) throws Exception {
		String collectionName = "";
		switch (section) {
		case "IPTV_CHA": // 실시간 채널
			collectionName = "IPTV_PRE_CHANNEL";
			break;
		default: // VOD
			collectionName = "IPTV_PRE";
			break;
		}

		List attList = new ArrayList();

		switch (collectionName) {
		case "IPTV_PRE_CHANNEL":
			List<V_channel> channelList = new ArrayList<V_channel>();
			for (int i = 0; i < result.getRealSize(); i++) {
				V_channel v_channel = new V_channel();
				for (int k = 0; k < result.getNumField(); k++) {

					String selectFieldName = new String((query.getSelectFields())[k].getField());
					String selectFieldValue = new String(result.getResult(i, k));
					if (selectFieldName.equals("SERVICE_ID")) {
						v_channel.setServiceId(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_NAME")) {
						v_channel.setServiceName(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_ENG_NAME")) {
						v_channel.setServiceEngName(selectFieldValue);
					} else if (selectFieldName.equals("CHANNEL_NO")) {
						v_channel.setChannelNo(selectFieldValue);
					} else if (selectFieldName.equals("PROGRAM_ID")) {
						v_channel.setProgramId(selectFieldValue);
					} else if (selectFieldName.equals("PROGRAM_NAME")) {
						v_channel.setProgramName(selectFieldValue);
					} else if (selectFieldName.equals("THM_IMG_URL")) {
						v_channel.setThmImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("THM_IMG_FILE_NAME")) {
						v_channel.setThmImgFileName(selectFieldValue);
					} else if (selectFieldName.equals("RATING")) {
						v_channel.setRating(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_TIME")) {
						v_channel.setBroadTime(selectFieldValue);
					} else if (selectFieldName.equals("DAY")) {
						v_channel.setDay(selectFieldValue);
					} else if (selectFieldName.equals("OVERSEER_NAME")) {
						v_channel.setOverseerName(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR")) {
						v_channel.setActor(selectFieldValue);
					} else if (selectFieldName.equals("P_NAME")) {
						v_channel.setPName(selectFieldValue);
					} else if (selectFieldName.equals("GENRE1")) {
						v_channel.setGenre1(selectFieldValue);
					} else if (selectFieldName.equals("GENRE2")) {
						v_channel.setGenre2(selectFieldValue);
					} else if (selectFieldName.equals("GENRE3")) {
						v_channel.setGenre3(selectFieldValue);
					} else if (selectFieldName.equals("SERIES_NO")) {
						v_channel.setSeriesNo(selectFieldValue);
					} else if (selectFieldName.equals("SUB_NAME")) {
						v_channel.setSubName(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_DATE")) {
						v_channel.setBroadDate(selectFieldValue);
					} else if (selectFieldName.equals("LOCAL_AREA")) {
						v_channel.setLocalArea(selectFieldValue);
					} else if (selectFieldName.equals("AV_RESOLUTION")) {
						v_channel.setAvResolution(selectFieldValue);
					} else if (selectFieldName.equals("Caption_Flag")) {
						v_channel.setCaptionFlag(selectFieldValue);
					} else if (selectFieldName.equals("Dvs_Flag")) {
						v_channel.setDvsFlag(selectFieldValue);
					} else if (selectFieldName.equals("IS_51_CH")) {
						v_channel.setIs51Ch(selectFieldValue);
					} else if (selectFieldName.equals("FILTERING_CODE")) {
						v_channel.setFilteringCode(selectFieldValue);
					} else if (selectFieldName.equals("CHNL_KEYWORD")) {
						v_channel.setChnlKeyword(selectFieldValue);
					} else if (selectFieldName.equals("CHNL_ICON_URL")) {
						v_channel.setChnlIconUrl(selectFieldValue);
						String rPre = "\\";
						rPre = rPre + rPre;
						String rAfter = "\\";
						v_channel.setChnlIconUrl(selectFieldValue.replace(rPre, rAfter));
					} else if (selectFieldName.equals("CHNL_ICON_FILE_NAME")) {
						v_channel.setChnlIconFileName(selectFieldValue);
					} else if (selectFieldName.equals("WEIGHT")) {
						v_channel.setWeight(selectFieldValue);
					}
				}
				channelList.add(v_channel);
			}

			attList = channelList;
			break;
		case "IPTV_PRE":
			List<V_vod> vodList = new ArrayList<V_vod>();

			for (int i = 0; i < result.getRealSize(); i++) {
				V_vod v_vod = new V_vod();
				String superId = "";
				String superName = "";
				for (int k = 0; k < result.getNumField(); k++) {

					String selectFieldName = new String((query.getSelectFields())[k].getField());
					String selectFieldValue = new String(result.getResult(i, k));

					if (selectFieldName.equals("RESULT_TYPE")) {
						v_vod.setResultType(selectFieldValue);
					} else if (selectFieldName.equals("SECTION")) {
						v_vod.setSection(selectFieldValue);
					} else if (selectFieldName.equals("CAT_GB")) {
						v_vod.setCatGb(selectFieldValue);
					} else if (selectFieldName.equals("CAT_ID")) {
						v_vod.setCatId(selectFieldValue);
					} else if (selectFieldName.equals("CAT_NAME")) {
						v_vod.setCatName(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_ID")) {
						v_vod.setAlbumId(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_NAME")) {
						v_vod.setAlbumName(selectFieldValue);
					} else if (selectFieldName.equals("IMG_URL")) {
						v_vod.setImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("IMG_FILE_NAME")) {
						int a = selectFieldValue.indexOf(".");
						if(selectFieldValue != null && StringUtils.isNotEmpty(selectFieldValue)){
							if(selectFieldValue.substring(a-2, a).equals("60")){
								selectFieldValue = "smartux/"+selectFieldValue;
							}
						}
						v_vod.setImgFileName(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_GB")) {
						v_vod.setServiceGb(selectFieldValue);
					} else if (selectFieldName.equals("PRICE")) {
						v_vod.setPrice(selectFieldValue);
					} else if (selectFieldName.equals("PR_INFO")) {
						v_vod.setPrInfo(selectFieldValue);
					} else if (selectFieldName.equals("RUNTIME")) {
						v_vod.setRuntime(selectFieldValue);
					} else if (selectFieldName.equals("IS_CAPTION")) {
						v_vod.setIsCaption(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR_GB")) {
						v_vod.setActorGb(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR")) {
						v_vod.setActor(selectFieldValue);
					} else if (selectFieldName.equals("STARRING_ACTOR")) {
						v_vod.setStarringActor(selectFieldValue);
					} else if (selectFieldName.equals("KOFIC_SUPPORTING_ACTOR")) {
						v_vod.setSupportingActor(selectFieldValue);
					} else if (selectFieldName.equals("KOFIC_EXTRA_ACTOR")) {
						v_vod.setExtraActor(selectFieldValue);
					} else if (selectFieldName.equals("OVERSEER_NAME")) {
						v_vod.setOverseerName(selectFieldValue);
					} else if (selectFieldName.equals("GENRE1")) {
						v_vod.setGenre1(selectFieldValue);
					} else if (selectFieldName.equals("GENRE2")) {
						v_vod.setGenre2(selectFieldValue);
					} else if (selectFieldName.equals("GENRE3")) {
						v_vod.setGenre3(selectFieldValue);
					} else if (selectFieldName.equals("SERIES_NO")) {
						v_vod.setSeriesNo(selectFieldValue);
					} else if (selectFieldName.equals("POINT")) {
						v_vod.setPoint(selectFieldValue);
					} else if (selectFieldName.equals("NSC_GB")) {
						v_vod.setNscGb(selectFieldValue);
					} else if (selectFieldName.equals("CATEGORY_TYPE")) {
						v_vod.setCategoryType(selectFieldValue);
					} else if (selectFieldName.equals("KIDS_GRADE")) {
						v_vod.setKidsGrade(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_DATE")) {
						v_vod.setBroadDate(selectFieldValue);
					} else if (selectFieldName.equals("RELEASE_DATE")) {
						v_vod.setReleaseDate(selectFieldValue);
					} else if (selectFieldName.equals("BROADCASTER")) {
						v_vod.setBroadcaster(selectFieldValue);
					} else if (selectFieldName.equals("SER_CAT_ID")) {
						v_vod.setSerCatId(selectFieldValue);
					} else if (selectFieldName.equals("MULTI_MAPPING_FLAG")) {
						v_vod.setMultiMappingFlag(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_URL")) {
						v_vod.setPosterFileUrl(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_NAME_10")) {
						v_vod.setPosterFileName10(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_NAME_30")) {
						v_vod.setPosterFileName30(selectFieldValue);
					} else if (selectFieldName.equals("TITLE_ENG")) {
						v_vod.setTitleEng(selectFieldValue);
					} else if (selectFieldName.equals("DIRECTOR_ENG")) {
						v_vod.setDirectorEng(selectFieldValue);
					} else if (selectFieldName.equals("PLAYER_ENG")) {
						v_vod.setPlayerEng(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME_ENG")) {
						v_vod.setCastNameEng(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME")) {
						v_vod.setCastName(selectFieldValue);
					} else if (selectFieldName.equals("TITLE_ORIGIN")) {
						v_vod.setTitleOrign(selectFieldValue);
					} else if (selectFieldName.equals("WRITER_ORIGIN")) {
						v_vod.setWriterOrigin(selectFieldValue);
					} else if (selectFieldName.equals("PUBLIC_CNT")) {
						v_vod.setPublicCnt(selectFieldValue);
					} else if (selectFieldName.equals("POINT_WATCHA")) {
						v_vod.setPointWatcha(selectFieldValue);
					} else if (selectFieldName.equals("RETENTION_YN")) {
						v_vod.setRetentionYn(selectFieldValue);
					} else if (selectFieldName.equals("TITLE")) {
						v_vod.setTitle(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_NO")) {
						v_vod.setAlbumNo(selectFieldValue);
					} else if (selectFieldName.equals("STILL_IMG_NAME")) {
						v_vod.setStillImgName(selectFieldValue);
					} else if (selectFieldName.equals("THEME_YN")) {
						v_vod.setThemeYn(selectFieldValue);
					} else if (selectFieldName.equals("CLOSE_YN")) {
						v_vod.setCloseYn(selectFieldValue);
					} else if (selectFieldName.equals("ORDER_DATE")) {
						v_vod.setOrderDate(selectFieldValue);
					} else if (selectFieldName.equals("WEIGHT")) {
						v_vod.setWeight(selectFieldValue);
					} else if (selectFieldName.equals("SUPER_ID")) {
						superId = selectFieldValue;
					} else if (selectFieldName.equals("SUPER_NAME")) {
						superName = (selectFieldValue);
					}
					 if(!StringUtil.isEmpty(superId) && (!StringUtil.isEmpty(superName))) {
						v_vod.setAlbumId(superId);
						v_vod.setAlbumName(superName);
			            }
					
				}
				vodList.add(v_vod);
			}
			attList = vodList;
			break;
		default:
			break;
		}

		return attList;
	}

	/**
	 * 연관검색어 리스트 담기
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList getDramaResult(Result result) throws Exception {
		ArrayList wordList = new ArrayList();

		String relationKeywordString = result.getValue("dramaResult");
		if (relationKeywordString != null) {
			String[] relationKeywords = relationKeywordString.split("\t");

			if (relationKeywords.length > 0) {
				for (int i = 0; i < relationKeywords.length; i++) {
					if (i % 2 == 1) {
						wordList.add(relationKeywords[i]);
					}
				}
			}
		}
		return wordList;
	}

	private OrderBySet[] makeOrderBySet(IptvPreVO searchVo) throws Exception {
		OrderBySet[] orderBySet = null;
		ArrayList orderByList = new ArrayList();
		String section = searchVo.getSection();

		if (searchVo.getCollection().equals("IPTVPRE_AUTO_COMPLETE")) {
//			orderByList.add(new OrderBySet(true, "SORT_POINT", Protocol.OrderBySet.OP_NONE)); // 포인트
																								// 내림차순
			orderByList.add(new OrderBySet(true, "SORT_POINT", Protocol.OrderBySet.OP_PREWEIGHT)); // 포인트

		} else {
			if (searchVo.getCollection().equals("IPTV_PRE")) {

				String sort = searchVo.getSort();

				if (!"".equals(sort) && (sort != null)) {

					// 방영일 : dsort=10(dsort=11), 금액 정렬 : dsort=20(dsort=21)

					if (sort.equals("fsort=10")) {
						// System.out.println("10");
						orderByList.add(new OrderBySet(false, "ORDER_DATE", Protocol.OrderBySet.OP_NONE)); // 날짜
																											// 우선
																											// 오름차순
					} else if (sort.equals("fsort=11")) {
						orderByList.add(new OrderBySet(true, "ORDER_DATE", Protocol.OrderBySet.OP_NONE)); // 날짜
																											// 우선
																											// 내림차순
					} else if (sort.equals("fsort=20")) {
						orderByList.add(new OrderBySet(false, "SORT_CAT_NAME_CHAR", Protocol.OrderBySet.OP_PREWEIGHT)); // 카테고리명
																														// 가나다
																														// 오름차순
					} else if (sort.equals("fsort=21")) {
						orderByList.add(new OrderBySet(true, "SORT_CAT_NAME_CHAR", Protocol.OrderBySet.OP_PREWEIGHT)); // 카테고리명
																														// 가나다
																														// 내림차순
					}

				} else {
					orderByList.add(new OrderBySet(true, "SORT_CAT_GB_IDX", Protocol.OrderBySet.OP_POSTWEIGHT)); //
				}

			} else if (searchVo.getCollection().equals("IPTV_PRE_CHANNEL")) {
				orderByList.add(new OrderBySet(true, "SORT_START_BROAD_TIME", Protocol.OrderBySet.OP_NONE)); // 날짜
			}
		}

		if (orderByList.size() > 0) {
			orderBySet = new OrderBySet[orderByList.size()];
			for (int i = 0; i < orderByList.size(); i++) {
				orderBySet[i] = (OrderBySet) orderByList.get(i);
			}
		}

		return orderBySet;
	}

	private FilterSet[] makeFilterSet(IptvPreVO searchVo) throws Exception {
		FilterSet[] filterSet = null;
		ArrayList filterList = new ArrayList();
		String section = searchVo.getSection();
		String sort = searchVo.getSort();
		String trunc = searchVo.getTrunc();
		String p = searchVo.getP();
		String d = searchVo.getD();

		String region1 = searchVo.getRegion1();
		String region3 = searchVo.getRegion2();
		String region2 = searchVo.getRegion3();

		if (searchVo.getCollection().equals("IPTV_PRE_CHANNEL")) {

			String[] arr = null;
			if (d != null && !"".equals(d)) {
				arr = d.split("~");
				arr[0] = "20" + arr[0];
				arr[1] = "20" + arr[1];
			} else {
				Calendar cal = java.util.Calendar.getInstance();
				// 현재 년도, 월, 일
				String year = String.valueOf(cal.get(cal.YEAR));
				String month = String.valueOf(cal.get(cal.MONTH) + 1);
				int m = cal.get(cal.MONTH) + 1;
				if (m < 10) {
					month = "0" + month;
				}
				String day = String.valueOf(cal.get(cal.DATE));
				int date = cal.get(cal.DATE);
				if (date < 10) {
					day = "0" + day;
				}

				String hour = String.valueOf(cal.get(cal.HOUR_OF_DAY));
				int h = cal.get(cal.HOUR_OF_DAY);
				if (h < 10) {
					hour = "0" + hour;
				}
				String min = String.valueOf(cal.get(cal.MINUTE));
				int s = cal.get(cal.MINUTE);
				if (s < 10) {
					min = "0" + min;
				}
				arr = new String[] { year + month + day + hour + min, "202912312359" };
			}

			filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_END_BROAD_TIME", arr));

			// 방송대역 설정
			ArrayList<String> arrArea = new ArrayList<String>();
			String[] arr_area = null;
			if ((region1 != null && !region1.equals("")) || (region2 != null && !region2.equals("")) || region3 != null && !region3.equals("")) {
				arrArea.add("0");
				arrArea.add("250");
				if (region1 != null && !region1.equals("")) {
					arrArea.add(region1);
				}
				if (region2 != null && !region2.equals("")) {
					arrArea.add(region2);
				}
				if (region3 != null && !region3.equals("")) {
					arrArea.add(region3);
				}

				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_LOCAL_AREA", arrArea.toArray(new String[0])));
			}
			if (searchVo.getKidsyn().equalsIgnoreCase("y")) {
				String [] kidChaFliter = searchVo.getIptvPreChaList().split(",");
				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_KIDS_CHA", kidChaFliter));
			}
		}
		
		if(searchVo.getCollection().equals("IPTV_PRE") && searchVo.getKidsyn().equalsIgnoreCase("y")) {
				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_NSC_GB", "KID"));
		}
		if(searchVo.getCollection().equals("IPTV_PRE") && ((searchVo.getSection().equals("IPTV_UF"))||(searchVo.getSection().equals("IPTV_UFPLUS")))) {
			filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_UF_GB", "UFX"));
			if(searchVo.getKidsyn().equalsIgnoreCase("y")) {
				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_NSC_GB", "KID"));
			}
		}
		if(searchVo.getCollection().equals("IPTVPRE_AUTO_COMPLETE")){
			if (searchVo.getKidsyn().equalsIgnoreCase("y")){
				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_NSC_GB", "Y"));
				if(searchVo.getOption().equalsIgnoreCase("n")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ALBUM_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("a")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ACTOR_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("d")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_DIRECTOR_GB", "Y"));
				}
			}else if(searchVo.getP().equalsIgnoreCase("p")){
				filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_LTE_PLUS", "Y"));
				if(searchVo.getOption().equalsIgnoreCase("n")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ALBUM_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("a")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ACTOR_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("d")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_DIRECTOR_GB", "Y"));
				}
			}else {
				if(searchVo.getOption().equalsIgnoreCase("n")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ALBUM_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("a")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_ACTOR_GB", "Y"));
				}else if(searchVo.getOption().equalsIgnoreCase("d")) {
					filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_DIRECTOR_GB", "Y"));
				}
			}
		}
		
			if (filterList.size() > 0) {
				filterSet = new FilterSet[filterList.size()];
				for (int i = 0; i < filterList.size(); i++) {
					filterSet[i] = (FilterSet) filterList.get(i);
				}
			}
		
		return filterSet;
	}

	/**
	 * KEYWORD 100 byte 자르기
	 * 
	 * @param str
	 * @return
	 */
	public String getByteLength(String str, int keyword_byte) {

		if (str != null) {
			str = str.trim();
		}

		int strLength = 0;
		char tempChar[] = new char[str.length()];
		String resultStr = str;

		for (int i = 0; i < tempChar.length; i++) {
			tempChar[i] = str.charAt(i);

			if (strLength > keyword_byte) {
				resultStr = str.substring(0, i - 1);
				break;
			}

			if (tempChar[i] < 128) {
				strLength++;
			} else {
				strLength += 2;
			}
		}

		return resultStr;
	}

	/**
	 * GroupBySet 만들기
	 * 
	 * @param voSearch
	 * @return groupbySet
	 */

	private GroupBySet[] getGroupBySet(IptvPreVO voSearch) {

		GroupBySet[] groupBySet = null;

		groupBySet = new GroupBySet[1];
		groupBySet[0] = new GroupBySet("GROUP_SECTION", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC");
		return groupBySet;
	}

	/**
	 * 그룹결과 맵에 담기
	 * 
	 * @param result
	 * @param query
	 * @param voSearch
	 * @return
	 */
	private List<Json_Category> getJsonGroupResultMap(Result result) {

		List<Json_Category> resultList = new ArrayList<Json_Category>();
		GroupResult[] groupResultlist = null;

		if (result != null && result.getGroupResultSize() != 0) {
			groupResultlist = result.getGroupResults(); // 상태,구분 = 2개

			HashMap listType = new HashMap();

			for (int i = 0; i < 1; i++) {
				int docu_life_sum = 0;
				for (int k = 0; k < groupResultlist[i].groupResultSize(); k++) {

					Json_Category vo = new Json_Category();
					String tempCode = new String(groupResultlist[i].getId(k)).trim();
					int tempCount = groupResultlist[i].getIntValue(k);

					if (tempCode.equals("LTE_DOCU") || tempCode.equals("LTE_LIFE")) {
						docu_life_sum += tempCount;
					} else {
						vo.setCat_name(tempCode);
						vo.setTotcnt(String.valueOf(tempCount));

						resultList.add(vo);
					}

				}

				if (docu_life_sum > 0) {
					Json_Category voo = new Json_Category();
					voo.setCat_name("LTE_LIFE");
					voo.setTotcnt(String.valueOf(docu_life_sum));

					resultList.add(voo);

				}

			}

		}

		List<Json_Category> resultList_after = new ArrayList<Json_Category>();

		int num = resultList.size();
		int[] score = new int[num];
		int[] rank = new int[num];

		for (int ck = 0; ck < num; ck++) {
			score[ck] = Integer.parseInt((String) resultList.get(ck).getTotcnt());
		}

		// 각 섹션 정확도에 순위 정하기
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < num; j++) {
				if (score[i] < score[j])
					rank[i] = rank[i] + 1;
			}
		}

		// 정한 순위 정확도순 내림차순 정렬하기
		for (int p = 0; p < num; p++) {
			for (int q = 0; q < num; q++) {
				if (p == rank[q]) {
					resultList_after.add(resultList.get(q));
				}
			}
		}

		return resultList_after;
	}

}