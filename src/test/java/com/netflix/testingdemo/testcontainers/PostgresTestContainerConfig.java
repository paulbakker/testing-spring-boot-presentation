package com.netflix.testingdemo.testcontainers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
@Import({ TestcontainersPropertySourceAutoConfiguration.class })
public class PostgresTestContainerConfig {

    @ServiceConnection
    @Bean
    PostgreSQLContainer postgreSQLContainer(DynamicPropertyRegistry properties) {
        var db = new PostgreSQLContainer("postgres");

        db.withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"),
                "/docker-entrypoint-initdb.d/init.sql");
        db.withInitScript("shows.sql");

        properties.add("spring.datasource.url", db::getJdbcUrl);
        properties.add("spring.datasource.username", db::getUsername);
        properties.add("spring.datasource.password", db::getPassword);

        return db;
    }
}
