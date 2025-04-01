package com.cloudx.common.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对象转换工具
 * @author heycm
 * @version 1.0
 * @since 2024/12/3 23:52
 */
public class ConvertHelper {

    private ConvertHelper() {
    }

    public static <E, R> List<R> toList(Collection<E> collection, Function<E, R> function) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        List<R> list = new ArrayList<>(collection.size());
        collection.forEach(item -> list.add(function.apply(item)));
        return list;
    }

    public static <E, R> List<R> toList(Collection<E> collection, BiFunction<E, Integer, R> function) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        List<R> list = new ArrayList<>(collection.size());
        int index = 0;
        for (E item : collection) {
            list.add(function.apply(item, index));
            index++;
        }
        return list;
    }
}
