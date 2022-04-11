package com.diquest.openapi.google;

import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.*;
import com.diquest.openapi.google.request.Entities;
import com.diquest.openapi.google.request.GoogleRequest;
import com.diquest.openapi.google.request.StructuredQuery;
import com.diquest.openapi.google.response.ErrorString;
import com.diquest.openapi.videopotal.VideoVO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class MakeQuery {
    public QuerySet makeQuerySet(GoogleRequest googleRequest) {

        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();

        // queryIntent 하나 고정 (추가되면 사용가능)
        int queryCount = 1;

        QuerySet querySet = new QuerySet(queryCount);
        Query[] queryArr = new Query[queryCount];
        for(int i = 0; i < queryCount; i++) {
            String collectionName = "";
            // collection 구분
            switch (queryIntent) {
                case "SWITCH_CHANNEL": // 대박영상
                    collectionName = "I30_CHANNEL";
                    break;
                default: // VOD
                    collectionName = "GOOGLE";
                    break;
            }

            googleRequest.setCollectionName(collectionName);

            queryArr[i] = makeQuery(googleRequest);

            queryArr[i].setLoggable(true);

            querySet.addQuery(queryArr[i]);
        }

        return querySet;
    }

    private Query makeQuery(GoogleRequest googleRequest) {

        String collectionName = googleRequest.getCollectionName();
        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        String q = "";

        if("SWITCH_CHANNEL".equals(queryIntent)){
            if(StringUtils.isNotEmpty(googleRequest.getStructuredQuery().getChannelName())){
                q = googleRequest.getStructuredQuery().getChannelName();
            }else{
                q = googleRequest.getStructuredQuery().getChannelNumber();
            }
        }else{
            q = googleRequest.getStructuredQuery().getSearchQuery();
        }


        int pagenum = 1;
        int outmax = 20;
        if("ENTITY_SEARCH".equals(queryIntent)
                || "PLAY_TVM".equals(queryIntent)
                || "SWITCH_CHANNEL".equals(queryIntent)){
            outmax = 1;
        }
        int startnum = (pagenum - 1) * outmax; // 시작번호
        int endnum = 0;
        endnum = startnum + outmax - 1;

        SelectSet[] selectSet = makeSelectSet(googleRequest);
        WhereSet[] whereSet = makeWhereSet(googleRequest);
        OrderBySet[] orderBySet = makeOrderBySet(googleRequest);
        FilterSet[] filterSet = makeFilterSet(googleRequest);
        GroupBySet[] groupbySet = null;
        /*
        if(StringUtils.equals("SEARCH",queryIntent) || StringUtils.equals("PLAY",queryIntent) ){
            groupbySet = getGroupBySet(googleRequest);
        }
*/
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
        /*if (groupbySet != null) {
            query.setGroupBy(groupbySet);
        }*/

        query.setValue("synYn", "N");
        query.setValue("searchTerm", googleRequest.getStructuredQuery().getSearchQuery());

        query.setFrom(collectionName);
        if (collectionName.equals("GOOGLE")) {

            query.setRankingOption((byte) Protocol.RankingOption.DOCUMENT_RANKING);
        }

        query.setResult(startnum, endnum);

        query.setDebug(true);
        query.setPrintQuery(false);
        query.setSearchKeyword(q);
        query.setLogKeyword(q.toCharArray());

        // query.setLoggable(true);
        query.setValue("search_type", "total");
        // 금지어, 불용어 사용함
        query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
        // 동의어, 유의어 확장
        query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));

        query.setValue("stcflag", "N");

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
        query.setValue("typo-parameters", q);

		 QueryParser queryParser = new QueryParser();
