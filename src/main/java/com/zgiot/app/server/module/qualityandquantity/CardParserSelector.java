package com.zgiot.app.server.module.qualityandquantity;

import com.zgiot.app.server.config.ApplicationContextListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CardParserSelector {

    public CardParser getParserByName(String parserName){
        Map<String, CardParser> cardParserMap = ApplicationContextListener.getApplicationContext().getBeansOfType(CardParser.class);
        for (Map.Entry<String, CardParser> entry:cardParserMap.entrySet()){
            CardParser value = entry.getValue();
            if (parserName.equals(value.getParserName())){
                return value;
            }
        }
        return null;
    }
}
