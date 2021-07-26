package com.south.system.model.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionRequest {

    private String subjectId;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
}
