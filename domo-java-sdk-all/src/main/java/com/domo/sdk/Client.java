package com.domo.sdk;

import com.domo.sdk.request.Config;
import com.domo.sdk.request.OAuthInterceptor;
import com.domo.sdk.data.DataClient;
import com.domo.sdk.request.Transport;
import com.domo.sdk.request.UrlBuilder;
import com.domo.sdk.users.UserClient;
import okhttp3.OkHttpClient;

public class Client {
    private final Config config;
    private final UserClient userClient;
    private final DataClient dataClient;

    private final OkHttpClient httpClient;
    private final Transport transport;
    private final UrlBuilder urlBuilder;

    private Client(Config config) {
        this.config = config;
        this.urlBuilder = new UrlBuilder(config);
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new OAuthInterceptor(urlBuilder, config))
                .build();
        this.transport = new Transport(httpClient);

        this.userClient = new UserClient(urlBuilder, transport);
        this.dataClient = new DataClient(urlBuilder, transport);
    }

    public static Client create(String clientId, String secret) {
        return new Client(new Config(clientId, secret, "api.domo.com"));
    }

    public static Client create(Config config) {
        return new Client(config);
    }

    public Config getConfig() {
        return config;
    }

    public UserClient userClient() {
        return userClient;
    }

    public DataClient dataClient() {
        return dataClient;
    }
}