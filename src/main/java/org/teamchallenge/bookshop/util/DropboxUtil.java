package org.teamchallenge.bookshop.util;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.teamchallenge.bookshop.exception.AccessTokenRefreshException;
import org.teamchallenge.bookshop.exception.MissingAccessTokenException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DropboxUtil {
    private static String ACCESS_TOKEN = System.getenv("DROPBOX_TOKEN");
    private static final String APP_KEY = System.getenv("APP_KEY");
    private static final String APP_SECRET = System.getenv("APP_SECRET");
    private static final String REFRESH_TOKEN = System.getenv("REFRESH_TOKEN");
    private static final String TOKEN_ENDPOINT = "https://api.dropbox.com/oauth2/token";

    public static DbxClientV2 getClient() throws DbxException {
        ensureAccessToken();
        DbxRequestConfig config = DbxRequestConfig.newBuilder("tcl").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        client = validateClient(client, config);
        return client;
    }

    private static void ensureAccessToken() throws MissingAccessTokenException {
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            ACCESS_TOKEN = refreshAccessToken();
        }
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            throw new MissingAccessTokenException();
        }
    }
    private static DbxClientV2 validateClient(DbxClientV2 client, DbxRequestConfig config)  {
        try {
            client.users().getCurrentAccount();
            return client;
        } catch (DbxException e) {
            if (e.getMessage().contains("expired_access_token")) {
                ACCESS_TOKEN = refreshAccessToken();
                return new DbxClientV2(config, ACCESS_TOKEN);
            } else {
                throw new MissingAccessTokenException();
            }
        }
    }

    private static String refreshAccessToken() throws AccessTokenRefreshException  {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(TOKEN_ENDPOINT);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            String body = String.format("grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s",
                    REFRESH_TOKEN, APP_KEY, APP_SECRET);
            post.setEntity(new StringEntity(body));
            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseBody, HashMap.class);
                return (String) map.get("access_token");
            }
        } catch (IOException e) {
            throw new AccessTokenRefreshException();
        }
    }
}