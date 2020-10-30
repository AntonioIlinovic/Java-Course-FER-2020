package hr.fer.oprpp1.custom.scripting.lexer;

public class Demo {
    public static void main(String[] args) {
        //String text = "A tag follows { {\\{${$";
        //String text = "{$= i i * @sin  \"0.000\" @decfmt $}";
        String text = """
                This is sample text.
                {$ FOR i 1 10 1 $}
                 This is {$= i $}-th time this message is generated.
                {$END$}
                {$FOR i -10 10.0 2 $}
                 sin({$=i$}^2) = {$= i i * @sin "0.000" @decfmt $}
                {$END$}""";

        SmartScriptLexer lexer = new SmartScriptLexer(text);

        while (true) {
            lexer.nextToken();
            System.out.println(lexer.getToken());
        }
    }
}
