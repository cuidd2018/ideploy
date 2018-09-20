package io.ideploy.deployment.cfg;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: code4china
 * @description:
 * @date: Created in 09:54 2018/9/17
 */
public class ModuleConfig {

    public static final String KEY_DEPLOY_DIR = "deployDir";

    public static final String KEY_BACKUP_DIR = "backupDir";

    public static final String KEY_GC_FILE = "gcFilePath";

    public static final String KEY_SHELL_DIR = "shellDir";

    private static Logger logger = LoggerFactory.getLogger(ModuleConfig.class);

    protected Map<String,String> moduleDefines = new HashMap<>();

    public ModuleConfig(){

    }

    public void load(String cfgDetails){
        Properties properties = new Properties();
        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(cfgDetails.getBytes());
            properties.load(input);
            for (Entry<Object,Object> entry: properties.entrySet()){
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                moduleDefines.put(key, value);
            }
        }catch (Exception e){
            logger.error("", e);
        }
        finally {
            IOUtils.closeQuietly(input);
        }
    }

}
