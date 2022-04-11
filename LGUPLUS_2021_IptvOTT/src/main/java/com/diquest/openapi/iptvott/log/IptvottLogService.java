package com.diquest.openapi.iptvott.log;

import com.diquest.openapi.log.LogManager;
import com.diquest.openapi.log.LogVO;
import com.diquest.openapi.util.info.STRING_TYPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class IptvottLogService {

	private static String googleUploadPath = STRING_TYPE.GOOGLE_LOG_PATH.getErrorMessage();
	
	public IptvottLogVO setCommonLog(IptvottLogVO returnVo) {
		
		LogVO logVo = new LogVO();
		
		String SEQ_ID = "";			// 테이터 타입
		String LOG_TIME="";
		String LOG_TYPE="";
		String SID="";
		String RESULT_CODE="";
		String REQ_TIME="";
		String RSP_TIME="";
		String REP_TIME="";
		String CLIENT_IP="";
		String DEV_INFO="";
		String OS_INFO="";
		String NW_INFO="";
		String SVC_NAME="";
		String DEV_MODEL="";
		String CARRIER_TYPE="";

		String SESSION_ID="";
		String ONEID_EMAIL="";
		String CTN="";
		String APP_EXE_PATH="";
		
		
		
		try {
			//SEQ_ID + 랜덤숫자4 + 랜덤숫자4
			Random random = new Random();
//			SEQ_ID = getNowDate(17)+random.nextInt(9999)+random.nextInt(9999);
			
			SEQ_ID = getNowDate(17);
			
			int firstRd = random.nextInt(9999);
			
			if (firstRd <10 ) {
				SEQ_ID = SEQ_ID+"000"+firstRd;
			}else if (firstRd <100) {
				SEQ_ID = SEQ_ID+"00"+firstRd;
			}else if (firstRd <1000) {
				SEQ_ID = SEQ_ID+"0"+firstRd;
			} else {
				SEQ_ID = SEQ_ID+firstRd;
			}
			
			firstRd = random.nextInt(9999);
			if (firstRd <10 ) {
				SEQ_ID = SEQ_ID+"000"+firstRd;
			}else if (firstRd <100) {
				SEQ_ID = SEQ_ID+"00"+firstRd;
			}else if (firstRd <1000) {
				SEQ_ID = SEQ_ID+"0"+firstRd;
			} else {
				SEQ_ID = SEQ_ID+firstRd;
			}
			
			//	CLIENT_IP
			HttpServletRequest req = getCurrentRequest();
	        CLIENT_IP = getClientIP(req);
			
			// LOG_TYPE (defult  : SVC)
			LOG_TYPE = "SVC";
			
			SVC_NAME="GOOGLE";
			//  String APP_EXE_PATH="";
			
			
			returnVo.setSEQ_ID(SEQ_ID);				//	YYYYMMDDHHmmSSsss+{순차 8자리}
			returnVo.setLOG_TYPE(LOG_TYPE);			//	사용자의 요청 발생 시간
			returnVo.setCLIENT_IP(CLIENT_IP);			//	접속 클라이언트 IP
			returnVo.setSVC_NAME(SVC_NAME);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnVo;
	}
	
	/**
	 * 로그 기록 처리
	 * @param vo
	 */

	public IptvottLogVO writeLog(IptvottLogVO vo, String was_num) {
		
		String logStr = "";
		
		String yyyyMMddHHmm = getNowDate(12);
    
        //String yyyyMMdd =  getNowDate(8);
//		String yyyyMMdd = yyyyMMddHHmm.substring(0, 8);
//		String yyyyStr = yyyyMMddHHmm.substring(0, 4);
//		String mmStr = yyyyMMddHHmm.substring(4, 6);
//		String ddStr = yyyyMMddHHmm.substring(6, 8);
//        long mm = Long.parseLong( yyyyMMddHHmm.substring(10) );
        
        String yyyyMMdd =  getNowDate(8);
        long mm = Long.parseLong( yyyyMMddHHmm.substring(10) );
		
//		// 폴더 체크
//		File path = new File(googleUploadPath+yyyyMMdd);

		// 폴더 체크
		String googleUploadPathDate = googleUploadPath+yyyyMMdd;
//		System.out.println("googleUploadPathDate : " + googleUploadPathDate);
		File googlePath = new File(googleUploadPathDate);
		if(!googlePath.exists()) {

			googlePath.mkdirs();

			try {
				Runtime.getRuntime().exec("chmod 755 "+googlePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

    	// 분이 0,1,2,3,4 분으로 끝날경우 0분 로그에 추가
    	if  ( mm%10 == 0 || mm%10 == 1 || mm%10 == 2 || mm%10 == 3 || mm%10 == 4 ) {
    		mm= mm- (mm%10);
    	} else {	//분이 5,6,7,8,9 분으로 끝날경우 5분 로그에 추가
    		mm= mm-(mm%5);
    	}
    	
    	String yyyyMMddHH = yyyyMMddHHmm.substring(0, 10);
    	if ( mm<10) {
    		yyyyMMddHHmm = yyyyMMddHH+"0"+mm;
    	} else {
    		yyyyMMddHHmm = yyyyMMddHH+mm;
    	}
		String googleFileFullName = googleUploadPathDate+was_num+"."+yyyyMMddHHmm+".log";
//		System.out.println("googleFileFullName :" + googleFileFullName);


		//String googleFileFullName = googleUploadPath+"GOOGLE_SEARCH"+"."+was_num+"."+yyyyMMddHHmm+".log";

		// 파일 체크
		try{
			File googleFilePath = new File(googleFileFullName);
			if(!googleFilePath.exists()) {	//	파일이 존재하지 않으면


				// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
				BufferedWriter fw = new BufferedWriter(new FileWriter(googleFileFullName, true));

				// 파일안에 문자열 쓰기
				logStr = makeLogStr(vo);  // 로그스트링 만들기
//				System.out.println("logStr" + logStr);
				vo.setLOG_TIME(getNowDate(14));
				fw.append(logStr);
				fw.newLine();
				fw.flush();

				// 객체 닫기
				fw.close();


			}else {

				BufferedWriter br = new BufferedWriter(new FileWriter(googleFileFullName, true));

				// 파일안에 문자열 쓰기
				String a ="";
				logStr = makeLogStr(vo);  // 로그스트링 만들기
//				System.out.println("logStr" + logStr);
				br.append(logStr);
				br.newLine();
				br.flush();

				// 객체 닫기
				br.close();
			}
			Runtime.getRuntime().exec("chmod 755 "+googleFilePath);


		}catch(Exception e){
			vo.setLOG_TIME(getNowDate(14));
			e.printStackTrace();
		}

		return vo;
	}
	
	/**
	 * 로그 스트링 만들기
	 * @param vo
	 * @return
	 */
	public String makeLogStr( IptvottLogVO vo ) {
		
		
		//	LOG_TIME 로그 wirte 일시
		vo.setLOG_TIME(getNowDate(14));
		String code = vo.getRESULT_CODE();
		if (code.equals("C001") || code.equals("C004") || code.equals("S001") || code.equals("0004") || code.equals("C005") || code.equals("0006"))
			code = "3000"+code;
		else if (code.equals("L001") || code.equals("L002") )
			code = "4000"+code;
		else if ( code.equals("1000") || code.equals("2000") || code.equals("3000"))
			code = "4000"+code;
		else if ( code.equals("4001") || code.equals("4002") || code.equals("4003")|| code.equals("4004") || code.equals("4001") || code.equals("5000") )
			code = "4000"+code;
		
		if ( vo.getONEID_EMAIL() == null) {
			vo.setONEID_EMAIL("");
		}
		
		if ( vo.getSID() == null) {
			vo.setSID("");
		}
		
		if ( vo.getCTN() == null) {
			vo.setCTN("");
		}

		String logStr = "SEQ_ID=" + vo.getSEQ_ID() + "|LOG_TIME=" + vo.getLOG_TIME() 
		+ "|LOG_TYPE="+vo.getLOG_TYPE() + "|SID="+ vo.getSID()  + "|RESULT_CODE="+ code
		+ "|REQ_TIME="+ vo.getREQ_TIME() + "|RSP_TIME="+ vo.getRSP_TIME() + "|CLIENT_IP="+ vo.getCLIENT_IP()
		+ "|DEV_INFO="+ vo.getDEV_INFO() + "|OS_INFO="+ vo.getOS_INFO() + "|NW_INFO="+ vo.getNW_INFO()
		+ "|SVC_NAME="+ vo.getSVC_NAME() + "|DEV_MODEL="+ vo.getDEV_MODEL() + "|CARRIER_TYPE="+ vo.getCARRIER_TYPE()
		+ "|SESSION_ID="+ vo.getSESSION_ID() + "|ONEID_EMAIL="+ vo.getONEID_EMAIL() + "|CTN="+ vo.getCTN()
		+ "|APP_EXE_PATH="+ vo.getAPP_EXE_PATH() + "|SVC_TYPE="+ vo.getSVC_TYPE() + "|SVC_CMD="+ vo.getSVC_CMD()
		+ "|SECTION="+ vo.getSECTION() + "|KEYWORD="+ vo.getKEYWORD();

		
		/*
		String logStr = "SEQ_ID=" + vo.getSEQ_ID() + "|LOG_TIME=" + vo.getLOG_TIME()
				+ "|LOG_TYPE="+vo.getLOG_TYPE() +"|SVC_NAME" + vo.getSVC_NAME() + "|SID="+ vo.getSID()  + "|SID_TYPE="
				+ vo.getSID_TYPE() +"|RESULT_CODE="+ code
				+ "|REQ_TIME="+ vo.getREQ_TIME() + "|RSP_TIME="+ vo.getRSP_TIME() + "|CLIENT_IP="+ vo.getCLIENT_IP()
				+ "|OS_INFO="+ vo.getOS_INFO() + "|NW_INFO="+ vo.getNW_INFO()//
				+ "|CARRIER_TYPE="+ vo.getCARRIER_TYPE() + "|SESSION_ID="+ vo.getSESSION_ID() + "|ONEID_EMAIL="+ vo.getONEID_EMAIL()
				+ "|CTN="+ vo.getCTN() + "|DEV_INFO="+ vo.getDEV_INFO() + "|DEV_MODEL="+ vo.getDEV_MODEL() + "|MAC_ADDR="  + vo.getMAC_ADDR()
				+ "|SERIAL_NO="+ vo.getSERIAL_NO() + "|APP_EXE_PATH="+ vo.getAPP_EXE_PATH() + "|BUY_AMONT=" + vo.getBUY_AMONT();
*/
		return logStr;
	}
	
	/**
	 * 리퀘스트 정보 가져오기
	 * @return
	 */
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = ( ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		
		HttpServletRequest  hsr = sra.getRequest();
		return hsr;
	}
	
	/**
	 * 아이피 정보 가져오기
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	 */
	public String getClientIP(HttpServletRequest request) throws UnknownHostException {

	     String ip = request.getHeader("X-FORWARDED-FOR"); 

	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("Proxy-Client-IP");
	     }

	     if (ip == null || ip.length() == 0) {
	         ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
	     }

	     if (ip == null || ip.length() == 0) {
	    	 
	         ip = request.getRemoteAddr() ;
	         if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
	             InetAddress inetAddress = InetAddress.getLocalHost();
	             String ipAddress = inetAddress.getHostAddress();
	             ip = ipAddress;
	         }
	     }

	     return ip;

	 }
	
	/**
	 * 클라이언트 정보 가져오기
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	 */
	public String getClientDevInfo(HttpServletRequest request) throws UnknownHostException {

	     String devInfo = request.getHeader("user-agent"); 
	     
	     return devInfo;

	 }
	
	/**
	 * 현재일시 가져오기 (자리수)
	 * @param num
	 * @return String
	 * @throws UnknownHostException 
	 */
	public String getNowDate(int num) {

		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String date = dayTime.format(new Date(time));
		if (17!=num){
			date = date.substring(0, num);
		}
		
	    return date;

	 }
	
	
}
