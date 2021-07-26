package com.south.system.model;

import com.south.system.model.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vote {

    @NotNull
    @NotEmpty
    private String sessionId;

    @NotNull
    @NotEmpty
    private VoteType vote;

    @NotNull
    @NotEmpty
    private String cpf;
}
