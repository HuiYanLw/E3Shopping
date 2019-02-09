package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClientPool jedisClientPool;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public E3Result LoginByUser(String username, String password) {
		// TODO Auto-generated method stub
		TbUserExample tbUserExample = new TbUserExample();
		tbUserExample.createCriteria().andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list==null||list.size()==0){
			return E3Result.build(400, "用户名或密码错误！");
		}
		TbUser tbUser = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
			return E3Result.build(400, "用户名或密码错误！");
		}
		String token = UUID.randomUUID().toString();
		tbUser.setPassword(null);
		jedisClientPool.set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
		jedisClientPool.expire("SESSION:"+token, SESSION_EXPIRE);
		return E3Result.ok(token);
	}

}
