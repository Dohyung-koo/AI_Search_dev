package com.diquest.openapi.log;

public class LogVO {

	/**
	 * Request Headers 관련 변수
	 */
	private String SEQ_ID = "";			// 테이터 타입
	private String LOG_TIME="";
	private String LOG_TYPE="";
	private String SID="";
	private String RESULT_CODE="";
	private String REQ_TIME="";
	private String RSP_TIME="";
	private String REP_TIME="";
	private String CLIENT_IP="";
	private String DEV_INFO="";
	private String OS_INFO="";
	private String NW_INFO="";
	private String SVC_NAME="";
	private String DEV_MODEL="";
	private String CARRIER_TYPE="";

	private String SESSION_ID="";
	private String ONEID_EMAIL="";
	private String CTN="";
	private String APP_EXE_PATH="";
	
	
	private String SVC_TYPE="";
	private String SVC_CMD="";
	private String SECTION="";
	private String KEYWORD="";
	
	
	public String getSECTION() {
		return SECTION;
	}
	public void setSECTION(String sECTION) {
		SECTION = sECTION;
	}
	public String getKEYWORD() {
		return KEYWORD;
	}
	public void setKEYWORD(String kEYWORD) {
		KEYWORD = kEYWORD;
	}
	public String getSVC_CMD() {
		return SVC_CMD;
	}
	public void setSVC_CMD(String sVC_CMD) {
		SVC_CMD = sVC_CMD;
	}
	
	public String getSVC_TYPE() {
		return SVC_TYPE;
	}
	public void setSVC_TYPE(String sVC_TYPE) {
		SVC_TYPE = sVC_TYPE;
	}
	public String getSEQ_ID() {
		return SEQ_ID;
	}
	public void setSEQ_ID(String sEQ_ID) {
		SEQ_ID = sEQ_ID;
	}
	public String getLOG_TIME() {
		return LOG_TIME;
	}
	public void setLOG_TIME(String lOG_TIME) {
		LOG_TIME = lOG_TIME;
	}
	public String getLOG_TYPE() {
		return LOG_TYPE;
	}
	public void setLOG_TYPE(String lOG_TYPE) {
		LOG_TYPE = lOG_TYPE;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getRESULT_CODE() {
		return RESULT_CODE;
	}
	public void setRESULT_CODE(String rESULT_CODE) {
		RESULT_CODE = rESULT_CODE;
	}
	public String getREQ_TIME() {
		return REQ_TIME;
	}
	public void setREQ_TIME(String rEQ_TIME) {
		REQ_TIME = rEQ_TIME;
	}
	public String getRSP_TIME() {
		return RSP_TIME;
	}
	public void setRSP_TIME(String rSP_TIME) {
		RSP_TIME = rSP_TIME;
	}
	public String getREP_TIME() {
		return REP_TIME;
	}
	public void setREP_TIME(String rEP_TIME) {
		REP_TIME = rEP_TIME;
	}
	public String getCLIENT_IP() {
		return CLIENT_IP;
	}
	public void setCLIENT_IP(String cLIENT_IP) {
		CLIENT_IP = cLIENT_IP;
	}
	public String getDEV_INFO() {
		return DEV_INFO;
	}
	public void setDEV_INFO(String dEV_INFO) {
		DEV_INFO = dEV_INFO;
	}
	public String getOS_INFO() {
		return OS_INFO;
	}
	public void setOS_INFO(String oS_INFO) {
		OS_INFO = oS_INFO;
	}
	public String getNW_INFO() {
		return NW_INFO;
	}
	public void setNW_INFO(String nW_INFO) {
		NW_INFO = nW_INFO;
	}
	public String getSVC_NAME() {
		return SVC_NAME;
	}
	public void setSVC_NAME(String sVC_NAME) {
		SVC_NAME = sVC_NAME;
	}
	public String getDEV_MODEL() {
		return DEV_MODEL;
	}
	public void setDEV_MODEL(String dEV_MODEL) {
		DEV_MODEL = dEV_MODEL;
	}
	public String getCARRIER_TYPE() {
		return CARRIER_TYPE;
	}
	public void setCARRIER_TYPE(String cARRIER_TYPE) {
		CARRIER_TYPE = cARRIER_TYPE;
	}
	public String getSESSION_ID() {
		return SESSION_ID;
	}
	public void setSESSION_ID(String sESSION_ID) {
		SESSION_ID = sESSION_ID;
	}
	public String getONEID_EMAIL() {
		return ONEID_EMAIL;
	}
	public void setONEID_EMAIL(String oNEID_EMAIL) {
		ONEID_EMAIL = oNEID_EMAIL;
	}
	public String getCTN() {
		return CTN;
	}
	public void setCTN(String cTN) {
		CTN = cTN;
	}
	public String getAPP_EXE_PATH() {
		return APP_EXE_PATH;
	}
	public void setAPP_EXE_PATH(String aPP_EXE_PATH) {
		APP_EXE_PATH = aPP_EXE_PATH;
	}
	
}
