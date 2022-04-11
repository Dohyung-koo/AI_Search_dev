package com.diquest.openapi.iptvott;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QueryParser;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.openapi.iptvott.request.IptvottRequest;

public class MakeQuery {
    public QuerySet makeQuerySet(IptvottRequest iptvottRequest) {
    	String w = iptvottRequest.getW();
    	
    	QuerySet querySet = null;
        // queryIntent 하나 고정 (추가되면 사용가능)
    	if(w.equals("IPTV_OTT_SEARCH")) {
    		int queryCount = (iptvottRequest.getSection() != null && iptvottRequest.getSection().size() > 0) ? iptvottRequest.getSection().size() : 2;
    		querySet =  new QuerySet(queryCount);
            System.out.println("쿼리 카운트 : "+ queryCount);
            
            Query[] queryArr = new Query[queryCount];
            for(int i = 0; i < queryCount; i++) {
            	String section = "";
            	
            	if(iptvottRequest.getSection().size() > 0) section = iptvottRequest.getSection().get(i);
                String collectionName = "";
                // collection 구분 OTT_VOD / OTT_CHANNEL
                if("IPTV_CHA".equals(section)) { //OTT_CHANNEL
                	collectionName = "I30_CHANNEL";
                	iptvottRequest.setOttId("");
                } else if("IPTV_VOD".equals(section)) { //OTT_VOD
                	collectionName = "OTT_VOD";
                	iptvottRequest.setOttId("");
                } else if(!section.equals("") && section.substring(0, 2).equals("OT")) {
                	collectionName = "OTT_VOD";
                	iptvottRequest.setOttId(section);
                } else {
                	if(i == 0) {
                		collectionName = "OTT_VOD";
                    	iptvottRequest.setOttId("");	
                	} else {
                		collectionName = "I30_CHANNEL";
                    	iptvottRequest.setOttId("");
                	}
                }
                
                iptvottRequest.setCollectionName(collectionName);

                queryArr[i] = makeQuery(iptvottRequest);

                queryArr[i].setLoggable(true);

                querySet.addQuery(queryArr[i]);
            }

            return querySet;
    	}else {
    		querySet =  new QuerySet(1);
            Query[] queryArr = new Query[1];
            for(int i = 0; i < 1; i++) {
                // collection 구분 OTT_VOD / OTT_CHANNEL
                
                iptvottRequest.setCollectionName("OTT_AUTO_COMPLETE");

                queryArr[i] = makeQuery(iptvottRequest);
                querySet.addQuery(queryArr[i]);
            }
            return querySet;
    	}
    }
    
    private Query makeQuery(IptvottRequest iptvottRequest) {
    	String w = iptvottRequest.getW();
		String collectionName = iptvottRequest.getCollectionName();

        //이부분 "이정재 나오는 재미있는 영화 틀어줘" 하게 될 경우 어떻게 cast,genre,keyword로 나눔?? 이후에 이부분 수정해야할듯
		String q = w.equals("IPTV_OTT_SEARCH") ? "" : iptvottRequest.getStructuredQuery().getQuery();
		if(w.equals("IPTV_OTT_SEARCH")) {

			if(iptvottRequest.getStructuredQuery().getMeta() == null) { //meta가 없는 경우
				q = iptvottRequest.getStructuredQuery().getQuery();
				iptvottRequest.getStructuredQuery().setQuery(q);
			}
		}
        
        int pagenum = Integer.valueOf(iptvottRequest.getPage());
        int outmax = Integer.valueOf(iptvottRequest.getOutmax());

        int startnum = (pagenum - 1) * outmax; // 시작번호
        int endnum = 0;
        endnum = startnum + outmax - 1;

        SelectSet[] selectSet = makeSelectSet(iptvottRequest);
        WhereSet[] whereSet = makeWhereSet(iptvottRequest);
        
        FilterSet[] filterSet = null;
        GroupBySet[] groupbySet = null;
        OrderBySet[] orderBySet = null;
        
        //orderBySet = makeOrderBySet(iptvottRequest);
        
        if(w.equals("IPTV_OTT_SEARCH")) {
        	filterSet = makeFilterSet(iptvottRequest);
            
            if(iptvottRequest.getCollectionName().equals("OTT_VOD")){
                groupbySet = getGroupBySet(iptvottRequest);
            }	
        }
        
        Query query = new Query("<strong>", "</strong>");
        query.setSearchOption(Protocol.SearchOption.CACHE);
        query.setSelect(selectSet);
        query.setWhere(whereSet);
        
        if (orderBySet != null) {
            query.setOrderby(orderBySet);
        }
        if (filterSet != null && filterSet.length > 0) {
            query.setFilter(filterSet);
        }
        if (groupbySet != null) {
            query.setGroupBy(groupbySet);
        }

        query.setValue("synYn", "N");
        query.setValue("searchTerm", q);

        query.setFrom(collectionName);

        query.setResult(startnum, endnum);

        query.setDebug(true);
        query.setPrintQuery(false);
        query.setSearchKeyword(q);
        query.setLogKeyword(q.toCharArray());

		 QueryParser queryParser = new QueryParser();
		 System.out.println("### Query : " + queryParser.queryToString(query));
        return query;
    }

