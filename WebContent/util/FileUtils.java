package com.PersonalCollection.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.PersonalCollection.utils.Prop;

/**
 * @ClassName: UploadHandleServlet
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: 孤傲苍狼
 * @date: 2015-1-3 下午11:35:50
 *
 */
public class FileUtils {

	private String fileName;// 文件名字
	private String fileParent;// 上级目录
	private String filePath;// 相对路径
	private String fileAbsolutePath;// 绝对路径
	private long fileLength;// 文件长度
	private long fileLastModified;// 最后修改时间
	private boolean fileCanRead;// 是否可读
	private boolean fileCanWrite;// 是否可写
	private boolean fileIsAbsolute;// 是否为绝对路径
	private boolean fileIsDirectory;// 是否为目录
	private boolean fileIsFile;// 是否为文件
	private boolean fileIsHidden;// 是否为隐藏文件
	private boolean fileExists;// 是否存在

	private String fileLastModifiedData;// 最后修改时间

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileParent() {
		return fileParent;
	}

	public void setFileParent(String fileParent) {
		this.fileParent = fileParent;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public long getFileLastModified() {
		return fileLastModified;
	}

	public void setFileLastModified(long fileLastModified) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(fileLastModified);
		String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
		this.fileLastModifiedData = sDateTime;
		this.fileLastModified = fileLastModified;
	}

	public boolean isFileCanRead() {
		return fileCanRead;
	}

	public void setFileCanRead(boolean fileCanRead) {
		this.fileCanRead = fileCanRead;
	}

	public boolean isFileCanWrite() {
		return fileCanWrite;
	}

	public void setFileCanWrite(boolean fileCanWrite) {
		this.fileCanWrite = fileCanWrite;
	}

	public boolean isFileIsAbsolute() {
		return fileIsAbsolute;
	}

	public void setFileIsAbsolute(boolean fileIsAbsolute) {
		this.fileIsAbsolute = fileIsAbsolute;
	}

	public boolean isFileIsDirectory() {
		return fileIsDirectory;
	}

	public void setFileIsDirectory(boolean fileIsDirectory) {
		this.fileIsDirectory = fileIsDirectory;
	}

	public boolean isFileIsFile() {
		return fileIsFile;
	}

	public void setFileIsFile(boolean fileIsFile) {
		this.fileIsFile = fileIsFile;
	}

	public boolean isFileIsHidden() {
		return fileIsHidden;
	}

	public void setFileIsHidden(boolean fileIsHidden) {
		this.fileIsHidden = fileIsHidden;
	}

	public boolean isFileExists() {
		return fileExists;
	}

	public void setFileExists(boolean fileExists) {
		this.fileExists = fileExists;
	}

	public String getFileLastModifiedData() {
		return fileLastModifiedData;
	}

	public void setFileLastModifiedData(String fileLastModifiedData) {
		this.fileLastModifiedData = fileLastModifiedData;
	}

	/**
	 * @author xx
	 * @param filepath
	 *            路径
	 * @return 集合
	 */

