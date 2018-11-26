package com.mariobgr.falcon.dao;

import com.mariobgr.falcon.models.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
public class MessageDao extends JdbcDaoSupport implements GenericDao {

    @Autowired
    private MessageSourceAccessor msa;

    @Autowired
    public MessageDao(@Qualifier("dataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Map get(int id) {

        String sql = msa.getMessage("sql.select.message");
        Object[] args = new Object[] { id };
        return getJdbcTemplate().queryForMap(sql, args);

    }

    @Override
    public List<Map<String, Object>> getAll(int page) {

        String sql = msa.getMessage("sql.select_all.message");
        sql = sql + " limit " + page * 20 + ", 20 ";

        return getJdbcTemplate().queryForList(sql);

    }

    @Override
    public int save(Object o) {

        MessageModel message = MessageModel.class.cast(o);

        Timestamp messageTime = new Timestamp(Integer.valueOf(message.getTimestamp()) * 1000L);

        String sql = msa.getMessage("sql.insert.message");
        Object[] args = new Object[] { message.getMessage(), messageTime, message.getRandom() };
        return getJdbcTemplate().update(sql, args);

    }

    @Override
    public void update(Object o) {

        MessageModel message = MessageModel.class.cast(o);

        Timestamp messageTime = new Timestamp(Integer.valueOf(message.getTimestamp()) * 1000L);

        String sql = msa.getMessage("sql.update.message");
        Object[] args = new Object[] { message.getMessage(), messageTime, message.getRandom(), message.getId() };
        getJdbcTemplate().update(sql, args);

    }

    @Override
    public void delete(Object o) {

        MessageModel message = MessageModel.class.cast(o);

        String sql = msa.getMessage("sql.delete.message");
        Object[] args = new Object[] { message.getId() };
        getJdbcTemplate().update(sql, args);

    }
}
