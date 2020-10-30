package hr.fer.oprpp1.hw02.prob1.demo;

import hr.fer.oprpp1.hw02.prob1.Lexer;

public class LexerDemo {

    public static void main(String[] args) {
        //String ulaz = "Ovo je 123ica, ab57.\nKraj";
        //String ulaz = "\\1\\2 ab\\\\\\2c\\3\\4d";
        String ulaz = "a3a#a3a#a3a";
        Lexer lexer = new Lexer(ulaz);

        while (true) {
            lexer.nextToken();
            System.out.println(lexer.getToken());
        }
    }

}
