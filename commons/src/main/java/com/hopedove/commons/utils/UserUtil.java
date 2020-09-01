package com.hopedove.commons.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 用户信息工具类
 * 1.获取当前token对应的用户信息
 * <p>
 * 目前通过get方法可用的key如下：
 * id
 * username
 * name
 * userType
 * enterpriseId
 * factoryId
 * deptId
 * deptCode
 * workcenterId
 * uid
 */
@Component
public class UserUtil {

    private final static Logger log = LoggerFactory.getLogger(UserUtil.class);

    //L1 cache 的数据键前缀，完整键应该是前缀+本地键
    private final static String L1_KEY = "cache:L1:user:";

    //L1 cache 失效时间
    private final static long INVALID_TIMEOUT = 1;

    //L1 cache 失效时间模式
    private final static TimeUnit INVALID_TIMEUNIT = TimeUnit.DAYS;//.MINUTES;

    //L1 client
    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate__;

    //L2 cache
    private static Cache<Object, Object> CACHEMAP = null;

    @PostConstruct
    public void init() {
        stringRedisTemplate = stringRedisTemplate__;
        log.debug("初始化stringRedisTemplate = {},stringRedisTemplate__= {}", stringRedisTemplate, stringRedisTemplate__);

//        try {
//            CACHEMAP = CacheBuilder.newBuilder()
//                    .maximumSize(100) // 设置缓存的最大容量
//                    .expireAfterWrite(INVALID_TIMEOUT, INVALID_TIMEUNIT) // 设置缓存在写入一分钟后失效
//                    .concurrencyLevel(10) // 设置并发级别为10
//                    .recordStats() // 开启缓存统计
//                    .build();
//        } catch (Exception e) {
//            log.error("初始化本地缓存CacheBuilder异常", e);
//        }
    }

    //获取缓存数据
    public static Map<String, Object> getCache(String key) {
        String cacheJson = stringRedisTemplate.opsForValue().get(L1_KEY + key);
        stringRedisTemplate.expire(L1_KEY + key, INVALID_TIMEOUT, INVALID_TIMEUNIT);
        return JsonUtil.readValue(cacheJson, Map.class);
    }

    //设置缓存数据
    public static void setCache(String key, Object value) {
        stringRedisTemplate.opsForValue().set(L1_KEY + key, JsonUtil.writeValueAsString(value));
        stringRedisTemplate.expire(L1_KEY + key, INVALID_TIMEOUT, INVALID_TIMEUNIT);
    }

    //获得当前登录用户的会话数据
    private final static Map<String, Object> getUserSession() {
        String token = ContextUtil.getRequest().getHeader("token");
        log.debug("从http header中获取token = {}", token);
        if (StringUtils.isEmpty(token)) {
            throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
        }

        try {
            Map<String, Object> contentMap = getCache(token);
            if(contentMap == null) {
                throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
            }
//            log.debug("User Session = {}", JsonUtil.writeValueAsString(contentMap));
            return contentMap;
        } catch (Exception e) {
            log.error("从token中获取用户会话信息失败", e);
            throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
        }
    }

    //获得当前登录用户的会话数据中的具体用户属性
    public final static <T> T get(String filedName, Class<T> tClass) {
        Map<String, Object> contentMap = getUserSession();
        Map<String, Object> userMap = (Map<String, Object>) contentMap.get("user");
//        log.debug("User Session -> UserVO = {}", JsonUtil.writeValueAsString(userMap));
//        log.debug("User Session -> UserVO -> userId = {}", userMap.get("id"));
//        log.debug("User Session -> UserVO -> userId.class = {}", userMap.get("id").getClass());

        return (T) userMap.get(filedName);
    }
    
    //获得当前登录用户的会话数据中的具体用户属性
    @SuppressWarnings("unchecked")
	public final static List<Map<String, Object>>  getUserRoles() {
        Map<String, Object> contentMap = getUserSession();
		List<Map<String, Object>> userRoleList =  (List<Map<String, Object>>) contentMap.get("userRoles");
        return userRoleList;
    }
    
    public static void main(String[] args) {
        long randomSeed = 19890918;
        Random random = new Random(randomSeed);

        for (int i = 0; i < 5; i++) {
            Map<String, Boolean> filter = new HashMap<>();

            int count = 0;
            String result = "";
            //33选6
            while (true) {
                if (count == 6) {
                    break;
                }
                int red = random.nextInt(34);
                if (red == 0) {
                    continue;
                }

                String filterKey = red + "";
                if (filter.get(filterKey) != null) {
                    continue;
                }

                filter.put(filterKey, true);
                result += red + " ";
                count++;
            }
            result += " - ";
            //16选1
            while (true) {
                int blue = random.nextInt(17);
                if (blue == 0) {
                    continue;
                }
                result += blue;
                break;
            }

            System.out.println("中奖号码：" + result);
        }
    }
}
