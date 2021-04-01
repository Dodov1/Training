package com.web.training;

import com.web.training.models.entities.Picture;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static com.web.training.config.appConstants.AppConstants.DEFAULT_PICTURE_NAME;

@SpringBootTest
public class ConsoleRunnerTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PictureRepository pictureRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ConsoleRunner consoleRunner;


    private User testUser;
    private Trainer testTrainer;

    @BeforeEach
    private void init() {
        this.testTrainer = new Trainer();
        this.testTrainer.setType(TrainerType.Group);
        this.testTrainer.setId(1L);
        this.testTrainer.setStatus(RelationStatus.Accepted);
        this.testTrainer.setUser(this.testUser);
        this.testTrainer.setUsers(new HashSet<>());
        this.testUser = new User() {{
            setId(1L);
            setEnabled(true);
            setTrainer(testTrainer);
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
        this.consoleRunner = new ConsoleRunner(this.userRepository, this.pictureRepository, this.trainerRepository, this.passwordEncoder, this.authorityRepository);
    }

    /**
     * ConsoleRunner - run(...) TESTS
     */

    @Test
    public void runShouldReturnCorrectPictureNotNull() throws Exception {
        Picture picture = new Picture();
        picture.setType("sad");
        picture.setLocation("Demo Location");
        Mockito.when(this.pictureRepository.count())
                .thenReturn(0L);
        Mockito.when(this.userRepository.count())
                .thenReturn(0L);
        Mockito.when(this.trainerRepository.count())
                .thenReturn(0L);
        Mockito.when(this.userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(this.testUser);
        Mockito.when(this.pictureRepository.getByName(DEFAULT_PICTURE_NAME))
                .thenReturn(picture);
        consoleRunner.run();
        Assertions.assertNotNull(picture.getUser(), "Application Init Not Correct!");
    }

    @Test
    public void runShouldReturnCorrectTrainerNotNull() throws Exception {
        Picture picture = new Picture();
        this.testUser.setTrainer(null);
        picture.setType("sad");
        picture.setLocation("Demo Location");
        Mockito.when(this.pictureRepository.count())
                .thenReturn(0L);
        Mockito.when(this.userRepository.count())
                .thenReturn(0L);
        Mockito.when(this.trainerRepository.count())
                .thenReturn(0L);
        Mockito.when(this.userRepository.saveAndFlush(Mockito.any(User.class)))
                .thenReturn(this.testUser);
        Mockito.when(this.trainerRepository.saveAndFlush(Mockito.any(Trainer.class)))
                .thenReturn(this.testTrainer);
        Mockito.when(this.pictureRepository.getByName(DEFAULT_PICTURE_NAME))
                .thenReturn(picture);
        consoleRunner.run();
        Assertions.assertNotNull(this.testUser.getTrainer(), "Application Init Not Correct!");
    }
}
