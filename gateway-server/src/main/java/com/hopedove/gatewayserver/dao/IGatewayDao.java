package com.hopedove.gatewayserver.dao;

import com.hopedove.gatewayserver.vo.GatewayApplication;

public interface IGatewayDao {
    GatewayApplication getGatewayApplication(GatewayApplication param);
}
