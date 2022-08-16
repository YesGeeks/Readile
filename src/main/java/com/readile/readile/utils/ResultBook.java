package com.readile.readile.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultBook {
    private String title;
    private String cover_i;
    private Integer number_of_pages_median;
    private List<String> author_name;
}