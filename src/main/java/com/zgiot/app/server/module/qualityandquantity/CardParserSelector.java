package com.zgiot.app.server.module.qualityandquantity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CardParserSelector {
    @Autowired
    private ApplicationContext applicationContext;

    public CardParser getParserByName(String parserName){
        Map<String, CardParser> cardParserMap = applicationContext.getBeansOfType(CardParser.class);
        for (Map.Entry<String, CardParser> entry:cardParserMap.entrySet()){
            CardParser value = entry.getValue();
            if (parserName.equals(value.getParserName())){
                return value;
            }
        }
        return null;
    }
}
