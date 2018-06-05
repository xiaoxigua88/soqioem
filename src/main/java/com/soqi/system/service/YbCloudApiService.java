package com.soqi.system.service;


public interface YbCloudApiService {
	/** 错误码 */
    public static final String RSP_CODE = "RspCode";

    /** 返回码描述 */
    public static final String RSP_DESC = "RspDesc";
    
    /**
     * @param action 动作
     * @param data
     * @return
     */
    String apiDo(String action, String data);
}
