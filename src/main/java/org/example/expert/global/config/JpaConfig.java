package org.example.expert.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfig {
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            DataSource dataSource,
//            JpaVendorAdapter jpaVendorAdapter
//    ) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setJpaVendorAdapter(jpaVendorAdapter);
//        em.setPackagesToScan("org.example.expert.domain");
//
//        Properties props = new Properties();
//        // props.setProperty("jakarta.persistence.exclude-unlisted-classes", "true");
//
//        em.setJpaProperties(props);
//        return em;
//    }
}
