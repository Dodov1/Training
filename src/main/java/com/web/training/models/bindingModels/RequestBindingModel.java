package com.web.training.models.bindingModels;

import com.web.training.models.enums.RelationStatus;

import javax.validation.constraints.NotNull;

public class RequestBindingModel {

    @NotNull
    private Long receiverId;
    @NotNull
    private RelationStatus statusType;

    public RelationStatus getStatusType() {
        return this.statusType;
    }

    public RequestBindingModel setStatusType(RelationStatus statusType) {
        this.statusType = statusType;
        return this;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public RequestBindingModel setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }
}
