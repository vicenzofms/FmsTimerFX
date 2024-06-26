package program;

// region IMPORTS
import classes.Themes;
import classes.solve;
import classes.solveStates;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
// endregion

public class session implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    //region ATTRIBUTES
    private session me = this;
    private int offsetY = 15;
    private boolean pressedKey;
    private transient Pane commentPane;
    private transient Pane timingPane;
    private transient Pane listPane;
    public String nameSession = "";
    private int lenghtScramble = 25;
    private String possibleMoves[] = {"Rx","Lx","Uy","Dy","Fz","Bz"};
    private String directions[] = {" ", "' ", "2 "};
    private Random r = new Random(System.currentTimeMillis());
    private String actualScramble = "";
    private transient Label lblScramble;
    private transient Label lblTime;
    private transient Button btnFocus;
    private int time[] = {0,0,0,0};
    private int ID;
    private transient Label txt2, txt3, txt4;
    public ArrayList<solve> allSolvesList = new ArrayList<solve>();
    public transient ObservableList<solve> allS = FXCollections.observableArrayList(allSolvesList);
    private transient ListView<solve> listTimes;
    private String commentInSession;
    private transient RadioButton rdbtnInspection;
    private boolean inspectionStarted = false;
    private boolean timerStarted = false;
    private solve actualSolve;
    private transient Stage win;
    private transient Button btnNewScramble;
    private transient Label txt5;
    // endregion+


    public session(int id){
        ID = id;
    }

    private String generateMove(String m1, String m2) {
        String move = possibleMoves[r.nextInt(possibleMoves.length)];

        if (m2 == move || sameAxis(m1,m2,move)) {
            return generateMove(m1, m2);
        }

        return move;
    }
    private void generateNewScramble() {

        String scramble = "";
        String move1 = "  ";
        String move2 = "  ";

        String direction = "";

        for (int i = 0; i < lenghtScramble; i++) {
            String currentMove = generateMove(move1, move2);
            direction = directions[r.nextInt(directions.length)];
            scramble += currentMove.charAt(0) + direction;
            move1 = move2;
            move2 = currentMove;
        }

        actualScramble = scramble;
        lblScramble.setText("Scramble: " + actualScramble);
        //System.out.println(scramble);
    }

    private boolean sameAxis(String m1, String m2, String m3) {
        if (m2.charAt(1) == m1.charAt(1) && m2.charAt(1) == m3.charAt(1)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings({ "unused" })
    public void calculateAverages() {
        if (allSolvesList.size() >= 3 && (allSolvesList.get(allSolvesList.size()-1).getStateOfSolve() != solveStates.DNF )
                && (allSolvesList.get(allSolvesList.size()-2).getStateOfSolve() != solveStates.DNF )
                && (allSolvesList.get(allSolvesList.size()-3).getStateOfSolve() != solveStates.DNF )) {
            int[] t1 = allSolvesList.get(allSolvesList.size()-1).getTime();
            int[] t2 = allSolvesList.get(allSolvesList.size()-2).getTime();
            int[] t3 = allSolvesList.get(allSolvesList.size()-3).getTime();
            float[] tS = new float[3];
            tS[0] = (t1[0]*3600) + (t1[1]*60) + (t1[2]) + (t1[3]/100.0f);
            tS[1] = (t2[0]*3600) + (t2[1]*60) + (t2[2]) + (t2[3]/100.0f);
            tS[2] = (t3[0]*3600) + (t3[1]*60) + (t3[2]) + (t3[3]/100.0f);

            float mo3 = (tS[0] + tS[1] + tS[2])/3;
            int milliseconds = (int)((mo3 - Math.floor(mo3))*100);
            int seconds = (int)(Math.floor(mo3));
            int minutes = 0;
            int hours = 0;
            while(seconds > 60){
                minutes++;
                seconds-=60;
            }
            while(minutes > 60){
                hours++;
                minutes-=60;
            }
            milliseconds++;
            if (hours == 0 && minutes == 0) {
                txt4.setText("mo3: " + seconds + "." + String.format("%02d", milliseconds));
            } else if (hours == 0) {
                txt4.setText("mo3: " + minutes+ ":" + seconds + "." + String.format("%02d", milliseconds));
            } else {
                txt4.setText("mo3: " + hours + ":" + minutes + ":" + seconds + "." + String.format("%02d", milliseconds));
            }
        } else if (allSolvesList.size() >= 3 && ((allSolvesList.get(allSolvesList.size()-1).getStateOfSolve() == solveStates.DNF )
                || (allSolvesList.get(allSolvesList.size()-2).getStateOfSolve() == solveStates.DNF )
                || (allSolvesList.get(allSolvesList.size()-3).getStateOfSolve() == solveStates.DNF ))){
            txt4.setText("mo3: DNF");
        } else {

            txt4.setText("mo3: ---");
        }


        if (allSolvesList.size() >= 5) {
            int dnfs = 0;
            int dnf1 = 0;
            for (int j = 1; j <= 5; j++) {
                if (allSolvesList.get(allSolvesList.size()-j).getStateOfSolve() == solveStates.DNF) {
                    dnfs++;
                    dnf1 = j-1;
                }
            }
            //System.out.println(dnf1);

            int[] t1 = allSolvesList.get(allSolvesList.size()-1).getTime();
            int[] t2 = allSolvesList.get(allSolvesList.size()-2).getTime();
            int[] t3 = allSolvesList.get(allSolvesList.size()-3).getTime();
            int[] t4 = allSolvesList.get(allSolvesList.size()-4).getTime();
            int[] t5 = allSolvesList.get(allSolvesList.size()-5).getTime();
            float[] tS = new float[5];
            tS[0] = (t1[0]*3600) + (t1[1]*60) + (t1[2]) + (t1[3]/100.0f);
            tS[1] = (t2[0]*3600) + (t2[1]*60) + (t2[2]) + (t2[3]/100.0f);
            tS[2] = (t3[0]*3600) + (t3[1]*60) + (t3[2]) + (t3[3]/100.0f);
            tS[3] = (t4[0]*3600) + (t4[1]*60) + (t4[2]) + (t4[3]/100.0f);
            tS[4] = (t5[0]*3600) + (t5[1]*60) + (t5[2]) + (t5[3]/100.0f);

            if (dnfs < 2) {
                float fastest = tS[0];
                float slowest = 0;

                for (int i = 0; i < 5; i++){
                    if (tS[i] < fastest){
                        fastest = tS[i];
                    }
                    if (tS[i] > slowest){
                        slowest = tS[i];
                    }
                }
                if (dnfs == 1) {
                    slowest = tS[dnf1];
                }
                float ao5 = ((tS[0] + tS[1] + tS[2] + tS[3] + tS[4])-(fastest)-(slowest))/3;
                int milliseconds = (int)((ao5 - Math.floor(ao5))*100);
                int seconds = (int)(Math.floor(ao5));
                int minutes = 0;
                int hours = 0;
                while(seconds > 60){
                    minutes++;
                    seconds-=60;
                }
                while(minutes > 60){
                    hours++;
                    minutes-=60;
                }
                milliseconds++;
                if (hours == 0 && minutes == 0) {
                    txt3.setText("ao5: " + seconds + "." + String.format("%02d", milliseconds));
                } else if (hours == 0) {
                    txt3.setText("ao5: " + minutes+ ":" + seconds + "." + String.format("%02d", milliseconds));
                } else {
                    txt3.setText("ao5: " + hours + ":" + minutes + ":" + seconds + "." + String.format("%02d", milliseconds));
                }
            } else {
                txt3.setText("ao5: DNF");
            }
        } else {
            txt3.setText("ao5: ---");
        }
        if (allSolvesList.size() >= 12) {
            int dnfs = 0;
            int dnf1 = 0;
            for (int j = 1; j <= 12; j++) {
                if (allSolvesList.get(allSolvesList.size()-j).getStateOfSolve() == solveStates.DNF) {
                    dnfs++;
                    dnf1 = j-1;
                }
            }

            if (dnfs < 2) {
                int[] t1 = allSolvesList.get(allSolvesList.size()-1).getTime();
                int[] t2 = allSolvesList.get(allSolvesList.size()-2).getTime();
                int[] t3 = allSolvesList.get(allSolvesList.size()-3).getTime();
                int[] t4 = allSolvesList.get(allSolvesList.size()-4).getTime();
                int[] t5 = allSolvesList.get(allSolvesList.size()-5).getTime();
                int[] t6 = allSolvesList.get(allSolvesList.size()-6).getTime();
                int[] t7 = allSolvesList.get(allSolvesList.size()-7).getTime();
                int[] t8 = allSolvesList.get(allSolvesList.size()-8).getTime();
                int[] t9 = allSolvesList.get(allSolvesList.size()-9).getTime();
                int[] t10 = allSolvesList.get(allSolvesList.size()-10).getTime();
                int[] t11 = allSolvesList.get(allSolvesList.size()-11).getTime();
                int[] t12 = allSolvesList.get(allSolvesList.size()-12).getTime();
                float[] tS = new float[12];
                tS[0] = (t1[0]*3600) + (t1[1]*60) + (t1[2]) + (t1[3]/100.0f);
                tS[1] = (t2[0]*3600) + (t2[1]*60) + (t2[2]) + (t2[3]/100.0f);
                tS[2] = (t3[0]*3600) + (t3[1]*60) + (t3[2]) + (t3[3]/100.0f);
                tS[3] = (t4[0]*3600) + (t4[1]*60) + (t4[2]) + (t4[3]/100.0f);
                tS[4] = (t5[0]*3600) + (t5[1]*60) + (t5[2]) + (t5[3]/100.0f);
                tS[5] = (t6[0]*3600) + (t6[1]*60) + (t6[2]) + (t6[3]/100.0f);
                tS[6] = (t7[0]*3600) + (t7[1]*60) + (t7[2]) + (t7[3]/100.0f);
                tS[7] = (t8[0]*3600) + (t8[1]*60) + (t8[2]) + (t8[3]/100.0f);
                tS[8] = (t9[0]*3600) + (t9[1]*60) + (t9[2]) + (t9[3]/100.0f);
                tS[9] = (t10[0]*3600) + (t10[1]*60) + (t10[2]) + (t10[3]/100.0f);
                tS[10] = (t11[0]*3600) + (t11[1]*60) + (t11[2]) + (t11[3]/100.0f);
                tS[11] = (t12[0]*3600) + (t12[1]*60) + (t12[2]) + (t12[3]/100.0f);

                float fastest = tS[0];
                float slowest = 0;

                for (int i = 0; i < 12; i++){
                    if (tS[i] < fastest){
                        fastest = tS[i];
                    }
                    if (tS[i] > slowest){
                        slowest = tS[i];
                    }
                }
                float ao12 = ((tS[0] + tS[1] + tS[2] + tS[3] + tS[4] + tS[5] + tS[6] + tS[7] + tS[8] + tS[9] + tS[10] + tS[11])-(fastest)-(slowest))/10;
                int milliseconds = (int)((ao12 - Math.floor(ao12))*100);
                int seconds = (int)(Math.floor(ao12));
                int minutes = 0;
                int hours = 0;
                while(seconds > 60){
                    minutes++;
                    seconds-=60;
                }
                while(minutes > 60){
                    hours++;
                    minutes-=60;
                }
                milliseconds++;
                if (hours == 0 && minutes == 0) {
                    txt2.setText("ao12: " + seconds + "." + String.format("%02d", milliseconds));
                } else if (hours == 0) {
                    txt2.setText("ao12: " + minutes+ ":" + seconds + "." + String.format("%02d", milliseconds));
                } else {
                    txt2.setText("ao12: " + hours + ":" + minutes + ":" + seconds + "." + String.format("%02d", milliseconds));
                }
            } else {
                txt2.setText("ao12: DNF");
            }
        } else {
            txt2.setText("ao12: ---");
        }


    }

    private void closeSession(){
        mainClass.listS.set(ID, this);
        mainClass.sessionOpen = false;
        win.close();
    }


    public void initialize(){
        r = new Random(System.currentTimeMillis());
        allS = FXCollections.observableArrayList(allSolvesList);

        win = new Stage();
        win.setOnCloseRequest(e -> {
            closeSession();
        });
        win.initStyle(StageStyle.UNDECORATED);
        Pane pNorm = new Pane();
        pNorm.setPrefSize(900+offsetY, 600+offsetY);

        Button btnCloseWindow = new Button("X");
        btnCloseWindow.resize(30,0);
        btnCloseWindow.getStyleClass().add("BtnCloseWindowX");
        btnCloseWindow.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btnCloseWindow.setStyle("-fx-background-color: #DA6062;/*#33312D*/");
            } else {
                if (mainClass.myTheme.equals(Themes.DEFAULT_THEME)){
                    btnCloseWindow.setStyle("-fx-background-color: #171814;");
                } else if (mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
                    btnCloseWindow.setStyle("-fx-background-color: #131317;");
                } else if (mainClass.myTheme.equals(Themes.DEFAULT_GREEN)){
                    btnCloseWindow.setStyle("-fx-background-color: #101010;");
                } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
                    btnCloseWindow.setStyle("-fx-background-color: #9cd1b6;");
                } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
                    btnCloseWindow.setStyle("-fx-background-color: #bdb4b4;");
                } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){
                    btnCloseWindow.setStyle("-fx-background-color: #ba3939;");
                }
            }
        });
        btnCloseWindow.setOnAction(e -> {
            closeSession();
        });

        Button btnMinWindow = new Button("-");
        btnMinWindow.resize(30,0);
        btnMinWindow.getStyleClass().add("BtnCloseWindowX");
        btnMinWindow.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                //HEY
                if (mainClass.myTheme.equals(Themes.DEFAULT_THEME) ||
                        mainClass.myTheme.equals(Themes.DEFAULT_GREEN)||
                        mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
                    btnMinWindow.setStyle("-fx-background-color: #3B3B38;/*#33312D*/");
                } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
                    btnMinWindow.setStyle("-fx-background-color: #D8E8E8;/*#33312D*/");
                } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
                    btnMinWindow.setStyle("-fx-background-color: #FFD5B3;/*#33312D*/");

                } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){

                    btnMinWindow.setStyle("-fx-background-color: #DAC46E;/*#33312D*/");
                }
            } else {
                if (mainClass.myTheme.equals(Themes.DEFAULT_THEME)){
                    btnMinWindow.setStyle("-fx-background-color: #171814;");
                } else if (mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
                    btnMinWindow.setStyle("-fx-background-color: #131317;");
                } else if (mainClass.myTheme.equals(Themes.DEFAULT_GREEN)){
                    btnMinWindow.setStyle("-fx-background-color: #101010;");
                } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
                    btnMinWindow.setStyle("-fx-background-color: #9cd1b6;");
                } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
                    btnMinWindow.setStyle("-fx-background-color: #bdb4b4;");
                } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){
                    btnMinWindow.setStyle("-fx-background-color: #ba3939;");
                }
            }
        });
        btnMinWindow.setOnAction(e -> {
            win.setIconified(true);
        });

        HBox topBar = new HBox();
        topBar.relocate(0,0);
        //topBar.resize(550,30);
        topBar.getStyleClass().add("topBarDesign");
        topBar.setPadding(new Insets(0, 900+offsetY/2, 0, 0));
        final Delta dragDelta = new Delta();
        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = win.getX() - mouseEvent.getScreenX();
                dragDelta.y = win.getY() - mouseEvent.getScreenY();
            }
        });
        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                win.setX(mouseEvent.getScreenX() + dragDelta.x);
                win.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        topBar.setVisible(true);

        Label lblTitle = new Label(" | "+nameSession);
        lblTitle.getStyleClass().add("NormalText");
        lblTitle.setStyle("-fx-text-fill: #F9F6E6;");

        topBar.getChildren().addAll(btnCloseWindow, btnMinWindow, lblTitle);
        pNorm.getChildren().add(topBar);

        timingPane  = new Pane();
        timingPane.setPrefSize(600, 352);
        timingPane.relocate(3,45+offsetY);
        timingPane.getStyleClass().add("generalPanels");
        pNorm.getChildren().add(timingPane);

        listPane  = new Pane();
        listPane.setPrefSize(296, 559);
        listPane.relocate(600+9,45+offsetY);
        listPane.getStyleClass().add("generalPanels");
        pNorm.getChildren().add(listPane);

        commentPane  = new Pane();
        commentPane.setPrefSize(600, 180);
        commentPane.relocate(3,423+offsetY);
        commentPane.getStyleClass().add("generalPanels");
        pNorm.getChildren().add(commentPane);

        Scene normScene = new Scene(pNorm, 900+offsetY/2, 600+offsetY*2);
        if (mainClass.myTheme.equals(Themes.DEFAULT_THEME)){
            normScene.getStylesheets().add("design/defaultTheme.css");
            normScene.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_GREEN)){
            normScene.getStylesheets().add("design/theme3.css");
            normScene.getStylesheets().removeAll("design/defaultTheme.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
            normScene.getStylesheets().add("design/theme2.css");
            normScene.getStylesheets().removeAll("design/theme3.css","design/defaultTheme.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
            normScene.getStylesheets().add("design/theme4.css");
            normScene.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/defaultTheme.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
            normScene.getStylesheets().add("design/theme5.css");
            normScene.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/defaultTheme.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){
            normScene.getStylesheets().add("design/theme6.css");
            normScene.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/defaultTheme.css");
        }
        win.setScene(normScene);
        win.setResizable(false);
        win.setTitle("FmsTimer X | " + nameSession);
        win.show();

        lblScramble = new Label();
        lblScramble.setText("Scramble: RONALDO É UM SCRAMBLE BOM PRA KARALH*");
        lblScramble.getStyleClass().add("ScrambleText");
        lblScramble.relocate(5, 15+offsetY);
        pNorm.getChildren().add(lblScramble);

        lblTime = new Label();
        lblTime.setText("00.00");
        lblTime.getStyleClass().add("TimeText");
        lblTime.relocate(0, 50); // x = 180
        lblTime.setPrefSize(600, 200);
        lblTime.setAlignment(Pos.CENTER);
        timingPane.getChildren().add(lblTime);

        Label lblDescTime = new Label();
        lblDescTime.setText("Press 'SPACE' to Start");
        lblDescTime.getStyleClass().add("NormalText");
        lblDescTime.relocate(0, 300); // x = 180
        lblDescTime.setPrefSize(600, 40);
        lblDescTime.setAlignment(Pos.CENTER);
        lblDescTime.setStyle("-fx-font-size: 23px;");
        timingPane.getChildren().add(lblDescTime);

        txt2 = new Label();
        txt2.setText("ao12: ---");
        txt2.getStyleClass().add("NormalText");
        txt2.relocate(10, 260); // x = 180
        timingPane.getChildren().add(txt2);

        txt3 = new Label();
        txt3.setText("ao5: ---");
        txt3.getStyleClass().add("NormalText");
        txt3.relocate(10, 275); // x = 180
        timingPane.getChildren().add(txt3);

        txt4 = new Label();
        txt4.setText("mo3: ---");
        txt4.getStyleClass().add("NormalText");
        txt4.relocate(10, 290); // x = 180
        timingPane.getChildren().add(txt4);

        txt5 = new Label();
        txt5.setText("Comment on the Session:");
        txt5.getStyleClass().add("NormalText");
        txt5.relocate(226, 402+offsetY); // x = 180
        txt5.setAlignment(Pos.CENTER);
        pNorm.getChildren().add(txt5);

        Label txt1 = new Label();
        txt1.setText("Times:");
        txt1.getStyleClass().add("NormalText");
        txt1.relocate(0, 4); // x = 180
        txt1.setPrefSize(296, 40);
        txt1.setAlignment(Pos.CENTER);
        txt1.setStyle("-fx-font-size: 18px;");
        listPane.getChildren().add(txt1);

        listTimes = new ListView<>(allS);
        listTimes.resize(294, 400);
        listTimes.relocate(25, 42);
        listTimes.getStyleClass().add("listViewTimes");
        listTimes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
                        (event.getTarget() instanceof LabeledText || allSolvesList.size() > 0)) {

                    new modify_solve(allSolvesList.get(listTimes.getSelectionModel().getSelectedIndex()), listTimes.getSelectionModel().getSelectedIndex()+1, me);
                    listTimes.getSelectionModel().clearSelection();
                    pNorm.requestFocus();
                }
            }
        });

        listPane.getChildren().add(listTimes);


        TextArea tC = new TextArea();
        tC.setText(commentInSession);
        tC.setPromptText("Write your comment here.");
        tC.getStyleClass().add("textAreaComment");
        tC.setPrefSize(600-6, 180-6);
        tC.setOnKeyReleased(event -> {
            commentInSession = tC.getText();
        });
        tC.relocate(3,3);
        commentPane.getChildren().add(tC);

        rdbtnInspection = new RadioButton();
        rdbtnInspection.getStyleClass().add("NormalText");
        rdbtnInspection.setText("Inspection");
        rdbtnInspection.setSelected(true);
        rdbtnInspection.relocate(10,8);
        rdbtnInspection.setOnAction( e -> {
            pNorm.requestFocus();
        });
        timingPane.getChildren().add(rdbtnInspection);

        pNorm.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                    if (pressedKey) {
                        pressedKey = false;
                    }
                    if (event.getCode() == KeyCode.SPACE) {
                        if (!tC.isFocused()){
                            if (rdbtnInspection.isSelected() && !inspectionStarted && !timerStarted) {
                                startInspection(lblTime);
                                lblDescTime.setText("Press 'SPACE' to finish inspection");
                            }
                            else if (!rdbtnInspection.isSelected() && !timerStarted) {
                                startTimer(lblTime);
                                lblDescTime.setText("Press 'SPACE' to stop");
                            } else if (timerStarted){
                                stopTimer(listTimes);
                                lblDescTime.setText("Press 'SPACE' to start");
                            } else if (inspectionStarted){
                                stopInspection();
                                startTimer(lblTime);
                                lblDescTime.setText("Press 'SPACE' to stop");
                            }
                        }
                    }

            }
        });

        btnNewScramble = new Button();
        btnNewScramble.setText("New Scramble");
        btnNewScramble.getStyleClass().add("NormalBtn");
        btnNewScramble.relocate(780, 10+offsetY);
        btnNewScramble.resize(btnNewScramble.getWidth(), 25);
        btnNewScramble.setOnAction(e -> {
            generateNewScramble();
            pNorm.requestFocus();
        });
        pNorm.getChildren().add(btnNewScramble);

        btnFocus = new Button();
        btnFocus.setText("Focus on Timer");
        btnFocus.getStyleClass().add("NormalBtn");
        btnFocus.relocate(461, 6);
        btnFocus.setOnAction(e -> {
            pNorm.requestFocus();
        });
        timingPane.getChildren().add(btnFocus);

        Button addTimeBtn = new Button();
        addTimeBtn.relocate(10, 447);
        addTimeBtn.getStyleClass().add("NormalBtn");
        addTimeBtn.setText("Add Time");

        Button deleteTimeBtn = new Button();
        deleteTimeBtn.relocate(10, 483);
        deleteTimeBtn.getStyleClass().add("NormalBtn");
        deleteTimeBtn.setText("Delete Time");

        Button resetSessionBtn = new Button();
        resetSessionBtn.relocate(10, 519);
        resetSessionBtn.getStyleClass().add("NormalBtn");
        resetSessionBtn.setText("Reset Session");

        resetSessionBtn.setOnAction(e -> {
            int conf = JOptionPane.showConfirmDialog(null, "Are you sure?", "Reset Session",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf == 0){
                allSolvesList = new ArrayList<solve>();
                allS.clear();
                calculateAverages();
                pNorm.requestFocus();
            }
        });
        deleteTimeBtn.setOnAction(e -> {
            if(listTimes.getSelectionModel().getSelectedIndex() >= 0) {
                allSolvesList.remove(listTimes.getSelectionModel().getSelectedIndex());
                allS.remove(listTimes.getSelectionModel().getSelectedIndex());
                listTimes.getSelectionModel().clearSelection();
                JOptionPane.showMessageDialog(null, "Time deleted sucessfully.");
                calculateAverages();
                pNorm.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "You need to select a time.");
            }
        });
        addTimeBtn.setOnAction(e -> {
            solve tSolve = new solve(0,0,0,0,false,solveStates.NORMAL,"");
            String timeS = JOptionPane.showInputDialog(null, "Digite o tempo:");
            if (timeS != null || timeS != "") {
                if (timeS.length() == 4) { // x.xx
                    String[] parts = timeS.split(Pattern.quote("."));
                    tSolve.addCenti(Integer.parseInt(parts[1]));
                    tSolve.addSeconds(Integer.parseInt(parts[0]));
                } else if (timeS.length() == 5) { // xx.xx
                    String[] parts = timeS.split(Pattern.quote("."));
                    tSolve.addCenti(Integer.parseInt(parts[1]));
                    tSolve.addSeconds(Integer.parseInt(parts[0]));
                } else if (timeS.length() == 7) { // x:xx.xx
                    char[] allchars = timeS.toCharArray();
                    String[] times = new String[3];
                    times[0] =  Character.toString(allchars[5]) + Character.toString(allchars[6]);
                    times[1] =  Character.toString(allchars[2]) + Character.toString(allchars[3]);
                    times[2] =  Character.toString(allchars[0]);
                    tSolve.addCenti(Integer.parseInt(times[0]));
                    tSolve.addSeconds(Integer.parseInt(times[1]));
                    tSolve.addMinutes(Integer.parseInt(times[2]));
                } else if (timeS.length() == 8) { // xx:xx.xx
                    char[] allchars = timeS.toCharArray();
                    String[] times = new String[3];
                    times[0] =  Character.toString(allchars[6]) + Character.toString(allchars[7]);
                    times[1] =  Character.toString(allchars[3]) + Character.toString(allchars[4]);
                    times[2] =  Character.toString(allchars[0]) + Character.toString(allchars[1]);
                    tSolve.addCenti(Integer.parseInt(times[0]));
                    tSolve.addSeconds(Integer.parseInt(times[1]));
                    tSolve.addMinutes(Integer.parseInt(times[2]));
                } else if (timeS.length() >= 10){ // x:xx:xx.xx
                    String[] divided = timeS.split(Pattern.quote(":"));
                    String[] parts = divided[2].split(Pattern.quote("."));
                    tSolve.addCenti(Integer.parseInt(parts[1]));
                    tSolve.addSeconds(Integer.parseInt(parts[0]));
                    tSolve.addMinutes(Integer.parseInt(divided[1]));
                    tSolve.addHours(Integer.parseInt(divided[0]));
                }
            }
            tSolve.setScramble(actualScramble);
            actualSolve = tSolve;
            tSolve = new solve(0,0,0,0,false,solveStates.NORMAL,"");

            allSolvesList.add(actualSolve);
            allS.add(actualSolve);
            calculateAverages();
            generateNewScramble();

            pNorm.requestFocus();
        });

        listPane.getChildren().addAll(addTimeBtn, deleteTimeBtn, resetSessionBtn);

        pNorm.requestFocus();
        calculateAverages();
        generateNewScramble();
    }

    private solve tSolve = new solve(0,0,0,0,false,solveStates.NORMAL,"");
    @SuppressWarnings("unused")
    private void startInspection(Label lblT) {
        commentPane.setVisible(false);
        listPane.setVisible(false);
        timingPane.scaleXProperty();
        timingPane.scaleYProperty();
        timingPane.setScaleX(1.49);
        timingPane.setScaleY(1.6875);
        timingPane.relocate(155,130+offsetY);
        btnFocus.setVisible(false);
        txt5.setVisible(false);
        btnNewScramble.setVisible(false);
        lblScramble.setVisible(false);
        rdbtnInspection.setVisible(false);

        inspectionStarted = true;
        Thread t = new Thread(this) {
                @Override
                public void run() {
                    int seconds = 0;
                    for(;;) {
                        if (inspectionStarted) {
                            try {

                                if (seconds >= 17) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            lblTime.setText("DNF");
                                        }
                                    });
                                    tSolve.setStateOfSolve(solveStates.DNF);
                                } else if (seconds >= 15) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            lblTime.setText("2+");
                                        }
                                    });
                                    tSolve.setStateOfSolve(solveStates.PLUS2);
                                }

                                if (seconds < 15) {
                                    int finalSeconds = seconds;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            lblTime.setText(Integer.toString(finalSeconds));
                                        }
                                    });
                                }
                                sleep(1000);
                                seconds++;
                            } catch (Exception e){

                            }
                        } else {
                            break;
                        }
                    }
                }



        }; t.start();
    }
    @SuppressWarnings("unused")
    private void startTimer(Label lblT) {
        commentPane.setVisible(false);
        listPane.setVisible(false);
        timingPane.scaleXProperty();
        timingPane.scaleYProperty();
        timingPane.setScaleX(1.49);
        timingPane.setScaleY(1.6875);
        timingPane.relocate(155,130+offsetY);
        btnFocus.setVisible(false);
        txt5.setVisible(false);
        btnNewScramble.setVisible(false);
        lblScramble.setVisible(false);
        rdbtnInspection.setVisible(false);

        timerStarted = true;
        time[0] = 0;time[1] = 0;time[2] = 0;time[3] = 0;
        Thread t = new Thread(this) {
            @Override
            public void run() {

                for(;;) {
                    if (timerStarted) {
                        try {

                            sleep(10);
                            if (time[3] >= 100) { // cent
                                time[3] = 0;
                                time[2]++;
                            }
                            if (time[2] >= 60) { // sec
                                time[3] = 0;
                                time[2] = 0;
                                time[1]++;
                            }
                            if (time[1] >= 60) { // min
                                time[3] = 0;
                                time[2] = 0;
                                time[1] = 0;
                                time[0]++;
                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblT.setText(time[0] + ":" + time[1] + ":" + time[2] + "." + time[3]);
                                }
                            });
                            tSolve.setTime(time[0], time[1], time[2], time[3]);
                            time[3]++;

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            }


        }; t.start();
    }

    private void stopTimer(ListView ob) {
        commentPane.setVisible(true);
        listPane.setVisible(true);
        timingPane.scaleXProperty();
        timingPane.scaleYProperty();
        timingPane.setScaleX(1);
        timingPane.setScaleY(1);
        timingPane.relocate(3,45+offsetY);
        btnFocus.setVisible(true);
        txt5.setVisible(true);
        btnNewScramble.setVisible(true);
        lblScramble.setVisible(true);
        rdbtnInspection.setVisible(true);


        System.out.println("Timer Parou!");
        timerStarted = false;
        tSolve.setScramble(actualScramble);
        actualSolve = tSolve;
        tSolve = new solve(0,0,0,0,false,solveStates.NORMAL,"");
        actualSolve.addCenti(2);
        if (actualSolve.getStateOfSolve() == solveStates.PLUS2){
            actualSolve.addSeconds(2);
        }
        allSolvesList.add(actualSolve);
        allS.add(actualSolve);
        generateNewScramble();
        calculateAverages();

    }

    private void stopInspection() {
        System.out.println("Inspeção Parou!");
        inspectionStarted = false;
    }


    @Override
    public String toString(){
        return nameSession;
    }


    @Override
    public void run() {

    }
}
