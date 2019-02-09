package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

public class Item extends TbItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String[] getImages() {
		String image2 = this.getImage();
		if (image2 != null && !"".equals(image2)) {
			String[] strings = image2.split(",");
			return strings;
		}
		return null;
	}
	public Item() {
		
	}
	
	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}

}
