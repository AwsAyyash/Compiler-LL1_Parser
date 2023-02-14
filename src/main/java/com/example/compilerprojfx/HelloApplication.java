package com.example.compilerprojfx;

import com.example.compilerprojfx.controler.LL1Parser;
import com.example.compilerprojfx.model.Lexeme;
import com.example.compilerprojfx.controler.LexicalAnalyzer;
import com.example.compilerprojfx.model.SyntaxErrorThrower;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HelloApplication extends Application {
    private File inputFile;
    private Label outputErrorLabel;

    @Override
    public void start(Stage stage) {


        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 70, 10));


        Label welcomeLabel = new Label("Welcome to LL1 parser");
        welcomeLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        welcomeLabel.setTextFill(Color.DARKGRAY);
        welcomeLabel.setPadding(new Insets(15));

        borderPane.setTop(welcomeLabel);
        BorderPane.setAlignment(welcomeLabel, Pos.TOP_CENTER);


        HBox hBoxLoadFile = new HBox(10);
        hBoxLoadFile.setPadding(new Insets(15));


        Label loadFileLabel = new Label("Choose an input file which contains the source code to be parsed by the compiler parser:");
        Button loadFileButton = new Button("Load File");
        hBoxLoadFile.getChildren().addAll(loadFileLabel, loadFileButton);


        outputErrorLabel = new Label("");

        outputErrorLabel.setTextFill(Color.RED);
        outputErrorLabel.setFont(Font.font("verdana", FontWeight.MEDIUM, FontPosture.REGULAR, 15));


        borderPane.setBottom(outputErrorLabel);

        BorderPane.setAlignment(outputErrorLabel, Pos.BOTTOM_CENTER);

        borderPane.setLeft(hBoxLoadFile);


        loadFileButton.setOnAction(actionEvent -> {

            FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showOpenDialog(stage);
            fileChooser.setTitle("Open the File");

            if (file != null) {
                this.inputFile = file;

                try {
                    String resultVal = parseTheCode();
                    outputErrorLabel.setText(resultVal);
                } catch (Exception e) {
                    outputErrorLabel.setText(e.getMessage());

                }
            }

        });


        Scene scene = new Scene(borderPane, 850, 650);
        stage.setTitle("LL1 Parser-Compiler");
        stage.setScene(scene);
        stage.show();

    }

    private String parseTheCode() throws Exception {

        String inputSourceString = readFile();
        List<Lexeme> lexemes = analyze(inputSourceString);
        LL1Parser ll1Parser = new LL1Parser();
        return ll1Parser.parse(lexemes);

    }

    private String readFile() throws Exception {

        String sourceCode = "";
        try {
            sourceCode = Files.readString(Paths.get(inputFile.getAbsolutePath()));

        } catch (Exception e) {
            outputErrorLabel.setText(e.getMessage());
            System.out.println(e);
        }
        if (sourceCode.equals(""))
            throw new Exception("Empty file! NO CODE to be parsed!");


        return sourceCode;


    }


    private static List<Lexeme> analyze(String projInputTxt) throws SyntaxErrorThrower {

        String[] lines = projInputTxt.split("\n");


        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();


        List<Lexeme> lexemes = lexicalAnalyzer.analyzeCode(lines);


        return lexemes;


    }
}