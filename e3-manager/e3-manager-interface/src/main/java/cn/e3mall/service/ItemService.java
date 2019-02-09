package cn.e3mall.service;

import cn.e3mall.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.utils.E3Result;

public interface ItemService{
	TbItem getItemById(Long ItemId);
	EasyUIDataGridResult getItemList(Integer page,Integer rows);
	E3Result addItem(TbItem item, String desc);
	E3Result getItemDescById(Long itemId);
//	E3Result getItemCatById(Long id);
	E3Result updateItem(TbItem item, String desc);
	E3Result deleteItems(String params);
	TbItemDesc getItemDescByItemId(Long ItemId);
}
