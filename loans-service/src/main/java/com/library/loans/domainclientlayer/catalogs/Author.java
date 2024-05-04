package com.library.loans.domainclientlayer.catalogs;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Author {

    private String firstName;
    private String lastName;
}
