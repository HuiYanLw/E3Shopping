package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.utils.E3Result;

@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		TbUser user =(TbUser) request.getAttribute("user");
		List<TbItem> cartlist = cartService.getCartlist(user.getId());
		request.setAttribute("cartList", cartlist);
		return "order-cart";
	}
	
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo, HttpServletRequest request){
		TbUser user =(TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		E3Result creatOrder = orderService.creatOrder(orderInfo);
		if(creatOrder.getStatus()==200){
			cartService.clearCartItem(user.getId());
		}
		request.setAttribute("orderId", creatOrder.getData().toString());
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}
	
}
