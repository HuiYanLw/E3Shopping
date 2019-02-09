package cn.e3mall.search.service;

import cn.e3mall.pojo.SearchResult;

public interface SearchService {
	public SearchResult search(String keyWord, int page, int rows)throws Exception;
}
