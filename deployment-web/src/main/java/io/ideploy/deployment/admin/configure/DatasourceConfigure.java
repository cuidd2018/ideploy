package io.ideploy.deployment.admin.configure;

import com.alibaba.druid.pool.DruidDataSource;
import io.ideploy.deployment.admin.vo.config.DbConfigVO;
import io.ideploy.deployment.datasource.MyBatisDao;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author: code4china
 * @description: 数据源配置
 * @date: Created in 14:37 2018/7/10
 */
@Configuration
@MapperScan(basePackages = "io.ideploy.deployment.admin.dao",annotationClass = MyBatisDao.class)
public class DatasourceConfigure {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,  ResourcePatternResolver resolver) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:/mybatis/**/*.xml"));
        return sqlSessionFactory;
    }

    @Bean
    public DataSource masterDataSource(DbConfigVO dbConfigVO){
        DruidDataSource masterDataSource = new DruidDataSource();
        masterDataSource.setUrl(dbConfigVO.getUrlMaster());
        masterDataSource.setUsername(dbConfigVO.getUserName());
        masterDataSource.setPassword(dbConfigVO.getPassword());
        masterDataSource.setInitialSize(dbConfigVO.getInitialSize());
        masterDataSource.setMinIdle(dbConfigVO.getMinIdle());
        masterDataSource.setMaxActive(dbConfigVO.getMaxActive());
        masterDataSource.setMaxWait(dbConfigVO.getMaxWait());
        masterDataSource.setTimeBetweenEvictionRunsMillis(dbConfigVO.getTimeBetweenEvictionRunsMillis());
        masterDataSource.setMinEvictableIdleTimeMillis(dbConfigVO.getMinEvictableIdleTimeMillis());
        masterDataSource.setValidationQuery(dbConfigVO.getValidationQuery());
        masterDataSource.setTestWhileIdle(dbConfigVO.isTestWhileIdle());
        masterDataSource.setTestOnBorrow(dbConfigVO.isTestOnBorrow());
        masterDataSource.setTestOnReturn(dbConfigVO.isTestOnReturn());
        masterDataSource.setPoolPreparedStatements(dbConfigVO.isPoolPreparedStatements());
        masterDataSource.setMaxPoolPreparedStatementPerConnectionSize(dbConfigVO.getMaxPoolPreparedStatementPerConnectionSize());
        masterDataSource.setUseGlobalDataSourceStat(dbConfigVO.isUseGlobalDataSourceStat());
        return masterDataSource;
    }

}
