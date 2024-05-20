package org.teamchallenge.bookshop.service.Impl;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.DropboxService;
import org.teamchallenge.bookshop.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class DropboxServiceImpl implements DropboxService {
    private static final String TOKEN= System.getenv("DROPBOX_TOKEN");
    private static final DbxRequestConfig config = DbxRequestConfig
            .newBuilder("tcl-bookshop")
            .build();
    private static final DbxClientV2 client = new DbxClientV2(config, TOKEN);

    @Override
    public void createFolder(String path) {
        try {
            client.files().createFolderV2(path);
        } catch (CreateFolderErrorException e) {
            System.out.println("Error occurred while creating a folder" + e.getMessage());
        } catch (DbxException e) {
            System.out.println("Dropbox exception:" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public String uploadImage(String path, BufferedImage bufferedImage) {
        try (InputStream in = new ByteArrayInputStream(ImageUtil.bufferedImageToBytes(bufferedImage))) {
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(in);
            return client.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UploadErrorException e) {
            throw new RuntimeException(e);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }
}
