package com.zgiot.app.server.module.sfmedium.entity.vo;

import java.util.List;

/**
 * 快捷加介系统选择
 */
public class MediumDosingSystemGroupVO {

    private String mediumDosingSystemName;

    private List<SeparatingSystem> separatingSystems;


    public String getMediumDosingSystemName() {
        return mediumDosingSystemName;
    }

    public void setMediumDosingSystemName(String mediumDosingSystemName) {
        this.mediumDosingSystemName = mediumDosingSystemName;
    }

    public List<SeparatingSystem> getSeparatingSystems() {
        return separatingSystems;
    }

    public void setSeparatingSystems(List<SeparatingSystem> separatingSystems) {
        this.separatingSystems = separatingSystems;
    }

    public class SeparatingSystem {

        private String separatingSystemName;

        private List<String> mediumDosingSystem;

        public String getSeparatingSystemName() {
            return separatingSystemName;
        }

        public void setSeparatingSystemName(String separatingSystemName) {
            this.separatingSystemName = separatingSystemName;
        }

        public List<String> getMediumDosingSystem() {
            return mediumDosingSystem;
        }

        public void setMediumDosingSystem(List<String> mediumDosingSystem) {
            this.mediumDosingSystem = mediumDosingSystem;
        }
    }


}
