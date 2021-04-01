package com.web.training.repositories;

import com.web.training.models.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> getByIdAndWorkoutIdAndWorkout_Day_Training_UserId(Long exerciseId, Long workoutId, Long userId);

    @Transactional
    @Modifying
    void deleteAllByWorkoutIdAndIdNotIn(Long workoutId, List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByWorkoutId(Long workoutId);
}
