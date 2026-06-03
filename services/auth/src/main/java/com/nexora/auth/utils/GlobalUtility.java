package com.nexora.auth.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class GlobalUtility {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        sortBy = sortBy == null ? "userName" : sortBy;
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }
}
