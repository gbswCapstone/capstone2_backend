package Capstone.capstoneProject.util;

// 제목이 길 때 maxLength만큼만 문자열 추출 + ... 붙여줌
public class StringUtils {
    public static String safeTruncate(String content, int maxLength) {
        if (content == null) return "";
        return content.length() <=maxLength ? content : content.substring(0, maxLength) + "...";
    }
}
