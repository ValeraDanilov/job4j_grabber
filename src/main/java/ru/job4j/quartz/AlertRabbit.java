package ru.job4j.quartz;

import org.quartz.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private final Properties pr = new Properties();

    public static void main(String[] args) {
        AlertRabbit al = new AlertRabbit();
        try (Connection connection = al.readFile()) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(al.pr.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception eo) {
            eo.printStackTrace();
        }
    }

    private Connection readFile() throws SQLException, ClassNotFoundException {
        try (InputStream io = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            this.pr.load(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(this.pr.getProperty("jdbc.driver"));
        return DriverManager.getConnection(this.pr.getProperty("jdbc.url"), this.pr.getProperty("jdbc.userName"), this.pr.getProperty("jdbc.userPassword"));
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("store");
            try (PreparedStatement pr = connection.prepareStatement("insert into rabbit(created_date) values(?)")) {
                pr.setString(1, String.valueOf(System.currentTimeMillis()));
                pr.execute();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
