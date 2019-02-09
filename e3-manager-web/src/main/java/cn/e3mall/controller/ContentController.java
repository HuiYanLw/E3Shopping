package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.utils.E3Result;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentListByCategoryId(long categoryId,int page,int rows){
		return contentService.getContentListByCategoryId(categoryId, page, rows);
	}
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent tbContent){
		return contentService.addContent(tbContent);
	}
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(String ids){
		return contentService.deleteContent(ids);
	}
}
