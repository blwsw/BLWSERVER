package com.hopedove.commons.utils;

import com.hopedove.commons.vo.BasicVO;
import com.hopedove.commons.vo.GPBasicVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * VO的公共操作类
 * 主要实现：
 * 1.新增时填充公共字段disabled、create信息、update信息
 * 2.修改时填充update信息
 */
public class VOUtil {

    private final static Logger log = LoggerFactory.getLogger(VOUtil.class);

    //填充公共字段disabled、create信息
    public final static BasicVO fillCreate(BasicVO vo) {
        vo.setDisabled(0);
        vo.setCreateDatetime(LocalDateTime.now());
        vo.setCreateIp(getIP());
        vo.setUpdateDatetime(LocalDateTime.now());
        vo.setUpdateIp(getIP());

        vo.setCreateUserId(UserUtil.get("id", Integer.class));
        vo.setCreateUserName(UserUtil.get("name", String.class));
        vo.setCreateDeptId(UserUtil.get("deptId", Integer.class));



        vo.setUpdateUserId(UserUtil.get("id", Integer.class));
        vo.setUpdateUserName(UserUtil.get("name", String.class));
        vo.setUpdateDeptId(UserUtil.get("deptId", Integer.class));
        log.debug("填充vo.create = {}", JsonUtil.writeValueAsString(vo));
        return vo;
    }

    //填充公共字段disabled、update信息
    public final static BasicVO fillUpdate(BasicVO vo) {
        vo.setDisabled(0);
        vo.setUpdateDatetime(LocalDateTime.now());
        vo.setUpdateIp(getIP());

        vo.setUpdateUserId(UserUtil.get("id", Integer.class));
        vo.setUpdateUserName(UserUtil.get("name", String.class));
        vo.setUpdateDeptId(UserUtil.get("deptId", Integer.class));
        log.debug("填充vo.update = {}", JsonUtil.writeValueAsString(vo));
        return vo;
    }

    //填充公共字段disabled、create信息 (特殊场景：没有登录的情况下需要填充创建者等数据，由调用方直接传入)
    public final static BasicVO fillCreateSpecial(BasicVO vo, BasicVO certificateVO) {
        vo.setDisabled(0);
        vo.setCreateDatetime(LocalDateTime.now());
        vo.setCreateIp(getIP());

        vo.setCreateUserId(certificateVO.getCreateUserId());
        vo.setCreateUserName(certificateVO.getCreateUserName());
        vo.setCreateDeptId(certificateVO.getCreateDeptId());
        vo.setUpdateUserId(certificateVO.getCreateUserId());
        vo.setUpdateUserName(certificateVO.getCreateUserName());
        vo.setUpdateDeptId(certificateVO.getCreateDeptId());
        return vo;
    }

    //填充公共字段disabled、update信息 (特殊场景：没有登录的情况下需要填充创建者等数据，由调用方直接传入)
    public final static BasicVO fillUpdateSpecial(BasicVO vo, BasicVO certificateVO) {
        vo.setDisabled(0);
        vo.setCreateDatetime(LocalDateTime.now());
        vo.setCreateIp(getIP());

        vo.setUpdateUserId(certificateVO.getUpdateUserId());
        vo.setUpdateUserName(certificateVO.getUpdateUserName());
        vo.setUpdateDeptId(certificateVO.getUpdateDeptId());
        return vo;
    }

    public final static GPBasicVO fillCreate(GPBasicVO vo) {
        vo.setSCBJ(0);
        vo.setCJRQ(LocalDate.now());
        vo.setCJSJ(LocalDateTime.now());

        vo.setGXRQ(LocalDate.now());
        vo.setGXSJ(LocalDateTime.now());

        vo.setCJRID(UserUtil.get("id", Integer.class).longValue());
        vo.setCJR(UserUtil.get("name", String.class));

        vo.setGXRID(UserUtil.get("id", Integer.class).longValue());
        vo.setGXR(UserUtil.get("name", String.class));

        return vo;
    }

    public final static GPBasicVO fillUpdate(GPBasicVO vo) {
        vo.setSCBJ(0);

        vo.setGXRQ(LocalDate.now());
        vo.setGXSJ(LocalDateTime.now());

        vo.setGXRID(UserUtil.get("id", Integer.class).longValue());
        vo.setGXR(UserUtil.get("name", String.class));
        return vo;
    }

    public static String getIP() {
        HttpServletRequest request = ContextUtil.getRequest();

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 复制对象属性（对象类型必须相同）
     *
     * @param orig 资源对象
     * @param dest 目标对象
     * @param clazz 源对象类
     * @param ignoreNull 是否忽略空（true:忽略，false：不忽略）
     * @return
     */
    public static <T> T copyProperties(T orig, T dest, Class<?> clazz, boolean ignoreNull) {
        if (orig == null || dest == null)
            return null;
        if(!clazz.isAssignableFrom(orig.getClass()))
            return null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(orig);
                if(!java.lang.reflect.Modifier.isFinal(field.getModifiers())){
                    if(!(ignoreNull && value == null)){
                        field.set(dest, value);
                    }
                }
                field.setAccessible(false);
            } catch (Exception e) {
            }
        }

        if(clazz.getSuperclass() == Object.class){
            return dest;
        }

        return copyProperties(orig, dest, clazz.getSuperclass(), ignoreNull);
    }

    /**
     * 复制对象属性（对象类型必须相同）
     * @param orig 资源对象
     * @param dest 目标对象
     * @param ignoreNull 是否忽略空（true:忽略，false：不忽略）
     */
    public static <T> T copyProperties(T orig, T dest, boolean ignoreNull) {
        if (orig == null || dest == null)
            return null;
        return copyProperties(orig, dest, orig.getClass(), ignoreNull);
    }

    /**
     * 复制对象
     * @param src 资源对象
     * @return 新对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T src){
        if(src == null){
            return null;
        }
        T newObject = null;
        try {
            newObject = (T) src.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return copyProperties(src, newObject, false);
    }
}
