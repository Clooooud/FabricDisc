package fr.cloud.fabricdiscord.config;

import lombok.Getter;

public class Configuration extends BaseConfiguration {

    @ConfigKey @Getter private final String token = new String("");
    @ConfigKey @Getter private final String serverId = new String("");
    @ConfigKey @Getter private final String channelId = new String("");

    public Configuration() {
        super("fabricdiscord");
    }
}
