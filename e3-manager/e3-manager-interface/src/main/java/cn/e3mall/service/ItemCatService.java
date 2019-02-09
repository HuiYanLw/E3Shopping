package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getCatList(Long parentId);
}
