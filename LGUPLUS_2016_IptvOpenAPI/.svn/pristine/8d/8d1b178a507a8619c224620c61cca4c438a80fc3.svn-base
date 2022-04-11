package com.diquest.openapi.preiptv;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;


public class MakeQuery {
	

	public QuerySet AutomakeQuerySet(IptvPreVO searchVO) throws Exception {
		
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String collectionName = "";
		String outmax = searchVO.getOutmax();
		int queryCount = 1;
		
		QuerySet querySet = new QuerySet(queryCount);
		Query[] queryArr = new Query[queryCount];
		
		if (w.equals("IPTV_PRE_QUICK")) {
			searchVO.setCollection("IPTVPRE_AUTO_COMPLETE");
			searchVO.setOutmax(outmax);
			queryArr[queryCount - 1] = makeQuery(searchVO);
			queryArr[queryCount - 1].setValue("search_type", "auto");
			querySet.addQuery(queryArr[queryCount - 1]);
		} 
		
		return querySet;
	}
	/**
	 * 검색 쿼리 목록 만들기
	 * @param searchVO
	 * @return
	 */
	public QuerySet makeQuerySet(IptvPreVO searchVO) throws Exception{
		//System.out.println("쿼리생성시작");
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String collectionName = "";
		String outmax = searchVO.getOutmax();
	
		// 통합검색 section 리스트
		String sectionList = searchVO.getSectionList();
		
		
		String[] sectionListArr = sectionList.split(",");
		

		// 순간검색 section 리스트
//		String quickList = searchVO.getQuickList();
//		String[] quickListArr = quickList.split(",");
		
		// 통합검색 여부 체크
		if(w !=null && w.equals("IPTV_PRE") && section != null && !section.equals("")) {
			sectionListArr = new String[]{section};
		} else if(StringUtils.isNotEmpty(w) && w.equals("IPTV_PRE_QUICK")) {
			sectionListArr = new String[]{"AUTO"};;
//			searchVO.setOutmax("1");
		}

		searchVO.setSectionListArr(sectionListArr);
		
		int queryCount = sectionListArr.length; // 세션수 
	

		//queryCount = queryCount ; 	// LTEVOD 는 연관검색어 쿼리 +1  ,  LTEVOD_QUICK는 자동완성 쿼리 +1
			
		QuerySet querySet = new QuerySet(queryCount);
		Query[] queryArr = new Query[queryCount];
		
		if(w.equals("IPTV_PRE")) {
			for (int i = 0; i < sectionListArr.length; i++) {
				
				section = sectionListArr[i];
				//System.out.println("section : "+section);
				searchVO.setSection(section);
				//collection 구분
				//System.out.println(section);
				if(StringUtils.equals("LTE_REP", section) 
						|| StringUtils.equals("LTE_MOV", section)
						|| StringUtils.equals("LTE_KIDS", section)) {
					//System.out.println("컨텐츠");
					collectionName = "IPTV_PRE";
				}else if(StringUtils.equals("LTE_CHA", section)) {
					collectionName = "I30_CHANNEL";
				}
				//System.out.println("collectionName : "+collectionName);
				searchVO.setCollection(collectionName);
				
				queryArr[i] = makeQuery(searchVO);
				
				queryArr[i].setLoggable(true);
				
				querySet.addQuery(queryArr[i]);
	
			
			}
		}else if (w.equals("IPTV_PRE_QUICK")) {
			searchVO.setCollection("IPTVPRE_AUTO_COMPLETE");
			searchVO.setOutmax(outmax);
			queryArr[queryCount - 1] = makeQuery(searchVO);
			queryArr[queryCount - 1].setValue("search_type", "auto");
			querySet.addQuery(queryArr[queryCount - 1]);
		} 
		//System.out.println("querySet.size() : "+querySet.querySize());
		return querySet;
	}
	
