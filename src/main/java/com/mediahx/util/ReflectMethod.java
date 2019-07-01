package com.mediahx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * 
 * @author ZHE
 *
 */
public class ReflectMethod {
	
	/**
	 * 
	 * @param objectClass
	 * @param fieldName
	 * @return Method
	 */
	@SuppressWarnings("unchecked")
	public static Method getGetMethod(Class objectClass, String fieldName) {

		StringBuffer sb = new StringBuffer();

		sb.append("get");

		sb.append(fieldName.substring(0, 1).toUpperCase());

		sb.append(fieldName.substring(1));

		try {

			return objectClass.getMethod(sb.toString());

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * 
	 * @param objectClass
	 * @param fieldName
	 * @return Method
	 */
	@SuppressWarnings("unchecked")
	public static Method getSetMethod(Class objectClass, String fieldName) {

		try {

			Class[] parameterTypes = new Class[1];

			Field field = objectClass.getDeclaredField(fieldName);

			parameterTypes[0] = field.getType();

			StringBuffer sb = new StringBuffer();

			sb.append("set");

			sb.append(fieldName.substring(0, 1).toUpperCase());

			sb.append(fieldName.substring(1));

			Method method = objectClass.getMethod(sb.toString(), parameterTypes);

			return method;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * Invoke Set method
	 * @param o
	 * @param fieldName
	 * @param value
	 */
	public static void invokeSet(Object o, String fieldName, Object value) {
		
		Method method = getSetMethod(o.getClass(), fieldName);
		try {
			method.invoke(o, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Ivoke Get Method
	 * @param o
	 * @param fieldName
	 * @return Object
	 */
	public static Object invokeGet(Object o, String fieldName) {
		
		Method method = getGetMethod(o.getClass(), fieldName);
		try {
			return method.invoke(o, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	public static String invokeGetMethod(Object o, String fieldName) {
		
		Method method = getGetMethod(o.getClass(), fieldName);
		try {
			Object obj = method.invoke(o, new Object[0]);
			if(obj!=null && !"".equals(obj)){
				 return obj.toString();
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
    public static String invokeGetMethodByNum(Object o, String fieldName) {
		
		Method method = getGetMethod(o.getClass(), fieldName);
		try {
			Object obj = method.invoke(o, new Object[0]);
			if(obj!=null && !"".equals(obj)){
				 return obj.toString();
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
