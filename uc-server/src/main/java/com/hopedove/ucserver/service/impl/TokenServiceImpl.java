package com.hopedove.ucserver.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hopedove.commons.utils.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.LocalDateTimeUtil;
import com.hopedove.commons.utils.RSAUtils;
import com.hopedove.ucserver.dao.IPermissionDao;
import com.hopedove.ucserver.dao.ITokenDao;
import com.hopedove.ucserver.service.ITokenService;
import com.hopedove.ucserver.service.IVcodeService;
import com.hopedove.ucserver.vo.AuthTokenFormVO;
import com.hopedove.ucserver.vo.AuthTokenVO;
import com.hopedove.ucserver.vo.RefreshAuthTokenFormVO;
import com.hopedove.ucserver.vo.UserMenuVO;
import com.hopedove.ucserver.vo.UserPermissionVO;
import com.hopedove.ucserver.vo.UserRoleVO;
import com.hopedove.ucserver.vo.UserVO;
import com.hopedove.ucserver.vo.VcodeFormVO;
import com.hopedove.ucserver.vo.VcodeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "登录令牌服务")
@RestController
public class TokenServiceImpl implements ITokenService {

    private final static Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ITokenDao tokenDao;

    @Autowired
    private IPermissionDao permissionDao;

    @Resource(name = "vcodeService")
    private IVcodeService vcodeService;

    @ApiOperation(value = "获取登录令牌", notes = "通过用户名和密码换取")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "用户名和密码", required = true, dataType = "AuthTokenFormVO", paramType = "body", defaultValue = "{\"username\":\"admin\", \"password\":\"123456\"}")
    })
    @PostMapping("/tokens")
    public RestResponse<AuthTokenVO> createToken(@RequestBody AuthTokenFormVO param) throws Exception {
        //1.验证账号和密码
        UserVO user = tokenDao.login(param);
        if (user == null) {
            throw new BusinException(ErrorCode.EXP_NOUSER);
        }

        //加密密码
        String enPwd = null;
        try {
            enPwd = RSAUtils.encryptContent(param.getPassword(), RSAUtils.AESKEY);
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
        }

        if (!user.getPassword().equals(enPwd)) {
            throw new BusinException(ErrorCode.EXP_PASSWORD);
        }

        //校验验证码
//        vcodeService.validateVcode(param.getCode(), param.getCodeId());

        return new RestResponse<>(createToken(user));
    }

    @ApiOperation(value = "刷新登录令牌", notes = "通过refreshToken换取, 关于refreshToken的获取请看POST /tokens接口")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "刷新凭证", required = true, dataType = "RefreshAuthTokenFormVO", paramType = "body")
    })
    @PostMapping("tokens/refresh")
    public RestResponse<AuthTokenVO> refreshToken(@RequestBody RefreshAuthTokenFormVO param) throws Exception {
        // 缺少必要参数
        if (param == null || StringUtils.isEmpty(param.getUid()) || StringUtils.isEmpty(param.getRefreshToken())) {
            throw new BusinException(ErrorCode.EXP_PARAMETER);
        }

        String refreshTokenCiphertext = param.getRefreshToken();
        String refreshTokenSource = RSAUtils.decryptContent(refreshTokenCiphertext, RSAUtils.AESKEY);

        //无效凭证
        if (StringUtils.isEmpty(refreshTokenSource)) {
            throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
        }

        String[] refreshTokenSources = refreshTokenSource.split(",");

        if (refreshTokenSources.length < 2 || StringUtils.isEmpty(refreshTokenSources[1])) {
            //无效凭证
            throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
        }

        String tokencCiphertext = refreshTokenSources[0];

        String refreshTokenValidTime = refreshTokenSources[1];

        Long validTime = Long.parseLong(refreshTokenValidTime);

        if (LocalDateTimeUtil.getCurrentMill() > validTime) {
            //失效凭证
          //  throw new BusinException(ErrorCode.EXP_BAD_CREDENTIALS);
        }

        //先验证刷新凭证有效期
        AuthTokenVO authTokenVO = null;

        String tokenSource = RSAUtils.decryptContent(tokencCiphertext, RSAUtils.AESKEY);

        String[] tokenSources = tokenSource.split(",");

        String userId = tokenSources[0];
        String uid = tokenSources[1];
        String tokenValidTime = tokenSources[2];

        if (param.getUid().equals(uid)) {
            //重新生成新凭证
            UserVO user = tokenDao.getUserById(Integer.parseInt(userId));
            authTokenVO = createToken(user);
        } else {
            //身份不对
            throw new BusinException(ErrorCode.EXP_IDENTITY_MISMATCH);
        }
        return new RestResponse<>(authTokenVO);
    }

    /**
     * 创建令牌
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    private AuthTokenVO createToken(UserVO userVO) throws Exception {
        String uid = userVO.getUid();

        Integer userId = userVO.getId();

        String userType = userVO.getUserType();

        //1.加载用户角色
        List<UserRoleVO> userRoles = null;//tokenDao.getUserRoles(userId);

        //2.加载用户菜单权限
        List<UserMenuVO> userMenus = null;

        //如果是平台管理员用户，要取专门的平台角色权限
//        if ("PM".equals(userType)) {
//            userMenus = tokenDao.getPMRMenus();
//        } else {
//            userMenus = tokenDao.getUserMenus(userId);
//        }

        //3.加载用户权限(包括功能权限和数据权限)
        List<UserPermissionVO> userPermissions = null;//tokenDao.getUserUserPermissions(userId);

        //4.生成TOKEN
        LocalDateTime time = LocalDateTime.now();

        //4.1有效截止时间:当前时间 + 20分钟
        LocalDateTime validLocalDateTime = LocalDateTimeUtil.plus(time, 20, ChronoUnit.MINUTES);
        LocalDateTime validLocalDateTime2 = LocalDateTimeUtil.plus(time, 1, ChronoUnit.DAYS);

        //4.2create token
        String tokenSource = userId + "," + uid + "," + LocalDateTimeUtil.getMilliByTime(validLocalDateTime);
        String token = RSAUtils.encryptContent(tokenSource, RSAUtils.AESKEY);

        //刷新token：主token + 本token有效时间（时间1天有效）
        String refreshToken = RSAUtils.encryptContent(token + "," + LocalDateTimeUtil.getMilliByTime(validLocalDateTime2), RSAUtils.AESKEY);

        //用于缓存的对象
        AuthTokenVO cacheAuthToken = new AuthTokenVO(userVO, userRoles, null, null, token, validLocalDateTime, refreshToken, validLocalDateTime2);
        UserUtil.setCache(token, cacheAuthToken);

        //用于返回的对象
        AuthTokenVO authTokenVO = new AuthTokenVO(userVO, userRoles, userMenus, userPermissions, token, validLocalDateTime, refreshToken, validLocalDateTime2);

        return authTokenVO;
    }
}
