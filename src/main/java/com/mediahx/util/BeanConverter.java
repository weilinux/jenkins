package com.mediahx.util;

import java.util.Date;

import org.apache.commons.beanutils.Converter;

/**
 *@author  ZHE
 * 
 */
public class BeanConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
			//System.out.println("ok============"+type.getName());
			if (!CommUtils.isObjEmpty(type)) {
				if (type.getName().equals(java.util.Date.class.getName())) {
					Date date = (Date) value;
					return date == null ? null : date;
				}else if (type.getName().equals(java.lang.String.class.getName())) {
					String str = (String) value;
					return str == null ? "" : str;
				}else if (type.getName().equals(java.lang.Short.class.getName())) {
					Short s = (Short) value;
					return s == null ? 0 : s;
				}else if (type.getName().equals(java.lang.Integer.class.getName())) {
					Integer i = (Integer) value;
					return i == null ? 0 : i;
				}else if (type.getName().equals(java.lang.Long.class.getName())) {
					Long i = (Long) value;
					return i == null ? 0 : i;
				}else if (type.getName().equals(java.lang.Double.class.getName())) {
					Double d = (Double) value;
					return d == null ? 0.0 : d;
				}else if (type.getName().equals(java.lang.Byte.class.getName())) {
					Byte b = (Byte) value;
					return b == null ? 0 : b;
				}
	
			}
			
			return null;
			
	}
}