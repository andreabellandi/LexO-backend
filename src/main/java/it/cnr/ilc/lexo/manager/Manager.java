package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.Arrays;

/**
 *
 * @author andreabellandi
 */
public interface Manager {
    
    public static void validateWithEnum(String parameterName, Class<? extends Enum> enumClass, String parameterValue) throws ManagerException {
        if (!EnumUtil.containsString(enumClass, parameterValue)) {
            throw new ManagerException(parameterName + " field must be one of " + Arrays.toString(EnumUtil.stringValues(enumClass))); 
        }
    }
    
}
