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
 * ���ݷ���ԭ��ʵ��javabeanȡֵ����ֵ
 * @author lenovo
 *
 */

public class BeanUtil {
/**
 * ��ȡjavabean�����е�����
 * @param model
 * @return
 * @throws NoSuchMethodException
 * @throws SecurityException
 */
	public static List<Object> fields(Object model) throws NoSuchMethodException, SecurityException {
		Field[] field = model.getClass().getDeclaredFields(); // ��ȡʵ������������ԣ�����Field����
		List<Object> list = new LinkedList<>();
		for (int j = 0; j < field.length; j++) { // ������������
			String name = field[j].getName(); // ��ȡ���Ե�����
			list.add(name);
		}
		return list;
	}
/**
 * ��ȡbean��ֵ
 * @param model
 * @return
 * @throws NoSuchMethodException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 * @throws InvocationTargetException
 */
	public static List<Object> beanValue(Object model)
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Field[] field = model.getClass().getDeclaredFields(); // ��ȡʵ������������ԣ�����Field����
		List<Object> list = new LinkedList<>();
		for (int j = 0; j < field.length; j++) { // ������������
			String name = field[j].getName(); // ��ȡ���Ե�����
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // �����Ե����ַ���д�����㹹��get��set����
			String type = field[j].getGenericType().toString(); // ��ȡ���Ե�����
			if (type.equals("class java.lang.String")) { // ���type�������ͣ���ǰ�����"class
															// "�����������
				Method m = model.getClass().getMethod("get" + name);
				String value = (String) m.invoke(model); // ����getter������ȡ����ֵ
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
 * ����ת����javabean
 * @param rs
 * @param object
 * @return
 * @throws Exception
 */
	// ���ݴ������Ķ����ResultSet�Զ�������ֵ
	@SuppressWarnings("unchecked")
	public static <T> Object getBean(ArrayList<String> rs, T object) throws Exception {
		Class<?> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();// �õ������е��ֶ�
		// ÿ��ѭ��ʱ������ʵ����һ���봫�����Ķ�������һ���Ķ���
		T objectCopy = (T) classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			Object value = null;
			// �����ֶ����;����������ʹ������get������������ȡ������
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
			// ������Ե�����ĸ��ת��Ϊ��д����setXXX��Ӧ
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });
			setMethod.invoke(objectCopy, new Object[] { value });// ���ö����setXXX����
		}
		return objectCopy;
	}
	
	
	
//	// ���ݴ������Ķ����ResultSet�Զ�������ֵ
//    public static <T> List<T>  getBean(ResultSet rs, T object) throws Exception {
//        Class<?> classType = object.getClass();
//        ArrayList<T> objList = new ArrayList<T>();
//        //SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
//        Field[] fields = classType.getDeclaredFields();//�õ������е��ֶ�
//        while (rs.next()) {
//            //ÿ��ѭ��ʱ������ʵ����һ���봫�����Ķ�������һ���Ķ���
//            T objectCopy = (T) classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
//            for (int i = 0; i < fields.length; i++) {
//                Field field = fields[i];
//                String fieldName = field.getName();
//                Object value = null;
//                //�����ֶ����;����������ʹ������get������������ȡ������
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
//                // ������Ե�����ĸ��ת��Ϊ��д����setXXX��Ӧ
//                String firstLetter = fieldName.substring(0, 1).toUpperCase();
//                String setMethodName = "set" + firstLetter
//                        + fieldName.substring(1);
//                Method setMethod = classType.getMethod(setMethodName,
//                        new Class[] { field.getType() });
//                setMethod.invoke(objectCopy, new Object[] { value });//���ö����setXXX����
//            }
//            
//            objList.add(objectCopy);
//        }
//        if(rs != null){
//            rs.close();
//        }
//        return objList;
//    }
// // ���ͷ���
//    public static <T> List<T> getBean(Class<T> clazz) throws Exception {
//        ArrayList<T> list = new ArrayList<T>();
//        Connection con = null;
//
//        // ע��JDBC������JAVA1.5�Ժ� JDBC�Զ����������� �������������ʡ�ԣ�
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//        // �ṩ��ַ�û������벢������Ӷ���
//        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
//                "root", "");
//
//        
//        // ��Connection���󴴽�PreparedStatement
//        PreparedStatement ps = con
//                .prepareStatement("select * from orderitem o where o.itemid >?");
//        // ���ò���,��������λ���Ǵ�1��ʼ��Hibernate��������λ���Ǵ�0��ʼ��
//        ps.setInt(1, 10);// ����itemid����10�ļ�¼
//        ResultSet rs = ps.executeQuery();
//        // ѭ����ȡ�������ÿһ�е�ÿһ��
//        Field[] fields = clazz.getDeclaredFields();
//        while (rs.next()) {
//            // ����޲ι�����󷽷�����ʵ��
//            T javabean = clazz.getConstructor(new Class[] {}).newInstance();
//            for (Field field : fields) {
//                String fieldName = field.getName();
//                Object value = null;
//                System.out.println(field.getType());
//                System.out.println(String.class);
//                System.out.println(Date.class);
//                // �����ֶ����;����������ʹ������get������������ȡ������
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
//                // ������Ե�����ĸ��ת��Ϊ��д����setXXX��Ӧ
//                String firstLetter = fieldName.substring(0, 1).toUpperCase();
//                String setMethodName = "set" + firstLetter
//                        + fieldName.substring(1);
//                Method setMethod = clazz.getMethod(setMethodName,
//                        new Class[] { field.getType() });
//                setMethod.invoke(javabean, new Object[] { value });// ���ö����setXXX����
//
//            }
//            list.add(javabean);
//        }
//        // �ر�
//        con.close();
//        ps.close();
//        return list;
//
//    }
}
