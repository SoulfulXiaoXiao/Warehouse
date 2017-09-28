

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import com.PersonalCollection.utils.Prop;

/**
 * @Description:上传，下载，获取全部文件
 * @ClassName:FileUtils.java
 * @author:�?
 * @date:2017�?8�?26�? 上午11:25:51
 */
public class FileUtils {

	private String fileName;// 文件名字
	private String fileParent;// 上级目录
	private String filePath;// 相对路径
	private String fileAbsolutePath;// 绝对路径
	private long fileLength;// 文件长度
	private long fileLastModified;// �?后修改时�?
	private boolean fileCanRead;// 是否可读
	private boolean fileCanWrite;// 是否可写
	private boolean fileIsAbsolute;// 是否为绝对路�?
	private boolean fileIsDirectory;// 是否为目�?
	private boolean fileIsFile;// 是否为文�?
	private boolean fileIsHidden;// 是否为隐藏文�?
	private boolean fileExists;// 是否存在

	private String fileLastModifiedData;// �?后修改时�?

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

	public List<Object> readfiles(String filepath) {
		FileUtils vo = new FileUtils();
		List<Object> list = new ArrayList<>();
		return new Thread() {
			// 可以改成�?个方�?
			private List<Object> readfile(String filepath, FileUtils vo, List<Object> list) {
				try {
					File file = new File(filepath);
					if (!file.isDirectory()) {
						vo.setFile(vo, file);
						list.add(vo);
						// 对象属�?�中值清�?
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

	/**
	 * @Method uploadFile
	 * @Description 多个文件上传
	 * @param file(
	 *            MultipartFile[])
	 * @return list的集�?(文件的径)
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:38:05
	 */
	public List<String> uploadFile(MultipartFile[] file) {
		try {
			List<String> list = new ArrayList<String>();
			for (MultipartFile item : file) {
				String realSavePath = uploadFile(item);
				list.add(realSavePath);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Method uploadFile
	 * @Description 单一文件上传
	 * @param file(MultipartFile)
	 * @return 文件路径
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:39:24
	 */
	public String uploadFile(MultipartFile file) {
		String filename = file.getOriginalFilename();// 得到上传的文件名称，

		HttpServletRequest request = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			Prop prop = new Prop("fileInfo.properties", "UTF-8");// 上传时生成的临时文件保存目录
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			// 上传时生成的临时文件保存目录
			String tempPath = request.getSession().getServletContext().getRealPath(prop.get("tempPath"));// "/WEB-INF/temp"
			// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安�?
			String savePath = request.getSession().getServletContext().getRealPath(prop.get("savePath"));// "/WEB-INF/upload"
			
			
			File tmpFile = new File(tempPath);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();// 创建临时目录
			}
			File saveFile = new File(savePath);
			if (!saveFile.exists()) {
				saveFile.mkdir();// 创建临时目录
			}
			// 使用Apache文件上传组件处理文件上传步骤�?
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成�?个临时文件存放到指定的临时目录当中�??
			factory.setSizeThreshold(1024 * 100);// 设置缓冲区的大小�?100KB，如果不指定，那么缓冲区的大小默认是10KB
			factory.setRepository(tmpFile);// 设置上传时生成的临时文件的保存目�?
			ServletFileUpload upload = new ServletFileUpload(factory);// 2、创建一个文件上传解析器
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
			// 设置上传单个文件的大小的�?大�?�，目前是设置为1024*1024字节，也就是1MB
			upload.setFileSizeMax(1024 * 1024);
			// 设置上传文件总量的最大�?�，�?大�??=同时上传的多个文件的大小的最大�?�的和，目前设置�?10MB
			upload.setSizeMax(1024 * 1024 * 10);
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是�?个List<FileItem>集合，每�?个FileItem对应�?个Form表单的输入项
			if (filename == null || filename.trim().equals("")) {
				return null;
			}
			// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
			filename = filename.substring(filename.lastIndexOf(File.separator) + 1);

			/*
			 * 如果�?要限制上传的文件类型，那么可以�?�过文件的扩展名来判断上传的文件类型是否合法 String ext =
			 * filename.substring(filename.lastIndexOf(".")+1);
			 * if(!types.contains(ext)){ request.setAttribute("message",
			 * "本系统不支持" + ext + "这种类型");
			 * request.getRequestDispatcher("/message.jsp").forward(request,
			 * response); return; }
			 */

			String saveFilename = makeFileName(filename);// 得到文件保存的名�?
			String realSavePath = makePath(saveFilename, savePath);// 得到文件的保存目�?
			String filePathName = realSavePath + File.separator + saveFilename;// 得到文件的保存目录和名字组合
			is = file.getInputStream();// 获取文件�?
			os = new FileOutputStream(filePathName);// 创建�?个文件输出流
			this.getIoStream(is, os);
			String path = filePathName.substring(request.getSession().getServletContext().getRealPath("/").length(),
					filePathName.length());
			System.out.println(path);
			return path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os.flush();
					os = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("message", "上传成功！！");
		return null;
	}

	/**
	 * @Method downloadFile
	 * @Description 文件下载
	 * @param path
	 *            文件路径
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:42:38
	 */

	public void downloadFile(String path, HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		InputStream is = null;
		try {
			// String str=new String(path.getBytes("ISO-8859-1"),"UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath(path);// �?要下载的文件,要下载文件的磁盘全路�?
			 filepath=new String(filepath.getBytes("ISO-8859-1"),"UTF-8");
//			filepath = URLEncoder.encode(filepath, "UTF-8");
			// get参数，中文需要还原字节码重新解码成UTF-8
			String filename = substringFileName(path);// 假如以中文名下载的话(截掉UUID)
			filename = URLEncoder.encode(filename, "UTF-8");// 转码，免得文件名中文乱码
			File file = new File(filepath);
			if (!file.exists() || !file.isFile()) {
				request.setAttribute("message", "对不起，您要下载的资源已被删�?");
				response.sendError(404, "目标文件不存在，或已删除。for " + path);
				return;
			}
			/* 设置文件下载�? */

			// 判断 是否要需�?304
			String chkkey1 = request.getHeader("If-Modified-Since");
			String chkkey2 = request.getHeader("If-None-Match");

			String ssv1 = this.getGMT(file.lastModified());
			String ssv2 = String.valueOf(file.lastModified());
			if (ssv1.equals(chkkey1)) {
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			if (ssv2.equals(chkkey2)) {
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			String mime = getFileMimeType(file);
			response.setContentType(mime);// 不在浏览器打�?，下载本�?

			response.setHeader("Last-Modified", this.getGMT(file.lastModified()));// 浏览器缓�?

			long adddays = 8640000;// 100�?
			response.setHeader("Cache-Control", "max-age=" + adddays);// 单位�? 100
																		// * 24
																		// *
																		// 3600
			response.setHeader("Expires", this.getGMT(file.lastModified() + adddays * 1000));

			response.setHeader("ETag", String.valueOf(file.lastModified()));/// ETag缓存

			// 通知浏览器以下载打开
			// 文件名需要用URLEncoder编码
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类�?
			response.setContentType("multipart/form-data");

			is = new BufferedInputStream(new FileInputStream(file));// 获取输入�?
			os = new BufferedOutputStream(response.getOutputStream());// 获取文件�?

			this.getIoStream(is, os);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os.flush();
					os = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Method downloadFileToWeb
	 * @Description 图片下载游览器中
	 * @param path
	 *            文件路径
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:43:56
	 */
	public void downloadFileToWeb(String path, HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		InputStream is = null;
		try {
			// String str=new String(path.getBytes("ISO-8859-1"),"UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath(path);// �?要下载的文件,要下载文件的磁盘全路�?
			filepath = URLEncoder.encode(filepath, "UTF-8");
			// get参数，中文需要还原字节码重新解码成UTF-8
			String filename = substringFileName(path);// 假如以中文名下载的话(截掉UUID)
			filename = URLEncoder.encode(filename, "UTF-8");// 转码，免得文件名中文乱码
			File file = new File(filepath);
			if (!file.exists() || !file.isFile()) {
				request.setAttribute("message", "对不起，您要下载的资源已被删�?");
				response.sendError(404, "目标文件不存在，或已删除。for " + path);
				return;
			}
			/* 设置文件下载�? */

			// 判断 是否要需�?304
			String chkkey1 = request.getHeader("If-Modified-Since");
			String chkkey2 = request.getHeader("If-None-Match");

			String ssv1 = this.getGMT(file.lastModified());
			String ssv2 = String.valueOf(file.lastModified());
			if (ssv1.equals(chkkey1)) {
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			if (ssv2.equals(chkkey2)) {
				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			response.setContentType(MIME_DOWNLOAD);// 是否直接在浏览器中显�?
			response.setHeader("Last-Modified", this.getGMT(file.lastModified()));// 浏览器缓�?

			long adddays = 8640000;// 100�?
			response.setHeader("Cache-Control", "max-age=" + adddays);// 单位�? 100
																		// * 24
																		// *
																		// 3600
			response.setHeader("Expires", this.getGMT(file.lastModified() + adddays * 1000));
			response.setHeader("ETag", String.valueOf(file.lastModified()));/// ETag缓存

			// 通知浏览器以下载打开
			// 文件名需要用URLEncoder编码
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类�?
			response.setContentType("multipart/form-data");

			is = new BufferedInputStream(new FileInputStream(file));// 获取输入�?
			os = new BufferedOutputStream(response.getOutputStream());// 获取文件�?

			this.getIoStream(is, os);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os.flush();
					os = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文�?
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

	/**
	 * @Method getIoStream
	 * @Description
	 * @param 输入流InputStream
	 * @param os
	 *            输出流OutputStream
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:55:09
	 */
	private void getIoStream(InputStream is, OutputStream os) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);

			// 创建�?个缓冲区
			byte buffer[] = new byte[1024];
			// 判断输入流中的数据是否已经读完的标识
			int len = 0;
			// 循环将输入流读入到缓冲区当中�?(len=in.read(buffer))>0就表示in里面还有数据
			while ((len = bis.read(buffer)) > 0) {
				// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录当中
				bos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 别忘了关流：应该在try-catch-finally里的
			try {
				if (bis != null) {
					bis.close();
					bis = null;
				}
				if (bos != null) {
					bos.close();
					bos.flush();
					bos = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os.flush();
					os = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @Method: makeFileName
	 * @Description 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名�?
	 * @param filename
	 *            文件的原始名�?
	 * @return uuid+"_"+文件的原始名�?
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:59:37
	 */

	private String makeFileName(String filename) { // 2.jpg
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯�?的文件名
		return UUID.randomUUID().toString().replace("-", "").toUpperCase() + "_" + filename;
	}

	/**
	 * @Method: makeFileName
	 * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名�?
	 * @Anthor:潇�??
	 * @param filename
	 *            文件的原始名�?
	 * @return 获取文件的名�?
	 */
	private String substringFileName(String filename) {
		filename = filename.substring(filename.lastIndexOf(File.separator), filename.length());
		return filename.substring(filename.indexOf("_") + 1, filename.length());
	}

	/**
	 * 
	 * @Method makePath
	 * @Description 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	 * @param filename
	 *            文件�?
	 * @param savePath
	 *            原始文件路径
	 * @return 文件路径
	 * @author �?
	 * @date 2017�?8�?26�? 下午12:05:23
	 */
	private String makePath(String filename, String savePath) {
		// 得到文件名的hashCode的�?�，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构�?�新的保存目�?
		String dir = savePath + File.separator + dir1 + File.separator + dir2; // upload\2\3
		// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存�?
		if (!file.exists()) {
			file.mkdirs();// 创建目录
		}
		return dir;
	}

	public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 
	 * @Method makePath
	 * @Description 同天的文件在同个文件�?
	 * @param savePath
	 *            原始文件路径
	 * @return 文件路径
	 * @author �?
	 * @date 2017�?8�?26�? 下午12:15:39
	 */
	@SuppressWarnings("unused")
	private String makePath(String savePath) {
		String format = MONTH_FORMAT.format(new Date());
		// 构�?�新的保存目�?
		String dir = savePath + File.separator + format;
		File file = new File(dir);
		// 如果目录不存�?
		if (!file.exists()) {
			file.mkdirs();// 创建目录
		}
		return dir;
	}

	/**
	 * @Method getFileMimeType
	 * @Description 设置文件下载时头文件信息 ，不在浏览器打开，下载本�?
	 * @param file
	 * @return 头文件信�?
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:46:53
	 */
	private static final String MIME_DOWNLOAD = "application/x-msdownload";// 获取
																			// 文件的mime类型

	private String getFileMimeType(File file) {
		try {
			return file.toURI().toURL().openConnection().getContentType();
		} catch (IOException ex) {
			return MIME_DOWNLOAD;
		}
	}

	/**
	 * @Description 设置文件下载时头文件信息，时间的转化
	 * @param time
	 *            时间long
	 * @return 时间
	 * @author �?
	 * @date 2017�?8�?26�? 上午11:52:26
	 */
	private SimpleDateFormat sdf;

	private String getGMT(long time) {
		if (sdf == null) {
			sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		return sdf.format(time);
	}

	@SuppressWarnings("unused")
	public HashMap<Object, Object> extMap() {
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

}
