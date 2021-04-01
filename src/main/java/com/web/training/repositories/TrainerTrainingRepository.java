package com.web.training.repositories;

import com.web.training.models.entities.TrainerTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerTrainingRepository extends JpaRepository<TrainerTraining, Long> {

    List<TrainerTraining> getAllByTrainerIdAndTraining_UserIsNull(Long trainerId);

    Optional<TrainerTraining> getByTrainerIdAndTrainingId(Long trainerId, Long trainingId);

    Optional<TrainerTraining> getByTrainerIdAndTrainingIdAndTraining_UserIsNull(Long trainerId, Long trainingId);
}
