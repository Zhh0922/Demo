//package com.example.demo;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.springframework.util.StringUtils;
//
//import java.util.Map;
//
///**
// * @author hhzhao
// * @date 2022-05-05 12:17
// */
//public class JsonUtils {
//    /**
//     * 比较两个json串是否完全一致
//     * @param expect 预期值
//     * @param actual 实际值
//     * @return 比较结果
//     */
//    public static boolean isSame(String expect, String actual) throws Exception {
//        if (StringUtils.isEmpty(expect) && StringUtils.isEmpty(actual)) {
//            return true;
//        }
//        if (StringUtils.isEmpty(expect) != StringUtils.isEmpty(actual)) {
//            return false;
//        }
//        JsonParser jsonParser = new JsonParser();
//        JsonElement expectJsonElement = jsonParser.parse(expect);
//        JsonElement actualJsonElement = jsonParser.parse(actual);
//        return isSame(expectJsonElement, actualJsonElement);
//    }
//
//    private static boolean isSame(JsonElement expectJsonElement, JsonElement actualJsonElement) {
//        if (expectJsonElement.isJsonNull() && actualJsonElement.isJsonNull()) {
//            return true;
//        }
//        if (expectJsonElement.isJsonPrimitive() && actualJsonElement.isJsonPrimitive()) {
//            String expectStr = expectJsonElement.getAsString();
//            String actualStr = actualJsonElement.getAsString();
//            if (StringUtils.isEmpty(expectStr) && StringUtils.isEmpty(actualStr)) {
//                return true;
//            }
//            return expectStr.equals(actualStr);
//        }
//        if (expectJsonElement.isJsonObject() && actualJsonElement.isJsonObject()) {
//            JsonObject expectJsonObject = expectJsonElement.getAsJsonObject();
//            JsonObject actualJsonObject = actualJsonElement.getAsJsonObject();
//            if (expectJsonObject.size()!= actualJsonObject.size()) {
//                return false;
//            }
//            for (Map.Entry<String, JsonElement> elementEntry : actualJsonObject.entrySet()) {
//                String elementEntryKey = elementEntry.getKey();
//                JsonElement elementEntryValue = elementEntry.getValue();
//                JsonElement jsonElement = expectJsonObject.get(elementEntryKey);
//                if (!isSame(jsonElement, elementEntryValue)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        if (expectJsonElement.isJsonArray() && actualJsonElement.isJsonArray()) {
//            JsonArray expectJsonElementAsJsonArray = expectJsonElement.getAsJsonArray();
//            JsonArray actualJsonElementAsJsonArray = actualJsonElement.getAsJsonArray();
//            if (expectJsonElementAsJsonArray.size() != actualJsonElementAsJsonArray.size()) {
//                return false;
//            }
//            for (JsonElement actualArray : actualJsonElementAsJsonArray) {
//                boolean flag = false;
//                for (JsonElement expectArray : expectJsonElementAsJsonArray) {
//                    if (isSame(actualArray, expectArray)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * 判断某一个json是否包含在另一个json里面
//     * @param complete 包含的json
//     * @param part     被包含的json
//     * @return 判断结果
//     */
//    public static boolean isContainer(String complete, String part) throws Exception {
//        if (null == part) {
//            return true;
//        }
//        if (null == complete) {
//            return false;
//        }
//        JsonElement completeJsonElement = JsonParser.parseString(complete);
//        JsonElement partJsonElement = JsonParser.parseString(part);
//        return isContainer(completeJsonElement, partJsonElement);
//    }
//
//    private static boolean isContainer(JsonElement completeJsonElement, JsonElement partJsonElement) {
//        if (null == partJsonElement) {
//            return true;
//        }
//        if (null == completeJsonElement) {
//            return false;
//        }
//        if (completeJsonElement.isJsonNull() && partJsonElement.isJsonNull()) {
//            return true;
//        }
//        if (partJsonElement.isJsonPrimitive() && completeJsonElement.isJsonPrimitive()) {
//            String partStr = partJsonElement.getAsString();
//            String completeStr = completeJsonElement.getAsString();
//            if (StringUtils.isEmpty(partStr) && StringUtils.isEmpty(completeStr)) {
//                return true;
//            }
//            return partStr.equals(completeStr);
//        }
//        if (partJsonElement.isJsonObject() && completeJsonElement.isJsonObject()) {
//            JsonObject partJsonObject = partJsonElement.getAsJsonObject();
//            JsonObject completeJsonObject = completeJsonElement.getAsJsonObject();
//            if (completeJsonObject.size() < partJsonObject.size()) {
//                return false;
//            }
//            for (Map.Entry<String, JsonElement> elementEntry : partJsonObject.entrySet()) {
//                String elementEntryKey = elementEntry.getKey();
//                JsonElement elementEntryValue = elementEntry.getValue();
//                JsonElement jsonElement = completeJsonObject.get(elementEntryKey);
//                if (!isContainer(jsonElement, elementEntryValue)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        if (partJsonElement.isJsonArray() && completeJsonElement.isJsonArray()) {
//            JsonArray partJsonArray = partJsonElement.getAsJsonArray();
//            JsonArray completeJsonArray = completeJsonElement.getAsJsonArray();
//            if (completeJsonArray.size() < partJsonArray.size()) {
//                return false;
//            }
//            for (JsonElement partJ : partJsonArray) {
//                boolean innerFlag = false;
//                for (JsonElement completeJ : completeJsonArray) {
//                    if (isContainer(completeJ, partJ)) {
//                        innerFlag = true;
//                        break;
//                    }
//                }
//                if (!innerFlag) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public static void main(String[] args) {
//        String s1 = XXX;//JSON字符串
//        String s2 = XXX;//另一个JSON字符串
//        boolean same = JsonSameUtil.same(s1, s2);
//        System.out.println(same);
//    }
//}
