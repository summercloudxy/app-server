package com.zgiot.app.server.module.auth.controller.AuthorityGroup;

import com.zgiot.app.server.module.auth.pojo.Module;
import com.zgiot.app.server.module.auth.pojo.PlatformClient;

public class PlatformClientAndModule {
    private PlatformClient platformClient;
    private Module module;

    public PlatformClient getPlatformClient() {
        return platformClient;
    }

    public Module getModule() {
        return module;
    }

    public void setPlatformClient(PlatformClient platformClient) {
        this.platformClient = platformClient;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
