package com.diquest.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.diquest.app.common.Constant.DELIMS;
import com.diquest.util.FileUtil;

public class TestSplit {
	public void readAndTest(){
		BufferedReader br = FileUtil.getBufferedReader("d:/test.txt");
		String line="";
		StringBuffer sb =  new StringBuffer();
		try {
			while((line =br.readLine())!=null){
				line = normalizeFileLine(line);
//				line = "U^|PCAT^|NSC^|E2028^|선물하기_시리즈^|M011549440PPV00^|[테스트베드배포] NoSQL 확대적용 20회^|http:^|^|^|SER^|^|20160617150156^|20151130^|B^|N^|04^|3^|N^|^|N^|N^|N^|N^|N^|박선영,고은미,정찬^|이민수^|^|방송^|드라마^|MBC^|^|4^|^|5000^|방영일: 4월9일(목)^|박선영,고은미,정찬^|^|MBC^|^|N^|^|^|http://1.214.97.140:8888/poster/^|M011549440PPV00M2110.png^|M011549440PPV00M6130.png^|^|^|^|^|^|한정임, 도혜빈, 박현성^|^|^|^|^|^|N^|MBC, 폭풍의 여자, 드라마, 박선영, 고은미, 정찬, 선우재덕, 송이우, 박정수, 한정임, 도혜빈, 박현성, 도준태, 장미영^|[테스트베드배포] NoSQL 확대적용 20회^|^|^|^|^|40^|^|^|4^|^|N^|<<<EOL>>>";
				//마지막을 의미하는 문자가 있을경우
				if(line.contains(DELIMS.ENDLINE)){
					sb.append(line);
					line = sb.toString();
					
					// 마지막을 의미하는 문자 기준으로 앞부분(a) 만 처리후 뒷부분(b) 은 다음 문장에서 이용
					int idx = line.indexOf(DELIMS.ENDLINE);
					
					String a = line.substring(0, idx);
					String b = line.substring(idx + DELIMS.ENDLINE.length() , line.length());
					List<String> result = customSplit(a, DELIMS.WORD_DELIM);
					int splitSize = result.size();
					System.out.println(splitSize);
					sb = new StringBuffer();
					sb.append(b);
					
				//마지막을 의미하는 문자가 없을 경우
				} else {	
					sb.append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileUtil.closeBufferedReader(br);
	}
	
	private String normalizeFileLine(String s){
		return s.replaceAll("\\\\", "\\\\\\\\");
	}
	
	/**
	 * java 의 split 은 구분자와 공백이 여러개 있을때 한개로 쪼개기 때문에 새로 작성
	 * 
	 * java split          a,b,,,c    >>>> [a,b,c] 
	 * customSplit      a,b,,,c     >>>>[a,b,,,c]
	 * @param s
	 * @param delim
	 * @return
	 */
	private List<String> customSplit(String s, String delim){
		List<String> list = new ArrayList<String>();
		while(s.length() != 0){
			int idx = s.indexOf(delim);
			if(idx==-1){
				list.add(s);
				s = "";
			} else {
				String node = s.substring(0, idx);
				list.add(node);
				String tmp = s.substring(idx+delim.length(), s.length());
				s = tmp;
			}
		}
		return list;
	}
	public static void main(String[] args) {
		TestSplit app = new TestSplit();
		app.readAndTest();
		
	}
}
