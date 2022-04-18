package cn.edu.bistu.cs.ir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 基于SpringBoot的信息检索示例工程启动器
 * @author chenruoyu
 */
@EnableAsync
@SpringBootApplication
public class IrDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrDemoApplication.class, args);
    }
}