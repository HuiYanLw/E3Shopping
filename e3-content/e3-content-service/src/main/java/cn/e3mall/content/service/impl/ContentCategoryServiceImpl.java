package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.utils.E3Result;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// TODO Auto-generated method stub
		TbContentCategoryExample categoryExample = new TbContentCategoryExample();
		categoryExample.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(categoryExample);
		List<EasyUITreeNode> nodeList=new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbContentCategory.getId());
			easyUITreeNode.setText(tbContentCategory.getName());
			easyUITreeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(easyUITreeNode);
		}
		
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// TODO Auto-generated method stub
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		Date date = new Date();
		tbContentCategory.setCreated(date);
		tbContentCategory.setUpdated(date);
		tbContentCategoryMapper.insertSelective(tbContentCategory);
		TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(contentCategory.getIsParent()!=null){
			contentCategory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		}
		return E3Result.ok(tbContentCategory);
	}

	@Override
	public E3Result deleteContentCategory(long parentId) {
		// TODO Auto-generated method stub
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(tbContentCategory.getIsParent()==true){
			getTbContentCategoryList(parentId);
			tbContentCategoryMapper.deleteByPrimaryKey(parentId);
		}else{
			tbContentCategoryMapper.deleteByPrimaryKey(parentId);
		}
		return E3Result.ok();
	}
	public void getTbContentCategoryList(long parentId){
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.clear();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> selectByExample = tbContentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : selectByExample) {
			if(tbContentCategory.getIsParent()==true){
				getTbContentCategoryList(tbContentCategory.getId());
				tbContentCategoryMapper.deleteByPrimaryKey(tbContentCategory.getId());
			}else{
				tbContentCategoryMapper.deleteByPrimaryKey(tbContentCategory.getId());
			}
		}
	}

	@Override
	public E3Result updateContentCategory(long parentId, String name) {
		// TODO Auto-generated method stub
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setId(parentId);
		tbContentCategory.setName(name);
		tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		return E3Result.ok();
	}
}
