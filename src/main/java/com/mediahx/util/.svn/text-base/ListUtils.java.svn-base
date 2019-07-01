package com.mediahx.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mediahx.bean.PageInfo;

/**
 * List工具
 * @author ZHE
 *
 */
public class ListUtils {
	 private static Logger logger = LoggerFactory.getLogger(ListUtils.class);
	/**
	 * 按最大记录数拆分List.
	 * @param entityList			记录List
	 * @param pageSize				拆分记录数
	 * @return						拆分List
	 */
	public static <T> List<List<T>> splitList(final List<T> entityList, final int pageSize) {
		List<List<T>> lists = new ArrayList<List<T>>();
		if (CommUtils.isObjEmpty(entityList)) {
			return lists;
		}
		if (entityList.size() <= pageSize) {
			lists.add(entityList);
			return lists;
		}
		int count = 0;
		List<T> list = new ArrayList<T>();
		for (T entity : entityList) {
			list.add(entity);
			if (++count % pageSize == 0) {
				lists.add(list);
				list = new ArrayList<T>();
			}
		}
		if (list.size() > 0) {
			lists.add(list);
		}
		return lists;
	}
	
	/**
	 * 按最大记录数拆分List.<p>
	 * 最大记录数默认为100
	 * @param entityList			记录List
	 * @return						拆分List
	 */
	public static <T> List<List<T>> splitList(final List<T> entityList) {
		return splitList(entityList, 100);
	}
	
	/**
	 * 分页拆分
	 * @param pageSize		每页最大记录数
	 * @param count			总记录数
	 * @return
	 */
	public static List<PageIndex> splitPages(int pageSize, int count) {
	    List<PageIndex> pageIndexs = new ArrayList<PageIndex>();
	    if (count > pageSize) {
	    	int size = count / pageSize;
	    	for (int i = 0; i <= size; i++) {
	    		int beginIndex = i * pageSize + 1;
	    		if (beginIndex > count) {
	    			break;
	    		}
				int endIndex = beginIndex + pageSize - 1;
				if (endIndex > count) {
					endIndex = count;
				}
				pageIndexs.add(new PageIndex(beginIndex, endIndex, i + 1));
	        }
	    } else {
	    	pageIndexs.add(new PageIndex(1, count, 1));
	    }
	    return pageIndexs;
    }
	

	public static class PageIndex {

		/** 起始页 */
		private int beginIndex;

		/** 结束页 */
		private int endIndex;

		/** 页数 */
		private int index;

		public int getBeginIndex() {
			return beginIndex;
		}

		public void setBeginIndex(int beginIndex) {
			this.beginIndex = beginIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public void setEndIndex(int endIndex) {
			this.endIndex = endIndex;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		/**
         * @param beginIndex
         * @param endIndex
         * @param index
         */
        public PageIndex(int beginIndex, int endIndex, int index) {
	        super();
	        this.beginIndex = beginIndex;
	        this.endIndex = endIndex;
	        this.index = index;
        }

		@Override
        public String toString() {
	        return "PageIndex [beginIndex=" + beginIndex + ", endIndex=" + endIndex + ", index="
	                + index + "]";
        }
	}
	
	 public static void createExcel(String templateFile, String destFile, Map<?, ?> beans)
	    {
	        XLSTransformer transformer = new XLSTransformer();
	        InputStream is = null;
	        OutputStream os = null;
	        try
	        {	
	        	File file = new File(destFile); 
	        	createParentDirectory(file.getParentFile());
	        	if(!file.exists()){
	        		file.createNewFile();
	        	}
	            is = new FileInputStream(templateFile);
	            os = new FileOutputStream(destFile);
	            Workbook workbook = transformer.transformXLS(is, beans);
	            workbook.write(os);
	            is.close();
	            os.close();
	        }
	        catch (Exception e)
	        {
	            logger.error("导出失败", e);
	        }
	    }
	    
	    public static void createParentDirectory(File file) {
	    	if(file.exists()){
	    		return ;
	    	}
	    	if (!file.getParentFile().exists()) {
	    		createParentDirectory(file.getParentFile());
			}
			try {
				file.mkdirs();
			} catch (Exception e) {
			}
		}
	    
	    public static void createCSV(String destFile, List<List<String>> contentList, String encoding)
	    {

	        BufferedWriter bufferedWriter = null;
	        StringBuilder sb = new StringBuilder();
	        List<String> rowList = null;
	        try
	        {
	            bufferedWriter =
	                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destFile)),
	                    encoding == null ? "GBK" : encoding));

