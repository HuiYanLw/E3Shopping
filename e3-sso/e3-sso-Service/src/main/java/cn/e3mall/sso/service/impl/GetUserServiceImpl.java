package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.jedis.JedisClientPool;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.GetUserService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;
@Service
public class GetUserServiceImpl implements GetUserService {
	@Autowired
	private JedisClientPool jedisClientPool;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		// TODO Auto-generated method stub
		String string = jedisClientPool.get("SESSION:"+token);
		if(!StringUtils.isNoneBlank(string)){
			return E3Result.build(400, "用户登录已过期，请重新登录！");
		}
		jedisClientPool.expire("SESSION:"+token, SESSION_EXPIRE);
		TbUser jsonToPojo = JsonUtils.jsonToPojo(string, TbUser.class);
		return E3Result.ok(jsonToPojo);
	}

}
