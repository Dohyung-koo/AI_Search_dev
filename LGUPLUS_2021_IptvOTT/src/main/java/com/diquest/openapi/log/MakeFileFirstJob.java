package com.diquest.openapi.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.diquest.openapi.util.info.STRING_TYPE;

@Component
public class  MakeFileFirstJob implements Job {
	
	private static String uploadPath = STRING_TYPE.LOG_PATH.getErrorMessage();
	private static String googleUploadPath = STRING_TYPE.GOOGLE_LOG_PATH.getErrorMessage();

	private static String was_num = null;
	private static String google_was_num = null;

	private static long second = 1000;

    private static long minute = second * 60;

    private static long hour = minute * 60;

    private static long day = hour * 24;
	
    
    /**
     * <p>
     * Empty constructor for job initilization
     * </p>
     * <p>
     * Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.
     * </p>
     */
    public MakeFileFirstJob() {
    }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     * 
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
    public synchronized void execute(JobExecutionContext context)
        throws JobExecutionException {

    	MakeFileFirstJob job = new MakeFileFirstJob();
		//System.out.println("(String)context.getMergedJobDataMap().get(\"googleWasnum\") : " +(String)context.getMergedJobDataMap().get("googleWasnum"));
    	job.jobStart((String)context.getMergedJobDataMap().get("wasnum"), (String)context.getMergedJobDataMap().get("googleWasnum"));
    }
	
