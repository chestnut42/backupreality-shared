package com.backupreality.shared.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public class CookieUtils
{
    private CookieUtils() {}


    public static Optional<Cookie> getCookie(
            HttpServletRequest request,
            String cookieName
    )
    {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie c : cookies)
            {
                if (c.getName().equals(cookieName))
                {
                    return Optional.of(c);
                }
            }
        }

        return Optional.empty();
    }
}

