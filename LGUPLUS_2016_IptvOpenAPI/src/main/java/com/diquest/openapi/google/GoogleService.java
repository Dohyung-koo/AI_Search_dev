package com.diquest.openapi.google;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.openapi.google.request.Entities;
import com.diquest.openapi.google.request.GoogleRequest;

import com.diquest.openapi.google.response.*;
import com.diquest.openapi.google.response.cha.DirectExecution;
import com.diquest.openapi.google.response.cha.GoogleChaRespones;
import com.diquest.openapi.google.response.entity.*;
import com.diquest.openapi.util.StringUtil;
import com.diquest.openapi.util.UtilManager;
import com.diquest.openapi.videopotal.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleService {

    public Logger logger = Logger.getLogger(this.getClass());

/*    public GoogleChaRespones getTestData(boolean b) {
        GoogleChaRespones googleChaRespones = new GoogleChaRespones();
        if(b){

            DirectExecution directExecution = new DirectExecution();
            AndroidIntent androidIntent = new AndroidIntent();
            List<Extras> extrasList = new ArrayList<com.diquest.openapi.google.response.Extras>();
            for(int i = 0; i < 1; i++){
                Extras extras = new Extras();
                extras.setKey("serviceID");
                extras.setValue("340");
                extrasList.add(extras);
            }
            androidIntent.setAction("android.intent.action.VIEW");
            androidIntent.setContentUri( "content://android.media.tv/tune/live");
            androidIntent.setExtrasList(extrasList);
            directExecution.setAndroidIntent(androidIntent);
            googleChaRespones.setDirectExecution(directExecution);
        }else{
            GoogleErrorResponse googleErrorResponse = new GoogleErrorResponse();
            FallbackExecution fallbackExecution = new FallbackExecution();

            fallbackExecution.setErrorCode("에러");
            googleErrorResponse.setFallbackExecution(fallbackExecution);

            googleChaRespones.setGoogleErrorResponse(googleErrorResponse);
        }

        return googleChaRespones;

    }*/

/*    public GoogleEntRespones getEntityTest(String queryIntent) {
        GoogleEntRespones googleChaRespones = new GoogleEntRespones();
        OperatorMediaQueryResponse operatorMediaQueryResponse = new OperatorMediaQueryResponse();
        operatorMediaQueryResponse.setQueryIntent(queryIntent);
        ResultList resultList = new ResultList();
        List<Items> itemsList = new ArrayList<Items>();
        List<Map<String,Object>> mapList = getIptvData(queryIntent);

        for(int i = 0; i < mapList.size(); i++){
            Map<String,Object> map = mapList.get(i);
            map.put("queryIntent",queryIntent);
            Items items = getItem(mapList.get(i));
            itemsList.add(items);
        }
        resultList.setItemsList(itemsList);
        operatorMediaQueryResponse.setResultList(resultList);
        googleChaRespones.setOperatorMediaQueryResponse(operatorMediaQueryResponse);

        return googleChaRespones;
    }*/

/*    private List<Map<String, Object>> getIptvData(String queryIntent) {
        List<Map<String, Object>> mapList = null;
        try { ObjectMapper mapper = new ObjectMapper();

            URL a = this.getClass().getResource("/");
            System.out.println("jsonFile Path : "+a.getPath());

            //System.out.println(logger.getName());
            mapList = mapper.readValue(new File( a.getPath()+queryIntent+".json"), new TypeReference<ArrayList<Map<String, Object>>>() {});
            //ArrayList<String> list = (ArrayList<String>) map.get("messages"); for (String msg : list) { System.out.println(msg); }
        } catch (JsonGenerationException e) { e.printStackTrace(); }
        catch (JsonMappingException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        return mapList;
    }*/

    private Items getItem(Map<String, Object> map) {
        Items items = new Items();
        String queryIntent = (String) map.get("queryIntent");
        items.setTitle((String) map.get("albumName"));
        //queryIntent가 PLAY가 아닐때
        if(!"PLAY".equals(queryIntent)) {
            items.setId((String) map.get("albumId"));
            items.setDescription("description 예정");
            items.setRating(getRating((String) map.get("prInfo")));

            items.setGenre((String) map.get("genre1"));
            /*if ("PLAY_TVM".equals(map.get("queryIntent"))) {
                items.setTmsId("미정");
                items.setTmsRootId("미정");
            }*/
            PosterImage posterImage = new PosterImage();
            posterImage.setImageUrl((String) map.get("imgUrl"));
            posterImage.setHeight(268);
            posterImage.setWidth(477);

            items.setPosterImage(posterImage);
            List<Badgings> badgingsList = new ArrayList<Badgings>();
            //빈 Array로 달라는 요청 반영
            //badgingsList = getBadgingsList(map);
            items.setBadgings(badgingsList);
        }


        List<CallToActions> callToActionsList = getCallToActionsListTest(map);
        items.setCallToActions(callToActionsList);


        return items;
    }

    private String getRating(Object prInfo) {
        String rating = "PG-13";
        if("01".equals(prInfo)){
            //rating = "전체";
            rating = "PG";
        } else if("02".equals(prInfo)){
            //rating = "7세이상";
            rating = "PG";
        } else if("03".equals(prInfo)){
            //rating = "12세이상";
            rating = "PG-13";
        } else if("04".equals(prInfo)){
            //rating = "15세이상";
            rating = "PG-13";
        } else if("05".equals(prInfo)){
            //rating = "19세이상";
            rating = "NC-17";
        } else{
            rating = "";
        }
        return  rating;
    }

    private List<Badgings> getBadgingsList(Map<String, Object> map) {
        List<Badgings> badgingsList = new ArrayList<Badgings>();

        Badgings badgings = new Badgings();
        badgings.setType("<TIME | NEW_OR_FEATURED | PRICING | USER_ACTION | SYSTEM>");

        StaticBadge staticBadge = new StaticBadge();
        staticBadge.setBadgeId("미정");
        badgings.setStaticBadge(staticBadge);
        badgingsList.add(badgings);

        badgings = new Badgings();
        badgings.setType("<TIME | NEW_OR_FEATURED | PRICING | USER_ACTION | SYSTEM>");

        RunTimeBadge runTimeBadge = new RunTimeBadge();
        runTimeBadge.setImageUrl((String)map.get("imgUrl"));
        runTimeBadge.setBadgeText("미정");
        runTimeBadge.setBadgeIconId("미정");
        badgings.setRunTimeBadge(runTimeBadge);
        badgingsList.add(badgings);

        return badgingsList;
    }

    private List<CallToActions> getCallToActionsListTest(Map<String, Object> map) {
        List<CallToActions> callToActionsList = new ArrayList<CallToActions>();
        CallToActions callToActions = new CallToActions();
        callToActions.setTitle((String) map.get("albumName"));
        callToActions.setType("PLAY");

        AndroidIntent androidIntent = new AndroidIntent();
        androidIntent.setAction("android.intent.action.VIEW");
        androidIntent.setContentUri("content://com.lguplus.iptv4.base.searchsuggester.vod");

        List<Extras> extrasList = new ArrayList<Extras>();
        String key = "albumId,catId,seriesYn,seriesNo,closeYN,albumName,keyword,prInfo";

        String[] keyArr = key.split(",");

        for(int i = 0;i < keyArr.length; i++){
            Extras extras = new Extras();
            extras.setKey(keyArr[i]);
            extras.setValue((String)map.get(keyArr[i]) == null ? "" : (String) map.get(keyArr[i]));
            extrasList.add(extras);
        }

        androidIntent.setExtrasList(extrasList);
        callToActions.setAndroidIntent(androidIntent);
        callToActionsList.add(callToActions);

        return callToActionsList;
    }

    public GoogleRequest stringToJson(String jsonRequest, GoogleRequest googleEntityRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            googleEntityRequest = objectMapper.readValue(jsonRequest , GoogleRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleEntityRequest;

    }


    public String getUrl(GoogleRequest googleRequest) {
        String queryIntent = "";

        if(googleRequest.getStructuredQuery() !=null){
            queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        }

        String url = "";
        if("SWITCH_CHANNEL".equals(queryIntent)){
            url= googleRequest.getRequestId()+"/"+googleRequest.getLanguageCode()+"/"+googleRequest.getCustomContext();
            if(googleRequest.getDeviceConfig()!= null) {
                url += "/"+googleRequest.getDeviceConfig().getDeviceModelId();
            }

            if(googleRequest.getStructuredQuery()!=null) {
                url +="/"+googleRequest.getStructuredQuery().getQueryIntent()
                        +"/"+googleRequest.getStructuredQuery().getChannelName()+"/"+googleRequest.getStructuredQuery().getChannelNumber();
            }

        } else if ("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent) || "PLAY".equals(queryIntent)
                || "SEARCH".equals(queryIntent)) {
            url= googleRequest.getRequestId()+"/"+googleRequest.getLanguageCode()+"/"+googleRequest.getCustomContext();
            if(googleRequest.getDeviceConfig()!= null) {
                url += "/"+googleRequest.getDeviceConfig().getDeviceModelId();
            }

            if(googleRequest.getStructuredQuery()!=null) {
                url +="/"+googleRequest.getStructuredQuery().getQueryIntent()
                        +"/"+googleRequest.getStructuredQuery().getSearchQuery();
                if(googleRequest.getStructuredQuery().getEntitiesList() !=null){
                    url += "/"+googleRequest.getStructuredQuery().getEntitiesStr();
                }
            }

        }
        return url;
    }

    public GoogleEntRespones searchJson(GoogleRequest googleRequest, int keyword_byte) {
        GoogleEntRespones googleEntRespones = new GoogleEntRespones();
        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        String q = googleRequest.getStructuredQuery().getSearchQuery();
        q = getByteLength(q, keyword_byte);
        googleRequest.getStructuredQuery().setSearchQuery(q);

        MakeQuery makeQuery = new MakeQuery();
        QuerySet querySet = makeQuery.makeQuerySet(googleRequest);
        ResultSet resultSet = null; // ResultSet
        Result[] resultList = null; // Result[]
        
        logger.debug("[SEARCH KEYWORD] " + q);

        CommandSearchRequest command = new CommandSearchRequest(googleRequest.getHost(), googleRequest.getPort());
        try {

            int rs = command.request(querySet);

            if (rs >= -1) {
                Query[] queryList = querySet.getQueryList();
                resultSet = command.getResultSet();
                resultList = resultSet.getResultList();

                googleEntRespones = makeResponseSearchJson(googleRequest, queryList, resultList, googleEntRespones);
                logger.debug("[SEARCH SUCCESS] searchJson");
            } else {
                logger.debug("[SEARCH FAIL] searchJson : " + rs + " - " + command.getException());
                googleEntRespones.setGoogleErrorCode(ErrorString.GENERAL.OPERATOR_INTERNAL_ERROR);
                googleEntRespones.setLgErrorCode(ErrorString.makeLgMarinerErrorCode(rs));
            }

        } catch (Exception e) {
            logger.error("[EXCEPTION START] " + e.toString());
            e.printStackTrace();
            googleEntRespones.setGoogleErrorCode(ErrorString.GENERAL.OPERATOR_INTERNAL_ERROR);
            googleEntRespones.setLgErrorCode(ErrorString.makeLgServerErrorCode(queryIntent));

        }

        return googleEntRespones;
        
    }

    public GoogleChaRespones googleChaJson(GoogleRequest googleRequest, int keyword_byte) {
        GoogleChaRespones googleChaRespones = new GoogleChaRespones();

        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        String q = googleRequest.getStructuredQuery().getChannelName();
        q = getByteLength(q, keyword_byte);
        googleRequest.getStructuredQuery().setSearchQuery(q);

        MakeQuery makeQuery = new MakeQuery();
        QuerySet querySet = makeQuery.makeQuerySet(googleRequest);
        ResultSet resultSet = null; // ResultSet
        Result[] resultList = null; // Result[]
        logger.debug("[SEARCH KEYWORD] " + q);

        CommandSearchRequest command = new CommandSearchRequest(googleRequest.getHost(), googleRequest.getPort());
        try {
            int rs = command.request(querySet);
            //System.out.println("rs : "+rs);
            if (rs >= -1) {
                Query[] queryList = querySet.getQueryList();
                resultSet = command.getResultSet();
                resultList = resultSet.getResultList();

                // System.out.println("검색후::::::::");
                googleChaRespones = makeResponseChaJson(googleRequest, queryList, resultList, googleChaRespones);
                logger.debug("[SEARCH SUCCESS] searchJson");
            } else {
                logger.debug("[SEARCH FAIL] searchJson : " + rs + " - " + command.getException());
                googleChaRespones.setGoogleErrorCode(ErrorString.GENERAL.OPERATOR_INTERNAL_ERROR);
                googleChaRespones.setLgErrorCode(ErrorString.makeLgMarinerErrorCode(rs));
            }
        } catch (Exception e) {
            logger.error("[EXCEPTION START] " + e.toString());
            e.printStackTrace();
            googleChaRespones.setGoogleErrorCode(ErrorString.GENERAL.OPERATOR_INTERNAL_ERROR);
            googleChaRespones.setLgErrorCode(ErrorString.makeLgServerErrorCode(queryIntent));


        }
        return googleChaRespones;
    }

    private GoogleChaRespones makeResponseChaJson(GoogleRequest googleRequest, Query[] queryList, Result[] resultList, GoogleChaRespones googleChaRespones) {
        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        DirectExecution directExecution = new DirectExecution();
        AndroidIntent androidIntent = new AndroidIntent();

        List extrasList = null;
        String serviceId = "";
//        System.out.println("resultList.length : "+resultList.length);
        for (int i = 0; i < resultList.length; i++) {
            Result result = resultList[i];
            extrasList =  makeGoogleJsonAtt(googleRequest, result, (Query) queryList[i]);
        }
/*        
        // directExecution.setItemsList(itemsList);
      //그룹에서사용.
        for (int i = 0; i < resultList.length; i++) {

            Result result = resultList[i];
            if (i == 0) {
                if (result == null || result.getGroupResultSize() == 0) {
                    //검색결과 없을시
                    System.out.println("검색결과 없을시");
                    googleChaRespones.setGoogleErrorCode(ErrorString.SWITCH_CHANNEL.CHANNEL_NOT_AVAILABLE);
                }else{
                    serviceId = getServiceId(result, googleRequest.getRegionStr());
                }
                if(StringUtils.isNotEmpty(serviceId)){
                    Extras extras = new Extras();
                    extras.setKey("serviceID");
                    extras.setValue(serviceId);
                    extrasList.add(extras);
                }
            }
            break;

        }
*/
        String uri = "";
        for (int i = 0; i < resultList.length; i++) {
            Result result = resultList[i];
             uri =  getContentsUri(result, (Query) queryList[i]);
        }
        
        if(!StringUtil.isEmpty(uri)) {
        	androidIntent.setContentUri(uri);	
        }
        
        androidIntent.setAction("android.intent.action.VIEW");
        androidIntent.setPackageName("com.lguplus.android.tv");
        androidIntent.setExtrasList(extrasList);
        directExecution.setAndroidIntent(androidIntent);
        googleChaRespones.setDirectExecution(directExecution);

        int totalSum = extrasList != null ? extrasList.size() : 0;

        logger.debug("[SEARCH Total CNT] " + totalSum);

        if (totalSum == 0 ) {
            //빈값이다.
            String errorCode = ErrorString.resultEmpty(queryIntent);
            googleChaRespones.setLgErrorCode(ErrorString.makeLgServerErrorCode("EMPTY"));
            googleChaRespones.setGoogleErrorCode(errorCode);
        }

//        System.out.println("packpagname" + androidIntent.getPackageName());
//        System.out.println("action" + androidIntent.getAction());
//        System.out.println("contenturi" + androidIntent.getContentUri());
//        System.out.println("extra" + androidIntent.getExtrasList().toString());
        
        return googleChaRespones;
    }

    private String getServiceId(Result result, String regionStr) {
//        System.out.println("getServiceId");
        GroupResult[] groupResultlist = null;
        String regionServiecId = "";
        String serviceId = "";
        if (result != null && result.getGroupResultSize() != 0) {
            groupResultlist = result.getGroupResults(); // 상태,구분 = 2개

            for (int i = 0; i < 1; i++) {

                for (int k = 0; k < groupResultlist[i].groupResultSize(); k++) {

                    String tempCode = new String(groupResultlist[i].getId(k)).trim();

                    String[] serviceIdArr = StringUtils.split(tempCode, "_");
                    if (serviceIdArr.length != 0) {
                        if (StringUtils.equals(regionStr, serviceIdArr[0])) {
                            regionServiecId = serviceIdArr[1];
                        } else {
                            serviceId = serviceIdArr[1];
                        }

                    }

                }
                if (StringUtils.isNotEmpty(regionServiecId)) {
                    //region이 일치할 경우 바로 반복종료
                    break;
                }
            }
            if(StringUtils.isNotEmpty(regionServiecId)){
                return regionServiecId;
            }else{
                return serviceId;
            }
        }

        return serviceId;
    }
    
    private String getContentsUri(Result result, Query query) {
      String contentsUri = "";
      
      for (int i = 0; i < result.getRealSize(); i++) {
          Extras extras = new Extras();
          for (int k = 0; k < result.getNumField(); k++) {

              String selectFieldName = new String((query.getSelectFields())[k].getField());
              String selectFieldValue = new String(result.getResult(i, k));
              if (selectFieldName.equals("CONTENTS_URI")) {
            	  contentsUri = selectFieldValue;
              }
          }
      }
      return contentsUri;
  }


    private GoogleEntRespones makeResponseSearchJson(GoogleRequest googleRequest, Query[] queryList, Result[] resultArr, GoogleEntRespones googleEntRespones) {
        String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
        OperatorMediaQueryResponse operatorMediaQueryResponse = new OperatorMediaQueryResponse();
        operatorMediaQueryResponse.setQueryIntent(queryIntent);
        ResultList resultList = new ResultList();
        List itemsList = null;
        for (int i = 0; i < resultArr.length; i++) {
            Result result = resultArr[i];
            itemsList =  makeGoogleJsonAtt(googleRequest, result, (Query) queryList[i]);
        }
        resultList.setItemsList(itemsList);

        //List<Map<String,Object>> mapList = getIptvData(queryIntent);
/*      테스트용
        for(int i = 0; i < mapList.size(); i++){
            Map<String,Object> map = mapList.get(i);
            map.put("queryIntent",queryIntent);
            Items items = getItem(mapList.get(i));
            itemsList.add(items);
        }
        resultList.setItemsList(itemsList);*/
        operatorMediaQueryResponse.setResultList(resultList);
        googleEntRespones.setOperatorMediaQueryResponse(operatorMediaQueryResponse);
        
        
        int totalSum = itemsList != null ? itemsList.size() : 0;



        logger.debug("[SEARCH Total CNT] " + totalSum);
        
        /*
         * // 연간검색어 담기 response.setRelWord(relWordList); logger.debug(
         * "[SEARCH RelWord CNT] "+relWordList.size());
         */
        if (totalSum == 0 ) {
            //빈값이다.
            String errorCode = ErrorString.resultEmpty(queryIntent);
            googleEntRespones.setLgErrorCode(ErrorString.makeLgServerErrorCode("EMPTY"));
            googleEntRespones.setGoogleErrorCode(errorCode);
        }

        return googleEntRespones;
    }


    private List makeGoogleJsonAtt(GoogleRequest googleRequest, Result result, Query query) {
        String collectionName = googleRequest.getCollectionName();

        List attList = new ArrayList();
        
        switch (collectionName) {
            case "I30_CHANNEL":
                List<Extras> extrasList = new ArrayList<Extras>();
                List<Extras> extrasLocalList = new ArrayList<Extras>();
                boolean localCheck = false;
                for (int i = 0; i < result.getRealSize(); i++) {
                    Extras extras = new Extras();
                    for (int k = 0; k < result.getNumField(); k++) {

                        String selectFieldName = new String((query.getSelectFields())[k].getField());
                        String selectFieldValue = new String(result.getResult(i, k));
                        if (selectFieldName.equals("SERVICE_ID")) {
                            extras.setKey("serviceID");
                            extras.setValue(selectFieldValue);
                        }

                        if(selectFieldName.equals("LOCAL_AREA")){
                            if(googleRequest.getRegionStr().equals(selectFieldValue)){
                                localCheck = true;
                            }
                        }
	                
                    }

                    if(localCheck){
                        extrasLocalList.add(extras);
                        break;
                    }else{
                        extrasList.add(extras);
                    }
                }
                if(localCheck){
                    attList = extrasLocalList;
                }else{
                    attList = extrasList;
                }

                //attList = extrasList;
                break;
            case "GOOGLE":
                List<Items> itemsList = new ArrayList<Items>();
                for (int i = 0; i < result.getRealSize(); i++) {
                    String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
                    Items items = getItems(queryIntent,query, result ,i);
                    if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent)) {
                        Entities entities =
                                googleRequest.getStructuredQuery().getEntitiesList() == null ? null : googleRequest.getStructuredQuery().getEntitiesList().get(0);
                        if(entities != null &&StringUtils.isNotEmpty(entities.getEntityType())){
                            items.setEntityType(entities.getEntityType());
                        }
                    }else{
                        if(StringUtils.isNotEmpty(googleRequest.getStructuredQuery().getMediaType())){
                            items.setEntityType(googleRequest.getStructuredQuery().getMediaType());
                        }
                    }

                    itemsList.add(items);
                }
                attList = itemsList;
                break;
            default:
                break;
        }
        return  attList;
    }

    private Items getItems(String queryIntent, Query query, Result result, int i) {
        Items items = new Items();
        PosterImage posterImage = new PosterImage();
        List<Badgings> badgingsList = new ArrayList<Badgings>();
        //CallToActionsList생성
        Map<String,String> map = new HashMap<>();
        //items.set
        String imgUrl = "";
        String imgFileName ="";
        String superId="";
        String superName="";

        for (int k = 0; k < result.getNumField(); k++) {

            String selectFieldName = new String((query.getSelectFields())[k].getField());
            String selectFieldValue = new String(result.getResult(i, k));

            if (selectFieldName.equals("ALBUM_NAME")) {
                String albumName = StringUtils.replace(selectFieldValue, "(소장)", "");
                map.put("albumName", albumName);
                items.setTitle(albumName);
            } else if (selectFieldName.equals("PR_INFO")) {
                items.setRating(getRating(selectFieldValue));
                map.put("prInfo",selectFieldValue);
            } else if (selectFieldName.equals("GENRE2")) {
                items.setGenre(selectFieldValue);
            } else if (selectFieldName.equals("IMG_URL_GOOGLE")) {
                imgUrl = selectFieldValue;
                //posterImage.setImageUrl(selectFieldValue);
                posterImage.setHeight(199);
                posterImage.setWidth(140);
            } else if (selectFieldName.equals("IMG_FILE_NAME")) {
                imgFileName = selectFieldValue;
                //posterImage.setImageUrl(posterImage.getImageUrl()+selectFieldValue);
            }  else if (selectFieldName.equals("CAT_ID")) {
                map.put("catId",selectFieldValue);
            } else if (selectFieldName.equals("SERIES_NO")) {
                map.put("seriesNo",selectFieldValue);
            } else if (selectFieldName.equals("CLOSE_YN")) {
                map.put("closeYN",selectFieldValue);
            } else if (selectFieldName.equals("ALBUM_ID")) {
                map.put("albumId",selectFieldValue);
            } else if (selectFieldName.equals("KEYWORD")) {
                map.put("keyword",selectFieldValue);
            } else if (selectFieldName.equals("DES")) {
                items.setDescription(selectFieldValue);
            } else if (selectFieldName.equals("TYPE")) {
                String val = "";
                if(StringUtils.equals("SER",selectFieldValue)){
                    val = "Y";
                }
                map.put("seriesYn", val);
            } else if (selectFieldName.equals("SUPER_ID")) {
            	superId = selectFieldValue;
            } else if (selectFieldName.equals("SUPER_NAME")) {
            	superName = selectFieldValue;
            } /*else if (selectFieldName.equals("CAT_GB")) {
                v_vod.setCatGb(selectFieldValue);
            }else if (selectFieldName.equals("CAT_NAME")) {
                v_vod.setCatName(selectFieldValue);
            } else if (selectFieldName.equals("IMG_FILE_NAME")) {
                v_vod.setImgFileName(selectFieldValue);
            } else if (selectFieldName.equals("SERVICE_GB")) {
                v_vod.setServiceGb(selectFieldValue);
            } else if (selectFieldName.equals("PRICE")) {
                v_vod.setPrice(selectFieldValue);
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
                v_vod.setGenre2(selectFieldValue);
            } else if (selectFieldName.equals("GENRE3")) {
                v_vod.setGenre3(selectFieldValue);
            }else if (selectFieldName.equals("POINT")) {
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
            } else if (selectFieldName.equals("ORDER_DATE")) {
                v_vod.setOrderDate(selectFieldValue);
            } else if (selectFieldName.equals("WEIGHT")) {
                v_vod.setWeight(selectFieldValue);
            }*/
            
            if(!StringUtil.isEmpty(superId) && (!StringUtil.isEmpty(superName))) {
            	map.put("albumId",superId);
            	map.put("albumName", superName);
            	items.setTitle(superName);
            }
        }


        List<CallToActions> callToActionsList = getCallToActionsList(map);
        //빈 Array로 달라는 요청 반영
        //badgingsList = getBadgingsList(map);
        items.setBadgings(badgingsList);
        if(StringUtils.isNotEmpty(imgUrl)&&StringUtils.isNotEmpty(imgFileName)){
            posterImage.setImageUrl(imgUrl+imgFileName);
        }else{
            posterImage.setImageUrl("");
        }
        items.setPosterImage(posterImage);
        items.setCallToActions(callToActionsList);

        return items;
    }

    private List<CallToActions> getCallToActionsList(Map<String, String> map) {
        List<CallToActions> callToActionsList = new ArrayList<CallToActions>();
        CallToActions callToActions = new CallToActions();
        callToActions.setTitle("에서 시청");
        callToActions.setType("PLAY");
        callToActions.setContentSource("VOD");

        AndroidIntent androidIntent = new AndroidIntent();

        androidIntent.setAction("android.intent.action.VIEW");
        androidIntent.setContentUri("content://com.lguplus.iptv4.base.searchsuggester.vod");
        androidIntent.setPackageName("com.lguplus.iptv4.base.searchsuggester");
        List<Extras> extrasList = new ArrayList<Extras>();
        String key = "albumId,catId,seriesYn,seriesNo,closeYN,albumName,keyword,prInfo";

        String[] keyArr = key.split(",");

        for(int i = 0;i < keyArr.length; i++){
            Extras extras = new Extras();
            extras.setKey(keyArr[i]);
            /*if("seriesYn".equals(keyArr[i])){
                if(map.get("seriesNo") == null || "".equals(map.get("seriesNo"))){
                    extras.setValue("N");
                }else{
                    extras.setValue("Y");
                }
            }else{
                extras.setValue(map.get(keyArr[i]) == null ? "" : map.get(keyArr[i]));
            }*/
            extras.setValue(map.get(keyArr[i]) == null ? "" : map.get(keyArr[i]));
            extrasList.add(extras);
        }

        androidIntent.setExtrasList(extrasList);
        callToActions.setAndroidIntent(androidIntent);
        callToActionsList.add(callToActions);

        return callToActionsList;
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


    public GoogleChaRespones googleChaPassJson(GoogleRequest googleRequest) {
        GoogleChaRespones googleChaRespones = new GoogleChaRespones();
        DirectExecution directExecution = new DirectExecution();
        AndroidIntent androidIntent = new AndroidIntent();
        List<Extras> extrasList = new ArrayList<Extras>();
        String channelId = googleRequest.getStructuredQuery().getChannelId();
        String channelNumber = googleRequest.getStructuredQuery().getChannelNumber();
        Extras extras = new Extras();
        extras.setKey("serviceID");
        if("".equals(channelNumber)){
            extras.setValue(channelId);
        }else{
            extras.setValue(channelNumber);
        }
        logger.debug("[SEARCH KEYWORD] " + extras.getValue());
        extrasList.add(extras);

        androidIntent.setAction("android.intent.action.VIEW");
        androidIntent.setPackageName("com.lguplus.android.tv");
        androidIntent.setContentUri("content://android.media.tv.ga/tune/live");
        androidIntent.setExtrasList(extrasList);
        directExecution.setAndroidIntent(androidIntent);
        googleChaRespones.setDirectExecution(directExecution);

        return googleChaRespones;

    }
}
