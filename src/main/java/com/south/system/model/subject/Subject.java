package com.south.system.model.subject;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Document(collection = "subject")
public class Subject implements Serializable {

    @Id
    private String id;

    private String topic;
    private Integer voteYes;
    private Integer voteNo;
    private Integer totalVotes;

    private List<String> cpfVotes;
}

