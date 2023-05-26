//package com.example.demo;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//import java.util.*;
//
//class CampareJson {
//    public String campareJsonObject(String oldJsonStr, String newJsonStr1) {
//        //将字符串转换为json对象
//        JSON oldJson = JSON.parseObject(oldJsonStr);
//        JSON newJson = JSON.parseObject(newJsonStr1);
//        System.out.println(oldJson);
//        System.out.println(newJson);
//        //递归遍历json对象所有的key-value，将其封装成path:value格式进行比较
//        Map<String, Object> oldMap = new LinkedHashMap<>();
//        Map<String, Object> newMap = new LinkedHashMap<>();
//        convertJsonToMap(oldJson, "", oldMap);
//        convertJsonToMap(newJson, "", newMap);
//        Map<String, Object> differenceMap = campareMap(oldMap, newMap);
//        //将最终的比较结果把不相同的转换为json对象返回
//        String jsonObject = convertMapToJson(differenceMap);
//        return jsonObject;
//    }
//
//    /**
//     * 将json数据转换为map存储用于比较
//     *
//     * @param json
//     * @param root
//     * @param resultMap
//     */
//    private void convertJsonToMap(Object json, String root, Map<String, Object> resultMap) {
//        if (json instanceof JSONObject) {
//            JSONObject jsonObject = ((JSONObject) json);
//            Iterator iterator = jsonObject.keySet().iterator();
//            while (iterator.hasNext()) {
//                Object key = iterator.next();
//                Object value = jsonObject.get(key);
//                String newRoot = "".equals(root) ? key + "" : root + "." + key;
//                if (value instanceof JSONObject || value instanceof JSONArray) {
//                    convertJsonToMap(value, newRoot, resultMap);
//                } else {
//                    resultMap.put(newRoot, value);
//                }
//            }
//        } else if (json instanceof JSONArray) {
//            JSONArray jsonArray = (JSONArray) json;
//            for (int i = 0; i < jsonArray.size(); i++) {
//                Object vaule = jsonArray.get(i);
//                String newRoot = "".equals(root) ? "[" + i + "]" : root + ".[" + i + "]";
//                if (vaule instanceof JSONObject || vaule instanceof JSONArray) {
//                    convertJsonToMap(vaule, newRoot, resultMap);
//                } else {
//                    resultMap.put(newRoot, vaule);
//                }
//            }
//        }
//    }
//
//    /**
//     * 比较两个map，返回不同数据
//     *
//     * @param oldMap
//     * @param newMap
//     * @return
//     */
//    private Map<String, Object> campareMap(Map<String, Object> oldMap, Map<String, Object> newMap) {
//        //遍历newMap，将newMap的不同数据装进oldMap，同时删除oldMap中与newMap相同的数据
//        campareNewToOld(oldMap, newMap);
//        //將舊的有新的沒有的數據封裝數據結構存在舊的裡面
//        campareOldToNew(oldMap);
//        return oldMap;
//    }
//
//    /**
//     * 將舊的有新的沒有的數據封裝數據結構存在舊的裡面
//     * @param oldMap
//     * @return
//     */
//    private void campareOldToNew(Map<String, Object> oldMap) {
//        //统一oldMap中newMap不存在的数据的数据结构，便于解析
//        for (Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<String, Object> item = it.next();
//            String key = item.getKey();
//            Object value = item.getValue();
//            int lastIndex = key.lastIndexOf(".");
//            if (!(value instanceof Map)) {
//                Map<String, Object> differenceMap = new HashMap<>();
//                differenceMap.put("oldValue", value);
//                differenceMap.put("newValue", "");
//                oldMap.put(key, differenceMap);
//            }
//        }
//    }
//
//    /**
//     * 將新的map與舊的比較，並將數據統一存在舊的裡面
//     * @param oldMap
//     * @param newMap
//     */
//    private void campareNewToOld(Map<String, Object> oldMap, Map<String, Object> newMap) {
//        for (Iterator<Map.Entry<String, Object>> it = newMap.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<String, Object> item = it.next();
//            String key = item.getKey();
//            Object newValue = item.getValue();
//            newValue = Optional.ofNullable(newValue).orElse("null");
//
//            Map<String, Object> differenceMap = new HashMap<>();
//            int lastIndex = key.lastIndexOf(".");
//            String lastPath = key.substring(lastIndex + 1).toLowerCase();
//            if (oldMap.containsKey(key)) {
//                Object oldValue =  oldMap.get(key);
//                oldValue = Optional.ofNullable(oldValue).orElse("null");
//                if (oldValue.equals(newValue)) {
//                    oldMap.remove(key);
//                    continue;
//                } else {
//                    differenceMap.put("oldValue", oldValue);
//                    differenceMap.put("newValue", newValue);
//                    oldMap.put(key, differenceMap);
//                }
//            } else {
//                differenceMap.put("oldValue", "");
//                differenceMap.put("newValue", newValue);
//                oldMap.put(key, differenceMap);
//            }
//        }
//    }
//
//    /**
//     * 将已经找出不同数据的map根据key的层级结构封装成json返回
//     *
//     * @param map
//     * @return
//     */
//    private String convertMapToJson(Map<String, Object> map) {
//        JSONObject resultJSONObject = new JSONObject();
//        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<String, Object> item = it.next();
//            String key = item.getKey();
//            Object value = item.getValue();
//            String[] paths = key.split("\\.");
//            int i = 0;
//            Object remarkObject = null;//用於深度標識對象
//            int indexAll = paths.length - 1;
//            while (i <= paths.length - 1) {
//                String path = paths[i];
//                if (i == 0) {
//                    //初始化对象标识
//                    if (resultJSONObject.containsKey(path)) {
//                        remarkObject = resultJSONObject.get(path);
//                    } else {
//                        if (indexAll > i) {
//                            if (paths[i + 1].matches("\\[[0-9]+\\]")) {
//                                remarkObject = new JSONArray();
//                            } else {
//                                remarkObject = new JSONObject();
//                            }
//                            resultJSONObject.put(path, remarkObject);
//                        } else {
//                            resultJSONObject.put(path, value);
//                        }
//                    }
//                    i++;
//                    continue;
//                }
//                if (path.matches("\\[[0-9]+\\]")) {//匹配集合对象
//                    int startIndex = path.lastIndexOf("[");
//                    int endIndext = path.lastIndexOf("]");
//                    int index = Integer.parseInt(path.substring(startIndex + 1, endIndext));
//                    if (indexAll > i) {
//                        if (paths[i + 1].matches("\\[[0-9]+\\]")) {
//                            while (((JSONArray) remarkObject).size() <= index) {
//                                if(((JSONArray) remarkObject).size() == index){
//                                    ((JSONArray) remarkObject).add(index,new JSONArray());
//                                }else{
//                                    ((JSONArray) remarkObject).add(null);
//                                }
//                            }
//                        } else {
//                            while(((JSONArray) remarkObject).size() <= index){
//                                if(((JSONArray) remarkObject).size() == index){
//                                    ((JSONArray) remarkObject).add(index,new JSONObject());
//                                }else{
//                                    ((JSONArray) remarkObject).add(null);
//                                }
//                            }
//                        }
//                        remarkObject = ((JSONArray) remarkObject).get(index);
//                    } else {
//                        while(((JSONArray) remarkObject).size() <= index){
//                            if(((JSONArray) remarkObject).size() == index){
//                                ((JSONArray) remarkObject).add(index, value);
//                            }else{
//                                ((JSONArray) remarkObject).add(null);
//                            }
//                        }
//                    }
//                } else {
//                    if (indexAll > i) {
//                        if (paths[i + 1].matches("\\[[0-9]+\\]")) {
//                            if(!((JSONObject) remarkObject).containsKey(path)){
//                                ((JSONObject) remarkObject).put(path, new JSONArray());
//                            }
//                        } else {
//                            if(!((JSONObject) remarkObject).containsKey(path)){
//                                ((JSONObject) remarkObject).put(path, new JSONObject());
//                            }
//                        }
//                        remarkObject = ((JSONObject) remarkObject).get(path);
//                    } else {
//                        ((JSONObject) remarkObject).put(path, value);
//                    }
//                }
//                i++;
//            }
//        }
//        return JSON.toJSONString(resultJSONObject);
//
//    }
//
//    public static void main(String[] args) {
////        String oldStr= "{a:'aaa',b:'bbb',c:['a','b']}";
////        String newStr= "{a:'aaa',b:'cc',c:['a','v']}";
////        String oldStr =  "{\"id\":\"1\",\"name\":\"n1\",\"height\":null,\"list\":[1,2,3],\"age\":0,\"items\":[{\"id\":\"11\",\"name\":\"n11\",\"copyId\":\"1\"},{\"id\":\"12\",\"name\":\"n12\",\"copyId\":\"2\"}]}";
////        String newStr = "{\"id\":\"1\",\"name\":\"n2\",\"height\":180,\"list\":[3,4,5],\"age\":null,\"items\":[{\"id\":\"11\",\"name\":\"n11\",\"copyId\":\"3\"},{\"id\":\"12\",\"name\":\"n13\",\"copyId\":\"2\"}]}";
//          String newold = "{\n" +
//                  "  'reason': '查询成功!',\n" +
//                  "  'result': {\n" +
//                  "    'city': '苏州',\n" +
//                  "    'realtime': {\n" +
//                  "      'temperature': '17',\n" +
//                  "      'humidity': '69',\n" +
//                  "      'info': '阴',\n" +
//                  "      'wid': '02',\n" +
//                  "      'direct': '东风',\n" +
//                  "      'power': '2级',\n" +
//                  "      'aqi': '30'\n" +
//                  "    },\n" +
//                  "    'future': [\n" +
//                  "      {\n" +
//                  "        'date': '2021-10-25',\n" +
//                  "        'temperature': '12\\/21℃',\n" +
//                  "        'weather': '多云',\n" +
//                  "        'wid': {\n" +
//                  "          'day': '01',\n" +
//                  "          'night': '01'\n" +
//                  "        },\n" +
//                  "        'direct': '东风'\n" +
//                  "      },\n" +
//                  "      {\n" +
//                  "        'date': '2021-10-26',\n" +
//                  "        'temperature': '13\\/21℃',\n" +
//                  "        'weather': '多云',\n" +
//                  "        'wid': {\n" +
//                  "          'day': '01',\n" +
//                  "          'night': '01'\n" +
//                  "        },\n" +
//                  "        'direct': '东风转东北风'\n" +
//                  "      },\n" +
//                  "      {\n" +
//                  "        'date': '2021-10-27',\n" +
//                  "        'temperature': '13\\/22℃',\n" +
//                  "        'weather': '多云',\n" +
//                  "        'wid': {\n" +
//                  "          'day': '01',\n" +
//                  "          'night': '01'\n" +
//                  "        },\n" +
//                  "        'direct': '东北风'\n" +
//                  "      },\n" +
//                  "      {\n" +
//                  "        'date': '2021-10-28',\n" +
//                  "        'temperature': '13\\/21℃',\n" +
//                  "        'weather': '多云转晴',\n" +
//                  "        'wid': {\n" +
//                  "          'day': '01',\n" +
//                  "          'night': '00'\n" +
//                  "        },\n" +
//                  "        'direct': '东北风'\n" +
//                  "      },\n" +
//                  "      {\n" +
//                  "        'date': '2021-10-29',\n" +
//                  "        'temperature': '14\\/21℃',\n" +
//                  "        'weather': '多云转小雨',\n" +
//                  "        'wid': {\n" +
//                  "          'day': '01',\n" +
//                  "          'night': '07'\n" +
//                  "        },\n" +
//                  "        'direct': '东北风'\n" +
//                  "      }\n" +
//                  "    ]\n" +
//                  "  },\n" +
//                  "  'error_code': 0\n" +
//                  "}";
//        System.out.println(new CampareJson().campareJsonObject(oldStr,newStr));
//    }
//}
//
