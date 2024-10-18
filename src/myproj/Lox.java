package myproj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    // take in a script file name, then that will be opened
    // if no script then we will make a REPL in the terminal
    // where user can type code line by line.

    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // runFIle will take the file path, read it to a bytes, then call run
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    // this will read a line from terminal, then for each line it reads, it will
    // call the run function on that line to interpret it.
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            // check if CTRL + D was clicked and line
            // becomes EOF which returns NULL
            if (line == null) break;
            run(line);
            // reset so it doesn't kill their session
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);

        List<Token> tokens = scanner.scanTokens();

        // for now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // basic error handling. We will not give the user all the info and make
    // the error handling super helpful since it is a lot of string munging code.
    // error handling here instead of in scanner or other phases to make sure that
    // we separate the code generating the errors from teh code reporting them.
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }


}