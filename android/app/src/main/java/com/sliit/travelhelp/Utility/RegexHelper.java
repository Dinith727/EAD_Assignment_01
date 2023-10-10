package com.sliit.travelhelp.Utility;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    private static String  emailRegexPattern = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static String NICPatternOld = "\\d{9}[A-Z]";
    private static String NICPatternNew = "^\\d{12}$";


    public  static  boolean matchEmail(String str){
        Pattern pattern = Pattern.compile(emailRegexPattern);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    public  static  boolean matchNicOld(String str){
        Pattern pattern = Pattern.compile(NICPatternOld);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public  static  boolean matchNicNew(String str){
        Pattern pattern = Pattern.compile(NICPatternNew);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }



}
