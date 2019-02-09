package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.utils.E3Result;

@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	private E3Result getSearchItemList(){
		return searchItemService.getSearchItemList();
	}
}
