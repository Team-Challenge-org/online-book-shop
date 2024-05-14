package org.teamchallenge.bookshop.service.Impl;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.DropboxService;

@Service
public class DropboxServiceImpl implements DropboxService {
    private static final String token = System.getenv("DROPBOX_TOKEN");

    static {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, token);
    }



}
