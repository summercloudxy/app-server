package com.zgiot.app.server.module.generic;

import com.zgiot.common.reloader.ServerReloadManager;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by xiayun on 2017/9/20.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/time")
    public ResponseEntity<String> getSystemTime(){
        return new ResponseEntity<>(ServerResponse.buildOkJson(new Date())
                , HttpStatus.OK);
    }

    @GetMapping("/reloadAll")
    public ResponseEntity<String> reloadAll(){

        ServerReloadManager.reloadAll();

        return new ResponseEntity<>(ServerResponse.buildOkJson("reloaded.")
                , HttpStatus.OK);
    }
}
