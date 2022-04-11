package com.diquest.openapi.log;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.springframework.stereotype.Component;



@Component
public class LogManager{

	static  String wasnum = null;
	static  String googleWasnum = null;

	public String getGoogleWasnum() {
		return googleWasnum;
	}

	public void setGoogleWasnum(String googleWasnum) {
		this.googleWasnum = googleWasnum;
	}

	public  String getWasnum() {
		return wasnum;
	}


	public  void setWasnum(String wasnum) {
		this.wasnum = wasnum;
	}

	@PostConstruct
	public void initDicLoad() {
		if(getWasnum() != null && getGoogleWasnum()!= null) {
			this.wasnum = getWasnum();
			this.googleWasnum = getGoogleWasnum();
			//System.out.println("PostConstruct : "+getGoogleWasnum());
			try {
				init(getWasnum(),getGoogleWasnum());
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	LogManager() throws ServletException {
		
	}
	
	
	public void init(String wasnum, String googleWasnum) throws ServletException {
		
		
	   LogMakeForder lm = new LogMakeForder();
	   try {
		lm.run(wasnum, googleWasnum);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   
	    
	      //  시작 - 1분 단위로 각 로그파일 체크 하여 없을 경우 생성
//	      JobDetail job2 = newJob(MakeFileJob.class).withIdentity("job2", "group2").build();
//		  Trigger trigger2 = newTrigger().withIdentity("trigger2", "group2").withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1)).startNow().build();
//	      
//		  sched2.scheduleJob(job2, trigger2);
//	
//		  // scheduler has been started)
//		  sched2.start();
	      
	      // executing...
	    //  종료 - 1분 단위로 각 로그파일 체크 하여 없을 경우 생성
	   
	  }


}
