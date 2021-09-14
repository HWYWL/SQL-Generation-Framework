package com.github.hwywl.model;

import lombok.*;

import java.util.List;

/**
 * @author: YI
 * @description: SQL的排序方式
 * @date: create in 2021/3/8 16:55
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBy {
    /**
     * 需要排序的字段
     */
    List<String> fields;
    /**
     * 排序方式：ASC、DESC
     */
    String sort;
}
