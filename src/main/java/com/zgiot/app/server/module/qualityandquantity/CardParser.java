package com.zgiot.app.server.module.qualityandquantity;


public interface CardParser {
    Object parse(String paramValueJson);
    String getParserName();
    Object parseTest(String paramValueJson);
}
