package com.rh.mapper;

import com.rh.basemapper.MyMapper;
import com.rh.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BookMapper extends MyMapper<Book> {

    //在使用通用Mapper的基础上 下面的都是mybatis注解的方法
    @Select("select * from book")
    List<Book> getAll();

    @Select("select\n" +
            "        name as name,\n" +
            "        author as author\n" +
            "        from\n" +
            "        book\n" +
            "        where name = #{name}")
    List<Book> getBookByName(Book book);
}
