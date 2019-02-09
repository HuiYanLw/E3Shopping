package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(Long parentId) {
		// TODO Auto-generated method stub
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
		List<EasyUITreeNode> easyUITreeNode=new ArrayList<EasyUITreeNode>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUITreeNode.add(treeNode);
		}
		return easyUITreeNode;
	}

}
