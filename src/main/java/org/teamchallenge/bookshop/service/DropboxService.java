package org.teamchallenge.bookshop.service;

import java.awt.image.BufferedImage;


public interface DropboxService {
    void createFolder(String path);

    String uploadImage(String path, BufferedImage bufferedImage);
}
