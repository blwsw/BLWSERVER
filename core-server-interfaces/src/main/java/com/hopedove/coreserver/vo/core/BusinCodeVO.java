package com.hopedove.coreserver.vo.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel("业务编码特殊参数实体")
public class BusinCodeVO extends BasicVO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime test;

    public LocalDateTime getTest() {
        return test;
    }

    public void setTest(LocalDateTime test) {
        this.test = test;
    }
}
