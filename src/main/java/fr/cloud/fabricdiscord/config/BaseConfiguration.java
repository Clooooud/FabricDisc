package fr.cloud.fabricdiscord.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.cloud.fabricdiscord.FabricDiscord;
import lombok.SneakyThrows;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseConfiguration {

    private final File file;

    protected BaseConfiguration(String name) {
        file = new File(FabricLoaderImpl.InitHelper.get().getConfigDir().toString(), name + ".json");
    }

    @SneakyThrows
    public void loadConfig() {
        if (!createFile()) {
            loadValues();
        }
    }

    private void loadValues() throws IOException, IllegalAccessException {
        if (!isValid()) {
            reset();
            FabricDiscord.LOGGER.error("Invalid configuration, resetting the file");
        }

        Map map = getMapFromFile();
        for (Field field : getConfigFields()) {
            field.set(this, map.get(field.getName()));
        }
    }

    private void reset() throws FileNotFoundException, IllegalAccessException {
        HashMap<String, String> configKeys = new HashMap<>();

        for (Field field : getConfigFields()) {
            configKeys.put(field.getName(), (String) field.get(this));
        }

        PrintWriter out = new PrintWriter(new FileOutputStream(file), true);
        String content = new GsonBuilder().setPrettyPrinting().create().toJson(configKeys);
        out.println(content);
        out.close();
    }

    private boolean createFile() throws IOException, IllegalAccessException {
        if (!file.exists()) {
            file.createNewFile();
            reset();
            return true;
        }
        return false;
    }

    private Map getMapFromFile() throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(file), Map.class);
    }

    private boolean isValid() throws FileNotFoundException {
        return getMapFromFile().keySet().containsAll(getConfigFields().stream().map(Field::getName).collect(Collectors.toList()));
    }

    private List<Field> getConfigFields() {
        return Stream.of(this.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ConfigKey.class))
                .peek(field -> field.setAccessible(true))
                .peek(FieldUtils::removeFinalModifier)
                .collect(Collectors.toList());
    }


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigKey {
    }
}

