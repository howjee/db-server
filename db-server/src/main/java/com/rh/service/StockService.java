package com.rh.service;

import com.rh.mapper.StockMapper;
import com.rh.model.Stock;
import com.rh.model.User;
import com.rh.model.UserAuths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    @Autowired
    private StockMapper stockMapper;

    public UserAuths getUserAuths(String type, String identifier) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentity_type(type);
        userAuths.setIdentifier(identifier);
        return stockMapper.getUserId(userAuths).get(0);
    }

    public User getUser(int id) {
        User user = new User();
        user.setId(id);
        return stockMapper.getUser(user).get(0);
    }

    public void updateUserMoney(User user) { stockMapper.updateUserMoney(user); }

    public void insetStock(Stock stock) {
        stockMapper.addStocks(stock);
    }

    public void updateStockCurentCount(Stock stock) {
        stockMapper.updateStockCurrentCount(stock);
    }
}
