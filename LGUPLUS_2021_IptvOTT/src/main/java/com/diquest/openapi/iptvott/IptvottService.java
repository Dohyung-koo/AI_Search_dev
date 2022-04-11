package com.diquest.openapi.iptvott;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.openapi.iptvott.request.IptvottQuickRequest;
import com.diquest.openapi.iptvott.request.IptvottRequest;
import com.diquest.openapi.iptvott.request.StructuredQuery;
import com.diquest.openapi.iptvott.response.FallbackExecution;
import com.diquest.openapi.iptvott.response.IptvottErrorResponse;
import com.diquest.openapi.iptvott.response.entity.IptvottQuickRespones;
import com.diquest.openapi.iptvott.response.entity.IptvottRespones;
import com.diquest.openapi.iptvott.response.entity.Iptvott_cha;
import com.diquest.openapi.iptvott.response.entity.Iptvott_vod;
import com.diquest.openapi.iptvott.response.entity.OttResult;
import com.diquest.openapi.iptvott.response.entity.OttResult_json;
import com.diquest.openapi.iptvott.response.entity.RelOttList;
import com.diquest.openapi.iptvott.response.entity.ResultInfo;
import com.diquest.openapi.iptvott.response.entity.Section;
import com.diquest.openapi.iptvott.response.entity.Section_json;
import com.diquest.openapi.iptvott.response.entity.SubOttList;
import com.diquest.openapi.util.info.ERROR_TYPE;

@Service
public class IptvottService {

    public Logger logger = Logger.getLogger(this.getClass());

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

