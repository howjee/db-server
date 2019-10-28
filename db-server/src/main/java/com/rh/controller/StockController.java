package com.rh.controller;

import com.rh.controller.util.StringUtils;
import com.rh.model.*;
import com.rh.service.BookService;
import com.rh.service.StockService;
import com.rh.service.token.CurrentUser;
import com.rh.service.token.LoginRequired;
import com.rh.service.token.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5556")
//@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class StockController {

    private final static Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private StockService stockService;

    /* Login and register support. */
    /* ====================================*/
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register_user(@RequestBody UserAuths userAuth) {
        return stockService.register(userAuth, User.Permission.USER.getValue());
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(@RequestBody UserAuths userAuth) {
        if (StringUtils.isEmpty(userAuth.getIdentity_type()) || !userAuth.getIdentity_type().equals("phone"))
            return "invalid identity type";

        if (StringUtils.isEmpty(userAuth.getIdentifier()) || !StringUtils.isMobile(userAuth.getIdentifier())) {
            return "invalid phone number";
        }

        if (StringUtils.isEmpty(userAuth.getCredential())) {
            return "invalid password";
        }

        String response = null;
        try {
            int userId = stockService.getUserIdWithCredential(userAuth);
            if (userId < 0) {
                response = "user identity or password error";
            } else {
                User user = stockService.getUser(userId);
                response = TokenUtils.createJwtToken(user);
            }
        } catch (Exception e) {
            response = "system error";
            logger.error("authentication failed, ", e);
        } finally {
            return response;
        }
    }

    /* APIs for user information. */
    /* ====================================*/
    @LoginRequired
    @RequestMapping(value = "/user/modify_nickname", method = RequestMethod.POST)
    public String modifyNickname(@CurrentUser User user, @RequestBody String nickname) {
        if (StringUtils.isEmpty(nickname)) {
            return "invalid nick name";
        }

        String response = "failure";
        try {
            int user_id = stockService.getUserIdByNickName(nickname);
            if (user_id > 0 && user_id != user.getId()) {
                response = "duplicate nickname";
            } else {
                user.setNickname(nickname);
                stockService.modifyNickname(user);
                response = "success";
            }
        } catch (Exception e) {
            logger.error("modify nickname error, ", e);
        } finally {
            return response;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/user/modify_password", method = RequestMethod.POST)
    public String modifyPassword(@CurrentUser User user, @RequestBody UserAuths auth) {
        if (StringUtils.isEmpty(auth.getIdentity_type()) || !auth.getIdentity_type().equals("phone"))
            return "invalid identity type";

        if (StringUtils.isEmpty(auth.getIdentifier()) || !StringUtils.isMobile(auth.getIdentifier())) {
            return "invalid phone number";
        }

        if (StringUtils.isEmpty(auth.getCredential())) {
            return "invalid password";
        }


        auth.setUser_id(user.getId());
        String response = "failure";
        try {
            if (stockService.modifyPassword(auth) > 0) {
                response = "success";
            }
        } catch (Exception e) {
            logger.error("modify password error, ", e);
        } finally {
            return response;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/user/modify_permission", method = RequestMethod.POST)
    public String modifyPermission(@CurrentUser User current_user, @RequestBody User user) {
        if (current_user.getPermission() != User.Permission.ROOT.getValue()) {
            return "permission denied";
        }

        if (StringUtils.isEmpty(user.getNickname()) || user.getPermission() < User.Permission.USER.getValue() || user.getPermission() > User.Permission.ROOT.getValue()) {
            return "parameter error";
        }

        System.out.println(user.getNickname() + "," + user.getPermission());
        String response = "success";
        try {
            stockService.updateUserPermission(user);
        } catch (Exception e) {
            response = "failure";
            logger.error("modify password error, ", e);
        } finally {
            return response;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public User getUserInfoFromToken(@CurrentUser User user) {
        return user;
    }

    @LoginRequired
    @RequestMapping(value = "/user/recharge", method = RequestMethod.POST)
    public String recharge(@CurrentUser User user, @RequestBody double money) {
        if (money < 0) {
            return "parameter error";
        }
        return stockService.addToUserMoney(user, money, MoneyHistory.Type.RECHARGE);
    }

    @LoginRequired
    @RequestMapping(value = "/user/withdraw", method = RequestMethod.POST)
    public String withdraw(@CurrentUser User user, @RequestBody double money) {
        if (money < 0) {
            return "parameter error";
        }

        String response = "failure";
        try {
            double user_money = stockService.getUserMoney(user.getId());
            if (money > user_money) {
                response = "money is not enough";
            } else {
                response = stockService.addToUserMoney(user, 0 - money, MoneyHistory.Type.WITHDRAW);
            }
        } catch (Exception e) {
            logger.error("withdraw error, ", e);
        } finally {
            return response;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/user/present", method = RequestMethod.POST)
    public String present(@CurrentUser User user, @RequestBody double money) {
        if (money < 0) {
            return "parameter error";
        }
        return stockService.addToUserMoney(user, money, MoneyHistory.Type.PRESENT);
    }

    /* APIs for stock information. */
    /* ====================================*/
    @LoginRequired
    @RequestMapping(value = "/order/all", method = RequestMethod.GET)
    public List<Order> getOrdersByUserId(@CurrentUser User user) {
        List<Order> stocks = null;
        try {
            stocks = stockService.getOrdersByUserId(user.getId());
        } catch (Exception e) {
            logger.error("get stocks of specific user error, ", e);
        } finally {
            return stocks;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/order/sum_money", method = RequestMethod.POST)
    public double getSumMoneyOfOrder(@CurrentUser User user, @RequestBody String stockNumber) {
        return stockService.getSumMoneyOfStock(user.getId(), stockNumber);
    }

    @LoginRequired
    @RequestMapping(value = "/order/new", method = RequestMethod.POST)
    public String insetOrder(@CurrentUser User user, @RequestBody Order stock) {
        String res = "success";
        try {
            stock.setUser_id(user.getId());
            stockService.addOrder(stock);
        } catch (Exception e) {
            res = "failure";
            logger.error("insetStock error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/order/update_current_count", method = RequestMethod.POST)
    public String updateOrderCurrentCount(@CurrentUser User user, @RequestBody Order order) {
        String res = "success";
        try {
            Order order_in_db = stockService.getOrderById(order.getId());
            if (order_in_db == null || order_in_db.getStatus() == Order.Status.DONE.getValue() || order_in_db.getUser_id() != user.getId()) {
                res = "permission denied";
            } else {
                if (order_in_db.getCurrent_count() == order.getCurrent_count()) {
                    res = "success";
                } else if (order_in_db.getCurrent_count() < order.getCurrent_count()) {
                    double delta_money = (order.getCurrent_count() - order_in_db.getCurrent_count()) * order_in_db.getStock_price();
                    double sum_money = stockService.getSumMoneyOfStock(user.getId(), order_in_db.getStock_number());
                    if (order.getCurrent_count() * order_in_db.getStock_price() > order_in_db.getMargin()) {
                        res = "margin is not enough";
                    } else {
                        if (delta_money + sum_money > 50000) {
                            res = "can't buy this stock any more";
                        } else {
                            stockService.updateOrderCurentCount(order);
                        }
                    }
                } else {
                    stockService.updateOrderCurentCount(order);
                }
            }
        } catch (Exception e) {
            res = "failure";
            logger.error("updateStockCurentCount error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/order/set_done", method = RequestMethod.POST)
    public String setOrderDone(@CurrentUser User user, @RequestBody int id) {
        String res = "success";
        try {
            Order order = stockService.getOrderById(id);
            if (order == null || order.getStatus() == Order.Status.DONE.getValue()) {
                res = "failure";
                return res;
            }
            float open_fee = order.getOpen_fee();
            float margin = order.getMargin();

            MoneyHistory moneyHist = new MoneyHistory();
            moneyHist.setUser_id(user.getId());
            moneyHist.setRemain(user.getMoney());
            moneyHist.setValue(margin);
            moneyHist.setType(MoneyHistory.Type.MARGIN.getValue());
            moneyHist.setDetail(Integer.toString(order.getId()));
            stockService.addMoneyHistory(moneyHist);

            MoneyHistory moneyHist2 = new MoneyHistory();
            moneyHist2.setUser_id(user.getId());
            moneyHist2.setRemain(user.getMoney());
            moneyHist2.setValue(open_fee);
            moneyHist2.setType(MoneyHistory.Type.OPEN_FEE.getValue());
            moneyHist2.setDetail(Integer.toString(order.getId()));
            stockService.addMoneyHistory(moneyHist2);

            // deduct user money.
            stockService.addToUserMoney(user, 0 - margin - open_fee);

            // update order status
            order.setStatus(Order.Status.DONE.getValue());
            stockService.updateOrderStatus(order);
        } catch (Exception e) {
            res = "failure";
            logger.error("updateStockStatus error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/order/update_stop_loss", method = RequestMethod.POST)
    public String updateOrderStopLoss(@CurrentUser User user, @RequestBody Order order) {
        String res = "success";
        try {
            Order order_in_db = stockService.getOrderById(order.getId());
            if (order_in_db.getStatus() == Order.Status.DONE.getValue()) {
                res = "order is done";
            } else {
                if (order_in_db.getStop_loss_rate() != order.getStop_loss_rate()) {
                    stockService.updateOrderStopLoss(order);
                }
            }
        } catch (Exception e) {
            res = "failure";
            logger.error("updateStockStopLoss error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/order/update_stop_profit", method = RequestMethod.POST)
    public String updateStockStopProfit(@CurrentUser User user, @RequestBody Order order) {
        String res = "success";
        try {
            Order order_in_db = stockService.getOrderById(order.getId());
            if (order_in_db.getStatus() == Order.Status.DONE.getValue()) {
                res = "order is done";
            } else {

                if (order_in_db.getStop_profit_rate() != order.getStop_profit_rate()) {
                    stockService.updateOrderStopProfit(order);
                }
            }
        } catch (Exception e) {
            res = "failure";
            logger.error("updateStockStopProfit error , ", e);
        } finally {
            return res;
        }
    }

    /* APIs for stock history information. */
    /* ====================================*/
    @LoginRequired
    @RequestMapping(value = "/stockhistory/user", method = RequestMethod.GET)
    public List<OrderHistory> getStockHistoryByUserId(@CurrentUser User user) {
        List<OrderHistory> historys = null;
        try {
            historys = stockService.getOrderHistoryByUserId(user.getId());
        } catch (Exception e) {
            logger.error("get stock histories of specific user error, ", e);
        } finally {
            return historys;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/stockhistory/stock", method = RequestMethod.POST)
    public List<OrderHistory> getStockHistoryByStockId(@CurrentUser User user, @RequestBody int id) {
        List<OrderHistory> historys = null;
        try {
            historys = stockService.getOrderHistoryByStockId(id);
        } catch (Exception e) {
            logger.error("get stock histories of specific stock error, ", e);
        } finally {
            return historys;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/stockhistory/new", method = RequestMethod.POST)
    public String addStockHistoy(@RequestBody OrderHistory history) {
        String res = "success";
        try {
            stockService.addOrderHistory(history);
        } catch (Exception e) {
            res = "failure";
            logger.error("add stock history error , ", e);
        } finally {
            return res;
        }
    }

    /* APIs for table KCB_stocks. */
    /* ====================================*/
    @LoginRequired
    @RequestMapping(value = "/KCBStock/new", method = RequestMethod.POST)
    public String addKCBStock(@CurrentUser User user, @RequestBody KCB_stock stock) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return "permission denied";
        }

        String res = "success";
        try {
            stockService.addKCBStock(stock);
        } catch (Exception e) {
            res = "failure";
            logger.error("insetKCBStock error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/KCBStock/delete", method = RequestMethod.POST)
    public String deleteKCBStock(@CurrentUser User user, @RequestBody String name) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return "permission denied";
        }

        String res = "success";
        try {
            stockService.deleteKCBStockByName(name);
        } catch (Exception e) {
            res = "failure";
            logger.error("deleteKCBStock error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/KCBStock/update_inuse", method = RequestMethod.POST)
    public String updateKCBStockStatus(@CurrentUser User user, @RequestBody KCB_stock stock) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return "permission denied";
        }

        String res = "success";
        try {
            stockService.updateKCBStockStatus(stock);
        } catch (Exception e) {
            res = "failure";
            logger.error("updateKCBStockStatus error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/KCBStock/all", method = RequestMethod.GET)
    public List<KCB_stock> getAllKCBStocks(@CurrentUser User user) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        List<KCB_stock> stocks = null;
        try {
            stocks = stockService.getAllKCBStocks();
        } catch (Exception e) {
            logger.error("get all KCB stocks error, ", e);
        } finally {
            return stocks;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/KCBStock/numbers", method = RequestMethod.GET)
    public String getAllKCBStocksNumber(@CurrentUser User user) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        List<String> stocks = null;
        try {
            stocks = stockService.getAllKCBStockNumbers();
        } catch (Exception e) {
            logger.error("get all KCB stocks error, ", e);
        } finally {
            if (stocks == null) {
                return "error";
            }
            return String.join(" ", stocks);
        }
    }

    /* APIs for table articles. */
    /* ====================================*/
    @LoginRequired
    @RequestMapping(value = "/article/new", method = RequestMethod.POST)
    public String addArticle(@CurrentUser User user, @RequestBody Article article) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.addArticle(article);
        } catch (Exception e) {
            res = "failure";
            logger.error("add article error, ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/article/delete", method = RequestMethod.POST)
    public String deleteArticle(@CurrentUser User user, @RequestBody String title) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.deleteArticleByTitle(title);
        } catch (Exception e) {
            res = "failure";
            logger.error("delete article error , ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/article/update_body", method = RequestMethod.POST)
    public String updateArticleBody(@CurrentUser User user, @RequestBody Article article) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.updateArticleBody(article);
        } catch (Exception e) {
            res = "failure";
            logger.error("update article body error, ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/article/all_titles", method = RequestMethod.GET)
    public List<String> getAllArticleTitles(@CurrentUser User user) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        List<String> titles = null;
        try {
            titles = stockService.selectArticleTitles();
        } catch (Exception e) {
            logger.error("get all article titles error, ", e);
        } finally {
            return titles;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public List<Article> getAllArticles(@CurrentUser User user) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        List<Article> articles = null;
        try {
            articles = stockService.selectArticles();
        } catch (Exception e) {
            logger.error("get all article titles error, ", e);
        } finally {
            return articles;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/article/body", method = RequestMethod.POST)
    public String getBodyOfTitle(@CurrentUser User user, @RequestBody String title) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String body = null;
        try {
            body = stockService.selectArticleBodyByTitle(title);
        } catch (Exception e) {
            logger.error("get article body of title error, ", e);
        } finally {
            return body;
        }
    }

    /* APIs for table configs */
    /*===========================================*/
    @LoginRequired
    @RequestMapping(value = "/config/all", method = RequestMethod.GET)
    public List<Config> getAllConfigs(@CurrentUser User user) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        List<Config> configs = null;
        try {
            configs = stockService.selectAllConfigs();
        } catch (Exception e) {
            logger.error("get all KCB stocks error, ", e);
        } finally {
            return configs;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/config/new", method = RequestMethod.POST)
    public String addConfig(@CurrentUser User user, @RequestBody Config config) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.addConfig(config);
        } catch (Exception e) {
            res = "failure";
            logger.error("add config error, ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/config/delete", method = RequestMethod.POST)
    public String deleteConfigById(@CurrentUser User user, @RequestBody String name) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.deleteConfigByName(name);
        } catch (Exception e) {
            res = "failure";
            logger.error("delete config error, ", e);
        } finally {
            return res;
        }
    }

    @LoginRequired
    @RequestMapping(value = "/config/update", method = RequestMethod.POST)
    public String updateConfig(@CurrentUser User user, @RequestBody Config config) {
        if (user.getPermission() < User.Permission.MANAGER.getValue()) {
            return null;
        }

        String res = "success";
        try {
            stockService.updateConfig(config);
        } catch (Exception e) {
            res = "failure";
            logger.error("update config error, ", e);
        } finally {
            return res;
        }
    }

    /*===========================================*/
    /* Other APIs */
    /*==========================================*/
    @RequestMapping(value = "/suggest3/sinajs/cn/{list}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public String getStockDetail(@PathVariable String list) {

        String url = "http://suggest3.sinajs.cn/suggest/" + list;
        logger.info(url);
        String result;
        try {
            URL u = new URL(url);
            byte[] b = new byte[256];
            InputStream in = null;
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            {
                try {
                    in = u.openStream();
                    int i;
                    while ((i = in.read(b)) != -1) {
                        bo.write(b, 0, i);
                    }
                    result = bo.toString("GBK");
                    //byte[] lens = bo.toByteArray();
                    //result = new String(lens);
                    bo.reset();
                    //String[] stocks = result.split(";");
                    //for (String stock : stocks) {
                    //    String[] datas = stock.split(",");
                    //System.out.println(datas);
                    // }

                } catch (Exception e) {
                    result = "get stock real-time infomation error, " + e.getMessage();
                    logger.error(result);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (Exception e) {
            result = "get stock real-time infomation error, " + e.getMessage();
            logger.error(result);
        }

        return result;
    }

    @RequestMapping(value = "/hq/sinajs/cn/{list}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    //@RequestMapping(value = "/hq/sinajs/cn/{list}", method = RequestMethod.GET)
    public String getStockInfo(@PathVariable String list) {

        String url = "http://hq.sinajs.cn/list=" + list;
        String result;
        try {
            URL u = new URL(url);
            byte[] b = new byte[256];
            InputStream in = null;
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            {
                try {
                    in = u.openStream();
                    int i;
                    while ((i = in.read(b)) != -1) {
                        bo.write(b, 0, i);
                    }
                    result = bo.toString("GBK");
                    //byte[] lens = bo.toByteArray();
                    //result = new String(lens);
                    bo.reset();
                    //String[] stocks = result.split(";");
                    //for (String stock : stocks) {
                    //    String[] datas = stock.split(",");
                    //System.out.println(datas);
                    // }

                } catch (Exception e) {
                    result = "get stock real-time infomation error, " + e.getMessage();
                    logger.error(result);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (Exception e) {
            result = "get stock real-time infomation error, " + e.getMessage();
            logger.error(result);
        }

        return result;
    }
}
