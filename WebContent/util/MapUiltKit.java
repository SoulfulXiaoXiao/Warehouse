package com.PersonalCollection.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapUiltKit {
	/**
	 * map的key模糊查询
	 * 
	 * @param <K>
	 * @param <V>
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> ArrayList<Object> likeString(String key, Map<K, V> map) {
		ArrayList<Object> list = new ArrayList<Object>();
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (((ArrayList<Object>) entry.getKey()).indexOf(key) != -1) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
/**
 * 举例 排序
 * @param list
 * @return
 */
	@SuppressWarnings("unchecked")
	public static <K> Map<K, List<Object>> OrderSortByGroup(List<Object> list) {
		Map<K, List<Object>> map = new HashMap<K, List<Object>>();

		for (Object object : list) {
			List<Object> staList = map.get("");// object对象中的某个值

			if (staList == null) {
				staList = new ArrayList<Object>();
			}
			Object od = new Object();

			Field[] fields = od.getClass().getDeclaredFields();
			for (Field field : fields) {
				String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				String setMethodName = "set" + field.getName().substring(0, 1).toUpperCase()
						+ field.getName().substring(1);
				Method method;
				try {
					method = object.getClass().getMethod(getMethodName);

					Object value = method.invoke(object) == null ? "" : method.invoke(object);

					Method setMethod = od.getClass().getMethod(setMethodName, new Class[] { field.getType() });
					setMethod.invoke(od, new Object[] { value });
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			staList.add(od);

			Collections.sort(staList, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					return "".compareTo(""); //// object对象中的某个值
				}
			});
			map.put((K) "", staList);
		}

		return map;

	}
}
