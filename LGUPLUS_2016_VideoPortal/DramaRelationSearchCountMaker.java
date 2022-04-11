package com.diquest.video;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.diquest.video.info.DBManager;
import com.diquest.video.info.MarinerSearchManager;

public class DramaRelationSearchCountMaker {
	private static final String SELECT_DRAMA_TERM_RELATION = "SELECT COLLECTION_ID, TERM_ID, REL_TERM_ID, SCORE FROM IR01_DRAMA_TERM_RELATION";
	
	private static final String INIT_DRAMA_DATA = "TRUNCATE TABLE DRAMA_DATA";
	private static final String INSERT_DRAMA_DATA_PREFIX = "INSERT INTO DRAMA_DATA (COLLECTION, TERM, REL_TERM) VALUES ";
	private static final String INSERT_DRAMA_DATA_VALUE_FORMAT = "('%s', '%s', '%s')";
	
	private final Map<String, Map<String, Set<DramaRelationInfo>>> dramaRelationMap = new HashMap<String, Map<String, Set<DramaRelationInfo>>>();
	
	private class DramaRelationInfo {
		public String relTerm;
		public long searchCount;
		public double score;
		
		public DramaRelationInfo(String relTerm, long searchCount, double score) {
			this.relTerm = relTerm;
			this.searchCount = searchCount;
			this.score = score;
		}
	}
	
	private class DramaRelationInfoComaparator implements Comparator<DramaRelationInfo> {
		@Override
		public int compare(	DramaRelationInfo o1,
							DramaRelationInfo o2) {
			if(o1.searchCount != o2.searchCount) {
				if(o1.searchCount > o2.searchCount) {
					return -1;
				}
				else {
					return 1;
				}
			}
			else if(o1.score != o2.score) {
				if(o1.score > o2.score) {
					return -1;
				}
				else {
					return 1;
				}
			}
			
			return o2.relTerm.compareTo(o1.relTerm);
		}
		
	}
	
	public void make() {
		System.out.println("Drama Relation Data Make Started");
		makeDramaRelationData();
		System.out.println("Drama Relation Data Make Finished");
		System.out.println("Drama Relation Data Insert Started");
		insertDramaData();
		System.out.println("Drama Relation Data Insert Finished");
	}
		
	public void makeDramaRelationData() {
		try {
			Connection conn = DBManager.makeConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_DRAMA_TERM_RELATION);
			ResultSet rs = stmt.executeQuery();
			
			int count = 0;
			
			while(rs.next()) {
				String collection = rs.getString("COLLECTION_ID");
				String term = rs.getString("TERM_ID");
				if(term != null) {
					term = term.replaceAll("'", "\\\\\'");
				}
				String relTerm = rs.getString("REL_TERM_ID");
				if(relTerm != null) {
					if(relTerm.contains("'")) {
						relTerm = relTerm.replaceAll("'", "\\\\\'");
					}
				}
				double score = rs.getDouble("SCORE");				
				int searchCount = MarinerSearchManager.getSearchCount(relTerm);
				
				if(dramaRelationMap.containsKey(collection) == false) {
					dramaRelationMap.put(collection, new HashMap<String, Set<DramaRelationInfo>>());
				}
				
				if(dramaRelationMap.get(collection).containsKey(term) == false) {
					dramaRelationMap.get(collection).put(term, new TreeSet<DramaRelationInfo>(new DramaRelationInfoComaparator()));
				}
				
				dramaRelationMap.get(collection).get(term).add(new DramaRelationInfo(relTerm, searchCount, score));
				
				++count;
				
				if((count % 1000) == 0) {
					System.out.print(".");
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertDramaData() {
		try {
			Connection conn = DBManager.makeConnection();
			PreparedStatement stmt = conn.prepareStatement(INIT_DRAMA_DATA);
			stmt.execute();
			stmt.close();
			conn.close();
			
			StringBuilder value = new StringBuilder();
			
			int count = 0;
			
			for(Entry<String, Map<String, Set<DramaRelationInfo>>> entry : dramaRelationMap.entrySet()) {
				String collection = entry.getKey();
				
				for(Entry<String, Set<DramaRelationInfo>> dramaInfoEntry : entry.getValue().entrySet()) {
					String term = dramaInfoEntry.getKey();
					
					StringBuilder relTerms = new StringBuilder();
					
					for(DramaRelationInfo info : dramaInfoEntry.getValue()) {
						if(relTerms.length() > 0) {
							relTerms.append(",");
						}
						
						relTerms.append(info.relTerm);
					}
					
					if(value.length() > 0) {
						value.append(", ");
					}
					else {
						value.append(INSERT_DRAMA_DATA_PREFIX);
					}
					
					value.append(String.format(INSERT_DRAMA_DATA_VALUE_FORMAT, collection, term, relTerms.toString()));
					
					++count;
					
					if((count % 10) == 0) {
						conn = DBManager.makeConnection();
						stmt = conn.prepareStatement(value.toString());
						stmt.execute();
						stmt.close();
						conn.close();
						
						value.setLength(0);
					}
				}
			}
			
			conn = DBManager.makeConnection();
			stmt = conn.prepareStatement(value.toString());
			stmt.execute();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DramaRelationSearchCountMaker maker = new DramaRelationSearchCountMaker();
		maker.make();
	}
}
