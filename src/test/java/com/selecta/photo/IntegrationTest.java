package com.selecta.photo;

import com.selecta.photo.config.AsyncSyncConfiguration;
import com.selecta.photo.config.DatabaseTestcontainer;
import com.selecta.photo.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        SelectaPhotoApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.selecta.photo.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
