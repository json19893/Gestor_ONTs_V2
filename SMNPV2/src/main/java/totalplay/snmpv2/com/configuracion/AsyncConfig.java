package totalplay.snmpv2.com.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name ="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
System.out.println("procesadores:::  "+Runtime.getRuntime().availableProcessors());
			executor.setCorePoolSize(250);
			executor.setMaxPoolSize(Integer.MAX_VALUE);
			executor.setQueueCapacity(Integer.MAX_VALUE);
			executor.setPrestartAllCoreThreads(true);
			executor.setAllowCoreThreadTimeOut(false);
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			executor.setThreadPriority(Thread.MAX_PRIORITY);
			
		
        //executor.setAllowCoreThreadTimeOut(true);

        executor.afterPropertiesSet();
        executor.setThreadNamePrefix("snmpThread-");
        executor.getActiveCount();
        //executor.setKeepAliveSeconds(120);
        executor.initialize();
        return executor;
    }
    
    @Bean(name ="taskExecutor2")
    public Executor taskExecutor2(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
System.out.println("procesadores:::  "+Runtime.getRuntime().availableProcessors());
			executor.setCorePoolSize(250);
			executor.setMaxPoolSize(Integer.MAX_VALUE);
			executor.setQueueCapacity(Integer.MAX_VALUE);
			executor.setPrestartAllCoreThreads(true);
			executor.setAllowCoreThreadTimeOut(true);
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			executor.setThreadPriority(Thread.MAX_PRIORITY);
			
		
        //executor.setAllowCoreThreadTimeOut(true);

        executor.afterPropertiesSet();
        executor.setThreadNamePrefix("snmpThread-");
        executor.getActiveCount();
        //executor.setKeepAliveSeconds(120);
        executor.initialize();
        return executor;
    }
}