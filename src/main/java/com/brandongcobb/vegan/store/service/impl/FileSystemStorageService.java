package com.brandongcobb.vegan.store.service.impl;

import com.brandongcobb.vegan.store.service.FileStorageService;

public class FileSystemStorageService implements FileStorageService {
    @Override
    public String store(String filename, InputStream data) {
        return null;
    }

    @Override
    public String generateUniqueFilename(String originalFilename) {
        return null;
    }

    @Override
    public String getPublicUrl(String filename) {
        return null;
    }
}
