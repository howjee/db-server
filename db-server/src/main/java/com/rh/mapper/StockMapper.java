package com.rh.mapper;

import com.rh.basemapper.MyMapper;
import com.rh.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StockMapper extends MyMapper<UserAuths> {
    /*======================================*/
    /* SQLs for table user and user_auths. */
    /* ====================================*/
    @Insert("insert into users(nickname, permission) values (#{nickname}, #{permission})")
    void addUser(User user);

    @Update("update users set permission=#{permission} where nickname=#{nickname}")
    void updateUserPermission(User user);

    @Insert("insert into user_auths(user_id, identity_type, identifier, credential) values " +
            "(#{user_id}, #{identity_type}, #{identifier}, #{credential})")
    void addUserAuth(UserAuths auth);

    @Update("update user_auths set credential = #{credential} where user_id = #{user_id} and " +
            "identity_type = #{identity_type} and identifier = #{identifier}")
    void modifyPassword(UserAuths auth);

    @Update("update users set nickname = #{nickname} where id=#{id}")
    void modifyNickName(User user);

    @Select("select id from users where nickname=#{nickname}")
    List<Integer> getUserIdByNickName(User user);

    @Select("select user_id from user_auths where identity_type = #{identity_type} and identifier = #{identifier}" +
            "and credential = #{credential}")
    List<Integer> getUserIdWithCredential (UserAuths userAuths);

    @Select("select user_id from user_auths where identifier = #{identifier}")
    List<Integer> getUserIdWithIdentifier(UserAuths userAuths);

    @Select("select * from users where id = #{id}")
    List<User> getUser (User user);

    @Select("select * from users")
    List<User> getAllUsers ();

    @Update("update users set money = money + #{money} where id = #{id}")
    void addToUserMoney (User user);

    @Select("select money from users where id = #{id}")
    double getUserMoney(User user);

    /*======================================*/
    /* SQLs for table money_history. */
    /* ====================================*/
    @Insert("insert into money_history(user_id,type,value,remain) values (#{user_id}, #{type}," +
            "#{value}, #{remain})")
    void addMoneyHistory(MoneyHistory history);

    @Select("select * from money_history where user_id=#{user_id}")
    List<MoneyHistory> getMoneyHistoriesByUserId(MoneyHistory history);

    /*======================================*/
    /* SQLs for table stocks. */
    /* ====================================*/
    @Insert("insert into orders(user_id, stock_number, stock_name, stock_price, strategy_mode, strategy_rate, margin, stop_loss_rate, stop_profit_rate, open_fee,\n" +
            "     delay_fee, auto_delay, current_count) values(#{user_id}, #{stock_number}, #{stock_name}, #{stock_price}, #{strategy_mode}, #{strategy_rate}, #{margin}, #{stop_loss_rate}, #{stop_profit_rate}, #{open_fee}, #{delay_fee}, #{auto_delay}, #{current_count})")
    void addOrder (Order order);

    @Update("update orders set current_count = #{current_count} where id = #{id}")
    void updateOrderCurrentCount(Order order);

    @Update("update orders set status = #{status} where id = #{id}")
    void updateOrderStatus(Order order);

    @Update("update orders set stop_loss_rate = #{stop_loss_rate} where id = #{id}")
    void updateOrderStopLoss(Order order);

    @Update("update orders set stop_profit_rate = #{stop_profit_rate} where id = #{id}")
    void updateOrderStopProfit(Order order);

    @Select("select * from orders where id = #{id}")
    Order getOrderById(Order order);

    @Select("select * from orders where user_id = #{user_id}")
    List<Order> getOrdersByUserId(Order order);

    @Select("select * from orders where user_id = #{user_id} and stock_number = #{stock_number}")
    List<Order> getOrdersByUserIdAndNumber(Order order);

    /*======================================*/
    /* SQLs for table stock_history. */
    /* ====================================*/
    @Select("select * from order_history where stock_id = #{stock_id}")
    List<OrderHistory> getOrderHistorysByStockId(OrderHistory history);

    @Select("select * from order_history where user_id = #{user_id}")
    List<OrderHistory> getOrderHistorysByUserId(OrderHistory history);

    @Insert("insert into order_history(user_id, stock_id, operate_type, buy_price, " +
            "sell_price, sell_count, remain_count, sell_mode, profit_rate, profit, " +
            "buy_time, sell_time) values (#{user_id}, #{stock_id}, #{operate_type}, " +
            "#{buy_price}, #{sell_price}, #{sell_count}, #{remain_count}, #{sell_mode}, " +
            "#{profit_rate}, #{profit}, #{buy_time}, #{sell_time})")
    void addOrderHistory(OrderHistory history);

    /*======================================*/
    /* SQLs for table KCB_stocks. */
    /* ====================================*/
    @Insert("insert into KCB_stocks(name, number, inuse) values (#{name}, #{number}, #{inuse})")
    void addKCBStock(KCB_stock stock);

    @Delete("delete from KCB_stocks where name = #{name}")
    void deleteKCBStockByName(KCB_stock stock);

    @Update("update KCB_stocks set inuse = #{inuse} where name = #{name}")
    void updateKCBStockStatus(KCB_stock stock);

    @Select("select * from KCB_stocks")
    List<KCB_stock> getAllKCBStocks();

    @Select("select number from KCB_stocks")
    List<String> getAllKCBStockNumbers();

    /*======================================*/
    /* SQLs for table articles. */
    /* ====================================*/
    @Insert("insert into articles(title, body) values(#{title}, #{body})")
    void addArticle(Article article);

    @Delete("delete from articles where title=#{title}")
    void deleteArticleByTitle(Article article);

    @Update("update articles set body = #{body} where title = #{title}")
    void updateArticleBody(Article article);

    @Select("select title from articles")
    List<String> selectArticleTitles();

    @Select("select title,left(body,50) as body from articles")
    List<Article> selectArticles();

    @Select("select body from articles where title = #{title}")
    String selectArticleBodyByTitle(Article article);

    /*======================================*/
    /* SQLs for table configs. */
    /*======================================*/
    @Insert("insert into configs(name, body) values(#{name}, #{body})")
    void addConfig(Config config);

    @Delete("delete from configs where name=#{name}")
    void deleteConfigByName(Config config);

    @Update("update configs set body=#{body} where name=#{name}")
    void updateConfig(Config config);

    @Select("select * from configs")
    List<Config> selectAllConfigs();
}