    private FilterSet[] makeFilterSet(IptvottRequest iptvottRequest) {
        FilterSet[] filterSet = null;
        
        ArrayList filterList = new ArrayList();
        
        if(!(iptvottRequest.getOttId().equals("") || iptvottRequest.getOttId() == null)) {
        	filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_OTT_ID", iptvottRequest.getOttId()));	
        }
        
		//여기서 현재날짜시간가져와서 START_BROAD_TIME과 END_BROAD_TIME 사이에 있으면 방송중 +1, END_BROAD_TIME보다 크면 방송예정 +1
        //그리고 section 빈값보내면 필터걸어서 START_BROAD_TIME과 END_BROAD_TIME 사이인 방송중 데이터만 가져오기
		
        //실시간 채널 section이 비어있는 경우(방송중만 표시)
        if(iptvottRequest.getCollectionName().equals("I30_CHANNEL")
        && (iptvottRequest.getSection() == null || iptvottRequest.getSection().size() < 1)) {
            String[] startArr = null;
            String[] endArr = null;
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
    		
    		startArr = new String[] { year + month + day + "0000", year + month + day + hour + min };
    		endArr = new String[] { year + month + day + hour + min, "205012312359" };
    		
    		filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_START_BROAD_TIME", startArr));
    		filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_END_BROAD_TIME", endArr));	
        }

		if (filterList.size() > 0) {
		    filterSet = new FilterSet[filterList.size()];
		    for (int i = 0; i < filterList.size(); i++) {
		    	filterSet[i] = (FilterSet) filterList.get(i);
		    }
		}
        return filterSet;
    }

