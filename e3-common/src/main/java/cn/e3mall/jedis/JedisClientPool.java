package cn.e3mall.jedis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientPool implements JedisClient {
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		jedisPool.close();
		return result;
	}
	@Override
	public void del(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.del(key);
		jedis.close();
		jedisPool.close();
	}
	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, field, value);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, field);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, field);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public Boolean hexists(String key, String field) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.hexists(key, field);
		jedis.close();
		jedisPool.close();
		return result;
	}

	@Override
	public List<String> hgets(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		List<String> hvals = jedis.hvals(key);
		jedis.close();
		jedisPool.close();
		return hvals;
	}

}
