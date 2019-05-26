package com.ljb.utils.utils.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements Filter {

    List<Filter> filters = new ArrayList<>();
    int index = 0;

    public FilterChain addFilter(Filter fileter) {
        filters.add(fileter);
        return this;
    }

    @Override
    public String doFilter(FilterChain chain) {
        if (index >= filters.size()) {
            return null;
        }
        Filter f = filters.get(index);
        index++;
        return f.doFilter(chain);

    }

}
