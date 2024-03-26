package biz.netcentric.digitalxn.aem.internal.services;

import biz.netcentric.digitalxn.aem.services.ChatAemEndpointService;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { ChatAemEndpointService.class }, immediate = true)
@Designate(ocd = ChatAemEndpointServiceImpl.Config.class)
public class ChatAemEndpointServiceImpl implements ChatAemEndpointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatAemEndpointServiceImpl.class);

    private String hmacKey;

    private String appUrl;

    @Override
    public String getAppUrl() {
        final String appUrl = StringUtils.isNotBlank(this.appUrl) ? this.appUrl : "";
        LOGGER.info("Using app url " + appUrl);
        return appUrl;
    }

    @Override
    public String getHmacKey() {
        return StringUtils.isNotBlank(this.hmacKey) ? this.hmacKey : "";
    }

    @Activate
    protected void activate(Config config) {
        LOGGER.info("Activating/updating ChatAemEndpointServiceImpl Service");
        this.hmacKey = config.hmac();
        this.appUrl = config.url();
    }

    @ObjectClassDefinition(name = "NC ChatAEM - Endpoint Service Configuration", description = "Integration config for ChatAEM LLM app endpoint.")
    protected @interface Config {

        @AttributeDefinition(name = "ChatAEM Hmac Key", description = "Hmac Key for token signing", type = AttributeType.STRING)
        String hmac() default "";

        @AttributeDefinition(name = "ChatAEM Application URL", description = "URL of the chat aem app. Points to localhost if not explicitly set.", type = AttributeType.STRING)
        String url() default "";
    }
}
