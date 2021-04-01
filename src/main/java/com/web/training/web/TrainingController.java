package com.web.training.web;

import com.web.training.models.bindingModels.SearchSuggestionBindingModel;
import com.web.training.models.bindingModels.TrainingAddBindingModel;
import com.web.training.models.bindingModels.TrainingEditBindingModel;
import com.web.training.models.enums.TrainingSortByEnum;
import com.web.training.models.serviceModels.*;
import com.web.training.models.viewModels.*;
import com.web.training.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.web.training.config.appConstants.AppConstants.ASCENDING_ORDER_NAME;
import static com.web.training.config.appConstants.AppConstants.DESCENDING_ORDER_NAME;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/**/users/{userId}/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final ModelMapper modelMapper;

    public TrainingController(TrainingService trainingService, ModelMapper modelMapper) {
        this.trainingService = trainingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainingViewModel> addTraining(@Valid @RequestBody TrainingAddBindingModel trainingAddBindingModel, @PathVariable("userId") Long ownerId) {
        TrainingServiceModel newTraining = this.trainingService.addNewTraining(ownerId, trainingAddBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(newTraining, TrainingViewModel.class));
    }

    @GetMapping(value = "/", params = {"page", "size", "sortBy", "orderBy"})
    public ResponseEntity<EntityModel<TrainingsViewModel>> getAllUserTrainingsById(@PathVariable("userId") Long userId,
                                                                                   @RequestParam("page") Integer page,
                                                                                   @RequestParam("size") Integer size,
                                                                                   @RequestParam("sortBy") String sortBy,
                                                                                   @RequestParam("orderBy") String orderBy
    ) {
        TrainingsServiceModel trainings = this.trainingService.getAllTrainingsForUserId(userId, page, size, sortBy, orderBy);
        TrainingsViewModel trainingsView = this.modelMapper.map(trainings, TrainingsViewModel.class);
        if (trainings.getTrainings().isEmpty()) {
            return ResponseEntity.ok(EntityModel.of(trainingsView));
        } else {
            return ResponseEntity.ok(EntityModel.of(trainingsView,
                    getTrainingsLinks(userId, page, size, trainings.getTotalPages(), sortBy, orderBy)));
        }
    }

    @GetMapping("/{trainingId}/fullInfo")
    public ResponseEntity<TrainingFullInfoViewModel> getFullTrainingDaysById(@PathVariable("userId") Long userId, @PathVariable("trainingId") Long trainingId) {
        TrainingFullInfoServiceModel trainingService = this.trainingService.getFullTraining(userId, trainingId);

        TrainingFullInfoViewModel training = new TrainingFullInfoViewModel();
        List<DayFullInfoViewModel> days = trainingService.getDays()
                .stream()
                .map(ex -> this.modelMapper.map(ex, DayFullInfoViewModel.class))
                .collect(Collectors.toList());
        training.setTraining(this.modelMapper.map(trainingService.getTraining(), TrainingViewModel.class));
        training.setDays(days);

        return ResponseEntity.ok(training);
    }

    @GetMapping("/{trainingId}/withDayLinks")
    public ResponseEntity<EntityModel<TrainingWithLinksViewModel>> getTrainingWithDayLinksById(@PathVariable("userId") Long userId,
                                                                                               @PathVariable("trainingId") Long trainingId) {
        TrainingWithLinksServiceModel trainingService = this.trainingService.getTrainingWithDayLinks(userId, trainingId);

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

    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingViewModel> editTrainingById(@Valid @RequestBody TrainingEditBindingModel trainingEditBindingModel, @PathVariable("userId") Long ownerId, @PathVariable("trainingId") Long trainingId) {
        TrainingServiceModel editedTraining = this.trainingService.editByTrainingById(ownerId, trainingId, trainingEditBindingModel);
        return ResponseEntity.ok(this.modelMapper.map(editedTraining, TrainingViewModel.class));
    }

    @GetMapping("/statistics")
    public ResponseEntity<TrainingStatisticViewModel> getAllUserTrainingsStatisticsById(@PathVariable("userId") Long userId) {
        TrainingStatisticServiceModel users = this.trainingService.getTrainingsStatistics(userId);
        return ResponseEntity.ok(this.modelMapper.map(users, TrainingStatisticViewModel.class));
    }

    @PostMapping("/getSearchSuggestions")
    public ResponseEntity<List<TrainingViewModel>> getTrainingsStartingWithTitle(@PathVariable("userId") Long userId,
                                                                                 @Valid @RequestBody SearchSuggestionBindingModel searchSuggestionBindingModel
    ) {
        List<TrainingServiceModel> user = this.trainingService.getSuggestionsByTitle(userId, searchSuggestionBindingModel);
        return ResponseEntity.ok(
                user
                        .stream()
                        .map(el -> this.modelMapper.map(el, TrainingViewModel.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/enums")
    public ResponseEntity<TrainingEnumsViewModel> getTrainingEnums() {
        TrainingEnumsServiceModel enums = this.trainingService.getTrainingEnums();
        return ResponseEntity.ok(this.modelMapper.map(enums, TrainingEnumsViewModel.class));
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "EET")
    public void markDaysAsFinished() {
        this.trainingService.setTrainingsStatusScheduled();
    }

    private EntityModel<DayIdViewModel> getDayLinks(Long userId, Long trainingId, DayIdViewModel day) {
        Link workoutsLink = linkTo(methodOn(DayController.class)
                .getFullDayById(userId, trainingId, day.getId()))
                .withRel("workouts");
        return EntityModel.of(day).add(List.of(workoutsLink).toArray(new Link[0]));
    }

    private Link[] getTrainingsLinks(Long userId, Integer page, Integer size, Integer totalPages, String sortBy, String orderBy) {
        Link self = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, page, size, sortBy, orderBy))
                .withSelfRel();
        Link next = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, page + 1, size, sortBy, orderBy))
                .withRel("next");
        Link prev = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, page - 1, size, sortBy, orderBy))
                .withRel("prev");
        Link first = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, sortBy, orderBy))
                .withRel("first");
        Link last = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, totalPages, size, sortBy, orderBy))
                .withRel("last");
        Link sortByFromDateAsc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.fromDate.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByFromDateAsc");
        Link sortByFromDateDesc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.fromDate.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByFromDateDesc");
        Link sortByTitleAsc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.title.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByTitleAsc");
        Link sortByTitleDesc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.title.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByTitleDesc");
        Link sortByStatusTypeAsc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.statusType.name(), ASCENDING_ORDER_NAME))
                .withRel("sortByStatusTypeAsc");
        Link sortByStatusTypeDesc = linkTo(methodOn(TrainingController.class)
                .getAllUserTrainingsById(userId, 0, size, TrainingSortByEnum.statusType.name(), DESCENDING_ORDER_NAME))
                .withRel("sortByStatusTypeDesc");
        if (page == 0) {
            return List.of(
                    self, next, first, last, sortByFromDateAsc, sortByFromDateDesc, sortByTitleAsc, sortByTitleDesc, sortByStatusTypeAsc, sortByStatusTypeDesc
            ).toArray(new Link[0]);
        } else if (page.equals(totalPages)) {
            return List.of(
                    self, prev, first, last, sortByFromDateAsc, sortByFromDateDesc, sortByTitleAsc, sortByTitleDesc, sortByStatusTypeAsc, sortByStatusTypeDesc
            ).toArray(new Link[0]);
        } else {
            return List.of(
                    self, prev, next, first, last, sortByFromDateAsc, sortByFromDateDesc, sortByTitleAsc, sortByTitleDesc, sortByStatusTypeAsc, sortByStatusTypeDesc
            ).toArray(new Link[0]);
        }
    }
}