	public List<Object> readfiles(String filepath) {
		FileUtils vo = new FileUtils();
		List<Object> list = new ArrayList<>();
		return new Thread() {
			// 可以改成一个方法
			private List<Object> readfile(String filepath, FileUtils vo, List<Object> list) {
				try {
					File file = new File(filepath);
					if (!file.isDirectory()) {
						vo.setFile(vo, file);
						list.add(vo);
						// 对象属性中值清空
						vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
					} else if (file.isDirectory()) {
						String[] filelist = file.list();
						for (int i = 0; i < filelist.length; i++) {
							File readfile = new File(filepath + File.separator + filelist[i]);
							if (!readfile.isDirectory()) {
								vo = setFile(vo, readfile);
								list.add(vo);
								vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
							} else if (readfile.isDirectory()) {
								vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
								readfile(filepath + File.separator + filelist[i], vo, list);
							}
						}
					}
				} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException e) {
					System.out.println("readfile()   Exception:" + e.getMessage());
				}
				return list;
			}
		}.readfile(filepath, vo, list);

	}

	private FileUtils setFile(FileUtils vo, File file) {
		vo.setFileName(file.getName());
		vo.setFileParent(file.getParent());
		vo.setFilePath(file.getPath());
		vo.setFileAbsolutePath(file.getAbsolutePath());
		vo.setFileLength(file.length());
		vo.setFileLastModified(file.lastModified());
		vo.setFileCanRead(file.canRead());
		vo.setFileCanWrite(file.canWrite());
		vo.setFileIsAbsolute(file.isAbsolute());
		vo.setFileIsDirectory(file.isDirectory());
		vo.setFileIsFile(file.isFile());
		vo.setFileIsHidden(file.isHidden());
		vo.setFileExists(file.exists());
		return vo;

	}

	// public List<Object> readfiles1(String filepath) {
	// File file = new File(filepath);
	// List<Object> list = new ArrayList<>();
	// FileUtils vo = new FileUtils();
	// List<Object> traverseFolder = traverseFolder(file,vo,list);
	//
	// return traverseFolder;
	// }
	//
	//
	// private List<Object> traverseFolder(File file, FileUtils vo, List<Object>
	// list){
	// if (!file.isDirectory()) {
	// try {
	// vo.setFileName(file.getName());
	// vo.setFilePath(file.getAbsolutePath());
	// vo.setFileSize(file.getTotalSpace());
	// list.add(vo);
	// vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new
	// Object[] {});
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// else if (file.isDirectory()) {
	// File[] fileArray = file.listFiles();
	// if (fileArray.length == 0) {
	// System.out.println("文件夹是空的!");
	// return list;
	// }
	// else {
	// for (File files : fileArray) {
	// if (files.isDirectory()) {
	// try {
	// vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new
	// Object[] {});
	// } catch (InstantiationException | IllegalAccessException |
	// IllegalArgumentException
	// | InvocationTargetException | NoSuchMethodException | SecurityException
	// e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// traverseFolder(files, vo, list);
	// } else {
	// try {
	// vo.setFileName(files.getName());
	// vo.setFilePath(files.getAbsolutePath());
	// vo.setFileSize(files.getTotalSpace());
	// list.add(vo);
	// vo = vo.getClass().getConstructor(new Class[] {}).newInstance(new
	// Object[] {});
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.out.println("文件:" + file.getAbsolutePath());
	// }
	// }
	// }
	// } else {
	// System.out.println("文件不存在!");
	// }
	// return list;
	// }
	//

	/**
	 * @param file
	 ************************************************************************************************************/

	@SuppressWarnings("unused")
	private HashMap<Object, Object> extMap() {
		HashMap<Object, Object> extMap = new HashMap<>();
		extMap.put("images", "gif,jpg,jpeg,png,bmp");
		extMap.put("flashs", "swf,flv");
		extMap.put("medias", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("files", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		Iterator<?> iter = extMap.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
		}
		String aa = "dfsdfsdf";
		String[] ext = aa.split(",");
		return extMap;
	}

	/*****************************************************************************************/

	/**
	 * 上传单一文件
	 * 
	 * @author xx
	 * @param file
	 *            文件
	 * @return 储存路径
	 */
	public String upload(MultipartFile file) {
		try {
			String filename = file.getOriginalFilename();// 得到上传的文件名称，
			InputStream in = file.getInputStream();// 获取item中的上传文件的输入流
			String realSavePath = uploadFile(filename, in);
			return realSavePath;
		} catch (Exception e) {
			e.printStackTrace();// message = "文件上传失败！";
		}
		return fileAbsolutePath;
	}

	/**
	 * 上传多个文件
	 * 
	 * @param file
	 * @param request
	 * @return 储存路径
	 * @throws ServletException
	 * @throws IOException
	 * @throws FileUploadBase.FileSizeLimitExceededException
	 * @throws FileUploadBase.SizeLimitExceededException
	 */
	public List<String> upload(MultipartFile[] file) {
		// 消息提示
		try {
			List<String> list = new ArrayList<String>();
			for (MultipartFile item : file) {
				String filename = item.getOriginalFilename();// 得到上传的文件名称，
				InputStream in = item.getInputStream();// 获取item中的上传文件的输入流
				String realSavePath = uploadFile(filename, in);
				list.add(realSavePath);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传时生成的保存文件保存目录
	 * 
	 * @author xx
	 * @return 文件(File)
	 */
	private File savePath() {
		Prop prop = new Prop("fileInfo.properties", "UTF-8");// 上传时生成的保存文件保存目录
		// String savePath = prop.get("savePath");// String savePath
		// ="D:/uploadfile/upload";
		// if (null == savePath || "".equals(savePath)) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String savePath = request.getSession().getServletContext().getRealPath(prop.get("savePath"));//"/WEB-INF/upload"
		// }
		File saveFile = new File(savePath);
		// 上传时生成的临时文件保存目录
		if (!saveFile.exists()) {
			saveFile.mkdir();// 创建保存目录
		}
		return saveFile;
	}

	/**
	 * 上传时生成的临时文件保存目录
	 * 
	 * @author xx
	 * @return 文件(File)
	 */
	private File tmpFile() {
		Prop prop = new Prop("fileInfo.properties", "UTF-8");// 上传时生成的临时文件保存目录
		// String tempPath = prop.get("tempPath");// String tmpPath =
		// "D:/uploadfile/temp";
		// if (null == tempPath || "".equals(tempPath)) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String tempPath = request.getSession().getServletContext().getRealPath(prop.get("tempPath"));//"/WEB-INF/temp"
		// }
		File tempFile = new File(tempPath);
		// 上传时生成的临时文件保存目录
		if (!tempFile.exists()) {
			// 创建保存目录
			tempFile.mkdir();
		}
		return tempFile;
	}

	/**
	 * 文件上传
	 * 
	 * @author xx
	 * @param filename上传文件名字
	 * @param in文件输入流
	 * @return 储存路径
	 */
	private String uploadFile(String filename, InputStream in) {
		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
			factory.setSizeThreshold(1024 * 100);// 设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
			// 设置上传时生成的临时文件的保存目录
			factory.setRepository(tmpFile());
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 监听文件上传进度
			upload.setProgressListener(new ProgressListener() {

				public void update(long pBytesRead, long pContentLength, int arg2) {
					System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
					/**
					 * 文件大小为：14608,当前已处理：4096 文件大小为：14608,当前已处理：7367
					 * 文件大小为：14608,当前已处理：11419 文件大小为：14608,当前已处理：14608
					 */
				}
			});
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
			upload.setFileSizeMax(1024 * 1024);
			// 设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
			upload.setSizeMax(1024 * 1024 * 10);
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项

			// System.out.println(filename);
			if (filename == null || filename.trim().equals("")) {
				return null;
			}
			// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
			// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
			// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
			filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
			// 得到上传文件的扩展名
			// String fileExtName = filename.substring(filename.lastIndexOf(".")
			// + 1);
			/*****/ // 如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
					// System.out.println("上传的文件的扩展名是：" + fileExtName);
					// 得到文件保存的名称
			String saveFilename = makeFileName(filename);
			// 得到文件的保存目录
			String realSavePath = makePath(saveFilename, savePath().getPath());
			// 得到文件的保存目录和名字组合
			String filePathName = realSavePath + File.separator + saveFilename;
			// 创建一个文件输出流
			FileOutputStream out = new FileOutputStream(filePathName);
			// 创建一个缓冲区
			byte buffer[] = new byte[1024];
			// 判断输入流中的数据是否已经读完的标识
			int len = 0;
			// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
			while ((len = in.read(buffer)) > 0) {
				// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" +
				// filename)当中
				out.write(buffer, 0, len);
			}
			// 关闭输入流
			in.close();
			// 关闭输出流
			out.close();
			System.out.println(realSavePath + File.separator + saveFilename);
			return savePath().getPath() + File.separator + saveFilename;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Method: makeFileName
	 * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
	 * @Anthor:xx
	 * @param filename
	 *            文件的原始名称
	 * @return uuid+"_"+文件的原始名称
	 */
	private String makeFileName(String filename) { // 2.jpg
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString().replace("-", "").toUpperCase() + "_" + filename;
	}

	/**
	 * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	 * 
	 * @Method: makePath
	 * @Description:
	 * @Anthor:孤傲苍狼
	 *
	 * @param filename
	 *            文件名，要根据文件名生成存储目录
	 * @param savePath
	 *            文件存储路径
	 * @return 新的存储目录
	 */
	private String makePath(String filename, String savePath) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录
		String dir = savePath + File.separator + dir1 + File.separator + dir2; // upload\2\3
		// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}

	/**
	 * 文件下载功能
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	public void down(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 模拟文件，myfile.txt为需要下载的文件
		String fileName = request.getSession().getServletContext().getRealPath("upload") + "/myfile.txt";
		// 获取输入流
		InputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
		// 假如以中文名下载的话
		String filename = "下载文件.txt";
		// 转码，免得文件名中文乱码
		filename = URLEncoder.encode(filename, "UTF-8");
		// 设置文件下载头
		response.addHeader("Content-Disposition", "attachment;filename=" + filename);
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		int len = 0;
		while ((len = bis.read()) != -1) {
			out.write(len);
			out.flush();
		}
		out.close();
		bis.close();
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 */

	/*
	 * public static boolean deletefile(String delpath) throws
	 * FileNotFoundException, IOException { try {
	 * 
	 * File file = new File(delpath); if (!file.isDirectory()) {
	 * System.out.println("1"); file.delete(); } else if (file.isDirectory()) {
	 * System.out.println("2"); String[] filelist = file.list(); for (int i = 0;
	 * i < filelist.length; i++) { File delfile = new File(delpath + "\\" +
	 * filelist[i]); if (!delfile.isDirectory()) { System.out.println("path=" +
	 * delfile.getPath()); System.out.println("absolutepath=" +
	 * delfile.getAbsolutePath()); System.out.println("name=" +
	 * delfile.getName()); delfile.delete(); System.out.println("删除文件成功"); }
	 * else if (delfile.isDirectory()) { deletefile(delpath + "\\" +
	 * filelist[i]); } } file.delete();
	 * 
	 * }
	 * 
	 * } catch (FileNotFoundException e) { System.out.println(
	 * "deletefile()   Exception:" + e.getMessage()); } return true; }
	 */

	// public static void main(String[] args) {
	// try {
	// readfile("e:/videos");
	// // deletefile("D:/file");
	// } catch (FileNotFoundException ex) {
	// } catch (IOException ex) {
	// }
	// System.out.println("ok");
	// }
}
