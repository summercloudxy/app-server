package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车方案选择
 */
public class StopChoiceVO {


    private String choiceTitle;

    private List<ChiceVO> choiceSets;

    private String isSingleChoice;


    public class ChiceVO {

        private String id;

        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getChoiceTitle() {
        return choiceTitle;
    }

    public void setChoiceTitle(String choiceTitle) {
        this.choiceTitle = choiceTitle;
    }

    public List<ChiceVO> getChoiceSets() {
        return choiceSets;
    }

    public void setChoiceSets(List<ChiceVO> choiceSets) {
        this.choiceSets = choiceSets;
    }

    public String getIsSingleChoice() {
        return isSingleChoice;
    }

    public void setIsSingleChoice(String isSingleChoice) {
        this.isSingleChoice = isSingleChoice;
    }
}
