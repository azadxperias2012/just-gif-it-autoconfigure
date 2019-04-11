package com.justgifit;

import com.justgifit.services.ConvertorService;
import com.justgifit.services.GifEncoderService;
import com.justgifit.services.VideoDecoderService;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import java.io.File;

@Configuration
@ConditionalOnClass({FFmpegFrameGrabber.class, AnimatedGifEncoder.class})
@EnableConfigurationProperties(JustGifItProperties.class)
public class JustGifItAutoConfiguration {

    @Inject
    private JustGifItProperties properties;

    @ConditionalOnProperty(prefix = "com.justgifit", name = "create-result-dir")
    public void createResultDir() {
        if (!properties.getGifLoacation().exists()) {
            properties.getGifLoacation().mkdir();
        }
    }

    @Bean
    @ConditionalOnMissingBean(VideoDecoderService.class)
    public VideoDecoderService videoDecoderService() {
        return new VideoDecoderService();
    }

    @Bean
    @ConditionalOnMissingBean(GifEncoderService.class)
    public GifEncoderService gifEncoderService() {
        return new GifEncoderService();
    }

    @Bean
    @ConditionalOnMissingBean(ConvertorService.class)
    public ConvertorService convertorService() {
        return new ConvertorService();
    }

    @Configuration
    @ConditionalOnWebApplication
    public static class WebConfiguration {

        @Value("${multipart.location}/gif/")
        private String gifLoacation;

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterHiddenHttpMethodFilter(
                HiddenHttpMethodFilter filter) {
            FilterRegistrationBean bean = new FilterRegistrationBean(filter);
            bean.setEnabled(false);
            return bean;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterHttpPutFormContentFilter(
                HttpPutFormContentFilter filter) {
            FilterRegistrationBean bean = new FilterRegistrationBean(filter);
            bean.setEnabled(false);
            return bean;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.justgifit", name = "optimize")
        public FilterRegistrationBean deRegisterRequestContextFilter(
                RequestContextFilter filter) {
            FilterRegistrationBean bean = new FilterRegistrationBean(filter);
            bean.setEnabled(false);
            return bean;
        }

        @Bean
        public WebMvcConfigurer webMvcConfigurer() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addResourceHandlers(ResourceHandlerRegistry registry) {
                    registry.addResourceHandler("/gif/**")
                            .addResourceLocations("file:" + gifLoacation);
                    super.addResourceHandlers(registry);
                }
            };
        }
    }

}
