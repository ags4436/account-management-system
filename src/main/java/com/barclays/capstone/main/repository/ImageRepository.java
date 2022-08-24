package com.barclays.capstone.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barclays.capstone.main.model.ImageUpload;


@Repository
public interface ImageRepository extends JpaRepository<ImageUpload, Integer> {

}
