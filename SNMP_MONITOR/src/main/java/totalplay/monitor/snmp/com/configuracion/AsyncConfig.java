package totalplay.monitor.snmp.com.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name ="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        //executor.setAllowCoreThreadTimeOut(true);

        executor.afterPropertiesSet();
        executor.setThreadNamePrefix("monitorThread-");
        executor.getActiveCount();
        //executor.setKeepAliveSeconds(120);
        executor.initialize();
        return executor;
    }
    

}