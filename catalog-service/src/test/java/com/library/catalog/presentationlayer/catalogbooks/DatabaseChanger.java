package com.library.catalog.presentationlayer.catalogbooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseChanger {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void changeSynopsisSize() {
        String sql = "ALTER TABLE books ALTER COLUMN synopsis VARCHAR(550)";
        jdbcTemplate.execute(sql);
    }
}
