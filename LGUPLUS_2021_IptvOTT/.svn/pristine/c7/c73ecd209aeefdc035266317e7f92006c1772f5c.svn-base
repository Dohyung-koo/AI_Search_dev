package com.diquest.openapi.util;

import com.diquest.openapi.util.info.ERROR_TYPE;

public enum UtilManager {
	INSTANCE;
	
	private UtilManager() {
		
	}
	
//	// 통합검색  상용
//	static String videoAuthKey = "vpsearch";
//	static String uflixAuthKey = "ufmsearch";
//	static String tvgAuthKey = "uftsearch";
//	
//	// 자동완성
//	static String videoQuickAuthKey = "vpauto";
////	static String videoQuickAuthKey = "vpsearch";
//	static String uflixQuickAuthKey = "ufmauto";
//	static String tvgQuickAuthKey = "uftauto";
//	
//	// 인기검색어
//	static String videoPopAuthKey = "vppopkeyword";
////	static String videoPopAuthKey = "vpsearch";
//	static String uflixPopAuthKey = "ufmpopkeyword";
//	static String tvgPopAuthKey = "uftpopkeyword";
	
	// 통합검색  개발,검수
//		static String videoAuthKey = "lguplusvpsearch";
//		static String uflixAuthKey = "lguplusufsearch";
//		static String tvgAuthKey = "lguplusufsearch";
//		
//		// 자동완성
//		static String videoQuickAuthKey = "lguplusvpsearch";
////		static String videoQuickAuthKey = "vpsearch";
//		static String uflixQuickAuthKey = "lguplusufsearch";
//		static String tvgQuickAuthKey = "lguplusufsearch";
//		
//		// 인기검색어
//		static String videoPopAuthKey = "lguplusvpsearch";
////		static String videoPopAuthKey = "vpsearch";
//		static String uflixPopAuthKey = "lguplusufsearch";
//		static String tvgPopAuthKey = "lguplusufsearch";
	
	
	
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
	 * @param rs
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
