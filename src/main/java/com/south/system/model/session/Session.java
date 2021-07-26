package com.south.system.model.session;

import com.south.system.model.subject.Subject;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Document(collection = "session")
public class Session {

    @Id
    private String id;

    @DBRef
    private Subject subject;

    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
}
