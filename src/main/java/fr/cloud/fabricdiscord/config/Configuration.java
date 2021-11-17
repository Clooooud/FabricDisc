package fr.cloud.fabricdiscord.config;

import lombok.Getter;

public class Configuration extends BaseConfiguration {

    @ConfigKey @Getter private String token = "";
    @ConfigKey @Getter private String serverId = "";
    @ConfigKey @Getter private String channelId = "";

    public Configuration() {
        super("fabricdiscord");
    }
}
