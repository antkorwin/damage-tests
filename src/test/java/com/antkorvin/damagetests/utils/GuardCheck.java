package com.antkorvin.damagetests.utils;

import com.antkorvin.damagetests.errorinfos.ErrorInfo;
import org.junit.Assert;


/**
 * Created by Korovin Anatolii on 13.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class GuardCheck {


    public static void check(Runnable action, Class<? extends Throwable> exceptionClass, ErrorInfo errorInfo) {

        boolean fail = false;
        try {
            action.run();
            fail = true;
        } catch (Throwable t) {
            // находим первопричину
            Throwable cause = t;
            while (cause.getCause() != null) cause = cause.getCause();

            if (cause instanceof RuntimeException && exceptionClass.isInstance(cause.getCause())) {
                Assert.assertEquals(errorInfo.getMessage(), cause.getCause().getMessage());
            } else if (exceptionClass.isInstance(cause)) {
                Assert.assertEquals(errorInfo.getMessage(), cause.getMessage());
            } else {
                Assert.fail(String.format("Bad exception type.\n\tExpected: %s,\n\tRecieved: %s",
                                          exceptionClass.getName(),
                                          cause.getClass().getName()));
            }
        }

        if (fail) Assert.fail(String.format("No exception was thrown, but expected: %s(%s)",
                                            exceptionClass.getSimpleName(),
                                            errorInfo));
    }
}
