package com.web.training.models.viewModels;

import java.util.List;

public class TrainersViewModel {

    private List<UserTrainerInfoViewModel> users;
    private Integer totalPages;

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public TrainersViewModel setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public List<UserTrainerInfoViewModel> getUsers() {
        return this.users;
    }

    public TrainersViewModel setUsers(List<UserTrainerInfoViewModel> users) {
        this.users = users;
        return this;
    }
}
