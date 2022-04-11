package com.diquest.app.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.COLLECTION;
import com.diquest.app.common.Constant.DB_INFO;
import com.diquest.app.common.Constant.DB_NAME;
import com.diquest.app.common.Constant.LOG;
import com.diquest.db.IsLogDB;
import com.diquest.db.MarinerIndexLogDB;
import com.diquest.db.connector.DBConnector;
import com.diquest.db.entity.MarinerIndexLogVO;
import com.diquest.util.FileUtil;

public class LogMakerManager {

	Connection conn;
	DBConnector Connector;
	IsLogDB isLogDB;
	MarinerIndexLogDB marinerIndexLogDB;

	private String YYYYMMDD;
	private String YYYYMMDDHHMM;
	private String YYYYMMDDHHMMSS_before1Minute;
	private String YYYYMMDDHHMMSS;
	private String YYYYMMDDHHMMSSSSS;

	private String isLogDir;
	private String marinerLogDir;
	private String marinerAddLogDir;
	static String errorMsg;

	public LogMakerManager() {
		FileUtil.makeFolder(LOG.IS_LOG_DIR);
		FileUtil.makeFolder(LOG.MARINER_LOG_DIR);
		isLogDir = FileUtil.appendEndsPath(LOG.IS_LOG_DIR);
		marinerLogDir = FileUtil.appendEndsPath(LOG.MARINER_LOG_DIR);
		marinerAddLogDir = FileUtil.appendEndsPath(LOG.MARINERADD_LOG_DIR);

	}

	public void openDb() {
		System.out.println("OPEN DB [" + DB_INFO.MARINER_DB_URL + "]");
		Connector = new DBConnector(DB_INFO.MARINER_DB_URL, DB_INFO.MARINER_DB_USER, DB_INFO.MARINER_DB_PASSWORD, DB_INFO.MARINER_DB_DRIVER);
		conn = Connector.getConnection();
		isLogDB = new IsLogDB(DB_NAME.IS_LOG, false);
		marinerIndexLogDB = new MarinerIndexLogDB(DB_NAME.MARINER_INDEX_LOG, false);
	}

	public void closeDb() {
		System.out.println("CLOSE DB");
		Connector.closeConnection(conn);
	}

