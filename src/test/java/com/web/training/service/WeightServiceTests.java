package com.web.training.service;

import com.web.training.models.exceptionModels.NotFoundException;
import com.web.training.helpers.WeightCalculator;
import com.web.training.helpers.impl.WeightCalculatorImpl;
import com.web.training.models.bindingModels.WeightBindingModel;
import com.web.training.models.entities.Trainer;
import com.web.training.models.entities.User;
import com.web.training.models.entities.Weight;
import com.web.training.models.enums.TrainerType;
import com.web.training.models.serviceModels.WeightServiceModel;
import com.web.training.models.serviceModels.WeightStatisticServiceModel;
import com.web.training.repositories.UserRepository;
import com.web.training.repositories.WeightRepository;
import com.web.training.service.impl.WeightServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.web.training.config.appConstants.AppConstants.WEIGHT_STATISTIC_VIEW_NUMBER;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WeightServiceTests {

    @Mock
    private UserRepository userRepository;
    private WeightService weightService;
    @Mock
    private WeightRepository weightRepository;
    private final WeightCalculator weightCalculator = new WeightCalculatorImpl();

    private final ModelMapper modelMapper = new ModelMapper();

    private static final Long CORRECT_USER_ID = 1L;
    private static final Long CORRECT_WEIGHT_ID = 1L;
    private static final Long WRONG_USER_ID = 2L;

    private User testUser;
    private Weight testWeight;
    private Weight testWeight2;

    @BeforeEach
    public void init() {
        Trainer trainer = new Trainer();
        trainer.setType(TrainerType.Group);
        trainer.setId(1L);
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
            setPassword("asdfg");
            setHeight(184);
        }};
        this.testWeight = new Weight() {{
            setUser(testUser);
            setId(CORRECT_WEIGHT_ID);
            setWeight(10.0);
            setDate(LocalDateTime.MIN);
            setBmi(12.5);
        }};
        this.testWeight2 = new Weight() {{
            setUser(testUser);
            setId(CORRECT_WEIGHT_ID);
            setWeight(9.0);
            setDate(LocalDateTime.MAX);
            setBmi(12.5);
        }};
        this.weightService = new WeightServiceImpl(this.weightRepository, this.userRepository, this.weightCalculator, this.modelMapper);
    }

    /**
     * WeightService - getWeightStatisticForUserId(...) TESTS
     */

    @Test
    void getWeightStatisticByUserIdWhichIsNullShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.weightService.getWeightStatisticForUserId(null));
    }

    @Test
    void getWeightStatisticMaxWeightByUserIdShouldReturnCorrect() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testWeight.getWeight() + WEIGHT_STATISTIC_VIEW_NUMBER, actual.getMaxWeight(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticMinWeightByUserIdShouldReturnCorrect() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(this.testWeight2.getWeight() - WEIGHT_STATISTIC_VIEW_NUMBER, actual.getMinWeight(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticDataByUserIdShouldReturnCorrect() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        List<Double> actualData = new ArrayList<>();
        actualData.add(this.testWeight.getWeight());
        actualData.add(this.testWeight2.getWeight());
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(actualData, actual.getData(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticLabelsByUserIdShouldReturnCorrect() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        List<String> actualData = new ArrayList<>();
        actualData.add(this.testWeight.getDate().toLocalDate().toString());
        actualData.add(this.testWeight2.getDate().toLocalDate().toString());
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(actualData, actual.getLabels(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticLabelsByUserIdShouldReturnWrong() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight2);
        list.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        List<String> actualData = new ArrayList<>();
        actualData.add(this.testWeight.getDate().toLocalDate().toString());
        actualData.add(this.testWeight2.getDate().toLocalDate().toString());
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNotEquals(actualData, actual.getLabels(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticDataByUserIdShouldReturnWrong() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight2);
        list.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        List<Double> actualData = new ArrayList<>();
        actualData.add(this.testWeight.getWeight());
        actualData.add(this.testWeight2.getWeight());
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNotEquals(actualData, actual.getData(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticMaxWeightByUserIdShouldReturnWrong() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNotEquals(this.testWeight.getWeight(), actual.getMaxWeight(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticMinWeightByUserIdShouldReturnWrong() {
        List<Weight> list = new ArrayList<>();
        list.add(this.testWeight);
        list.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightAsc(CORRECT_USER_ID))
                .thenReturn(this.testWeight2);
        Mockito.when(this.weightRepository
                .getTop1ByUser_IdOrderByWeightDesc(CORRECT_USER_ID))
                .thenReturn(this.testWeight);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNotEquals(this.testWeight2.getWeight(), actual.getMinWeight(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticByWrongUserIdShouldTrowException() {
        assertThrows(NotFoundException.class, () -> this.weightService
                .getWeightStatisticForUserId(WRONG_USER_ID));
    }

    @Test
    void getWeightStatisticByCorrectUserIdShouldReturnNoLabels() {
        List<Weight> list = new ArrayList<>();
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(new ArrayList<String>(), actual.getLabels(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticByCorrectUserIdShouldReturnNoData() {
        List<Weight> list = new ArrayList<>();
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertEquals(new ArrayList<Double>(), actual.getData(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticByCorrectUserIdShouldReturnNoMinValue() {
        List<Weight> list = new ArrayList<>();
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNull(actual.getMinWeight(), "Weight  is not correct");
    }

    @Test
    void getWeightStatisticByCorrectUserIdShouldReturnNoMaxValue() {
        List<Weight> list = new ArrayList<>();
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .findAllByUserId(CORRECT_USER_ID))
                .thenReturn(list);
        WeightStatisticServiceModel actual = this.weightService.getWeightStatisticForUserId(CORRECT_USER_ID);
        Assertions.assertNull(actual.getMaxWeight(), "Weight  is not correct");
    }


    /**
     * WeightService - addWeightToUser(...) TESTS
     */

    @Test
    void addWeightsByUserIdShouldReturnCorrectBmi() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService
                .addWeightToUser(CORRECT_USER_ID, this.modelMapper.map(this.testWeight, WeightBindingModel.class));
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getBmi(), actual.getBmi(), "Weight  is not correct");
    }

    @Test
    void addWeightsByUserIdShouldReturnCorrectChange() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService
                .addWeightToUser(CORRECT_USER_ID, this.modelMapper.map(this.testWeight, WeightBindingModel.class));
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getChange(), actual.getChange(), "Weight  is not correct");
    }

    @Test
    void addWeightsByUserIdShouldReturnCorrectWeight() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService
                .addWeightToUser(CORRECT_USER_ID, this.modelMapper.map(this.testWeight, WeightBindingModel.class));
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getWeight(), actual.getWeight(), "Weight  is not correct");
    }

    @Test
    void addWeightsByUserIdShouldReturnCorrectDate() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService
                .addWeightToUser(CORRECT_USER_ID, this.modelMapper.map(this.testWeight, WeightBindingModel.class));
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getDate(), actual.getDate(), "Weight  is not correct");
    }

    @Test
    void addWeightsByWrongUserIdShouldTrowException() {
        assertThrows(NotFoundException.class, () -> this.weightService
                .addWeightToUser(WRONG_USER_ID, this.modelMapper.map(this.testWeight, WeightBindingModel.class)));
    }

    /**
     * WeightService - getCurrentWeightByUserId(...) TESTS
     */
    @Test
    void getCurrentWeightsByUserIdShouldReturnCorrectBmi() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getBmi(), actual.getBmi(), "Weight  is not correct");
    }

    @Test
    void getCurrentWeightsByUserIdShouldReturnCorrectChange() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getChange(), actual.getChange(), "Weight  is not correct");
    }

    @Test
    void getCurrentWeightsByUserIdShouldReturnCorrectWeight() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getWeight(), actual.getWeight(), "Weight  is not correct");
    }

    @Test
    void getCurrentWeightsByUserIdShouldReturnCorrectWeightId() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        expected.setId(1L);
        Assertions.assertEquals(expected.getId(), actual.getId(), "Weight  is not correct");
    }

    @Test
    void getCurrentWeightsByUserIdShouldReturnCorrectDate() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getDate(), actual.getDate(), "Weight  is not correct");
    }

    @Test
    void getCurrentWeightsByUserIdShouldThrowNoWeightException() {
        List<Weight> weights = new ArrayList<>();
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        assertThrows(NotFoundException.class, () -> this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID));
    }

    @Test
    void getCurrentWeightsByWrongUserIdShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.weightService.getCurrentWeightByUserId(WRONG_USER_ID));
    }

    @Test
    void getCurrent2WeightsByUserIdShouldReturnCorrectDate() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        weights.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("--");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getDate(), actual.getDate(), "Weight  is not correct");
    }

    @Test
    void getCurrent2WeightsByUserIdShouldReturnCorrectChangePlus1Kg() {
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight);
        weights.add(this.testWeight2);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("+1 kg");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getChange(), actual.getChange(), "Weight  is not correct");
    }

    @Test
    void getCurrent2WeightsByUserIdWhichIsNullShouldThrowException() {
        assertThrows(NotFoundException.class, () -> this.weightService.getCurrentWeightByUserId(null));
    }

    @Test
    void getCurrent2WeightsByUserIdShouldReturnCorrectChangeMinus1Kg() {
        this.testWeight.setDate(LocalDateTime.MAX);
        List<Weight> weights = new ArrayList<>();
        weights.add(this.testWeight2);
        weights.add(this.testWeight);
        Mockito.when(this.userRepository
                .findById(CORRECT_USER_ID))
                .thenReturn(Optional.of(this.testUser));
        Mockito.when(this.weightRepository
                .getTop2ByUserIdOrderByDateDesc(this.testUser.getId()))
                .thenReturn(weights);
        WeightServiceModel actual = this.weightService.getCurrentWeightByUserId(CORRECT_USER_ID);
        WeightServiceModel expected = new WeightServiceModel();
        expected.setChange("-1 kg");
        expected.setDate(LocalDate.MIN);
        expected.setWeight(this.testWeight.getWeight());
        expected.setBmi(3.0);
        Assertions.assertEquals(expected.getChange(), actual.getChange(), "Weight  is not correct");
    }
}
