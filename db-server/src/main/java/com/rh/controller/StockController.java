package com.rh.controller;

import com.rh.model.Stock;
import com.rh.model.StockHistory;
import com.rh.model.User;
import com.rh.model.UserAuths;
import com.rh.service.BookService;
import com.rh.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@RestController
public class StockController {

    private final static Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/user/{type}/{identifier}", method = RequestMethod.GET)
    public User userinfo(@PathVariable String type, @PathVariable String identifier) {
        User user = null;
        try {
            UserAuths userAuths = stockService.getUserAuths(type, identifier);
            user = stockService.getUser(userAuths.getUser_id());
        } catch (Exception e) {
            logger.error("get useinfo error , ", e);
        } finally {
            return user;
        }
    }

    @RequestMapping(value = "/user/update_money", method = RequestMethod.POST)
    public String updateUserMoney(@RequestBody User user) {
        String res = "success";
        try {
            stockService.updateUserMoney(user);
        } catch (Exception e) {
            res = "failure";
            logger.error("updateUserMoney error , ", e);
        } finally {
            return res;
        }
    }

    @RequestMapping(value = "/stock/user_{userId}", method = RequestMethod.GET)
    public List<Stock> getStocksByUserId(@PathVariable String userId) {
        List<Stock> stocks = null;
        try {
            stocks = stockService.getStocksByUserId(Integer.parseInt(userId));
        } catch (Exception e) {
            logger.error("get stocks of specific user error, ", e);
        } finally {
            return stocks;
        }
    }

    @RequestMapping(value = "/stock/new", method = RequestMethod.POST)
    public String insetStock(@RequestBody Stock stock) {
        String res = "success";
        try {
            stockService.insetStock(stock);
        } catch (Exception e) {
            res = "failure";
            logger.error("insetStock error , ", e);
        } finally {
            return res;
        }
    }

    @RequestMapping(value = "/stock/update_current_count", method = RequestMethod.POST)
    public String updateStockCurrentCount(@RequestBody Stock stock) {
        String res = "success";
        try {
            stockService.updateStockCurentCount(stock);
        } catch (Exception e) {
            res = "failure";
            logger.error("updateStockCurentCount error , ", e);
        } finally {
            return res;
        }
    }

    @RequestMapping(value = "/stockhistory/user_{userId}", method = RequestMethod.GET)
    public List<StockHistory> getStockHistoryByUserId(@PathVariable String userId) {
        List<StockHistory> historys = null;
        try {
            historys = stockService.getStockHistoryByUserId(Integer.parseInt(userId));
        } catch (Exception e) {
            logger.error("get stock histories of specific user error, ", e);
        } finally {
            return historys;
        }
    }

    @RequestMapping(value = "/stockhistory/stock_{stockId}", method = RequestMethod.GET)
    public List<StockHistory> getStockHistoryByStockId(@PathVariable String stockId) {
        List<StockHistory> historys = null;
        try {
            historys = stockService.getStockHistoryByStockId(Integer.parseInt(stockId));
        } catch (Exception e) {
            logger.error("get stock histories of specific stock error, ", e);
        } finally {
            return historys;
        }
    }

    @RequestMapping(value = "/stockhistory", method = RequestMethod.POST)
    public String addStockHistoy(@RequestBody StockHistory history) {
        String res = "success";
        try {
            stockService.addStockHistory(history);
        } catch (Exception e) {
            res = "failure";
            logger.error("add stock history error , ", e);
        } finally {
            return res;
        }
    }

    @RequestMapping(value = "/hq/sinajs/cn/{list}", method = RequestMethod.GET)
    public String getStockInfo(@PathVariable  String list) {

        String url = "http://hq.sinajs.cn/list="+ list;
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
                    result = bo.toString();
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