	public void getNowTime() {
		Date currentTime = new Date();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDD = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMM = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMMSS = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMMSSSSS = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.MINUTE, -1);
		mTime = mSimpleDateFormat.format(cal.getTime());
		YYYYMMDDHHMMSS_before1Minute = mTime;

	}

	public String exformat(String date) throws ParseException {
		SimpleDateFormat origin_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMddHHmmss");

		Date origin_data = origin_format.parse(date);
		String new_data = new_format.format(origin_data);

		return new_data;

	}

	/**
	 * 렌덤 8개숫자 생성
	 * 
	 * @return
	 */
	private String getRandomEgihtNumber() {
		double r = Math.random();
		double squre = 100000000;
		int aa = (int) (r * squre);
		String result = String.valueOf(aa);
		while (result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 렌덤 4개숫자 생성
	 * 
	 * @return
	 */
	private String getRandomFourNumber() {
		double r = Math.random();
		double squre = 10000;
		int aa = (int) (r * squre);
		String result = String.valueOf(aa);
		while (result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}
	
	/**
	 * 렌덤 3개숫자 생성
	 * 
	 * @return
	 */
	private String getRandomThreeNumber() {
		double r = Math.random();
		double squre = 1000;
		int aa = (int) (r * squre);
		String result = String.valueOf(aa);
		while (result.length() < 3) {
			result = "0" + result;
		}
		return result;
	}

	public boolean checkChange() {
		return true;
	}

	public static File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choise = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choise = file;
				lastMod = file.lastModified();
			}
		}
		return choise;
	}

	/**
	 * 수집엔진 로그파일 쓰기
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void writeIsLog() throws IOException, ParseException {
		String isWorkDir = isLogDir + YYYYMMDD;
		isWorkDir = FileUtil.appendEndsPath(isWorkDir);
		FileUtil.makeFolder(isWorkDir);

		BufferedWriter bw = null;
		StringBuilder logBuilder = new StringBuilder();
		setIsLogBuilder(logBuilder);

		// 5분으로 나누어진 경우
		if ((Integer.valueOf(YYYYMMDDHHMMSS.substring(10, 12))) % 5 == 0) {
			bw = FileUtil.getBufferedWriter(isWorkDir + Constant.LOG.IS_LOG_SER + YYYYMMDDHHMM + ".log");
			if (logBuilder.length() != 0) {
				bw.write(logBuilder.toString());
			}
		}
		// 그 외 시간인 경우
		else {
			if (logBuilder.length() != 0) {
				int lastMinute = Integer.valueOf(YYYYMMDDHHMMSS.substring(11, 12));
				// 1~4분인 경우
				if (lastMinute >= 1 && lastMinute < 5) {
					// 0분 파일에 기록
					bw = FileUtil.getBufferedWriter(isWorkDir + Constant.LOG.IS_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "0.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
				// 6~9분인 경우
				else {
					// 5분 파일에 기록
					bw = FileUtil.getBufferedWriter(isWorkDir + Constant.LOG.IS_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "5.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
			}
		}
		FileUtil.closeBufferedWriter(bw);
	}

	/**
	 * 검색엔진 로그파일 쓰기
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void writeMarinerLog() throws IOException, ParseException {
		String marinerWorkDir = marinerLogDir + YYYYMMDD;
		marinerWorkDir = FileUtil.appendEndsPath(marinerWorkDir);
		FileUtil.makeFolder(marinerWorkDir);

		BufferedWriter bw = null;
		StringBuilder logBuilder = new StringBuilder();
		setMarinerLogBuilder(logBuilder);
		// logBuilder.append("TEST");

		// 5분으로 나누어진 경우
		if ((Integer.valueOf(YYYYMMDDHHMMSS.substring(10, 12))) % 5 == 0) {
			bw = FileUtil.getBufferedWriter(marinerWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM + ".log");
			if (logBuilder.length() != 0) {
				bw.write(logBuilder.toString());
			}
		}
		// 그 외 시간인 경우
		else {
			if (logBuilder.length() != 0) {
				int lastMinute = Integer.valueOf(YYYYMMDDHHMMSS.substring(11, 12));
				// 1~4분인 경우
				if (lastMinute >= 1 && lastMinute < 5) {
					// 0분 파일에 기록
					bw = FileUtil.getBufferedWriter(marinerWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "0.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
				// 6~9분인 경우
				else {
					// 5분 파일에 기록
					bw = FileUtil.getBufferedWriter(marinerWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "5.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
			}
		}
		FileUtil.closeBufferedWriter(bw);
	}

	public void writeMarinerLogAdd() throws IOException, ParseException {
		String marinerAddWorkDir = marinerLogDir + YYYYMMDD;
		marinerAddWorkDir = FileUtil.appendEndsPath(marinerAddWorkDir);
		FileUtil.makeFolder(marinerAddWorkDir);
		BufferedWriter bw = null;
		StringBuilder logBuilder = new StringBuilder();
		setMarinerAddLogBuilder(logBuilder);

		// 5분으로 나누어진 경우
		if ((Integer.valueOf(YYYYMMDDHHMMSS.substring(10, 12))) % 5 == 0) {
			bw = FileUtil.getBufferedWriter(marinerAddWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM + ".log");
			if (logBuilder.length() != 0) {
				bw.write(logBuilder.toString());
			}
		}
		// 그 외 시간인 경우
		else {
			if (logBuilder.length() != 0) {
				int lastMinute = Integer.valueOf(YYYYMMDDHHMMSS.substring(11, 12));
				// 1~4분인 경우
				if (lastMinute >= 1 && lastMinute < 5) {
					// 0분 파일에 기록
					bw = FileUtil.getBufferedWriter(marinerAddWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "0.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
				// 6~9분인 경우
				else {
					// 5분 파일에 기록
					bw = FileUtil.getBufferedWriter(marinerAddWorkDir + Constant.LOG.MARINER_LOG_SER + YYYYMMDDHHMM.substring(0, 11) + "5.log");
					bw.write(logBuilder.toString());
					bw.newLine();
				}
			}
		}
		FileUtil.closeBufferedWriter(bw);
	}

	/**
	 * select된 Row 마다 수집엔진 로그 한줄을 기록한다
	 * 
	 * @param sb
	 * @throws ParseException
	 */
	private void setIsLogBuilder(StringBuilder sb) throws ParseException {
		List<String> catIdList = new ArrayList<String>();
		List<String> bbsIdList = new ArrayList<String>();

		for (String s : LOG.IS_KOFIC_BBS_ID) {
			catIdList.add(LOG.IS_KOFIC_CATEGORY_ID);
			bbsIdList.add(s);
		}
		for (String s : LOG.IS_NAVER_BBS_ID) {
			catIdList.add(LOG.IS_NAVER_CATEGORY_ID);
			bbsIdList.add(s);
		}

		for (int i = 0, iSize = bbsIdList.size(); i < iSize; i++) {

			List<String> logList = isLogDB.selectLogMsg(conn, catIdList.get(i), bbsIdList.get(i), YYYYMMDDHHMMSS_before1Minute, YYYYMMDDHHMMSS);
			// List<String> logList = isLogDB.selectLogMsg(conn,
			// catIdList.get(i), bbsIdList.get(i), "20160610200000",
			// "20160610200100");
			if (logList.size() == 0)
				continue;
			String sTimenew = null;
			String eTimenew = null;
			String errorMsg = null;
			boolean prehasPosition = false;
			boolean prehasMessage = false;
			for (String logs : logList) {

				String resultCode = "20000000";

				String errorCnt = "";
				String saveCnt = "";

				String[] logToks = logs.split("\n");

				boolean hasPosition = false;
				boolean hasMessage = false;

				for (String log : logToks) {
					if (log.contains("[POSITION]")) {
						hasPosition = true;
					}
					if (log.contains("MESSAGE")) {
						String labelMessage = "[MESSAGE]	:";
						log = log.substring(log.indexOf(labelMessage) + labelMessage.length(), log.length());
						errorMsg = log.trim();
						if (errorMsg != null && !errorMsg.equals("")) {
							hasMessage = true;
						}
					} else if (log.contains("저장 건수")) {
						log = log.substring(log.indexOf("DB= ") + 4, log.length());
						saveCnt = log.trim();
					} else if (log.contains("오류 건수")) {
						String labelErrCnt = "[오류 건수]	:";
						log = log.substring(log.indexOf(labelErrCnt) + labelErrCnt.length(), log.length());
						// log = log.substring(log.indexOf("[오류 건수] :"),
						// log.length());
						errorCnt = log.trim();
					}
				}

				if (hasPosition == true && hasMessage == true) {
					prehasPosition = hasPosition;
					prehasMessage = hasMessage;
					continue;
				}

				String[] logTime = logs.split("\n");

				for (String logtimes : logTime) {
					String Start = "";
					String End = "";
					if (logtimes.contains("소요 시간")) {
						logtimes = logtimes.substring(logtimes.indexOf("START=") + 10, logtimes.length());
						Start = logtimes.substring(logtimes.indexOf("START=") + 8, logtimes.indexOf(",")).trim();
						End = logtimes.substring(logtimes.indexOf("END=") + 34, logtimes.length()).trim();
						sTimenew = exformat(Start);
						eTimenew = exformat(End);
					}
				}

				if ((saveCnt.equals(errorCnt)) || (prehasPosition == true && prehasMessage == true)) {

					if (bbsIdList.get(i).equals("187")) {
						resultCode = Constant.LOG.IS_FAIL_KOFIC_INFO_RESULT_CODE;
					} else if (bbsIdList.get(i).equals("188")) {
						resultCode = Constant.LOG.IS_FAIL_KOFIC_SUM_RESULT_CODE;
					} else if (bbsIdList.get(i).equals("299")) {
						resultCode = Constant.LOG.IS_FAIL_NAVER_TV_RESULT_CODE;
					} else if (bbsIdList.get(i).equals("300")) {
						resultCode = Constant.LOG.IS_FAIL_NAVER_DRAMA_RESULT_CODE;
					} else if (bbsIdList.get(i).equals("301")) {
						resultCode = Constant.LOG.IS_FAIL_NAVER_MOVIE_RESULT_CODE;
					}
				}

				sb.append("SEQ_ID");
				sb.append("=");
				sb.append(YYYYMMDDHHMMSSSSS);
				sb.append(getRandomEgihtNumber());
				sb.append(LOG.delims);

				sb.append("LOG_TIME");
				sb.append("=");
				sb.append(YYYYMMDDHHMMSS);
				sb.append(LOG.delims);

				sb.append("LOG_TYPE");
				sb.append("=");
				sb.append("SVC");
				sb.append(LOG.delims);

				sb.append("SID");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("RESULT_CODE");
				sb.append("=");
				sb.append(resultCode);
				sb.append(LOG.delims);

				sb.append("REQ_TIME");
				sb.append("=");
				sb.append(sTimenew);
				sb.append(getRandomThreeNumber());
				sb.append(LOG.delims);

				sb.append("RSP_TIME");
				sb.append("=");
				sb.append(eTimenew);
				sb.append(getRandomThreeNumber());
				sb.append(LOG.delims);

				sb.append("CLIENT_IP");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("DEV_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("OS_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("NW_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SVC_NAME");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("DEV_MODEL");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("CARRIER_TYPE");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SESSION_ID");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("ONEID_EMAIL");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("CTN");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("APP_EXE_PATH");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SVC_TYPE");
				sb.append("=");
				if (LOG.IS_KOFIC_CATEGORY_ID.equals(catIdList.get(i))) {
					sb.append("MA");
				} else if (LOG.IS_NAVER_CATEGORY_ID.equals(catIdList.get(i))) {
					sb.append("NN");
				}
				sb.append(LOG.delims);

				sb.append("COLLECT_COUNT");
				sb.append("=");
				sb.append(saveCnt);
				sb.append(System.getProperty("line.separator"));

				prehasPosition = false;
				prehasMessage = false;
			}
		}
	}

	/**
	 * select된 Row 마다 검색엔진 로그 한줄을 기록한다
	 * 
	 * @param sb
	 * @throws ParseException
	 * @throws IOException
	 */
	private void setMarinerLogBuilder(StringBuilder sb) throws ParseException, IOException {

		List<MarinerIndexLogVO> list = marinerIndexLogDB.selectLogMsg(conn, YYYYMMDDHHMMSS_before1Minute, YYYYMMDDHHMMSS);

		for (MarinerIndexLogVO vo : list) {
			String CollectionStarted = vo.getCollectionStarted();
			String collection = vo.getCollectionId();
			// 아래 컬랙션 작업은 로그남기지 않음
			if (collection.equals(COLLECTION.LGUPLUS_DUMMY) || collection.equals(COLLECTION.DRAMA_DUMMY)) {
				continue;
			}

			String sTimenew = null;
			String eTimenew = null;
			
			String status = vo.getStatus();
			String serviceAble = vo.getServiceable();

			// 뒤에 .0으로 끝나는 초 제거
			String sTime = vo.getStarted();
//			sTime = sTime.substring(0, sTime.length() - 2);
			sTimenew = exformat(sTime);

			String eTime = vo.getEnded();
//			eTime = eTime.substring(0, eTime.length() - 2);
			eTimenew = exformat(eTime);

			sb.append("SEQ_ID");
			sb.append("=");
			sb.append(YYYYMMDDHHMMSSSSS);
			sb.append(getRandomEgihtNumber());
			sb.append(LOG.delims);

			sb.append("LOG_TIME");
			sb.append("=");
			sb.append(YYYYMMDDHHMMSS);
			sb.append(LOG.delims);

			sb.append("LOG_TYPE");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("SID");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("RESULT_CODE");
			sb.append("=");
			if (COLLECTION.VIDEO_VOD.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_CHANNEL.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_HIT_UCC.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_HIGHLIGHT.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_EVENT.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.UFLIX_VOD.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.I30.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.I30_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.I30_CHANNEL.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			}//Choihu 20180806 골프,공연 로그추가
			else if (COLLECTION.GOLF.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.GOLF_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.SHOW.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.SHOW_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			}	else if (COLLECTION.AR.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.AR_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VR.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VR_CONTENTS.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VR_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.GOLF2.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.GOLF2_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("20000000");
			} else if (COLLECTION.VIDEO_VOD.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002001");
			} else if (CollectionStarted.equals("1970-01-01 09:00:00.0")) {
				sb.append("40000001");
			} else if (COLLECTION.VIDEO_CHANNEL.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002002");
			} else if (COLLECTION.VIDEO_HIT_UCC.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002003");
			} else if (COLLECTION.VIDEO_HIGHLIGHT.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002004");
			} else if (COLLECTION.VIDEO_EVENT.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002005");
			} else if (COLLECTION.VIDEO_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002006");
			} else if (COLLECTION.UFLIX_VOD.equals(collection) && !status.equalsIgnoreCase("S")&& !status.equalsIgnoreCase("")) {
				sb.append("40002007");
			} else if (COLLECTION.UFLIX_MOBILE_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002008");
			} else if (COLLECTION.UFLIX_TVG_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002010");
			} else if (COLLECTION.UFLIX_THEME.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002011");
			} else if (COLLECTION.DRAMA.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002012");
			} else if(COLLECTION.I30.equals(collection) && !status.equalsIgnoreCase("S")){
				sb.append("40002013");
			} else if(COLLECTION.I30_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")){
				sb.append("40002014");
			} else if(COLLECTION.I30_CHANNEL.equals(collection) && !status.equalsIgnoreCase("S")){
				sb.append("40002015");
			}//Choihu 20180806 골프,공연 로그추가
			else if (COLLECTION.GOLF.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002016");
			} else if (COLLECTION.GOLF_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002017");
			} else if (COLLECTION.SHOW.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002018");
			} else if (COLLECTION.SHOW_AUTO_COMPLETE.equals(collection) && !status.equalsIgnoreCase("S")) {
				sb.append("40002019");
			} else if (COLLECTION.VR.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002020");
			} else if (COLLECTION.VR_CONTENTS.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002021");
			} else if (COLLECTION.VR_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002022");
			} else if (COLLECTION.AR.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002023");
			} else if (COLLECTION.AR_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002024");
			}else if (COLLECTION.GOLF2.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002025");
			} else if (COLLECTION.GOLF2_AUTO_COMPLETE.equals(collection) && status.equalsIgnoreCase("S")) {
				sb.append("40002026");
			}

			sb.append(LOG.delims);

			sb.append("REQ_TIME");
			sb.append("=");
			sb.append(sTimenew);
			sb.append(getRandomThreeNumber());
			sb.append(LOG.delims);

			sb.append("RSP_TIME");
			sb.append("=");
			sb.append(eTimenew);
			sb.append(getRandomThreeNumber());
			sb.append(LOG.delims);

			sb.append("CLIENT_IP");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("DEV_INFO");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("OS_INFO");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("NW_INFO");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("SVC_NAME");
			sb.append("=");
			sb.append("SEARCH");
			sb.append(LOG.delims);

			sb.append("DEV_MODEL");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("CARRIER_TYPE");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("SESSION_ID");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("ONEID_EMAIL");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("CTN");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("APP_EXE_PATH");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("INDEX_TYPE");
			sb.append("=");
			if (COLLECTION.VIDEO_VOD.equals(collection) || COLLECTION.VIDEO_CHANNEL.equals(collection) || COLLECTION.VIDEO_EVENT.equals(collection)
					|| COLLECTION.VIDEO_HIGHLIGHT.equals(collection) || COLLECTION.VIDEO_HIT_UCC.equals(collection)
					|| COLLECTION.VIDEO_CHANNEL.equals(collection) || COLLECTION.VIDEO_AUTO_COMPLETE.equals(collection)) {
				sb.append("VP");
			} else if (COLLECTION.UFLIX_VOD.equals(collection) || COLLECTION.UFLIX_MOBILE_AUTO_COMPLETE.equals(collection)
					|| COLLECTION.UFLIX_TVG_AUTO_COMPLETE.equals(collection)) {
				sb.append("UFM");
			} else if (COLLECTION.I30.equals(collection) || COLLECTION.I30_AUTO_COMPLETE.equals(collection)
					|| COLLECTION.I30_CHANNEL.equals(collection)) {
				sb.append("IPTV");
			}//Choihu 20180806 골프,공연 로그추가
			else if (COLLECTION.GOLF.equals(collection) || COLLECTION.GOLF_AUTO_COMPLETE.equals(collection)) {
				sb.append("GOLF");
			} else if (COLLECTION.SHOW.equals(collection) || COLLECTION.SHOW_AUTO_COMPLETE.equals(collection)) {
				sb.append("SHOW");
			} else if (COLLECTION.AR.equals(collection) || COLLECTION.AR_AUTO_COMPLETE.equals(collection)) {
				sb.append("AR");
			} else if (COLLECTION.VR.equals(collection) || COLLECTION.VR_CONTENTS.equals(collection) ||COLLECTION.VR_AUTO_COMPLETE.equals(collection)) {
				sb.append("VR");
			} else if (COLLECTION.GOLF2.equals(collection) || COLLECTION.GOLF2_AUTO_COMPLETE.equals(collection)) {
				sb.append("GOLF2");
			}
			sb.append(LOG.delims);

			sb.append("INDEX_NAME");
			sb.append("=");
			//Choihu 20180806 골프,공연 로그추가
			if (COLLECTION.VIDEO_VOD.equals(collection) || COLLECTION.VIDEO_CHANNEL.equals(collection) || COLLECTION.VIDEO_EVENT.equals(collection)
					|| COLLECTION.VIDEO_HIGHLIGHT.equals(collection) || COLLECTION.VIDEO_HIT_UCC.equals(collection)
					|| COLLECTION.VIDEO_CHANNEL.equals(collection) || COLLECTION.UFLIX_VOD.equals(collection) ||  COLLECTION.I30.equals(collection) 
					|| COLLECTION.I30_CHANNEL.equals(collection) || COLLECTION.GOLF.equals(collection)|| COLLECTION.SHOW.equals(collection)
					|| COLLECTION.VR.equals(collection) ||COLLECTION.AR.equals(collection) || COLLECTION.VR_CONTENTS.equals(collection)
					|| COLLECTION.GOLF2.equals(collection)) {
				sb.append("total");
			} else if (COLLECTION.VIDEO_AUTO_COMPLETE.equals(collection) || COLLECTION.UFLIX_MOBILE_AUTO_COMPLETE.equals(collection)
					|| COLLECTION.UFLIX_TVG_AUTO_COMPLETE.equals(collection) || COLLECTION.I30_AUTO_COMPLETE.equals(collection)
					|| COLLECTION.GOLF_AUTO_COMPLETE.equals(collection) || COLLECTION.SHOW_AUTO_COMPLETE.equals(collection)
					|| COLLECTION.AR_AUTO_COMPLETE.equals(collection) || COLLECTION.VR_AUTO_COMPLETE.equals(collection) || COLLECTION.GOLF2_AUTO_COMPLETE.equals(collection)) {
				sb.append("typing");
			}
			
			sb.append(LOG.delims);

			sb.append("INDEX_RESULT");
			sb.append("=");
			if (status.equalsIgnoreCase("S")) {
				sb.append("Y");
			} else {
				sb.append("N");
			}
			sb.append(LOG.delims);

			sb.append("INDEX_DESC");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("INDEX_SIZE");
			sb.append("=");
			sb.append(serviceAble);
			sb.append(LOG.delims);

			sb.append("INDEX_START_TIME");
			sb.append("=");
			sb.append(sTime);
			sb.append(LOG.delims);

			sb.append("INDEX_END_TIME");
			sb.append("=");
			sb.append(eTime);
			sb.append(LOG.delims);
			
			sb.append("SVC_TYPE");
			sb.append("=");
			sb.append(LOG.delims);

			sb.append("COLLECT_COUNT");
			sb.append("=");
			sb.append(System.getProperty("line.separator"));

			if (COLLECTION.UFLIX_VOD.equals(collection)) {
				String tmp = sb.toString();
				tmp = tmp.replaceFirst("INDEX_TYPE=UFM", "INDEX_TYPE=TVG");
				sb.append(tmp);
				
			} else if (COLLECTION.UFLIX_TVG_AUTO_COMPLETE.equals(collection)) {
				String tmp = sb.toString();
				sb.delete(0, tmp.length());
				tmp = tmp.replaceFirst("INDEX_TYPE=UFM", "INDEX_TYPE=TVG");
				sb.append(tmp);
			}
		}
	}

	/**
	 * select된 Row 마다 검색엔진 로그 한줄을 기록한다
	 * 
	 * @param sb
	 * @throws IOException
	 * @throws ParseException
	 */

	private void setMarinerAddLogBuilder(StringBuilder sb) throws IOException, ParseException {
		File file = lastFileModified(Constant.LOG.ADD_LOG_DIR);

		BufferedReader brnew = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()), "UTF-8"));

		if (brnew != null) {
			String line = null;
			String checkAddLog = brnew.readLine();
			String ErrorFile = brnew.readLine();
			String IMCSType = brnew.readLine();
			String MIMSType = brnew.readLine();
			String FileSize = brnew.readLine();
			String nowtime = brnew.readLine();
			String resultCode = "20000000";
			brnew.close();

			if (checkAddLog != null && checkAddLog.equals("AddLog=N")) {
				sb.append("SEQ_ID");
				sb.append("=");
				sb.append(YYYYMMDDHHMMSSSSS);
				sb.append(getRandomEgihtNumber());
				sb.append(LOG.delims);

				sb.append("LOG_TIME");
				sb.append("=");
				sb.append(YYYYMMDDHHMMSS);
				sb.append(LOG.delims);

				sb.append("LOG_TYPE");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SID");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("RESULT_CODE");
				sb.append("=");
				if (ErrorFile.equals("ERRORFILE=Y")) {
					sb.append("50000001");
				} else if (FileSize.equals("FILESIZE=0")) {
					sb.append("50000002");
				} else {
					sb.append(resultCode);
				}
				sb.append(LOG.delims);

				sb.append("REQ_TIME");
				sb.append("=");
				sb.append(nowtime);
				sb.append(LOG.delims);

				sb.append("RSP_TIME");
				sb.append("=");
				sb.append(nowtime);
				sb.append(LOG.delims);

				sb.append("CLIENT_IP");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("DEV_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("OS_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("NW_INFO");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SVC_NAME");
				sb.append("=");
				sb.append("SEARCH");
				sb.append(LOG.delims);

				sb.append("DEV_MODEL");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("CARRIER_TYPE");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SESSION_ID");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("ONEID_EMAIL");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("CTN");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("APP_EXE_PATH");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("INDEX_TYPE");
				sb.append("=");
				sb.append("FILE");
				sb.append(LOG.delims);

				sb.append("INDEX_NAME");
				sb.append("=");
				if ((IMCSType.equals("IMCS=Y")) && (MIMSType.equals("MIMS=Y"))) {
					sb.append("IMCS|MIMS");
				} else if (IMCSType.equals("IMCS=Y")) {
					sb.append("IMCS");
				} else if (MIMSType.equals("MIMS=Y")) {
					sb.append("MIMS");
				}
				sb.append(LOG.delims);

				sb.append("INDEX_RESULT");
				sb.append("=");
				sb.append(LOG.delims);
				
				sb.append("INDEX_DESC");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("INDEX_SIZE");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("INDEX_START_TIME");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("INDEX_END_TIME");
				sb.append("=");
				sb.append(LOG.delims);

				sb.append("SVC_TYPE");
				sb.append("=");
				if ((IMCSType.equals("IMCS=Y")) && (MIMSType.equals("MIMS=Y"))) {
					sb.append("IMCS|MIMS");
				} else if (IMCSType.equals("IMCS=Y")) {
					sb.append("IMCS");
				} else if (MIMSType.equals("MIMS=Y")) {
					sb.append("MIMS");
				}
				sb.append(LOG.delims);

				sb.append("COLLECT_COUNT");
				sb.append("=");
				if (IMCSType.equals("IMCS=N") || (MIMSType.equals("MIMS=N"))) {
					sb.append("0");
				}
				sb.append(System.getProperty("line.separator"));

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"));
				bw.write("AddLog=Y");
				bw.close();
			}
		}

	}
}
