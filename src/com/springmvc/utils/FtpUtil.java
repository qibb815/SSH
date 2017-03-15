package com.springmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FTP 工具类 
 * android app下载
 * @author Administrator
 *
 */
@Controller
public class FtpUtil {

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param remotePath
	 *            //FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String host = "192.168.1.12";
	private int port = 21;
	private String username = "ftpadmin";
	private String password = "123456";

	private boolean binaryTransfer = true;
	private boolean passiveMode = true;
	private String encoding = "UTF-8";
	private int clientTimeout = 3000;
	private boolean flag = true;

	// ---------------------------------------------------------------------
	// private method
	// ---------------------------------------------------------------------
	/**
	 * 返回一个FTPClient实例
	 * 
	 * @throws Exception
	 */
	private FTPClient getFTPClient() throws Exception {
		FTPClient ftpClient = new FTPClient(); // 构造一个FtpClient实例
		ftpClient.setControlEncoding(encoding); // 设置字符集

		connect(ftpClient); // 连接到ftp服务器
		logger.info("ftp连接成功");
		// 设置为passive模式
		if (passiveMode) {
			ftpClient.enterLocalPassiveMode();
		}
		setFileType(ftpClient); // 设置文件传输类型

		try {
			ftpClient.setSoTimeout(clientTimeout);
		} catch (SocketException e) {
			throw new Exception("Set timeout error.", e);
		}

		return ftpClient;
	}

