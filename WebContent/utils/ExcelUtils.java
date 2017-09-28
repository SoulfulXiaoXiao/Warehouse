package com.PersonalCollection.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @ClassName: ExcelUtil
 * @Description: Excel���뵼��������
 * @author xiao
 * @date 2017-8-1 00:00:00
 */
public class ExcelUtils {
	private static final Logger logger = Logger.getLogger(ExcelUtils.class);

	/**
	 * @Title: createWorkbook @Description:
	 * �ж�excel�ļ���׺�������ɲ�ͬ��workbook @param @param is @param @param
	 * excelFileName @param @return @param @throws IOException @return
	 * Workbook @throws
	 */
	public Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
		if (excelFileName.endsWith(".xls")) {
			return new HSSFWorkbook(is);
		} else if (excelFileName.endsWith(".xlsx")) {
			return new XSSFWorkbook(is);
		}
		return null;
	}

	/**
	 * @Title: getSheet @Description: ����sheet�����Ż�ȡ��Ӧ��sheet @param @param
	 * workbook @param @param sheetIndex @param @return @return Sheet @throws
	 */
	public Sheet getSheet(Workbook workbook, int sheetIndex) {
		return workbook.getSheetAt(0);
	}

	/**
	 * @Title: importDataFromExcel @Description: ��sheet�е����ݱ��浽list�У�
	 * 1�����ô˷���ʱ��vo�����Ը��������excel�ļ�ÿ�����ݵ�������ͬ��һһ��Ӧ��vo���������Զ�ΪString
	 * 2����action���ô˷���ʱ�������� private File excelFile;�ϴ����ļ� private String
	 * excelFileName;ԭʼ�ļ����ļ��� 3��ҳ���file�ؼ�name���ӦFile���ļ��� @param @param vo
	 * javaBean @param @param is ������ @param @param
	 * excelFileName @param @return @return List<Object> @throws
	 */
	public List<Object> importDataFromExcel(Object vo, InputStream is, String excelFileName) {
		List<Object> list = new ArrayList<Object>();
		try {
			// ����������
			Workbook workbook = this.createWorkbook(is, excelFileName);
			// ����������sheet
			Sheet sheet = this.getSheet(workbook, 0);
			// ��ȡsheet�����ݵ�����
			int rows = sheet.getPhysicalNumberOfRows();
			// ��ȡ��ͷ��Ԫ�����
			int cells = sheet.getRow(0).getPhysicalNumberOfCells();
			// ���÷��䣬��JavaBean�����Խ��и�ֵ
			Field[] fields = vo.getClass().getDeclaredFields();
			for (int i = 1; i < rows; i++) {// ��һ��Ϊ���������ӵڶ��п�ʼȡ����
				Row row = sheet.getRow(i);
				int index = 0;
				while (index < cells) {
					Cell cell = row.getCell(index);
					if (null == cell) {
						cell = row.createCell(index);
					}
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String value = null == cell.getStringCellValue() ? "" : cell.getStringCellValue();

					Field field = fields[index];
					String fieldName = field.getName();
					String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method setMethod = vo.getClass().getMethod(methodName, new Class[] { String.class });
					setMethod.invoke(vo, new Object[] { value });
					index++;
				}
				System.out.println(vo.toString());
				System.out.println(isHasValues(vo));
				if (!isHasValues(vo)) {// �ж϶��������Ƿ���ֵ
					list.add(vo);
					vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});// ���´���һ��vo����
					System.out.println(vo);
				}

			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				is.close();// �ر���
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return list;

	}

	/**
	 * @Title: isHasValues @Description:
	 * �ж�һ���������������Ƿ���ֵ�����һ��������ֵ(�ֿ�)���򷵻�true @param @param
	 * object @param @return @return boolean @throws
	 */
	public boolean isHasValues(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		boolean flag = false;
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getMethod;
			try {
				getMethod = object.getClass().getMethod(methodName);
				Object obj = getMethod.invoke(object);
				if (null != obj && "".equals(obj)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
				logger.error(e);
			}

		}
		return flag;

	}

	public <T> void exportDataToExcel(List<T> list, String[] headers, String title, OutputStream os) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// ����һ�����
		HSSFSheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п�15���ֽ�
		sheet.setDefaultColumnWidth(15);
		// ����һ����ʽ
		HSSFCellStyle style = this.getCellStyle(workbook);
		// ����һ������
		HSSFFont font = this.getFont(workbook);
		// ������Ӧ�õ���ǰ��ʽ
		style.setFont(font);

		// ���ɱ�����
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 300);
		HSSFCell cell = null;

		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// �����ݷ���sheet��
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			T t = list.get(i);
			// ���÷��䣬����JavaBean���Ե��Ⱥ�˳�򣬶�̬����get�����õ����Ե�ֵ
			// Field[] fields = t.getClass().getFields();
			Field[] fields = t.getClass().getDeclaredFields();
			try {
				for (int j = 0; j < fields.length; j++) {
					cell = row.createCell(j);
					Field field = fields[j];
					String fieldName = field.getName();
					String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method getMethod = t.getClass().getMethod(methodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});

					if (null == value)
						value = "";
					cell.setCellValue(value.toString());
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}

		try {
			workbook.write(os);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	/**
	 * @Title: getCellStyle @Description: ��ȡ��Ԫ���ʽ @param @param
	 * workbook @param @return @return HSSFCellStyle @throws
	 */
	public HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		return style;
	}

	/**
	 * @Title: getFont @Description: ����������ʽ @param @param
	 * workbook @param @return @return HSSFFont @throws
	 */
	public HSSFFont getFont(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.WHITE.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		return font;
	}

	public boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 ? true : false;
	}
}