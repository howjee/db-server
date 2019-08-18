package com.rh.mapper;

import com.rh.basemapper.MyMapper;
import com.rh.model.Book;
import com.rh.model.Stock;
import com.rh.model.User;
import com.rh.model.UserAuths;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StockMapper extends MyMapper<UserAuths> {
    @Select("select * from user_auths where identity_type = #{identity_type} and identifier = #{identifier}")
    List<UserAuths> getUserId (UserAuths userAuths);

    @Select("select * from users where id = #{id}")
    List<User> getUser (User user);

    @Insert("insert into stocks(user_id, stock_number, stock_name, stock_price, strategy_mode, strategy_rate, margin, stop_loss_rate, stop_profit_rate, open_fee,\n" +
            "     delay_fee, auto_delay, current_count) values(#{user_id}, #{stock_number}, #{stock_name}, #{stock_price}, #{strategy_mode}, #{strategy_rate}, #{margin}, #{stop_loss_rate}, #{stop_profit_rate}, #{open_fee}, #{delay_fee}, #{auto_delay}, #{current_count})")
    void addStocks (Stock stock);

    @Update("update stocks set current_count = #{current_count} where id = #{id}")
    void updateStockCurrentCount(Stock stock);
}
