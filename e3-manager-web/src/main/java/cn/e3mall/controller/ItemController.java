package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.JsonUtils;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public String addItem(TbItem item, String desc){
		return JsonUtils.objectToJson(itemService.addItem(item, desc));
	}
	
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public String getItemDescById(@PathVariable("itemId")Long itemId){
		return JsonUtils.objectToJson(itemService.getItemDescById(itemId));
	}
	
//	@RequestMapping("/rest/item/param/item/query/{id}")
//	@ResponseBody
//	public String getItemCatById(@PathVariable("id")Long id){
//		return JsonUtils.objectToJson(itemService.getItemCatById(id));
//	}
	
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public String updateItem(TbItem item, String desc){
		return JsonUtils.objectToJson(itemService.updateItem(item, desc));
	}
	
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public String deleteItems(String ids){
		return JsonUtils.objectToJson(itemService.deleteItems(ids));
	}
	
}