	/**
	 * 검색 쿼리 생성 
	 * @param searchVO
	 * @return
	 */
	private Query makeQuery(IptvPreVO searchVO) throws Exception{
		String w = searchVO.getW();
		String section = searchVO.getSection();
		String collectionName = searchVO.getCollection();
		int pagenum = Integer.parseInt( searchVO.getPg() );	// 페이지번호
	
		int outmax = Integer.parseInt( searchVO.getOutmax() );	// 목록개수
		int startnum = ( pagenum-1 ) * outmax ;	// 시작번호
	
		int endnum = 0;
		
		
		endnum = startnum +outmax-1 ;
		/*System.out.println("세부쿼리생성");
		System.out.println("getVrConSelect: "+searchVO.getVrConSelect());
		*/
		SelectSet[] selectSet = makeSelectSet(searchVO);
		WhereSet[] whereSet = makeWhereSet(searchVO);
		OrderBySet[] orderBySet = makeOrderBySet(searchVO);
		FilterSet[] filterSet = makeFilterSet(searchVO);
		
		Query query = new Query("<strong>", "</strong>");
		query.setSearchOption(Protocol.SearchOption.CACHE);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		if ( orderBySet != null) {
			query.setOrderby(orderBySet);
		}
		if ( filterSet != null) {
			query.setFilter(filterSet);
		}
		query.setValue("synYn", "N");
		query.setValue("searchTerm", searchVO.getQ());
		query.setValue("section", searchVO.getSection());

		query.setFrom(collectionName);
		
		if (collectionName.equals("IPTV_PRE")) {
			//query.setProfile(section);
			query.setRankingOption(Protocol.RankingOption.DOCUMENT_RANKING);
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

		query.setValue("stcflag", "N");
		/*QueryParser queryParser = new QueryParser();
		System.out.println("### Query : " + queryParser.queryToString(query));
		*/return query;
	}
	
	/**
	 * SelectSet 구성 
	 * @param searchInfo
	 * @return
	 */
	private SelectSet[] makeSelectSet(IptvPreVO searchVO) throws Exception{
		
		SelectSet[] selectSet = null;
		ArrayList selectList = new ArrayList();
		String collectionname = searchVO.getCollection();
		//System.out.println("makeSelectSet collectionname : "+collectionname);
		if ("IPTV_PRE".equals(collectionname)) {
			for(String selectVal : searchVO.getIptvPreSelect()) {
				//System.out.println("SELECT : "+selectVal);
				selectList.add(new SelectSet(selectVal, Protocol.SelectSet.NONE));
			}
			
		} else if("I30_CHANNEL".equals(collectionname)) {
			for(String selectVal : searchVO.getIptvPreChaSelect()) {
				//System.out.println("SELECT : "+selectVal);
				selectList.add(new SelectSet(selectVal, Protocol.SelectSet.NONE));
			}
		} else if ("IPTVPRE_AUTO_COMPLETE".equals(collectionname)) {
			selectList.add(new SelectSet("KEYWORD", Protocol.SelectSet.NONE));
		}

		selectSet = new SelectSet[selectList.size()];
		for( int i=0 ; i<selectList.size() ; i++ ) {
			selectSet[i] = (SelectSet) selectList.get(i);
		}
		
		return selectSet;
	}
	
	/**
	 * WhereSet 구성
	 * @param searchVO
	 * @return
	 */
	private WhereSet[] makeWhereSet(IptvPreVO searchVO) throws Exception{
		
		WhereSet[] whereSet = null;
		ArrayList whereList = new ArrayList();
		String Collection = searchVO.getCollection();
		String section = searchVO.getSection();
		//System.out.println("makeWhereSet : "+Collection);
		if (StringUtils.equals("IPTV_PRE", Collection)) {
	/*
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			
			for (int i = 0; i < searchVO.getIptvPreWhere().length; i++) {
				//System.out.println("\t"+searchVO.getKidsVodWhere()[i]);
				whereList.add(new WhereSet(searchVO.getIptvPreWhere()[i], Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				if (i != searchVO.getIptvPreWhere().length - 1) {
					//System.out.println("OR");
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				}
			}
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			*/
			
			String p = searchVO.getP(); // p 파라미터 분야제한
			if (p == null || "".equals(p) || "20".equals(p)) {
				p = "01,02,03,04,05";
			}
			String[] p_arr = p.split(",");
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("IDX_PR_INFO_F", Protocol.WhereSet.OP_HASANY, p_arr));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));

