package me.middleclicker;

public class test {
    public static void main(String[] args) {
        String escapeCode = "\033[38;2;27;161;147m";
        String resetCode = "\033[0m";

        System.out.println(escapeCode + "Hello, World!");
        System.out.println("Some more stuff I want to say.");
        System.out.println(resetCode);
        System.out.println("Now styling is default again.");
    }
}
