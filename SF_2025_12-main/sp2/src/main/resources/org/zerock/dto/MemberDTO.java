package org.zerock.dto;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {

    private int mno;
    private String name;
    private String email;
    private String password;
    private String regdate;
    private String updatedate;

}
