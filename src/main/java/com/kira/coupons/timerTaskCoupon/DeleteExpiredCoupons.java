package com.kira.coupons.timerTaskCoupon;//package com.kira.coupons.timerTaskCoupon;


import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.logic.CouponsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class DeleteExpiredCoupons {
    private CouponsLogic couponsLogic;

    @Autowired
    public DeleteExpiredCoupons(CouponsLogic couponsLogic) {
        this.couponsLogic = couponsLogic;
    }


    @Scheduled (cron = "0  * * * * ?")
    void executeDailyTask() throws ServerException {
        new Thread(()->{
            try {
                couponsLogic.deleteExpiredCoupons();
            } catch (ServerException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
