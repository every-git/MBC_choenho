package org.zerock.mapper;

import org.zerock.dto.AccountDTO;
import org.apache.ibatis.annotations.Param;


public interface AccountMapper {
	int insert(AccountDTO accountDTO);
	int insertRoles(AccountDTO accountDTO);

	AccountDTO selectOne(@Param("uid") String uid);


}
