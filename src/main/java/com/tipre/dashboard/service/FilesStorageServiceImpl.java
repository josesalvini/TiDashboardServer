package com.tipre.dashboard.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tipre.dashboard.model.fileinfo.FileDB;
import com.tipre.dashboard.model.user.User;
import com.tipre.dashboard.repository.FileRepository;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  //private static final Logger LOGGER = LoggerFactory.getLogger(FilesStorageServiceImpl.class);	
	
  private final Path root = Paths.get("uploads");

  @Autowired
  private FileRepository fileRepository;
  
  @Autowired
  private UsersService usersService;

  @Override
  @Transactional
  public User store(MultipartFile file, Long id) throws IOException {
	
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB fileDB = FileDB
		    		.builder()
		    		.name(fileName)
		    		.type(file.getContentType())
		    		.data(file.getBytes())
		    		.build();
        
    Optional<User> user = usersService.findById(id);
    User updatedUser = null;
    
    if (user.isPresent()) {  	
    	updatedUser = user.get();
    	
    	//LOGGER.info("Usuario encontrado {}", updatedUser.getUsername()); 
    	    	
    	updatedUser.setAvatar(fileDB);    	
    	usersService.save(updatedUser); 	    	
    	 	   	
    }
    
    return updatedUser != null ? updatedUser : null;
  }

  @Override
  @Transactional
  public FileDB getFile(Long id) {
    return fileRepository.findById(id).get();
  }
  
  @Override
  @Transactional
  public Stream<FileDB> getAllFiles() {
    return fileRepository.findAll().stream();
  }
  
  @Override
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file) {
    try {
      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        throw new RuntimeException("Existe un archivo con el mismo nombre.");
      }

      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }
}