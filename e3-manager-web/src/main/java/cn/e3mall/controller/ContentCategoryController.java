package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.utils.E3Result;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0")long parentId){
		return contentCategoryService.getContentCategoryList(parentId);
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategory(long parentId, String name){
		return contentCategoryService.addContentCategory(parentId, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCategory(long id){
		return contentCategoryService.deleteContentCategory(id);
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCategory(@RequestParam(value="id")long parentId,String name){
		return contentCategoryService.updateContentCategory(parentId,name);
	}
}
