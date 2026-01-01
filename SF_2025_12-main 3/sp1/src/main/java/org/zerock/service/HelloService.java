package org.zerock.service;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HelloService {

    public void hello1() {
        
    }
    public String hello2(String name) {
        return "hello " + name;
    }

}

