package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.IDUtils;
import cn.e3mall.utils.JsonUtils;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClientPool jedisClientPool;
	
	public TbItem getItemById(Long ItemId) {
		//根据主键查询商品
		try{
			String string = jedisClientPool.get("ITEM_INFO:"+ItemId+":BASE");
			if(StringUtils.isNoneBlank(string)){
				TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
				return tbItem;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(ItemId);
		if(tbItem!=null){
			try{
				jedisClientPool.set("ITEM_INFO:"+ItemId+":BASE", JsonUtils.objectToJson(tbItem));
				jedisClientPool.expire("ITEM_INFO:"+ItemId+":BASE",60);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return tbItem;
	}
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//创建返回结果集
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)pageInfo.getTotal());
		result.setRows(list);
		return result;
	}
	@Override
	public E3Result addItem(TbItem item, String desc) {
		// TODO Auto-generated method stub
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte)1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		tbItemMapper.insertSelective(item);
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		tbItemDescMapper.insertSelective(tbItemDesc);
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session arg0) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage createTextMessage = arg0.createTextMessage(itemId+"");
				return createTextMessage;
			}
		});
		
		return E3Result.ok();
	}
	@Override
	public E3Result getItemDescById(Long itemId) {
		// TODO Auto-generated method stub
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		return new E3Result(tbItemDesc);
	}
//	@Override
//	public E3Result getItemCatById(Long id) {
//		// TODO Auto-generated method stub
//		TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
//		TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
//		return new E3Result(tbItemCat);
//	}
	@Override
	public E3Result updateItem(TbItem item, String desc) {
		// TODO Auto-generated method stub
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		tbItemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(item.getId());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
		return E3Result.ok();
	}
	@Override
	public E3Result deleteItems(String params) {
		// TODO Auto-generated method stub
		String[] ids = params.split(",");
		for (String string : ids) {
			TbItem tbItem = new TbItem();
			tbItem.setId(Long.valueOf(string));
			tbItem.setStatus((byte)3);
			tbItemMapper.updateByPrimaryKeySelective(tbItem);
		}
		return E3Result.ok();
	}
	@Override
	public TbItemDesc getItemDescByItemId(Long ItemId) {
		// TODO Auto-generated method stub
		try{
			String string = jedisClientPool.get("ITEM_INFO:"+ItemId+":DESC");
			if(StringUtils.isNoneBlank(string)){
				TbItemDesc jsonToPojo = JsonUtils.jsonToPojo(string, TbItemDesc.class);
				return jsonToPojo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(ItemId);
		try{
			jedisClientPool.set("ITEM_INFO:"+ItemId+":DESC", JsonUtils.objectToJson(itemDesc));
			jedisClientPool.expire("ITEM_INFO:"+ItemId+":DESC", 60);
		}catch(Exception e){
			e.printStackTrace();
		}
		return itemDesc;
	}

}
