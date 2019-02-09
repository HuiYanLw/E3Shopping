package cn.e3mall.sso.service;

import cn.e3mall.pojo.TbUser;
import cn.e3mall.utils.E3Result;

public interface UserService {
	E3Result CheckDataToUser(String param,Integer type);
	E3Result RegisterUser(TbUser tbUser);
}
