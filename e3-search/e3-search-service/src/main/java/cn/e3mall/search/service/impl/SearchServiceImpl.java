package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import cn.e3mall.search.service.dao.SearchDao;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		SolrQuery query = new SolrQuery();
		query.setQuery(keyWord);
		query.setStart((page-1)*rows);
		query.setRows(rows);
		query.set("df", "item_title");
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		SearchResult search = searchDao.search(query);
		int count = search.getRecourdCount();
		int pages=(int)(count/rows);
		if(count%rows>0)pages++;
		search.setTotalPages(pages);
		return search;
	}

}
