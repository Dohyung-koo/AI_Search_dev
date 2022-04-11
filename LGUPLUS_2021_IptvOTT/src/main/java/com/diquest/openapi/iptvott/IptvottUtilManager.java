package com.diquest.openapi.iptvott;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.diquest.openapi.iptvott.request.IptvottQuickRequest;
import com.diquest.openapi.iptvott.request.IptvottRequest;
import com.diquest.openapi.util.Json_Error;
import com.diquest.openapi.util.OpenAPIErrorResponse;
import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.util.info.ERROR_TYPE;

public enum IptvottUtilManager {
	INSTANCE;
	
	private IptvottUtilManager() {
		
	}
	
	static String iptvottAuthKey = "iptvottsearch";
	
	
	/**
	 * 유효성 검사 & 디코드- 통합검색
	 * @param vo
	 * @return vo
	 */
	public static IptvottRequest checkiptvottInfo(IptvottRequest vo, String decodeYn) {
		String[] sectionArr = {"IPTV_VOD","IPTV_CHA","OT001","OT002","OT003","OT004","OT005","OT006","OT007","OT008","OT009","OT010","OT011","OT012","OT013"};
		String[] sortArr = {"1","2","3"};
		String[] ratingArr = {"01","02","03","04","05","06"};
		
		String authKey = vo.getAuthKey();
		String encryptYn = vo.getEncryptYn();
		String uniqueKey = vo.getUniqueKey(); //이건 물어봐야함
		
		//encrypt-yn
		if ( "".equals(encryptYn) || encryptYn == null ) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		} else {
			if ( !"Y".equals(encryptYn) && !"N".equals(encryptYn)) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
		}
		
		//auth-key
		if ( "".equals(authKey) || authKey == null ) {
			vo.setErrorCode("3000C001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000C001.getErrorMessage());
			return vo;
		} else {//유효인증키 검사
			if ( "Y".equals(encryptYn)) {
				try{
					authKey = lguplus.security.vulner.SecurityModule.AES256_Decrypt(authKey);
				}catch (Exception e){
					vo.setErrorCode("3000C004");
					vo.setErrorResponse(ERROR_TYPE.CODE_3000C004.getErrorMessage());
					return vo;
				}
			}
			
			if ( !authKey.equals(iptvottAuthKey) ) {
				vo.setErrorCode("3000C004");
				vo.setErrorResponse(ERROR_TYPE.CODE_3000C004.getErrorMessage());
				return vo;
			}
		}
		
		//unique-key
		if ( "".equals(uniqueKey) || uniqueKey == null ) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}
		
