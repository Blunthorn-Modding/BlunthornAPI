package net.wouterb.blunthornapi.api.config;

import net.fabricmc.loader.api.FabricLoader;
import net.wouterb.blunthornapi.BlunthornAPI;
import org.apache.commons.compress.utils.FileNameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Properties;


public abstract class BlunthornConfig {
    private static final Path CONFIG_DIR = Path.of(String.valueOf(FabricLoader.getInstance().getConfigDir()));
    private static final String FILE_EXTENSION = ".properties";
    private File configFile;


    /**
     * The filepath used for the config file. Supports sub-folders using forward slashes.
     * i.e. <code>my_mod/my_mod_config.properties</code>
     */
    protected String filePath = "DEFAULT_BLUNTHORNAPI_CONFIG.properties";
    protected String modId = "BlunthornAPI";

    protected void init() {
        if (!filePath.endsWith(FILE_EXTENSION))
            filePath += FILE_EXTENSION;
        Path path = Path.of(String.valueOf(CONFIG_DIR), filePath);
        configFile = path.toFile();
        if (!configFile.exists()) {
            generateDefaultConfig();
        } else {
            load();
        }
    }

    public String getConfigName() {
        return FileNameUtils.getBaseName(filePath);
    }

    public String getConfigId() {
        return modId + "_" + getConfigName();
    }

    public void load() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(configFile.getAbsolutePath())) {
            properties.load(fileInputStream);
            BlunthornAPI.LOGGER.info("Configuration loaded from file successfully.");
        } catch (Exception e) {
            BlunthornAPI.LOGGER.error("Error loading configuration from file: " + e.getMessage());
        }

        Field[] fields = this.getClass().getDeclaredFields();

        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);

            for (Field field : fields) {
                if (field.getName().equals(propertyName)) {
                    field.setAccessible(true);
                    try {
                        setFieldValue(field, propertyValue);
                    } catch (IllegalAccessException | NumberFormatException e) {
                        System.err.println("Error setting field: " + propertyName);
                    }
                    break;
                }
            }
        }
    }

    private void writeProperty(FileWriter writer, String propertyName, String propertyValue) throws IOException {
        writer.write(propertyName + "=" + propertyValue + "\n");
    }

    public void setFieldValue(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            this.setFieldValue(field, value);
        } catch (Exception e) {
            BlunthornAPI.LOGGER.error("setFieldValue: " + e);
        }
    }

    private void setFieldValue(Field field, Object value) throws IllegalAccessException {
        try {
            field.setAccessible(true);
            var type = field.getType();
            if (type == int.class)
                field.setInt(this, (int) value);
            else if (type == boolean.class)
                field.setBoolean(this, Boolean.parseBoolean(value.toString()));
            else if (type == float.class)
                field.setFloat(this, Float.parseFloat(value.toString()));
            else
                field.set(this, value);
        } catch (ClassCastException e) {
            BlunthornAPI.LOGGER.error(String.format("Could not parse %s with value: '%s'! Using default value...", field.getName(), value));
        }
    }

    private void generateDefaultConfig() {
        configFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(configFile)) {
            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                if (!field.isAnnotationPresent(StoreInConfig.class)) continue;

                String fieldName = field.getName();

                if (field.isAnnotationPresent(BlankLine.class)){
                    writer.write("\n");
                }

                if (field.isAnnotationPresent(Comment.class)){
                    Comment comment = field.getAnnotation(Comment.class);
                    writer.write("# " + comment.value() + "\n");
                }
                try {
//                    Object value = field.get(this); // Get the value of the field for the current instance
                    writeProperty(writer, fieldName, field.get(this).toString());
                } catch (IllegalAccessException e) {
                    BlunthornAPI.LOGGER.error("Error accessing field: " + fieldName);
                }
            }
        } catch (IOException e) {
            BlunthornAPI.LOGGER.error("Error writing configuration to file: " + e.getMessage());
        }
    }
}
