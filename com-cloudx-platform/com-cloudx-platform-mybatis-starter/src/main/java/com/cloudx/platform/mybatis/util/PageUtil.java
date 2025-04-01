package com.cloudx.platform.mybatis.util;

import com.cloudx.common.entity.response.Page;
import com.github.pagehelper.PageInfo;
import java.util.function.Function;

/**
 * 分页工具类
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:56
 */
public class PageUtil {

    public static <E> Page<E> convert(PageInfo<E> pageInfo) {
        Page<E> page = new Page<>();
        page.setPage(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageSize());
        page.setTotal(pageInfo.getTotal());
        page.setPages(pageInfo.getPages());
        page.setList(pageInfo.getList());
        return page;
    }

    public static <E, R> Page<R> convert(PageInfo<E> pageInfo, Function<E, R> function) {
        Page<R> page = new Page<>();
        page.setPage(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageSize());
        page.setTotal(pageInfo.getTotal());
        page.setPages(pageInfo.getPages());
        page.setList(pageInfo.getList().stream().map(function).toList());
        return page;
    }

    public static <E, R> Page<R> convert(Page<E> page, Function<E, R> function) {
        Page<R> convert = new Page<>();
        convert.setPage(page.getPage());
        convert.setSize(page.getSize());
        convert.setTotal(page.getTotal());
        convert.setPages(page.getPages());
        convert.setList(page.getList().stream().map(function).toList());
        return convert;
    }
}
