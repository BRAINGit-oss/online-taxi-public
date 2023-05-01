package com.brain.serviceprice.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码工具类
 */
public class MySqlGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-price?characterEncoding=utf-8&ServerTimezone=GMT%2B8",
                "root","root")
                .globalConfig(builder -> {
                    builder.author("Brain")
                            .fileOverride()
                            .outputDir("D:\\SpringBootPro\\online-taxi-public\\service-price\\src\\main\\java"); //输出到java目录（绝对路径）
                })
                .packageConfig(builder -> {
                    builder.parent("com.brain.serviceprice")  //父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "D:\\SpringBootPro\\online-taxi-public\\service-price\\src\\main\\java\\com\\brain\\serviceprice\\mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("price_rule");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
