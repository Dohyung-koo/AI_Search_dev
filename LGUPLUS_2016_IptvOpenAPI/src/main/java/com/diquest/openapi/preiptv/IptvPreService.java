package com.diquest.openapi.preiptv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.openapi.util.Json_Section;
import com.diquest.openapi.util.Json_Section_List;
import com.diquest.openapi.util.OpenAPIQuickResponseJson;
import com.diquest.openapi.util.OpenAPIResponseJson;

@Service
public class IptvPreService {

	public Logger logger = Logger.getLogger(this.getClass());

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
			searchVO.setD((String) obj.get("d"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return searchVO;
	}

	/**
	 * 통합검색 처리 (json)
	 * 
	 * @param searchVO
	 * @return
	 */
	public OpenAPIResponseJson searchJson(IptvPreVO searchVO, int keyword_byte) throws Exception {
		OpenAPIResponseJson resultResponse = new OpenAPIResponseJson();
		// System.out.println("searchJson 검색시작");
		String q = searchVO.getQ();
		q = getByteLength(q, keyword_byte);
		searchVO.setQ(q);
		MakeQuery makeQuery = new MakeQuery();
		QuerySet querySet = makeQuery.makeQuerySet(searchVO);

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

				// System.out.println("검색후::::::::"+getNowDate(17));
				resultResponse = makeResponseJson(searchVO, queryList, resultList, resultResponse,
						searchVO.getSectionListArr());
				logger.debug("[SEARCH SUCCESS] searchJson");
			} else {
				logger.debug("[SEARCH FAIL] searchJson : " + rs + " - " + command.getException());
				resultResponse.setErrorResponse(IptvPreUtilManager.makeRsInfoJson(rs));
			}

		} catch (Exception e) {
			logger.error("[EXCEPTION START] " + e.toString());
			e.printStackTrace();
			resultResponse.setErrorResponse(IptvPreUtilManager.makeErrorJson("1000"));
			// System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION END
			// =+=+=+=+=+=+=+=+=+=+=+=+=+=");
		}
		// System.out.println("가공후::::::::"+getNowDate(17));

