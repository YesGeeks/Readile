package com.readile.readile.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultBook {
    private String name;
    private String coverURL;
    private Integer length;
    private List<String> authorNames;
}