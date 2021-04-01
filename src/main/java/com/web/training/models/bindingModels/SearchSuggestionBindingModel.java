package com.web.training.models.bindingModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SearchSuggestionBindingModel {

    @NotNull(message = "Input cannot be null")
    private String input;
    @NotNull(message = "NotIds cannot be null")
    private List<Long> notIds;

}
