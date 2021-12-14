package com.company.webstore.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Builder
@Getter
@Setter
public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private LocalDate publishDate;
    private String authorName;
}
