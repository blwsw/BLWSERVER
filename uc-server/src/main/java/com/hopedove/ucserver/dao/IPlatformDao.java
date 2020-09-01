package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.PlatformVO;

import java.util.List;
import java.util.Map;

public interface IPlatformDao {

    List<PlatformVO> getPlatforms(Map<String, Object> param);

    int getPlatformsCount(Map<String, Object> param);

    PlatformVO getPlatform(PlatformVO platformVo);

    int addPlatform(PlatformVO platformVo);

    int modifyPlatform(PlatformVO platformVo);

}
