package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	@Autowired
	private ContentService contentService;
	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	@RequestMapping("/index")
	public String getIndex(Model model){
		List<TbContent> contentListBycId = contentService.getContentListBycId(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", contentListBycId);
		return "index";
	}
}
