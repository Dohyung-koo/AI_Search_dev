package com.diquest.openapi.log;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogMakeForder {

	public synchronized  void run(String wasnum, String googleWasnum) throws Exception {
	    // First we must get a reference to a scheduler
	    SchedulerFactory sf = new StdSchedulerFactory();
	    Scheduler sched;
		
			sched = sf.getScheduler();
			
		//  시작 - 서버 재기동시 당일 생성되지 않은 로그 파일 생성 작업
	    JobDetail job1 = newJob(MakeFileFirstJob.class).withIdentity("job1", "group1").usingJobData("wasnum", wasnum).usingJobData("googleWasnum",googleWasnum).build();
//	    Trigger trigger1 = newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(1,10)).startNow().build();		/// 매일 1시 10분에 실행
	    Trigger trigger1 = newTrigger().withIdentity("trigger1", "group1").withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1)).startNow().build();
	    
	    sched.scheduleJob(job1, trigger1);
	    
    	 
    	try {
    		sched.start();
    		
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			sched.shutdown();
			e1.printStackTrace();
		}
	    
    	
//    	 SchedulerFactory sf2 = new StdSchedulerFactory();
// 	    Scheduler sched2;
// 	   sched2 = sf2.getScheduler();
// 	    
//	      //  시작 - 1분 단위로 각 로그파일 체크 하여 없을 경우 생성
//	      JobDetail job2 = newJob(MakeFileJob.class).withIdentity("job2", "group2").build();
//		  Trigger trigger2 = newTrigger().withIdentity("trigger2", "group2").withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1)).startNow().build();
//	      
//		  try {
//	    		sched2.start();
//	    		
//			} catch (SchedulerException e1) {
//				// TODO Auto-generated catch block
//				sched2.shutdown();
//				e1.printStackTrace();
//			}
		  
	      
	      // executing...
	    //  종료 - 1분 단위로 각 로그파일 체크 하여 없을 경우 생성
	   
	  }
	  
}
