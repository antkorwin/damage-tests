package com.antkorvin.damagetests.exceptions;

import com.antkorvin.damagetests.errorinfos.ErrorInfo;

/**
 * Created by Korovin Anatolii on 13.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class ConditionValidationException extends BaseException {

    public ConditionValidationException(String message, Integer code) {
        super(message, code);
    }

    public ConditionValidationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
