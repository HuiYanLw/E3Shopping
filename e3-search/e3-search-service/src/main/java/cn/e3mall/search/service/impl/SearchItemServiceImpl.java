package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.pojo.TbSearchItem;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.search.service.mapper.TbSearchItemMapper;
import cn.e3mall.utils.E3Result;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private TbSearchItemMapper tbSearchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public E3Result getSearchItemList() {
		// TODO Auto-generated method stub
		try{
			List<TbSearchItem> list = tbSearchItemMapper.getSearchItemList();
			for (TbSearchItem tbSearchItem : list) {
				SolrInputDocument solrInputDocument = new SolrInputDocument();
				solrInputDocument.addField("id", tbSearchItem.getId());
				solrInputDocument.addField("item_title", tbSearchItem.getTitle());
				solrInputDocument.addField("item_sell_point", tbSearchItem.getSell_point());
				solrInputDocument.addField("item_price", tbSearchItem.getPrice());
				solrInputDocument.addField("item_image", tbSearchItem.getImage());
				solrInputDocument.addField("item_category_name", tbSearchItem.getCategory_name());
				//写入索引库
				solrServer.add(solrInputDocument);
			}
			solrServer.commit();
			return E3Result.ok();
		}catch(Exception e){
			System.out.println(e);
			return E3Result.build(500, "数据导入索引库失败！");
		}
	}
}
