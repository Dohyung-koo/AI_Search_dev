package com.diquest.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.diquest.ir.util.common.DateUtil;

public class TestString {
	
	public static void main(String[] args) {
		
		System.out.println(DateUtil.getMonth());
		System.out.println(DateUtil.getDay());
		System.out.println(DateUtil.getHour());
		System.out.println(DateUtil.getMin());
		System.out.println(DateUtil.getYear());
	
		
		//날짜 재조
//		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMddHHmmssSSS", Locale.KOREA );
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMddHHmmssSSS");
		Date currentTime = new Date ();
		String mTime = mSimpleDateFormat.format ( currentTime );
		System.out.println ( mTime );
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.MINUTE, -1);
		mTime = mSimpleDateFormat.format ( cal.getTime() );
		System.out.println(mTime);
//		
//		StringBuilder nowTimeBuilder = new StringBuilder();
////		+++++
//		nowTimeBuilder.append(DateUtil.getYear());
//		nowTimeBuilder.append(DateUtil.getMonth());
//		nowTimeBuilder.append(DateUtil.getDay());
//		nowTimeBuilder.append(DateUtil.getHour());
//		nowTimeBuilder.append(DateUtil.getMin());
//		System.out.println(nowTimeBuilder.toString());
		
		//난수
		
//		for (int i = 0; i < 100; i++) {
//			double r = Math.random();
//			double s =  100000000;
//			int aa = (int) (r*s);
//			String result = String.valueOf(aa);
//			while(result.length()<8){
//				result = "0" +result;
//			}
//			
//			System.out.println(result);
//		}
		
		
//		BufferedWriter bw = FileUtil.getBufferedWriter("d:/test.log");
//		FileUtil.closeBufferedWriter(bw);
//		
//		
//		String YYYYMMDDHHMMSS = "201606241620";
//		System.out.println(Integer.valueOf(YYYYMMDDHHMMSS.substring(10, 12)));
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("[수집종료]\n");
		sb.append("[POSITION]	: Category= 영화진흥원(11), Bbs= 영화인(186)\n");
		sb.append("[수집모드]	: 추가 수집\n");
		sb.append("[오류 건수]	: 0\n");
		sb.append("[저장 건수]	: File= 0, DB= 0\n");
		sb.append("[소요 시간]	: Start= 2016-06-10 20:00:00, End= 2016-06-10 20:00:25\n");
		
		String logs = sb.toString();
		String[] logToks = logs.split("\n");
		
		for(String log : logToks){
			if(log.contains("저장 건수")){
				log = log.substring(log.indexOf("DB= ") + 4, log.length());
				System.out.println(log);
			}
		}
		
		StringBuilder logBuilder = new StringBuilder();
		System.out.println(logBuilder.length());
		
		
		String tmp = "SEQ_ID=2016062817030068435168683|LOG_TIME=20160628170300|LOG_TYPE=|SID=|RESULT_CODE=20002007|REQ_TIME=|RSP_TIME=|CLIENT_IP=|DEV_INFO=|OS_INFO=|NW_INFO=|SVC_NAME=|DEV_MODEL=|CARRIER_TYPE=|SESSION_ID=|ONEID_EMAIL=|CTN=|APP_EXE_PATH=|INDEX_TYPE=UFM|INDEX_NAME=total|INDEX_RESULT=Y|INDEX_SIZE=20544|INDEX_START_TIME=2016-06-28 17:02:27|INDEX_END_TIME=2016-06-28 17:02:51";
		StringBuilder ssb = new StringBuilder();
		ssb.append(tmp);
//		System.out.println(tmp.replaceFirst("INDEX_TYPE=UFM", "INDEX_TYPE=TVG"));
		
		
		String tttmp = ssb.toString();
		ssb.delete(0, tmp.length());
		tttmp = tttmp.replaceFirst("INDEX_TYPE=UFM", "INDEX_TYPE=TVG");
		ssb.append(tttmp);
		System.out.println(ssb.toString());
	}

}
