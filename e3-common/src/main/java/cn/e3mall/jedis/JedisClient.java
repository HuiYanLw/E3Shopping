package cn.e3mall.jedis;

import java.util.List;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Boolean hexists(String key,String field);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	List<String> hgets(String key);
	Long hdel(String key, String... field);
	void del(String key);
}