//		 System.out.println("### Query : " + 	 queryParser.queryToString(query));
        return query;
    }




    private FilterSet[] makeFilterSet(GoogleRequest googleRequest) {
        FilterSet[] filterSet = null;
        ArrayList filterList = new ArrayList();

        String[] regionArr = StringUtils.split(googleRequest.getRegionStr(),"|");

//        System.out.println(regionArr.length);
        String region1 = "";
        String region3 = "";
        String region2 = "";
        int regionArrLength = regionArr.length;
        for (int i = 0; i < regionArrLength; i++) {
            if(regionArrLength == 3) {
                region1 = regionArr[0];
                region2 = regionArr[1];
                region3 = regionArr[2];
            }else if (regionArrLength == 2) {
                region1 = regionArr[0];
                region2 = regionArr[1];
            }else{
                region1 = regionArr[0];
            }
        }
        String d = "";
        if (googleRequest.getCollectionName().equals("I30_CHANNEL")) {

            String[] arr = null;
            /* 기간 지금 이순간 이후
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
                arr = new String[] { year + month + day + hour + min, "202112312359" };
            }

            filterList.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "FILTER_END_BROAD_TIME", arr));
*/
            // 방송대역 설정
            ArrayList<String> arrArea = new ArrayList<String>();
//            String[] arr_area = null;
            arrArea.add("0");
            arrArea.add("250");
                
                if (region1 != null && !region1.equals("")) {
                    googleRequest.setRegionStr(region1);
                    arrArea.add(region1);
                }
                if (region2 != null && !region2.equals("")) {
                    googleRequest.setRegionStr(region2);
                    arrArea.add(region2);
                    
                }
               if (region3 != null && !region3.equals("")) {
                    googleRequest.setRegionStr(region3);
                    arrArea.add(region3);
                }
                filterList.add(new FilterSet(Protocol.FilterSet.OP_MATCH, "FILTER_LOCAL_AREA", arrArea.toArray(new String[0])));
            }
            if (filterList.size() > 0) {
                filterSet = new FilterSet[filterList.size()];
                for (int i = 0; i < filterList.size(); i++) {
                    filterSet[i] = (FilterSet) filterList.get(i);
                }
            }
        return filterSet;
    }




    private OrderBySet[] makeOrderBySet(GoogleRequest googleRequest) {
        OrderBySet[] orderBySet = null;
        ArrayList orderByList = new ArrayList();

        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        //String section = searchVo.getSection();
        String collectionName = googleRequest.getCollectionName();
        Entities entities =
                googleRequest.getStructuredQuery().getEntitiesList() == null ? null : googleRequest.getStructuredQuery().getEntitiesList().get(0);

        if (collectionName.equals("GOOGLE")) {
            if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent)){
                if(entities != null &&StringUtils.isNotEmpty(entities.getEntityType())) {
                    String entityType = entities.getEntityType();
                    if(StringUtils.equals("TV_SHOW",entityType)){
                    	//가중치 전 TYPE값으로 정렬
                    	orderByList.add(new OrderBySet(true, "SORT_ALBUM_LEN", Protocol.OrderBySet.OP_PREWEIGHT)); //
//                        orderByList.add(new OrderBySet(true, "SORT_TYPE", Protocol.OrderBySet.OP_POSTWEIGHT)); //
                        orderByList.add(new OrderBySet(true, "SORT_RESULT_TYPE", Protocol.OrderBySet.OP_POSTWEIGHT)); //
                    }else if(StringUtils.equals("MOVIE",entityType)) {
                    	orderByList.add(new OrderBySet(true, "SORT_ALBUM_LEN", Protocol.OrderBySet.OP_PREWEIGHT)); //
                    	orderByList.add(new OrderBySet(true, "SORT_RESULT_TYPE_ORDER", Protocol.OrderBySet.OP_POSTWEIGHT)); //
                    }
                }

                orderByList.add(new OrderBySet(false, "SORT_SERIES_NO", Protocol.OrderBySet.OP_NONE)); //
            }else{
                orderByList.add(new OrderBySet(true, "SORT_SERIES_NO", Protocol.OrderBySet.OP_PREWEIGHT)); //
            }

        } else if (collectionName.equals("I30_CHANNEL")) {
            //orderByList.add(new OrderBySet(true, "SORT_START_BROAD_TIME", Protocol.OrderBySet.OP_NONE)); // 날짜
//        	orderByList.add(new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE));
            orderByList.add(new OrderBySet(true, "SORT_LOCAL_AREA", Protocol.OrderBySet.OP_POSTWEIGHT));
            
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

    private WhereSet[] makeWhereSet(GoogleRequest googleRequest) {

        WhereSet[] whereSet = null;
        ArrayList whereList = new ArrayList();
        String[] sectionArr = {"LTE_MOV","LTE_CHA","LTE_ANI","LTE_REP","LTE_KIDS","LTE_LIFE",};
        
        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();

        String collectionName = googleRequest.getCollectionName();
        String q = googleRequest.getQ();

/*        if("I30_CHANNEL". equals(collectionName)){
            q = googleRequest.getStructuredQuery().getChannelName();
        }else{
            StructuredQuery structuredQuery = googleRequest.getStructuredQuery();
            if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent)) {
                Entities e =structuredQuery.getEntitiesList().get(0);
                if(StringUtils.isNotEmpty(e.getContentId())){
                    q = e.getContentId();
                }else if(StringUtils.isNotEmpty(e.getTitle())){
                    q = e.getTitle();
                }else if(StringUtils.isNotEmpty(structuredQuery.getSearchQuery())){
                    q = googleRequest.getStructuredQuery().getSearchQuery();
                }

            }  else if("PLAY".equals(queryIntent) || "SEARCH".equals(queryIntent)) {
                q = structuredQuery.getSearchQuery();
            } else if("SWITCH_CHANNEL".equals(queryIntent)) {
                q = structuredQuery.getChannelName();
            }
        }*/

        switch (collectionName)  {
            case "I30_CHANNEL":
                if(StringUtils.equals(googleRequest.getSearchType(),"name")){
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

                    whereList.add(new WhereSet("IDX_SERVICE_NAME_F", Protocol.WhereSet.OP_HASALL, q.replaceAll(" ", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_SERVICE_NAME_B", Protocol.WhereSet.OP_HASALL, q.replaceAll(" ", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                }else if(StringUtils.equals(googleRequest.getSearchType(),"number")){
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

                    whereList.add(new WhereSet("IDX_CHANNEL_NO_F", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                }


                break;
            case "GOOGLE":

                if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent)){

                    Entities entities =
                            googleRequest.getStructuredQuery().getEntitiesList() == null ? null : googleRequest.getStructuredQuery().getEntitiesList().get(0);

                    if(entities != null &&StringUtils.isNotEmpty(entities.getEntityType())){
                        String entityType = entities.getEntityType();
                        if(StringUtils.equals("MOVIE",entityType)){
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_MOV"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                        }else if(StringUtils.equals("TV_SHOW",entityType)){
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_REP"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_LIFE"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_KIDS"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_ANI"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_DOCU"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                        }
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                    if(googleRequest.getSearchType().equals("id")){
                        whereList.add(new WhereSet("IDX_ALBUM_ID_F", Protocol.WhereSet.OP_HASALL, q));
                    }else{
//                    	System.out.println(q.replace(" ", ""));
                      whereList.add(new WhereSet("IDX_ALBUM_NAME_G", Protocol.WhereSet.OP_HASALL, q.replace(" ", "")));
                      whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                      whereList.add(new WhereSet("IDX_ALBUM_NAME_GTEST", Protocol.WhereSet.OP_HASALL, q.replace(" ", "")));
//                      whereList.add(new WhereSet("IDX_SUPER_NAME_G", Protocol.WhereSet.OP_HASALL, q.replace(" ", "")));
                        /*whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                        whereList.add(new WhereSet("IDX_ALBUM_NAME_NO_B", Protocol.WhereSet.OP_HASANY, q));*/
                    }

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

                }else {
                    //"PLAY".equals(queryIntent) || "SEARCH".equals(queryIntent))
                    if(StringUtils.isNotEmpty(googleRequest.getStructuredQuery().getMediaType())){
                        String mediaType = googleRequest.getStructuredQuery().getMediaType();
                        if(StringUtils.equals("MOVIE",mediaType)){
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_MOV"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                        }else if(StringUtils.equals("TV_SHOW",mediaType)){
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                            whereList.add(new WhereSet("IDX_SECTION", Protocol.WhereSet.OP_HASALL, "LTE_REP"));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                        }

                    }
                    if(StringUtils.isNotEmpty(googleRequest.checkType())){
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                        if(StringUtils.equals(googleRequest.checkType(),"genre")){

                            whereList.add(new WhereSet("IDX_GENRE2_F", Protocol.WhereSet.OP_HASALL, googleRequest.getGenre()));

                        }else if(StringUtils.equals(googleRequest.checkType(),"actor")){

                            whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, googleRequest.getActor().replaceAll("\\s", "")));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));

                        }else if(StringUtils.equals(googleRequest.checkType(),"director")){

                            whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_DIRECTOR_ENG_B", Protocol.WhereSet.OP_HASALL, googleRequest.getDirector()));


                        }else if(StringUtils.equals(googleRequest.checkType(),"related")){

                            whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, googleRequest.getActor().replaceAll("\\s", "")));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));
                            whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                            whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, googleRequest.getActor()));

                        }
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
                        whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    }

                    //String p = searchVO.getP(); // p 파라미터 분야제한
                    String p = null;
                    if (p == null || "".equals(p) || "20".equals(p)) {
                        p = "01,02,03,04,05";
                    }
                    String[] p_arr = p.split(",");
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                    whereList.add(new WhereSet("IDX_PR_INFO_F", Protocol.WhereSet.OP_HASANY, p_arr));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
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



                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));

                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_TW", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_BROADCASTER_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_TITLE_ENG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_DIRECTOR_ENG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_TITLE_ORIGIN_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_WRITER_ORIGIN_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));

                    whereList.add(new WhereSet("IDX_TITLE_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_TITLE_K", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_TITLE_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_TITLE_TB", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_W", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_ENG_D", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_K", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_TB", Protocol.WhereSet.OP_HASALL, q.replaceAll("\\s", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_PR", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_K", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_TB", Protocol.WhereSet.OP_HASALL, q.replaceAll("\\s", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_PR", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, q.replaceAll("\\s", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_PR", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_CAT_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ALBUM_NAME_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_STARRING_ACTOR_CHOSUNG_PR", Protocol.WhereSet.OP_HASALL, q));

                    // 복합 인덱스
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_MULTI_FILED", Protocol.WhereSet.OP_HASALL, q));

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

                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_F", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_OVERSEER_NAME_PR", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_VOICE_ACTOR_TW", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_BROADCASTER_F", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_DIRECTOR_ENG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_PLAYER_ENG_B", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_WRITER_ORIGIN_D", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_RW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_TW", Protocol.WhereSet.OP_HASALL, q.replaceAll("\\s", "")));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACTOR_RB", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_D", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_W", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_TW", Protocol.WhereSet.OP_HASALL, q));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_ACT_DISP_MAX_ENG_D", Protocol.WhereSet.OP_HASALL, q));

                    // 복합 인덱스
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
                    whereList.add(new WhereSet("IDX_MULTI_FILED_NO_TITLE", Protocol.WhereSet.OP_HASALL, q));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
                    whereList.add(new WhereSet("IDX_RESULT_TYPE_W", Protocol.WhereSet.OP_HASALL, "CALB"));
                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));


                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

                    whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

                    break;
                }

            default:
                break;
        }

        whereSet = new WhereSet[whereList.size()];
        for (int i = 0; i < whereList.size(); i++) {
            whereSet[i] = (WhereSet) whereList.get(i);
        }

        return whereSet;
    }

    private SelectSet[] makeSelectSet(GoogleRequest googleRequest) {

        SelectSet[] selectSet = null;
        ArrayList selectList = new ArrayList();

        String collectionName = googleRequest.getCollectionName();
        switch (collectionName) {
            case "I30_CHANNEL":
                selectList.add(new SelectSet("SERVICE_ID", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("LOCAL_AREA", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CONTENTS_URI", Protocol.SelectSet.NONE));
                break;
            case "GOOGLE":
                selectList.add(new SelectSet("RESULT_TYPE", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("SECTION", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CAT_GB", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CAT_ID", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("CAT_NAME", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("ALBUM_ID", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("ALBUM_NAME", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("IMG_URL_GOOGLE", Protocol.SelectSet.NONE));
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

                selectList.add(new SelectSet("DESCRIPTION", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("KEYWORD", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("DES", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("TYPE", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("SUPER_ID", Protocol.SelectSet.NONE));
                selectList.add(new SelectSet("SUPER_NAME", Protocol.SelectSet.NONE));

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
     * GroupBySet 만들기
     *
     * @param voSearch
     * @return groupbySet
     */

    private GroupBySet[] getGroupBySet(GoogleRequest voSearch) {

        GroupBySet[] groupBySet = null;

        groupBySet = new GroupBySet[1];
        groupBySet[0] = new GroupBySet("GROUP_SUPER_ID", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "ASC");
        // groupBySet[0] = new GroupBySet("GROUP_CAT_ID",
        // (byte)(Protocol.GroupBySet.OP_COUNT |
        // Protocol.GroupBySet.ORDER_COUNT), "DESC");

        return groupBySet;
    }
}
