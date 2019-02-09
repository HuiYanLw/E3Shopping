package cn.e3mall.order.service;

import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.utils.E3Result;

public interface OrderService {
	public E3Result creatOrder(OrderInfo orderInfo);
}
