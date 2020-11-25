package com.innilabs.restboard.util;


public class StringUtil {
    public static boolean equal(String s1, String s2){
        if(s1==null || s2==null){
            return false;
        }
        return s1.equals(s2);
    }

	public static boolean isEmpty(String s) {
    
		return s == null || s.length()==0 ;
	}
}