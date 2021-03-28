package it.cnr.ilc.lexo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author andreabellandi
 */
public class ReflectionUtil {

    public static Object getValue(Object object, String field) throws ReflectiveOperationException {
        try {
            field = getFielName("get", field);
            return object.getClass().getMethod(field).invoke(object);
        } catch (NullPointerException | StringIndexOutOfBoundsException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    public static void setValue(Object object, String field, Object value) throws ReflectiveOperationException {
        try {
            field = getFielName("set", field);
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(field)) {
                    method.invoke(object, new Object[]{value});
                }
            }
        } catch (StringIndexOutOfBoundsException ex) {
            throw new ReflectiveOperationException(ex);
        }
    }

    public static String getFielName(String type, String field) {
        return type.concat(field.substring(0, 1).toUpperCase().concat(field.substring(1)));
    }

    public static void copyFields(Object object1, Object object2) throws ReflectiveOperationException {
        if (!object1.getClass().isAssignableFrom(object2.getClass())) {
            throw new ReflectiveOperationException(object2.getClass().getSimpleName() + " is not a " + object1.getClass().getSimpleName());
        }
        for (Field field : object1.getClass().getDeclaredFields()) {
            setValue(object2, field.getName(), getValue(object1, field.getName()));
        }
    }

    public <T> T newInstance(Class<T> clazz) throws ReflectiveOperationException {
        return clazz.getConstructor().newInstance();
    }
}
