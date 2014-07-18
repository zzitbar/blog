/*
 * @(#)FileUploadAction.java 2014-7-18
 * 
 * Copy Right@ 纽海信息技术有限公司
 */ 

package com.coderme.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 * @author zhangtengfei
 *
 *
 * 创建日期: 2014-7-18
 * 修改人 :  
 * 修改说明: 
 * 评审人 ：
 * </pre>
 */
@Controller
@RequestMapping("/fileUpload")
public class FileUploadAction {

	@RequestMapping(method = RequestMethod.GET)
	public String upload() {
		return "fileUpload";
	}
	
	//如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解 
    //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解 
    //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件 
	@RequestMapping(method = RequestMethod.POST)
	public String upload(MultipartFile myfile, HttpServletRequest request, HttpServletResponse response) {
		 if(myfile.isEmpty()){ 
             System.out.println("文件未上传"); 
         }else{ 
             System.out.println("文件长度: " + myfile.getSize()); 
             System.out.println("文件类型: " + myfile.getContentType()); 
             System.out.println("文件名称: " + myfile.getName()); 
             System.out.println("文件原名: " + myfile.getOriginalFilename()); 
             System.out.println("========================================"); 
             //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中 
             String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload"); 
             //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的 
             try {
				FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));
			} catch (IOException e) {
				e.printStackTrace();
			} 
         } 
		
		return "success";
	}
}
