package com.ingenieriasch.fileupload.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenieriasch.fileupload.model.File;

@Repository
public interface ImageRepository extends JpaRepository<File, String> {

}