			if (searchVO.getW().equals("IPTV_PRE_QUICK")) {
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
				if (searchVO.getSection().equals("") || searchVO.getSection() == null || searchVO.getSection().equals("IPTV_TOTAL")) {
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

				// 복합 인덱스
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_MULTI_FILED", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PALB"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "PCAT"));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));


				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

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
			}
			
			
		}else if (StringUtils.equals("I30_CHANNEL", Collection)) {
	
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			
			for (int i = 0; i < searchVO.getIptvPreChaWhere().length; i++) {
				//System.out.println("\t"+searchVO.getKidsChaWhere()[i]);
				whereList.add(new WhereSet(searchVO.getIptvPreChaWhere()[i], Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				if (i != searchVO.getIptvPreChaWhere().length - 1) {
					//System.out.println("OR");
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				}
			}
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		} else if (StringUtils.equals("IPTVPRE_AUTO_COMPLETE", Collection)) {

			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			
				String q = searchVO.getQ().replaceAll(" ", "");
				whereList.add(new WhereSet("IDX_KEYWORD", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_KEYWORD_T", Protocol.WhereSet.OP_HASALL, q));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("IDX_KEYWORD_PR", Protocol.WhereSet.OP_HASALL, searchVO.getQ()));
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

		}
	
		whereSet = new WhereSet[whereList.size()];
		for( int i=0 ; i<whereList.size() ; i++ ) {
			whereSet[i] = (WhereSet)whereList.get(i);
		}

		return whereSet;
	}
	
	private OrderBySet[] makeOrderBySet( IptvPreVO searchVo ) throws Exception{
		OrderBySet[] orderBySet = null;
		ArrayList orderByList = new ArrayList();
		String section = searchVo.getSection();

		String sort = searchVo.getSort();
		if (searchVo.getCollection().equals("IPTVPRE_AUTO_COMPLETE")) {
			orderByList.add(new OrderBySet(true, "ORDER_KEYWORD", Protocol.OrderBySet.OP_PREWEIGHT)); // 포인트
																								// 내림차순
		} else if (searchVo.getCollection().equals("IPTV_PRE")
		/* || searchVo.getCollection().equals("KIDS_CHA") */) {
			if (StringUtils.equals(sort, "21")) {
				orderByList.add(new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE));
			} else {

				// System.out.println("DESC"); 과거검색
				orderByList.add(new OrderBySet(false, "SORT_ORDER_DATE", Protocol.OrderBySet.OP_POSTWEIGHT));

			}

		}else if (searchVo.getCollection().equals("I30_CHANNEL")) {
			if (StringUtils.equals(sort, "21")) {
				orderByList.add(new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE));
			} else{
				orderByList.add(new OrderBySet(false, "SORT_START_BROAD_TIME", Protocol.OrderBySet.OP_POSTWEIGHT)); // 날짜
			}
	// 우선
			// 내림차순
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
		String d = searchVo.getD();

		String[] arr = null;
		if (d != null && !"".equals(d)) {
			arr = d.split("~");
			arr[0] = "20" + arr[0];
			arr[1] = "20" + arr[1];
		} else {
			Calendar cal = java.util.Calendar.getInstance();
			// 현재 년도, 월, 일
			String year = String.valueOf(cal.get(Calendar.YEAR));
			String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			int m = cal.get(Calendar.MONTH) + 1;
			if (m < 10) {
				month = "0" + month;
			}
			String day = String.valueOf(cal.get(Calendar.DATE));
			int date = cal.get(Calendar.DATE);
			if (date < 10) {
				day = "0" + day;
			}

			String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
			int h = cal.get(Calendar.HOUR_OF_DAY);
			if (h < 10) {
				hour = "0" + hour;
			}
			String min = String.valueOf(cal.get(Calendar.MINUTE));
			int s = cal.get(Calendar.MINUTE);
			if (s < 10) {
				min = "0" + min;
			}
			arr = new String[] { year + month + day + hour + min, "202112312359" };
		}

		
		if (searchVo.getCollection().equals("IPTV_PRE")) {
			filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_SECTION", section));
			//filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_ORDER_DATE", arr));
			
		} else if("I30_CHANNEL".equals(searchVo.getCollection())) {
			String[] kidChaFliter = {"EBS kids","어린이TV","브라보키즈"}; 
			filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_SERVICE_NAME", kidChaFliter));
			filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_END_BROAD_TIME", arr));
		}


		if (filterList.size() > 0) {
			filterSet = new FilterSet[filterList.size()];
			for (int i = 0; i < filterList.size(); i++) {
				filterSet[i] = (FilterSet) filterList.get(i);
			}
		}

		return filterSet;
	}

}

