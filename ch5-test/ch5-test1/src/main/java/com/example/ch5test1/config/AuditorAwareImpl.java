package com.example.ch5test1.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    // Optional 타입은, null 체크, 반환 타입이 존재를 하면, 그 반환 타입으로 받고,
    // 만약, null로 반환을 해도, 받아준다. null 예외를 발생 시키지 않음.
    @Override
    public Optional<String> getCurrentAuditor() {
        // 시큐리티의 특성 상, 인증의 절차를 거칠 때,
        // 멤버 서비스에서, 해당 이메일로 인증을, 로그인을 했기 때문에
        // 로그인의 정보를 시큐리티가 가지고 있음
        // 결론, 시큐리티로 로그인이 되었다면, 로그인의 정보를 시큐리티가 가지고 있다.
        // 그래서, 시큐리티에서 로그인 정보를 조회를 하거나,
        // 마치 세션에서 정보를 가지고 오는 것과 비슷한 구조
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
            System.out.println("userId(=authentication.getName()) : " + userId);
        }
        return Optional.of(userId);
    }

}