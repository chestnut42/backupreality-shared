package com.backupreality.shared.security;

import com.backupreality.shared.utils.CookieUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


public class TokenCookieHandler
{
    private final String tokenCookieName;
    private final boolean isSecure;


    public TokenCookieHandler(
            String tokenCookieName,
            boolean isSecure
    )
    {
        this.tokenCookieName = tokenCookieName;
        this.isSecure = isSecure;
    }


    public Optional<String> readToken(HttpServletRequest request)
    {
        return CookieUtils
                .getCookie(request, this.tokenCookieName)
                .map((c) -> c.getValue());
    }


    public void setToken(HttpServletResponse response, String token)
    {
        this.setTokenCookie(response, Optional.of(token));
    }


    public void removeToken(HttpServletResponse response)
    {
        this.setTokenCookie(response, Optional.empty());
    }


    private void setTokenCookie(HttpServletResponse response, Optional<String> value)
    {
        Cookie tokenCookie = new Cookie(this.tokenCookieName, value.orElse(null));
        tokenCookie.setSecure(this.isSecure);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        if (!value.isPresent())
        {
            tokenCookie.setMaxAge(0);
        }

        response.addCookie(tokenCookie);
    }
}

