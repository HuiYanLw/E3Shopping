package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.utils.E3Result;

public interface CartService {
	E3Result addCart(long userId,long itemId,int num);
	E3Result mergeCart(long userId,List<TbItem> itemlist);
	List<TbItem> getCartlist(long userId);
	E3Result updateCartNum(long userId,long itemId,int num);
	E3Result deleteCartItem(long userId,long itemId);
	E3Result clearCartItem(long userId);
}
