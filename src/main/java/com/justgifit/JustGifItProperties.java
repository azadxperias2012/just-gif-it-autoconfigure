package com.justgifit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "com.justgifit")
public class JustGifItProperties {

    /**
     * The location of the animated gifs
     */
    private File gifLoacation;

    /**
     * Whether or not to optimize web filters
     */
    private Boolean optimize;

    public File getGifLoacation() {
        return gifLoacation;
    }

    public void setGifLoacation(File gifLoacation) {
        this.gifLoacation = gifLoacation;
    }

    public Boolean getOptimize() {
        return optimize;
    }

    public void setOptimize(Boolean optimize) {
        this.optimize = optimize;
    }
}
