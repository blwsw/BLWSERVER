package com.hopedove.commons.exception;

import java.io.Serializable;

/**
 * 异常错误枚举定义
 */
public enum ErrorCode {
    EXP_TOKEN("登录失败"),
    EXP_NOUSER("用户账户不存在"),
    EXP_PASSWORD("密码错误"),
    EXP_PASSWORD_ENCODE("密码加密失败"),
    EXP_USER_NOMATCH("用户数据不匹配"),
    EXP_PWD_PWD2("前后2次密码不匹配"),
    EXP_PWD_OLDPWD("旧密码错误"),
    EXP_BAD_CREDENTIALS("无效凭证"),
    EXP_IDENTITY_MISMATCH("身份不匹配"),
    EXP_CODE_EXIST("编码已存在"),
    EXP_CODE_NULL("编码不能为空"),
    EXP_FACTORY_CODE_EXIST("工厂编码已存在"),
    EXP_STYLENUMBER_EXIST("款号已存在"),
    EXP_FORBIDDEN("权限不足"),
    EXP_SIGN("签名错误"),
    EXP_PARAMETER("缺少必要参数"),
    EXP_NODATA("未找到数据"),
    EXP_NODATA_USER("未找到用户数据"),
    EXP_MULTIPLE("找到多条数据"),
    EXP_CODE_NODATA("未找到编码规则"),
    EXP_CODE_RANGE("编码范围已耗尽"),
    EXP_NUMBER("数量不正确"),
    EXP_FILE_NOTEXISTS("文件不存在"),
    EXP_EXCEL_HEAD("EXCEL列头不符合规范"),
    EXP_IDENTIFY_EXIST("标识名称已存在"),
    EXP_VCOCE_NOTEXISTS("验证码已过期或不存在"),
    EXP_VCOCE("验证码错误"),
    EXP_LEVEL_EXIST("类型值已存在"),
    EXP_LEVELNAME_EXIST("类型定义已存在"),
    EXP_LEVELVALUE_EXIST("系数已存在"),
    EXP_NAME_EXIST("名称已存在"),
    EXP_PERMISSION_CODE_EXIST("权限编码已存在"),
    EXP_PERMISSION_CODE_NULL("权限编码不能为空"),
    ;

    ErrorCode(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