		// 파라미터 'w' 검사
		if (vo.getW().equals("")) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}else {
			if(!vo.getW().equals("IPTV_OTT_SEARCH")) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;	
			}
		}
		
		if(vo.getStructuredQuery() == null){
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}
		
		String query = vo.getStructuredQuery().getQuery();
		
		if (StringUtils.isEmpty(query)) { 
			//query없는경우
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}
		
		//section
		List sectionList = vo.getSection();
		if(sectionList != null) {
			for(int i = 0; i < sectionList.size(); i++) {
				if(!Arrays.asList(sectionArr).contains(String.valueOf(sectionList.get(i)))) {
					vo.setErrorCode("30000006");
					vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;	
				}
			}
		}
		
		//sort
		String sort = vo.getSort();
		if(!sort.equals("")) {
			if(!Arrays.asList(sortArr).contains(String.valueOf(sort))) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;	
			}
		}
		
		//rating
		String rating = vo.getRating();
		if(!rating.equals("")) {
			if(!Arrays.asList(ratingArr).contains(String.valueOf(rating))) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;	
			}
		}
		
		return vo;
	}
	
	/**
	 * 유효성 검사 & 디코드- 통합검색
	 * @param vo
	 * @return vo
	 */
	public static IptvottQuickRequest checkiptvottQuickInfo(IptvottQuickRequest vo, String decodeYn) {
		String authKey = vo.getAuthKey();
		String encryptYn = vo.getEncryptYn();
		String uniqueKey = vo.getUniqueKey(); //이건 물어봐야함
		
		//encrypt-yn
		if ( "".equals(encryptYn) || encryptYn == null ) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		} else {
			if ( !"Y".equals(encryptYn) && !"N".equals(encryptYn)) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
		}
		
		//auth-key
		if ( "".equals(authKey) || authKey == null ) {
			vo.setErrorCode("3000C001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000C001.getErrorMessage());
			return vo;
		} else {//유효인증키 검사
			if ( "Y".equals(encryptYn)) {
				try{
					authKey = lguplus.security.vulner.SecurityModule.AES256_Decrypt(authKey);
				}catch (Exception e){
					vo.setErrorCode("3000C004");
					vo.setErrorResponse(ERROR_TYPE.CODE_3000C004.getErrorMessage());
					return vo;
				}
			}
			
			if ( !authKey.equals(iptvottAuthKey) ) {
				vo.setErrorCode("3000C004");
				vo.setErrorResponse(ERROR_TYPE.CODE_3000C004.getErrorMessage());
				return vo;
			}
		}
		
		//unique-key
		if ( "".equals(uniqueKey) || uniqueKey == null ) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}
		
		// 파라미터 'w' 검사
		if (vo.getW().equals("")) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}else {
			if(!vo.getW().equals("IPTV_OTT_QUICK")) {
				vo.setErrorCode("30000006");
				vo.setErrorResponse(ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;	
			}
		}
		
		//파라미터 'q' 검사
		if ( vo.getQ().equals("")) {
			vo.setErrorCode("3000S001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}
		
		return vo;
	}
	
	/**
	 * 검색 에러 코드 XML response 만들기
	 * @param rs
	 * @return response
	 */
	public static OpenAPIErrorResponse makeRsInfo(int rs) {
		OpenAPIErrorResponse  response= new OpenAPIErrorResponse();
		int code = rs;
		
		if ( code == -1  ) {
			response.setCode("20001000");
			response.setMessage(ERROR_TYPE.CODE_20001000.getErrorMessage());
		} else if ( code <-1 && code > -129) {
			response.setCode("4004");
			response.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		} else if ( code == -60003 ) {
			response.setCode("L001");
			response.setMessage(ERROR_TYPE.CODE_4000L001.getErrorMessage());
		} else if ( code == -60004 ) {
			response.setCode("4001");
			response.setMessage(ERROR_TYPE.CODE_40004001.getErrorMessage());
		} else if ( code == -60005 ) {
			response.setCode("4002");
			response.setMessage(ERROR_TYPE.CODE_40004002.getErrorMessage());
		} else if ( code == -60006 ) {
			response.setCode("4003");
			response.setMessage(ERROR_TYPE.CODE_40004003.getErrorMessage());
		} else if ( code == -60011) {
			response.setCode("4004");
			response.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		} else {
			response.setCode("4004");
			response.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		}
		
		return response;
	}
	
	/**
	 * 검색 에러 코드 Json response 만들기
	 * @param rs
	 * @return response
	 */
	public static OpenAPIErrorResponseJson makeRsInfoJson(int rs) {
		OpenAPIErrorResponseJson  response= new OpenAPIErrorResponseJson();
		Json_Error json_error = new Json_Error();
		int code = rs;
		
		if ( code == -1  ) {
			json_error.setCode("20001000");
			json_error.setMessage(ERROR_TYPE.CODE_20001000.getErrorMessage());
		} else if ( code <-1 && code > -129) {
			json_error.setCode("4004");
			json_error.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		} else if ( code == -60003 ) {
			json_error.setCode("L001");
			json_error.setMessage(ERROR_TYPE.CODE_4000L001.getErrorMessage());
		} else if ( code == -60004 ) {
			json_error.setCode("4001");
			json_error.setMessage(ERROR_TYPE.CODE_40004001.getErrorMessage());
		} else if ( code == -60005 ) {
			json_error.setCode("4002");
			json_error.setMessage(ERROR_TYPE.CODE_40004002.getErrorMessage());
		} else if ( code == -60006 ) {
			json_error.setCode("4003");
			json_error.setMessage(ERROR_TYPE.CODE_40004003.getErrorMessage());
		} else if ( code == -60011) {
			json_error.setCode("4004");
			json_error.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		} else {
			json_error.setCode("4004");
			json_error.setMessage(ERROR_TYPE.CODE_40004004.getErrorMessage());
		}
		
		response.setJson_Error(json_error);
		
		return response;
	}
	
	/**
	 * 숫자 체크
	 * @param s
	 * @return boolean
	 */
	public static boolean isNumeric(String s) {
	  try {
	      Double.parseDouble(s);
	      return true;
	  } catch(NumberFormatException e) {
	      return false;
	  }
	}
	
	/**
	 * 통합검색 API 검색 실패 XML response 만들기
	 * @param code
	 * @return response
	 */
	public static OpenAPIErrorResponse makeError(String code) {
		
		OpenAPIErrorResponse  response= new OpenAPIErrorResponse();
		response.setCode(code);
		if (code.equals("1000"))
			response.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
		else if (code.equals("2000") )
			response.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
		else if ( code.equals("3000"))
			response.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
		else if ( code.equals("L001"))
			response.setMessage(ERROR_TYPE.CODE_4000L001.getErrorMessage());
		else if ( code.equals("L002"))
			response.setMessage(ERROR_TYPE.CODE_4000L002.getErrorMessage());
		return response;
	}
	
	/**
	 * 통합검색 API 검색 실패 Json response 만들기
	 * @return response
	 */
	public static OpenAPIErrorResponseJson makeErrorJson(String code) {
		
		OpenAPIErrorResponseJson  response= new OpenAPIErrorResponseJson();
		Json_Error json_error = new Json_Error();
		json_error.setCode(code);
		if (code.equals("1000"))
			json_error.setMessage(ERROR_TYPE.CODE_40001000.getErrorMessage());
		else if (code.equals("2000") )
			json_error.setMessage(ERROR_TYPE.CODE_40002000.getErrorMessage());
		else
			json_error.setMessage(ERROR_TYPE.CODE_40003000.getErrorMessage());
		
		response.setJson_Error(json_error);
		return response;
	}
	
}
