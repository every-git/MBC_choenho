package org.zerock.security;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.account.AccountDTO;
import org.zerock.dto.AccountRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("---------loadUserByUsername---------");
        log.info("username: {}", username);


        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUid(username);
        accountDTO.setUpw(passwordEncoder.encode("1111"));
        accountDTO.addRole(AccountRole.USER);
        accountDTO.addRole(AccountRole.MANAGER);

        return accountDTO;
    }

}
