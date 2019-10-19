package com.rh.service;

import com.rh.controller.util.StringUtils;
import com.rh.mapper.StockMapper;
import com.rh.model.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockMapper stockMapper;

    private final static Logger logger = LoggerFactory.getLogger(StockService.class);


    /* ====================================*/
    /* Interfaces for table user and user_auths. */
    /* ====================================*/
    public String register(UserAuths userAuth, int permission) {
        if (StringUtils.isEmpty(userAuth.getIdentity_type()) || !userAuth.getIdentity_type().equals("phone"))
            return "invalid identity type";

        if (StringUtils.isEmpty(userAuth.getIdentifier()) || !StringUtils.isMobile(userAuth.getIdentifier())) {
            return "invalid phone number";
        }

        if(StringUtils.isEmpty(userAuth.getCredential())) {
            return "invalid password";
        }

        String response = null;
        try {
            String nickName = null;
            if (getUserIdWithIdentifier(userAuth) > 0) {
                response = "user exists";
            } else {
                String nickname = genarateNickName();
                if (nickname == null) {
                    response = "system error";
                } else {
                    User user = new User();
                    user.setNickname(nickname);
                    user.setPermission(User.Permission.USER.getValue());
                    int user_id = addUser(user);
                    if (user_id < 0) {
                        response = "system error";
                    } else {
                        userAuth.setUser_id(user_id);
                        user_id = addUserAuth(userAuth);
                        if (user_id < 0) {
                            response = "system error";
                        } else {
                            response = "success";
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("register failed, ", e);
        } finally {
            return response;
        }
    }

    public int addUser(User user) {
        stockMapper.addUser(user);

        return getUserIdByNickName(user.getNickname());
    }

    public int addUserAuth(UserAuths userAuth) {
        stockMapper.addUserAuth(userAuth);
        return getUserIdWithCredential(userAuth);
    }

    public int modifyPassword(UserAuths userAuth) {
        stockMapper.modifyPassword(userAuth);
        List<Integer> res = stockMapper.getUserIdWithIdentifier(userAuth);
        if (res != null && res.size() > 0) {
            return res.get(0).intValue();
        }

        return -1;
    }

    public void modifyNickname(User user) {
        stockMapper.modifyNickName(user);
    }

    public void updateUserPermission(User user) {
        stockMapper.updateUserPermission(user);
    }

    public int getUserIdByNickName(String nickname) {
        User user = new User();
        user.setNickname(nickname);
        List<Integer> res = stockMapper.getUserIdByNickName(user);
        if (res != null && res.size() > 0) {
            return res.get(0).intValue();
        }

        return -1;
    }

    public String genarateNickName() {
        int retryCount = 10;
        String nickname = null;
        while (retryCount > 0) {
            String randomUserName = StringUtils.getStringRandom(8);
            int resId = getUserIdByNickName(randomUserName);
            if (resId < 0) {
                return randomUserName;
            }
        }

        return null;
    }

    public int getUserIdWithIdentifier(UserAuths userAuths) {
        List<Integer> res = stockMapper.getUserIdWithIdentifier(userAuths);
        if (res != null && res.size() > 0) {
            return res.get(0).intValue();
        }

        return -1;
    }

    public int getUserIdWithCredential(UserAuths userAuths) {
        List<Integer> res = stockMapper.getUserIdWithCredential(userAuths);
        if (res != null && res.size() > 0) {
            return res.get(0).intValue();
        }

        return -1;
    }

    public User getUser(int id) {
        User user = new User();
        user.setId(id);
        return stockMapper.getUser(user).get(0);
    }

    public List<User> getAllUsers() {
        return stockMapper.getAllUsers();
    }

    public String addToUserMoney(User user, double increment) {
        /* Update money_history first, then user money table in case updating history fails. */
        String res = "success";
        try {
            user.setMoney(increment);
            stockMapper.addToUserMoney(user);
        } catch (Exception e) {
            res = "failure";
            logger.error("add money to user error, ", e);
        }

        /* FIXME: notify user to recharge if remain is not enough. */
        if (user.getMoney() <= 0) {
        }

        return res;
    }

    public String addToUserMoney(User user, double increment, MoneyHistory.Type type) {
        /* Update money_history first, then user money table in case updating history fails. */
        String res = "success";
        try {
            double remain = user.getMoney();
            MoneyHistory hist = new MoneyHistory();
            hist.setUser_id(user.getId());
            hist.setType(type.getValue());
            hist.setValue(increment);
            hist.setRemain(remain);
            stockMapper.addMoneyHistory(hist);

            user.setMoney(increment);
            stockMapper.addToUserMoney(user);
        } catch (Exception e) {
            res = "failure";
            logger.error("add money to user error, ", e);
        }

        /* FIXME: notify user to recharge if remain is not enough. */
        if (user.getMoney() <= 0) {
        }

        return res;
    }

    public double getUserMoney(int id) {
        User user = new User();
        user.setId(id);
        return stockMapper.getUserMoney(user);
    }

    /*======================================*/
    /* Interfaces for table money_history. */
    /* ====================================*/
    public void addMoneyHistory(MoneyHistory history) {
        stockMapper.addMoneyHistory(history);
    }

    public List<MoneyHistory> getMoneyHistoriesByUserId(int user_id) {
        MoneyHistory history = new MoneyHistory();
        history.setUser_id(user_id);
        return stockMapper.getMoneyHistoriesByUserId(history);
    }

    /* ====================================*/
    /* Interfaces for table stocks. */
    /* ====================================*/
    public void addOrder(Order stock) {
        stockMapper.addOrder(stock);
    }

    public void updateOrderCurentCount(Order stock) {
        stockMapper.updateOrderCurrentCount(stock);
    }

    public void updateOrderStatus(Order stock) {
        stockMapper.updateOrderStatus(stock);
    }

    public void updateOrderStopLoss(Order stock) {
        stockMapper.updateOrderStopLoss(stock);
    }

    public void updateOrderStopProfit(Order stock) {
        stockMapper.updateOrderStopProfit(stock);
    }

    public Order getOrderById(int id) {
        Order order = new Order();
        order.setId(id);
        return stockMapper.getOrderById(order);
    }

    public List<Order> getOrdersByUserId(int userId) {
        Order stock = new Order();
        stock.setUser_id(userId);
        return stockMapper.getOrdersByUserId(stock);
    }

    ;

    public List<Order> getOrdersByUserIdAndNumber(int userId, String stockNumber) {
        Order stock = new Order();
        stock.setUser_id(userId);
        stock.setStock_number(stockNumber);
        return stockMapper.getOrdersByUserIdAndNumber(stock);
    }

    public double getSumMoneyOfStock(int userId, String stockNumber) {
        List<Order> orders = null;
        try {
            orders = getOrdersByUserIdAndNumber(userId, stockNumber);
        } catch (Exception e) {
            logger.error("get stocks of specific user error, ", e);
            return -1;
        } finally {
            double sumMoney = 0;
            if (orders == null) {
                return 0;
            }
            for (Order order : orders) {
                if (order.isIs_valid()) {
                    sumMoney += order.getStock_price()*order.getCurrent_count();
                }
            }

            return sumMoney;
        }
    }

    /* ====================================*/
    /* Interfaces for table order_history. */
    /* ====================================*/
    public List<OrderHistory> getOrderHistoryByUserId(int userId) {
        OrderHistory history = new OrderHistory();
        history.setUser_id(userId);
        return stockMapper.getOrderHistorysByUserId(history);
    }

    public List<OrderHistory> getOrderHistoryByStockId(int stockId) {
        OrderHistory history = new OrderHistory();
        history.setStock_id(stockId);
        return stockMapper.getOrderHistorysByStockId(history);
    }

    public void addOrderHistory(OrderHistory history) {
        stockMapper.addOrderHistory(history);
    }

    /* ====================================*/
    /* Interfaces for table KCB_stockes. */
    /* ====================================*/
    public void addKCBStock(KCB_stock stock) {
        stockMapper.addKCBStock(stock);
    }

    public void deleteKCBStockByName(String name) {
        KCB_stock stock = new KCB_stock();
        stock.setName(name);
        stockMapper.deleteKCBStockByName(stock);
    }

    public void updateKCBStockStatus(KCB_stock stock) {
        stockMapper.updateKCBStockStatus(stock);
    }

    public List<KCB_stock> getAllKCBStocks() {
        return stockMapper.getAllKCBStocks();
    }

    public List<String> getAllKCBStockNumbers() {
        return stockMapper.getAllKCBStockNumbers();
    }

    /* ====================================*/
    /* Interfaces for table articles. */
    /* ====================================*/
    public void addArticle(Article article) {
        stockMapper.addArticle(article);
    }

    public void deleteArticleByTitle(String title) {
        Article article = new Article();
        article.setTitle(title);
        stockMapper.deleteArticleByTitle(article);
    }

    public void updateArticleBody(Article article) {
        stockMapper.updateArticleBody(article);
    }

    public List<String> selectArticleTitles() {
        return stockMapper.selectArticleTitles();
    }

    public String selectArticleBodyByTitle(String title) {
        Article article = new Article();
        article.setTitle(title);
        return stockMapper.selectArticleBodyByTitle(article);
    }

    /* ====================================*/
    /* Interfaces for table configs. */
    /* ====================================*/
    public void addConfig(Config config) {
        stockMapper.addConfig(config);
    }

    public void deleteConfigByName(String name) {
        Config config = new Config();
        config.setName(name);

        stockMapper.deleteConfigByName(config);
    }

    public void updateConfig(Config config) {
        stockMapper.updateConfig(config);
    }

    public List<Config> selectAllConfigs() {
        return stockMapper.selectAllConfigs();
    }
}
