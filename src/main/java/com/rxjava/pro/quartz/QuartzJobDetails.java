package com.rxjava.pro.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzJobDetails {

	public void schedulerTask() {

		JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity("testJob").build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever()).build();

		CronTrigger cronTigger = TriggerBuilder.newTrigger().withIdentity("crontrigger", "crontriggergroup1")
				.withSchedule(CronScheduleBuilder.cronSchedule("10 * * * * ?")).build();

		StdSchedulerFactory schedularFactory = new StdSchedulerFactory();
		try {
			Scheduler schedule = schedularFactory.getScheduler();
			schedule.start();

			schedule.scheduleJob(job, cronTigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
