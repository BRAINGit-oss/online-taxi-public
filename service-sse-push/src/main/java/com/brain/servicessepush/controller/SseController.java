package com.brain.servicessepush.controller;

import com.brain.servicepassengeruser.internalcommon.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SseController {
    public static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    /**
     * 通过SseEmitter建立前后端间的连接
     * @param userId 用户ID
     * @param identity 身份标识 1：乘客 2：司机
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId,@RequestParam String identity){
        log.info("用户ID"+userId+",身份类型："+identity);
        SseEmitter sseEmitter = new SseEmitter(0L);

        String sseEmitterKey = SsePrefixUtils.generatorSseKey(userId, identity);

        sseEmitterMap.put(sseEmitterKey,sseEmitter);

        return sseEmitter;
    }

    /**
     * 发送消息
     * @param userId
     * @param identity
     * @param content
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam Long userId,@RequestParam String identity,@RequestParam String content){
        String sseEmitterKey = SsePrefixUtils.generatorSseKey(userId, identity);

        try {
            if(sseEmitterMap.containsKey(sseEmitterKey)){
                sseEmitterMap.get(sseEmitterKey).send(content);
            }else{
                return "推送失败";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "给用户:"+sseEmitterKey+"，发送了消息："+content;
    }

    @GetMapping("/close")
    public String close(@RequestParam Long userId, @RequestParam String identity){
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        System.out.println("关闭连接："+sseMapKey);
        if (sseEmitterMap.containsKey(sseMapKey)){
            sseEmitterMap.remove(sseMapKey);
        }
        return "close 成功";
    }

}
