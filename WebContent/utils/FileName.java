package com.PersonalCollection.utils;

import java.io.File;

public class FileName {
	static String fName = " G:\\Java_Source\\navigation_tigra_menu\\demo1\\img\\lev1_arrow.gif ";

	public String fileName(String fName) {
		File tempFile = new File(fName.trim());
		String fileName = tempFile.getName();
		return fileName;
	}

	// ����һ��
	public void fileName1() {
		File tempFile = new File(fName.trim());
		String fileName = tempFile.getName();
		System.out.println("fileName = " + fileName);
	}

	// ��������
	public void fileName2() {
		String fileName = fName.trim();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		// ����
		// String fileName = fName.substring(fName.lastIndexOf("\\")+1);
		System.out.println("fileName = " + fileName);
	}

	// ��������
	public void fileName3() {
		String fileName = fName.trim();
		String temp[] = fileName
				.split("\\\\"); /** split���������������ʽ��"\\"�������Ƕ��ַ���ת�� */
		fileName = temp[temp.length - 1];
		System.out.println("fileName = " + fileName);
	}
}
