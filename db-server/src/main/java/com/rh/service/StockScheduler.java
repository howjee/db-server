package com.rh.service;

import com.rh.model.MoneyHistory;
import com.rh.model.Order;
import com.rh.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockScheduler {
    private final static Logger logger = LoggerFactory.getLogger(StockScheduler.class);

    @Autowired
    private StockService stockService;

    @Scheduled(cron = "0 43 23 * * *")
    public void deductDelayFeeEveryDay() {
        logger.info("start to deduct delay fee...");

        List<User> users = stockService.getAllUsers();
        if (users == null) {
            logger.info("no need to deduct delay fee for there is no user.");
            return;
        }

        for (User user : users) {
            List<Order> orders = stockService.getOrdersByUserId(user.getId());
            if (orders == null) {
                return;
            }

            double old_money = user.getMoney();
            double decrement = 0.0;
            for (Order order : orders) {
                if (order.isIs_valid() && Order.Status.DONE.getValue() == order.getStatus() && order.getDelay_fee() > 0) {
                    decrement += order.getDelay_fee();
                    if (user.getMoney() < 0) {
                        logger.warn("FIX ME: notify the user that you need to pay!!!");
                    }

                   /* Add delay fee deduction history to money_history table. */
                    MoneyHistory moneyHist = new MoneyHistory();
                    moneyHist.setUser_id(user.getId());
                    moneyHist.setType(MoneyHistory.Type.DELAY_FEE.getValue());
                    moneyHist.setValue(order.getDelay_fee());
                    moneyHist.setRemain(user.getMoney());
                    moneyHist.setDetail(Integer.toString(order.getId()));
                    stockService.addMoneyHistory(moneyHist);
                } else {
                    logger.info("There is no need to deduct delay fee for order " + order.toString());
                }
            }
            
            if (decrement > 0) {
                logger.info("Deduct " + Double.toString(decrement) + " for " + user.toString());
                stockService.addToUserMoney(user, decrement);
            } else {
                logger.info("There is no need to deduct for " + user.toString());
            }
        }

        logger.info("succeed to deduct delay fee!!");

    }
}
