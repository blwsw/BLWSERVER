package com.hopedove.ucserver;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.hopedove"})
@EnableTransactionManagement
@MapperScan("com.hopedove.ucserver.dao")
@EnableEurekaClient

@Import(FdfsClientConfig.class)
public class UserCenterServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserCenterServerApplication.class, args);
//        SpringApplication springApplication = new SpringApplication(Application.class);
//        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
//        WebSocketController.setApplicationContext(configurableApplicationContext);  // 解决WebSocket不能注入的问题
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jdk8Module());
//        JavaTimeModule module = new JavaTimeModule();
//        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
}
