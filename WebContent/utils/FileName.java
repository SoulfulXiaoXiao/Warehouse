package com.PersonalCollection.utils;

import java.io.File;

public class FileName {
	static String fName = " G:\\Java_Source\\navigation_tigra_menu\\demo1\\img\\lev1_arrow.gif ";

	public String fileName(String fName) {
		File tempFile = new File(fName.trim());
		String fileName = tempFile.getName();
		return fileName;
	}

	// 方法一：
	public void fileName1() {
		File tempFile = new File(fName.trim());
		String fileName = tempFile.getName();
		System.out.println("fileName = " + fileName);
	}

	// 方法二：
	public void fileName2() {
		String fileName = fName.trim();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		// 或者
		// String fileName = fName.substring(fName.lastIndexOf("\\")+1);
		System.out.println("fileName = " + fileName);
	}

	// 方法三：
	public void fileName3() {
		String fileName = fName.trim();
		String temp[] = fileName
				.split("\\\\"); /** split里面必须是正则表达式，"\\"的作用是对字符串转义 */
		fileName = temp[temp.length - 1];
		System.out.println("fileName = " + fileName);
	}
}
