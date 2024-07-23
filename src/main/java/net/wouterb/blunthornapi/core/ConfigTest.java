package net.wouterb.blunthornapi.core;

import net.wouterb.blunthornapi.api.config.BlankLine;
import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.Comment;
import net.wouterb.blunthornapi.api.config.StoreInConfig;

public class ConfigTest extends BlunthornConfig {
    @StoreInConfig
    private String testing = "TEST";
    @StoreInConfig
    @BlankLine
    @Comment("comment test")
    private boolean booltest = true;

    public String getTesting() {
        return testing;
    }

    public boolean getBooltest() {
        return booltest;
    }

    public ConfigTest() {
        this.filePath = "config_test.properties";
        this.modId = "ConfigTest";
        init();
    }
}
