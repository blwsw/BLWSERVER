package com.hopedove.gatewayserver.service.impl;

import com.hopedove.gatewayserver.dao.IGatewayDao;
import com.hopedove.gatewayserver.service.IGatewayService;
import com.hopedove.gatewayserver.vo.GatewayApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayServiceImpl implements IGatewayService {

    @Autowired
    private IGatewayDao gatewayDao;

    @Override
    public GatewayApplication getGatewayApplication(GatewayApplication param) {
        return gatewayDao.getGatewayApplication(param);
    }
}
