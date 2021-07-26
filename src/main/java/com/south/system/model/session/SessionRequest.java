package com.south.system.model.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionRequest {

    @NotNull
    @NotEmpty
    private String subjectId;

    @NotNull
    @NotEmpty
    private LocalDateTime dateBegin;

    private LocalDateTime dateEnd;
}
