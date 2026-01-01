package com.backend.vanat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDTO {
    private byte[] data;
    private String contentType;
}
