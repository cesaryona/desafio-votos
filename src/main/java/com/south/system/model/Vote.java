package com.south.system.model;

import com.south.system.model.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vote {

    private String sessionId;
    private VoteType vote;
    private String cpf;
}
