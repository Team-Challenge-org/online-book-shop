package org.teamchallenge.bookshop.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public interface DropboxService {
    void createFolder(String path);

    String uploadImage(String path, BufferedImage bufferedImage);
}