    private OrderBySet[] makeOrderBySet(IptvottRequest iptvottRequest) {
    	String sort = iptvottRequest.getSort();
        OrderBySet[] orderBySet = null;
        
        ArrayList orderByList = new ArrayList();
        
        String collectionName = iptvottRequest.getCollectionName();
        String w = iptvottRequest.getW();
        
        if(w.equals("IPTV_OTT_SEARCH")) {
            switch (collectionName)  {
		        case "OTT_VOD":
					if(sort.equals("2")) {
						orderByList.add(new OrderBySet(false, "SORT_OTT_GRP_NAME", Protocol.OrderBySet.OP_POSTWEIGHT)); //가나다순 오름차순
					} else if(sort.equals("3")) {
						orderByList.add(new OrderBySet(true, "SORT_RELEASE_DATE", Protocol.OrderBySet.OP_POSTWEIGHT)); //최신순 내림차순
					} else {
						//OP_POSTWEIGHT -> 필드와 가중치 이중정렬인데 필드 우선
						//OP_PREWEIGHT -> 필드와 가중치 이중정렬인데 가중치 우선
						//OP_NONE -> 필드로만 정렬
						//출력된데이터를 그대로 인덱싱하는거는 false, 역순으로 인덱싱을 하면 true
						orderByList.add(new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE));//정확도 내림차순
					}
				break;
				
		        case "I30_CHANNEL":
					if(sort.equals("2")) {
						orderByList.add(new OrderBySet(false, "SORT_PROGRAM_NAME_CHAR", Protocol.OrderBySet.OP_POSTWEIGHT)); //가나다순 오름차순
					} else if(sort.equals("3")) {
						orderByList.add(new OrderBySet(false, "SORT_START_BROAD_TIME", Protocol.OrderBySet.OP_POSTWEIGHT)); //최신순 내림차순
					} else {
						//OP_POSTWEIGHT -> 필드와 가중치 이중정렬인데 필드 우선
						//OP_PREWEIGHT -> 필드와 가중치 이중정렬인데 가중치 우선
						//OP_NONE -> 필드로만 정렬
						//출력된데이터를 그대로 인덱싱하는거는 false, 역순으로 인덱싱을 하면 true
						orderByList.add(new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE));//정확도 내림차순
					}
		        break;
            }
			
    		
            if (orderByList.size() > 0) {
                orderBySet = new OrderBySet[orderByList.size()];
                for (int i = 0; i < orderByList.size(); i++) {
                    orderBySet[i] = (OrderBySet) orderByList.get(i);
                }
            }	
        }

        return orderBySet;
    }

    private WhereSet[] makeWhereSet(IptvottRequest iptvottRequest) {

        WhereSet[] whereSet = null;
        ArrayList whereList = new ArrayList();
        
        String q 		= ""; //meta가 null일때는 q, 있으면 contents
        List castList 		= new ArrayList<>();
        String director = "";
        String genre 	= "";
        String nation 	= "";
        String season 	= "";
        String episode 	= "";
        String pg 		= "";
        String pay 		= "";
        String ott 		= "";
        List keywordList 	= new ArrayList<>();
        
        String collectionName = iptvottRequest.getCollectionName();
        
        if(iptvottRequest.getStructuredQuery().getMeta() == null) {
        	q = iptvottRequest.getStructuredQuery().getQuery();
        }else {
        	q 			= iptvottRequest.getStructuredQuery().getMeta().getContents();
        	castList	= iptvottRequest.getStructuredQuery().getMeta().getCast();
        	genre 		= iptvottRequest.getStructuredQuery().getMeta().getGenre();
        	nation 		= iptvottRequest.getStructuredQuery().getMeta().getNation();
        	season 		= iptvottRequest.getStructuredQuery().getMeta().getSeason();
        	episode 	= iptvottRequest.getStructuredQuery().getMeta().getEpisode();
        	pg 			= iptvottRequest.getStructuredQuery().getMeta().getPg();
        	pay 		= iptvottRequest.getStructuredQuery().getMeta().getPay();
        	ott 		= iptvottRequest.getStructuredQuery().getMeta().getOtt();
        	keywordList = iptvottRequest.getStructuredQuery().getMeta().getKeyword();
        }
        
        String w = iptvottRequest.getW();
        String rating = iptvottRequest.getRating();
        
        if(w.equals("IPTV_OTT_SEARCH")) {
            switch (collectionName)  {
            case "OTT_VOD":
            	whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
            	
            	if(iptvottRequest.getStructuredQuery().getMeta() == null) {
                    whereList.add(new WhereSet("IDX_OTT_GRP_NAME_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_PLAYER", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_DIRECTOR", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_DISTRUBUTOR_NAME_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OTT_GENRE_NAME_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_PRICE_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_COUNTRY_ORIGIN_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_POINT_F", Protocol.WhereSet.OP_HASALL, q));
                	whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                	whereList.add(new WhereSet("IDX_RATING_F", Protocol.WhereSet.OP_HASALL, rating));
                }else {
                	System.out.println("캐스트 사이즈 : "+castList.size());
                    if(castList.size() > 0) {
                    	System.out.println("여기!!!!!!");
                    	whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                    	for(int i = 0; i < castList.size(); i++) {
                    		whereList.add(new WhereSet("IDX_PLAYER", Protocol.WhereSet.OP_HASALL, String.valueOf(castList.get(i)))); //주연
                    		if(i < castList.size() - 1) whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    	}
                    	whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                    	whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
                    if(!ott.equals("")) {
                		whereList.add(new WhereSet("IDX_OTT_GRP_NAME_B", Protocol.WhereSet.OP_HASALL, ott)); //OTT그룹명
                		whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                	}
                    if(!director.equals("")) {
                        whereList.add(new WhereSet("IDX_DIRECTOR", Protocol.WhereSet.OP_HASALL, director)); //감독
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
//                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
//                    whereList.add(new WhereSet("IDX_DISTRUBUTOR_NAME_B", Protocol.WhereSet.OP_HASALL, ott)); //배급사
                    if(!genre.equals("")) {
                        whereList.add(new WhereSet("IDX_OTT_GENRE_NAME_B", Protocol.WhereSet.OP_HASALL, genre)); //OTT장르명
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
//                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
//                    whereList.add(new WhereSet("IDX_PRICE_F", Protocol.WhereSet.OP_HASALL, pay)); //가격
                    if(!nation.equals("")) {
                        whereList.add(new WhereSet("IDX_COUNTRY_ORIGIN_B", Protocol.WhereSet.OP_HASALL, nation)); //제작국가
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
//                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
//                    whereList.add(new WhereSet("IDX_POINT_F", Protocol.WhereSet.OP_HASALL, q)); //별점
                    if(!rating.equals("")) {
                    	whereList.add(new WhereSet("IDX_RATING_F", Protocol.WhereSet.OP_HASALL, rating)); //시청등급	
                    }
                }
            	
            	whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                break;
             
            case "I30_CHANNEL":
            	whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
            	if(iptvottRequest.getStructuredQuery().getMeta() == null) {
                    whereList.add(new WhereSet("IDX_PROGRAM_NAME", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("P_NAME_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CHNL_KEYWORD", Protocol.WhereSet.OP_HASALL, q));	
            	}else {
                    if(castList.size() > 0) {
                    	for(int i = 0; i < castList.size(); i++) {
                    		whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, String.valueOf(castList.get(i)))); //주연
                    		if(i < castList.size() - 1) whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    	}
                    	whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
                    
                    if(!q.equals("")) {
                    	whereList.add(new WhereSet("IDX_PROGRAM_NAME", Protocol.WhereSet.OP_HASALL, q)); //프로그램 명
                    	whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
            		}
                    
                    if(!director.equals("")) {
                        whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, director)); //감독
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }
//                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
//                    whereList.add(new WhereSet("P_NAME_F", Protocol.WhereSet.OP_HASALL, q)); //방송사
                    if(keywordList.size() > 0) {
                    	for(int i = 0; i < keywordList.size(); i++) {
                    		whereList.add(new WhereSet("IDX_CHNL_KEYWORD", Protocol.WhereSet.OP_HASALL, String.valueOf(keywordList.get(i)))); //키워드
                    		if(i < castList.size() - 1) whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    	}
                    }
            	}
                whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
            break;

            default:
                break;
            }
        }else {
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("IDX_KEYWORD", Protocol.WhereSet.OP_HASALL, q));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_T", Protocol.WhereSet.OP_HASALL, q));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, q));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, q));
			whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereList.add(new WhereSet("IDX_KEYWORD_PR", Protocol.WhereSet.OP_HASALL, q));
		
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
        }


        whereSet = new WhereSet[whereList.size()];
        for (int i = 0; i < whereList.size(); i++) {
            whereSet[i] = (WhereSet) whereList.get(i);
        }

        return whereSet;
    }

    private SelectSet[] makeSelectSet(IptvottRequest iptvottRequest) {
    	String w = iptvottRequest.getW();
    	
        SelectSet[] selectSet = null;
        ArrayList selectList = new ArrayList();
        
        String collectionName = iptvottRequest.getCollectionName();
        
    	if(w.equals("IPTV_OTT_SEARCH")) {
            switch (collectionName) {
            case "OTT_VOD":
                selectList.add(new SelectSet("OTT_ALBUM_ID", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_ALBUM_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_ID", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_GRP_ID", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_GRP_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("RUNTIME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("PLAYER", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("DIRECTOR", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("DISTRUBUTOR_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("RELEASE_DATE", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("CREATE_YEAR", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_GENRE_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("RATING", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("PRICE", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("POSTER_FILE_NAME", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("STILL_FILE_NAME", Protocol.SelectSet.NONE));//
                selectList.add(new SelectSet("COUNTRY_ORIGIN", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("POINT", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("OTT_TYPE", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("LINK_URL", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("SUPER_ID", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("RESULT_TYPE", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("CAT_ID", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("SERIES_NO", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("TITLE", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("CLOSE_YN", Protocol.SelectSet.NONE)); //
                selectList.add(new SelectSet("SUB_LIST", Protocol.SelectSet.NONE)); //
                break;
            case "I30_CHANNEL":
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
                selectList.add(new SelectSet("CAPTION_FLAG", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("DVS_FLAG", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("IS_51_CH", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("FILTERING_CODE", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CHNL_KEYWORD", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CHNL_ICON_URL", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CHNL_ICON_FILE_NAME", Protocol.SelectSet.NONE));
                break;
            default:
                break;
            }	
    	}else { //자동완성
    		selectList.add(new SelectSet("KEYWORD", Protocol.SelectSet.NONE));
    	}

        selectSet = new SelectSet[selectList.size()];
        for (int i = 0; i < selectList.size(); i++) {
            selectSet[i] = (SelectSet) selectList.get(i);
        }

        return selectSet;
    }
    /**
     * GroupBySet 만들기
     *
     * @param voSearch
     * @return groupbySet
     */
    private GroupBySet[] getGroupBySet(IptvottRequest iptvottRequest) {

        GroupBySet[] groupBySet = null;

        groupBySet = new GroupBySet[1];
        groupBySet[0] = new GroupBySet("GROUP_OTT_ID", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "ASC");

        return groupBySet;
    }
}
