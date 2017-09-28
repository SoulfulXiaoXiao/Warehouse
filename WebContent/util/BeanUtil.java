package com.PersonalCollection.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
/**
 * 根据反射原理实现javabean取值和设值
 * @author lenovo
 *
 */

public class BeanUtil {
/**
 * 获取javabean中所有的属性
 * @param model
 * @return
 * @throws NoSuchMethodException
 * @throws SecurityException
 */
	public static List<Object> fields(Object model) throws NoSuchMethodException, SecurityException {
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		List<Object> list = new LinkedList<>();
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String name = field[j].getName(); // 获取属性的名字
			list.add(name);
		}
		return list;
	}
/**
 * 获取bean的值
 * @param model
 * @return
 * @throws NoSuchMethodException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 * @throws InvocationTargetException
 */
	public static List<Object> beanValue(Object model)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		List<Object> list = new LinkedList<>();
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String name = field[j].getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class
															// "，后面跟类名
				Method m = model.getClass().getMethod("get" + name);
				String value = (String) m.invoke(model); // 调用getter方法获取属性值
				if (value != null) {
					list.add(value);
				}
			}
			if (type.equals("class java.lang.Integer")) {
				Method m = model.getClass().getMethod("get" + name);
				Integer value = (Integer) m.invoke(model);
				if (value != null) {
					list.add(value);
				}
			}
			if (type.equals("class java.lang.Short")) {
				Method m = model.getClass().getMethod("get" + name);
				Short value = (Short) m.invoke(model);
				if (value != null) {
					list.add(value);
				}
			}
			if (type.equals("class java.lang.Double")) {
				Method m = model.getClass().getMethod("get" + name);
				Double value = (Double) m.invoke(model);
				if (value != null) {
					list.add(value);
				}
			}
			if (type.equals("class java.lang.Boolean")) {
				Method m = model.getClass().getMethod("get" + name);
				Boolean value = (Boolean) m.invoke(model);
				if (value != null) {
					String cc = value == true ? "1" : "0";
					list.add(cc);
				}
			}
			if (type.equals("class java.util.Date")) {
				Method m = model.getClass().getMethod("get" + name);
				Date value = (Date) m.invoke(model);
				if (value != null) {
					list.add(value);
					System.out.println(type + "attribute value:" + value.toString());
				}
			}
		}
		return list;
	}
/**
 * 数组转换成javabean
 * @param rs
 * @param object
 * @return
 * @throws Exception
 */
	// 根据传过来的对象和ResultSet自动给对象赋值
	@SuppressWarnings("unchecked")
	public static <T> Object getBean(ArrayList<String> rs, T object) throws Exception {
		Class<?> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();// 得到对象中的字段
		// 每次循环时，重新实例化一个与传过来的对象类型一样的对象
		T objectCopy = (T) classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			Object value = null;
			// 根据字段类型决定结果集中使用哪种get方法从数据中取到数据
			if (field.getType().equals(String.class)) {
				value = rs.get(i);
				if (value == null) {
					value = "";
				}
			}
			if (field.getType().equals(int.class)) {
				value = Integer.parseInt(rs.get(i));
			}
			if (field.getType().equals(java.util.Date.class)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
				value = sdf.parse(rs.get(i));
			}
			// 获得属性的首字母并转换为大写，与setXXX对应
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });
			setMethod.invoke(objectCopy, new Object[] { value });// 调用对象的setXXX方法
		}
		return objectCopy;
	}
	
	
	
//	// 根据传过来的对象和ResultSet自动给对象赋值
//    public static <T> List<T>  getBean(ResultSet rs, T object) throws Exception {
//        Class<?> classType = object.getClass();
//        ArrayList<T> objList = new ArrayList<T>();
//        //SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
//        Field[] fields = classType.getDeclaredFields();//得到对象中的字段
//        while (rs.next()) {
//            //每次循环时，重新实例化一个与传过来的对象类型一样的对象
//            T objectCopy = (T) classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
//            for (int i = 0; i < fields.length; i++) {
//                Field field = fields[i];
//                String fieldName = field.getName();
//                Object value = null;
//                //根据字段类型决定结果集中使用哪种get方法从数据中取到数据
//                if (field.getType().equals(String.class)) {
//                    value = rs.getString(fieldName);
//                    if(value==null){
//                        value="";
//                    }
//                }
//                if (field.getType().equals(int.class)) {
//                    value = rs.getInt(fieldName);
//                }
//                if (field.getType().equals(Java.util.Date.class)) {
//                    value = rs.getDate(fieldName);
//                }
//                // 获得属性的首字母并转换为大写，与setXXX对应
//                String firstLetter = fieldName.substring(0, 1).toUpperCase();
//                String setMethodName = "set" + firstLetter
//                        + fieldName.substring(1);
//                Method setMethod = classType.getMethod(setMethodName,
//                        new Class[] { field.getType() });
//                setMethod.invoke(objectCopy, new Object[] { value });//调用对象的setXXX方法
//            }
//            
//            objList.add(objectCopy);
//        }
//        if(rs != null){
//            rs.close();
//        }
//        return objList;
//    }
// // 泛型方法
//    public static <T> List<T> getBean(Class<T> clazz) throws Exception {
//        ArrayList<T> list = new ArrayList<T>();
//        Connection con = null;
//
//        // 注册JDBC驱动，JAVA1.5以后 JDBC自动加载驱动了 所以这句代码可以省略；
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//        // 提供地址用户名密码并获得连接对象
//        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
//                "root", "");
//
//        
//        // 有Connection对象创建PreparedStatement
//        PreparedStatement ps = con
//                .prepareStatement("select * from orderitem o where o.itemid >?");
//        // 设置参数,参数索引位置是从1开始（Hibernate参数索引位置是从0开始）
//        ps.setInt(1, 10);// 过滤itemid大于10的记录
//        ResultSet rs = ps.executeQuery();
//        // 循环读取结果集的每一行的每一列
//        Field[] fields = clazz.getDeclaredFields();
//        while (rs.next()) {
//            // 获得无参构造对象方法创建实例
//            T javabean = clazz.getConstructor(new Class[] {}).newInstance();
//            for (Field field : fields) {
//                String fieldName = field.getName();
//                Object value = null;
//                System.out.println(field.getType());
//                System.out.println(String.class);
//                System.out.println(Date.class);
//                // 根据字段类型决定结果集中使用哪种get方法从数据中取到数据
//                if ((String.class).equals(field.getType())) {
//                    value = rs.getString(fieldName);
//                    if (value == null) {
//                        value = "";
//                    }
//                }
//
//                if (field.getType().equals(int.class)) {
//                    value = rs.getInt(fieldName);
//                }
//                if (field.getType().equals(Date.class)) {
//                    value = rs.getDate(fieldName);
//                }
//
//                // 获得属性的首字母并转换为大写，与setXXX对应
//                String firstLetter = fieldName.substring(0, 1).toUpperCase();
//                String setMethodName = "set" + firstLetter
//                        + fieldName.substring(1);
//                Method setMethod = clazz.getMethod(setMethodName,
//                        new Class[] { field.getType() });
//                setMethod.invoke(javabean, new Object[] { value });// 调用对象的setXXX方法
//
//            }
//            list.add(javabean);
//        }
//        // 关闭
//        con.close();
//        ps.close();
//        return list;
//
//    }
}
