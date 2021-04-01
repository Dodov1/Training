package com.web.training.repositories;

import com.web.training.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> getTop5ByUsernameStartsWithAndIdNotIn(String username, List<Long> ids);

    List<User> getTop5ByUsernameStartsWith(String username);

    Optional<User> findByEmail(String email);

    @Query("select count(t.id) from User as u JOIN Training as t on u.id = t.user.id WHERE u.id =:userId")
    Optional<Integer> countAllTrainingsById(@Param("userId") Long userId);

}
