package com.backupreality.shared.exceptions;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class ExceptionTranslator
{
    private static final Class<? extends Throwable> DEFAULT_EXCEPTION_BASE_CLASS = Exception.class;


    @ExceptionTranslationSource
    private final List<Object> translationBeans = null;

    private List<ExceptionTranslationImpl> translationImpl;

    private final Class<? extends Throwable> exceptionBaseClass;


    public ExceptionTranslator() {
        this(DEFAULT_EXCEPTION_BASE_CLASS);
    }

    public ExceptionTranslator(Class<? extends Throwable> exceptionBaseClass) {
        this.exceptionBaseClass = exceptionBaseClass;
    }


    @PostConstruct
    private void initTranslation()
            throws TranslationConfigurationException
    {
        List<ExceptionTranslationImpl> resultImpl = new ArrayList<>();

        for (Object bean : translationBeans)
        {
            // search for annotated methods
            for (Method method : bean.getClass().getDeclaredMethods())
            {
                if (method.getDeclaredAnnotation(ExceptionTranslation.class) != null)
                {
                    // check the signature

                    // check accessibility
                    if (!method.isAccessible())
                    {
                        method.setAccessible(true);
                    }

                    // return type
                    Class<?> returnClass = method.getReturnType();
                    if (!exceptionBaseClass.isAssignableFrom(returnClass))
                    {
                        throw new TranslationConfigurationException(
                                "Return type <" + returnClass + "> of the method <" + method + "> "
                                        + " is not a subclass of " + exceptionBaseClass
                        );
                    }

                    // arguments
                    Class<?>[] parameters = method.getParameterTypes();
                    if (parameters.length != 1)
                    {
                        throw new TranslationConfigurationException(
                                "Method <" + method + "> should have exactly one parameter"
                        );
                    }

                    Class<?> parameterType = parameters[0];
                    if (!exceptionBaseClass.isAssignableFrom(parameterType))
                    {
                        throw new TranslationConfigurationException(
                                "Argument type <" + parameterType + "> of the method <" + method + "> "
                                        + " is not a subclass of " + exceptionBaseClass
                        );
                    }

                    resultImpl.add(new ExceptionTranslationImpl(
                            bean,
                            method,
                            parameterType
                    ));
                }
            }
        }

        translationImpl = Collections.unmodifiableList(resultImpl);
    }


    public Exception translate(Exception exception)
            throws TranslationInvocationException
    {
        Optional<Exception> result = Optional.empty();

        for (ExceptionTranslationImpl impl : translationImpl)
        {
            Optional<Exception> translatedException = impl.translate(exception);
            if (translatedException.isPresent())
            {
                if (result.isPresent())
                {
                    System.out.println("ERROR: Exception <" + exception + "> resulted in multiple translations.");
                    result.get().addSuppressed(translatedException.get());
                }
                else
                {
                    result = translatedException;
                }
            }
        }

        return result.orElse(exception);
    }
}

