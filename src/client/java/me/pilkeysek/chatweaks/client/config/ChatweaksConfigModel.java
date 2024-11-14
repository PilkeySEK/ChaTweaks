package me.pilkeysek.chatweaks.client.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;

import java.util.List;

@Modmenu(modId = "chatweaks")
@Config(name = "chatweaks-config", wrapperName = "ChatweaksConfig")
public class ChatweaksConfigModel {
    @Nest
    public PrefixNest prefixNest = new PrefixNest();
    @Nest
    public SuffixNest suffixNest = new SuffixNest();
    @Nest
    public FilteringNest filteringNest = new FilteringNest();

    public static class PrefixNest {
        public boolean usePrefix = false;
        public String prefix = "";
        public boolean addPrefixToHistory = true;
        public List<String> disablePrefixIfPrefix = List.of("/");
        public List<String> disablePrefixIfSuffix = List.of();
    }
    public static class SuffixNest {
        public boolean useSuffix = false;
        public String suffix = "";
        public boolean addSuffixToHistory = true;
        public List<String> disableSuffixIfPrefix = List.of("/");
        public List<String> disableSuffixIfSuffix = List.of();
    }
    public static class FilteringNest {
        public boolean useFilter = false;
        public String filterRegex = "";
    }
}
