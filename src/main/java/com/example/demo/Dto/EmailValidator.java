package com.example.demo.Dto;

import java.util.function.Predicate;

public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
//ToDo:Regex to validate email
        return true;
    }
}
