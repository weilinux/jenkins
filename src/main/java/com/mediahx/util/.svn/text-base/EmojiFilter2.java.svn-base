package com.mediahx.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信过滤表情
 *
 *
 */
public class EmojiFilter2 {
	public static void main(String[] args) {
		String string = "💋(String)万万没想到👌平静��给你��";
	    System.out.println(EmojiFilter2.filterEmoji(string));
	    String str2 = "万万没想到平��给你��xxxadasfAAADFD阿萨德发123";
	    System.out.println(CommUtils.isSpecialChar(str2));
	}
    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            source = emojiMatcher.replaceAll("?");
            return source;
        }
        return source;
    }
}