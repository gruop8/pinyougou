package com.pinyougou.service;

import java.util.List;
import java.util.Map;

public interface OrderSearchService {
    List<Map<String,Object>> search(Integer page, Integer rows, String userId);
}
