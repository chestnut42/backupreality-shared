package com.backupreality.shared.utils;

public class ComparableUtils
{
    // contains only static methods
    private ComparableUtils() {}


    public static <T extends Comparable<? super T>> T max(T a, T b)
    {
        return (a.compareTo(b) > 0) ? a : b;
    }


    public static <T extends Comparable<? super T>> T min(T a, T b)
    {
        return (a.compareTo(b) < 0) ? a : b;
    }


    /**
     * Returns whether {@code a} is less than {@code b}.
     * @param <T> the type of object.
     * @param a the first argument.
     * @param b the second argument.
     * @return whether {@code a} is less than {@code b}.
     */
    public static <T extends Comparable<? super T>> boolean less(T a, T b)
    {
        return a.compareTo(b) < 0;
    }


    public static <T extends Comparable<? super T>> boolean lessOrEqual(T a, T b)
    {
        return !greater(a, b);
    }


    public static <T extends Comparable<? super T>> boolean greater(T a, T b)
    {
        return a.compareTo(b) > 0;
    }


    public static <T extends Comparable<? super T>> boolean greaterOrEqual(T a, T b)
    {
        return !less(a, b);
    }
}

