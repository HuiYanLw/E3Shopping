package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private static final long serialVersionUID=-1l;
	private int totalPages;
	private int recourdCount;
	private List<TbSearchItem> itemList;
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(int recourdCount) {
		this.recourdCount = recourdCount;
	}
	public List<TbSearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbSearchItem> itemList) {
		this.itemList = itemList;
	}

}
