package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.utils.E3Result;

public interface ContentService {
	EasyUIDataGridResult getContentListByCategoryId(long categoryId,int page,int rows); 
	E3Result addContent(TbContent tbContent);
	E3Result deleteContent(String ids);
	List<TbContent> getContentListBycId(long cid);
}