    public IptvottRequest stringToJson(String jsonRequest, IptvottRequest iptvottRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
        	iptvottRequest = objectMapper.readValue(jsonRequest , IptvottRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iptvottRequest;

    }
    
    public IptvottQuickRequest stringToJson(String jsonRequest, IptvottQuickRequest iptvottQuickRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
        	iptvottQuickRequest = objectMapper.readValue(jsonRequest , IptvottQuickRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iptvottQuickRequest;

    }
    
    private SubOttList stringToJson(String jsonRequest, SubOttList subOttList) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
        	subOttList = objectMapper.readValue(jsonRequest , SubOttList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subOttList;

    }

    public String getUrl(IptvottRequest googleRequest) {
//        String queryIntent = "";
//
//        if(googleRequest.getStructuredQuery() !=null){
//            queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
//        }

        String url = "";
//        if("SWITCH_CHANNEL".equals(queryIntent)){
//            url= googleRequest.getRequestId()+"/"+googleRequest.getLanguageCode()+"/"+googleRequest.getCustomContext();
//            if(googleRequest.getDeviceConfig()!= null) {
//                url += "/"+googleRequest.getDeviceConfig().getDeviceModelId();
//            }
//
//            if(googleRequest.getStructuredQuery()!=null) {
//                url +="/"+googleRequest.getStructuredQuery().getQueryIntent()
//                        +"/"+googleRequest.getStructuredQuery().getChannelName()+"/"+googleRequest.getStructuredQuery().getChannelNumber();
//            }
//
//        } else if ("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent) || "PLAY".equals(queryIntent)
//                || "SEARCH".equals(queryIntent)) {
//            url= googleRequest.getRequestId()+"/"+googleRequest.getLanguageCode()+"/"+googleRequest.getCustomContext();
//            if(googleRequest.getDeviceConfig()!= null) {
//                url += "/"+googleRequest.getDeviceConfig().getDeviceModelId();
//            }
//
//            if(googleRequest.getStructuredQuery()!=null) {
//                url +="/"+googleRequest.getStructuredQuery().getQueryIntent()
//                        +"/"+googleRequest.getStructuredQuery().getSearchQuery();
//                if(googleRequest.getStructuredQuery().getEntitiesList() !=null){
//                    url += "/"+googleRequest.getStructuredQuery().getEntitiesStr();
//                }
//            }
//
//        }
        return url;
    }

    public IptvottRespones searchJson(IptvottRequest iptvottRequest, int keyword_byte) {
        IptvottRespones iptvottRespones = new IptvottRespones();
        
        MakeQuery makeQuery = new MakeQuery();
        QuerySet querySet = makeQuery.makeQuerySet(iptvottRequest);
        ResultSet resultSet = null; // ResultSet
        Result[] resultList = null; // Result[]
        
        //logger.debug("[SEARCH KEYWORD] " + q);
        //쿼리 리스트를 2개로 분류 해야하는건가??? 메소드 2개를 만드는건가?? I30_CHANNEL참고
        CommandSearchRequest command = new CommandSearchRequest(iptvottRequest.getHost(), iptvottRequest.getPort());
        try {
            int rs = command.request(querySet);

            if (rs >= -1) {
                Query[] queryList = querySet.getQueryList();
                resultSet = command.getResultSet();
                resultList = resultSet.getResultList();

                iptvottRespones = makeResponseSearchJson(iptvottRequest, queryList, resultList, iptvottRespones);
                logger.debug("[SEARCH SUCCESS] searchJson");
            } else {
                logger.debug("[SEARCH FAIL] searchJson : " + rs + " - " + command.getException());
                iptvottRespones.setIptvottErrorResponse("40001000");
                iptvottRespones.setLgErrorCode("40001000");
            }

        } catch (Exception e) {
            logger.error("[EXCEPTION START] " + e.toString());
            e.printStackTrace();
            iptvottRespones.setIptvottErrorResponse("40001000");
            iptvottRespones.setLgErrorCode("40001000");
        }

        return iptvottRespones;
        
    }
    
	public IptvottQuickRespones searchQuickJson(IptvottQuickRequest iptvottQuickRequest, int keyword_byte) {
		IptvottQuickRespones resultResponse = new IptvottQuickRespones();
//		String q = iptvottQuickRequest.getQ();
//		logger.debug("[SEARCH KEYWORD] "+getByteLength(q,keyword_byte));
//		q= getByteLength(q,40);
		
		IptvottRequest searchVO = new IptvottRequest();
		StructuredQuery structuredQuery = new StructuredQuery();
		structuredQuery.setQuery(iptvottQuickRequest.getQ());
		
		searchVO.setStructuredQuery(structuredQuery);
		searchVO.setW(iptvottQuickRequest.getW());
		searchVO.setOutmax(iptvottQuickRequest.getOutmax());
		
		MakeQuery makeQuery = new MakeQuery();
		QuerySet querySet = makeQuery.makeQuerySet(searchVO);
		
		ResultSet resultSet = null;										//ResultSet
		Result[] resultList = null;										//Result[]		
//		logger.debug("[SEARCH KEYWORD] "+searchVO.getQ());
		//ResultSet
		CommandSearchRequest command = new CommandSearchRequest(iptvottQuickRequest.getHost(), iptvottQuickRequest.getPort());
		try {
			int rs = command.request(querySet);
			
			if(rs >= -1) {
				Query[] queryList = querySet.getQueryList();
				resultSet = command.getResultSet();
				resultList = resultSet.getResultList();
				
				resultResponse = makeQuickResponseJson(iptvottQuickRequest, queryList, resultList, resultResponse);
//				System.out.println("### search success TvgService.searchQuick");
				logger.debug("[SEARCH SUCCESS] searchQuickJson");
			} else {
//				System.out.println("### search fail TvgService.searchQuickJson: " + rs + " - " + command.getException());
				logger.debug("[SEARCH FAIL] searchQuickJson : " + rs + " - " + command.getException());
				resultResponse.setIptvottErrorResponse("40002000");
				resultResponse.setLgErrorCode("40002000");
			}
		}
		catch (Exception e) {
			logger.error("[EXCEPTION START] "+ e.toString());
			e.printStackTrace();
			resultResponse.setIptvottErrorResponse("40002000");
			resultResponse.setLgErrorCode("40002000");
		}
				
		return resultResponse;
	}
	
	private IptvottQuickRespones makeQuickResponseJson(IptvottQuickRequest iptvottQuickRequest, Query[] queryList, Result[] resultList, IptvottQuickRespones iptvottQuickRespones) {
		String typoResult = "";
		// String typoCorrectResult = "";
		String synSearcher = "";
		int relWordSum = 0;
		int wordSum = 0;
		int totalSum = 0;

		ArrayList relWordList = new ArrayList();
		List wordList = new ArrayList();
		ResultInfo resultInfo = new ResultInfo();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.length; i++) {
				Result result = resultList[i];

				if (iptvottQuickRequest.getW().equals("IPTV_OTT_QUICK") && i == resultList.length - 1) {
					wordList = makeAutoWord(result, queryList[i]);
					wordSum = wordSum + wordList.size();
				}
			}
		}
		totalSum = wordSum;
		iptvottQuickRespones.setAutoWord(wordList);
		logger.debug("[SEARCH AutoWord CNT] " + wordList.size());

		if (totalSum == 0) {
			resultInfo.setCode("20001000");
			resultInfo.setMsg(ERROR_TYPE.CODE_20001000.getErrorMessage());
		}else {
			resultInfo.setCode("20000000");
			resultInfo.setMsg(ERROR_TYPE.CODE_20000000.getErrorMessage());
		}
		iptvottQuickRespones.setResultInfo(resultInfo);
		// logger.debug("[SEARCH Corqry] " + response.getCorqry());
		return iptvottQuickRespones;
	}

