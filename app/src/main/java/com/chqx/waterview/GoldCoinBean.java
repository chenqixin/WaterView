package com.chqx.waterview;

/**
 * author  ChenQiXin
 * date    2019-11-13
 * 描述   : 获取金币列表信息
 * 修订版本:
 */
public class GoldCoinBean {

    private String id;
    private int amt;
    //自定义-1 查看更多
    private int type;
    private String countdownTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(String countdownTime) {
        this.countdownTime = countdownTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }
}
