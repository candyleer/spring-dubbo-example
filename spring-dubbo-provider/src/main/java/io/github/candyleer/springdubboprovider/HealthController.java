package io.github.candyleer.springdubboprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping("/health")
    public String health() {
        return "200";
    }

    @RequestMapping("/ready")
    public String ready() {
        return "200";
    }

    @RequestMapping("/jdbc")
    public Object jdbc() {
        String replaceSql = "REPLACE INTO test (name,age) values (?,?);";

        Object[] objects1 = {"liming", 123};
        Object[] objects2 = {"lican", 13};
        Object[] objects3 = {"djj", 133};
        Object[] objects4 = {"cjj", 143};
        List<Object[]> objects = new ArrayList<>();
        objects.add(objects1);
        objects.add(objects2);
        objects.add(objects3);
        objects.add(objects4);
        int[] ints = jdbcTemplate.batchUpdate(replaceSql, objects);
        return ints;
    }
}
