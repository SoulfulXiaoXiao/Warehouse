package com.PersonalCollection.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.PersonalCollection.common.util.FileUtils;
import com.PersonalCollection.entity.Files;
import com.PersonalCollection.service.IFileService;
import com.PersonalCollection.utils.Prop;
import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/file")
public class FileController {
	@Resource
	private IFileService iService;
	/**
	 * 获取全部文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listPhoto", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView listPhoto(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Files> listFile = iService.listFile();
			map.put("files", listFile);
		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return new ModelAndView("photo_album", map);
	}
	/**
	 * 获取全部文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listFile", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView listFile(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Files> listFile = iService.listFile();
			map.put("files", listFile);
		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return new ModelAndView("file_manager", map);
	}
	
	
	
	/**
	 * 得到某个文件下所有文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getFileInfo", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView getFileInfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Prop prop = new Prop("fileInfo.properties", "UTF-8");
			map.put("result", new FileUtils().readfiles(prop.get("filePath")));
		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		return new ModelAndView("files", map);
	}
/**
 * 上传文件单一
 * @param file
 * @param fileInfo
 * @param request
 * @return
 */
	/**
	 * @Method uploadFile
	 * @Description
	 * @param file
	 * @param fileInfo
	 * @param request
	 * @param response
	 * @return
	 * @author 瀟
	 * @date 2017年9月6日 上午11:19:20
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json")
	public String uploadFile(@RequestParam MultipartFile file, Files fileInfo,HttpServletRequest request,HttpServletResponse response) {
		try {

			FileUtils fileUtils = new FileUtils();
			String upload = fileUtils.uploadFile(file);
			
			fileInfo.setFileURL(upload);
			fileInfo.setFileName(upload.substring(upload.lastIndexOf(File.separator)+1,upload.length()));
			fileInfo.setBaseName(upload.substring(upload.lastIndexOf(File.separator)+1,upload.lastIndexOf(".")));
			fileInfo.setExtName(upload.substring(upload.lastIndexOf(".")+1,upload.length()));
			fileInfo.setFilePath(upload.substring(0,upload.lastIndexOf(File.separator)+1));
			fileInfo.setFileSize(file.getSize());
			fileInfo.setOriginalName(file.getOriginalFilename());
			
			iService.insertFile(fileInfo);
			response.getWriter().print("success");
		} catch (IllegalArgumentException | SecurityException | IOException e) {
			e.printStackTrace();
		}
        
		return null;
	}
	
	/**
	 * 文件下载
	 * @param fileInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/downFile", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView downFile(Files fileInfo,HttpServletRequest request,HttpServletResponse response ) {
		try {
			Files file = iService.getFile(fileInfo);
			FileUtils fileUtils = new FileUtils();
			fileUtils.downloadFile(file.getFileURL(),request,response);
			}
		 catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
