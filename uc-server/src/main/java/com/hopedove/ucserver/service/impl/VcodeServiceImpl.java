package com.hopedove.ucserver.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.RSAUtils;
import com.hopedove.ucserver.service.IVcodeService;
import com.hopedove.ucserver.vo.VcodeFormVO;
import com.hopedove.ucserver.vo.VcodeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "验证码服务")
@RestController("vcodeService")
public class VcodeServiceImpl implements IVcodeService {
    private final static Logger log = LoggerFactory.getLogger(VcodeServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取验证码
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码", notes = "通过用户名和工厂代码换取")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "用户名和工厂代码", required = true, dataType = "VcodeFormVO", paramType = "body", defaultValue = "{\"username\":\"admin2\", \"factoryCode\":\"XZF\"}")
    })
    @PostMapping("/vcode")
    public RestResponse<VcodeVO> getVcode(@RequestParam(required = false) String vid) {
        if (StringUtils.isNotEmpty(vid)) {
            String key = "vcode:" + vid;
            stringRedisTemplate.delete(key);
        }
        return new RestResponse<>(createVcode());
    }

    @ApiOperation(value = "校验验证码", notes = "通过用户名和工厂代码获取")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "验证Vcode", required = true, dataType = "VcodeFormVO", paramType = "body")
    })

    @PostMapping("/vcode/validate")
    public void validateVcode(@RequestParam String vcode, @RequestParam String vid) {
        // 缺少必要参数
        if (StringUtils.isEmpty(vcode) || StringUtils.isEmpty(vcode)) {
            throw new BusinException(ErrorCode.EXP_VCOCE);
        }
        String key = "vcode:" + vid;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            log.debug("验证码验证不通过 input:{}, {}; target:{}", vcode, vid, value);
            throw new BusinException(ErrorCode.EXP_VCOCE_NOTEXISTS);
        }

        if (!value.equals(vcode)) {
            log.debug("验证码验证不通过 input:{}, {}; target:{}", vcode, vid, value);
            throw new BusinException(ErrorCode.EXP_VCOCE);
        }

        log.debug("验证码验证通过，清除验证码缓存");
        stringRedisTemplate.delete(key);
    }

    /**
     * 创建验证码
     *
     * @return
     * @throws Exception
     */
    private VcodeVO createVcode() {
        String vid = UUID.randomUUID().toString();
        String vcode = "";

        //位数
        int digit = 4;
        //ascii
        //数字 48 - 57
        //小写字母 97 - 122

        //随机数奇偶性决定： 奇数取数字，偶数取字母
        Random random = new Random();
        for (int i = 0; i < digit; i++) {
            char c = (char) (random.nextInt(10) + 48);
//            if (random.nextInt(10) % 2 == 0) {
//                c = (char) (random.nextInt(10) + 48);
//            } else {
//                c = (char) (random.nextInt(26) + 97);
//            }
            vcode += c;
        }

        //缓存 有效截止时间:当前时间 + 20分钟
        String key = "vcode:" + vid;
        stringRedisTemplate.opsForValue().set(key, vcode);
        stringRedisTemplate.expire(key, 2, TimeUnit.MINUTES);

        return new VcodeVO(vcode, vid);
    }

}
