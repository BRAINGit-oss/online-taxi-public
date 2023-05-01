package com.brain.servicemap.controller;

import com.brain.servicemap.service.TerminalService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    TerminalService terminalService;

    /**
     * 创建终端
     * @param name 终端名称
     * @param desc 终端描述：carId
     * @return 返回终端id:tid
     */
    @PostMapping("/add")
    public ResponseResult<TerminalResponse> addTerminal(@RequestParam String name,@RequestParam String desc){
        return terminalService.addTerminal(name,desc);
    }

    /**
     * 创建轨迹
     * @param center 中心点
     * @param radius 半径 [0-5000]
     * @return List<TerminalResponse>tid,carId</> 后续根据carId提供乘客查询终端位置
     */
    @PostMapping("/aroundsearch")
    public ResponseResult<List<TerminalResponse>> aroundSearch(@RequestParam String center, @RequestParam Integer radius){

        return terminalService.aroundSearch(center,radius);
    }
}
