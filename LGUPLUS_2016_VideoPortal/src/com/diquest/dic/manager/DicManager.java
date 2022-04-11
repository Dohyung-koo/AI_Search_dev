package com.diquest.dic.manager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diquest.dic.NormalizeDic;
import com.diquest.dqdic.transmitable.Transmitable;
import com.diquest.dqdic.util.CharVector;

/**
 * 이미 만들어진 사전을 사용하는 클래스
 * 
 * @author hoho
 *
 */
public class DicManager {
	private static DicManager instance = null;
	private static Object lock = 0;
	//
	
	private static final String DFLT_DCD_DIR = "./data/dic";
//	private static final String DFLT_DCD_DIR = "/dlvs-apps/mariner/data/dic";
	private static String dcdDir = DFLT_DCD_DIR;

	public synchronized static DicManager getInstance() {
		if(instance == null)
			init();
		return instance;
	}
	
	private static void init() {
		synchronized (lock) {
			if (instance == null) {
				instance = new DicManager();
			}
		}
	}

	private static Map<String, NormalizeDic> dicMap;
	private static List<String> classTypes;
	private static CharVector buffer = new CharVector();
	
	
	/**
	 *  초기화를 Normailize Dic 에 있는 사전 목록으로 해준다
	 *  
	 */
	
	private DicManager() {
		dicMap = new HashMap<String, NormalizeDic>(8);
		classTypes = new ArrayList<String>(8);
		dcdDir = DFLT_DCD_DIR;
		
//		classTypes.add("keyword");
		
		for(String s : NormalizeDic.repretermDic){
			if("".equals(s))
				continue;
			classTypes.add(s);
		}
		for(String s : NormalizeDic.simpleDic){
			if("".equals(s))
				continue;
			classTypes.add(s);
		}
//		classTypes.add("stopwords");
//		classTypes.add("stopwords");
//		classTypes.add("stopwords");
//		classTypes.add("stoprelations");
	}
	
	public void clear() {
		dicMap = null;
	}
	
	public void setDicDir(String dir) {
		synchronized (lock) {
			dcdDir = dir;
			dicMap = new HashMap<String, NormalizeDic>(8);
		}
		reloadAll();
	}
	
	public void changeDicDir(String dir) {
		synchronized (lock) {
			dcdDir = dir;
			dicMap = new HashMap<String, NormalizeDic>(8);
		}
	}
	
	public boolean exist(String type, String keyword) {
		if(isEmptyTrimmed(keyword)) {
			System.out.println("keyword is null or empty.");
			return false;
		}
		
		NormalizeDic dic = dicMap.get(type.toLowerCase());
		if(dic == null) {
			try {
				dic = loadDic(type, false);
				dicMap.put(type.toLowerCase(), dic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String trimmed = removeWhitepace(keyword);
		return dic.get(trimmed, buffer);
	}
	
	public String normalize(String type, String keyword) {
		if(isEmptyTrimmed(keyword)) {
			System.out.println("keyword is null or empty.");
			return null;
		}
		
		NormalizeDic dic = dicMap.get(type.toLowerCase());
		if(dic == null) {
			try {
				dic = loadDic(type, false);
				dicMap.put(type.toLowerCase(), dic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String trimmed = removeWhitepace(keyword);
		if(dic.get(trimmed, buffer)) {
			return buffer.toString();
		}
		return null;
	}
	
	
	public int getEntrySize(String type) {
		NormalizeDic dic = dicMap.get(type.toLowerCase());
		if(dic == null) {
			try {
				dic = loadDic(type, false);
				dicMap.put(type.toLowerCase(), dic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dic.getEntrySize();
	}

	private NormalizeDic loadDic(String type, boolean reload) throws FileNotFoundException, IOException {
		NormalizeDic dic = dicMap.get(type.toLowerCase());
		if(reload || dic == null) {
			String file = dcdDir + "/" + type.toLowerCase() + ".dcd";
			System.out.println("[DicManager] open dcd file : " + file);
			dic = (NormalizeDic)Transmitable.readFrom(new BufferedInputStream(new FileInputStream(file)));
		}
		return dic;
	}

	public boolean reloadDic(String type) {
		if(!validType(type)) {
			System.out.println("invalid type : " + type);
			return false;
		}
		boolean well = false;
		NormalizeDic dic;
		try {
			dic = loadDic(type, true);
			dicMap.put(type.toLowerCase(), dic);
			well = true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return well;
	}
	
	private static boolean validType(String type) {
		for(String s : classTypes) {
			if(s.equalsIgnoreCase(type))
				return true;
		}
		return false;
	}
	

	public void reloadAll() {
		for(String s : classTypes) {
			reloadDic(s);
		}
	}

	public static String removeWhitepace(String s) {
		char[] ch = s.toCharArray();
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < ch.length; i++) {
			if(!Character.isWhitespace(ch[i])){
				b.append(ch[i]);
			}
		}
		s = b.toString();
		return s;
	}
	
	public static final boolean isEmptyTrimmed(String s) {
		return (s == null || s.trim().length() == 0);
	}
	
	public static void main(String[] args) {
		DicManager manager = DicManager.getInstance();
		String s ="./data/dic";
//		String sub ="인민정부";
//		String obj ="공민";
		manager.setDicDir(s);
		
//		if(manager.exist("stopwords", "채권")){
//			System.out.println("불용어");
//		}
//		
//		if(manager.exist("keyword", "aaa")){
//			System.out.println("aaa");
//		}
		System.out.println(manager.normalize("keyword", "O"));
//		System.out.println(manager.normalize("repreterms", "갈등원인"));
//		System.out.println(manager.normalize("repreterms", "IPTV"));
//		System.out.println(manager.normalize("repreterms", "인터넷프로토콜텔레비전"));
	}
	
}
