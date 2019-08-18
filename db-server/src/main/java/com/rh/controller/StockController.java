package com.rh.controller;

import com.rh.model.Stock;
import com.rh.model.User;
import com.rh.model.UserAuths;
import com.rh.service.BookService;
import com.rh.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StockController {

    private final static Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/user/info/{type}/{identifier}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/stock", method = RequestMethod.POST)
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
}