		return resultResponse;
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
	private OpenAPIQuickResponseJson makeQuickResponseJson(IptvPreVO searchVO, Query[] queryList, Result[] resultList,
			OpenAPIQuickResponseJson response, String[] sectionListArr) throws Exception {
		String typoResult = "";
		// String typoCorrectResult = "";
		String synSearcher = "";
		int relWordSum = 0;
		int wordSum = 0;
		int totalSum = 0;

		ArrayList relWordList = new ArrayList();
		List wordList = new ArrayList();
		List JsonSectionList = new ArrayList();
		if (resultList == null) {

		} else {
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
					wordList = makeAutoWord(result, queryList[i]);
					wordSum = wordSum + wordList.size();
				} else {

				}
			}

		}
		totalSum = wordSum;
		response.setAutoWord(wordList);
		logger.debug("[SEARCH AutoWord CNT] " + wordList.size());

		if (totalSum == 0) {
			response.setErrorResponse(IptvPreUtilManager.makeRsInfoJson(-1));
		}
		// logger.debug("[SEARCH Corqry] " + response.getCorqry());
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

	/*	*//**
			 * 자동완성/순간 검색 처리 (json)
			 * 
			 * @param searchVO
			 * @return
			 */
	public OpenAPIQuickResponseJson searchQuickJson(IptvPreVO searchVO, int keyword_byte) throws Exception {
		OpenAPIQuickResponseJson resultResponse = new OpenAPIQuickResponseJson();

		String q = searchVO.getQ();

		if (StringUtils.isNotEmpty(searchVO.getSection())) {
			searchVO.setSection("");
		}

		q = getByteLength(q, keyword_byte);
		searchVO.setQ(q);
		List wordList = new ArrayList();

		MakeQuery makeQuery = new MakeQuery();
		QuerySet querySet = makeQuery.makeQuerySet(searchVO);

		ResultSet resultSet = null; // ResultSets
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
				resultResponse = makeQuickResponseJson(searchVO, queryList, resultList, resultResponse,
						searchVO.getSectionListArr());

				logger.debug("[SEARCH SUCCESS] searchQuickJson");
			} else {
				// System.out.println("### search fail
				// VideoService.searchQuickJson: " + rs + " - " +
				// command.getException());
				logger.debug("[SEARCH FAIL] searchQuickJson : " + rs + " - " + command.getException());

				resultResponse.setErrorResponse(IptvPreUtilManager.makeRsInfoJson(rs));
			}

		} catch (Exception e) {
			logger.error("[EXCEPTION START] " + e.toString());
			e.printStackTrace();
			resultResponse.setErrorResponse(IptvPreUtilManager.makeErrorJson("2000"));
			// System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION END
			// =+=+=+=+=+=+=+=+=+=+=+=+=+=");
		}

		return resultResponse;

	}

	/*	*//**
			 * 통합 검색 결과별 후 처리 (json)
			 * 
			 * @param searchVO
			 * @param queryList
			 * @param resultList
			 * @param response
			 * @return
			 */
	private OpenAPIResponseJson makeResponseJson(IptvPreVO searchVO, Query[] queryList, Result[] resultList,
			OpenAPIResponseJson response, String[] sectionListArr) throws Exception {
		String typoResult = "";
		// String typoCorrectResult = "";
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

		int totalCnt = 0;
		Json_Section json_section = null;
		for (int i = 0; i < resultList.length; i++) {

			json_section = new Json_Section();
			Result result = resultList[i];

			// 참조용 파라미터 셋팅
			json_section.setSection_name(sectionListArr[i]); // 검색 섹션
			/*
			 * if(StringUtils.equals("VR_BEST", sectionListArr[i])
			 * ||StringUtils.equals("VR_STAR", sectionListArr[i])
			 * ||StringUtils.equals("VR_MOV", sectionListArr[i])
			 * ||StringUtils.equals("VR_TH", sectionListArr[i])) {
			 * searchVO.setCollection("iptvPre_vod"); }else if(StringUtils.equals("VR_HVD",
			 * sectionListArr[i])) { searchVO.setCollection("VIDEO_HIT_UCC"); }else
			 * if(StringUtils.equals("VR_WT", sectionListArr[i])
			 * ||StringUtils.equals("VR_GAME", sectionListArr[i])
			 * ||StringUtils.equals("VR_PNRM", sectionListArr[i])) {
			 * searchVO.setCollection("VIDEO_HIT_UCC"); }
			 */

			if (result.getTotalSize() > 0) {
				logger.debug("[SEARCH SECTION_NAME] " + sectionListArr[i] + "(" + result.getTotalSize() + ")");
			}

			json_section.setOutcnt(String.valueOf(result.getRealSize()));

			// 공통 파라미터 셋팅
			json_section.setTotcnt(result.getTotalSize() + ""); // 검색 결과 전체 건수
			totalSum = totalSum + result.getTotalSize();
			json_section.setMaxcnt(searchVO.getOutmax()); // 사용자 클릭 요청수
			json_section.setPagenum(searchVO.getPg()); // xml 결과 출력 페이지

			List attList = makeIptvPreJsonAtt(searchVO, result, queryList[i], sectionListArr[i]);

			// sectionName.add(sectionListArr[i]);

			json_section.setJson_att_list(attList);
			Json_Section_List json_section_list = new Json_Section_List();
			json_section_list.setJson_section(json_section);
			JsonSectionList.add(json_section_list);

			// totalCnt = 0;

		}

		logger.debug("[SEARCH Total CNT] " + totalSum);

		// 검색 결과 담기
		response.setJson_section_list(JsonSectionList);

		// 연간검색어 담기
		if (totalSum == 0) {
			response.setErrorResponse(IptvPreUtilManager.makeRsInfoJson(-1));
		}
		return response;
	}

	/**
	 * 검색 결과 json 담기
	 * 
	 * @param response
	 * @param searchVO,response,result,query
	 * @return OpenAPIVideoResponseresult
	 */
	private List makeIptvPreJsonAtt(IptvPreVO searchVO, Result result, Query query, String section) throws Exception {
		String collectionName = "";
		List attList = new ArrayList();
		if(StringUtils.equals("LTE_REP", section) 
				|| StringUtils.equals("LTE_MOV", section)
				|| StringUtils.equals("LTE_KIDS", section)) {
			//System.out.println("컨텐츠");
			collectionName = "IPTV_PRE";
		}else if(StringUtils.equals("LTE_CHA", section)) {
			collectionName = "I30_CHANNEL";
		}
		
		List<IptvPre_vod> iptvPreList = new ArrayList<IptvPre_vod>();
		List<IptvPre_cha> channelList = new ArrayList<IptvPre_cha>();
		// System.out.println(section);
		if("IPTV_PRE".equals(collectionName)) {
			for (int i = 0; i < result.getRealSize(); i++) {
				String broadDate = "";
				String endTime = "";
				String orderDate = "";

				IptvPre_vod iptvPre_vod = new IptvPre_vod();
				// System.out.println("searchVO.getCollection() : "+searchVO.getCollection());
				for (int k = 0; k < result.getNumField(); k++) {
					String selectFieldName = new String((query.getSelectFields())[k].getField());
					String selectFieldValue = new String(result.getResult(i, k));
					if (selectFieldName.equals("RESULT_TYPE")) {
						iptvPre_vod.setResultType(selectFieldValue);
					} else if (selectFieldName.equals("CAT_GB")) {
						iptvPre_vod.setCatGb(selectFieldValue);
					} else if (selectFieldName.equals("CAT_ID")) {
						iptvPre_vod.setCatId(selectFieldValue);
					} else if (selectFieldName.equals("CAT_NAME")) {
						iptvPre_vod.setCatName(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_ID")) {
						iptvPre_vod.setAlbumId(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_NAME")) {
						iptvPre_vod.setAlbumName(selectFieldValue);
					} else if (selectFieldName.equals("IMG_URL")) {
						iptvPre_vod.setImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("IMG_FILE_NAME")) {
						iptvPre_vod.setImgFileName(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_GB")) {
						iptvPre_vod.setServiceGb(selectFieldValue);
					} else if (selectFieldName.equals("PRICE")) {
						iptvPre_vod.setPrice(selectFieldValue);
					} else if (selectFieldName.equals("PR_INFO")) {
						iptvPre_vod.setPrInfo(selectFieldValue);
					} else if (selectFieldName.equals("RUNTIME")) {
						iptvPre_vod.setRuntime(selectFieldValue);
					} else if (selectFieldName.equals("IS_CAPTION")) {
						iptvPre_vod.setIsCaption(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR")) {
						iptvPre_vod.setActor(selectFieldValue);
					} else if (selectFieldName.equals("OVERSEER_NAME")) {
						iptvPre_vod.setOverseerName(selectFieldValue);
					} else if (selectFieldName.equals("GENRE1")) {
						iptvPre_vod.setGenre1(selectFieldValue);
					} else if (selectFieldName.equals("GENRE2")) {
						iptvPre_vod.setGenre2(selectFieldValue);
					} else if (selectFieldName.equals("GENRE3")) {
						iptvPre_vod.setGenre3(selectFieldValue);
					} else if (selectFieldName.equals("SERIES_NO")) {
						iptvPre_vod.setSeriesNo(selectFieldValue);
					} else if (selectFieldName.equals("POINT")) {
						iptvPre_vod.setPoint(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_DATE")) {
						broadDate = selectFieldValue;
						iptvPre_vod.setBroadDate(selectFieldValue);
					} else if (selectFieldName.equals("RELEASE_DATE")) {
						iptvPre_vod.setReleaseDate(selectFieldValue);
					} else if (selectFieldName.equals("STARRING_ACTOR")) {
						iptvPre_vod.setStarringActor(selectFieldValue);
					} else if (selectFieldName.equals("VOICE_ACTOR")) {
						iptvPre_vod.setVoiceActor(selectFieldValue);
					} else if (selectFieldName.equals("BROADCASTER")) {
						iptvPre_vod.setBroadcaster(selectFieldValue);
					} else if (selectFieldName.equals("SER_CAT_ID")) {
						iptvPre_vod.setSerCatId(selectFieldValue);
					} else if (selectFieldName.equals("MULTI_MAPPING_FLAG")) {
						iptvPre_vod.setMultiMappingFlag(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_URL")) {
						iptvPre_vod.setPosterFileUrl(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_NAME_10")) {
						iptvPre_vod.setPosterFileName10(selectFieldValue);
					} else if (selectFieldName.equals("POSTER_FILE_NAME_30")) {
						iptvPre_vod.setPosterFileName30(selectFieldValue);
					} else if (selectFieldName.equals("TITLE_ENG")) {
						iptvPre_vod.setTitleEng(selectFieldValue);
					} else if (selectFieldName.equals("DIRECTOR_ENG")) {
						iptvPre_vod.setDirectorEng(selectFieldValue);
					} else if (selectFieldName.equals("PLAYER_ENG")) {
						iptvPre_vod.setPlayerEng(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME_ENG")) {
						iptvPre_vod.setCastNameEng(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME")) {
						iptvPre_vod.setCastName(selectFieldValue);
					} else if (selectFieldName.equals("TITLE_ORIGIN")) {
						iptvPre_vod.setTitleOrigin(selectFieldValue);
					} else if (selectFieldName.equals("WRITER_ORIGIN")) {
						iptvPre_vod.setWriterOrigin(selectFieldValue);
					} else if (selectFieldName.equals("PUBLIC_CNT")) {
						iptvPre_vod.setPublicCnt(selectFieldValue);
					} else if (selectFieldName.equals("POINT_WATCHA")) {
						iptvPre_vod.setPointWatcha(selectFieldValue);
					} else if (selectFieldName.equals("RETENTION_YN")) {
						iptvPre_vod.setRetentionYn(selectFieldValue);
					} else if (selectFieldName.equals("KEYWORD")) {
						iptvPre_vod.setKeyword(selectFieldValue);
					} else if (selectFieldName.equals("TITLE")) {
						iptvPre_vod.setTitle(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME_MAX")) {
						iptvPre_vod.setCastNameMax(selectFieldValue);
					} else if (selectFieldName.equals("CAST_NAME_MAX_ENG")) {
						iptvPre_vod.setCastNameMaxEng(selectFieldValue);
					} else if (selectFieldName.equals("ACT_DISP_MAX")) {
						iptvPre_vod.setActDispMax(selectFieldValue);
					} else if (selectFieldName.equals("ACT_DISP_MAX_ENG")) {
						iptvPre_vod.setActDispMaxEng(selectFieldValue);
					} else if (selectFieldName.equals("ALBUM_NO")) {
						iptvPre_vod.setAlbumNo(selectFieldValue);
					} else if (selectFieldName.equals("STILL_IMG_NAME")) {
						iptvPre_vod.setStillImgName(selectFieldValue);
					} else if (selectFieldName.equals("THEME_YN")) {
						iptvPre_vod.setThemeYn(selectFieldValue);
					} else if (selectFieldName.equals("CLOSE_YN")) {
						iptvPre_vod.setCloseYn(selectFieldValue);
					} else if (selectFieldName.equals("WEIGHT")) {
						iptvPre_vod.setWeight(selectFieldValue);
					} else if (selectFieldName.equals("CUESHEET_TYPE")) {
						iptvPre_vod.setCueSheetType(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR_ID")) {
						iptvPre_vod.setActorId(selectFieldValue);
					} /*
						 * else if (selectFieldName.equals("ACTOR_NAME")) {
						 * iptvPre_vod.setActorName(selectFieldValue); }
						 */
					else if (selectFieldName.equals("CONCERT_IMG_URL")) {
						iptvPre_vod.setConcertImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("CONCERT_IMG_FILE_NAME")) {
						iptvPre_vod.setConcertImgFileName(selectFieldValue);
					} else if (selectFieldName.equals("CONCERT_IMG_URL")) {
						iptvPre_vod.setConcertImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("CUESHEET_VIDEO_TYPE")) {
						iptvPre_vod.setCueSheetVideoType(selectFieldValue);
					} else if (selectFieldName.equals("END_TIME")) {
						endTime = selectFieldValue;
						iptvPre_vod.setEndTime(selectFieldValue);
					} else if (selectFieldName.equals("VIDEO_TYPE")) {
						iptvPre_vod.setVideoType(selectFieldValue);
					} else if (selectFieldName.equals("RUN_TIME")) {
						iptvPre_vod.setRun_time(selectFieldValue);
					} /*
						 * else if (selectFieldName.equals("CHANNEL_ID")) {
						 * iptvPre_vod.setChannelId(selectFieldValue); } // 공연2차고도화 값추가 else if
						 * (selectFieldName.equals("PROGRAM_ID")) {
						 * iptvPre_vod.setProgramId(selectFieldValue); } else if
						 * (selectFieldName.equals("CORNERID")) {
						 * iptvPre_vod.setCornerId(selectFieldValue); } else if
						 * (selectFieldName.equals("CLIPORDER")) {
						 * iptvPre_vod.setClipOrder(selectFieldValue); } else if
						 * (selectFieldName.equals("MEDIAURL")) {
						 * iptvPre_vod.setMediaUrl(selectFieldValue); } else if
						 * (selectFieldName.equals("MEZZOAD")) { iptvPre_vod.setMezzoAd(selectFieldValue);
						 * } else if (selectFieldName.equals("START_DT")) {
						 * iptvPre_vod.setStartDate(selectFieldValue); } else if
						 * (selectFieldName.equals("END_DT")) { iptvPre_vod.setEndDate(selectFieldValue); }
						 * else if (selectFieldName.equals("BADGE_DATA")) {
						 * iptvPre_vod.setBadgeData(selectFieldValue); } else if
						 * (selectFieldName.equals("BADGE_DATA2")) {
						 * iptvPre_vod.setBadgeData2(selectFieldValue); } else if
						 * (selectFieldName.equals("DEVICE_DATA")) {
						 * iptvPre_vod.setDeviceData(selectFieldValue); }
						 */ else if (selectFieldName.equals("ORDER_DATE")) {
						orderDate = selectFieldValue;
						iptvPre_vod.setOrderDate(selectFieldValue);
					} else if (selectFieldName.equals("NSC_GB")) {
						iptvPre_vod.setNscGb(selectFieldValue);
					}  else if (selectFieldName.equals("KIDS_GRADE")) {
						iptvPre_vod.setKidsGrade(selectFieldValue);
					} 
				}
				//// LIVE , ToDay 체크
				if (StringUtils.isNotEmpty(orderDate)) {
					boolean liveCheck = false;
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
					String today = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
					if (StringUtils.isNotEmpty(endTime)) {
						try {
							Date DorderDate = format.parse(orderDate);
							Date DtoDay = format.parse(today);
							Date DendTime = format.parse(endTime);
						
							if (DtoDay.compareTo(DorderDate) > 0 && DendTime.compareTo(DtoDay) > 0) {
								// 현재상영
								iptvPre_vod.setTagName("LIVE");
								liveCheck = true;
							} else {
								String Mtoday = new SimpleDateFormat("yyyyMMdd").format(new Date());
								String MorderDate = new SimpleDateFormat("yyyyMMdd").format(format.parse(orderDate));

								if (StringUtils.equals(Mtoday, MorderDate)) {
									// 당일날
									iptvPre_vod.setTagName("Today");
								} else if (DorderDate.compareTo(DtoDay) > 0) {
									Calendar CtoDay = Calendar.getInstance();
									Calendar CorderDate = Calendar.getInstance();

									CtoDay.set(Integer.parseInt(StringUtils.substring(Mtoday, 0, 4)), // 년
											Integer.parseInt(StringUtils.substring(Mtoday, 4, 6)), // 월
											Integer.parseInt(StringUtils.substring(Mtoday, 6, 8)));// 일

									CorderDate.set(Integer.parseInt(StringUtils.substring(MorderDate, 0, 4)),
											Integer.parseInt(StringUtils.substring(MorderDate, 4, 6)),
											Integer.parseInt(StringUtils.substring(MorderDate, 6, 8)));

									long lToday = CtoDay.getTimeInMillis() / (24 * 60 * 60 * 1000);
									long lOrderDate = CorderDate.getTimeInMillis() / (24 * 60 * 60 * 1000);

									long dDay = lToday - lOrderDate;
									if (dDay < 0) {
										// Dday계산
										iptvPre_vod.setTagName("D" + dDay);
									}
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				///

				String videoType = iptvPre_vod.getVideoType();

				if (StringUtils.equals(videoType, "C")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("각도별옴니뷰");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",각도별옴니뷰");
					}
				} else if (StringUtils.equals(videoType, "O")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("멤버별옴니뷰");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",멤버별옴니뷰");
					}
				}

				String serviceGb = iptvPre_vod.getServiceGb();

				if (StringUtils.equals(serviceGb, "V23")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("2D 360");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",2D 360");
					}
				} else if (StringUtils.equals(serviceGb, "V21")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("2D 180");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",2D 180");
					}
				} else if (StringUtils.equals(serviceGb, "V3U")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("3D 360(Up-Bottom)");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",3D 360(Up-Bottom)");
					}
				} else if (StringUtils.equals(serviceGb, "V1U")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("3D 180(Up-Bottom)");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",3D 180(Up-Bottom)");
					}
				} else if (StringUtils.equals(serviceGb, "V3S")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("3D 360(Side-by-side)");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",3D 360(Side-by-side)");
					}
				} else if (StringUtils.equals(serviceGb, "V1S")) {
					if (iptvPre_vod.getTagName().equals("")) {
						iptvPre_vod.setTagName("3D 180(Side-by-side)");
					} else {
						iptvPre_vod.setTagName(iptvPre_vod.getTagName() + ",3D 180(Side-by-side)");
					}
				}
				iptvPreList.add(iptvPre_vod);

			}
		}

		else if("I30_CHANNEL".equals(collectionName)) {
			for (int i = 0; i < result.getRealSize(); i++) {
				IptvPre_cha kids_char = new IptvPre_cha();
				for (int k = 0; k < result.getNumField(); k++) {

					String selectFieldName = new String((query.getSelectFields())[k].getField());
					String selectFieldValue = new String(result.getResult(i, k));
					if (selectFieldName.equals("SERVICE_ID")) {
						kids_char.setServiceId(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_NAME")) {
						kids_char.setServiceName(selectFieldValue);
					} else if (selectFieldName.equals("SERVICE_ENG_NAME")) {
						kids_char.setServiceEngName(selectFieldValue);
					} else if (selectFieldName.equals("CHANNEL_NO")) {
						kids_char.setChannelNo(selectFieldValue);
					} else if (selectFieldName.equals("PROGRAM_ID")) {
						kids_char.setProgramId(selectFieldValue);
					} else if (selectFieldName.equals("PROGRAM_NAME")) {
						kids_char.setProgramName(selectFieldValue);
					} else if (selectFieldName.equals("THM_IMG_URL")) {
						kids_char.setThmImgUrl(selectFieldValue);
					} else if (selectFieldName.equals("THM_IMG_FILE_NAME")) {
						kids_char.setThmImgFileName(selectFieldValue);
					} else if (selectFieldName.equals("RATING")) {
						kids_char.setRating(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_TIME")) {
						kids_char.setBroadTime(selectFieldValue);
					} else if (selectFieldName.equals("DAY")) {
						kids_char.setDay(selectFieldValue);
					} else if (selectFieldName.equals("OVERSEER_NAME")) {
						kids_char.setOverseerName(selectFieldValue);
					} else if (selectFieldName.equals("ACTOR")) {
						kids_char.setActor(selectFieldValue);
					} else if (selectFieldName.equals("P_NAME")) {
						kids_char.setPName(selectFieldValue);
					} else if (selectFieldName.equals("GENRE1")) {
						kids_char.setGenre1(selectFieldValue);
					} else if (selectFieldName.equals("GENRE2")) {
						kids_char.setGenre2(selectFieldValue);
					} else if (selectFieldName.equals("GENRE3")) {
						kids_char.setGenre3(selectFieldValue);
					} else if (selectFieldName.equals("SERIES_NO")) {
						kids_char.setSeriesNo(selectFieldValue);
					} else if (selectFieldName.equals("SUB_NAME")) {
						kids_char.setSubName(selectFieldValue);
					} else if (selectFieldName.equals("BROAD_DATE")) {
						kids_char.setBroadDate(selectFieldValue);
					} else if (selectFieldName.equals("LOCAL_AREA")) {
						kids_char.setLocalArea(selectFieldValue);
					} else if (selectFieldName.equals("AV_RESOLUTION")) {
						kids_char.setAvResolution(selectFieldValue);
					} else if (selectFieldName.equals("Caption_Flag")) {
						kids_char.setCaptionFlag(selectFieldValue);
					} else if (selectFieldName.equals("Dvs_Flag")) {
						kids_char.setDvsFlag(selectFieldValue);
					} else if (selectFieldName.equals("IS_51_CH")) {
						kids_char.setIs51Ch(selectFieldValue);
					} else if (selectFieldName.equals("FILTERING_CODE")) {
						kids_char.setFilteringCode(selectFieldValue);
					} else if (selectFieldName.equals("CHNL_KEYWORD")) {
						kids_char.setChnlKeyword(selectFieldValue);
					} else if (selectFieldName.equals("CHNL_ICON_URL")) {
						kids_char.setChnlIconUrl(selectFieldValue);
						String rPre = "\\";
						rPre = rPre + rPre;
						String rAfter = "\\";
						kids_char.setChnlIconUrl(selectFieldValue.replace(rPre, rAfter));
					} else if (selectFieldName.equals("CHNL_ICON_FILE_NAME")) {
						kids_char.setChnlIconFileName(selectFieldValue);
					} else if (selectFieldName.equals("WEIGHT")) {
						kids_char.setWeight(selectFieldValue);
					}
				}
				channelList.add(kids_char);
			}
		}


		if("IPTV_PRE".equals(collectionName)) {
			attList = iptvPreList;
		}else if("I30_CHANNEL".equals(collectionName)) {
			attList = channelList;
		}
		

		return attList;
	}

	/**
	 * KEYWORD 100 byte 자르기
	 * 
	 * @param strs
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



	/*
	 * public OpenAPIResponseJson searchJsonTest(KidsVO vo, int keyword_byte) { //
	 * TODO Auto-generated method stub OpenAPIResponseJson resultResponse = new
	 * OpenAPIResponseJson(); resultResponse = makeResponseJsonTest(vo,
	 * resultResponse); System.out.println("searchJsonTest"); return resultResponse;
	 * }
	 */

	/*
	 * private OpenAPIResponseJson makeResponseJsonTest(KidsVO vo,
	 * OpenAPIResponseJson resultResponse) { String[] sectionListArr =
	 * vo.getSectionListArr(); Json_Section json_section = null; List
	 * JsonSectionList = new ArrayList(); for (int i = 0; i < sectionListArr.length;
	 * i++) {
	 * 
	 * json_section = new Json_Section();
	 * 
	 * // 참조용 파라미터 셋팅 json_section.setSection_name(sectionListArr[i]); // 검색 섹션
	 * 
	 * json_section.setOutcnt(String.valueOf(vo.getOutmax()));
	 * 
	 * // 공통 파라미터 셋팅 json_section.setTotcnt(vo.getOutmax() + ""); // 검색 결과 전체 건수
	 * json_section.setMaxcnt(vo.getOutmax()); // 사용자 클릭 요청수
	 * json_section.setPagenum(1+""); // xml 결과 출력 페이지
	 * 
	 * List attList = makeKidsJsonAttTest(sectionListArr[i],vo.getOutmax());
	 * 
	 * // sectionName.add(sectionListArr[i]);
	 * 
	 * json_section.setJson_att_list(attList); Json_Section_List json_section_list =
	 * new Json_Section_List(); json_section_list.setJson_section(json_section);
	 * JsonSectionList.add(json_section_list);
	 * 
	 * // totalCnt = 0;
	 * 
	 * }
	 * 
	 * System.out.println("makeResponseJsonTest"); // 검색 결과 담기
	 * resultResponse.setJson_section_list(JsonSectionList); return resultResponse;
	 * }
	 */
	/*
	 * private List makeKidsJsonAttTest(String section, String outmax) { List
	 * attList = new ArrayList();
	 * 
	 * List<iptvPre_vod> kidsVodList = new ArrayList<iptvPre_vod>(); List<Kids_cha>
	 * kidsChaList = new ArrayList<Kids_cha>();
	 * 
	 * // System.out.println(section); int resultSize = Integer.parseInt(outmax);
	 * for (int i = 0; i < resultSize; i++) { String broadDate = ""; String endTime
	 * = ""; String orderDate = "";
	 * 
	 * iptvPre_vod iptvPre_vod = new iptvPre_vod(); Kids_cha kids_cha = new Kids_cha();
	 * String text = "testData"; if(StringUtils.equals("MKIDS_CHA", section)) {
	 * kids_cha.kids_cha_test(text+i); } else {
	 * 
	 * iptvPre_vod.setResultType("outmax 만큼 출력"); iptvPre_vod.setTagName(text + i);
	 * iptvPre_vod.setCatGb(text + i); iptvPre_vod.setCatId(text + i);
	 * iptvPre_vod.setCatName(text + i); iptvPre_vod.setAlbumId(text + i);
	 * iptvPre_vod.setAlbumName(text + i); iptvPre_vod.setImgUrl(text + i);
	 * iptvPre_vod.setImgFileName(text + i); iptvPre_vod.setServiceGb(text + i);
	 * iptvPre_vod.setPrice(text + i); iptvPre_vod.setPrInfo(text + i);
	 * iptvPre_vod.setRuntime(text + i); iptvPre_vod.setIsCaption(text + i);
	 * iptvPre_vod.setActor(text + i); iptvPre_vod.setOverseerName(text + i);
	 * iptvPre_vod.setGenre1(text + i); iptvPre_vod.setGenre2(text + i);
	 * iptvPre_vod.setGenre3(text + i); iptvPre_vod.setSeriesNo(text + i);
	 * iptvPre_vod.setPoint(text + i); iptvPre_vod.setBroadDate(text + i);
	 * iptvPre_vod.setReleaseDate(text + i); iptvPre_vod.setStarringActor(text + i);
	 * iptvPre_vod.setVoiceActor(text + i); iptvPre_vod.setBroadcaster(text + i);
	 * iptvPre_vod.setSerCatId(text + i); iptvPre_vod.setMultiMappingFlag(text + i);
	 * iptvPre_vod.setPosterFileUrl(text + i); iptvPre_vod.setPosterFileName10(text + i);
	 * iptvPre_vod.setPosterFileName30(text + i); iptvPre_vod.setTitleEng(text + i);
	 * iptvPre_vod.setDirectorEng(text + i); iptvPre_vod.setPlayerEng(text + i);
	 * iptvPre_vod.setCastNameEng(text + i); iptvPre_vod.setCastName(text + i);
	 * iptvPre_vod.setTitleOrigin(text + i); iptvPre_vod.setWriterOrigin(text + i);
	 * iptvPre_vod.setPublicCnt(text + i); iptvPre_vod.setPointWatcha(text + i);
	 * iptvPre_vod.setRetentionYn(text + i); iptvPre_vod.setKeyword(text + i);
	 * iptvPre_vod.setTitle(text + i); iptvPre_vod.setCastNameMax(text + i);
	 * iptvPre_vod.setCastNameMaxEng(text + i); iptvPre_vod.setActDispMax(text + i);
	 * iptvPre_vod.setActDispMaxEng(text + i); iptvPre_vod.setAlbumNo(text + i);
	 * iptvPre_vod.setStillImgName(text + i); iptvPre_vod.setThemeYn(text + i);
	 * iptvPre_vod.setCloseYn(text + i); iptvPre_vod.setWeight(text + i);
	 * iptvPre_vod.setCueSheetType(text + i); iptvPre_vod.setActorId(text + i);
	 * iptvPre_vod.setActorName(text + i); iptvPre_vod.setConcertImgUrl(text + i);
	 * iptvPre_vod.setConcertImgFileName(text + i); iptvPre_vod.setConcertImgUrl(text +
	 * i); iptvPre_vod.setCueSheetVideoType(text + i); iptvPre_vod.setEndTime(text + i);
	 * iptvPre_vod.setVideoType(text + i); iptvPre_vod.setRun_time(text + i);
	 * iptvPre_vod.setChannelId(text + i); iptvPre_vod.setProgramId(text + i);
	 * iptvPre_vod.setCornerId(text + i); iptvPre_vod.setClipOrder(text + i);
	 * iptvPre_vod.setMediaUrl(text + i); iptvPre_vod.setMezzoAd(text + i);
	 * iptvPre_vod.setStartDate(text + i); iptvPre_vod.setEndDate(text + i);
	 * iptvPre_vod.setBadgeData(text + i); iptvPre_vod.setBadgeData2(text + i);
	 * iptvPre_vod.setDeviceData(text + i); iptvPre_vod.setOrderDate(text + i);
	 * iptvPre_vod.setNscGb(text + i); iptvPre_vod.setKidsGrade(text+i); } //
	 * System.out.println("searchVO.getCollection() : "+searchVO.getCollection());
	 * 
	 * 
	 * kidsVodList.add(iptvPre_vod); kidsChaList.add(kids_cha);
	 * 
	 * }
	 * 
	 * if(StringUtils.equals("MKIDS_CHA", section)) { attList = kidsChaList; }else {
	 * attList = kidsVodList; }
	 * 
	 * //System.out.println("makeResponseJsonTest aa"); return attList;
	 * 
	 * }
	 */

	public OpenAPIQuickResponseJson searchQuickJsonTest(IptvPreVO vo, int keyword_byte) {
		OpenAPIQuickResponseJson resultResponse = new OpenAPIQuickResponseJson();
		resultResponse = makeQuickResponseJsonTest(resultResponse); 
		return resultResponse;
	}

	private OpenAPIQuickResponseJson makeQuickResponseJsonTest(OpenAPIQuickResponseJson resultResponse) {
		List wordList = new ArrayList();
		List JsonSectionList = new ArrayList();
		wordList = makeAutoWordTest();
		resultResponse.setAutoWord(wordList);
		logger.debug("[SEARCH AutoWord CNT] " + wordList.size());


		// logger.debug("[SEARCH Corqry] " + response.getCorqry());
		return resultResponse;
	}
	private List makeAutoWordTest() {
		ArrayList wordList = new ArrayList();

		for (int i = 0; i < 10; i++) {
			wordList.add("testData" + i);
		}

		return wordList;
	}
}