	/**
	 * 设置文件传输类型
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private void setFileType(FTPClient ftpClient) throws Exception {
		try {
			if (binaryTransfer) {
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
		} catch (IOException e) {
			throw new Exception("Could not to set file type.", e);
		}
	}

	/**
	 * 连接到ftp服务器
	 * 
	 * @param ftpClient
	 * @return 连接成功返回true，否则返回false
	 * @throws Exception
	 */
	public boolean connect(FTPClient ftpClient) throws Exception {
		try {
			ftpClient.connect(host, port);

			// 连接后检测返回码来校验连接是否成功
			int reply = ftpClient.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				// 登陆到ftp服务器
				if (ftpClient.login(username, password)) {
					setFileType(ftpClient);
					return true;
				}
			} else {
				ftpClient.disconnect();
				throw new Exception("FTP server refused connection.");
			}
		} catch (IOException e) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect(); // 断开连接
				} catch (IOException e1) {
					throw new Exception("Could not disconnect from server.", e);
				}

			}
			throw new Exception("Could not connect to server.", e);
		}
		return false;
	}

	/**
	 * 断开ftp连接
	 * 
	 * @throws Exception
	 */
	private void disconnect(FTPClient ftpClient) throws Exception {
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			throw new Exception("Could not disconnect from server.", e);
		}
	}

	// ---------------------------------------------------------------------
	// public method
	// ---------------------------------------------------------------------
	/**
	 * 上传一个本地文件到远程指定文件
	 * 
	 * @param serverFile
	 *            服务器端文件名(包括完整路径)
	 * @param localFile
	 *            本地文件名(包括完整路径)
	 * @return 成功时，返回true，失败返回false
	 * @throws Exception
	 */
	public boolean put(String serverFile, String localFile) throws Exception {
		return put(serverFile, localFile, false);
	}

	/**
	 * 上传一个本地文件到远程指定文件
	 * 
	 * @param serverFile
	 *            服务器端文件名(包括完整路径)
	 * @param localFile
	 *            本地文件名(包括完整路径)
	 * @param delFile
	 *            成功后是否删除文件
	 * @return 成功时，返回true，失败返回false
	 * @throws Exception
	 */
	public boolean put(String serverFile, String localFile, boolean delFile) throws Exception {
		FTPClient ftpClient = null;
		InputStream input = null;
		try {
			ftpClient = getFTPClient();
			// 处理传输
			input = new FileInputStream(localFile);
			ftpClient.storeFile(serverFile, input);
			input.close();
			if (delFile) {
				(new File(localFile)).delete();
			}
			return true;
		} catch (FileNotFoundException e) {
			throw new Exception("local file not found.", e);
		} catch (IOException e) {
			throw new Exception("Could not put file to server.", e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				throw new Exception("Couldn't close FileInputStream.", e);
			}
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
	}

	/**
	 * 下载一个远程文件到本地的指定文件
	 * 
	 * @param serverFile
	 *            服务器端文件名(包括完整路径)
	 * @param localFile
	 *            本地文件名(包括完整路径)
	 * @return 成功时，返回true，失败返回false
	 * @throws Exception
	 */
	public boolean get(String serverFile, String localFile) throws Exception {
		return get(serverFile, localFile, false);
	}

	/**
	 * 下载一个远程文件到本地的指定文件
	 * 
	 * @param serverFile
	 *            服务器端文件名(包括完整路径)
	 * @param localFile
	 *            本地文件名(包括完整路径)
	 * @return 成功时，返回true，失败返回false
	 * @throws Exception
	 */
	public boolean get(String serverFile, String localFile, boolean delFile) throws Exception {
		OutputStream output = null;
		try {
			output = new FileOutputStream(localFile);
			return get(serverFile, output, delFile);
		} catch (FileNotFoundException e) {
			throw new Exception("local file not found.", e);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				throw new Exception("Couldn't close FileOutputStream.", e);
			}
		}
	}

	/**
	 * 下载一个远程文件到指定的流 处理完后记得关闭流
	 * 
	 * @param serverFile
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public boolean get(String serverFile, OutputStream output) throws Exception {
		return get(serverFile, output, false);
	}

	/**
	 * 下载一个远程文件到指定的流 处理完后记得关闭流
	 * 
	 * @param serverFile
	 * @param output
	 * @param delFile
	 * @return
	 * @throws Exception
	 */
	public boolean get(String serverFile, OutputStream output, boolean delFile) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient();
			// 处理传输
			ftpClient.retrieveFile(serverFile, output);
			if (delFile) { // 删除远程文件
				ftpClient.deleteFile(serverFile);
			}
			return true;
		} catch (IOException e) {
			throw new Exception("Couldn't get file from server.", e);
		} finally {
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
	}

	/**
	 * 从ftp服务器上删除一个文件
	 * 
	 * @param delFile
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String delFile) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient();
			ftpClient.deleteFile(delFile);
			return true;
		} catch (IOException e) {
			throw new Exception("Couldn't delete file from server.", e);
		} finally {
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
	}

	/**
	 * 批量删除
	 * 
	 * @param delFiles
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String[] delFiles) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient();
			for (String s : delFiles) {
				ftpClient.deleteFile(s);
			}
			return true;
		} catch (IOException e) {
			throw new Exception("Couldn't delete file from server.", e);
		} finally {
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
	}

	/**
	 * 列出远程默认目录下所有的文件
	 * 
	 * @return 远程默认目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
	 * @throws Exception
	 */
	public String[] listNames() throws Exception {
		return listNames(null);
	}

	/**
	 * 列出远程目录下所有的文件
	 * 
	 * @param remotePath
	 *            远程目录名
	 * @return 远程目录下所有文件名的列表，目录不存在或者目录下没有文件时返回0长度的数组
	 * @throws Exception
	 */
	public String[] listNames(String remotePath) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient();
			String[] listNames = ftpClient.listNames(remotePath);
			return listNames;
		} catch (IOException e) {
			throw new Exception("列出远程目录下所有的文件时出现异常", e);
		} finally {
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
	}

	public boolean isExist(String remoteFile) throws Exception {

		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient();
			String[] listNames = ftpClient.listNames(remoteFile);
			System.out.println(remoteFile);
			for (int i = 0; i < listNames.length; i++) {

				if (remoteFile.equals(listNames[i])) {
					flag = true;
					System.out.println("文件:" + remoteFile + "已经存在了");
					break;
				} else {
					flag = false;
				}
			}

		} catch (IOException e) {
			throw new Exception("查询文件是否存在文件时出现异常", e);
		} finally {
			if (ftpClient != null) {
				disconnect(ftpClient); // 断开连接
			}
		}
		return flag;
	}

	/**
	 * 安卓下载erp app
	 * @throws Exception
	 */
	@RequestMapping("/getApp.ajax")
	@ResponseBody
	public void getApp(HttpServletRequest request,HttpServletResponse response) throws Exception{
		FtpUtil ftp = new FtpUtil();
		
		Properties prop = new Properties(); 
		String filename = "erp.apk";
		
		byte b[]=new byte[1024]; 
        if(!ftp.isExist(filename)){
        	logger.info("文件："+filename + "不存在！");
        	return;
		}
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));  
        
        OutputStream fos = response.getOutputStream();
		try {
			ftp.get(filename,fos);
			fos.write(b);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FtpUtil ftp = new FtpUtil();
	}

}
