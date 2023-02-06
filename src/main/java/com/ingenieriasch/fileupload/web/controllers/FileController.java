package com.ingenieriasch.fileupload.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ingenieriasch.fileupload.exceptions.InvalidContentTypeException;
import com.ingenieriasch.fileupload.exceptions.MyFileNotFoundException;
import com.ingenieriasch.fileupload.model.File;
import com.ingenieriasch.fileupload.model.ResourceDto;
import com.ingenieriasch.fileupload.services.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileService fileService;
	
	@GetMapping("")
	public ResponseEntity<?> getFiles() {

		try {

			List<File> files = fileService.loadAll();
			return ResponseEntity.ok(files);

		}  catch (Exception e) {

			return ResponseEntity.internalServerError().build();

		}

	}

	@PostMapping("/upload")
	public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) {

		try {

			File res = fileService.upload(file);

			return ResponseEntity.ok(res);

		} catch (InvalidContentTypeException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.internalServerError().build();

		}

	}

	@GetMapping("/{fileId}")
	public ResponseEntity<?> getFile(@PathVariable("fileId") String fileId) {

		try {

			File file = fileService.loadFile(fileId);
			return ResponseEntity.ok(file);

		} catch (MyFileNotFoundException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {

			return ResponseEntity.internalServerError().build();

		}

	}

	@GetMapping("/resources/{fileId}")
	public ResponseEntity<?> getResource(@PathVariable("fileId") String fileId) {

		try {

			ResourceDto file = fileService.loadFileAsResource(fileId);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContentType()))
					.body(file.getResource());

		} catch (MyFileNotFoundException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {

			return ResponseEntity.internalServerError().build();

		}

	}

}
