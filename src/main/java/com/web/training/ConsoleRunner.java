package com.web.training;

import com.web.training.models.entities.Authority;
import com.web.training.models.entities.Picture;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.enums.RelationStatus;
import com.web.training.models.enums.TrainerType;
import com.web.training.repositories.AuthorityRepository;
import com.web.training.repositories.PictureRepository;
import com.web.training.repositories.TrainerRepository;
import com.web.training.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.web.training.config.appConstants.AppConstants.*;

@Controller
@Transactional
public class ConsoleRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public ConsoleRunner(UserRepository userRepository, PictureRepository pictureRepository, TrainerRepository trainerRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.pictureRepository.count() == 0) {
            Picture defaultPicture = new Picture();
            defaultPicture.setUser(null);
            defaultPicture.setType("image/png");
            defaultPicture.setLocation("assets\\images\\default-user-male.png");
            defaultPicture.setName(DEFAULT_PICTURE_NAME);
            this.pictureRepository.saveAndFlush(defaultPicture);
        }

        if (this.userRepository.count() == 0 && this.trainerRepository.count() == 0) {
            Picture picture = this.pictureRepository.getByName(DEFAULT_PICTURE_NAME);

            String passEncoded = this.passwordEncoder.encode("admin");
            User admin = new User("admin", "admin", "admin", passEncoded, "admin@mail.bg", 18, 185, true,
                    null, picture);
            admin = this.userRepository.saveAndFlush(admin);
            Authority user = new Authority(DEFAULT_USER_AUTHORITY, admin);
            this.authorityRepository.saveAndFlush(user);
            Authority trainer = new Authority(DEFAULT_TRAINER_AUTHORITY, admin);
            this.authorityRepository.saveAndFlush(trainer);
            Authority adminAuthority = new Authority(DEFAULT_ADMIN_AUTHORITY, admin);
            this.authorityRepository.saveAndFlush(adminAuthority);

            picture.setUser(admin);
            this.pictureRepository.saveAndFlush(picture);

            Trainer adminTrainer = new Trainer(admin, TrainerType.Group, RelationStatus.Accepted, LocalDate.now(), "+359882735286");
            admin.setTrainer(adminTrainer);
            this.trainerRepository.saveAndFlush(adminTrainer);
        }
    }
}
