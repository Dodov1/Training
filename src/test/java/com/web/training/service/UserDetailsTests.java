package com.web.training.service;

import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.security.UserDetailsModified;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.UserRepository;
import com.web.training.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
public class UserDetailsTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private TrainerService trainerService;

    private UserDetailsService userDetailsService;


    private static final String WRONG_USER_USERNAME = "demo1";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper = new ModelMapper();


    private User testUser;


    @BeforeEach
    public void init() {
        Trainer trainer = new Trainer();
        trainer.setType(TrainerType.Group);
        trainer.setId(1L);
        trainer.setStatus(RelationStatus.Accepted);
        trainer.setUser(this.testUser);
        trainer.setUsers(new HashSet<>());
        this.testUser = new User() {{
            setId(1L);
            setEnabled(true);
            setTrainer(trainer);
            setTrainers(new HashSet<>());
            setTrainings(new HashSet<>());
            setWeights(new HashSet<>());
            setFirstName("PeshoF");
            setLastName("PeshoL");
            setUsername("PeshoU");
            setEmail("Pesho@mail.bg");
            setAge(18);
            setPassword(passwordEncoder.encode("asdfg"));
            setHeight(184);
        }};

        this.userDetailsService = new UserServiceImpl(this.userRepository, this.trainerService, this.authorityRepository, this.passwordEncoder, this.modelMapper, this.pictureRepository, publisher);
    }

    /**
     * UserService getUserById(...) Tests
     */

    @Test
    void loadUserByUsernameShouldReturnCorrectUserDetailsModifiedAuthoritiesSize() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        UserDetailsModified actual = (UserDetailsModified) this.userDetailsService.loadUserByUsername(this.testUser.getUsername());
        Assertions.assertEquals(this.testUser.getAuthorities().size(), actual.getAuthorities().size(), "User is not correct");
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectUserDetailsModifiedUsername() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        UserDetailsModified actual = (UserDetailsModified) this.userDetailsService.loadUserByUsername(this.testUser.getUsername());
        Assertions.assertEquals(this.testUser.getUsername(), actual.getUsername(), "User is not correct");
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectUserDetailsModifiedPassword() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        UserDetailsModified actual = (UserDetailsModified) this.userDetailsService.loadUserByUsername(this.testUser.getUsername());
        Assertions.assertEquals(this.testUser.getPassword(), actual.getPassword(), "User is not correct");
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectUserDetailsModifiedId() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        UserDetailsModified actual = (UserDetailsModified) this.userDetailsService.loadUserByUsername(this.testUser.getUsername());
        Assertions.assertEquals(this.testUser.getId(), actual.getId(), "User is not correct");
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectUserDetailsModifiedTrainer() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        UserDetailsModified actual = (UserDetailsModified) this.userDetailsService.loadUserByUsername(this.testUser.getUsername());
        Assertions.assertEquals(this.testUser.getTrainer(), actual.getTrainer(), "User is not correct");
    }

    @Test
    void loadUserByUsernameShouldThrowException() {
        Mockito.when(this.userRepository
                .findByUsername(this.testUser.getUsername()))
                .thenReturn(Optional.of(this.testUser));
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> this.userDetailsService.loadUserByUsername(WRONG_USER_USERNAME)
                , "User is not correct");
    }
}
