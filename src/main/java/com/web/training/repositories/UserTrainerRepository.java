package com.web.training.repositories;


import com.web.training.models.entities.UserTrainer;
import com.web.training.models.enums.RelationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTrainerRepository extends JpaRepository<UserTrainer, Long> {

    List<UserTrainer> getAllByTrainerIdAndStatusTypeAndIsTrainerRequesterIsFalse(Long trainerId, RelationStatus relationStatus);

    List<UserTrainer> getAllByUserIdAndStatusTypeAndIsTrainerRequesterIsTrue(Long trainerId, RelationStatus relationStatus);

    List<UserTrainer> getAllByUserIdAndStatusType(Long userId, RelationStatus status);

    List<UserTrainer> getAllByTrainerIdAndStatusType(Long trainerId, RelationStatus status);

    Optional<UserTrainer> getByTrainerIdAndUserId(Long trainerId, Long userId);

    Optional<UserTrainer> getByTrainerIdAndUserIdAndStatusType(Long trainerId, Long userId, RelationStatus status);

    List<UserTrainer> getAllByTrainerIdAndStatusType(Long trainerId, RelationStatus relationStatus, Pageable pageable);

    @Query(value = "SELECT ut.id, ut.date, ut.status, ut.trainer_id, ut.user_id, ut.is_trainer_requester, ut.rating \n" +
            "FROM users_trainers as ut\n" +
            "         LEFT JOIN users u on u.id = ut.user_id\n" +
            "         LEFT JOIN trainings t on u.id = t.user_id\n" +
            "WHERE ut.trainer_id = ?\n" +
            "  AND ut.status = 'Accepted'\n" +
            "GROUP BY u.id\n" +
            "ORDER BY COUNT(t.id) DESC" +
            " LIMIT ?,? ;", nativeQuery = true)
    List<UserTrainer> getAllUsersTrainerIdSortedByTotalTrainingsDesc(Long trainerId, Long from, Long to);

    @Query(value = "SELECT ut.id, ut.date, ut.status, ut.trainer_id, ut.user_id, ut.is_trainer_requester, ut.rating \n" +
            "FROM users_trainers as ut\n" +
            "         LEFT JOIN users u on u.id = ut.user_id\n" +
            "         LEFT JOIN trainings t on u.id = t.user_id\n" +
            "WHERE ut.trainer_id = ?\n" +
            "  AND ut.status = 'Accepted'\n" +
            "GROUP BY u.id\n" +
            "ORDER BY COUNT(t.id)" +
            " LIMIT ?,? ;", nativeQuery = true)
    List<UserTrainer> getAllUsersTrainerIdSortedByTotalTrainingsAsc(Long trainerId, Long from, Long to);

    @Query(value = "SELECT  ut.id, ut.date, ut.status, ut.trainer_id, ut.user_id, ut.is_trainer_requester, ut.rating \n" +
            "FROM users_trainers as ut\n" +
            "WHERE ut.user_id = ?\n" +
            "  AND ut.status = 'Accepted'\n" +
            "GROUP BY ut.trainer_id\n" +
            "ORDER BY (SELECT COUNT(ut2.trainer_id)\n" +
            "          FROM users_trainers as ut2\n" +
            "          WHERE ut2.trainer_id = ut.trainer_id\n" +
            "            AND ut2.status = 'Accepted'\n" +
            "         ) DESC  LIMIT ?,?;", nativeQuery = true)
    List<UserTrainer> getAllTrainersUserIdSortedByTotalUsersDesc(Long trainerId, Long from, Long to);

    @Query(value = "SELECT  ut.id, ut.date, ut.status, ut.trainer_id, ut.user_id, ut.is_trainer_requester,ut.rating \n" +
            "FROM users_trainers as ut\n" +
            "WHERE ut.user_id = ?\n" +
            "  AND ut.status = 'Accepted'\n" +
            "GROUP BY ut.trainer_id\n" +
            "ORDER BY (SELECT COUNT(ut2.trainer_id)\n" +
            "          FROM users_trainers as ut2\n" +
            "          WHERE ut2.trainer_id = ut.trainer_id\n" +
            "            AND ut2.status = 'Accepted'\n" +
            "         )  LIMIT ?,?;", nativeQuery = true)
    List<UserTrainer> getAllTrainersUserIdSortedByTotalUsersAsc(Long trainerId, Long from, Long to);

    List<UserTrainer> getAllByUserIdAndStatusType(Long userId, RelationStatus relationStatus, Pageable pageable);

    Integer countAllByTrainerIdAndStatusType(Long userId, RelationStatus relationStatus);

    @Query("SELECT AVG(ut.rating) FROM UserTrainer as ut WHERE ut.trainer.id=:trainerId" +
            " and ut.rating is not null ")
    Optional<Double> sumAvgRatingForTrainer(@Param("trainerId") Long trainerId);
}
