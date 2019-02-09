package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.utils.E3Result;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	E3Result addContentCategory(long parentId, String name);
	E3Result deleteContentCategory(long parentId);
	E3Result updateContentCategory(long parentId,String name);
}