	public void jobStart(String wasnum , String google_was_num) {	// 파일 생성
		// Say Hello to the World and display the date/time
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmm");
		String yyyyMMddHHmm = dayTime.format(new Date(time));	// 폴더에 만들어져 있는 마지막 파일명
		String lastFile_yyyyMMddHHmm = "";
    
        String yyyyMMdd = yyyyMMddHHmm.substring(0, 8);
        String yyyyStr = yyyyMMddHHmm.substring(0, 4);
        String mmStr = yyyyMMddHHmm.substring(4, 6);
        String ddStr = yyyyMMddHHmm.substring(6, 8);
        
//        long mm = Long.parseLong( yyyyMMddHHmm.substring(10) );
//		
//        // 분이 0,1,2,3,4 분으로 끝날경우 *0분 까지 
//    	if  ( mm%10 == 0 || mm%10 == 1 || mm%10 == 2 || mm%10 == 3 || mm%10 == 4 ) {
//    		mm= mm- (mm%10);
//    	} else {	//분이 5,6,7,8,9 분으로 끝날경우 *5분 까지
//    		mm= mm-(mm%5);
//    	}
//    	
//    	String yyyyMMddHH = yyyyMMddHHmm.substring(0, 10);
//    	if ( mm<10) {
//    		yyyyMMddHHmm = yyyyMMddHH+"0"+mm;
//    	} else {
//    		yyyyMMddHHmm = yyyyMMddHH+mm;
//    	}
//		
    	// 폴더 체크
		File path = new File(uploadPath+yyyyMMdd);
    	if(!path.exists()) {
    		
    		path.mkdirs();
    		
    		try {
				Runtime.getRuntime().exec("chmod 755 "+path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
//    	
//    	//특정 directory 내 파일 목록 가져오기
//    	File[] files = path.listFiles();
//    	String fileNm = "";	//마지막 파일명
//    	for (File file : files) {
//
//	    	//파일이 directory 가 아닌 file 일때
//	    	if (file.isFile()) {
//		    	
//	    		String [] arr = file.getName().split("\\.");
//	    		lastFile_yyyyMMddHHmm = arr[2];
//	    	}
//
//    	}
//    	
//	  Date date1 = null;
//      Date date2 = null;
//      SimpleDateFormat sdf = new SimpleDateFormat();
//      sdf.applyPattern("yyyyMMddHHmm");
//      
//      try {
//          date1 = sdf.parse(yyyyMMddHHmm);
//          date2 = sdf.parse(lastFile_yyyyMMddHHmm);
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//
//      long intervalMilli = millis(date2, date1);
//      System.out.println("date2:::"+date2+"///////"+date1);
//      long cnt =intervalMilli/minute/5;
//
////    	System.out.println(intervalMilli/minute+"분차이반복회수:::"+cnt);
//    	long firsttime = Long.parseLong(yyyyMMddHHmm); 
//				
//		Calendar c = Calendar.getInstance();
//        c.setTime(date2);
//        
//        String fileFullName = "";
//    	for ( int i = 0; i<cnt; i++ ) {
//    		
//    		c.add(Calendar.MINUTE, 5); 
//    		
//    		fileFullName = uploadPath+yyyyMMdd+"/SNS.001."+dayTime.format(c.getTime())+".log";
//        	System.out.println(fileFullName);
//        	// 파일 체크
//        	File filePath = new File(fileFullName);
//        	if(!filePath.exists()) {	//	파일이 존재하지 않으면	
//        		
//        		// 파일 생성
//                try{
//                                 
//                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
//                    BufferedWriter fw = new BufferedWriter(new FileWriter(fileFullName, true));
//                    fw.flush();
//                    // 객체 닫기
//                    fw.close(); 
//                     
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//        		
//        	}
//    		
//    	}
    	
        
        long mm = Long.parseLong( yyyyMMddHHmm.substring(10) );
		
    	
    	// 분이 0,1,2,3,4 분으로 끝날경우 0분 로그에 추가
    	if  ( mm%10 == 0 || mm%10 == 1 || mm%10 == 2 || mm%10 == 3 || mm%10 == 4 ) {
    		mm= mm- (mm%10);
    	} else {	//분이 5,6,7,8,9 분으로 끝날경우 5분 로그에 추가
    		mm= mm-(mm%5);
    	}
    	
    	String yyyyMMddHH = yyyyMMddHHmm.substring(0, 10);
    	if ( mm<10) {
    		yyyyMMddHHmm = yyyyMMddHH+"0"+mm;
    	} else {
    		yyyyMMddHHmm = yyyyMMddHH+mm;
    	}
    	
    	
    	String fileFullName = uploadPath+yyyyMMdd+wasnum+"."+yyyyMMddHHmm+".log";
    	
    		
        	// 파일 체크
        	File filePath = new File(fileFullName);
        	if(!filePath.exists()) {	//	파일이 존재하지 않으면	
        		
        		// 파일 생성
                try{
                                 
                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
                    BufferedWriter fw = new BufferedWriter(new FileWriter(fileFullName, true));
                     
                    // 파일안에 문자열 쓰기
                    fw.flush();
         
                    // 객체 닫기
                    fw.close(); 
                     
                    Runtime.getRuntime().exec("chmod 755 "+filePath);  
                }catch(Exception e){
                    e.printStackTrace();
                }
        		
        	}


		// 폴더 체크
		String googleUploadPathDate = googleUploadPath+yyyyStr+"/"+mmStr+"/"+ddStr+"/";
		File googlePath = new File(googleUploadPathDate);
		if(!googlePath.exists()) {

			googlePath.mkdirs();

			try {
				Runtime.getRuntime().exec("chmod 755 "+googlePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String googleFileFullName = googleUploadPathDate+google_was_num+"."+yyyyMMddHHmm+".log";

		// 파일 체크
		File googleFilePath = new File(googleFileFullName);
		if(!googleFilePath.exists()) {	//	파일이 존재하지 않으면

			// 파일 생성
			try{

				// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
				BufferedWriter fw = new BufferedWriter(new FileWriter(googleFileFullName, true));

				// 파일안에 문자열 쓰기
				fw.flush();

				// 객체 닫기
				fw.close();

				Runtime.getRuntime().exec("chmod 755 "+googleFilePath);
			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}
	
	/**
     * @param startDate Start Date
     * @param endDate End Date
     * @return milliseconds difference
     */
    public static long millis(Date startDate, Date endDate) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);

        return c2.getTimeInMillis() - c1.getTimeInMillis();

    }
    
    /**
     * @param startDate Start date
     * @param endDate End Date
     * @return minutes difference
     */
    public static long minutes(Date startDate, Date endDate) {
        long millis = millis(startDate, endDate);
        return millis / minute;
    }
    
    /**
     * @param startDate Start Date
     * @param endDate End Date
     * @return hours difference
     */
    public static long hours(Date startDate, Date endDate) {
        long millis = millis(startDate, endDate);
        return millis / hour;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MakeFileFirstJob job = new MakeFileFirstJob();
		job.jobStart(was_num , google_was_num);
	}
	@PostConstruct
	public void main(){
		System.out.println(google_was_num);
		String a = google_was_num;
	}
}