	private List makeAutoWord(Result result, Query query) {
		ArrayList wordList = new ArrayList();

		for (int i = 0; i < result.getRealSize(); i++) {

			for (int k = 0; k < result.getNumField(); k++) {

				String selectFieldName = new String((query.getSelectFields())[k].getField());
				String selectFieldValue = new String(result.getResult(i, k));

				if (selectFieldName.equals("KEYWORD")) {
					wordList.add(selectFieldValue);
				}

			}
		}
		return wordList;
	}

	private IptvottRespones makeResponseSearchJson(IptvottRequest iptvottRequest, Query[] queryList, Result[] resultArr, IptvottRespones iptvotttRespones) {
        
        List<OttResult> ottResultList = new ArrayList<OttResult>();
        List<Section> resultList = new ArrayList<Section>();
        OttResult_json ottResultJson = new OttResult_json();
        Section_json sectionJson = new Section_json();
        
        System.out.println("resultArr 갯수 : "+resultArr.length);
        for (int i = 0; i < resultArr.length; i++) {
        	List attList = null;
        	Section section = new Section();
        	
        	String collectionName = new String(queryList[i].getFromField());
        	
            Result result = resultArr[i];
            attList =  makeIptvottJsonAtt(iptvottRequest, result, (Query) queryList[i]);
            section.setAttList(attList);
            
            //section이 VOD이면 OTT_VOD, CHA이면 OTT_CHANNEL
            if(collectionName.equals("OTT_VOD")) {
            	section.setSectionName("VOD콘텐츠");	
            }else {
            	//오늘 날짜 기준으로 결정 방송중과 예정 갯수 구하기
            	section.setSectionName("실시간채널");
            	section.setTotcntOnAir("1"); //CHA인 경우 추가
                section.setTotcntSchedule("1"); //CHA인 경우 추가
            }
            section.setTotcnt(""+result.getRealSize());
            section.setMaxcnt(iptvottRequest.getOutmax());
            section.setOutcnt("1");
            section.setPagenum(iptvottRequest.getPage());
            section.setSort(iptvottRequest.getSort());
            section.setOtt("");
            
            resultList.add(section);
            
            if(collectionName.equals("OTT_VOD")) {
                //ott리스트 만들기
                int grlCnt = resultArr[i].getGroupResults()[0].groupResultSize(); //그룹 사이즈
                //int[] ottCntArr = resultArr[i].getGroupResults()[0].getIntValues(); //ott리스트 별 갯수
    			
    			char[][] charArr = resultArr[i].getGroupResults()[0].getIds();
    			for(int k = 0; k < grlCnt; k++){
    				OttResult ottResult = new OttResult();
    				String ottId = new String(charArr[k]);
    				ottResult.setOttId(ottId);
    				if(ottId.equals("OT001")) {
    					ottResult.setOttName("유튜브");	
    				}else if(ottId.equals("OT002")) {
    					ottResult.setOttName("U+ 프로야구");
    				}else if(ottId.equals("OT003")) {
    					ottResult.setOttName("U+골프");
    				}else if(ottId.equals("OT004")) {
    					ottResult.setOttName("U+아이돌Live");
    				}else if(ottId.equals("OT005")) {
    					ottResult.setOttName("U+홈트나우");
    				}else if(ottId.equals("OT006")) {
    					ottResult.setOttName("디즈니플러스");
    				}else if(ottId.equals("OT007")) {
    					ottResult.setOttName("넷플릭스");
    				}else if(ottId.equals("OT008")) {
    					ottResult.setOttName("Vlive");
    				}else if(ottId.equals("OT009")) {
    					ottResult.setOttName("U+영화월정액");
    				}else if(ottId.equals("OT010")) {
    					ottResult.setOttName("유튜브 키즈");
    				}else if(ottId.equals("OT011")) {
    					ottResult.setOttName("U+ VOD");
    				}else if(ottId.equals("OT012")) {
    					ottResult.setOttName("캐치온");
    				}else if(ottId.equals("OT013")) {
    					ottResult.setOttName("아이들나라");
    				}
    				ottResult.setOttCount(""+resultArr[i].getGroupResults()[0].getIntValue(k));
    				
    				ottResultList.add(ottResult);
    			}	
            }
        }
        ottResultJson.setOttResultList(ottResultList);
        sectionJson.setSectionList(resultList);
        
        iptvotttRespones.setResultList(sectionJson);
        iptvotttRespones.setOttList(ottResultJson);
        iptvotttRespones.setResultCode("20000000");

        return iptvotttRespones;
    }


