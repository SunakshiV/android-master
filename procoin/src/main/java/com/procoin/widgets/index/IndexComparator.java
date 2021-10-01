package com.procoin.widgets.index;

/**
 * @author zhengmj
 * <p/>
 * Copyright (c) 排序，这个主要用于好友的排序，。
 * 字母排在后，其他#的排先
 */


import com.procoin.http.model.User;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Comparator;
import java.util.HashMap;

public class IndexComparator implements Comparator<User> {

    private HashMap<String, String> nameMap;

    public void setNameMap(HashMap<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public IndexComparator() {
        nameMap = new HashMap<String, String>();
        nameMap.put("曾", "Z");
        nameMap.put("单", "S");
        nameMap.put("种", "C");
        nameMap.put("解", "X");
        nameMap.put("查", "Z");
        nameMap.put("缪", "M");
        nameMap.put("区", "O");
        nameMap.put("繁", "P");
        nameMap.put("仇", "Q");
    }

    @Override
    public int compare(User lhs, User rhs) {
        String namepy1 = (lhs.shiftKey == null ? "" : lhs.shiftKey);
        String namepy2 = (rhs.shiftKey == null ? "" : rhs.shiftKey);
        return namepy1.compareTo(namepy2);
    }

    /**
     * 获取 中文姓 名的首字母
     *
     * @param src 中文的名字
     * @return
     */
    public String getPingYinSpell(String src) {
        if (src == null || "".equals(src) || src.length() == 0) return "#";
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            // 判断是否为汉字字符函数
            src = src.substring(0, 1);
            if (src.matches("[a-zA-Z]")) {
                return src.toUpperCase();
            }
            if (src.matches("[\\u4E00-\\u9FA5]+")) {
                if (!nameMap.containsKey(src)) {
                    String[] result = PinyinHelper.toHanyuPinyinStringArray(src.charAt(0), format);
                    if (result == null || result.length == 0) return "#";
                    if (result[0].length() > 0) return result[0].substring(0, 1);
                    return "#";
                } else {
                    return nameMap.get(src);
                }
            } else {
                return "#";
            }
        } catch (Exception e1) {
            // e1.printStackTrace();
        }
        return "#";
    }
}
