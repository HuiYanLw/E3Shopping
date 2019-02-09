package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClientPool jedisClientPool;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		// TODO Auto-generated method stub
		Boolean hexists = jedisClientPool.hexists(REDIS_CART_PRE+":"+userId, itemId+"");
		if(hexists){
			String json = jedisClientPool.hget(REDIS_CART_PRE+":"+userId, itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			jedisClientPool.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		String image = tbItem.getImage();
		if(StringUtils.isNoneBlank(image)){
			String[] split = image.split(",");
			tbItem.setImage(split[0]);
		}
		jedisClientPool.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemlist) {
		// TODO Auto-generated method stub
		for (TbItem tbItem : itemlist) {
			addCart(userId,tbItem.getId(),tbItem.getNum());
		}
		return E3Result.ok();
	}
	@Override
	public List<TbItem> getCartlist(long userId) {
		// TODO Auto-generated method stub
		List<String> list = jedisClientPool.hgets(REDIS_CART_PRE+":"+userId);
		List<TbItem> tbItemlist=new ArrayList<>();
		for (String string : list) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			tbItemlist.add(tbItem);
		}
		return tbItemlist;
	}
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		// TODO Auto-generated method stub
		String json = jedisClientPool.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClientPool.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}
	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		// TODO Auto-generated method stub
		jedisClientPool.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return E3Result.ok();
	}
	@Override
	public E3Result clearCartItem(long userId) {
		// TODO Auto-generated method stub
		jedisClientPool.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}
