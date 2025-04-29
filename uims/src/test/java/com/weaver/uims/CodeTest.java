package com.weaver.uims;

import org.junit.Test;

/**
 * @author zh 2024/9/4 20:03
 */
public class CodeTest {
    @Test
    public void test() {
        System.out.println(longestBeautifulSubstring("eauoiouieaaoueiuaieoeauoiaueoiaeoiuieuaoiaeouiaueo"));
    }

    public int longestBeautifulSubstring(String word) {
        return dfs(word, "", 0).length();
    }

    public String dfs(String parent, String sub, Integer startIndex) {
        if (startIndex >= parent.length()) {
            return sub;
        }
        String pick = "";
        String ignore = "";
        char s = parent.charAt(startIndex);
        if (sub.length() == 0) {
            if (s != 'a') {
                pick = dfs(parent, "", startIndex + 1);
                ignore = dfs(parent, "", startIndex + 1);
            } else{
                pick = dfs(parent, String.valueOf(s), startIndex + 1);
                ignore = dfs(parent, "", startIndex + 1);
            }
        } else {
            ignore = dfs(parent, "", startIndex + 1);
            ;
            if (s >= sub.charAt(sub.length() - 1)) {
                pick = dfs(parent, sub + s, startIndex + 1);
            } else {
                pick = sub;
            }
        }
        if (pick.indexOf("u") != -1) {
            if (ignore.indexOf("u") != -1) {
                return pick.length() > ignore.length() ? pick : ignore;
            }
            return pick;
        } else {
            if (ignore.indexOf("u") != -1) {
                return ignore;
            } else {
                return "";
            }
        }

    }
}
