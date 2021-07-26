package com.south.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
}