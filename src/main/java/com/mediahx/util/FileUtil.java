package com.mediahx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author ZHE
 *
 */
public class FileUtil {
	private static final String RECORDENTER = "\r\n";
	
	protected final static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 删除文件，可以是文件或文件夹
	 * 
	 * @param fileName
	 *            要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			// System.out.println("删除文件失败:" + fileName + "不存在！");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				// System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				// System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			// System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 * 
	 * @param dir
	 *            要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		// System.out.println("File.separator="+ File.separator);
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			// System.out.println("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtil.deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			// System.out.println("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			// System.out.println("删除目录" + dir + "成功！");
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		/*
		 * // 删除单个文件 String file = "C:\\TEMP\\1.txt";
		 * DeleteFileUtil.deleteFile(file); System.out.println();
		 */
		// 删除一个目录
		String dir = "C:\\TEMP\\abc";
		// DeleteFileUtil.deleteDirectory(dir);
		System.out.println();
		// 删除文件
		// dir = "c:/test/test0";
		FileUtil.delete(dir);

	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean deletefile(String delpath)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + File.separator
							+ filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + File.separator + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	

	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
		} else {
			f.createNewFile();
		}
		return s;
	}

	// 递归
	public static long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;

	}

	/**
	 * 复制文件或者目录
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param distFolder
	 *            目标文件夹
	 * @IOException 当操作发生异常时抛出
	 */
	public static boolean copyFile(String resFilePath, String distFolder) {
		File resFile = new File(resFilePath);
		File distFile = new File(distFolder);
		try {
			if (resFile.isDirectory()) {
				org.apache.commons.io.FileUtils.copyDirectoryToDirectory(
						resFile, distFile);
			} else if (resFile.isFile()) {
				org.apache.commons.io.FileUtils.copyFile(resFile, distFile);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void moveFile(String resFilePath, String distFolder)
			throws IOException {
		File resFile = new File(resFilePath);
		File distFile = new File(distFolder);
		if (resFile.isDirectory())
			org.apache.commons.io.FileUtils.moveDirectoryToDirectory(resFile,
					distFile, true);
		else if (resFile.isFile())
			org.apache.commons.io.FileUtils.moveFileToDirectory(resFile,
					distFile, true);
	}

	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            文件目录
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 */
	public static void renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}
			if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else {
				oldfile.renameTo(newfile);
			}
		} else {
			System.out.println("新文件名和旧文件名相同...");
		}
	}
	
	/**
	 * 写txt
	 * @param fileContentStr
	 * @param fileSavePath
	 * @param curFileName
	 */
	public static synchronized  void writeTxtFile(String fileContentStr,String fileSavePath,String curFileName){
		  OutputStreamWriter bw = null;
			try {
				File file = new File(fileSavePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				bw = new OutputStreamWriter(new FileOutputStream(new File(fileSavePath, curFileName)),"UTF-8");
				bw.write(fileContentStr);
				bw.flush();
				bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	
	/**
	 * 写txt
	 * @param fileContentStr
	 * @param fileSavePath
	 * @param curFileName
	 */
	public static void writeFileGBK(String fileContentStr,String fileSavePath,String curFileName){
		  OutputStreamWriter bw = null;
			try {
				File file = new File(fileSavePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				bw = new OutputStreamWriter(new FileOutputStream(new File(fileSavePath, curFileName)),"GBK");
				bw.write(fileContentStr);
				bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	/**
	 * 读取txt文件
	 * @param fileSavePath
	 * @param curFileName
	 * @return
	 */
	public static String readTxtFile(String fileSavePath, String curFileName) {
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileSavePath + curFileName), "UTF-8"));
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				sb.append(read);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取log文件
	 * @param fileSavePath
	 * @param curFileName
	 * @return
	 */
	public static String readLogFile(String fileSavePath, String curFileName) {
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileSavePath + curFileName), "GBK"));
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				sb.append(read);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取shell文件
	 * @param fileSavePath
	 * @param curFileName
	 * @return
	 */
	public static String readShellFile(String fileSavePath, String curFileName) {
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileSavePath + curFileName), "UTF-8"));
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				sb.append(read);
				sb.append(RECORDENTER);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取Resource文件
	 * @param relativePath
	 * @param curFileName
	 * @return
	 */
	public static String readResourcesFile(String relativePath, String curFileName) {
		URL url = PropertiesUtil.getResource(relativePath + curFileName);
		File file = new File(url.getPath());
		
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String read = null;
			while ((read = bufferedReader.readLine()) != null) {
				sb.append(read);
				sb.append(RECORDENTER);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void mergeFiles(String outFile, String[] files) {
		int BUFSIZE = 1024 * 8;
		FileChannel outChannel = null;
		FileInputStream fis=null;
		LOGGER.info("Merge " + Arrays.toString(files) + " into " + outFile);
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			for(String f : files){
				LOGGER.info("Merging::" + f);
				fis = new FileInputStream(f);
				FileChannel fc = fis.getChannel(); 
				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				while(fc.read(bb) != -1){
					bb.flip();
					outChannel.write(bb);
					bb.clear();
				}
				fc.close();
			}
			fis.close();
			LOGGER.info("Merged End!! ");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
		}
	}
	
	/**
	 * 返回xml节点文本
	 * @param filePath
	 * @param fileName
	 * @param txtNode
	 * @return
	 */
	public static String getXmlFileNodeText(String filePath, String fileName, String txtNode) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(filePath + fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Node n = document.selectSingleNode(txtNode);
		if (!CommUtils.isEmpty(n.getText())) {
			return n.getText().trim();
		}
		return "";
	}
	
	/**
	 * 创建目录
	 * @param filePath
	 */
	public static void mkDir(String filePath){
		 File file = new File(filePath);
		 if(!file.exists()){
			 file.mkdirs();
		 }
	}
	
	/** 
     * 根据路径 下载图片 然后 保存到对应的目录下 
     * @param urlString 
     * @param filename 
     * @param savePath 
     * @return 
     * @throws Exception 
     */  
    public static void downloadPic(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求的路径  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+ File.separatorChar +filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
          
        is.close();  
    }   
}