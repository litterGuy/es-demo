package ga.litterguy.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "ga.litterguy.esjdbc.dao", sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {
    @Value("${spring.datasource.druid.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.druid.driver-class-name}")
    private String driverClassName;
    private String maplocation = "classpath:mapper/*.xml";

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(datasourceUrl);
        datasource.setDriverClassName(driverClassName);
        return datasource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(maplocation));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate userSqlSessionTemplate(DataSource dataSource) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(dataSource));
    }
}
