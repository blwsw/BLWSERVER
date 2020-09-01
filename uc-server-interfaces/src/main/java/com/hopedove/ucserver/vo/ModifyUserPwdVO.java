package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

public class ModifyUserPwdVO extends BasicVO {
    private String oldPwd;

    private String newPwd;

    private String newPwd2;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getNewPwd2() {
        return newPwd2;
    }

    public void setNewPwd2(String newPwd2) {
        this.newPwd2 = newPwd2;
    }
}
