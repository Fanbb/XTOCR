package com.ocr.system.domain;

public class SysUserChannel {
    /**
     * 角色ID
     */
    private Long userId;

    /**
     * 渠道号
     */
    private String channelCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    @Override
    public String toString() {
        return "SysUserChannel{" +
                "userId=" + userId +
                ", channelCode='" + channelCode + '\'' +
                '}';
    }
}