    private List makeIptvottJsonAtt(IptvottRequest iptvottRequest, Result result, Query query) {
        String collectionName = new String(query.getFromField());
        System.out.println("컬렉션 이름 : "+ collectionName);
        System.out.println("결과 사이즈 : "+ result.getRealSize());
        List attList = new ArrayList();
        switch (collectionName) {
            case "OTT_VOD":
                for (int i = 0; i < result.getRealSize(); i++) {
                    Iptvott_vod iptvott_vod = new Iptvott_vod();
                    
    				for (int k = 0; k < result.getNumField(); k++) {
    					String selectFieldName = new String((query.getSelectFields())[k].getField());
    					String selectFieldValue = new String(result.getResult(i, k));
    					if (selectFieldName.equals("OTT_ALBUM_ID")) {
    						iptvott_vod.setOttAlbumId(selectFieldValue);
    					} else if (selectFieldName.equals("OTT_ALBUM_NAME")) {
    						iptvott_vod.setOttAlbumName(selectFieldValue);
    					} else if (selectFieldName.equals("OTT_ID")) {
    						iptvott_vod.setOttId(selectFieldValue);
    					} else if (selectFieldName.equals("OTT_NAME")) {
    						iptvott_vod.setOttName(selectFieldValue);
    					} else if (selectFieldName.equals("OTT_GRP_ID")) {
    						iptvott_vod.setOttGrpId(selectFieldValue); 
    					} else if (selectFieldName.equals("OTT_GRP_NAME")) {
    						iptvott_vod.setOttGrpName(selectFieldValue);
    					} else if (selectFieldName.equals("RUNTIME")) {
    						iptvott_vod.setRunTime(selectFieldValue);
    					} else if (selectFieldName.equals("PLAYER")) {
    						iptvott_vod.setPlayer(selectFieldValue);
    					} else if (selectFieldName.equals("DIRECTOR")) {
    						iptvott_vod.setDirector(selectFieldValue);
    					} else if (selectFieldName.equals("DISTRUBUTOR_NAME")) {
    						iptvott_vod.setDistrubutorName(selectFieldValue);
    					} else if (selectFieldName.equals("RELEASE_DATE")) {
    						iptvott_vod.setReleaseDate(selectFieldValue);
    					} else if (selectFieldName.equals("CREATE_YEAR")) {
    						iptvott_vod.setCreateYear(selectFieldValue); 
    					} else if (selectFieldName.equals("OTT_GENRE_NAME")) {
    						iptvott_vod.setOttGenreName(selectFieldValue);
    					} else if (selectFieldName.equals("RATING")) {
    						iptvott_vod.setRating(selectFieldValue);
    					} else if (selectFieldName.equals("PRICE")) {
    						iptvott_vod.setPrice(selectFieldValue);
    					} else if (selectFieldName.equals("POSTER_FILE_NAME")) {
    						iptvott_vod.setPosterFileName(selectFieldValue);
    					} else if (selectFieldName.equals("STILL_FILE_NAME")) {
    						iptvott_vod.setStillFileName(selectFieldValue);
    					} else if (selectFieldName.equals("COUNTRY_ORIGIN")) {
    						iptvott_vod.setCountryOrigin(selectFieldValue);
    					} else if (selectFieldName.equals("POINT")) {
    						iptvott_vod.setPoint(selectFieldValue);
    					} else if (selectFieldName.equals("OTT_TYPE")) {
    						iptvott_vod.setOttType(selectFieldValue);
    					} else if (selectFieldName.equals("LINK_URL")) {
    						iptvott_vod.setLinkurl(selectFieldValue);
    					} else if (selectFieldName.equals("SUPER_ID")) {
    						iptvott_vod.setSuperId(selectFieldValue);
    					} else if (selectFieldName.equals("RESULT_TYPE")) {
    						iptvott_vod.setResultType(selectFieldValue);
    					} else if (selectFieldName.equals("CAT_ID")) {
    						iptvott_vod.setCatId(selectFieldValue);
    					} else if (selectFieldName.equals("SERIES_NO")) {
    						iptvott_vod.setSeriesNo(selectFieldValue);
    					} else if (selectFieldName.equals("TITLE")) {
    						iptvott_vod.setTitle(selectFieldValue);
    					} else if (selectFieldName.equals("CLOSE_YN")) {
    						iptvott_vod.setCloseYn(selectFieldValue);
    					} else if(selectFieldName.equals("SUB_LIST")) {
    						iptvott_vod.setRelOttList(makeRelOttList(selectFieldValue));
    					}
    					//여기에 relottList도 넣어줘야 하는데 db에 데이터 어떻게 들어가있는지 확인을 하고 추가해야 할듯
    				}
    				attList.add(iptvott_vod);
                }
                //attList = extrasList;
                break;
            case "I30_CHANNEL":
            	 for (int i = 0; i < result.getRealSize(); i++) {
                     Iptvott_cha iptvott_cha = new Iptvott_cha();
                     
     				for (int k = 0; k < result.getNumField(); k++) {
     					String selectFieldName = new String((query.getSelectFields())[k].getField());
     					String selectFieldValue = new String(result.getResult(i, k));
     					
     					if (selectFieldName.equals("SERVICE_ID")) {
     						iptvott_cha.setServiceId(selectFieldValue);
     					} else if (selectFieldName.equals("SERVICE_NAME")) {
     						iptvott_cha.setServiceName(selectFieldValue);
     					} else if (selectFieldName.equals("SERVICE_ENG_NAME")) {
     						iptvott_cha.setServiceEngName(selectFieldValue);
     					} else if (selectFieldName.equals("CHANNEL_NO")) {
     						iptvott_cha.setChannelNo(selectFieldValue);
     					} else if (selectFieldName.equals("PROGRAM_ID")) {
     						iptvott_cha.setProgramId(selectFieldValue);
     					} else if (selectFieldName.equals("PROGRAM_NAME")) {
     						iptvott_cha.setProgramName(selectFieldValue);
     					} else if (selectFieldName.equals("THM_IMG_RL")) {
     						iptvott_cha.setThmImgUrl(selectFieldValue);
     					} else if (selectFieldName.equals("THM_IMG_FILE_NAME")) {
     						iptvott_cha.setThmImgFileName(selectFieldValue);
     					} else if (selectFieldName.equals("RATING")) {
     						iptvott_cha.setRating(selectFieldValue);
     					} else if (selectFieldName.equals("BROAD_TIME")) {
     						iptvott_cha.setBroadTime(selectFieldValue);
     					} else if (selectFieldName.equals("DAY")) {
     						iptvott_cha.setDay(selectFieldValue);
     					} else if (selectFieldName.equals("OVERSEER_NAME")) {
     						iptvott_cha.setOverseerName(selectFieldValue);
     					} else if (selectFieldName.equals("ACTOR")) {
     						iptvott_cha.setActor(selectFieldValue);
     					} else if (selectFieldName.equals("P_NAME")) {
     						iptvott_cha.setpName(selectFieldValue);
     					} else if (selectFieldName.equals("GENRE1")) {
     						iptvott_cha.setGenre1(selectFieldValue);
     					} else if (selectFieldName.equals("GENRE2")) {
     						iptvott_cha.setGenre2(selectFieldValue);
     					} else if (selectFieldName.equals("GENRE3")) {
     						iptvott_cha.setGenre3(selectFieldValue);
     					} else if (selectFieldName.equals("SERIES_NO")) {
     						iptvott_cha.setSeriesNo(selectFieldValue);
     					} else if (selectFieldName.equals("SUB_NAME")) {
     						iptvott_cha.setSubName(selectFieldValue);
     					} else if (selectFieldName.equals("BROAD_DATE")) {
     						iptvott_cha.setBroadDate(selectFieldValue);
     					} else if (selectFieldName.equals("LOCAL_AREA")) {
     						iptvott_cha.setLocalArea(selectFieldValue);
     					} else if (selectFieldName.equals("AV_RESOLUTION")) {
     						iptvott_cha.setAvResolution(selectFieldValue);
     					} else if (selectFieldName.equals("CAPTION_FLAG")) {
     						iptvott_cha.setCaptionFlag(selectFieldValue);
     					} else if (selectFieldName.equals("DVS_FLAG")) {
     						iptvott_cha.setDvsFlag(selectFieldValue);
     					} else if (selectFieldName.equals("IS51_CH")) {
     						iptvott_cha.setIs51Ch(selectFieldValue);
     					} else if (selectFieldName.equals("FILTERING_CODE")) {
     						iptvott_cha.setFilteringCode(selectFieldValue);
     					} else if (selectFieldName.equals("CHNL_KEYWORD")) {
     						iptvott_cha.setChnlKeyword(selectFieldValue);
     					} else if (selectFieldName.equals("CHNL_ICON_URL")) {
     						iptvott_cha.setChnlIconUrl(selectFieldValue);
     					} else if (selectFieldName.equals("CHNL_ICON_FILE_NAME")) {
     						iptvott_cha.setChnlIconFileName(selectFieldValue);
     					}
     				}
     				attList.add(iptvott_cha);
                 }
                break;
            default:
                break;
        }
        return attList;
    }

