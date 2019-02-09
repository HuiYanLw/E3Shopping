package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.e3mall.utils.E3Result;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	@Autowired
	private JedisClientPool jedisClientPool;
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	
	@Override
	public E3Result creatOrder(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		//利用redis生成id
		if(!jedisClientPool.exists(ORDER_GEN_KEY)){
			jedisClientPool.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}
		String oderId=jedisClientPool.incr(ORDER_GEN_KEY).toString();
		orderInfo.setOrderId(oderId);
		orderInfo.setPostFee("0");
		orderInfo.setStatus(1);
		Date date = new Date();
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		tbOrderMapper.insertSelective(orderInfo);
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			Long incr = jedisClientPool.incr(ORDER_ITEM_ID_GEN_KEY);
			tbOrderItem.setId(incr.toString());
			tbOrderItem.setOrderId(oderId);
			tbOrderItemMapper.insertSelective(tbOrderItem);
		}
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(oderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		tbOrderShippingMapper.insertSelective(orderShipping);
		return E3Result.ok(oderId);
	}

}
