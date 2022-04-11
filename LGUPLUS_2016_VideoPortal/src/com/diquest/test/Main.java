package com.diquest.test;

import java.util.ArrayList;
import java.util.List;

public class Main {
	private final static String[] ALL_COLUMN = new String[]{
			"RESULT_TYPE",
			"CAT_GB",
			"CAT_ID",
			"CAT_NAME",
			"ALBUM_ID",
			"ALBUM_NAME",
			"IMG_URL",
			"IMG_FILE_NAME",
			"DESCRIPTION",
			"TYPE",
			"FLASH_YN",
			"APPL_ID",
			"SERVICE_GB",
			"APPL_URL",
			"CREATE_DATE",
			"UPDATE_DATE",
			"IMG_TYPE",
			"PRICE",
			"PR_INFO",
			"RUNTIME",
			"IS_51_CH",
			"IS_HOT",
			"IS_CAPTION",
			"IS_HD",
			"CLOSE_YN",
			"3D_YN",
			"FILTER_GB",
			"ACTOR",
			"OVERSEER_NAME",
			"CONTS_SUBNAME",
			"GENRE1",
			"GENRE2",
			"GENRE3",
			"SERIES_NO",
			"PT",
			"DES",
			"PRICE_2",
			"BROAD_DATE",
			"STARRING_ACTOR",
			"VOICE_ACTOR",
			"BROADCASTER",
			"RELEASE_DATE",
			"IS_FH",
			"SER_CAT_ID",
			"MULTI_MAPPING_FLAG",
			"POSTER_FILE_URL",
			"POSTER_FILE_NAME_10",
			"POSTER_FILE_NAME_30",
			"KEYWORD_DESC",
			"TITLE_ENG",
			"DIRECTOR_ENG",
			"PLAYER_ENG",
			"CAST_NAME_ENG",
			"CAST_NAME",
			"TITLE_ORIGIN",
			"WRITER_ORIGIN",
			"PUBLIC_CNT",
			"POINT_WATCHA",
			"GENRE_UXTEN",
			"RETENTION_YN",
			"KEYWORD",
			"TITLE",
			"CAST_NAME_MAX",
			"CAST_NAME_MAX_ENG",
			"ACT_DISP_MAX",
			"ACT_DISP_MAX_ENG",
			"POINT_ORDER",
			"ALBUM_NO",
			"STILL_IMG_NAME",
			"CP_PROPERTY",
			"CP_PROPERTY_UFX",
			"THEME_YN",
			"SECTION",
/*			"KOFIC_SUPPORTING_ACTOR",
			"KOFIC_EXTRA_ACTOR",
			"KOFIC_SYNOPSIS",
			"KOFIC_KEYWORD",
			"KOFIC_FILM_STUDIO",
			"KOFIC_FILM_DISTRIBUTOR",
			"KOFIC_MAKE_NATION",
			"KOFIC_MAKE_YEAR",
			"KOFIC_SUM_SALE",
			"KOFIC_SUM_AUDIENCE",*/
			"CAT_NAME_CHOSUNG",
			"ALBUM_NAME_CHOSUNG",
			"ACTOR_CHOSUNG",
			"OVERSEER_NAME_CHOSUNG",
			"STARRING_ACTOR_CHOSUNG",
			"VOICE_ACTOR_CHOSUNG",
			"CAST_NAME_CHOSUNG",
			"KEYWORD_CHOSUNG",
			"TITLE_CHOSUNG",
			"CAST_NAME_MAX_CHOSUNG",
			"ACT_DISP_MAX_CHOSUNG",
			"BROAD_DATE_SORT",//Choihu 2018.06.12 �÷��߰�
			//���� �߰� �÷� 2018.06.26
			"CUESHEET_TYPE",
			"ACTOR_ID",
			"CONCERT_IMG_URL",
			"CONCERT_IMG_FILE_NAME",
			"CUESHEET_VIDEO_TYPE",
			"ACTOR_NAME",
			"ACTOR_NAME_CHOSUNG",
			//Choihu 20180808 VIDEO_TYPE �߰�
			"VIDEO_TYPE",
			"RUN_TIME",
			"MAIN_PROPERTY",
			"SUB_PROPERTY",
	};
	public static void main(String[] args) {
		String[] test = ALL_COLUMN;
		String q = "INSERT INTO SHOW_VOD (";
		for (String s : test) {
			q+= s;
			q+= ",";
		}
		q +=")";
		q = q.replace(",)",")");
		q += "VALUES " + 
				"(";
		
		String val ="PCAT^|NSC^|E20S7^|���� �׽�Ʈ^|M01188S004PPV00^|���� �׽�Ʈ-44ȸ^|http://123.141.15.150:88/poster/^|M01188S004PPV00M6110.png^|^|SER^|^|20180731165334^|20200602^|B^|N^|01^|1^|N^|^|N^|N^|N^|N^|N^|^|^|^|���^|��������^|SBS PLUS^|44ȸ^|^|���� �׽�Ʈ^|0^|202005201900^|^|^|SBS PLUS^|^|N^|^|0^|http://123.141.15.150:88/poster/^|M01188S004PPV00M6110.png^|M01188S004PPV00M6130.png^|^|^|^|^|^|^|^|^|^|^|TV�ٽú���^|Y^|DRAMATIC,1�� ����,You &amp; Me (feat. �ֳ��÷�),���̳�,Ruby Heart,Black Heart,Never Thought\r\n" + 
				"(I&apos;d Fall In Love),Complete(�� ���� ����),Down,������,SUNSET,SHE BAD,Beautiful Goodbye,LET ME,���Ҹ�,Summer Dream,�αٵα�(DKDK),��ȭ�� (Is it true),���� �̷� ����^|���� �׽�Ʈ-44ȸ^|^|^|^|^|0^|44ȸ^|^|18^|^|N^|202005201900^|B^|G66262|G66274|G66276|P31072|G66279|P31035|P31091|G66267|G66282|G66283|P31111|G66282|G66281|G66280|P31090|G66279|G66278|G66277|G66275^|^|^|B^|M^|000000^|^|^|^|^|^|^|^|LTE^|0^|^|^|";
		List<String> result = customSplit(val, "^|");
		//System.out.println("esese" + result.size());
		for(int i = 0; i < test.length; i++) {
			if(i < result.size()) {
				q+="'";
				if("SERVICE_GB".equals(test[i])
						||"PT".equals(test[i])){
					q+="";
				}else {
					q+=result.get(i);
					//q+="?";
				}

				q+="'";
			}else {
				q+="''";
			}
			
			if(test.length-1 != i) {
				q+=",";
			}
		}
		q += ")";
		System.out.println(q);
	}
	
	private static List<String> customSplit(String s, String delim) {
		// List<String> list = new ArrayList<String>();
		// while(s.length() != 0){
		// int idx = s.indexOf(delim);
		// String node = s.substring(0, idx);
		// list.add(node);
		// String tmp = s.substring(idx+delim.length(), s.length());
		// s = tmp;
		// }
		// return list;
		List<String> list = new ArrayList<String>();
		while (s.length() != 0) {
			int idx = s.indexOf(delim);
			if (idx == -1) {
				list.add(s);
				s = "";
			} else {
				String node = s.substring(0, idx);
				list.add(node);
				String tmp = s.substring(idx + delim.length(), s.length());
				s = tmp;
			}
		}
		return list;
	}
}
