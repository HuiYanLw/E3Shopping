package cn.e3mall.search.service.ActiveMq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.pojo.TbSearchItem;
import cn.e3mall.search.service.mapper.TbSearchItemMapper;

public class ItemChangeListener implements MessageListener {
	@Autowired
	private TbSearchItemMapper tbSearchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		try{
			TextMessage textMessage=(TextMessage)arg0;
			long itemId=Long.valueOf(textMessage.getText());
			Thread.sleep(1000);
			TbSearchItem tbSearchItem = tbSearchItemMapper.getSearchItemById(itemId);
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			solrInputDocument.addField("id", tbSearchItem.getId());
			solrInputDocument.addField("item_title", tbSearchItem.getTitle());
			solrInputDocument.addField("item_sell_point", tbSearchItem.getSell_point());
			solrInputDocument.addField("item_price", tbSearchItem.getPrice());
			solrInputDocument.addField("item_image", tbSearchItem.getImage());
			solrInputDocument.addField("item_category_name", tbSearchItem.getCategory_name());
			solrServer.add(solrInputDocument);
			solrServer.commit();
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
