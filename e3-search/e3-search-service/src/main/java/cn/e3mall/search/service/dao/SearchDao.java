package cn.e3mall.search.service.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.pojo.SearchResult;
import cn.e3mall.pojo.TbSearchItem;

@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) throws Exception{
		QueryResponse response = solrServer.query(query);
		SolrDocumentList results = response.getResults();
		long numFound = results.getNumFound();
		SearchResult searchResult = new SearchResult();
		searchResult.setRecourdCount((int)numFound);
		List<TbSearchItem> itemList=new ArrayList<>();
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : results) {
			//取商品信息
			TbSearchItem tbSearchItem = new TbSearchItem();
			tbSearchItem.setCategory_name((String)solrDocument.get("item_category_name"));
			tbSearchItem.setId((String)solrDocument.get("id"));
			tbSearchItem.setImage((String)solrDocument.get("item_image"));
			tbSearchItem.setPrice((long)solrDocument.get("item_price"));
			tbSearchItem.setSell_point((String)solrDocument.get("item_sell_point"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle="";
			if(list!=null&&list.size()>0){
				itemTitle=list.get(0);
			}else{
				itemTitle=(String)solrDocument.get("item_title");
			}
			tbSearchItem.setTitle(itemTitle);
			itemList.add(tbSearchItem);
		}
		searchResult.setItemList(itemList);
		return searchResult;
	}
	
}
