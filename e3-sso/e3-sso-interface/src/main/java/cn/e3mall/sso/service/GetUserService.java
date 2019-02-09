package cn.e3mall.sso.service;

import cn.e3mall.utils.E3Result;

public interface GetUserService {
	E3Result getUserByToken(String token);
}
