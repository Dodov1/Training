package com.web.training.repositories;

import com.web.training.models.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> getAllByDay_Training_UserIdAndDay_DateOrderById(Long userId, LocalDate localDate);

    Optional<Integer> countAllByDay_DateAndLinkIsNullAndDay_Training_UserId(LocalDate date, Long userId);

    @Query("SELECT count(w.id) FROM Workout  w " +
            "JOIN Day as d on d.id=w.day.id " +
            "JOIN Training as t on t.id=d.training.id " +
            "JOIN User as u on u.id=t.user.id " +
            "WHERE w.day.date=:date and w.link is not null and u.id=:userId")
    Optional<Integer> sumLinkWorkouts(@Param("date") LocalDate date, @Param("userId") Long userId);

    @Query("SELECT sum (size(w.exercises)) FROM Workout w " +
            "JOIN Day as d on d.id=w.day.id " +
            "JOIN Training as t on t.id=d.training.id " +
            "JOIN User as u on u.id=t.user.id " +
            "WHERE w.day.date=:date and u.id=:userId")
    Optional<Integer> sumExercises(@Param("date") LocalDate date, @Param("userId") Long userId);

    Optional<Integer> countAllByDayIdAndLinkIsNull(Long dayId);

    Optional<Integer> countAllByDayIdAndLinkIsNotNull(Long dayId);

    @Query("select sum (size(w.exercises)) FROM Workout as w WHERE w.day.id = :dayId")
    Optional<Integer> countAllExercisesForDayId(@Param("dayId") Long dayId);

    @Transactional
    @Modifying
    void deleteAllByDayIdAndIdNotIn(Long dayId, List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByDayId(Long dayId);
}
