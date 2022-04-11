package com.diquest.openapi.relword;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.Result;

@Service
public class RelService {
	public Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 검색 쿼리 생성 
	 * @param searchVO
	 * @return
	 */
	public Query makeQuery(String keyword, String gubun) throws Exception{
		
		SelectSet[] selectSet = makeSelectSet();
		WhereSet[] whereSet = makeWhereSet(keyword, gubun);
		
		OrderBySet[] orderbySet = new OrderBySet[1]; 
		orderbySet[0] = new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE);
		
		Query query = new Query("<strong>", "</strong>");
		query.setSearchOption(Protocol.SearchOption.CACHE);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setOrderby(orderbySet);
		query.setFrom("DRAMA");		
		query.setResult(0, 0);
		
		query.setDebug(true);
		query.setPrintQuery(false);
		query.setSearchKeyword(keyword);
		query.setLogKeyword(keyword.toCharArray());
		
		query.setLoggable(false);
		
//		QueryParser queryParser = new QueryParser();
//		System.out.println("### Query : " + queryParser.queryToString(query));
		
		return query;
	}
	
	/**
	 * 검색 쿼리 생성 
	 * @param searchVO
	 * @return
	 */
	public Query makeQuery(String keyword) throws Exception{
		
		SelectSet[] selectSet = makeSelectSet();
		WhereSet[] whereSet = makeWhereSet(keyword);
		
		OrderBySet[] orderbySet = new OrderBySet[1]; 
		orderbySet[0] = new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE);
		
		Query query = new Query("<strong>", "</strong>");
		query.setSearchOption(Protocol.SearchOption.CACHE);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setOrderby(orderbySet);
		
		query.setFrom("DRAMA");		
		query.setResult(0, 0);
		
		query.setDebug(true);
		query.setPrintQuery(false);
		query.setSearchKeyword(keyword);
		query.setLogKeyword(keyword.toCharArray());
		
		query.setLoggable(false);
		
//		QueryParser queryParser = new QueryParser();
//		System.out.println("### Query : " + queryParser.queryToString(query));
		
		return query;
	}
	
	
	/**
	 * SelectSet 구성 
	 * @param searchInfo
	 * @return
	 */
	private SelectSet[] makeSelectSet() throws Exception{
		
		SelectSet[] selectSet = null;
		ArrayList selectList = new ArrayList();
		
		selectList.add(new SelectSet("REL_TERM", Protocol.SelectSet.NONE));
			
		
		selectSet = new SelectSet[selectList.size()];
		for( int i=0 ; i<selectList.size() ; i++ ) {
			selectSet[i] = (SelectSet) selectList.get(i);
		}
		
		return selectSet;
	}
	
	
	/**
	 * WhereSet 구성
	 * @param searchVO
	 * @return
	 */
//	private WhereSet[] makeWhereSet(String keyword, String gubun) throws Exception{
		private WhereSet[] makeWhereSet(String keyword) throws Exception{
		
			WhereSet[] whereSet = null;
		ArrayList whereList = new ArrayList();
		
		whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN) );
		
		whereList.add( new WhereSet("IDX_TERM_B", Protocol.WhereSet.OP_HASALL, keyword));
		
		whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) );
		
		
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN) );
//			
//			whereList.add( new WhereSet("IDX_TERM_EX", Protocol.WhereSet.OP_HASALL, keyword));
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_OR) );
//			whereList.add( new WhereSet("IDX_TERM_B", Protocol.WhereSet.OP_HASALL, keyword));
//			
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) );
//			
//			
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_AND) );
//			
//			
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN) );
//			if ( gubun.equals("video") ) {
//				whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "VIDEO_KEYWORD"));
//			} else if ( gubun.equals("mobile") ) {
//				whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "UFLIX_MOBILE_KEYWORD"));
//			} else {
//				whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "UFLIX_TVG_KEYWORD"));
//			}
//			
//			whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) );
			
		
		whereSet = new WhereSet[whereList.size()];
		for( int i=0 ; i<whereList.size() ; i++ ) {
			whereSet[i] = (WhereSet)whereList.get(i);
		}

		return whereSet;
	}
		
		
		/**
		 * WhereSet 구성
		 * @param searchVO
		 * @return
		 */
		private WhereSet[] makeWhereSet(String keyword, String gubun) throws Exception{
			
				WhereSet[] whereSet = null;
			ArrayList whereList = new ArrayList();
			
			
			
			
				whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN) );
				
				whereList.add( new WhereSet("IDX_TERM_EX", Protocol.WhereSet.OP_HASALL, keyword));
				whereList.add( new WhereSet(Protocol.WhereSet.OP_OR) );
				whereList.add( new WhereSet("IDX_TERM_TEX", Protocol.WhereSet.OP_HASALL, keyword.replaceAll("\\s", "")));
				whereList.add( new WhereSet(Protocol.WhereSet.OP_OR) );
				whereList.add( new WhereSet("IDX_TERM_B", Protocol.WhereSet.OP_HASALL, keyword));
				
				whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) );
				
				
				whereList.add( new WhereSet(Protocol.WhereSet.OP_AND) );
				
				
				whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN) );
				if ( gubun.equals("video") ) {
					whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "VIDEO_KEYWORD"));
				} else if ( gubun.equals("mobile") ) {
					whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "UFLIX_MOBILE_KEYWORD"));
				} else {
					whereList.add( new WhereSet("IDX_COLLECTION", Protocol.WhereSet.OP_HASALL, "UFLIX_TVG_KEYWORD"));
				}
				
				whereList.add( new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) );
				
			
			whereSet = new WhereSet[whereList.size()];
			for( int i=0 ; i<whereList.size() ; i++ ) {
				whereSet[i] = (WhereSet)whereList.get(i);
			}

			return whereSet;
		}
	
	/**
	 * 검색 결과 XML DOC Element 담기
	 * @param searchVO,response,result,query
	 * @return OpenAPIVideoResponseresult
	 */
	public ArrayList makeRelWord(Result result, Query query) throws Exception{
		
		ArrayList relWordList = new ArrayList();
		
		for( int i=0; i<result.getRealSize(); i++ ) {
			for( int k=0 ; k<result.getNumField(); k++ ) {
				
				String selectFieldName = new String( (query.getSelectFields())[k].getField() );
				String selectFieldValue = new String( result.getResult(i, k) );
				
				if (selectFieldName.equals("REL_TERM") ) {
					relWordList.add( selectFieldValue );
				}
			}
		}
		
		ArrayList resultList = new ArrayList();
		if ( relWordList.size()>0) {
			String rel =(String)relWordList.get(0);
			String [] arr= rel.split(",");
			
			for (int i =0; i<arr.length;i++) {
				resultList.add(arr[i]);
			}
		}
		
		
		return resultList;
	}
}
