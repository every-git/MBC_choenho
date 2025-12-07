package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.zerock.dto.MemberDTO;


@Service
@RequiredArgsConstructor
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;

    public List<MemberDTO> getList() {
        return memberMapper.getList();
    }

    public MemberDTO memberById(int mno) {
        return memberMapper.memberById(mno);
    }

    public void update(MemberDTO dto) {
        memberMapper.update(dto);
    }

    public void insert(MemberDTO dto) {
        memberMapper.insert(dto);
    }

    public void delete(int mno) {
        memberMapper.delete(mno);
    }

}
