package com.antkorvin.damagetests.utills;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.boot.SpringApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by Korovin A. on 29.03.2017.
 *
 * Вспомогательный класс для запуска SpringBoot приложения,
 * выводит конфигурацию запущеного модуля:
 *  - имя
 *  - ip-адрес
 *  - порт
 *  - профили-запуска
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class CustomRunner {

    public static void run(Class<?> runClass, String[] args){

        Logger logger = LoggerFactory.getLogger(runClass);
        Environment env = SpringApplication.run(runClass, args).getEnvironment();
        try {
            logger.info("\n----------------------------------------------------------\n\t" +
                     "Application '{}' is running! \n\t" +
                     "Local: \t\thttp://localhost:{}\n\t" +
                     "External: \thttp://{}:{}\n\t" +
                     "Profile(s): \t{}\n"+
                     "----------------------------------------------------------",
                     env.getProperty("spring.application.name"),
                     env.getProperty("server.port"),
                     InetAddress.getLocalHost().getHostAddress(),
                     env.getProperty("server.port"),
                     Arrays.toString(env.getActiveProfiles()));

        }catch (UnknownHostException e){
            logger.error("Exception host-address",e);
        }
    }
}
