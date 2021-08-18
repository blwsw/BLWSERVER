package com.hopedove.coreserver.global;

/**
 * 
 * @author hilbert.wang@hotmail.com<br>
 * Created on 2017年5月16日
 */
public class PayConstants {

	public static final String STATUS_INIT = "INIT"; // 初始
	public static final String STATUS_PREPAY = "PREPAY"; // 统一下单之后
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_CLOSED = "CLOSED"; // 关闭
	public static final String STATUS_REVOKED = "REVOKED"; // 已撤销
	public static final String STATUS_USERPAYING = "USERPAYING"; // 等待
	public static final String STATUS_PAYERROR = "PAYERROR"; // 错误
	
	public static final String PAY_TYPE_WECHAT = "1";
	public static final String PAY_TYPE_ALIPAY = "2";
	// 公众号支付
	public static final String PAY_TYPE_WECHAT_WEB = "3";
	
	public static final String PAY_TYPE_AMT = "5"; // 储值卡
	
	public static final String resultCodeSystemError = "SYSTEMERROR";
	public static final String resultCodeSuccess = "SUCCESS";

	public static final String getResultCodeSuccessYH = "00";
	public static final String resultCodeSuccessYH = "02";//"02"-- 交易成功

	public static final String PAY_ORDER_STATUS_WWC = "1";
	public static final String PAY_ORDER_STATUS_WC = "2";//订单状态(1:未完成、2:已完成)

	public static final String businessScenario ="YEBDTX";
}
