package com.ingenieriasch.fileupload.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriasch.fileupload.exceptions.InvalidContentTypeException;
import com.ingenieriasch.fileupload.services.FileService;
import com.ingenieriasch.fileupload.web.payload.UploadResponse;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) {

		try {

			UploadResponse res = fileService.fileUpload(file);

			return ResponseEntity.ok(res);

		} catch (InvalidContentTypeException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.internalServerError().build();

		}

	}
	
	
	@GetMapping("/{fileCode}")
	public ResponseEntity<?> getFile(@PathVariable("fileCode") String fileCode) {
		
		//TODO get file
		//fileService.get(fileCode);
		
		return null;
		
	}

}
