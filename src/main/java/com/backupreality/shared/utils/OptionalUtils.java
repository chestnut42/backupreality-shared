package com.backupreality.shared.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class OptionalUtils
{
    private OptionalUtils() {}


    public static <T> Optional<List<T>> flattenList(List<Optional<T>> listOfOptionals)
    {
        for (Optional<T> opv : listOfOptionals)
        {
            if (!opv.isPresent())
            {
                return Optional.empty();
            }
        }

        return Optional.of(
                listOfOptionals.stream()
                        .map((opv) -> opv.get())
                        .collect(Collectors.toList())
        );
    }
}

