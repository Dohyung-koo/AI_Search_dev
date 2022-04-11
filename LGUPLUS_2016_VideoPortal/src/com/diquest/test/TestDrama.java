package com.diquest.test;

import java.util.ArrayList;
import java.util.List;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.exception.IRException;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.result.Result;

public class TestDrama {
	private static final String DRAMA_METHOD_CO = "CO";
	private static final String DRAMA_METHOD_MI = "MI";
	private static final String DRAMA_METHOD_XQ = "XQ";
	private static final int DFLT_RESULT_SIZE = 50;
	private String marinerIp;
	private int marinerPort;
	private String marinerCollection;
	private String dramaCollection;
	private String dramaIndex;
	private String dramaOption;
	
	public TestDrama(String marinerIp, int marinerPort, String marinerCollection){
		this(marinerIp, marinerPort, marinerCollection, null, null, null);
	}
	
	public TestDrama(String marinerIp, int marinerPort, String marinerCollection, String dramaCollection, String dramaIndex, String dramaOption){
		this.marinerIp = marinerIp;
		this.marinerPort = marinerPort;
		this.marinerCollection = marinerCollection;
		this.dramaCollection = dramaCollection;
		this.dramaIndex = dramaIndex;
		this.dramaOption = dramaOption;
	}
	
	public void setDrama(String dramaCollection, String dramaIndex, String dramaOption) {
		this.dramaCollection = dramaCollection;
		this.dramaIndex = dramaIndex;
		this.dramaOption = dramaOption;
	}
	
	public void setProps() {
		setProps(marinerIp, marinerPort, 5000, 20, 20);
	}
	
	public void setProps(String marinerIp, int marinerPort, int poolWaitTimeout, int minPoolSize, int maxPoolSize) {
		CommandSearchRequest.setProps(marinerIp, marinerPort, poolWaitTimeout, minPoolSize, maxPoolSize);
	}
	
	/**
	 * 해당검색어의 연관어를 검색후 List<String> 형태로 리턴한다.
	 * @param searchTerm	검색어
	 * @return List<String>	연관어 리스트
	 */
	public List<String> getDramaList(String searchTerm) {
		return getDramaList(searchTerm, false, false);
	}
	
	/**
	 * 해당검색어의 연관어를 검색후 List<String> 형태로 리턴한다.
	 * @param searchTerm	검색어
	 * @param useStopword	불용어, 불용관계 사용여부
	 * @return List<String>	연관어 리스트
	 */
	public List<String> getDramaList(String searchTerm, boolean useStopword) {
		return getDramaList(searchTerm, useStopword, false);
	}
	
	/**
	 * 해당검색어의 연관어를 검색후 List<String> 형태로 리턴한다.
	 * @param searchTerm	검색어
	 * @param useStopword	불용어, 불용관계 사용여부
	 * @param printQuery		Query 출력여부 설정
	 * @return List<String>	연관어 리스트
	 */
	public List<String> getDramaList(String searchTerm, boolean useStopword, boolean printQuery) {
		return getDramaList(searchTerm, DFLT_RESULT_SIZE, useStopword, printQuery);
	}
	
	public List<String> getDramaList(String searchTerm, int resultSize, boolean useStopword, boolean printQuery) {
		List<String> stringResultList = new ArrayList<String>();
		String dramaResult = "";
		
		Query query = getDramaQuery(resultSize, useStopword, printQuery);
		query.setValue("dramaKeyword", searchTerm);
		
		
		// QuerySet Setting
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		
		CommandSearchRequest command = new CommandSearchRequest(marinerIp, marinerPort);
		int rs = -1;
		try {
			rs = command.request(querySet);
			Result result = command.getResultSet().getResultList()[0];
			dramaResult = result.getValue("dramaResult");
		} catch (IRException e1) {
			return null;
		} catch (NullPointerException e2) {
			return null;
		}
		
		if(rs >= 0 && dramaResult != null) {
    		String[] toks = dramaResult.split("\t");
    		for (int i = 0; i < toks.length; i++) {
    			if(i%2 == 0){
    				continue;
    			}
    			stringResultList.add(toks[i]);
    		}
		}
		
		return stringResultList;
	}
	
	/**
	 * Drama에서 사용하는 쿼리를 만들어 준다.
	 * @return Query
	 */
	public Query getDramaQuery(boolean useStopword){
		return getDramaQuery(DFLT_RESULT_SIZE, useStopword, false);
	}
	
	public Query getDramaQuery(int resultSize, boolean useStopword){
		return getDramaQuery(resultSize, useStopword, false);
	}
	
	public Query getDramaQuery(int resultSize, boolean useStopword, boolean printQuery){
		Query query = new Query(); 
		query.setFrom(marinerCollection);
		query.setSearchOption((byte)Protocol.SearchOption.CACHE);
		query.setSearch(false);
		query.setDebug(true);
		
		query.setValue("dramaCollection", dramaCollection);
		query.setValue("dramaField", dramaIndex);
		query.setValue("dramaOption", dramaOption);
		query.setValue("dramaResultSize", String.valueOf(resultSize));
		
		if(printQuery) {
    		query.setPrintQuery(true);
    		query.setValue("printModifier", "true");
		}
		
//		query.setResultModifier("mixed");
//		query.setResultModifierList("video");
		
//		query.setValue("useRepreterms", "true");
//		query.setQueryModifier("nanet");
		
//		if(useStopword) {
//			query.setValue("useStopword", "true");
//    		query.setResultModifier("mixed");
//    		query.setResultModifierList("drama,nanet");
//		} else {
//			query.setResultModifier("drama");
//		}
		query.setResultModifier("drama");
		return query;
	}
	
	public static void main(String[] args) {
		String[] searchTerms = {"뉴욕영화제"};
		
		boolean useStopword = false;
		boolean printQuery = true;
		
//		TestDrama searchApi1 = new TestDrama("106.103.234.166", 5555, "VIDEO_KEYWORD");
		TestDrama searchApi1 = new TestDrama("58.72.188.195", 5555, "VIDEO_VOD");
		searchApi1.setDrama("VIDEO_KEYWORD", "IDX_KEYWORD_RELATION", TestDrama.DRAMA_METHOD_XQ);
		
		int resultSize = 50;
		for(int k = 0; k < searchTerms.length; k++) {
			List<String> list1 = searchApi1.getDramaList(searchTerms[k], resultSize, useStopword, printQuery);
			
			System.out.println("\t" + searchTerms[k]);
//			System.out.println("" + "\t " + TestDrama.DRAMA_METHOD_CO + "\t " + TestDrama.DRAMA_METHOD_MI + "\t " + TestDrama.DRAMA_METHOD_XQ);
//			System.out.println(list1);
			if(list1!= null){
				for (int i = 0; i < list1.size(); i++) {
					System.out.println((i+1) + "\t " + list1.get(i));
				}
			} else {
				System.out.println("result null");
			}
		}
		
	}
	
}
