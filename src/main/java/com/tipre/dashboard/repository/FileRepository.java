package com.tipre.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tipre.dashboard.model.fileinfo.FileDB;

public interface FileRepository extends JpaRepository<FileDB, Long>{

}
