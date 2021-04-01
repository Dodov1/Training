package com.web.training.web;

import com.web.training.exceptions.ExceptionErrorModel;
import com.web.training.models.exceptionModels.NoTrainerFoundForUserException;
import com.web.training.models.exceptionModels.TrainerNotFoundException;
import com.web.training.models.exceptionModels.UserIsAlreadyTrainerException;
import com.web.training.models.bindingModels.*;
import com.web.training.models.enums.TrainerSortByEnum;
import com.web.training.models.serviceModels.*;
import com.web.training.models.viewModels.*;
import com.web.training.service.TrainerService;
import com.web.training.service.UserService;
import com.web.training.service.UserTrainerService;
import com.web.training.service.WorkoutService;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.ASCENDING_ORDER_NAME;
import static com.web.training.config.appConstants.AppConstants.DESCENDING_ORDER_NAME;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TrainerService trainerService;
    private final WorkoutService workoutService;
    private final UserTrainerService userTrainerService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, TrainerService trainerService, WorkoutService workoutService, ModelMapper modelMapper, UserTrainerService userTrainerService) {
        this.userService = userService;
        this.trainerService = trainerService;
        this.workoutService = workoutService;
        this.modelMapper = modelMapper;
        this.userTrainerService = userTrainerService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<UserViewModel>> getUserById(@PathVariable("userId") Long userId) {
        UserServiceModel user = this.userService.getUserById(userId);
        return ResponseEntity.ok(EntityModel.of(this.modelMapper.map(user, UserViewModel.class)));
    }

    @GetMapping("/{userId}/enable")
    public ResponseEntity<HttpStatus> enableUserById(@PathVariable("userId") Long userId) {
        this.userService.enableUserById(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{userId}/trainers", params = {"page", "size", "sortBy", "orderBy"})
    public ResponseEntity<EntityModel<UsersViewModel>> getTrainersByUserId(@PathVariable("userId") Long userId,
                                                                           @RequestParam("page") Integer page,
                                                                           @RequestParam("size") Integer size,
                                                                           @RequestParam("sortBy") String sortBy,
                                                                           @RequestParam("orderBy") String orderBy
    ) {
        UsersViewModel usersViewModel = new UsersViewModel();
        List<TrainerUserInfoServiceModel> trainers = this.userTrainerService.getTrainersPageForUserId(userId, page, size, sortBy, orderBy);
        usersViewModel.setTrainers(
                trainers
                        .stream()
                        .map(el -> this.modelMapper.map(el, TrainerUserInfoViewModel.class))
                        .collect(Collectors.toList())
        );
        Integer totalPages = this.userTrainerService.getTotalTrainerPagesCountForUserId(userId, size);
        usersViewModel.setTotalPages(totalPages);
        EntityModel<UsersViewModel> model = EntityModel.of(usersViewModel);
        if (!usersViewModel.getTrainers().isEmpty()) {
            model.add(getUsersLinks(userId, page, size, totalPages, sortBy, orderBy));
        }
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{userId}/trainers/{trainerId}")
    public ResponseEntity<UserProfileViewModel> getTrainerByUserIdAndTrainerId(@PathVariable("userId") Long userId,
                                                                               @PathVariable("trainerId") Long trainerId) {
        try {
            UserProfileServiceModel trainers = this.userTrainerService.getTrainerByIdForUser(userId, trainerId);
            return ResponseEntity.ok(this.modelMapper.map(trainers, UserProfileViewModel.class));
        } catch (TrainerNotFoundException ex) {
            return ResponseEntity.ok(this.modelMapper.map(this.catchException(trainerId), UserProfileViewModel.class));
        }
    }

    @PostMapping("/{userId}/trainers/{trainerId}/rating")
    public ResponseEntity<RatingViewModel> rateTrainerByUserIdAndTrainerId(@Valid @RequestBody RatingBindingModel ratingBindingModel,
                                                                           @PathVariable("userId") Long userId,
                                                                           @PathVariable("trainerId") Long trainerId) {
        try {
            RatingServiceModel rating = this.userTrainerService.rateTrainer(userId, trainerId, ratingBindingModel);
            return ResponseEntity.ok(this.modelMapper.map(rating, RatingViewModel.class));
        } catch (NoTrainerFoundForUserException ignored) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{userId}/trainerRequests")
    public ResponseEntity<List<RequestViewModel>> getUserRequestsByTrainersId(@PathVariable("userId") Long userId) {
        List<RequestServiceModel> trainerRequests = this.userTrainerService.getAllRequestsForUser(userId);
        return ResponseEntity.ok(trainerRequests
                .stream()
                .map(el -> this.modelMapper.map(el, RequestViewModel.class))
                .collect(Collectors.toList())
        );
    }

    @PostMapping("/{userId}/request")
    public ResponseEntity<HttpStatus> addRequestToUser(@Valid @RequestBody RequestBindingModel requestDto, @PathVariable("userId") Long userId) {
        this.userTrainerService.requestManager(requestDto, requestDto.getReceiverId(), userId, false);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/request")
    public ResponseEntity<HttpStatus> addTrainerByUserId(@Valid @RequestBody RequestBindingModel requestDto, @PathVariable("userId") Long userId) {
        this.userTrainerService.requestManager(requestDto, requestDto.getReceiverId(), userId, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserViewModel> getAllUserTrainingsById(@Valid @RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        UserServiceModel isCreated = this.userService.addNewUser(userRegisterBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(isCreated, UserViewModel.class));
    }

    @GetMapping("/checkUsername/{username}")
    public ResponseEntity<HttpStatus> checkIfUserNameExistsStartingWith(@PathVariable("username") String username) {
        boolean exists = this.userService.checkIfUsernameExists(username);
        if (exists) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<HttpStatus> checkIfEmailExistsStartingWith(@PathVariable("email") String email) {
        boolean exists = this.userService.checkIfEmailExists(email);
        if (exists) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getSearchSuggestions")
    public ResponseEntity<List<UserBasicPicViewModel>> getUsersStartingWithUsername(
            @Valid @RequestBody SearchSuggestionBindingModel searchSuggestionBindingModel
    ) {
        List<UserBasicPicServiceModel> user = this.userService.getSuggestionsByUsername(searchSuggestionBindingModel);
        return ResponseEntity.ok(
                user
                        .stream()
                        .map(el -> this.modelMapper.map(el, UserBasicPicViewModel.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<TodayViewModel> getAllWorkoutsForToday(@PathVariable("userId") Long userId) {
        LocalDate now = LocalDate.now();
        TodayServiceModel today = this.workoutService.getAllWorkoutsForToday(now, userId);
        return ResponseEntity.ok(this.modelMapper.map(today, TodayViewModel.class));
    }

    @PostMapping("/{userId}/requestToBecomeTrainer")
    public ResponseEntity<HttpStatus> addRequestForTrainer(@Valid @RequestBody AddTrainerRequestBindingModel
                                                                   requestDto,
                                                           @PathVariable("userId") Long userId) {
        try {
            this.userService.requestToBecomeTrainer(userId, requestDto);
        } catch (UserIsAlreadyTrainerException e) {
            this.handleUserExistsException(e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/enums")
    public ResponseEntity<TrainerEnumsViewModel> getTrainingEnums() {
        TrainerEnumsServiceModel enums = this.userService.getTrainerEnums();
        return ResponseEntity.ok(this.modelMapper.map(enums, TrainerEnumsViewModel.class));
    }


    @ExceptionHandler(value = {UserIsAlreadyTrainerException.class})
    protected ResponseEntity<Object> handleUserExistsException(UserIsAlreadyTrainerException ex) {
        ExceptionErrorModel apiError = new ExceptionErrorModel(CONFLICT);
        apiError.setMessage(ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(apiError);
    }

    @ExceptionHandler(value = TrainerNotFoundException.class)
    public UserProfileViewModel catchException(Long id) {
        return this.modelMapper.map(
                this.trainerService.getTrainerProfileById(id), UserProfileViewModel.class
        );
    }

    private Link[] getUsersLinks(Long userId, Integer page, Integer size, Integer totalPages, String sortBy, String
            orderBy) {
        Link self = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, page, size, sortBy, orderBy))
                .withSelfRel();
        Link next = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, page + 1, size, sortBy, orderBy))
                .withRel("next");
        Link prev = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, page - 1, size, sortBy, orderBy))
                .withRel("prev");
        Link first = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, sortBy, orderBy))
                .withRel("first");
        Link last = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, totalPages, size, sortBy, orderBy))
                .withRel("last");
        Link sortByTypeAsc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.type.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByTypeAsc");
        Link sortByTypeDesc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.type.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByTypeDesc");
        Link sortByRatingAsc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.rating.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByRatingAsc");
        Link sortByRatingDesc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.rating.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByRatingDesc");
        Link sortByTotalUsersAsc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.totalUsers.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByTotalUsersAsc");
        Link sortByTotalUsersDesc = linkTo(methodOn(UserController.class)
                .getTrainersByUserId(userId, 0, size, TrainerSortByEnum.totalUsers.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByTotalUsersDesc");

        if (totalPages == 0) {
            return List.of(self,
                    sortByTypeAsc, sortByTypeDesc, sortByRatingAsc, sortByRatingDesc, sortByTotalUsersAsc, sortByTotalUsersDesc
            ).toArray(new Link[0]);
        }
        if (page == 0) {
            return List.of(self, next, first, last,
                    sortByTypeAsc, sortByTypeDesc, sortByRatingAsc, sortByRatingDesc, sortByTotalUsersAsc, sortByTotalUsersDesc
            ).toArray(new Link[0]);
        } else if (page.equals(totalPages)) {
            return List.of(
                    self, prev, first, last, sortByTypeAsc, sortByTypeDesc, sortByRatingAsc, sortByRatingDesc, sortByTotalUsersAsc, sortByTotalUsersDesc
            ).toArray(new Link[0]);
        } else {
            return List.of(
                    self, prev, next, first, last, sortByTypeAsc, sortByTypeDesc, sortByRatingAsc, sortByRatingDesc, sortByTotalUsersAsc, sortByTotalUsersDesc
            ).toArray(new Link[0]);
        }
    }
}
