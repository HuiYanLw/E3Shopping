package cn.e3mall.search.service.mapper;

import java.util.List;

import cn.e3mall.pojo.TbSearchItem;

public interface TbSearchItemMapper {
	List<TbSearchItem> getSearchItemList();
	TbSearchItem getSearchItemById(long id);
}
