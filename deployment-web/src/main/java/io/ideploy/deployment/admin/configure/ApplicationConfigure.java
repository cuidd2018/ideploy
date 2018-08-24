package io.ideploy.deployment.admin.configure;

import io.ideploy.deployment.admin.configure.vo.RedisConfVO;
import io.ideploy.deployment.common.util.redis.Redis;
import io.ideploy.deployment.common.util.redis.RedisFactory;
import io.ideploy.deployment.encrypt.DefaultAesEncoder;
import io.ideploy.deployment.encrypt.ValueEncoder;
import io.ideploy.deployment.encrypt.ValueEncoderFactory;
import java.io.File;
import java.net.URL;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author: code4china
 * @description: 应用全局配置
 * @date: Created in 13:43 2018/6/21
 */
@Configuration
public class ApplicationConfigure {


    @Bean
    @ConditionalOnMissingBean(InternalResourceViewResolver.class)
    public InternalResourceViewResolver defaultViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }


    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(10000);
        threadPoolTaskExecutor.setKeepAliveSeconds(1000);
        return threadPoolTaskExecutor;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean
    public ValueEncoder valueEncoder(Environment environment){
        String encryptName= environment.getProperty("value.encrypt.name",
                DefaultAesEncoder.ENCODER_NAME);
        String encryptKey= environment.getProperty("value.encrypt.key",DefaultAesEncoder.DEFAULT_KEY);
        return  ValueEncoderFactory.getValueEncoder(encryptName, encryptKey);
    }

    @Bean
    public RedisFactory redisFactory(RedisConfVO redisConfVO){
        RedisFactory redisFactory = new RedisFactory();
        redisFactory.setTimeBetweenEvictionRunsMillis(redisConfVO.getTimeBetweenEvictionRunsMillis());
        redisFactory.setMinEvictableIdleTimeMillis(redisConfVO.getMinEvictableIdleTimeMillis());
        redisFactory.setNumTestsPerEvictionRun(redisConfVO.getNumTestsPerEvictionRun());
        redisFactory.setTestWhileIdle(redisConfVO.isTestWhileIdle());
        redisFactory.setMaxWaitMillis(redisConfVO.getMaxWaitMillis());
        redisFactory.setTestOnBorrow(redisConfVO.isTestOnBorrow());
        redisFactory.setTestOnReturn(redisConfVO.isTestOnReturn());
        redisFactory.setMaxTotal(redisConfVO.getMaxTotal());
        redisFactory.setMinIdle(redisConfVO.getMinIdle());
        redisFactory.setConnectionTimeout(redisConfVO.getConnectionTimeout());
        redisFactory.setSoTimeout(redisConfVO.getSoTimeout());
        redisConfVO.setJmxEnabled(redisConfVO.isJmxEnabled());
        return redisFactory;
    }

    @Bean
    public Redis redis(RedisFactory redisFactory, RedisConfVO redisConfVO){
        return redisFactory.initRedis(redisConfVO.getSingleServer());
    }

}
