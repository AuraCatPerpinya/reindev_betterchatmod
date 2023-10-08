package com.auracat.betterchatmod.client.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextSeparators {
    public List<Character> include = Arrays.asList(' ');
    public List<Character> exclude = Arrays.asList(',', '.');

    public List<Character> getMergedList() {
        List<Character> list = new ArrayList<>();

        list.addAll(include);
        list.addAll(exclude);

        return list;
    }
}
