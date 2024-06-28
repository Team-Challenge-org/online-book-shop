package org.teamchallenge.bookshop.service.Impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.DropBoxException;
import org.teamchallenge.bookshop.exception.DropboxDeleteException;
import org.teamchallenge.bookshop.exception.DropboxFolderCreationException;
import org.teamchallenge.bookshop.exception.ImageUploadException;
import org.teamchallenge.bookshop.service.DropboxService;
import org.teamchallenge.bookshop.util.DropboxUtil;
import org.teamchallenge.bookshop.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DropboxServiceImpl implements DropboxService {
    @Override
    public void createFolder(String path) {
        try {
            DbxClientV2 client = DropboxUtil.getClient();
            client.files().createFolderV2(path);
        } catch (CreateFolderErrorException e) {
            throw new RuntimeException(e);
        } catch (DbxException e) {
            throw new DropboxFolderCreationException();
        }
    }

    @Override
    public String uploadImage(String path, BufferedImage bufferedImage)  {
        try {
            DbxClientV2 client = DropboxUtil.getClient();
            try (InputStream in = new ByteArrayInputStream(ImageUtil.bufferedImageToBytes(bufferedImage))) {
                FileMetadata metadata = client.files().uploadBuilder(path)
                        .withMode(WriteMode.ADD)
                        .uploadAndFinish(in);
                return client.sharing()
                        .createSharedLinkWithSettings(metadata.getPathLower())
                        .getUrl()
                        .replace("www.dropbox.com", "dl.dropboxusercontent.com");
            } catch (IOException e) {
                throw new ImageUploadException();
            }
        } catch (DbxException e) {
            throw new DropBoxException();
        }
    }
    public void deleteFolder(String folderPath) {
        try {
            DbxClientV2 client = DropboxUtil.getClient();
            client.files().deleteV2(folderPath);
        } catch (DbxException e) {
            throw new DropboxDeleteException(folderPath);
        }
    }

}
