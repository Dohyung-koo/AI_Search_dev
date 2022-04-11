package com.diquest.openapi;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.diquest.openapi.google.GoogleUtilManager;
import com.diquest.openapi.google.log.GoogleLogService;
import com.diquest.openapi.google.log.GoogleLogVO;
import com.diquest.openapi.google.response.ErrorString;
import com.diquest.openapi.google.response.FallbackExecution;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.diquest.openapi.google.GoogleService;
import com.diquest.openapi.google.request.GoogleRequest;
import com.diquest.openapi.google.response.GoogleErrorResponse;
import com.diquest.openapi.google.response.cha.GoogleChaRespones;
import com.diquest.openapi.google.response.entity.GoogleEntRespones;
import com.diquest.openapi.iptvpre.IptvPreService;
import com.diquest.openapi.iptvpre.IptvPreVO;
import com.diquest.openapi.iptvpre.PreUtilManager;
import com.diquest.openapi.log.LogService;
import com.diquest.openapi.log.LogVO;

//import com.diquest.openapi.preiptv.IptvPreService;
//import com.diquest.openapi.preiptv.IptvPreVO;
//import com.diquest.openapi.preiptv.IptvPreUtilManager;
//import com.diquest.openapi.log.LogService;
//import com.diquest.openapi.log.LogVO;

import com.diquest.openapi.util.Json_Error;
import com.diquest.openapi.util.OpenAPIErrorResponse;
import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.util.OpenAPIQuickResponseJson;
import com.diquest.openapi.util.OpenAPIResponseJson;
import com.diquest.openapi.util.Suberror;
import com.diquest.openapi.util.UtilManager;
import com.diquest.openapi.util.info.ERROR_TYPE;
import com.diquest.openapi.videopotal.OpenAPIVideoQuickResponse;
import com.diquest.openapi.videopotal.OpenAPIVideoQuickResponseJson;
import com.diquest.openapi.videopotal.OpenAPIVideoResponse;
import com.diquest.openapi.videopotal.OpenAPIVideoResponseJson;
import com.diquest.openapi.videopotal.VideoService;
import com.diquest.openapi.videopotal.VideoVO;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@Path("/")
public class OpenAPI {

	@Autowired
	VideoService videoService;

	@Autowired
	GoogleService googleService;
	
	@Autowired
	IptvPreService iptvPreService;

	@Autowired
	LogService logService;

	@Autowired
	GoogleLogService googleLogService;

	@Value("${was_num}") private String was_num;
	@Value("${google_was_num}") private String google_was_num;
	@Value("${search.host}") private String searchHost;
	@Value("${search.port}") private int searchPort;
	@Value("${keyword_byte}") private int keyword_byte;
	
	@Value("${section.list}") private String sectionList;
	@Value("${quick.list}") private String quickList;
	
	@Value("${iptvpre.section.list}") private String iptvPresectionList;
	@Value("${iptvpre.quick.list}") private String iptvPrequickList;
	
	@Value("${keyword_num") private String keyword_num;
	
	@Value("${iptvpre.kidscha.list}") private String iptvPreChaList;
	
	public Logger logger = Logger.getLogger(this.getClass());
	
//	private final String searchRequestURL                 = "/video";
	private final String searchIptvRequestURL         = "/search/iptv/total";
	private final String searchGoogleRequestURL = "/google/total";
	private final String searchPreIptvRequestURL = "/search/iptv_pre/total";
	private final String searchTestRequestURL = "/wastest";
	private final String videoHangCheckRequestURL = "/video/HangCheck";
	private final String aliveCheckMessage = "Alive ACK!!!!";
	private final String aliveCheckResponseMessage = "I'm Alive";
	private final String sectionIptvPreList = "LTE_REP,LTE_MOV,LTE_KIDS";
	
	///////////////////////////////////////////// 관리도구 /////////////////////////////////////////////
	
	/**
	 *  관리도구 오타보정, 연관검색어
	 * @param input
	 * @param request
	 * @param uriInfo
	 * @return
	 */
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
//	@Path(searchRequestURL)
//	public OpenAPIResponse searchPostProcess(String input, @Context HttpServletRequest request) {
//
//		// 검색 요청 기본 정보 설정
//		
//		try {
////			System.out.println("### input : " + input);			
//			// 검색 요청 변수 유효성 검사
//			SearchVO searchVO = searchService.parseInput(input);
//			searchVO.setHost(searchHost);	// 검색엔진 ip
//			searchVO.setPort(searchPort);	// 검색엔진 port
//			
//			// 검색 수행
//			return searchService.search(searchVO);
//		} catch (Exception e) {
//			logger.error("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION START =+=+=+=+=+=+=+=+=+=+=+=+=+=");
//			logger.error("EXCEPTION 위치 : " + request.getServletPath());
//			logger.error("EXCEPTION 내용 : \n" + e.toString());
//			logger.error("=+=+=+=+=+=+=+=+=+=+=+=+=+= EXCEPTION END   =+=+=+=+=+=+=+=+=+=+=+=+=+=");
//			return null;
//		} finally {
//			logger.debug("=========================== DEBUG START ===========================");
//			logger.debug("DEBUG 위치 : " + request.getServletPath());
//			logger.debug("=========================== DEBUG END   ===========================");
//		}
//		
//	}
	
	///////////////////////////////////////////// VIDEO 통합검색/순간검색:자동완성 /////////////////////////////////////////////
	
