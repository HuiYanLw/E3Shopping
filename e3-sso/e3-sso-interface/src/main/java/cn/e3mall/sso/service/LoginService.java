package cn.e3mall.sso.service;

import cn.e3mall.utils.E3Result;

public interface LoginService {
	E3Result LoginByUser(String username,String password);
}
