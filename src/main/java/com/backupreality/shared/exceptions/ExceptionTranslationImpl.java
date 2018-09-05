package com.backupreality.shared.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;


class ExceptionTranslationImpl
{
    private final Object bean;
    private final Method method;
    private final Class<?> argumentType;


    ExceptionTranslationImpl(
            Object bean,
            Method method,
            Class<?> argumentType
    )
    {
        this.bean = bean;
        this.method = method;
        this.argumentType = argumentType;
    }


    Optional<Exception> translate(Exception exception)
            throws TranslationInvocationException
    {
        try
        {
            if (argumentType.isAssignableFrom(exception.getClass()))
            {
                Exception result = (Exception) method.invoke(bean, exception);
                return Optional.ofNullable(result);
            }
            else
            {
                return Optional.empty();
            }
        }
        catch (IllegalAccessException | InvocationTargetException ex)
        {
            throw new TranslationInvocationException(ex);
        }
    }
}

