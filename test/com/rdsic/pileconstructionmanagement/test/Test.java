/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdsic.pileconstructionmanagement.test;

import com.rdsic.pcm.common.GenericHql;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 *
 * @author langl
 */
public class Test {

    public static void main(String[] args) {
        String sql = "select * from user";

        List<Map<String, Object>> list = (ArrayList) GenericHql.INSTANCE.querySQL(sql, 20);

        if (!list.isEmpty()) {
            System.out.println("cnt:" + list.size());

            JSONObject r = new JSONObject();
            r.put("items", list);

            System.out.println(r.toString());

            list.stream().map((item) -> {
                System.out.println("RECORD");
                return item;
            }).forEach((item) -> {

                for (String k : item.keySet()) {
                    JSONObject j = new JSONObject(item);
                    System.out.println(j.toString());

                    System.out.println(StringUtils.rightPad(k, 20, " ") + item.get(k).toString());
                }
            });
        }
        System.exit(0);
    }
}