    private List<RelOttList> makeRelOttList(String selectFieldValue){
    	List relOttList = new ArrayList<>();
    	
    	if(!selectFieldValue.equals("")) {
        	String[] strArr = selectFieldValue.replaceAll("[\\[\\]]", "").split("\\},\\{");
        	System.out.println("테스트용!! : "+ Arrays.toString(strArr));

        	//여기부터!!!! pjy
        	for(int i = 0; i < strArr.length; i++) {
        		SubOttList subOttList = new SubOttList();
        		RelOttList relOtt = new RelOttList();
        		String str = strArr[i];
        		if(i == 0) {
        			str = str+"}";
        		}else if(i == strArr.length -1) {
        			str = "{"+str;
        		}
        		subOttList = stringToJson(str, subOttList);
        		
        		relOtt.setRelOttId(subOttList.getOttId());
        		relOtt.setRelOttType("");
        		relOtt.setRelOttName(subOttList.getOttName());
        		relOtt.setRelOttAlbumId(subOttList.getOttAlbumId());
        		relOtt.setRelOttAlbumName(subOttList.getOttAlbumName());
        		relOtt.setLinkUrl(subOttList.getLinkUrl());
        		relOtt.setSuperId(subOttList.getSuperId());
        		relOtt.setResultType(subOttList.getResultType());
        		relOtt.setCatId(subOttList.getCatId());
        		relOtt.setSeriesNo(subOttList.getSeriesNo());
        		relOtt.setTitle(subOttList.getTitle());
        		relOtt.setCloseYn(subOttList.getCloseYn());
        		
        		relOttList.add(relOtt);
        	}
    	}
    	return relOttList;
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
}