	            for (int i = 0; i < contentList.size(); i++)
	            {
	                rowList = contentList.get(i);
	                for (String content : rowList)
	                {
	                    sb.append((content==null || content.trim().equals("null"))?"":content).append(String.valueOf((char) 30) ).append(",");
	                }
	                bufferedWriter.write(sb.toString());
	                bufferedWriter.newLine();
	                sb=new StringBuilder();
	                sb.append(i + 1).append(",");
	            }
	            bufferedWriter.flush();
	            bufferedWriter.close();
	            bufferedWriter = null;
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            try
	            {
	                if (bufferedWriter != null)
	                {
	                    bufferedWriter.flush();
	                    bufferedWriter.close();
	                }
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	        }

	    }

	    public static Map<String, Object> compressZip(String strZipName, List<String> file) throws Exception
	    {
	        Map<String, Object> fileMap = new HashMap<String, Object>();
	        List<String> childlist = new ArrayList<String>();
	        ZipOutputStream out = null;
	        FileInputStream fis = null;
	        try
	        {
	            File zipFile = new File(strZipName);
	            if (file != null && !file.isEmpty())
	            {
	                byte[] buffer = new byte[1024];
	                out = new ZipOutputStream(new FileOutputStream(zipFile));
	                String fileName = null;
	                for (int i = 0; i < file.size(); i++)
	                {
	                    fileName = file.get(i);
	                    File childFile = new File(fileName);
	                    fis = new FileInputStream(childFile);
	                    out.putNextEntry(new ZipEntry(childFile.getName()));
	                    int len = 0;
	                    while ((len = fis.read(buffer)) > 0)
	                    {
	                        out.write(buffer, 0, len);
	                    }
	                    out.closeEntry();
	                    fis.close();
	                    childlist.add(childFile.getName());
	                    childFile.delete();
	                }
	                fileMap.put("export_files", childlist);
	                fileMap.put("export_zip", zipFile.getName());
	                
	                for (int i = 0; i < file.size(); i++)
	                {
	                    File childFile = new File(file.get(i));
	                    if(childFile.exists()){
	                    	childFile.delete();
	                    }
	                }
	            }
	        }
	        catch (Exception ex)
	        {
	            logger.error("压缩成.zip失败", ex);
	        }
	        finally
	        {
	            if (out != null)
	            {
	                out.close();
	            }
	            if (fis != null)
	            {
	                fis.close();
	            }
	        }
	        return fileMap;
	    }

	    /**
	     * ExcelFileName
	     * @return
	     */
	    public static long getCurrentTimeMillis()
	    {
	        return System.currentTimeMillis();
	    }

	    public static Map<String, Object> copyMapPager(Map<String, Object> map, int pageNum, int pageSize)
	    {
	        Map<String, Object> copiedMap = Collections.synchronizedMap(map);
	        copiedMap.put("pageNum", pageNum);
	        copiedMap.put("numPerPage", pageSize);
	        return copiedMap;
	    }

	    public static int getTotalPage(int numPerPage, int totalCount)
	    {
	        int temp = 0;
	        if (totalCount % numPerPage != 0)
	        {
	            temp++;
	        }
	        return totalCount / numPerPage + temp;
	    }

	    public static void removePager(Map<String, Object> map)
	    {
	        map.remove("pageNum");
	        map.remove("numPerPage");
	    }

		public static void converPager(Map<String, Object> map, PageInfo pageInfo) {
			if (map.get("pageNum") != null) {
				pageInfo.setPageNum(Integer.parseInt(map.get("pageNum") + ""));
			} else {
				map.put("pageNum", pageInfo.getPageNum());
			}
			if (map.get("numPerPage") != null) {
				pageInfo.setNumPerPage(Integer.parseInt(map.get("numPerPage") + ""));
			} else {
				map.put("numPerPage", pageInfo.getNumPerPage());
			}
			if (map.get("totalPage") != null) {
				pageInfo.setTotalPage(Integer.parseInt(map.get("totalPage") + ""));
			}
			if (map.get("totalCount") != null) {
				pageInfo.setTotalCount(Integer.parseInt(map.get("totalCount") + ""));
			}
			
			// 计算当前页记录数
			if (pageInfo.getPageNum()>=0) {
				pageInfo.setCurrentPageNum((pageInfo.getPageNum())*pageInfo.getNumPerPage());
			}else{
				pageInfo.setCurrentPageNum(0);
			}
			map.put("currentPageNum", pageInfo.getCurrentPageNum());
		}

	    public static Map<Integer, String> converIntToStr(Map<String, String> map)
	    {
	        Map<Integer, String> result = new HashMap<Integer, String>();
	        if (!map.isEmpty())
	        {
	            String entryKey = null;
	            for (Map.Entry<String, String> entry : map.entrySet())
	            {
	                entryKey = entry.getKey();
	                result.put(Integer.parseInt(entryKey), entry.getValue());
	            }
	        }
	        return result;
	    }

	    public static BigDecimal changeF2Y(BigDecimal fenAmount)
	    {
	        if (fenAmount == null)
	        {
	            return new BigDecimal("0.00");
	        }
	        BigDecimal yanAmount = fenAmount.divide(new BigDecimal(100)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	        return yanAmount;
	    }
}