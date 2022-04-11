package com.diquest.db;

@Deprecated
public class SampleDB {
//	
//	private String TABLE = "schedule";
//	// ID >> auto_increment
//	private final String[] ALL_COLUMN = new String[]{
//			"SERVICE_ID",
//			"SERVICE_NAME",
//			"SERVICE_ENG_NAME",
//			"CHANNEL_NO",
//			"PROGRAM_ID",
//			"PROGRAM_NAME",
//			"THM_IMG_URL",
//			"THM_IMG_FILE_NAME",
//			"RATING",
//			"BROAD_TIME",
//			"DAY",
//			"OVERSEER_NAME",
//			"ACTOR",
//			"P_NAME",
//			"GENRE1",
//			"GENRE2",
//			"GENRE3",
//			"SERIES_NO",
//			"SUB_NAME",
//			"BROAD_DATE",
//			"LOCAL_AREA",
//			"AV_RESOLUTION",
//			"Caption_Flag",
//			"Dvs_Flag",
//			"IS_51_CH",
//			"FILTERING_CODE",
//			"CHNL_KEYWORD",
//			"CHNL_ICON_URL",
//			"CHNL_ICON_FILE_NAME",
//	};
//	private final String[] SELECT_COLUMN = new String[]{"SOURCE","CATEGORY","PARENT_ID","SCATEGORY"};
//	private final String[] INDEX_COLUMN =  new String[]{"ID"};
//	private final String[] INSERT_COLUMN = new String[]{
//			"SERVICE_ID",
//			"SERVICE_NAME",
//			"SERVICE_ENG_NAME",
//			"CHANNEL_NO",
//			"PROGRAM_ID",
//			"PROGRAM_NAME",
//			"THM_IMG_URL",
//			"THM_IMG_FILE_NAME",
//			"RATING",
//			"BROAD_TIME",
//			"DAY",
//			"OVERSEER_NAME",
//			"ACTOR",
//			"P_NAME",
//			"GENRE1",
//			"GENRE2",
//			"GENRE3",
//			"SERIES_NO",
//			"SUB_NAME",
//			"BROAD_DATE",
//			"LOCAL_AREA",
//			"AV_RESOLUTION",
//			"Caption_Flag",
//			"Dvs_Flag",
//			"IS_51_CH",
//			"FILTERING_CODE",
//			"CHNL_KEYWORD",
//			"CHNL_ICON_URL",
//			"CHNL_ICON_FILE_NAME",
//
//	};
//	private final String[] NEXTSEQ_COLUMN = new String[]{"MAX("+INDEX_COLUMN[0]+")+1"};
//	
//	private boolean batch;
//	PreparedStatement nextSeqPstmt = null;
//	PreparedStatement insertPstmt = null;
//	PreparedStatement insertAllPstmt = null;
//	AtomicInteger seq;
//	
//	public SampleDB(String tableName) {
//		this.TABLE = tableName;
//	}
//	public SampleDB(boolean batch, String tableName) {
//		this.batch = batch;
//		this.TABLE = tableName;
//	}
//	public SampleDB(boolean batch, String tableName, Connection conn) {
//		this.batch = batch;
//		this.TABLE = tableName;
//		setSeq(conn);
//	}
//	
//	private void setSeq(Connection conn){
//		createNextSeqPstmt(conn);
//		this.seq = new AtomicInteger(0);
//		this.seq.set(nextSeqFirst() -1);
//		closeNextSeqPstmt();
//	}
//	
//	private int nextSeqFirst(){
//		int nextSeq = -1;
//		try {
//			ResultSet rs = nextSeqPstmt.executeQuery();
//			while(rs.next()) {
//				nextSeq = rs.getInt(1);
//			}
//			rs.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if(nextSeq==0){
//			return 1;
//		}
//		return nextSeq;
//	}
//	
//	public int nextSeq() {
//		return seq.incrementAndGet();
//	}
//	
//	
//	private void createNextSeqPstmt(Connection conn) { 
//		if(nextSeqPstmt == null){
//			StringBuffer sb = new StringBuffer();
//			SqlUtil.generateSelectSql(sb, TABLE, NEXTSEQ_COLUMN);
//			String sql = sb.toString();
//			nextSeqPstmt = SqlUtil.generatePstmt(conn, sql);
//		}
//	}
//	
//	public void createInsertPstmt(Connection conn) {
//		if(insertPstmt == null){
//			StringBuffer sb = new StringBuffer();
//			SqlUtil.generateInsertSql(sb, TABLE, INSERT_COLUMN);
//			String sql = sb.toString();
//			insertPstmt = SqlUtil.generatePstmt(conn, sql);	
//		}
//	}
//	public void createInsertAllPstmt(Connection conn) {
//		if(insertAllPstmt == null){
//			StringBuffer sb = new StringBuffer();
//			SqlUtil.generateInsertSql(sb, TABLE, ALL_COLUMN);
//			String sql = sb.toString();
//			insertAllPstmt = SqlUtil.generatePstmt(conn, sql);	
//		}
//	}
//	
//	public void closeInsertPstmt(){		closePstmt(insertPstmt);	}
//	public void closeInsertAllPstmt(){		closePstmt(insertAllPstmt);	}
//	public void closeNextSeqPstmt(){	closePstmt(nextSeqPstmt);}
//	
//	
//	public ScheduleVO[] getMappingList(Connection conn,String tableName) {
//		List<ScheduleVO> list = new ArrayList<ScheduleVO>();
//		try {
//			StringBuffer sb= new StringBuffer();
//			SqlUtil.generateSelectSql(sb, tableName, SELECT_COLUMN);
//			String sql = sb.toString();
//			PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sql);
//			ResultSet rs =  pstmt.executeQuery();
//			
//			while(rs.next()){
//				ScheduleVO s = new ScheduleVO();
////				s.setSource(rs.getString(1));
////				s.setCategory(rs.getString(2));
////				s.setParentId(rs.getString(3));
////				s.setScategory(rs.getString(4));
//				list.add(s);
//			}
//			pstmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list.toArray(new ScheduleVO[list.size()]);
//	}
//	
//	public ScheduleVO[] getMappingList(Connection conn) {
//		List<ScheduleVO> list = new ArrayList<ScheduleVO>();
//		try {
//			StringBuffer sb= new StringBuffer();
//			SqlUtil.generateSelectSql(sb, TABLE, SELECT_COLUMN);
//			String sql = sb.toString();
//			PreparedStatement pstmt = SqlUtil.generatePstmt(conn, sql);
//			ResultSet rs =  pstmt.executeQuery();
//			
//			while(rs.next()){
//				ScheduleVO s = new ScheduleVO();
////				s.setSource(rs.getString(1));
////				s.setCategory(rs.getString(2));
////				s.setParentId(rs.getString(3));
////				s.setScategory(rs.getString(4));
//				list.add(s);
//			}
//			pstmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return list.toArray(new ScheduleVO[list.size()]);
//	}
//	
//	public int insertAll(ScheduleVO vo) {
//		int resultCnt = -1;
//		try {
//			insertAllPstmt.setString(1, vo.getServiceId());
//			insertAllPstmt.setString(2, vo.getServiceName());
//			insertAllPstmt.setString(3, vo.getServiceEngName());
//			insertAllPstmt.setString(4, vo.getChannelNo());
//			insertAllPstmt.setString(5, vo.getProgramId());
//			insertAllPstmt.setString(6, vo.getProgramName());
//			insertAllPstmt.setString(7, vo.getThmImgUrl());
//			insertAllPstmt.setString(8, vo.getThmImgFileName());
//			insertAllPstmt.setString(9, vo.getRating());
//			insertAllPstmt.setString(10, vo.getBroadTime());
//			insertAllPstmt.setString(11, vo.getDay());
//			insertAllPstmt.setString(12, vo.getOverseerName());
//			insertAllPstmt.setString(13, vo.getActor());
//			insertAllPstmt.setString(14, vo.getpName());
//			insertAllPstmt.setString(15, vo.getGenre1());
//			insertAllPstmt.setString(16, vo.getGenre2());
//			insertAllPstmt.setString(17, vo.getGenre3());
//			insertAllPstmt.setString(18, vo.getSeriesNo());
//			insertAllPstmt.setString(19, vo.getSubName());
//			insertAllPstmt.setString(20, vo.getBroadDate());
//			insertAllPstmt.setString(21, vo.getLocalArea());
//			insertAllPstmt.setString(22, vo.getAvResolution());
//			insertAllPstmt.setString(23, vo.getCaptionFlag());
//			insertAllPstmt.setString(24, vo.getDvsFlag());
//			insertAllPstmt.setString(25, vo.getIs51ch());
//			insertAllPstmt.setString(26, vo.getFilteringCode());
//			insertAllPstmt.setString(27, vo.getChnlKeyword());
//			insertAllPstmt.setString(28, vo.getChnlIconUrl());
//			insertAllPstmt.setString(29, vo.getChnlIconFileName());
//			if(batch) {
//				insertAllPstmt.addBatch();
//			} else {
//				resultCnt = insertAllPstmt.executeUpdate();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return resultCnt;
//	}
//
//	
//	public static void closePstmt(PreparedStatement pstmt){
//		try {
//			if(pstmt != null)
//				pstmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}	
//	}
//	
//	public void executeInsertBatch() {
//		try {
//			insertPstmt.executeBatch();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void executeInsertAllBatch() {
//		try {
//			insertAllPstmt.executeBatch();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
