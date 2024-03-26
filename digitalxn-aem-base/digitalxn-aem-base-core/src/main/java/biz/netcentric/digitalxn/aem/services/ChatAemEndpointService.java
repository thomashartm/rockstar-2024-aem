package biz.netcentric.digitalxn.aem.services;

public interface ChatAemEndpointService {

    /** Provides the chat aem application URL as a string. */
    String getAppUrl();

    /** Provides the chat aem hmac key which is required to sign the auth token. */
    String getHmacKey();
}
