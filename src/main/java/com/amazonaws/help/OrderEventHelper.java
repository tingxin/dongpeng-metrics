package com.amazonaws.help;

import com.amazonaws.bean.OrderEvent;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OrderEventHelper {
    public static OrderEvent createBy(JsonNode jsonNode, SimpleDateFormat formatter) throws ParseException {
        OrderEvent item = new OrderEvent();
        item.setUserMail(jsonNode.get("user_mail").asText());
        item.setStatus(jsonNode.get("status").asText());
        item.setGoodCount(jsonNode.get("good_count").asInt());
        item.setCity(jsonNode.get("city").asText());
        item.setAmount(jsonNode.get("amount").asDouble());

        String strCreateTime = jsonNode.get("create_time").asText();
        String strUpdateTime = jsonNode.get("update_time").asText();
        item.setCreateTime(formatter.parse(strCreateTime));
        item.setUpdateTime(formatter.parse(strUpdateTime));
        return item;
    }
}
