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


    public static Optional<Long> parseLong(String str)
    {
        try
        {
            return Optional.of(Long.valueOf(str));
        }
        catch (NumberFormatException ex)
        {
            return Optional.empty();
        }
    }


    public static Optional<Double> parseDouble(String str)
    {
        try
        {
            return Optional.of(Double.valueOf(str));
        }
        catch (NumberFormatException ex)
        {
            return Optional.empty();
        }
    }
}

