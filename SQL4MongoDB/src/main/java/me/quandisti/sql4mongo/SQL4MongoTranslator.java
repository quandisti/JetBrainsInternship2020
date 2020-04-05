package me.quandisti.sql4mongo;

import java.util.Scanner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class SQL4MongoTranslator {
    public static void main(String[] args){
        String query = "";
        if (args.length < 1) {
            System.out.println("Hello! Type SQL request to convert and send EOF (usually by pressing Ctrl-D)");
            Scanner inp = new Scanner(System.in);
            System.out.println(translateQuery(inp.nextLine()));
        }
        else
            System.out.println(translateQuery(String.join(" ", args)));
    }

    static String translateQuery(String query){
        SQL4MongoLexer lexer = new SQL4MongoLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQL4MongoParser parser = new SQL4MongoParser(tokens);

        ParseTree tree = parser.query();
        ParseTreeWalker walker = new ParseTreeWalker();

        SQL4MongoQueryListener listener = new SQL4MongoQueryListener();

        walker.walk(listener, tree);

        return listener.queryResult;
    }
}
