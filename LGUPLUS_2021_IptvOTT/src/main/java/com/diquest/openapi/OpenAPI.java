package com.diquest.openapi;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.diquest.openapi.iptvott.IptvottService;
import com.diquest.openapi.iptvott.IptvottUtilManager;
import com.diquest.openapi.iptvott.request.IptvottQuickRequest;
import com.diquest.openapi.iptvott.request.IptvottRequest;
import com.diquest.openapi.iptvott.response.ErrorString;
import com.diquest.openapi.iptvott.response.FallbackExecution;
import com.diquest.openapi.iptvott.response.IptvottErrorResponse;
import com.diquest.openapi.iptvott.response.entity.IptvottQuickRespones;
import com.diquest.openapi.iptvott.response.entity.IptvottRespones;
import com.diquest.openapi.log.LogService;
import com.diquest.openapi.log.LogVO;
import com.diquest.openapi.util.OpenAPIErrorResponse;
import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.util.info.ERROR_TYPE;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

@Component
@Path("/")
public class OpenAPI {

	@Autowired
	IptvottService iptvottService;

	@Autowired
	LogService logService;

	@Value("${was_num}") private String was_num;
	@Value("${search.host}") private String searchHost;
	@Value("${search.port}") private int searchPort;
	@Value("${keyword_byte}") private int keyword_byte;
	
	public Logger logger = Logger.getLogger(this.getClass());
	
	private final String searchiptvottRequestURL = "/search/iptvott/total";
	private final String searchTestRequestURL = "/wastest";
	
	///////////////////////////////////////////// 관리도구 /////////////////////////////////////////////		
	
