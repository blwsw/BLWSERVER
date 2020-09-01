package com.hopedove.gatewayserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.exception.GatewayException;
import com.hopedove.commons.exception.SignException;
import com.hopedove.commons.response.ErrorBody;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.RSAUtils;
import com.hopedove.gatewayserver.config.GatewayConfig;
import com.hopedove.gatewayserver.service.IGatewayService;
import com.hopedove.gatewayserver.vo.GatewayApplication;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

//todo 接口的登录验证，需要整理出哪些接口需要登录验证，哪些不需要
public class AuthFilter extends ZuulFilter {

    private final static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final static String[] version = {"v1"};

    private final static String[] ignores = {"/files"};

    @Autowired
    private IGatewayService gatewayService;

    @Autowired
    private GatewayConfig gatewayConfig;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("__token__");
//        return sholdVerify(request.getRequestURI());
        return false;
    }

    //判定为需要验签的请求返回True
    private final static boolean sholdVerify(String requestURI) {
        for (String ignore : ignores) {
            if (requestURI.indexOf(ignore) > -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.debug("requestURI = {} ", request.getRequestURI());
        log.debug("requestURL = {} ", request.getRequestURL().toString());
        //获取http header中参数
        String appId = request.getHeader("__appId__");
        String timestamp = request.getHeader("__timestamp__");
        String version = request.getHeader("__version__");
        String uid = request.getHeader("__uid__");
        String sign = request.getHeader("__sign__");
        String token = request.getHeader("__token__");

        //获取请求参数
        String queryString = request.getQueryString();
        log.debug("request queryString = {}", queryString);

        String queryURI = request.getRequestURI();
        log.debug("request queryURI = {}", queryURI);

        try {
//            //查询对应的应用信息
//            GatewayApplication application = new GatewayApplication();
//            application.setAppId(appId);
//            application = gatewayService.getGatewayApplication(application);
//            //http://domain.com ?  a=1&b=
//            //加签规则：MD5withRSA(appId + version + timestamp + queryURI + queryString + MD5withRSA(body))
//            StringBuffer sb = new StringBuffer();
//
//            if (appId != null) {
//                sb.append(appId);
//            }
//
//            if (version != null) {
//                sb.append(version);
//            }
//
//            if (timestamp != null) {
//                sb.append(timestamp);
//            }
//
//            if (queryURI != null) {
//                sb.append(queryURI);
//            }
//
//            if (queryString != null) {
//                sb.append(queryString);
//            }
//
//            //获取body参数
//            String body = null;
//            if (!ctx.isChunkedRequestBody()) {
//                ServletInputStream inp = null;
//                inp = ctx.getRequest().getInputStream();
//                if (inp != null) {
//                    body = IOUtils.toString(inp);
//                    log.debug("request body : {}", body);
//                }
//            }
//
//            if (body != null) {
//                sb.append(body);
//            }
//
//
//            //公钥验签，私钥加签
//
//            boolean isOK = RSAUtils.verify(sb.toString().getBytes(), application.getAppPublicKey(), sign);
//
//            log.debug("验签结果 >>>> {}", isOK);

//            if (isOK) {
//            } else {
//                throw new SignException();
//            }
        } catch (Exception e) {
            log.error("验签异常", e);
            if (e instanceof SignException) {
                throw (SignException) e;
            } else {
                throw new GatewayException("BAD_GATEWAY", "网关内部错误");
            }
        }
        return null;
    }


}
