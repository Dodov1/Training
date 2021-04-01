package com.web.training.repositories;

import com.web.training.models.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Picture getByName(String name);

    Optional<Picture> findByUserId(Long userId);

    Optional<Picture> getByUser_TrainerId(Long trainerId);
}
