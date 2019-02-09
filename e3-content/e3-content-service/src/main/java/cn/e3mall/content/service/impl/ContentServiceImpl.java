package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClientPool jedisClientPool;
	@Override
	public EasyUIDataGridResult getContentListByCategoryId(long categoryId,int page,int rows) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, rows);
		TbContentExample tbContentExample = new TbContentExample();
		tbContentExample.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = tbContentMapper.selectByExample(tbContentExample);
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)pageInfo.getTotal());
		result.setRows(list);
		return result;
	}

	@Override
	public E3Result addContent(TbContent tbContent) {
		// TODO Auto-generated method stub
		Date date = new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		tbContentMapper.insert(tbContent);
		try{
			String json = jedisClientPool.hget("ContentBigadver",tbContent.getCategoryId()+"");
			if(StringUtils.isNoneBlank(json)){
				jedisClientPool.hdel("ContentBigadver",tbContent.getCategoryId()+"");
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContent(String ids) {
		// TODO Auto-generated method stub
		String[] ides = ids.split(",");
		for (String id : ides) {
			TbContent tbContent = tbContentMapper.selectByPrimaryKey(Long.valueOf(id));
			try{
				String json = jedisClientPool.hget("ContentBigadver",tbContent.getCategoryId()+"");
				if(StringUtils.isNoneBlank(json)){
					jedisClientPool.hdel("ContentBigadver",tbContent.getCategoryId()+"");
				}
			}catch(Exception e){
				System.out.println(e);
			}
			tbContentMapper.deleteByPrimaryKey(Long.valueOf(id));
		}
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListBycId(long cid) {
		// TODO Auto-generated method stub
		try{
			String json = jedisClientPool.hget("ContentBigadver",cid+"");
			if(StringUtils.isNoneBlank(json)){
				List<TbContent> jsonToList = JsonUtils.jsonToList(json, TbContent.class);
				return jsonToList;
			}
		}catch(Exception e){
			System.out.println(e);
		}
		TbContentExample tbContentExample = new TbContentExample();
		tbContentExample.createCriteria().andCategoryIdEqualTo(cid);
		List<TbContent> example = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
		try{
			String json = JsonUtils.objectToJson(example);
			jedisClientPool.hset("ContentBigadver",cid+"",json);
		}catch(Exception e){
			System.out.println(e);
		}
		return example;
	}

}
