package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.E3Result;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Override
	public E3Result CheckDataToUser(String param, Integer type) {
		// TODO Auto-generated method stub
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		if(type==1){
			criteria.andUsernameEqualTo(param);
		}else if(type==2){
			criteria.andPhoneEqualTo(param);
		}else if(type==3){
			criteria.andEmailEqualTo(param);
		}
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list!=null&&list.size()>0){
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	@Override
	public E3Result RegisterUser(TbUser tbUser) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(tbUser.getUsername())||StringUtils.isBlank(tbUser.getPassword())){
			return E3Result.build(400, "提交数据不全！");
		}
		if(!(boolean)CheckDataToUser(tbUser.getUsername(),1).getData()){
			return E3Result.build(400, "用户名已被注册！");
		}
		if(!(boolean)CheckDataToUser(tbUser.getPhone(),2).getData()){
			return E3Result.build(400, "手机号码已被注册！");
		}
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5DigestAsHex);
		Date date = new Date();
		tbUser.setCreated(date);
		tbUser.setUpdated(date);
		tbUserMapper.insertSelective(tbUser);
		return E3Result.ok();
	}

}
