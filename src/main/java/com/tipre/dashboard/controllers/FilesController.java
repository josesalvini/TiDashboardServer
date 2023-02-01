package com.tipre.dashboard.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tipre.dashboard.dto.ResponseFile;
import com.tipre.dashboard.dto.ResponseMessage;
import com.tipre.dashboard.model.fileinfo.FileDB;
import com.tipre.dashboard.service.FilesStorageService;


@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FilesController {
	  @Autowired
	  FilesStorageService storageService;

	  @PostMapping("/{id}")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
	    String message = "";
	    try {
	      storageService.store(file, id);
	      
	      message = "Uploaded: Archivos subidos correctamente " + file.getOriginalFilename();
	      return ResponseEntity
	    		  .status(HttpStatus.OK)
	    		  .body(ResponseMessage
	    				  .builder()
	    				  .statusCode(HttpStatus.OK.value())
	    				  .message(message)
	    				  .build());
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	      return ResponseEntity
	    		  .status(HttpStatus.EXPECTATION_FAILED)
	    		  .body(ResponseMessage
						  .builder()
						  .statusCode(HttpStatus.EXPECTATION_FAILED.value())
						  .message(message)
						  .build());
	    }
	  }
	  
	  @GetMapping
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/api/v1/files/")
	          .path(dbFile.getId().toString())
	          .toUriString();

	      return new ResponseFile(
	          dbFile.getName(),
	          fileDownloadUri,
	          dbFile.getType(),
	          dbFile.getData().length);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }
	  
	  @GetMapping("/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
	    FileDB fileDB = storageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  }
	  
	  /*@GetMapping
	  public ResponseEntity<List<FileInfo>> getListFiles() {
	    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
	      String filename = path.getFileName().toString();
	      String url = MvcUriComponentsBuilder
	          .fromMethodName(FilesController.class, 
	        		  "getFile", 
	        		  path.getFileName().toString()).build().toString();

	      return new FileInfo(filename, url);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	  }

	  @GetMapping("/files/{filename:.+}")
	  @ResponseBody
	  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
	    Resource file = storageService.load(filename);
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	  }*/
}
