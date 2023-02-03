package com.tipre.dashboard.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.tipre.dashboard.model.fileinfo.FileDB;
import com.tipre.dashboard.model.user.User;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file);

  public Resource load(String filename);

  public void deleteAll();

  public Stream<Path> loadAll();
  
  public User store(MultipartFile file, Long id) throws IOException;
  public FileDB getFile(Long id);
  public Stream<FileDB> getAllFiles();
}