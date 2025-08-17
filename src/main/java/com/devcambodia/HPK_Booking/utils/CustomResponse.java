package com.devcambodia.HPK_Booking.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class CustomResponse {
    public ResponseEntity<Object> success(String msg, Object object, HttpStatus status){
        Map<String, Object> map = new HashMap<>();
        map.put("message :",msg);
        map.put("data :",object);
        map.put("date :",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("status :",status.value());
        return new ResponseEntity<>(map,status);
    }
    public ResponseEntity<Object> successWithPagination(String msg, Object object,int total,HttpStatus status){
        Map<String, Object> map = new HashMap<>();
        map.put("message :",msg);
        if (total != 0){
            map.put("Total :",total);
        }
        map.put("data :",object);
        map.put("date :",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("status :",status.value());
        return new ResponseEntity<>(map,status);
    }
    public ResponseEntity<Object> error(String msg, HttpStatus status){
        Map<String, String> map = new HashMap<>();
        map.put("message :",msg);
        map.put("date :",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("status :",String.valueOf(status.value()));
        return new ResponseEntity<>(map,status);
    }
}
