package com.antkorvin.damagetests.utills;

import com.antkorvin.damagetests.errorinfos.ErrorInfo;
import com.antkorvin.damagetests.exceptions.ConditionValidationException;
import com.antkorvin.damagetests.exceptions.NotFoundException;

/**
 * Created by Korovin Anatolii on 13.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class Guard {

    public static void checkEntityExist(Object entity, ErrorInfo errorInfo){
        if(entity == null) throw new NotFoundException(errorInfo);
    }

    public static void checkConditionValid(boolean condition, ErrorInfo errorInfo){
        if(!condition) throw new ConditionValidationException(errorInfo);
    }
}
