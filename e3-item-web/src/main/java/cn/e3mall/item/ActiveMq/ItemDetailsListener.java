package cn.e3mall.item.ActiveMq;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemDetailsListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GET_PATH}")
	private String HTML_GET_PATH;
	@Override
	public void onMessage(Message arg0) {
		try{
			// TODO Auto-generated method stub
			TextMessage textMessage=(TextMessage)arg0;
			Long itemId = Long.valueOf(textMessage.getText());
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescByItemId(itemId);
			Map<Object, Object> data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out =new FileWriter(HTML_GET_PATH+itemId+".html");
			template.process(data, out);
			out.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