	/**
	 * 통합검색 POST (xml, json)
	 * @param returnType
	 * @param URL
	 * @param w
	 * @param q
	 * @param section
	 * @param pg
	 * @param outmax
	 * @param sort
	 * @param p
	 * @param d
	 * @param csq
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path(searchIptvRequestURL)
	public Response videoSearchPostProcess( 
			
			@DefaultValue("text/xml") @HeaderParam("return-type") String returnType,
			
			@HeaderParam("auth-key") String authKey,
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey,
			@HeaderParam("user-sid") String userSid,
			@HeaderParam("email") String email,
			@RequestParam String all,
			@DefaultValue("") @FormParam("w") String w,
			@DefaultValue("") @FormParam("q") String q,
			@DefaultValue("") @FormParam("section") String section,
			@DefaultValue("1") @FormParam("pg") String pg,
			@DefaultValue("10") @FormParam("outmax") String outmax,
			@DefaultValue("") @FormParam("sort") String sort,
			@DefaultValue("") @FormParam("p") String p,
			@DefaultValue("") @FormParam("region1") String region1,
			@DefaultValue("") @FormParam("region2") String region2,
			@DefaultValue("") @FormParam("region3") String region3,
			@DefaultValue("") @FormParam("cat_gb") String cat_gb
			
			) {
//		System.out.println("all::::::::::::::"+all);
		logger.debug("=========================== IPTV SERVICE API(POST) START   ===========================");
		String url="/";
		String [] qarr = all.split("&");
		
		for ( int i = 0 ;i<qarr.length ; i++) {
			
			if (qarr[i].indexOf("q=") == 0 ) {
				q = qarr[i].toString().replaceFirst("q=", "");
				if (i <qarr.length-1) {
					if (qarr[i+1].toString().indexOf("w=")>-1 || qarr[i+1].toString().indexOf("section=")>-1|| qarr[i+1].toString().indexOf("pg=")>-1|| qarr[i+1].toString().indexOf("outmax=")>-1|| qarr[i+1].toString().indexOf("sort=")>-1|| qarr[i+1].toString().indexOf("p=")>-1|| qarr[i+1].toString().indexOf("region1=")>-1||qarr[i+1].toString().indexOf("region2=")>-1||qarr[i+1].toString().indexOf("region3=")>-1|| qarr[i+1].toString().indexOf("cat_gb=")>-1){
						//
					} else {// 'q' 값에 '&' 가 들어간경우 합침 
						q = q+"&"+qarr[i+1].toString();
					}
				}else if (i == qarr.length-1 && (all.substring(all.length()-1,all.length())).equals("&")){
					q = q+"&";
				}
					
			} else if (qarr[i].indexOf("w=") == 0) {
				w = qarr[i].toString().replaceFirst("w=", "");
			}else if (qarr[i].indexOf("section=") ==0) {
				section = qarr[i].toString().replaceFirst("section=", "");
			}else if (qarr[i].indexOf("pg=") ==0) {
				pg = qarr[i].toString().replaceFirst("pg=", "");
			}else if (qarr[i].indexOf("outmax=") ==0) {
				outmax = qarr[i].toString().replaceFirst("outmax=", "");
			}else if (qarr[i].indexOf("sort=") ==0) {
				sort = qarr[i].toString().replaceFirst("sort=", "");
			}else if (qarr[i].indexOf("p=") ==0) {
				p = qarr[i].toString().replaceFirst("p=", "");
			}else if (qarr[i].indexOf("region1=") ==0) {
				region1 = qarr[i].toString().replaceFirst("region1=", "");
			}else if (qarr[i].indexOf("region2=") ==0) {
				region2 = qarr[i].toString().replaceFirst("region2=", "");
			}else if (qarr[i].indexOf("region3=") ==0) {
				region3 = qarr[i].toString().replaceFirst("region3=", "");
			}else if (qarr[i].indexOf("cat_gb=") ==0) {
				cat_gb = qarr[i].toString().replaceFirst("cat_gb=", "");
			}
		}
		
		
		//	로그 기본 셋팅
		LogVO logvo = new LogVO();
		logvo.setSID(uniqueKey);
		logvo.setCTN(userSid); 	//
		logvo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간
		logvo.setONEID_EMAIL(email);
		logvo.setSVC_TYPE("IPTV");
		if ( w != null && w.equals("IPTV") ) {
			logvo.setSVC_CMD("total");
		} else if ( w != null && w.equals("IPTV_QUICK") ) {
			logvo.setSVC_CMD("typing");
		} else {
			logvo.setSVC_CMD("");
		}
		
		if ( section ==null || section.equals("") ) {
			logvo.setSECTION("N");
		} else {
			logvo.setSECTION(section);
		}
		
		Response result = null;
		
		// 파라미터 셋팅
		VideoVO vo = new VideoVO();
		vo.setReturnType(returnType);
		vo.setAuthKey(authKey);
		vo.setEncryptYn(encryptYn);
		vo.setUniqueKey(uniqueKey);
		vo.setUserSid(userSid);
		vo.setEmail(email);
		
		vo.setW(w);
		
			
		vo.setQ(q);
		vo.setSection(section);
		vo.setPg(pg);
		if ( outmax.equals("")) {
			outmax="10";
		}
		vo.setOutmax(outmax);
		vo.setSort(sort);
		vo.setP(p);
		vo.setRegion1(region1);
		vo.setRegion2(region2);
		vo.setRegion3(region3);
		vo.setCat_gb(cat_gb);
		
		url += w+"/"+q+"/section="+section+"/pg="+pg+"/outmax="+vo.getOutmax()+"/sort="+sort+"/p="+p+"/region1="+region1+"/region2="+region2+"/region3="+region3+"/cat_gb="+cat_gb;
		logger.debug("[CALL URL] " + url);
		
		vo.setHost(searchHost);
		vo.setPort(searchPort);
		vo.setSectionList(sectionList);
		vo.setQuickList(quickList);
		
		try {
			
			// 동의어 사전
//			SynonymDict synonymDict = SynonymDict.getInstance(synonymDic);;
//			vo.setSynonymDict(synonymDict);
			
			// 검색 요청 변수 유효성 검사 & 디코드
			vo = UtilManager.checkVideoInfo(vo, "N", "iptv"); // N : 디코드 안함 ,  Y : 디코드 처리
			logvo.setCTN(vo.getUserSid()); 	//
//			logvo.setKEYWORD(vo.getQ());
			logvo.setKEYWORD(videoService.getByteLength(vo.getQ(),keyword_byte));
			
			if ( vo.hasErrorResponse() ) {	// 에러 O
				
				// suberror 담기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(vo.getErrorCode());
				suberror.setMessage(vo.getErrorResponse());
				suberrorList.add(suberror);
				
				if(returnType.equals("text/xml")) {
					OpenAPIErrorResponse response =  new OpenAPIErrorResponse();
					response.setCode(vo.getErrorCode());
					response.setMessage(vo.getErrorResponse());
					
					response.setSuberror(suberrorList);
					
					result =  Response.ok(response, MediaType.TEXT_XML).build();
				} else if(returnType.equals("application/json")) {
					OpenAPIErrorResponseJson response =  new OpenAPIErrorResponseJson();
					Json_Error json_error = new Json_Error();
					json_error.setCode(vo.getErrorCode());
					json_error.setMessage(vo.getErrorResponse());
					
					json_error.setSuberror(suberrorList);
					
					response.setJson_Error(json_error);
					
					result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
				}
				logger.debug("[RESULT CODE] " + vo.getErrorCode());
				logvo.setRESULT_CODE(vo.getErrorCode());
				
			} // 에러 O ------ (END)
			else {	// 에러 X
				if(returnType.equals("text/xml")) {	// retrun-type : XML
					if(w != null && w.equals("IPTV")) {
						OpenAPIVideoResponse videoResponse =  videoService.search(vo,keyword_byte);	// 검색 수행
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 O
							OpenAPIErrorResponse response =  videoResponse.getErrorResponse();
							
							if (response.getCode().equals("20001000") ) {
								videoResponse.setErrorResponse(null);
								result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
								logger.debug("[RESULT CODE] 20001000");
								logvo.setRESULT_CODE("20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getCode());
								suberror.setMessage(response.getMessage());
								suberrorList.add(suberror);
								
								response.setSuberror(suberrorList);
								
								result =  Response.ok(response, MediaType.TEXT_XML).build();
								
								logvo.setRESULT_CODE(response.getCode());
								logger.debug("[RESULT CODE] "+response.getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
							
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
							
						}
					} else if(w != null && w.equals("IPTV_QUICK")) {
						OpenAPIVideoQuickResponse videoResponse =  videoService.searchQuick(vo,keyword_byte);
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 X
							OpenAPIErrorResponse response =  videoResponse.getErrorResponse();
							if (response.getCode().equals("20001000") ) {
								videoResponse.setErrorResponse(null);
								result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getCode());
								suberror.setMessage(response.getMessage());
								suberrorList.add(suberror);
								
								response.setSuberror(suberrorList);
								
								result =  Response.ok(response, MediaType.TEXT_XML).build();
								
								logvo.setRESULT_CODE(response.getCode());
								logger.debug("[RESULT CODE] "+response.getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
							
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				} // retrun-type : XML ------ (END)
				else if(returnType.equals("application/json")) {	// retrun-type : json
					if(w != null && w.equals("IPTV")) {
						OpenAPIVideoResponseJson videoResponse =  videoService.searchJson(vo,keyword_byte);		// 검색 수행
						
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 O
							OpenAPIErrorResponseJson response =  videoResponse.getErrorResponse();
							
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
								
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
						
					} else if(w != null && w.equals("IPTV_QUICK")) {
						OpenAPIVideoQuickResponseJson videoResponse =  videoService.searchQuickJson(vo,keyword_byte);
						
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 일때
							OpenAPIErrorResponseJson response =  videoResponse.getErrorResponse();
							
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
								
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				}	//  retrun-type : json ------ (END)
			}	// 에러 X ------ (END)
			return result;
		} catch (Exception e) {
			logger.debug("[EXCEPTION START]");
//			logger.error("EXCEPTION 위치 : " + request.getServletPath());
			logger.debug("EXCEPTION 내용 : \n" + e.toString());
			
			String code="";
			if (w.equals("IPTV")) {
				code = "1000";
			} else {
				code = "2000";
			}
			if(returnType.equals("text/xml")) {
				OpenAPIErrorResponse response =  new OpenAPIErrorResponse();
				
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(code);
				
				response.setCode(code);
				if (code.equals("1000")){
					response.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					response.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					logvo.setRESULT_CODE("40002000");
					logger.debug("[RESULT CODE] 40002000");
				}
				
				suberrorList.add(suberror);
				
				response.setSuberror(suberrorList);
				
				result =  Response.ok(response, MediaType.TEXT_XML).build();

			} else if(returnType.equals("application/json")) {
				OpenAPIErrorResponseJson response =  new OpenAPIErrorResponseJson();
				Json_Error json_error = new Json_Error();
				
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(code);
				
				json_error.setCode(code);
				if (code.equals("1000")){
					json_error.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					json_error.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					logvo.setRESULT_CODE("40003000");
					logger.debug("[RESULT CODE] 40003000");
				}
				
				suberrorList.add(suberror);
				
				json_error.setSuberror(suberrorList);
				
				response.setJson_Error(json_error);
				result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
			}
			return result;
		} finally {
//			logger.debug("DEBUG 위치 : " + request.getServletPath());
			
			logvo = logService.setCommonLog(logvo);
			logvo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
			logService.writeLog(logvo,was_num);
			
			logger.debug("=========================== IPTV SERVICE API(POST) END   ===========================");
		}
		
	}
	
	/**
	 * 통합검색 - GET (xml, json)
	 * @param returnType
	 * @param w
	 * @param q
	 * @param p
	 * @param section
	 * @param pg
	 * @param outmax
	 * @param sort
	 * @param d
	 * @param csq
	 * @return
	 */
	@GET
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	@Path(searchIptvRequestURL+"/{w}/{q}/{section}/{pg}/{outmax}/{sort}/{p}/{region1}/{region2}/{region3}/{cat_gb}")
	public Response videoSearchGetProcess(
			@DefaultValue("text/xml") @HeaderParam("return-type") String returnType,
			@HeaderParam("auth-key") String authKey,
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey,
			@HeaderParam("user-sid") String userSid,
			@HeaderParam("email") String email,
			
			@PathParam("w") String w, 
			@PathParam("q") String q, 
			@PathParam("section") String section, 
			@PathParam("pg") String pg, 
			@PathParam("outmax") String outmax, 
			@PathParam("sort") String sort,
			@PathParam("p") String p,
			@PathParam("region1") String region1,
			@PathParam("region2") String region2,
			@PathParam("region3") String region3,
			@PathParam("cat_gb") String cat_gb
			) {
		logger.debug("=========================== IPTV SERVICE API(GET) START   ===========================");
		String url="/";
		Response result = null;
		// get파라미터 셋팅
		VideoVO getVO = new VideoVO();
		getVO.setReturnType(returnType);
		getVO.setAuthKey(authKey);
		getVO.setEncryptYn(encryptYn);
		getVO.setUniqueKey(uniqueKey);
		getVO.setUserSid(userSid);
		getVO.setEmail(email);
		
		getVO.setW(w);
		getVO.setQ(q.replaceFirst("q=", ""));
		getVO.setPg((pg+"").replaceFirst("pg=", ""));
		getVO.setSection(section.replaceFirst("section=", ""));
		getVO.setOutmax(outmax.replaceFirst("outmax=", ""));
		if ( getVO.getOutmax() !=null && getVO.getOutmax().equals("")) {
			outmax="10";
			getVO.setOutmax(outmax);
		}
		getVO.setSort(sort.replaceFirst("sort=", ""));
		getVO.setP(p.replaceFirst("p=", ""));
		getVO.setRegion1(region1.replaceFirst("region1=", ""));
		getVO.setRegion2(region2.replaceFirst("region2=", ""));
		getVO.setRegion3(region3.replaceFirst("region3=", ""));
		
		getVO.setCat_gb(cat_gb.replaceFirst("cat_gb=", ""));
		
		
		url += w+"/"+q+"/"+section+"/"+pg+"/outmax="+getVO.getOutmax()+"/"+sort+"/"+p+"/"+region1+"/"+region2+"/"+region3+"/"+cat_gb;
		logger.debug("[CALL URL] " + url);
		
		getVO.setHost(searchHost);
		getVO.setPort(searchPort);
		getVO.setSectionList(sectionList);
		getVO.setQuickList(quickList);
		
		//		로그 기본 셋팅
		
		
		LogVO logvo = new LogVO();
		logvo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간
		logvo.setSID(uniqueKey);
		logvo.setCTN(userSid); 	// 
		logvo.setONEID_EMAIL(email);
		logvo.setSVC_TYPE("IPTV");
		if ( w != null && w.equals("IPTV") ) {
			logvo.setSVC_CMD("total");
		} else if ( w != null && w.equals("IPTV_QUICK") ) {
			logvo.setSVC_CMD("typing");
		} else {
			logvo.setSVC_CMD("");
		}
		if ( getVO.getSection() ==null || getVO.getSection().equals("") ) {
			logvo.setSECTION("N");
		} else {
			logvo.setSECTION(getVO.getSection());
		}
		
		try {
			
			// 검색 요청 변수 유효성 검사 & 디코드
			getVO = UtilManager.checkVideoInfo(getVO, "Y", "iptv");	 // N : 디코드 안함 ,  Y : 디코드 처리
			logvo.setCTN(getVO.getUserSid()); 	//
//			logvo.setKEYWORD(getVO.getQ());
			logvo.setKEYWORD(videoService.getByteLength(getVO.getQ(),keyword_byte));
			
			if ( getVO.hasErrorResponse() ) {	// 에러 O
				
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(getVO.getW());
				suberror.setCode(getVO.getErrorCode());
				suberror.setMessage(getVO.getErrorResponse());
				suberrorList.add(suberror);
				
				if(returnType.equals("text/xml")) {
					OpenAPIErrorResponse response =  new OpenAPIErrorResponse();
					response.setCode(getVO.getErrorCode());
					response.setMessage(getVO.getErrorResponse());
					
					response.setSuberror(suberrorList);
					result =  Response.ok(response, MediaType.TEXT_XML).build();
				} else if(returnType.equals("application/json")) {
					OpenAPIErrorResponseJson response =  new OpenAPIErrorResponseJson();
					Json_Error json_error = new Json_Error();
					json_error.setCode(getVO.getErrorCode());
					json_error.setMessage(getVO.getErrorResponse());
					
					json_error.setSuberror(suberrorList);
					response.setJson_Error(json_error);
					result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
				}
				
				logvo.setRESULT_CODE(getVO.getErrorCode());
				logger.debug("[RESULT CODE] "+getVO.getErrorCode());
			} // 에러 O ------ (END)
			else {	// 에러 X
				if(returnType.equals("text/xml")) {	// retrun-type : XML
					if(w != null && w.equals("IPTV")) {
						OpenAPIVideoResponse videoResponse =  videoService.search(getVO,keyword_byte);	// 검색 수행
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 O
							OpenAPIErrorResponse response =  videoResponse.getErrorResponse();
							
//							검색성공했지만 건수가 0 일때
							if (response.getCode().equals("20001000") ) {
								videoResponse.setErrorResponse(null);
								result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(getVO.getW());
								suberror.setCode(response.getCode());
								suberror.setMessage(response.getMessage());
								suberrorList.add(suberror);
	
								response.setSuberror(suberrorList);
								
								result =  Response.ok(response, MediaType.TEXT_XML).build();
								
								logvo.setRESULT_CODE(response.getCode());
								logger.debug("[RESULT CODE] "+response.getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					} else if(w != null && w.equals("IPTV_QUICK")) {
						OpenAPIVideoQuickResponse videoResponse =  videoService.searchQuick(getVO,keyword_byte);
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 X
							OpenAPIErrorResponse response =  videoResponse.getErrorResponse();

							//							검색성공했지만 건수가 0 일때
							if (response.getCode().equals("20001000") ) {
								videoResponse.setErrorResponse(null);
								result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
							// suberror 담기
							List suberrorList = new ArrayList<Suberror>();
							Suberror suberror = new Suberror();
							suberror.setName(getVO.getW());
							suberror.setCode(response.getCode());
							suberror.setMessage(response.getMessage());
							suberrorList.add(suberror);

							response.setSuberror(suberrorList);
							
							result =  Response.ok(response, MediaType.TEXT_XML).build();
							logvo.setRESULT_CODE(response.getCode());
							logger.debug("[RESULT CODE] "+response.getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.TEXT_XML).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				} // retrun-type : XML ------ (END)
				else if(returnType.equals("application/json")) {	// 시작 retrun-type : json
					if(w != null && w.equals("IPTV")) {
						OpenAPIVideoResponseJson videoResponse =  videoService.searchJson(getVO,keyword_byte);		// 검색 수행
						
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 O
							OpenAPIErrorResponseJson response =  videoResponse.getErrorResponse();
							
//							검색성공했지만 건수가 0 일때
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {							
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(getVO.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
	
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
						
					} else if(w != null && w.equals("IPTV_QUICK")) {
						OpenAPIVideoQuickResponseJson videoResponse =  videoService.searchQuickJson(getVO,keyword_byte);
						
						if ( videoResponse.getErrorResponse() != null ) {	// 검색 에러 일때
							OpenAPIErrorResponseJson response =  videoResponse.getErrorResponse();
							
//							검색성공했지만 건수가 0 일때
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(getVO.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
								
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								response.getJson_Error().getCode();
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				}	//  retrun-type : json ------ (END)
			}	// 에러 X ------ (END)
			
			return result;
		} catch (Exception e) {
			logger.debug("[EXCEPTION START]");
//			logger.error("EXCEPTION 위치 : " + request.getServletPath());
			logger.debug("EXCEPTION 내용 : \n" + e.toString());
			
			String code="";
			if (w.equals("IPTV")) {
				code = "1000";
			} else {
				code = "2000";
			}
			if(returnType.equals("text/xml")) {
				OpenAPIErrorResponse response =  new OpenAPIErrorResponse();
				
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(getVO.getW());
				suberror.setCode(code);

				response.setCode(code);
				if (code.equals("1000")){
					response.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					response.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					logvo.setRESULT_CODE("40002000");
					logger.debug("[RESULT CODE] 40002000");
				}
				suberrorList.add(suberror);

				response.setSuberror(suberrorList);

				result =  Response.ok(response, MediaType.TEXT_XML).build();

			} else if(returnType.equals("application/json")) {
				OpenAPIErrorResponseJson response =  new OpenAPIErrorResponseJson();
				Json_Error json_error = new Json_Error();
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(getVO.getW());
				suberror.setCode(code);
				
				json_error.setCode(code);
				if (code.equals("1000")){
					json_error.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					json_error.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					logvo.setRESULT_CODE("40003000");
					logger.debug("[RESULT CODE] 40003000");
				}
				suberrorList.add(suberror);

				json_error.setSuberror(suberrorList);

				response.setJson_Error(json_error);
				result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
			}
			return result;
		} finally {
			
//			logger.debug("DEBUG 위치 : " + request.getServletPath());
			logvo = logService.setCommonLog(logvo);
			logvo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
			logService.writeLog(logvo,was_num);
			logger.debug("=========================== IPTV SERVICE API(GET) END   ===========================");
		}
		
	}
	
	
	///////////////////////////////////////////// 인기검색어 검색 API /////////////////////////////////////////////
	
/*	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(searchGoogleRequestURL)
	public Response googleGetProcess(@Context HttpHeaders headers, @RequestBody GoogleRequest googleRequest) {
//	    System.out.println("all::::::::::::::"+all);
		// System.out.println(requestHeader.toString());
		logger.debug("=========================== GOOGLE SERVICE API(GET) START   ===========================");
		Response result = null;
		System.out.println(headers.getRequestHeaders());
		// MultivaluedMap<String, String> rh = headers.getRequestHeaders();

		System.out.println(googleRequest.toString().toString());

		String queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
		if ("SWITCH_CHANNEL".equals(queryIntent)) {
			GoogleChaRespones googleChaRespones = googleService.getTestData(true);
			result = Response.ok(googleChaRespones, MediaType.APPLICATION_JSON).build();
		} else if ("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent) || "PLAY".equals(queryIntent)
				|| "SEARCH".equals(queryIntent)) {
			GoogleEntRespones googleEntRespones = googleService.getEntityTest(queryIntent);
			result = Response.ok(googleEntRespones, MediaType.APPLICATION_JSON).build();
		} else {
			// 강제에러
			GoogleChaRespones googleChaRespones = googleService.getTestData(false);
			GoogleErrorResponse googleErrorResponse = new GoogleErrorResponse();
			googleErrorResponse
					.setFallbackExecution(googleChaRespones.getGoogleErrorResponse().getFallbackExecution());
			result = Response.ok(googleErrorResponse, MediaType.APPLICATION_JSON).build();
			System.out.println("QueryIntent mismatch : " + queryIntent);
		}
		return result;
	}*/

	@POST
	//@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(searchGoogleRequestURL)
	public Response googlePostProcess(
			@HeaderParam("auth-key") String authKey,
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey,
			@HeaderParam("user-sid") String userSid,
			@HeaderParam("email") String email,
			@Context HttpHeaders headers,
			//@RequestBody GoogleRequest googleRequest,
			@RequestParam String all
			) {
//	    System.out.println("all::::::::::::::"+all);
		logger.debug("=========================== API START   ===========================");
//		System.out.println("headers : "+headers.getRequestHeaders().toString());
//		System.out.println("json : "+all);
		all = all.replaceAll("[\\x00]","");
		GoogleRequest googleRequest = null;
		googleRequest = googleService.stringToJson(all,googleRequest );
		logger.debug("=========================== GOOGLE SERVICE API(POST) START   ===========================");
		String customContext = googleRequest.getCustomContext();
		String[] arr={""};
		if(StringUtils.isNotEmpty(customContext)){
			arr = StringUtils.split(customContext,"@");
//			System.out.println("arr : "+arr.length);
			for(String a : arr){
//				System.out.println(a);
			}
			if(arr.length == 2){
				googleRequest.setRegionStr(arr[1]);
			}
		}

		String queryIntent = "";
		if(googleRequest.getStructuredQuery()!= null){
			queryIntent = googleRequest.getStructuredQuery().getQueryIntent();
		}

		logger.debug("[CALL URL] " + googleService.getUrl(googleRequest));

		LogVO logVo = new LogVO();
//		GoogleLogVO logVo = new GoogleLogVO();
		//System.out.println(logVo.toString());

		//String authKey = headers.getRequestHeader("auth-key")== null ? "" : (String) headers.getRequestHeader("auth-key");
		logVo.setSEQ_ID(googleRequest.getRequestId());
		logVo.setKEYWORD(googleRequest.getQ());
		logVo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간
		logVo.setSID(arr[0]);
//		logVo.setSID_TYPE("ENTR_NO");
		logVo.setNW_INFO("");
		logVo.setCARRIER_TYPE("L");
		logVo.setLOG_TYPE("SVC");
		logVo.setCTN(userSid);
		logVo.setONEID_EMAIL(email);
		logVo.setSVC_TYPE("GOOGLE");
		

		googleRequest.setHost(searchHost);
		googleRequest.setPort(searchPort);

		Response result = null;
//		System.out.println(googleRequest.toString());
/*
		if ("SWITCH_CHANNEL".equals(queryIntent)) {
			GoogleChaRespones googleChaRespones = googleService.getTestData(true);
			result = Response.ok(googleChaRespones, MediaType.APPLICATION_JSON).build();
		} else if ("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent) || "PLAY".equals(queryIntent)
				|| "SEARCH".equals(queryIntent)) {
			GoogleEntRespones googleEntRespones = googleService.getEntityTest(queryIntent);
			result = Response.ok(googleEntRespones, MediaType.APPLICATION_JSON).build();
		}*/
		try {

			// 검색 요청 변수 유효성 검사 & 디코드
			googleRequest = GoogleUtilManager.checkGoogleInfo(googleRequest, "Y");	 // N : 디코드 안함 ,  Y : 디코드 처리
			//logVo.setCTN(getVO.getUserSid()); 	//
			//logVo.setKEYWORD(videoService.getByteLength(getVO.getQ(),keyword_byte));

			if ( googleRequest.hasErrorResponse() ) {	// 에러 O

				GoogleErrorResponse googleErrorResponse = new GoogleErrorResponse();
				FallbackExecution fallbackExecution = new FallbackExecution();
				fallbackExecution.setErrorCode(googleRequest.getErrorCode());
				googleErrorResponse.setFallbackExecution(fallbackExecution);
				result = Response.ok(googleErrorResponse, MediaType.APPLICATION_JSON).build();

				String lgErrorCode =ErrorString.makeLgServerErrorCode("");
				logVo.setRESULT_CODE(lgErrorCode);

				logger.debug("[ERROR GOOGLE CODE] "+googleRequest.getErrorCode());
				logger.debug("[ERROR CODE] "+lgErrorCode);
				} // 에러 O ------ (END)
			else {	// 에러 X

				queryIntent = googleRequest.getStructuredQuery().getQueryIntent();

				if("ENTITY_SEARCH".equals(queryIntent) || "PLAY_TVM".equals(queryIntent) || "PLAY".equals(queryIntent)
						|| "SEARCH".equals(queryIntent)) {
					//OpenAPIVideoResponseJson videoResponse =  videoService.searchJson(getVO,keyword_byte);		// 검색 수행
					GoogleEntRespones googleEntRespones = googleService.searchJson(googleRequest,keyword_byte);
					if ( googleEntRespones.getGoogleErrorResponse() != null ) {	// 검색 에러 O
						GoogleErrorResponse errorResponse =  googleEntRespones.getGoogleErrorResponse();
						result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
						logVo.setRESULT_CODE(googleEntRespones.getLgErrorCode());
						logger.debug("[GOOGLE CODE] "+errorResponse.getFallbackExecution().getErrorCode());
						logger.debug("[RESULT CODE] "+logVo.getRESULT_CODE());


/*//							검색성공했지만 건수가 0 일때
						if (errorResponse.getFallbackExecution().getErrorCode().equals("20001000") ) {
							result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE("20001000");
							logger.debug("[RESULT CODE] 20001000");
						} else {
							result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE(errorResponse.getFallbackExecution().getErrorCode());
							logger.debug("[RESULT CODE] "+errorResponse.getFallbackExecution().getErrorCode());
						}*/
					} else {
						result =  Response.ok(googleEntRespones, MediaType.APPLICATION_JSON).build();
						logVo.setRESULT_CODE("20000000");
						logger.debug("[RESULT CODE] 20000000");
					}

				} else if("SWITCH_CHANNEL".equals(queryIntent)) {
					String channelName = googleRequest.getStructuredQuery().getChannelName();
					String channelNumber = googleRequest.getStructuredQuery().getChannelNumber();
					if(StringUtils.isNotEmpty(channelName) || StringUtils.isNotEmpty(channelNumber)){
						GoogleChaRespones googleChaRespones = googleService.googleChaJson(googleRequest,keyword_byte);
						//OpenAPIVideoQuickResponseJson videoResponse =  videoService.searchQuickJson(getVO,keyword_byte);

						if ( googleChaRespones.getGoogleErrorResponse() != null ) {	// 검색 에러 일때
							GoogleErrorResponse errorResponse =  googleChaRespones.getGoogleErrorResponse();
							result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE(errorResponse.getFallbackExecution().getErrorCode());
							logger.debug("[GOOGLE CODE] "+errorResponse.getFallbackExecution().getErrorCode());
							logger.debug("[RESULT CODE] "+googleChaRespones.getLgErrorCode());
//							검색성공했지만 건수가 0 일때
							/*						if (response.getJson_Error().getCode().equals("20001000") ) {*//**//*
							result =  Response.ok(videoResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20001000");
							logger.debug("[RESULT CODE] 20001000");
						} else {
							// suberror 담기
							List suberrorList = new ArrayList<Suberror>();
							Suberror suberror = new Suberror();
							suberror.setName(getVO.getW());
							suberror.setCode(response.getJson_Error().getCode());
							suberror.setMessage(response.getJson_Error().getMessage());
							suberrorList.add(suberror);

							Json_Error error = response.getJson_Error();
							error.setSuberror(suberrorList);
							response.setJson_Error(error);

							result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
							response.getJson_Error().getCode();
							logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
						}*/
						} else {
							result =  Response.ok(googleChaRespones, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}else{
						GoogleChaRespones googleChaRespones = googleService.googleChaPassJson(googleRequest);
						result =  Response.ok(googleChaRespones, MediaType.APPLICATION_JSON).build();
						logVo.setRESULT_CODE("20000000");
						logger.debug("[RESULT CODE] 20000000");
					}

				}

			}	// 에러 X ------ (END)

			return result;
		} catch (Exception e) {
			logger.debug("[EXCEPTION START]");
//			logger.error("EXCEPTION 위치 : " + request.getServletPath());
			logger.debug("EXCEPTION 내용 : \n" + e.toString());

			GoogleErrorResponse googleErrorResponse = new GoogleErrorResponse();
			FallbackExecution fallbackExecution = new FallbackExecution();
			fallbackExecution.setErrorCode(ErrorString.GENERAL.OPERATOR_INTERNAL_ERROR);
			googleErrorResponse.setFallbackExecution(fallbackExecution);
			result = Response.ok(googleErrorResponse, MediaType.APPLICATION_JSON).build();
			logger.debug("[ERROR CODE] "+ErrorString.makeLgServerErrorCode(queryIntent));

			return result;
		} finally {

//			logger.debug("DEBUG 위치 : " + request.getServletPath());
			logVo = logService.setCommonLog(logVo);
			logVo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
			logVo.setLOG_TIME(logService.getNowDate(17));
//			System.out.println(logVo);
			logService.writeLog(logVo,was_num);
			logger.debug("=========================== GOOGLE SERVICE API(POST) END   ===========================");
		}

		//return result;

	}
	
/*	
	///////////////////////////////////////////// 구형 IPTV 통합검색 / 자동완성 /////////////////////////////////////////////
	
	*//**
	 * 통합검색 POST (xml, json)
	 * @param returnType
	 * @param URL
	 * @param w
	 * @param q
	 * @param section
	 * @param pg
	 * @param outmax
	 * @param sort
	 * @param p
	 * @param d
	 * @param csq
	 * @return
	 *//*
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path(searchPreIptvRequestURL)
	public Response iptvPreSearchPostProcess(
			@DefaultValue("application/json") @HeaderParam("return-type") String returnType,
			@HeaderParam("auth-key") String authKey, 
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey, 
			@HeaderParam("user-sid") String userSid,
			@HeaderParam("email") String email,

			@RequestParam String all, 
			@DefaultValue("") @FormParam("w") String w,
			@DefaultValue("") @FormParam("q") String q, 
			@DefaultValue("") @FormParam("section") String section,
			@DefaultValue("1") @FormParam("pg") String pg, 
			@DefaultValue("10") @FormParam("outmax") String outmax,
			@DefaultValue("") @FormParam("sort") String sort,
			@DefaultValue("") @FormParam("p") String p,
			@DefaultValue("") @FormParam("region1") String region1,
			@DefaultValue("") @FormParam("region2") String region2,
			@DefaultValue("") @FormParam("region3") String region3,
			@DefaultValue("") @FormParam("cat_gb") String cat_gb
	) {
//		System.out.println("all::::::::::::::"+all);
		logger.debug("=========================== IPTVPRE SERVICE API(POST) START   ===========================");
		String url="/";
		String [] qarr = all.split("&");
		
		for ( int i = 0 ;i<qarr.length ; i++) {
			
			if (qarr[i].indexOf("q=") == 0 ) {
				q = qarr[i].toString().replaceFirst("q=", "");
				if (i <qarr.length-1) {
					if (qarr[i+1].toString().indexOf("w=")>-1 || qarr[i+1].toString().indexOf("section=")>-1|| qarr[i+1].toString().indexOf("pg=")>-1|| qarr[i+1].toString().indexOf("outmax=")>-1|| qarr[i+1].toString().indexOf("sort=")>-1|| qarr[i+1].toString().indexOf("p=")>-1|| qarr[i+1].toString().indexOf("region1=")>-1||qarr[i+1].toString().indexOf("region2=")>-1||qarr[i+1].toString().indexOf("region3=")>-1|| qarr[i+1].toString().indexOf("cat_gb=")>-1){
						//
					} else {// 'q' 값에 '&' 가 들어간경우 합침 
						q = q+"&"+qarr[i+1].toString();
					}
				}else if (i == qarr.length-1 && (all.substring(all.length()-1,all.length())).equals("&")){
					q = q+"&";
				}
					
			} else if (qarr[i].indexOf("w=") == 0) {
				w = qarr[i].toString().replaceFirst("w=", "");
			}else if (qarr[i].indexOf("section=") ==0) {
				section = qarr[i].toString().replaceFirst("section=", "");
			}else if (qarr[i].indexOf("pg=") ==0) {
				pg = qarr[i].toString().replaceFirst("pg=", "");
			}else if (qarr[i].indexOf("outmax=") ==0) {
				outmax = qarr[i].toString().replaceFirst("outmax=", "");
			}else if (qarr[i].indexOf("sort=") ==0) {
				sort = qarr[i].toString().replaceFirst("sort=", "");
			}else if (qarr[i].indexOf("p=") ==0) {
				p = qarr[i].toString().replaceFirst("p=", "");
			}else if (qarr[i].indexOf("region1=") ==0) {
				region1 = qarr[i].toString().replaceFirst("region1=", "");
			}else if (qarr[i].indexOf("region2=") ==0) {
				region2 = qarr[i].toString().replaceFirst("region2=", "");
			}else if (qarr[i].indexOf("region3=") ==0) {
				region3 = qarr[i].toString().replaceFirst("region3=", "");
			}else if (qarr[i].indexOf("cat_gb=") ==0) {
				cat_gb = qarr[i].toString().replaceFirst("cat_gb=", "");
			}
		}
		// System.out.println("1::::::::::::::"+q);
		// System.out.println("2::::::::::::::"+w);
		// System.out.println("3::::::::::::::"+section);
		// System.out.println("4::::::::::::::"+pg);
		// System.out.println("5::::::::::::::"+outmax);
		// System.out.println("6::::::::::::::"+sort);
		// System.out.println("7::::::::::::::"+p);
		// System.out.println("8::::::::::::::"+d);
		// System.out.println("9::::::::::::::"+trunc);

		// 로그 기본 셋팅
		LogVO logvo = new LogVO();
		logvo.setREQ_TIME(logService.getNowDate(17)); // 사용자 요청 발생시간
		logvo.setSID(uniqueKey);
		logvo.setCTN(userSid); //
		logvo.setONEID_EMAIL(email);
		logvo.setSVC_TYPE("IPTV_PRE");
		if (w != null && w.equals("IPTV_PRE")) {
			logvo.setSVC_CMD("total");
		} else if (w != null && w.equals("IPTV_PRE_QUICK")) {
			logvo.setSVC_CMD("typing");
		} else {
			logvo.setSVC_CMD("");
		}
		if (section == null || section.equals("")) {
			logvo.setSECTION("N");
		} else {
			logvo.setSECTION(section);
		}

		Response result = null;
		// 파라미터 셋팅
		IptvPreVO vo = new IptvPreVO();
		vo.setReturnType(returnType);
		vo.setAuthKey(authKey);
		vo.setEncryptYn(encryptYn);
		vo.setUniqueKey(uniqueKey);
		vo.setUserSid(userSid);
		vo.setEmail(email);
		vo.setP(p);
		vo.setRegion1(region1);
		vo.setRegion2(region2);
		vo.setRegion3(region3);

		vo.setW(w);
		//vo.setQ(q);
		if (vo.getW() != null && vo.getW().equals("IPTV_PRE_QUICK")) {
			vo.setSection("");
		} else {
			vo.setSection(section);
		}
		vo.setPg(pg);
		if (outmax.equals("") && StringUtils.isNotEmpty(vo.getSection())) {
			outmax = "10";
		}
		vo.setOutmax(outmax);
		vo.setSort(sort);

		vo.setIptvPreSelect(iptvPreSelectArr.split(","));
		vo.setIptvPreWhere(iptvPreWhereArr.split(","));
		
		try {
			// 검색어 %포함시 에러수정  
			// URL디코딩 
			String replaceq = q.replace("%", "%25");
			q = URLDecoder.decode(q, "UTF-8");

			
			vo.setSectionList(sectionIptvPreList);
			vo.setSectionListArr(sectionIptvPreList.split(","));
			vo.setQ(q);
			url += w + "/" + q + "/section=" + section + "/pg=" + pg + "/outmax=" + vo.getOutmax() + "/sort="
					+ vo.getSort();
			logger.debug("[CALL URL] " + url);

			vo.setHost(searchHost);
			vo.setPort(searchPort);

			vo.setQuickList("");//20200117 section이 없음 수정해야함


			// 검색 요청 변수 유효성 검사 & 디코드
			vo = IptvPreUtilManager.checkPreIptvInfo(vo, "N", "iptvpre"); // N : 디코드 안함 , Y : 디코드 처리

			// logvo.setKEYWORD(vo.getQ());
			logvo.setKEYWORD(preiptvService.getByteLength(vo.getQ(), keyword_byte));
			if (vo.hasErrorResponse()) { // 에러 O
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(vo.getErrorCode());
				suberror.setMessage(vo.getErrorResponse());
				suberrorList.add(suberror);

				if (returnType.equals("application/json")) {
					OpenAPIErrorResponseJson response = new OpenAPIErrorResponseJson();
					Json_Error json_error = new Json_Error();
					json_error.setCode(vo.getErrorCode());
					json_error.setMessage(vo.getErrorResponse());
					json_error.setSuberror(suberrorList);
					response.setJson_Error(json_error);
					result = Response.ok(response, MediaType.APPLICATION_JSON).build();
				}

				logvo.setRESULT_CODE(vo.getErrorCode());
				logger.debug("[RESULT CODE] " + vo.getErrorCode());
			} // 에러 O ------ (END)
			else { // 에러 X
				if (returnType.equals("application/json")) { // retrun-type : json
					if (w != null && (w.equals("IPTV_PRE"))) {
						OpenAPIResponseJson iptvPreResponse = preiptvService.searchJson(vo, keyword_byte); // 검색 수행

						if (iptvPreResponse.getErrorResponse() != null) { // 검색 에러 O
							System.out.println("검색 에러 O");
							OpenAPIErrorResponseJson response = iptvPreResponse.getErrorResponse();

							// 검색성공했지만 건수가 0 일때
							if (response.getJson_Error().getCode().equals("20001000")) {
								result = Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);

								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);

								result = Response.ok(response, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] " + response.getJson_Error().getCode());
							}
						} else {
							result = Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}

					} else if (w != null && w.equals("IPTV_PRE_QUICK")) {
						OpenAPIQuickResponseJson iptvPreResponse = preiptvService.searchQuickJson(vo, keyword_byte);

						if (iptvPreResponse.getErrorResponse() != null) { // 검색 에러 일때
							OpenAPIErrorResponseJson response = iptvPreResponse.getErrorResponse();

							// 검색성공했지만 건수가 0 일때
							if (response.getJson_Error().getCode().equals("20001000")) {
								result = Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);

								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);

								result = Response.ok(response, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] " + response.getJson_Error().getCode());
							}
						} else {
							result = Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				} // retrun-type : json ------ (END)
			} // 에러 X ------ (END)

			return result;
		} catch (Exception e) {
			logger.debug("[EXCEPTION START]");
			// logger.error("EXCEPTION 위치 : " + request.getServletPath());
			logger.debug("EXCEPTION 내용 : \n" + e.toString());

			String code = "";
			if (w.equals("IPTV_PRE")) {
				code = "1000";
			} else {
				code = "2000";
			}
			if (returnType.equals("application/json")) {
				OpenAPIErrorResponseJson response = new OpenAPIErrorResponseJson();
				Json_Error json_error = new Json_Error();

				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(code);

				json_error.setCode(code);
				if (code.equals("1000")) {
					json_error.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					json_error.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
					logvo.setRESULT_CODE("40002000");
					logger.debug("[RESULT CODE] 40002000");
				}

				suberrorList.add(suberror);

				json_error.setSuberror(suberrorList);

				response.setJson_Error(json_error);
				result = Response.ok(response, MediaType.APPLICATION_JSON).build();

			}

			return result;
		} finally {
			// logger.debug("DEBUG 위치 : " + request.getServletPath());
			logvo = logService.setCommonLog(logvo);
			logvo.setRSP_TIME(logService.getNowDate(17)); // 사용자 요청 응답 발생시간
			logService.writeLog(logvo, was_num);
			logger.debug("======================== IPTVPRE SERVICE API(POST) END   ========================");
		}

	}
	*/	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path(searchPreIptvRequestURL)
	public Response iptvPreSearchPostProcess( 
			
			@DefaultValue("text/xml") @HeaderParam("return-type") String returnType,
			@HeaderParam("auth-key") String authKey,
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey,
			@HeaderParam("user-sid") String userSid,
			@HeaderParam("email") String email,
			@RequestParam String all,
			@DefaultValue("") @FormParam("w") String w,
			@DefaultValue("") @FormParam("q") String q,
			@DefaultValue("") @FormParam("section") String section,
			@DefaultValue("1") @FormParam("pg") String pg,
			@DefaultValue("10") @FormParam("outmax") String outmax,
			@DefaultValue("") @FormParam("sort") String sort,
			@DefaultValue("") @FormParam("p") String p,
			@DefaultValue("") @FormParam("region1") String region1,
			@DefaultValue("") @FormParam("region2") String region2,
			@DefaultValue("") @FormParam("region3") String region3,
			@DefaultValue("") @FormParam("cat_gb") String cat_gb,
			@DefaultValue("") @FormParam("kidsyn") String kidsyn,
			@DefaultValue("") @FormParam("option") String option,
			@DefaultValue("") @FormParam("uf") String uf
			
			
			) {
//		System.out.println("all::::::::::::::"+all);
		logger.debug("=========================== IPTVPRE SERVICE API(POST) START   ===========================");
		String url="/";
		String [] qarr = all.split("&");
		
		for ( int i = 0 ;i<qarr.length ; i++) {
			
			if (qarr[i].indexOf("q=") == 0 ) {
				q = qarr[i].toString().replaceFirst("q=", "");
				if (i <qarr.length-1) {
					if (qarr[i+1].toString().indexOf("w=")>-1 || qarr[i+1].toString().indexOf("section=")>-1|| qarr[i+1].toString().indexOf("pg=")>-1|| qarr[i+1].toString().indexOf("outmax=")>-1|| qarr[i+1].toString().indexOf("sort=")>-1|| qarr[i+1].toString().indexOf("p=")>-1|| qarr[i+1].toString().indexOf("region1=")>-1||qarr[i+1].toString().indexOf("region2=")>-1||qarr[i+1].toString().indexOf("region3=")>-1|| qarr[i+1].toString().indexOf("cat_gb=")>-1 || qarr[i+1].toString().indexOf("kidsyn=")>-1
							|| qarr[i+1].toString().indexOf("option=")>-1	|| qarr[i+1].toString().indexOf("uf=")>-1 ){
						
						//
					} else {// 'q' 값에 '&' 가 들어간경우 합침 
						q = q+"&"+qarr[i+1].toString();
					}
				}else if (i == qarr.length-1 && (all.substring(all.length()-1,all.length())).equals("&")){
					q = q+"&";
				}
					
			} else if (qarr[i].indexOf("w=") == 0) {
				w = qarr[i].toString().replaceFirst("w=", "");
			}else if (qarr[i].indexOf("section=") ==0) {
				section = qarr[i].toString().replaceFirst("section=", "");
			}else if (qarr[i].indexOf("pg=") ==0) {
				pg = qarr[i].toString().replaceFirst("pg=", "");
			}else if (qarr[i].indexOf("outmax=") ==0) {
				outmax = qarr[i].toString().replaceFirst("outmax=", "");
			}else if (qarr[i].indexOf("sort=") ==0) {
				sort = qarr[i].toString().replaceFirst("sort=", "");
			}else if (qarr[i].indexOf("p=") ==0) {
				p = qarr[i].toString().replaceFirst("p=", "");
			}else if (qarr[i].indexOf("region1=") ==0) {
				region1 = qarr[i].toString().replaceFirst("region1=", "");
			}else if (qarr[i].indexOf("region2=") ==0) {
				region2 = qarr[i].toString().replaceFirst("region2=", "");
			}else if (qarr[i].indexOf("region3=") ==0) {
				region3 = qarr[i].toString().replaceFirst("region3=", "");
			}else if (qarr[i].indexOf("cat_gb=") ==0) {
				cat_gb = qarr[i].toString().replaceFirst("cat_gb=", "");
			}else if (qarr[i].indexOf("kidsyn=") ==0) {
				kidsyn = qarr[i].toString().replaceFirst("kidsyn=", "");
			}else if (qarr[i].indexOf("option=") ==0) {
				option = qarr[i].toString().replaceFirst("option=", "");
			}else if(qarr[i].indexOf("uf=") ==0) {
				option = qarr[i].toString().replaceFirst("uf=", "");
			}
			
		}
		
		
		//	로그 기본 셋팅
		LogVO logvo = new LogVO();
		logvo.setSID(uniqueKey);
		logvo.setCTN(userSid); 	//
		logvo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간
		logvo.setONEID_EMAIL(email);
		logvo.setSVC_TYPE("IPTVPRE");
		if ( w != null && w.equals("IPTV_PRE") ) {
			logvo.setSVC_CMD("total");
		} else if ( w != null && w.equals("IPTV_PRE_QUICK") ) {
			logvo.setSVC_CMD("typing");
		} else {
			logvo.setSVC_CMD("");
		}
		
		if ( section ==null || section.equals("") ) {
			logvo.setSECTION("N");
		} else {
			logvo.setSECTION(section);
		}
		
		Response result = null;
		
		// request 파라미터 셋팅
		IptvPreVO vo = new IptvPreVO();
		vo.setReturnType(returnType);
		vo.setAuthKey(authKey);
		vo.setEncryptYn(encryptYn);
		vo.setUniqueKey(uniqueKey);
		vo.setUserSid(userSid);
		vo.setEmail(email);
		
		vo.setW(w);
		
			
		vo.setQ(q);
		vo.setSection(section);
		vo.setPg(pg);
		if ( outmax.equals("")) {
			outmax="10";
		}
		vo.setOutmax(outmax);
		vo.setSort(sort);
		vo.setP(p);
		vo.setRegion1(region1);
		vo.setRegion2(region2);
		vo.setRegion3(region3);
		vo.setCat_gb(cat_gb);
		vo.setOption(option);
		vo.setKidsyn(kidsyn);
		vo.setUf(uf);
		
		url += w+"/"+q+"/section="+section+"/pg="+pg+"/outmax="+vo.getOutmax()+"/sort="+sort+"/p="+p+"/region1="+region1+"/region2="+region2+"/region3="+region3+"/cat_gb="+cat_gb+"/kidsyn="+kidsyn+"/option="+option+"/uf="+uf;
		logger.debug("[CALL URL] " + url);
		
		vo.setHost(searchHost);
		vo.setPort(searchPort);
		
		String tempIptvPresectionList = iptvPresectionList;
		
		if("p".equalsIgnoreCase(p)) {
			tempIptvPresectionList = tempIptvPresectionList + ",IPTV_PLUS" ; 	
		}
		if("y".equalsIgnoreCase(uf)) {
			tempIptvPresectionList = tempIptvPresectionList + ",IPTV_UF" ;
		}
		if("p".equalsIgnoreCase(p) && ("y".equalsIgnoreCase(uf))) {
			tempIptvPresectionList = tempIptvPresectionList + ",IPTV_UFPLUS" ;
		}
		
		
		vo.setSectionList(tempIptvPresectionList);
		vo.setQuickList(iptvPrequickList);
		vo.setIptvPreChaList(iptvPreChaList);
		
		try {
			// 검색 요청 변수 유효성 검사 & 디코드
			vo = PreUtilManager.checkPreIptvInfo(vo, "N", "iptvpre"); // N : 디코드 안함 ,  Y : 디코드 처리
			logvo.setCTN(vo.getUserSid()); 	//
			logvo.setKEYWORD(iptvPreService.getByteLength(vo.getQ(),keyword_byte));
			
			if (vo.hasErrorResponse() ) {	// 에러 O
				
				// suberror 담기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(vo.getErrorCode());
				suberror.setMessage(vo.getErrorResponse());
				suberrorList.add(suberror);
				
				if(returnType.equals("application/json")) {
					OpenAPIErrorResponseJson response =  new OpenAPIErrorResponseJson();
					Json_Error json_error = new Json_Error();
					json_error.setCode(vo.getErrorCode());
					json_error.setMessage(vo.getErrorResponse());
					
					json_error.setSuberror(suberrorList);
					
					response.setJson_Error(json_error);
					
					result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
				}
				logger.debug("[RESULT CODE] " + vo.getErrorCode());
				logvo.setRESULT_CODE(vo.getErrorCode());
				
			} // 에러 O ------ (END)
			else {	// 에러 X
				if(returnType.equals("application/json")) {	// retrun-type : json
					if(w != null && w.equals("IPTV_PRE")) {
						com.diquest.openapi.iptvpre.OpenAPIVideoResponseJson iptvPreResponse =  iptvPreService.searchJson(vo,keyword_byte);		// 검색 수행
						
						if ( iptvPreResponse.getErrorResponse() != null ) {	// 검색 에러 O
							OpenAPIErrorResponseJson response =  iptvPreResponse.getErrorResponse();
							
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
								
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
						
					} else if(w != null && w.equals("IPTV_PRE_QUICK")) {
						com.diquest.openapi.iptvpre.OpenAPIVideoQuickResponseJson iptvPreResponse =  iptvPreService.searchQuickJson(vo,keyword_byte);
						
						if (iptvPreResponse.getErrorResponse() != null ) {	// 검색 에러 일때
							OpenAPIErrorResponseJson response =  iptvPreResponse.getErrorResponse();
							
							if (response.getJson_Error().getCode().equals("20001000") ) {
								result =  Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
								logvo.setRESULT_CODE("20001000");
								logger.debug("[RESULT CODE] 20001000");
							} else {
								// suberror 담기
								List suberrorList = new ArrayList<Suberror>();
								Suberror suberror = new Suberror();
								suberror.setName(vo.getW());
								suberror.setCode(response.getJson_Error().getCode());
								suberror.setMessage(response.getJson_Error().getMessage());
								suberrorList.add(suberror);
								
								Json_Error error = response.getJson_Error();
								error.setSuberror(suberrorList);
								response.setJson_Error(error);
								
								result =  Response.ok(response, MediaType.APPLICATION_JSON).build();
								
								logvo.setRESULT_CODE(response.getJson_Error().getCode());
								logger.debug("[RESULT CODE] "+response.getJson_Error().getCode());
							}
						} else {
							result =  Response.ok(iptvPreResponse, MediaType.APPLICATION_JSON).build();
							logvo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				}	//  retrun-type : json ------ (END)
			}	// 에러 X ------ (END)
			return result;
		} catch (Exception e) {
			logger.debug("[EXCEPTION START]");
//			logger.error("EXCEPTION 위치 : " + request.getServletPath());
			logger.debug("EXCEPTION 내용 : \n" + e.toString());
			
			String code="";
			if (w.equals("IPTV_PRE")) {
				code = "1000";
			} else {
				code = "2000";
			}
			if(returnType.equals("application/json")) {
				OpenAPIErrorResponseJson iptvPreresponse =  new OpenAPIErrorResponseJson();
				Json_Error json_error = new Json_Error();
				
				// suberror 만들기
				List suberrorList = new ArrayList<Suberror>();
				Suberror suberror = new Suberror();
				suberror.setName(vo.getW());
				suberror.setCode(code);
				
				json_error.setCode(code);
				if (code.equals("1000")){
					json_error.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
					logvo.setRESULT_CODE("40001000");
					logger.debug("[RESULT CODE] 40001000");
				} else {
					json_error.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					suberror.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
					logvo.setRESULT_CODE("40003000");
					logger.debug("[RESULT CODE] 40003000");
				}
				
				suberrorList.add(suberror);
				
				json_error.setSuberror(suberrorList);
				
				iptvPreresponse.setJson_Error(json_error);
				result =  Response.ok(iptvPreresponse, MediaType.APPLICATION_JSON).build();
			}
			return result;
		} finally {
//			logger.debug("DEBUG 위치 : " + request.getServletPath());
			
			logvo = logService.setCommonLog(logvo);
			logvo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
			logService.writeLog(logvo,was_num);
			
			logger.debug("=========================== IPTVPRE SERVICE API(POST) END   ===========================");
		}
		
	}
	


	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path(videoHangCheckRequestURL)
	public String naviHangCheckProcess(String input, @Context HttpServletRequest request, @Context UriInfo uriInfo) throws InterruptedException {
		input = input.trim();
		
		String result = aliveCheckResponseMessage;
//		Thread.sleep(10000000);
		/*if(ConstantManager.isPropertiesOK() == false) {
			return propertiesErrorResponseMessage;
		}
		else {
			LogManager.deleteBeforeLog();
			
			if(input.equalsIgnoreCase(aliveCheckMessage) == false) {
				return "";
			}
		}*/
		
		
		
		return result;
	}
	
	
	
	
	
	/**
	 * wastest
	 * @param input
	 * @param request
	 * @param uriInfo
	 * @return
	 */
	
	@GET
	@Path(searchTestRequestURL)
	public String getAsText() {
//		System.out.println("호출");
		try {
//			Thread.sleep(10000000);
//			System.out.println("정상호출끝");
			return "호출성공";
			// TODO Auto-generated catch block
		}finally {
			
		}
		
	}
	
	
	public String getStringError(OpenAPIErrorResponse result) throws Exception {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(OpenAPIErrorResponse.class);
	    Marshaller marshaller = jaxbContext.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() { 
	        public void escape(char[] ac, int i, int j, boolean flag,
	        Writer writer) throws IOException {
	        writer.write( ac, i, j ); }
	        });
	    StringWriter stringWriter = new StringWriter(); 
	    marshaller.marshal(result, stringWriter);
	    return stringWriter.toString();

}
	
	
	public String getStringJsonError(OpenAPIErrorResponseJson result) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();

		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(result);
		
	    return jsonInString;

	}
	
	
	
}