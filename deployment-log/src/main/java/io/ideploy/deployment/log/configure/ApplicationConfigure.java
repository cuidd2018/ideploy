package io.ideploy.deployment.log.configure;

import io.ideploy.deployment.common.util.redis.Redis;
import io.ideploy.deployment.common.util.redis.RedisFactory;
import io.ideploy.deployment.log.configure.vo.RedisConfVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: code4china
 * @description:
 * @date: Created in 10:26 2018/8/23
 */
@Configuration
public class ApplicationConfigure {

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
