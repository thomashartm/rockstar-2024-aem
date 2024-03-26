package biz.netcentric.digitalxn.aem.internal.models.v1.chataemauth;

import biz.netcentric.digitalxn.aem.models.ChatAemAuth;
import biz.netcentric.digitalxn.aem.services.ChatAemEndpointService;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ChatAemAuth.class,
        ComponentExporter.class }, resourceType = ChatAemAuthImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ChatAemAuthImpl implements ChatAemAuth {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatAemAuthImpl.class);

    public static final String RESOURCE_TYPE = "digitalxn/base/components/chataemauth";

    public static final String EMPTY_KEY_MESSAGE = "missing-hmac-key";

    public static final long DAY = 3600 + 2;

    @OSGiService
    private ChatAemEndpointService chatAemEndpointService;

    @Self
    private SlingHttpServletRequest request;

    @Override
    public String getToken() {
        return this.generateToken();
    }

    @Override
    public String getAppUrl() {
        return chatAemEndpointService.getAppUrl();
    }

    private String generateToken() {
        final String principalName = request.getUserPrincipal().getName();

        final Instant instant = Instant.now();
        final long timeStampSeconds = instant.getEpochSecond();
        final String hmacKey = chatAemEndpointService.getHmacKey();
        final String appUrl = chatAemEndpointService.getAppUrl();
        LOGGER.info("Using App url: " + appUrl);
        if (StringUtils.isNotEmpty(hmacKey)) {
            try {
                final AccessToken accessToken = new AccessToken();
                accessToken.addPayload("iss", request.getHeader("Host"));
                accessToken.addPayload("exp", Long.toString(timeStampSeconds + DAY));
                accessToken.addPayload("iat", Long.toString(timeStampSeconds));
                accessToken.addPayload("auth_time", Long.toString(timeStampSeconds));
                accessToken.addPayload("principal", principalName);

                accessToken.addPayload("app_url", appUrl);
                return accessToken.encode(hmacKey);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return EMPTY_KEY_MESSAGE;
    }
}