	@POST
	//@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(searchiptvottRequestURL)
	public Response iptvottPostProcess(
			@HeaderParam("auth-key") String authKey,
			@HeaderParam("encrypt-yn") String encryptYn,
			@HeaderParam("unique_key") String uniqueKey,
			@Context HttpHeaders headers,
			@RequestParam String all
			) {
//	    System.out.println("all::::::::::::::"+all);
		//logger.debug("=========================== API START   ===========================");
		System.out.println("headers : "+headers.getRequestHeaders().toString());
		System.out.println("json : "+all);
		all = all.replaceAll("[\\x00]","");
		
		String wCheck = "";
		
		if(all.indexOf("IPTV_OTT_SEARCH") > 0) {
			wCheck = "IPTV_OTT_SEARCH";
		}else if(all.indexOf("IPTV_OTT_QUICK") > 0) {
			wCheck = "IPTV_OTT_QUICK";
		}
		
		
		Response result = null;
		logger.debug("=========================== IPTVOTT SERVICE API(POST) START   ===========================");
		
		if(wCheck.equals("IPTV_OTT_QUICK")) {
			IptvottQuickRequest iptvottQuickRequest = null;
			
			iptvottQuickRequest = iptvottService.stringToJson(all,iptvottQuickRequest);

			//logger.debug("[CALL URL] " + iptvottService.getUrl(iptvottQuickRequest));

			LogVO logVo = new LogVO();

			logVo.setKEYWORD(iptvottQuickRequest.getQ());
			logVo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간

			logVo.setNW_INFO("");
			logVo.setCARRIER_TYPE("L");
			logVo.setLOG_TYPE("SVC");
			logVo.setSVC_TYPE("IPTVOTT");
			

			iptvottQuickRequest.setHost(searchHost);
			iptvottQuickRequest.setPort(searchPort);
			
			try {
				// 검색 요청 변수 유효성 검사 & 디코드
				iptvottQuickRequest.setAuthKey(authKey);
				iptvottQuickRequest.setUniqueKey(uniqueKey);
				iptvottQuickRequest.setEncryptYn(encryptYn);
				
				iptvottQuickRequest = IptvottUtilManager.checkiptvottQuickInfo(iptvottQuickRequest, "Y");	 // N : 디코드 안함 ,  Y : 디코드 처리

				if ( iptvottQuickRequest.hasErrorResponse() ) {	// 에러 O

					IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
					FallbackExecution fallbackExecution = new FallbackExecution();
					fallbackExecution.setErrorCode(iptvottQuickRequest.getErrorCode());
					fallbackExecution.setErrorMsg(iptvottQuickRequest.getErrorResponse());
					iptvottErrorResponse.setFallbackExecution(fallbackExecution);
					result = Response.ok(iptvottErrorResponse, MediaType.APPLICATION_JSON).build();

					String lgErrorCode =ErrorString.makeLgServerErrorCode("");
					logVo.setRESULT_CODE(lgErrorCode);

					logger.debug("[ERROR IPTVOTT CODE] "+iptvottQuickRequest.getErrorCode());
					logger.debug("[ERROR CODE] "+lgErrorCode);
					} // 에러 O ------ (END)
				else {	// 에러 X
					String w = iptvottQuickRequest.getW();
					if(w != null && w.equals("IPTV_OTT_QUICK")) {
						IptvottQuickRespones iptvottRespones = iptvottService.searchQuickJson(iptvottQuickRequest, keyword_byte);
						if ( iptvottRespones.getIptvottErrorResponse() != null ) {	// 검색 에러 O
							IptvottErrorResponse errorResponse =  iptvottRespones.getIptvottErrorResponse();
							result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE(iptvottRespones.getLgErrorCode());
							logger.debug("[IPTVOTT CODE] "+errorResponse.getFallbackExecution().getErrorCode());
							logger.debug("[RESULT CODE] "+logVo.getRESULT_CODE());
						} else {
							result =  Response.ok(iptvottRespones, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					}
				}	// 에러 X ------ (END)

				return result;
			} catch (Exception e) {
				logger.debug("[EXCEPTION START]");
//				logger.error("EXCEPTION 위치 : " + request.getServletPath());
				logger.debug("EXCEPTION 내용 : \n" + e.toString());

				IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
				FallbackExecution fallbackExecution = new FallbackExecution();
				fallbackExecution.setErrorCode("40002000");
				fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40002000.getErrorMessage());
				iptvottErrorResponse.setFallbackExecution(fallbackExecution);
				result = Response.ok(iptvottErrorResponse, MediaType.APPLICATION_JSON).build();
				logger.debug("[ERROR CODE] "+ERROR_TYPE.CODE_40002000.getErrorMessage());

				return result;
			} finally {

//				logger.debug("DEBUG 위치 : " + request.getServletPath());
				logVo = logService.setCommonLog(logVo);
				logVo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
				logVo.setLOG_TIME(logService.getNowDate(17));
//				System.out.println(logVo);
				logService.writeLog(logVo,was_num);
				logger.debug("=========================== IPTVOTT SERVICE API(POST) END   ===========================");
			}
		}else if(wCheck.equals("IPTV_OTT_SEARCH")){
			
			IptvottRequest iptvottRequest = null;
			iptvottRequest = iptvottService.stringToJson(all,iptvottRequest);

			logger.debug("[CALL URL] " + iptvottService.getUrl(iptvottRequest));

			LogVO logVo = new LogVO();

			logVo.setKEYWORD(iptvottRequest.getStructuredQuery().getQuery());
			logVo.setREQ_TIME(logService.getNowDate(17)); 	// 사용자 요청 발생시간

			logVo.setNW_INFO("");
			logVo.setCARRIER_TYPE("L");
			logVo.setLOG_TYPE("SVC");
			logVo.setSVC_TYPE("IPTVOTT");
			

			iptvottRequest.setHost(searchHost);
			iptvottRequest.setPort(searchPort);

			
			try {
				// 검색 요청 변수 유효성 검사 & 디코드
				iptvottRequest.setAuthKey(authKey);
				iptvottRequest.setUniqueKey(uniqueKey);
				iptvottRequest.setEncryptYn(encryptYn);
				
				iptvottRequest = IptvottUtilManager.checkiptvottInfo(iptvottRequest, "Y");	 // N : 디코드 안함 ,  Y : 디코드 처리

				if ( iptvottRequest.hasErrorResponse() ) {	// 에러 O

					IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
					FallbackExecution fallbackExecution = new FallbackExecution();
					fallbackExecution.setErrorCode(iptvottRequest.getErrorCode());
					fallbackExecution.setErrorMsg(iptvottRequest.getErrorResponse());
					iptvottErrorResponse.setFallbackExecution(fallbackExecution);
					result = Response.ok(iptvottErrorResponse, MediaType.APPLICATION_JSON).build();

					String lgErrorCode =ErrorString.makeLgServerErrorCode("");
					logVo.setRESULT_CODE(lgErrorCode);

					logger.debug("[ERROR IPTVOTT CODE] "+iptvottRequest.getErrorCode());
					logger.debug("[ERROR CODE] "+lgErrorCode);
					} // 에러 O ------ (END)
				else {	// 에러 X
					String w = iptvottRequest.getW();
					
					if(w != null && w.equals("IPTV_OTT_SEARCH")) {
						IptvottRespones iptvottRespones = iptvottService.searchJson(iptvottRequest, keyword_byte);
						if ( iptvottRespones.getIptvottErrorResponse() != null ) {	// 검색 에러 O
							IptvottErrorResponse errorResponse =  iptvottRespones.getIptvottErrorResponse();
							result =  Response.ok(errorResponse, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE(iptvottRespones.getLgErrorCode());
							logger.debug("[IPTVOTT CODE] "+errorResponse.getFallbackExecution().getErrorCode());
							logger.debug("[RESULT CODE] "+logVo.getRESULT_CODE());
						} else {
							result =  Response.ok(iptvottRespones, MediaType.APPLICATION_JSON).build();
							logVo.setRESULT_CODE("20000000");
							logger.debug("[RESULT CODE] 20000000");
						}
					} 
				}	// 에러 X ------ (END)

				return result;
			} catch (Exception e) {
				logger.debug("[EXCEPTION START]");
//				logger.error("EXCEPTION 위치 : " + request.getServletPath());
				logger.debug("EXCEPTION 내용 : \n" + e.toString());

				IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
				FallbackExecution fallbackExecution = new FallbackExecution();
				fallbackExecution.setErrorCode("40001000");
				fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40001000.getErrorMessage());
				iptvottErrorResponse.setFallbackExecution(fallbackExecution);
				result = Response.ok(iptvottErrorResponse, MediaType.APPLICATION_JSON).build();
				logger.debug("[ERROR CODE] "+ERROR_TYPE.CODE_40001000.getErrorMessage());

				return result;
			} finally {

//				logger.debug("DEBUG 위치 : " + request.getServletPath());
				logVo = logService.setCommonLog(logVo);
				logVo.setRSP_TIME(logService.getNowDate(17)); 	// 사용자 요청 응답 발생시간
				logVo.setLOG_TIME(logService.getNowDate(17));
//				System.out.println(logVo);
				logService.writeLog(logVo,was_num);
				logger.debug("=========================== IPTVOTT SERVICE API(POST) END   ===========================");
			}
		}else {
			IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
			FallbackExecution fallbackExecution = new FallbackExecution();
			fallbackExecution.setErrorCode("30000006");
			fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_30000006.getErrorMessage());
			iptvottErrorResponse.setFallbackExecution(fallbackExecution);
			result = Response.ok(iptvottErrorResponse, MediaType.APPLICATION_JSON).build();
			logger.debug("[ERROR CODE] "+ERROR_TYPE.CODE_30000006.getErrorMessage());

			return result;
		}
	}	
	
//	/**
//	 * wastest
//	 * @param input
//	 * @param request
//	 * @param uriInfo
//	 * @return
//	 */
//	
//	@GET
//	@Path(searchTestRequestURL)
//	public String getAsText() {
////		System.out.println("호출");
//		try {
////			Thread.sleep(10000000);
////			System.out.println("정상호출끝");
//			return "호출성공";
//			// TODO Auto-generated catch block
//		}finally {
//			
//		}
//		
//	}
	
	
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