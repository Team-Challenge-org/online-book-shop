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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DropboxUtil {
    private static String ACCESS_TOKEN = "";
    private static final String APP_KEY = System.getenv("APP_KEY");
    private static final String APP_SECRET = System.getenv("APP_SECRET");
    private static final String REFRESH_TOKEN = System.getenv("REFRESH_TOKEN");
    private static final String TOKEN_ENDPOINT = "https://api.dropbox.com/oauth2/token";

    public static DbxClientV2 getClient() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("tcl").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        try {
            client.users().getCurrentAccount();
        } catch (DbxException e) {
            if (e.getMessage().contains("Invalid authorization value")) {
                ACCESS_TOKEN = refreshAccessToken();
                client = new DbxClientV2(config, ACCESS_TOKEN);
            }
        }
        return client;
    }

    private static String refreshAccessToken(){
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
            throw new RuntimeException(e);
        }
    }
}