package com.kmutt.sit.theater.membership;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonarrayParseString {

    /*public static String[] jsonarrayParseString(JSONArray jsonArray, String name){
        String result[] = new
        if(jsonArray != null){

        }
    }*/

    public static String parseString(JSONArray jsonArray, String name, int index){
        String pattern = "{\"" + name + "\":\"";
        String result = "";
        try {
            if (jsonArray.get(index) != null && jsonArray != null) {
                String str = jsonArray.get(index).toString();
                if(str.length() == 0) return "Array is null !!";
                for(int i=0; i<str.length(); i++){
                    if( i+5+name.length()<=str.length() & str.substring(i,i+5+name.length()).equals(pattern) ){
                        if(index == 0){
                            for(int j=i+5+name.length(); j<str.length(); j++){
                                if(str.charAt(j) == '"') break;
                                result += str.charAt(j);
                            }
                            return result;
                        }
                        index--;
                    }
                    if(i == jsonArray.length()-1) return "can't find value";
                }
            }else return "Array is null";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return "jsonParseFail !!";
        return "";
    }

    public static String parseString2(JSONArray jsonArray, String name, int index){
        String pattern = "\"" + name + "\":\"";
        String result = "";
        if (jsonArray != null) {
            String str = jsonArray.toString();
            if(str.length() == 0) return "Array is null !!";
            for(int i=0; i<str.length(); i++){
                if( i+4+name.length()<=str.length() & str.substring(i,i+4+name.length()).equals(pattern) ){
                    if(index == 0){
                        for(int j=i+4+name.length(); j<str.length(); j++){
                            if(str.charAt(j) == '"') break;
                            result += str.charAt(j);
                        }
                        return result;
                    }
                    index--;
                }
                if(i == str.length()-1) return "cfv"+jsonArray.toString();
            }
        }else return "Array is null";
        //return "jsonParseFail !!";
        return "";
    }
}
