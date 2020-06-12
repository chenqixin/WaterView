package com.chqx.waterview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WaterView waterView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        initData();
        initListener();
    }


    private void initData() {

        List<GoldCoinBean> goldCoinBeanList = new ArrayList<>();
        GoldCoinBean goldCoinBean = new GoldCoinBean();
        goldCoinBean.setAmt(1);
        goldCoinBean.setCountdownTime("429000");
        goldCoinBean.setType(1);

        GoldCoinBean goldCoinBean1 = new GoldCoinBean();
        goldCoinBean1.setAmt(2);
        goldCoinBean1.setId("33");
        goldCoinBean1.setCountdownTime("30000");
        goldCoinBean1.setType(1);

        GoldCoinBean goldCoinBean2 = new GoldCoinBean();
        goldCoinBean2.setAmt(1);
        goldCoinBean2.setCountdownTime("21604000");
        goldCoinBean2.setType(0);


        goldCoinBeanList.add(goldCoinBean);
        goldCoinBeanList.add(goldCoinBean1);
        goldCoinBeanList.add(goldCoinBean2);

        for (int i = 0; i < 10; i++) {
            GoldCoinBean goldCoinBeanv = new GoldCoinBean();
            goldCoinBeanv.setAmt(i);
            goldCoinBeanv.setId(i + "");
            goldCoinBeanList.add(goldCoinBeanv);
        }
        waterView.setWaters(goldCoinBeanList);
    }


    private void initListener() {
        /**
         * 获取金币回调
         */
        waterView.setGrabCoinListener(new WaterView.OnCoinClickListener() {
            @Override
            public void onClick(GoldCoinBean goldCoin) {

            }
        });

        /**
         * 点击更多弹窗
         *
         */
        waterView.setMoreClickListener(new WaterView.OnMoreClickListener() {
            @Override
            public void onMoreClick() {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waterView.countDownTimer != null) {
            waterView.countDownTimer.cancel();
            waterView.countDownTimer = null;
        }
    }
}
