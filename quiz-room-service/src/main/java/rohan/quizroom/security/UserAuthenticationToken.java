package rohan.quizroom.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long userId;

    public UserAuthenticationToken(String username, Long userId) {
        super(username, null, Collections.emptyList());
        this.userId = userId;
    }

    public UserAuthenticationToken(String username, Long userId,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(username, null, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}