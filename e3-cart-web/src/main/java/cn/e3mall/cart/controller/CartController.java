package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String setCartItem(@PathVariable Long itemId,@RequestParam(defaultValue="1")int num,
			HttpServletRequest request,HttpServletResponse response){
		//登录状态下
		TbUser tbUser=(TbUser)request.getAttribute("user");
		if(tbUser!=null){
			cartService.addCart(tbUser.getId(), itemId, num);
			return "cartSuccess";
		}
		//未登录状态下
		List<TbItem> list = this.getCartItemByCookie(request);
		boolean iscartexit=false;
		for (TbItem tbItem : list) {
			if(tbItem.getId()==itemId.longValue()){
				tbItem.setNum(tbItem.getNum()+num);
				iscartexit=true;
				break;
			}
		}
		if(!iscartexit){
			TbItem tbItem = itemService.getItemById(itemId);
			String image = tbItem.getImage();
			if(StringUtils.isNoneBlank(image)){
				String[] split = image.split(",");
				tbItem.setImage(split[0]);
			}
			tbItem.setNum(num);
			list.add(tbItem);
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_EXPIRE,true);
		return "cartSuccess";
	}
	
	public List<TbItem> getCartItemByCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		List<TbItem> jsonToList = JsonUtils.jsonToList(json, TbItem.class);
		return jsonToList;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response){
		List<TbItem> list = this.getCartItemByCookie(request);
		TbUser tbUser=(TbUser)request.getAttribute("user");
		if(tbUser!=null){
			//合并cook和redis中的商品列表
			cartService.mergeCart(tbUser.getId(), list);
			CookieUtils.deleteCookie(request, response, "cart");
			list = cartService.getCartlist(tbUser.getId());
		}
		request.setAttribute("cartList", list);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartItem(@PathVariable Long itemId,@PathVariable int num,
			HttpServletRequest request,HttpServletResponse response){
		TbUser tbUser=(TbUser)request.getAttribute("user");
		if(tbUser!=null){
			cartService.updateCartNum(tbUser.getId(), itemId, num);
			return E3Result.ok();
		}
		List<TbItem> list = this.getCartItemByCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId()==itemId.longValue()){
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_EXPIRE,true);
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		TbUser tbUser=(TbUser)request.getAttribute("user");
		if(tbUser!=null){
			cartService.deleteCartItem(tbUser.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		List<TbItem> list = this.getCartItemByCookie(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId()==itemId.longValue()){
				list.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_EXPIRE,true);
		return "redirect:/cart/cart.html";
	}
}
