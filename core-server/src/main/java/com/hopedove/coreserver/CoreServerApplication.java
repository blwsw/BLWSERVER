package com.hopedove.coreserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication(scanBasePackages = {"com.hopedove"})
@EnableTransactionManagement
@MapperScan("com.hopedove.coreserver.dao")
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.hopedove.ucserver.**"})
@Import(FdfsClientConfig.class)
public class CoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreServerApplication.class, args);
    }
}
