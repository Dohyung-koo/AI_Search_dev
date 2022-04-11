package com.diquest.openapi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.diquest.openapi.util.info.ERROR_TYPE;
import com.diquest.openapi.videopotal.VideoVO;

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
	 * 유효성 검사 & 디코드- 통합검색, 순간검색어, 자동완성
	 * @param vo
	 * @return vo
	 */
	public static VideoVO checkVideoInfo(VideoVO vo, String decodeYn, String gubun) {
		
		boolean video = "iptv".equals(gubun);	// true >> vidoe potal 
		
		String w = vo.getW();									// (필수) 수집 영역 
		String q = vo.getQ();									// (필수) 검색어
		String section = vo.getSection();				// 검색세부 영역
		String pg = vo.getPg();								// 페이지
		String outmax = vo.getOutmax();				// 페이지 출력수
		String sort = vo.getSort();							// (필수)
		String p = vo.getP();									// 분야제안
		String d = vo.getD();		
		String region1 = vo.getRegion1();									// 분야제안
		String region2 = vo.getRegion2();									// 분야제안
		String region3 = vo.getRegion3();									// 분야제안
		
		
		
		
		if ( "".equals(w) || w == null ) {
			vo.setErrorCode("S001");
			vo.setErrorResponse("w "+ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		} else {
			if ( video ) {
				if ( !w.equals("IPTV") &&  !w.equals("IPTV_QUICK") ) {
					vo.setErrorCode("0006");
					vo.setErrorResponse("w" + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
				
				
			} else {
				vo.setErrorCode("0006");
				vo.setErrorResponse("w " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
		}
		
		// 파라미터 'section'  검사
		if ( !"".equals(section) && section != null ) {
			
			if ( w.equals("IPTV")) {
				String sectionArr[] = {"LTE_CHA","LTE_REP","LTE_MOV","LTE_KIDS","LTE_ANI","LTE_LIFE","LTE_PLUS"};
				String sectionCheck = "N";
				for ( int s = 0; s<sectionArr.length; s++) {
					if ( sectionArr[s].equals(section) ) {
						sectionCheck = "Y";
					}
				}
				
				if  (sectionCheck.equals("N") ) {
					vo.setErrorCode("0006");
					vo.setErrorResponse("section " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
				
			} 
			
		}
		
		// 파라미터 'q'  검사
		if ( "".equals(q) || q == null ) {
			vo.setErrorCode("S001");
			vo.setErrorResponse("q " + ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		}  else {
			if ( decodeYn.equals("Y")) {
				try {
//					q=q.replaceAll("[+]", "%2B");
					q = URLDecoder.decode(q, "utf-8");
					
					vo.setQ(q);
				} catch (Exception e) {
					vo.setQ(q);
				}
			}
		}
		
		
		// 파라미터 'pg' 검사
		if ( !isNumeric(pg) ) {	//숫자형 검사
			vo.setErrorCode("0006");
			vo.setErrorResponse("pg " + ERROR_TYPE.CODE_30000006.getErrorMessage());
			return vo;
		} 	
		
		// 파라미터 'outmax' 검사
		if ( !isNumeric(outmax) ) {	//숫자형 검사
			vo.setErrorCode("0006");
			vo.setErrorResponse("outmax" + ERROR_TYPE.CODE_30000006.getErrorMessage());
			return vo;
		}
		
		// 파라미터 'sort' 검사 (유형:'msort=m:1:1','msort=d:1:1')
		if ( !"".equals(sort) && sort != null  ) {
			if ( decodeYn.equals("Y")) {
				try {
					sort = URLDecoder.decode(sort, "utf-8");
					vo.setSort(sort);
				} catch (Exception e) {
					vo.setSort(sort);
				}
			}
			
			
			if ( video ) {	//비디오 포털 일때 
				
				if ( sort.indexOf("fsort=") > -1  ) {	// 문자 규칙 검사
					
					String [] arr = sort.split(",");
					
					if ( arr.length == 1 ) {	//	 정렬 조건 하나 일때
						if ( 	sort.equals("fsort=")||sort.equals("fsort=10")|| sort.equals("fsort=11")||sort.equals("fsort=20")||sort.equals("fsort=21") ){
								
							} else {
								vo.setErrorCode("0006");
								vo.setErrorResponse("sort" + ERROR_TYPE.CODE_30000006.getErrorMessage());
								return vo;
							}
						
					} else {
						vo.setErrorCode("0006");
						vo.setErrorResponse("sort " + ERROR_TYPE.CODE_30000006.getErrorMessage());
						return vo;
					}
				} else {
					vo.setErrorCode("0006");
					vo.setErrorResponse("sort " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
				
			}	// end - 비디오 포털 일때 
			
		}
		
		
		// 파라미터 'p' 디코드 
		if ( !"".equals(p) && p != null  ) {
			if ( decodeYn.equals("Y")) {
				try {
					p = URLDecoder.decode(p, "utf-8");
				
				} catch (Exception e) {
					
				}
			} 
			
			p = p.replaceFirst("p1=", "");
			vo.setP(p);
			// 파라미터 'p' 유효성 검사
			
			if (p.equals("")) {
				vo.setP("01,02,03,04,05");
			}else if (p.equals("01")) {
				vo.setP("01");
			}else if (p.equals("02")) {
				vo.setP("01,02");
			}else if (p.equals("03")) {
				vo.setP("01,02,03");
			}else if (p.equals("04")) {
				vo.setP("01,02,03,04");
			}else if (p.equals("05")) {
				vo.setP("01,02,03,04,05");
			}else if (p.equals("06")) {
				vo.setP("01,02,03,04,05,06");
			}else if (p.equals("20")) {
				vo.setP("20");
			}else {
				vo.setErrorCode("0006");
				vo.setErrorResponse("p " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
			
		}
		
		if (section!=null && "LTE_PLUS".equals(section) ) {
			if ( !vo.getP().equals("01,02,03,04,05,06") ) {
				vo.setErrorCode("0006");
				vo.setErrorResponse("p " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
		}
		
		
		// 파라미터 'd' 디코드 
		if ( !"".equals(d) && d != null  ) {
			if ( decodeYn.equals("Y")) {
				try {
					d = URLDecoder.decode(d, "utf-8");
				
				} catch (Exception e) {
					
				}
			}
			d = d.replaceFirst("d2=", "");
			d = d.replaceFirst("d1=", "");
			d = d.replaceFirst("d=", "");
			
			String [] dArr = d.split("~");
			
			if ( dArr.length != 2) {
				vo.setErrorCode("0006");
				vo.setErrorResponse("d " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			} else {
				if ( dArr[0].length() !=10 || !isNumeric(dArr[0]) ) {
					vo.setErrorCode("0006");
					vo.setErrorResponse("d " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
				
				if ( dArr[1].length() !=10 ||!isNumeric(dArr[1]) ) {
					vo.setErrorCode("0006");
					vo.setErrorResponse("d " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
			}
			
			vo.setD(d);
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
