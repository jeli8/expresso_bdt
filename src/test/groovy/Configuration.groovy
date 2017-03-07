import jdk.nashorn.internal.ir.PropertyKey

/**
 * Created by yossit on 3/7/17.
 */
class AppConfiguration {
    private static final AppConfiguration instance = new AppConfiguration();
    private static final String DEV_PROPS = "dev.env.properties"
    private static final String PROD_PROPS = "prod.env.properties"

    static AppConfiguration getInstance() {
        return instance
    }

    Properties loadedProperties = null

    private AppConfiguration() {
        loadedProperties = loadPropertiesFromFIle(getPropertiesFile())
    }

    String getProperty(String key) {
        return loadedProperties.getProperty(key)
    }

    private getPropertiesFile() {
        String env = System.getProperty("env");
        if (env == null || env.isEmpty() || "DEV".equalsIgnoreCase(env)) {
            return DEV_PROPS
        } else {
            return PROD_PROPS
        }
    }

    private loadPropertiesFromFIle(String file) {
        Properties props = new Properties();
        final InputStream stream = this.getClass().getResourceAsStream(DEV_PROPS)
        props.load(stream);
        return props;
    }

    interface PropertyKeys {
        String BASE_URL = 'com.exelate.bdt.test.url'
    }

}



