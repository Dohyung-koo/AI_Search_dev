package com.diquest.openapi.util.info;

public enum ERROR_TYPE {
	
	/**
	 * 인터페이스 에러 코드
	 */
	CODE_C001("인증키가 정의되지 않았습니다."),
	CODE_C002("클라이언트 정보가 존재하지 않습니다. 클라이언트 환경 파일이 존재하지 않음"),
	CODE_C003("클라이언트 정보 폴더가 존재하지 않음. 서버에 정의된 디렉토리 정보가 잘못 정의 되어 있음"),
	CODE_C004("요청 서비스 정보와 인증키가 일치하지 않습니다."),
	CODE_C005("사용중지된 인증키 정보입니다"),
	CODE_C006("클라이언트 TRAFFIC관리 디렉토리가 없습니다."),
	CODE_C007("클라이언트 최대 트래픽을 초과하였습니다."),
	CODE_C008("클라이언트 TRAFFIC관리 파일이 손상되었습니다."),
	CODE_C009("암호화되지 않은 인증키입니다."),
	
	CODE_U001("LGU+ 사용자 정보 불일치"),
	CODE_L001("검색 서버 연동 에러"),
	CODE_T001("TLO로그 초기화 에러"),
	CODE_T002("TLO로그 생성 에러"),
	CODE_E001("예외 에러 발생"),
	CODE_R001("클라이언트 예상 트래픽을 초과 하였습니다."),
	CODE_S001("필수 파라미터 누락"),
	CODE_S002("검색 시스템 장애가 발생하였습니다."),
	CODE_S003("질의어 제한길이(64byte)가 초과되었습니다. "),
	
	CODE_20000000("성공"),
	CODE_20001000("요청에 대한 정상 처리이지만, 반환할 값이 없는 경우 (검색 결과가 없음)"),
	CODE_3000C001("인증키(auth-key) 없음"),
	CODE_3000C004("유효하지 않은 auth-key (요청서비스와 인증키가 일치하지 않음)"),
	CODE_3000S001("필수 파라미터 누락 (가입번호 등)"),
	CODE_30000004("unique-key 누락됨"),
	CODE_3000C005("암호화 키 만료"),
	CODE_30000006("파라미터 규칙 오류"),
	CODE_40005000("property 파일 오류"),
	CODE_40004001("검색 서버의 응답시간 타임아웃"),
	CODE_40004002("검색 서버 전송 중 IOException 발생"),
	CODE_40004003("검색결과 전송 시 전달 오류"),
	CODE_40004004("검색 서버 Exception 발생으로 close 된 경우"),
	CODE_4000L001("검색 서버 연동 에러"),
	CODE_4000L002("데이터베이스 연동 오류"),
	CODE_40001000("통합검색 API 검색 실패"),
	CODE_40002000("자동완성 API 검색 실패"),
	CODE_40003000("인기검색어 API 검색 실패");
	
		private String error;
	
	private ERROR_TYPE(String error) {
		this.error = error;
	}
	
	public String getErrorMessage() {
		return this.error;
	}
}

