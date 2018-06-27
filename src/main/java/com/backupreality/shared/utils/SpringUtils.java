package com.backupreality.shared.utils;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SpringUtils
{
    // contains only static methods
    private SpringUtils() {}


    public static <T extends Enum<T> & GrantedAuthority>
    Optional<T> parseAuthority(String authName, Class<T> clazz)
    {
        for (T auth : clazz.getEnumConstants())
        {
            if (auth.getAuthority().equals(authName))
            {
                return Optional.of(auth);
            }
        }

        return Optional.empty();
    }


    public static <T extends Enum<T> & GrantedAuthority>
    Optional<List<T>> parseAuthority(List<String> authNames, Class<T> clazz)
    {
        return OptionalUtils.flattenList(authNames.stream()
                .map((an) -> parseAuthority(an, clazz))
                .collect(Collectors.toList()));
    }
}

