package com.mediahx.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 汉字按拼音排序
 * 
 * @author ZHE
 *
 */
public class PinyinComparator implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		char c1 = ((String) o1).charAt(0);
		char c2 = ((String) o2).charAt(0);
		return concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c2)));
	}

	private String concatPinyinStringArray(String[] pinyinArray) {
		StringBuffer pinyinSbf = new StringBuffer();
		if ((pinyinArray != null) && (pinyinArray.length > 0)) {
			for (int i = 0; i < pinyinArray.length; i++) {
				pinyinSbf.append(pinyinArray[i]);
			}
		}
		return pinyinSbf.toString();
	}
	
	public static List<String> sortPinyin(List<String> list){
		if(!CommUtils.isObjEmpty(list) && list.size()>0){
			Collections.sort(list,new PinyinComparator());
		}
		return list;
	}

/*	@Test
	public void testSortPinyin() {
		String[] arr = { "张三", "李四", "王五", "赵六","应就"};
		List<String> list = Arrays.asList(arr);
//		Arrays.sort(arr, new PinyinComparator());
		list = sortPinyin(list);
		System.out.println(list);
	}*/
}