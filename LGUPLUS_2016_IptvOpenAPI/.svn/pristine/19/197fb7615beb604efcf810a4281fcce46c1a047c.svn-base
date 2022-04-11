package com.diquest.openapi.iptvpre;

public class IptvPreVO {

	private String gubun = "";					// video, ufilx 구분 : video,uflix
	/**
	 * Request Headers 관련 변수
	 */
	private String returnType = "";			// 테이터 타입
	private String authKey = "";					// 클라이언트 인증키 (필수)
	private String encryptYn = "";				// Y(필수)
	private String uniqueKey = "";				// 고객 특정 key(필수)
	private String userSid = "";					// 고객 SID 번호
	private String email = "";						// 고객 email 정보
	
	/**
	 * 페이징 관련 변수
	 */
	private int listPage;					// 현재 페이지 번호
	private int listCount;					// 페이지 검색개수
	private int listViewCount;				// 페이지 표시개수
	
	/**
	 * 검색엔진 관련 변수
	 */
	private String host = "";				// 검색엔진 아이피
	private int port = 0;					// 검색엔진 포트
	private String target = "";				// 검색대상
	private String query = "";				// 검색어
	private String collection = "";			// 컬렉션명

	private String w = "";					// 검색영역
	private String q = "";					// 사용자쿼리(query)
	private String sort = "";				// 정렬
	private String d = "";					// 범위제한 검색
	private String csq = "";				// 중복 색인 검색
	private String section = "";			// 검색 세부 영역표시
	private String pg = "1";				// 페이지 num (default 1)
	private String outmax = "";			// 페이지 슬로우수 값 (지정하지 않으면 각 section별로 default 값 적용
	private String p = "";					// 분야제한 검색
	private String trunc = "";				// 출력 유형
	
	private String region1="";
	private String region2="";
	private String region3="";
	private String cat_gb="";
	private String kidsyn="";
	private String option="";
	private String uf="";


	private String key;
	
	private String sectionList = "";		// 통합검색 섹션 목록
	private String[] sectionListArr;		// 검색 섹션 목록
	private String quickList = "";			// 순간검색 섹션 목록
	private String iptvPreChaList = "";
	

	private String errorResponse = "";
	private String errorCode = "";
	
	private String cloneSection;
	
	
	
	public String getCloneSection() {
		return cloneSection;
	}

	public void setCloneSection(String cloneSection) {
		this.cloneSection = cloneSection;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getEncryptYn() {
		return encryptYn;
	}

	public void setEncryptYn(String encryptYn) {
		this.encryptYn = encryptYn;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getUserSid() {
		return userSid;
	}

	public void setUserSid(String userSid) {
		this.userSid = userSid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean hasErrorResponse() {
		if("".equals(errorResponse)) {
			return false;
		}
		
		return true;
	}
	
	public String getErrorResponse() {
		return errorResponse;
	}



	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}



	public String getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}



	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public int getListPage() {
		return listPage;
	}
	public void setListPage(int listPage) {
		this.listPage = listPage;
	}
	public int getListCount() {
		return listCount;
	}
	public void setListCount(int listCount) {
		this.listCount = listCount;
	}
	public int getListViewCount() {
		return listViewCount;
	}
	public void setListViewCount(int listViewCount) {
		this.listViewCount = listViewCount;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getW() {
		return w;
	}
	public void setW(String w) {
		this.w = w;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getCsq() {
		return csq;
	}
	public void setCsq(String csq) {
		this.csq = csq;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getPg() {
		return pg;
	}
	public void setPg(String pg) {
		if(pg == null || pg.equals("")) {
			pg = "1";
		}
		this.pg = pg;
	}
	public String getOutmax() {
		return outmax;
	}
	public void setOutmax(String outmax) {
		if(outmax == null || outmax.equals("")) {
			outmax = "10";
		}
		this.outmax = outmax;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getSectionList() {
		return sectionList;
	}
	public void setSectionList(String sectionList) {
		this.sectionList = sectionList;
	}
	public String[] getSectionListArr() {
		return sectionListArr;
	}
	public void setSectionListArr(String[] sectionListArr) {
		this.sectionListArr = sectionListArr;
	}
	public String getQuickList() {
		return quickList;
	}
	public void setQuickList(String quickList) {
		this.quickList = quickList;
	}
	
	public String getTrunc() {
		return trunc;
	}
	public void setTrunc(String trunc) {
		this.trunc = trunc;
	}

	public String getRegion1() {
		return region1;
	}

	public void setRegion1(String region1) {
		this.region1 = region1;
	}

	public String getRegion2() {
		return region2;
	}

	public void setRegion2(String region2) {
		this.region2 = region2;
	}

	public String getRegion3() {
		return region3;
	}

	public void setRegion3(String region3) {
		this.region3 = region3;
	}

	public String getCat_gb() {
		return cat_gb;
	}

	public void setCat_gb(String cat_gb) {
		this.cat_gb = cat_gb;
	}
	
	public String getKidsyn() {
		return kidsyn;
	}

	public void setKidsyn(String kidsyn) {
		this.kidsyn = kidsyn;
	}
	
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIptvPreChaList() {
		return iptvPreChaList;
	}

	public void setIptvPreChaList(String iptvPreChaList) {
		this.iptvPreChaList = iptvPreChaList;
	}


}