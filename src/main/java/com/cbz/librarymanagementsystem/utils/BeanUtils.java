package com.cbz.librarymanagementsystem.utils;

import java.lang.reflect.Field;
public class BeanUtils {

    /**
     * 同属性转换
     * @param obj    源对象
     * @param tClass 转换后的类的字节码
     * @param <T>   类型
     * @return      T 的对象
     */
    public static <T> T toBeanDTO(Object obj, Class<T> tClass)  {

        T t = null;

        try {
            //利用类字节码创建对象
            t = tClass.getConstructor().newInstance();

            Field[] tFields = tClass.getDeclaredFields();

            for (Field tField : tFields) {
                tField.setAccessible(true);

                Field field = obj.getClass().getDeclaredField(tField.getName());
                field.setAccessible(true);
                Object o = field.get(obj);

                tField.set(t,o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
}
