package com.south.system.model.associate;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Document(collection = "associate")
public class Associate implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private String cpf;

    private String status;

}