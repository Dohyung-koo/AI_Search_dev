package com.diquest.openapi.preiptv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.diquest.openapi.util.Json_Error;
import com.diquest.openapi.util.OpenAPIErrorResponse;
import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.util.info.ERROR_TYPE;

public enum IptvPreUtilManager {
	INSTANCE;
	
	private IptvPreUtilManager() {
		
	}
	public static void main(String[] args) {
		System.out.println("test");
		IptvPreVO vo = new IptvPreVO();
		vo.setAuthKey("iptvpresearch");
		vo.setReturnType("application/json");
		vo.setEncryptYn("N");
		vo.setUniqueKey("testUniqueKey");
		vo.setUserSid("testUserSid");
		vo.setEmail("");
		vo.setW("IPTV_PRE");
		vo.setQ("test");
		vo = checkPreIptvInfo(vo, "N", "iptvpre");
		System.out.println(vo.getErrorResponse());
	}
	// 통합검색  상용
	static String iptvpreAuthKey = "iptvpresearch";
	
	// 자동완성
	static String iptvpreQuickAuthKey = "iptvpreauto";


/*	// 통합검색  개발,검수
		static String videoAuthKey = "lguplusvpsearch";
		static String uflixAuthKey = "lguplusufsearch";
		static String tvgAuthKey = "lguplusufsearch";
		static String showAuthKey = "showsearch";
		
		// 자동완성
		static String videoQuickAuthKey = "lguplusvpsearch";
//		static String videoQuickAuthKey = "vpsearch";
		static String showQuickAuthKey = "showauto";
		static String uflixQuickAuthKey = "lguplusufsearch";
		static String tvgQuickAuthKey = "lguplusufsearch";
		
		// 인기검색어
		static String videoPopAuthKey = "lguplusvpsearch";
//		static String videoPopAuthKey = "vpsearch";
		static String uflixPopAuthKey = "lguplusufsearch";
		static String tvgPopAuthKey = "lguplusufsearch";
		static String showPopAuthKey = "showpopkey";	*/	
	

	
	/**
	 * 유효성 검사 & 디코드- 통합검색, 순간검색어, 자동완성
	 * @param vo
	 * @return vo
	 */
	public static IptvPreVO checkPreIptvInfo(IptvPreVO vo, String decodeYn, String gubun) {
		
		boolean iptvPre = "iptvpre".equals(gubun);
		
		String returnType = vo.getReturnType();
		String authKey = vo.getAuthKey();			// (필수)
		String encryptYn = vo.getEncryptYn();	// (필수)
		String uniqueKey = vo.getUniqueKey();	// (필수)
		String userSid = vo.getUserSid();
		String email = vo.getEmail();
		
		String w = vo.getW();									// (필수) 수집 영역 
		String q = vo.getQ();									// (필수) 검색어
		String section = vo.getSection();				// 검색세부 영역
		String pg = vo.getPg();								// 페이지
		String outmax = vo.getOutmax();				// 페이지 출력수
		String sort = vo.getSort();							// (필수)
		String d = vo.getD();									// (필수) 범위제한 검색

		
		// 헤더  'encryptYn'  검사
		if ( "".equals(encryptYn) || encryptYn == null ) {
			vo.setErrorCode("S001");
			vo.setErrorResponse("encryptYn "+ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		} else {
			if ( !"Y".equals(encryptYn) && !"N".equals(encryptYn)) {
				vo.setErrorCode("0006");
				vo.setErrorResponse("encryptYn " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			}
		}
		
		
		// 헤더 'authKey'  검사
		if ( "".equals(authKey) || authKey == null ) {
			vo.setErrorCode("C001");
			vo.setErrorResponse(ERROR_TYPE.CODE_3000C001.getErrorMessage());
			return vo;
		} else {	//유효인증키 검사
			
			if ( "Y".equals(encryptYn)) {
				try{
					authKey = lguplus.security.vulner.SecurityModule.AES256_Decrypt(authKey);
				}catch (Exception e){
					vo.setErrorCode("0006");
					vo.setErrorResponse("encryptYn " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
			}
			
			if ( iptvPre ) {//-choihu 키체크 SPO_BB_QUICK?부분 어케수정할지
				if(w.equals("IPTV_PRE")){
					if ( !authKey.equals(iptvpreAuthKey)  ) {
						vo.setErrorCode("C004");
						vo.setErrorResponse( ERROR_TYPE.CODE_3000C004.getErrorMessage());
						return vo;
					}
				}else if(w.equals("IPTV_PRE_QUICK")){
					if ( !authKey.equals(iptvpreQuickAuthKey)  ) {
						vo.setErrorCode("C004");
						vo.setErrorResponse( ERROR_TYPE.CODE_3000C004.getErrorMessage());
						return vo;
					}
				}
				
			} 
		}
		
		
		
		// 헤더  'uniqueKey'  검사
		if ( "".equals(uniqueKey) || uniqueKey == null ) {
			vo.setErrorCode("0004");
			vo.setErrorResponse(ERROR_TYPE.CODE_30000004.getErrorMessage());
			return vo;
		} else {	
			
		}
		

		
		
		
		// 헤더 'userSid'  검사  (userSid 값이 암호화해서 넘어오면 >>> 복호화 )
		if ( !"".equals(userSid) && userSid != null  ) {
			try{
				
				if ( iptvPre  ) {
//					if ( userSid.indexOf("==") > -1) {
					String keyDir = "/svc/diquest/lg_openapi_videoPortal/WEB-INF/SecurityKey.ser";
//					String keyDir = "D:/2016_project_lguplus/eGovFrameDev-3.2.0-64bit/workspace/LGUPLUS_2016_VideoOpenAPI/src/main/webapp/WEB-INF/SecurityKey.ser";
					
					// 파일 체크
		        	File filePath = new File(keyDir);
		        	if(filePath.exists()) {	//	파일이 존재
		        		// 파일 생성
		                                 
		                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
		                    BufferedReader fw = new BufferedReader(new FileReader(filePath));
		                     String secretKey = "";
		                     String sline="";
		                    // 파일안에 문자열 쓰기
		                    while((sline = fw.readLine()) != null ){

		                    	secretKey += sline;
		                    }
		                    
		                    // 객체 닫기
		                    fw.close(); 
		                    
		                    byte[] keyData = secretKey.getBytes();
		            	    String iv = secretKey.substring(0, 16);

		            	    SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		            	    
		            	    Cipher cipher;
		            	    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		            	    cipher.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes("UTF-8")));

		                    
		        		    byte[] decryptBytes = cipher.doFinal( com.sun.org.apache.xml.internal.security.utils.Base64.decode(userSid) );
		        		    
		        		    vo.setUserSid(new String(decryptBytes));
		        		
		        	}
					
//							userSid = lguplus.security.vulner.SecurityModule.AES256_Decrypt(userSid);
					
					
//					} 
					
					
				}else {
					 
					 vo.setUserSid(userSid);
				
				}
			
			}catch (Exception e){
				
			}
		}
		
		
		
		// 헤더 'email'  검사
		//System.out.println("w : "+w);
		if ( "".equals(w) || w == null ) {
			vo.setErrorCode("S001");
			vo.setErrorResponse("w "+ERROR_TYPE.CODE_3000S001.getErrorMessage());
			return vo;
		} else {
			if (iptvPre)  {
				if ( !w.equals("IPTV_PRE") &&  !w.equals("IPTV_PRE_QUICK") ) {//-choihu 공연추가
					vo.setErrorCode("0006");
					vo.setErrorResponse("w " + ERROR_TYPE.CODE_30000006.getErrorMessage());
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
			
			if (w.equals("IPTV_PRE")) {
				String sectionCheck = "N";
				
				String sectionArr[] = vo.getSectionList().split(",");
				for (int s = 0; s < sectionArr.length; s++) {
					if (sectionArr[s].equals(section)) {
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
					if ( "".equals(q.trim()) ) {
						vo.setErrorCode("S001");
						vo.setErrorResponse("q " + ERROR_TYPE.CODE_3000S001.getErrorMessage());
						return vo;
					}
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
			
			
			if (iptvPre){ //-choihu 공연
				if ( "11".equals(sort)||"21".equals(sort)) {	
					
				}else {
					vo.setErrorCode("0006");
					vo.setErrorResponse("sort" + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
			}
			else {
				vo.setErrorCode("0006");
				vo.setErrorResponse("sort " + ERROR_TYPE.CODE_30000006.getErrorMessage());
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
			
			String [] dArr = d.split("~");
			
			if ( dArr.length != 2) {
				vo.setErrorCode("0006");
				vo.setErrorResponse("d " + ERROR_TYPE.CODE_30000006.getErrorMessage());
				return vo;
			} else {
				if ( dArr[0].length() !=12 || !isNumeric(dArr[0]) ) {
					vo.setErrorCode("0006");
					vo.setErrorResponse("d " + ERROR_TYPE.CODE_30000006.getErrorMessage());
					return vo;
				}
				
				if ( dArr[1].length() !=12 ||!isNumeric(dArr[1]) ) {
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
