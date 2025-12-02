package com.saeyan.dto;


import java.sql.Timestamp;

import lombok.Data;

@Data
public class BoardVO {

    private int num;
    private String name;
    private String email;
    private String pass;
    private String title;
    private String content;
    private int readcount;
    private Timestamp writedate;
}
