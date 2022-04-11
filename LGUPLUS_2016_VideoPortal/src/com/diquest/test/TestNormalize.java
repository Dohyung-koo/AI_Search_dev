package com.diquest.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.diquest.app.common.NormalizeManager;

public class TestNormalize {

	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<String>() {{
			this.add("1억 미생물의 전쟁");
			this.add("드래곤볼 2기");
			this.add("드래곤볼 23화");
			this.add("제 2 화 모두 나가자");
			this.add("넥센 vs 엘지 대만족");
			this.add("넥센 vs LG");
			this.add("대명사 3강 4주차 이다");
			this.add("대명사 24주차");
			this.add("대명사 6강");
			this.add("1232134기 테스트");
			this.add("1 5 2 3 5 1 2 4");
			this.add("왕좌의 게임 시즌 5");
			this.add("막이래쑈10");
			this.add("퍼슨 오브 인터레스트 시즌4 15회");
			this.add("착한 일 하기 외");			
			this.add("1341313543131351");
			this.add("1238313535464");
			this.add("1238313535464");
		}};
		
		
		for(String i : list) {
			String s = NormalizeManager.regexNomalized_Contents(i);		
		
			System.out.println(s);
		}
	}
}
