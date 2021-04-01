package com.web.training.web;

import com.web.training.exceptions.ErrorResponse;
import com.web.training.models.exceptionModels.NoUserFoundForTrainerException;
import com.web.training.models.exceptionModels.UserNotFoundException;
import com.web.training.models.bindingModels.RequestBindingModel;
import com.web.training.models.bindingModels.SearchSuggestionBindingModel;
import com.web.training.models.bindingModels.TrainingAddBindingModel;
import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.bindingModels.ReadyTrainingStartBindingModel;
import com.web.training.models.enums.UserSortByEnum;
import com.web.training.models.serviceModels.*;
import com.web.training.models.viewModels.*;
import com.web.training.service.TrainerService;
import com.web.training.service.TrainerTrainingService;
import com.web.training.service.UserService;
import com.web.training.service.UserTrainerService;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.ASCENDING_ORDER_NAME;
import static com.web.training.config.appConstants.AppConstants.DESCENDING_ORDER_NAME;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerTrainingService trainerTrainingService;
    private final UserTrainerService userTrainerService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    public TrainerController(TrainerService trainerService, TrainerTrainingService trainerTrainingService, UserTrainerService userTrainerService, UserService userService, ModelMapper modelMapper) {
        this.trainerService = trainerService;
        this.trainerTrainingService = trainerTrainingService;
        this.userTrainerService = userTrainerService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerViewModel> getTrainerById(@PathVariable("trainerId") Long trainerId) {
        TrainerServiceModel trainer = this.trainerService.getTrainerById(trainerId);
        return ResponseEntity.ok(this.modelMapper.map(trainer, TrainerViewModel.class));
    }

    @GetMapping(value = "/{trainerId}/users", params = {"page", "size", "sortBy", "orderBy"})
    public ResponseEntity<EntityModel<TrainersViewModel>> getUserTrainersById(@PathVariable("trainerId") Long trainerId,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("size") Integer size,
                                                                              @RequestParam("sortBy") String sortBy,
                                                                              @RequestParam("orderBy") String orderBy
    ) {
        TrainersViewModel trainersViewModel = new TrainersViewModel();
        trainersViewModel.setUsers(
                this.userTrainerService.getUsersPageForTrainerId(trainerId, page, size, sortBy, orderBy)
                        .stream()
                        .map(el -> this.modelMapper.map(el, UserTrainerInfoViewModel.class))
                        .collect(Collectors.toList())
        );
        Integer totalPages = this.userTrainerService.getTotalUserPagesCountForTrainerId(trainerId, size);
        trainersViewModel.setTotalPages(totalPages);
        EntityModel<TrainersViewModel> model = EntityModel.of(trainersViewModel);
        if (!trainersViewModel.getUsers().isEmpty()) {
            model.add(getUsersLinks(trainerId, page, size, totalPages, sortBy, orderBy));
        }
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{trainerId}/users/{userId}")
    public ResponseEntity<UserProfileViewModel> getUserByIdFromTrainerUsers(@PathVariable("trainerId") Long trainerId,
                                                                            @PathVariable("userId") Long userId) {
        try {
            UserProfileServiceModel users = this.userTrainerService.getUserByIdForTrainer(trainerId, userId);
            return ResponseEntity.ok(this.modelMapper.map(users, UserProfileViewModel.class));
        } catch (UserNotFoundException ex) {
            return ResponseEntity.ok(this.modelMapper.map(this.catchException(userId), UserProfileViewModel.class));
        }
    }

    @GetMapping("/{trainerId}/usersRequests")
    public ResponseEntity<List<RequestViewModel>> getUserRequestsByTrainersId(@PathVariable("trainerId") Long trainerId) {
        List<RequestServiceModel> usersRequests = this.userTrainerService.getAllRequestsForTrainer(trainerId);
        return ResponseEntity.ok(usersRequests
                .stream()
                .map(el -> this.modelMapper.map(el, RequestViewModel.class))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{trainerId}/readyTrainings")
    public ResponseEntity<List<TrainingBasicInfoViewModel>> getTrainerReadyTrainingsTrainersId(@PathVariable("trainerId") Long trainerId) {
        List<TrainingBasicInfoServiceModel> usersRequests = this.trainerTrainingService.getReadyTrainings(trainerId);
        return ResponseEntity.ok(usersRequests
                .stream()
                .map(el -> this.modelMapper.map(el, TrainingBasicInfoViewModel.class))
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{trainerId}/readyTrainings/{trainingId}")
    public ResponseEntity<TrainingFullInfoViewModel> getTrainerReadyTrainingFullInfoTrainersId(@PathVariable("trainerId") Long trainerId,
                                                                                               @PathVariable("trainingId") Long trainingId) {
        TrainingFullInfoServiceModel training = this.trainerTrainingService.getReadyTrainingById(trainerId, trainingId);
        return ResponseEntity.ok(this.modelMapper.map(training, TrainingFullInfoViewModel.class));
    }

    @PutMapping("/{trainerId}/readyTrainings/{trainingId}")
    public ResponseEntity<ReadyTrainingViewModel> editTrainingById(@Valid @RequestBody TrainingEditBindingModel trainingEditBindingModel,
                                                                   @PathVariable("trainerId") Long trainerId,
                                                                   @PathVariable("trainingId") Long trainingId) {
        ReadyTrainingServiceModel editedTraining = this.trainerTrainingService.editByTrainerAndTrainingById(trainerId, trainingId, trainingEditBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(editedTraining, ReadyTrainingViewModel.class));
    }

    @PostMapping("/{trainerId}/readyTrainings")
    public ResponseEntity<ReadyTrainingViewModel> addReadyTrainingToUser(@Valid @RequestBody TrainingAddBindingModel trainingAddBindingModel,
                                                                         @PathVariable("trainerId") Long trainerId) {
        ReadyTrainingServiceModel newTraining = this.trainerService.addReadyTraining(trainerId, trainingAddBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(newTraining, ReadyTrainingViewModel.class));
    }

    @PostMapping("/{trainerId}/users/{userId}/readyTrainings/{trainingId}")
    public ResponseEntity<TrainingViewModel> addReadyTrainingToUser(@Valid @RequestBody ReadyTrainingStartBindingModel startDate, @PathVariable("trainerId") Long trainerId,
                                                                    @PathVariable("userId") Long userId,
                                                                    @PathVariable("trainingId") Long trainingId) throws NoUserFoundForTrainerException {

        TrainingServiceModel newTraining = this.trainerService.addReadyTrainingToUser(trainerId, userId, trainingId, startDate);
        return ResponseEntity.ok(this.modelMapper.map(newTraining, TrainingViewModel.class));
    }

    @PostMapping("/{trainerId}/trainings/{userId}/add")
    public ResponseEntity<TrainingViewModel> addTrainingToUser(@Valid @RequestBody TrainingAddBindingModel trainingAddBindingModel, @PathVariable("trainerId") Long trainerId,
                                                               @PathVariable("userId") Long userId) throws NoUserFoundForTrainerException {

        TrainingServiceModel newTraining = this.trainerService.addTrainingToUser(trainerId, userId, trainingAddBindingModel);

        return ResponseEntity.ok(this.modelMapper.map(newTraining, TrainingViewModel.class));
    }

    @PutMapping("/{trainerId}/trainings/{userId}/edit/{trainingId}")
    public ResponseEntity<TrainingViewModel> editTrainingToUser(@Valid @RequestBody TrainingEditBindingModel trainingEditBindingModel, @PathVariable("trainerId") Long trainerId,
                                                                @PathVariable("userId") Long userId,
                                                                @PathVariable("trainingId") Long trainingId) throws NoUserFoundForTrainerException {

        TrainingServiceModel newTraining = this.trainerService.editTrainingToUser(trainerId, userId, trainingId, trainingEditBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(newTraining, TrainingViewModel.class));
    }

    @GetMapping("/{trainerId}/users/{userId}/trainings/{trainingId}/withDayLinks")
    public ResponseEntity<EntityModel<TrainingWithLinksViewModel>> getTrainingToUser(@PathVariable("userId") Long userId,
                                                                                     @PathVariable("trainingId") Long trainingId,
                                                                                     @PathVariable("trainerId") Long trainerId) throws NoUserFoundForTrainerException {
        TrainingWithLinksServiceModel trainingService = this.trainerService.getTrainingWithDayLinks(trainerId, userId, trainingId);
        TrainingWithLinksViewModel training = new TrainingWithLinksViewModel();
        List<EntityModel<DayIdViewModel>> days = trainingService.getDays()
                .stream()
                .map(ex -> this.modelMapper.map(ex, DayIdViewModel.class))
                .map(el -> getDayLinks(userId, trainingId, el))
                .collect(Collectors.toList());
        training.setTraining(this.modelMapper.map(trainingService.getTraining(), TrainingViewModel.class));
        training.setDays(days);

        return ResponseEntity.ok(EntityModel.of(training));
    }

    @GetMapping("/{trainerId}/users/{userId}/trainings/{trainingId}/fullInfo")
    public ResponseEntity<TrainingFullInfoViewModel> getFullTrainingToUser(@PathVariable("userId") Long userId,
                                                                           @PathVariable("trainingId") Long trainingId,
                                                                           @PathVariable("trainerId") Long trainerId) throws NoUserFoundForTrainerException {
        TrainingFullInfoServiceModel trainingService = this.trainerService.getFullTraining(trainerId, userId, trainingId);
        TrainingFullInfoViewModel training = new TrainingFullInfoViewModel();
        List<DayFullInfoViewModel> days = trainingService.getDays()
                .stream()
                .map(ex -> this.modelMapper.map(ex, DayFullInfoViewModel.class))
                .collect(Collectors.toList());
        training.setTraining(this.modelMapper.map(trainingService.getTraining(), TrainingViewModel.class));
        training.setDays(days);

        return ResponseEntity.ok(training);
    }

    @PostMapping("/{trainerId}/request")
    public ResponseEntity<HttpStatus> addRequestToTrainer(@Valid @RequestBody RequestBindingModel requestDto, @PathVariable("trainerId") Long trainerId) {
        this.userTrainerService.requestManager(requestDto, trainerId, requestDto.getReceiverId(), true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{trainerId}/request")
    public ResponseEntity<HttpStatus> addUserTrainersId(@Valid @RequestBody RequestBindingModel requestDto, @PathVariable("trainerId") Long trainerId) {
        this.userTrainerService.requestManager(requestDto, trainerId, requestDto.getReceiverId(), false);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<TrainerFullInfoViewModel> getTrainerInfoByUsername(@PathVariable("username") String username) {
        TrainerFullInfoServiceModel user = this.trainerService.getTrainerInfoByUsername(username);
        return ResponseEntity.ok(this.modelMapper.map(user, TrainerFullInfoViewModel.class));
    }

    @PostMapping("/getSearchSuggestions")
    public ResponseEntity<List<UserBasicPicViewModel>> getUsersStartingWithUsername(
            @Valid @RequestBody SearchSuggestionBindingModel searchSuggestionBindingModel
    ) {
        List<UserBasicPicServiceModel> user = this.trainerService.getSuggestionsByUsername(searchSuggestionBindingModel);
        return ResponseEntity.ok(
                user
                        .stream()
                        .map(el -> this.modelMapper.map(el, UserBasicPicViewModel.class))
                        .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public UserProfileViewModel catchException(Long id) {
        return this.modelMapper.map(
                this.userService.getUserById(id), UserProfileViewModel.class
        );
    }

    @ExceptionHandler(value = NoUserFoundForTrainerException.class)
    public ResponseEntity<ErrorResponse> catchException(NoUserFoundForTrainerException e) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(),
                Objects.requireNonNull(HttpStatus.resolve(apiError.getStatus())));
    }

    private EntityModel<DayIdViewModel> getDayLinks(Long userId, Long trainingId, DayIdViewModel day) {
        Link workoutsLink = linkTo(methodOn(DayController.class)
                .getFullDayById(userId, trainingId, day.getId()))
                .withRel("workouts");
        return EntityModel.of(day).add(List.of(workoutsLink).toArray(new Link[0]));
    }

    private Link[] getUsersLinks(Long trainerId, Integer page, Integer size, Integer totalPages, String sortBy, String orderBy) {
        Link self = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, page, size, sortBy, orderBy))
                .withSelfRel();
        Link next = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, page + 1, size, sortBy, orderBy))
                .withRel("next");
        Link prev = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, page - 1, size, sortBy, orderBy))
                .withRel("prev");
        Link first = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, sortBy, orderBy))
                .withRel("first");
        Link last = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, totalPages, size, sortBy, orderBy))
                .withRel("last");
        Link sortByAgeAsc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.age.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByAgeAsc");
        Link sortByAgeDesc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.age.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByAgeDesc");
        Link sortByHeightAsc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.height.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByHeightAsc");
        Link sortByHeightDesc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.height.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByHeightDesc");
        Link sortByTotalTrainingsAsc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.totalTrainings.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByTotalTrainingsAsc");
        Link sortByTotalTrainingsDesc = linkTo(methodOn(TrainerController.class)
                .getUserTrainersById(trainerId, 0, size, UserSortByEnum.totalTrainings.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByTotalTrainingsDesc");
        if (totalPages == 0) {
            return List.of(
                    self, sortByAgeAsc, sortByAgeDesc, sortByHeightAsc, sortByHeightDesc, sortByTotalTrainingsAsc, sortByTotalTrainingsDesc
            ).toArray(new Link[0]);
        }
        if (page == 0) {
            return List.of(
                    self, next, first, last, sortByAgeAsc, sortByAgeDesc, sortByHeightAsc, sortByHeightDesc, sortByTotalTrainingsAsc, sortByTotalTrainingsDesc
            ).toArray(new Link[0]);
        } else if (page.equals(totalPages)) {
            return List.of(
                    self, prev, first, last, sortByAgeAsc, sortByAgeDesc, sortByHeightAsc, sortByHeightDesc, sortByTotalTrainingsAsc, sortByTotalTrainingsDesc
            ).toArray(new Link[0]);
        } else {
            return List.of(
                    self, prev, next, first, last, sortByAgeAsc, sortByAgeDesc, sortByHeightAsc, sortByHeightDesc, sortByTotalTrainingsAsc, sortByTotalTrainingsDesc
            ).toArray(new Link[0]);
        }
    }
}
