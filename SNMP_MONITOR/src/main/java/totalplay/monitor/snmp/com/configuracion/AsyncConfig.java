package totalplay.monitor.snmp.com.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name ="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(12);
        executor.setQueueCapacity(30);
        //executor.setAllowCoreThreadTimeOut(true);
        executor.afterPropertiesSet();
        executor.setThreadNamePrefix("monitorThread-");
        executor.getActiveCount();
        //executor.setKeepAliveSeconds(120);
        executor.initialize();
        return executor;
    }

    @Bean(name ="taskScheduledExecutor")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(4);
        threadPoolTaskScheduler.setThreadNamePrefix("task-scheduler");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}