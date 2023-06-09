//package com.example.zhh;
//import org.assertj.core.data.Index;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.awt.print.Book;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.logging.Level;
//
//import static java.util.concurrent.ThreadLocalRandom.current;
//
///**
// * @author xhliu
// * @create 2022-03-18-15:00
// **/
//@SpringBootTest
//public class Solution {
//    static final Set<Class<?>> BASIC_CLASS_SET = new HashSet<>();
//
//    static {
//        BASIC_CLASS_SET.add(Number.class);
//        BASIC_CLASS_SET.add(Byte.class);
//        BASIC_CLASS_SET.add(Short.class);
//        BASIC_CLASS_SET.add(Integer.class);
//        BASIC_CLASS_SET.add(Long.class);
//        BASIC_CLASS_SET.add(Float.class);
//        BASIC_CLASS_SET.add(Double.class);
//        BASIC_CLASS_SET.add(BigDecimal.class);
//        BASIC_CLASS_SET.add(BigInteger.class);
//
//        BASIC_CLASS_SET.add(String.class);
//        BASIC_CLASS_SET.add(Character.class);
//        BASIC_CLASS_SET.add(Date.class);
//        BASIC_CLASS_SET.add(java.sql.Date.class);
//        BASIC_CLASS_SET.add(LocalDateTime.class);
//        BASIC_CLASS_SET.add(LocalDate.class);
//        BASIC_CLASS_SET.add(Instant.class);
//    }
//
//    final static class Node<T> {
//        final T oldVal, newVal;
//
//        public Node(T oldVal, T newVal) {
//            this.oldVal = oldVal;
//            this.newVal = newVal;
//        }
//
//        @Override
//        public String toString() {
//            return "Node{" +
//                    "oldVal=" + oldVal +
//                    ", newVal=" + newVal +
//                    '}';
//        }
//    }
//
//    /**
//     * 比较两个对象之间的每个属性是否完全一致，如果不一致，
//     * 则使用一个 Map 记录不同的属性位置以及原有旧值和新值
//     *
//     * @param o1 : 旧有数据对象
//     * @param o2 : 新的数据对象
//     * @return : 如果两个对象的属性完全一致，返回 true，否则，返回 false
//     */
//    boolean compObject(Object o1, Object o2) {
//        Map<String, Node<Object>> map = new HashMap<>();
//        boolean res = dfs(o1, o2, "", map);
//
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.setPrettyPrinting().create();
//
//        System.out.println(gson.toJson(map));
//
//        return res;
//    }
//
//    boolean dfs(Object o1, Object o2, String prefix, Map<String, Node<Object>> map) {
//        if (o1 == null && o2 == null) return true;
//        if (o1 == null) {
//            map.put(prefix, new Node<>(null, o2));
//            return false;
//        }
//
//        if (o2 == null) {
//            map.put(prefix, new Node<>(o1, null));
//            return false;
//        }
//
//        checkParams(o1.getClass() != o2.getClass(), "o1 和 o2 的对象类型不一致");
//
//        boolean res = true;
//
//        // 检查当前对象的属性以及属性对象的子属性的值是否一致
//        Field[] fields = o1.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            int modifiers = field.getModifiers();
//            if ((modifiers & Modifier.STATIC) != 0) continue; // 过滤静态修饰符修饰的字段
//            field.setAccessible(true);
//
//            String curFiled = prefix + (prefix.length() > 0 ? "." : "") + field.getName();
//            try {
//                final Class<?> fieldClass = field.getType();
//                Object v1 = field.get(o1), v2 = field.get(o2);
//                if (checkHandle(checkBasicType(v1, v2, fieldClass, curFiled, map))) continue;
//
//                if (checkHandle(checkCollection(v1, v2, fieldClass, curFiled, map))) continue;
//
//                if (checkHandle(checkMap(v1, v2, fieldClass, curFiled, map))) continue;
//
//                if (checkHandle(checkEnum(v1, v2, fieldClass))) continue;
//
//                res &= dfs(v1, v2, curFiled, map);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return res;
//    }
//
//    final static int EQUALS     =   1 << 1;     // 表示比较的对象的当前属性相等
//    final static int NO_EQUALS  =   1 << 2;     // 表示当前对象的类型能够进行处理，但是两个对象值并不相等
//    final static int DISABLE    =   1 << 3;     // 表示当前传入的对象该方法无法进行处理
//
//    boolean checkHandle(int handle) {
//        return handle == EQUALS || handle == NO_EQUALS;
//    }
//
//    int checkBasicType(
//            Object v1, Object v2, Class<?> fieldClass,
//            String curFiled, Map<String, Node<Object>> map
//    ) {
//
//        if (isBasicType(fieldClass)) {
//            if (v1 == null && v2 == null) return EQUALS;
//            if (v1 == null) {
//                map.put(curFiled, new Node<>(null, v2));
//                return NO_EQUALS;
//            }
//
//            if (v2 == null) {
//                map.put(curFiled, new Node<>(v1, null));
//                return NO_EQUALS;
//            }
//
//            if (equalsObj(v1, v2)) return 1 << 1;
//            map.put(curFiled, new Node<>(v1, v2));
//
//            return NO_EQUALS;
//        }
//
//        return DISABLE;
//    }
//
//    int checkCollection(
//            Object v1, Object v2, Class<?> fieldClass,
//            String curFiled, Map<String, Node<Object>> map
//    ) {
//        if (isCollection(fieldClass))
//            return equalsCollect((Collection<?>) v1, (Collection<?>) v2, curFiled, map) ? EQUALS : NO_EQUALS;
//        return DISABLE;
//    }
//
//    int checkMap(
//            Object v1, Object v2, Class<?> fieldClass,
//            String curFiled, Map<String, Node<Object>> map
//    ) {
//        if (isMap(fieldClass))
//            return equalsMap((Map<?, ?>) v1, (Map<?, ?>) v2, curFiled, map) ? EQUALS : NO_EQUALS;
//
//        return DISABLE;
//    }
//
//    int checkEnum(
//            Object v1, Object v2, Class<?> fieldClass
//    ) {
//        if (isEnum(fieldClass)) {
//            return equalsEnum((Enum<?>) v1, (Enum<?>) v2) ? EQUALS : NO_EQUALS;
//        }
//
//        return DISABLE;
//    }
//
//    boolean isBasicType(Class<?> c) {
//        if (c.isPrimitive()) return true;
//        return BASIC_CLASS_SET.contains(c);
//    }
//
//    boolean isCollection(Class<?> c) {
//        return Collection.class.isAssignableFrom(c);
//    }
//
//    boolean isMap(Class<?> c) {
//        return Map.class.isAssignableFrom(c);
//    }
//
//    boolean isEnum(Class<?> c) {
//        return c == Enum.class;
//    }
//
//    // 检查两个枚举类型数据是否相同
//    boolean equalsEnum(Enum<?> o1, Enum<?> o2) {
//        checkParams(o1.getClass() != o2.getClass(), "o1 和 o2 不同时为枚举类型");
//        return o1 == o2;
//    }
//
//    /**
//     * 比较两个对象是否相等，如果对象实现了 Comparable 接口，则使用 compareTo 方法进行比较
//     * 否则使用 Object 的 equals 方法进行对象的比较
//     *
//     * @param o1 : 旧值数据对象
//     * @param o2 : 新值数据对象
//     */
//    @SuppressWarnings("unchecked")
//    boolean equalsObj(Object o1, Object o2) {
//        if (o1 instanceof Comparable)
//            return ((Comparable<Object>) o1).compareTo(o2) == 0;
//        return o1.equals(o2);
//    }
//
//    /**
//     * 判断两个集合（Collection）中的元素是否相同，这里的实现只针对 Set 和 List <br />
//     * 对于 Set : 如果存在不同的元素，则直接将两个集合作为比较对象保存到 differMap 中 <br />
//     * 对于 List : 如果相同的索引位置的元素不同，那么会记录当前元素的索引位置的新旧值到 differMap，
//     * 如果两个列表的长度不一致，则会使用 null 来代替不存在的元素
//     * 对于元素的比较同样基于 {@code dfs}
//     * <br />
//     * <br />
//     * 对于其它的集合类型，将会抛出一个 {@code RuntimeException}
//     * <br />
//     * <br />
//     *
//     * @param c1        : 旧集合数据对象
//     * @param c2        : 新集合数据对象
//     * @param prefix    : 当前集合属性所在的级别的前缀字符串表现形式
//     * @param differMap : 存储不同属性字段的 Map
//     */
//    boolean equalsCollect(
//            Collection<?> c1, Collection<?> c2,
//            String prefix, Map<String, Node<Object>> differMap
//    ) {
//        checkParams(c1.getClass() != c2.getClass(), "集合 c1 和 c2 的类型不一致.");
//
//        /*
//            对于集合来讲，只能大致判断一下两个集合的元素是否是一致的，
//            这是由于集合本身不具备随机访问的特性，因此如果两个集合存在不相等的元素，
//            那么将会直接将两个集合存储的不同节点中
//         */
//        if (c1 instanceof Set) {
//            // 分别计算两个集合的信息指纹
//            long h1 = 0, h2 = 0;
//            long hash = BigInteger
//                    .probablePrime(32, current())
//                    .longValue(); // 随机的大质数用于随机化信息指纹
//
//            Set<?> s1 = (Set<?>) c1, s2 = (Set<?>) c2;
//
//            for (Object obj : s1) h1 += genHash(obj) * hash;
//            for (Object obj : s2) h2 += genHash(obj) * hash;
//
//            if (h1 != h2) {
//                differMap.put(prefix, new Node<>(s1, s2));
//                return false;
//            }
//
//            return true;
//        }
//
//        /*
//            对于列表来讲，由于列表的元素存在顺序，
//            因此可以针对不同的索引位置的元素进行对应的比较
//         */
//        if (c1 instanceof List) {
//            boolean res = true;
//            List<?> list1 = (List<?>) c1, list2 = (List<?>) c2;
//            Map<String, Node<Object>> tmpMap = new HashMap<>(); // 记录相同索引位置的索引元素的不同
//
//            int i;
//            for (i = 0; i < list1.size() && i < list2.size(); ++i) {
//                res &= dfs(list1.get(i), list2.get(i), prefix + "#" + i, tmpMap);
//            }
//
//            differMap.putAll(tmpMap); // 添加到原有的不同 differMap 中
//
//            // 后续如果集合存在多余的元素，那么肯定这两个位置的索引元素不同
//            while (i < list1.size()) {
//                res = false;
//                differMap.put(prefix + "#" + i, new Node<>(list1.get(i), null));
//                i++;
//            }
//            while (i < list2.size()) {
//                res = false;
//                differMap.put(prefix + "#" + i, new Node<>(null, list2.get(i)));
//                i++;
//            }
//            // 后续元素处理结束
//
//            return res;
//        }
//
//        log.debug("type={}", c1.getClass());
//        throw new RuntimeException("未能处理的集合类型异常");
//    }
//
//    /**
//     * 比较两个 Map 属性值，对于不相交的 key，使用 null 来代替现有的 key 值
//     * 当两个 Map 中都存在相同的 key 是，将会使用递归处理 {@code dfs} 继续比较 value 是否一致
//     *
//     * @param m1        : 旧值属性对象 Map 字段
//     * @param m2        : 新值属性对象的 Map 字段
//     * @param prefix    : 此时已经处理的对象的字段深度
//     * @param differMap : 记录不同的属性值的 Map
//     */
//    boolean equalsMap(
//            Map<?, ?> m1, Map<?, ?> m2,
//            String prefix, Map<String, Node<Object>> differMap
//    ) {
//        checkParams(m1.getClass() != m2.getClass(), "map1 和 map2 类型不一致");
//
//        boolean res = true;
//
//        // 首先比较两个 Map 都存在的 key 对应的 value 对象
//        for (Object key : m1.keySet()) {
//            String curPrefix = prefix + "." + key;
//            if (!m2.containsKey(key)) { // 如果 m2 不包含 m1 的 key，此时是一个不同元素值
//                differMap.put(curPrefix, new Node<>(m1.get(key), null));
//                res = false;
//                continue;
//            }
//
//            res &= dfs(m1.get(key), m2.get(key), curPrefix, differMap);
//        }
//        // 检查 m1 中存在没有 m2 的 key 的情况
//        for (Object key : m2.keySet()) {
//            String curPrefix = prefix + "." + key;
//            if (!m1.containsKey(key)) {
//                differMap.put(curPrefix, new Node<>(null, m2.get(key)));
//                res = false;
//            }
//        }
//
//        return res;
//    }
//
//    private void checkParams(boolean b, String s) {
//        if (b) {
//            throw new RuntimeException(s);
//        }
//    }
//
//    static int genHash(Object obj) {
//        return Objects.hashCode(obj);
//    }
//
//    private final static Logger log = LoggerFactory.getLogger(Solution.class);
//
//    @Test
//    public void compareTest() {
//        Solution solution = new Solution();
//
//        Author author1 = new Author(
//                "Richard", 44, "Unix Professor",
//                new Education("MIT", Level.MASTER)
//        );
//
//        Author author2 = new Author(
//                "Ken Thomson", 65, "Unix Invented",
//                new Education("BSD", Level.PHD)
//        );
//
//        Author author3 = new Author(
//                "Ritchie Denis", 44, "C Programmer",
//                new Education("MIT", Level.PHD)
//        );
//
//        Author author4 = new Author(
//                "Brain Kenihan", 55, "Awk Programmer",
//                new Education("CMU", Level.PHD)
//        );
//
//        Index index1 = new Index("comics", 10);
//        Index index2 = new Index("introduction", 2);
//        Index index3 = new Index("algorithm", 50);
//        Index index4 = new Index("performance", 100);
////        Index index5 = new Index("design", 200);
//
//        List<Index> indexList1 = new ArrayList<>();
//        indexList1.add(index1);
//        indexList1.add(index2);
//
//        List<Index> indexList2 = new ArrayList<>();
//        indexList2.add(index3);
//        indexList2.add(index4);
//
//        Set<Author> authors1 = new HashSet<>();
//        authors1.add(author1);
//        authors1.add(author2);
//
//        Set<Author> authors2 = new HashSet<>();
//        authors2.add(author3);
//        authors2.add(author4);
//
//        Book book1 = new Book(indexList1, "APUE", authors1);
//        Book book2 = new Book(indexList2, "CSAPP", authors2);
//
//        book1.setFlag((short) 10);
//        book2.setFlag((short) 10);
//
//        solution.compObject(book1, book2);
////        Gson gson = new GsonBuilder().setPrettyPrinting().create();
////        System.out.println(gson.toJson(book1));
//    }
//}
