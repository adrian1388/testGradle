package com.test;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.test.data.eclipselink.UuidConverter;
import com.test.logic.SensorReading;

@ComponentScan(basePackages={"com.test.logic"})
@ComponentScan(basePackageClasses = Module.class)
@EnableJpaRepositories(basePackageClasses = {
    Module.class
})
//@EntityScan(basePackageClasses = {
//	Jsr310JpaConverters.class
//})
@Configuration
@EnableScheduling
public class Module extends JpaBaseConfiguration {
    @Autowired
    DataSource dataSource;

    /**
     * Registers the service that saves changes in {@link Sensor}.
     */
    @Bean
    public SensorReading sensorReading() {
        SensorReading readingController = new SensorReading();
        readingController.setDataSource(dataSource);

        return readingController;
    }
    
    protected Module(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, properties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    /**
     * Configure various JPA properties.
     * 
     * <p>This configuration is usually in persistence.xml</p>
     */
    @Override
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, "com.test.data.eclipselink.GenericSession");
//      <property name="eclipselink.weaving" value="false"/>
        properties.put(PersistenceUnitProperties.WEAVING, "static");

        /*
         * Show the parameters passed to an SQL command when logging is turned on (for debugging purposes).
         * 
         * Leaving this on for production shouldn't matter as long as "spring.jpa.show-sql" in application.properties is false.
         */
        properties.put(PersistenceUnitProperties.LOGGING_PARAMETERS, "true");

        return properties;
    }
    
}
