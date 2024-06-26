package program;

import javafx.application.Application;
import classes.Themes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

class Delta { double x, y; }

public class mainClass extends Application implements Serializable {

    private static final long serialVersionUID = 1L;

    public static boolean sessionOpen;
    private static Button btnCloseWindow;
    private static Button btnMinWindow;
    private Stage window;
    private static Scene s;
    public static Themes myTheme;
    private TextField txtFieldNameNewSession;
    public static ObservableList<session> listS = FXCollections.observableArrayList();

    public static void main(String[] args){
        launch(args);
    }


    public static void switchTheme(Themes t){
        myTheme = t;
        if (myTheme.equals(Themes.DEFAULT_THEME)){
            s.getStylesheets().add("design/defaultTheme.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (myTheme.equals(Themes.DEFAULT_GREEN)){
            s.getStylesheets().add("design/theme3.css");
            s.getStylesheets().removeAll("design/defaultTheme.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (myTheme.equals(Themes.DEFAULT_BLUE)){
            s.getStylesheets().add("design/theme2.css");
            s.getStylesheets().removeAll("design/theme3.css","design/defaultTheme.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (myTheme.equals(Themes.WHITE_RED)){
            s.getStylesheets().add("design/theme4.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/defaultTheme.css","design/theme5.css","design/theme6.css");
        } else if (myTheme.equals(Themes.ORANGE_BLUE)){
            s.getStylesheets().add("design/theme5.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/defaultTheme.css","design/theme6.css");
        } else if (myTheme.equals(Themes.YELLOW_PURPLE)){
            s.getStylesheets().add("design/theme6.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/defaultTheme.css");
        }

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


    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            File file1 = new File("c:/FmsTimer/saveFmsTimer.fmstiming");
            FileInputStream fis = new FileInputStream(file1);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList arraylistTemp = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            listS = FXCollections.observableList(arraylistTemp);

            File file2 = new File("c:/FmsTimer/cfg.fmstheme");
            FileInputStream fis2 = new FileInputStream(file2);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);

            String t;
            t = (String) ois2.readObject();
            ois2.close();
            fis2.close();
            myTheme = Themes.valueOf(t);

        }catch(IOException ioe){
            ioe.printStackTrace();
            //return;
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            // return;
        }

        window = primaryStage;
        window.setOnCloseRequest(e -> {
            System.exit(0);
        });

        if (myTheme == null){
            myTheme = Themes.DEFAULT_THEME;
        }

        window.initStyle(StageStyle.UNDECORATED);
        Pane pane = new Pane();

        s = new Scene(pane, 550, 600);


        window.setTitle("FmsTimer X");
        window.setScene(s);
        window.setResizable(false);
        window.show();


        btnCloseWindow = new Button("X");
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
            int sure = JOptionPane.showConfirmDialog(null, "Want to exit?", "Exit Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (sure == 0 && !sessionOpen){ // ok
                File file = new File("c:/FmsTimer");
                if (!file.exists()) {
                    if (file.mkdir()) {
                        System.out.println("Directory is created!");
                    } else {
                        System.out.println("Failed to create directory!");
                    }
                }

                try{
                    ArrayList<session> s = new ArrayList<>(listS);
                    File file1 = new File("c:/FmsTimer/saveFmsTimer.fmstiming");
                    FileOutputStream fos1 = new FileOutputStream(file1);
                    ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                    oos1.writeObject(s);
                    oos1.close();
                    fos1.close();

                    File file2 = new File("c:/FmsTimer/cfg.fmstheme");
                    FileOutputStream fos2= new FileOutputStream(file2);
                    ObjectOutputStream oos2= new ObjectOutputStream(fos2);
                    oos2.writeObject(myTheme.name());
                    oos2.close();
                    fos2.close();

                    System.exit(1);
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }


            } else if (sessionOpen){
                JOptionPane.showMessageDialog(null,"Close all the sessions!");
            }
        });

        btnMinWindow = new Button("-");
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
            window.setIconified(true);
        });
        switchTheme(myTheme);
        HBox topBar = new HBox();
        topBar.relocate(0,0);
        //topBar.resize(550,30);
        topBar.getStyleClass().add("topBarDesign");
        topBar.setPadding(new Insets(0, 550, 0, 0));
        final Delta dragDelta = new Delta();
        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = window.getX() - mouseEvent.getScreenX();
                dragDelta.y = window.getY() - mouseEvent.getScreenY();
            }
        });
        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                window.setX(mouseEvent.getScreenX() + dragDelta.x);
                window.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        topBar.setVisible(true);
        topBar.getChildren().addAll(btnCloseWindow, btnMinWindow);
        pane.getChildren().add(topBar);


        if (listS.size() <= 0){
            for (int i = 0; i < 15; i++){
                listS.add(new session(i));
                listS.get(i).nameSession = "Session: " + (int)(i + 1);
            }
        }

        ListView<session> listSessions = new ListView<session>(listS);
        listSessions.setPrefSize(180, 400);
        listSessions.relocate(30, 150);
        listSessions.getStyleClass().add("listViewSessions");
        pane.getChildren().add(listSessions);

        Label lblTitle = new Label();
        lblTitle.setText("FmsTimer X");
        lblTitle.setAlignment(Pos.CENTER);
        lblTitle.relocate(80,20);
        lblTitle.getStyleClass().add("lblTitle");
        pane.getChildren().add(lblTitle);

        Label lblTxt1 = new Label();
        lblTxt1.setText("Sessions:");
        lblTxt1.relocate(90,127);
        lblTxt1.getStyleClass().add("NormalText");
        pane.getChildren().add(lblTxt1);

        Button btnEnter = new Button();
        btnEnter.relocate(250, 180);
        btnEnter.resize(150, 50);
        btnEnter.getStyleClass().add("BtnEnter");
        btnEnter.setText("Enter Session");
        btnEnter.setOnAction(e -> {
            if (listSessions.getSelectionModel().getSelectedIndex() >= 0 && !sessionOpen){
                listS.get(listSessions.getSelectionModel().getSelectedIndex()).initialize();
                sessionOpen = true;
            }
        });

        Button btnCreateNewSession = new Button();
        btnCreateNewSession.relocate(250, 230);
        btnCreateNewSession.resize(150, 50);
        btnCreateNewSession.getStyleClass().add("NormalBtn");
        btnCreateNewSession.setText("Create Session");
        btnCreateNewSession.setOnAction(e -> {
            String newSessionName = txtFieldNameNewSession.getText();
            boolean nameOk = true;
            if (newSessionName.equals("")) {
                newSessionName = "Session " + (int)(listS.size()+1);
            }
            for (int h = 0; h < listS.size(); h++) {
                if (newSessionName.equals(listS.get(h).nameSession)) {
                    nameOk = false;
                    break;
                }
            }
            if (nameOk) {
                session newS = new session(listS.size());
                newS.nameSession = newSessionName;
                listS.add(newS);
            } else {
                JOptionPane.showMessageDialog(null, "Session Name(" + newSessionName + ") is already in use. Try a different one.");
            }
        });

        txtFieldNameNewSession = new TextField();
        txtFieldNameNewSession.setPromptText("New Session Name");
        txtFieldNameNewSession.relocate(390, 240);
        pane.getChildren().add(txtFieldNameNewSession);

        Button btnDeleteSession = new Button();
        btnDeleteSession.relocate(250, 280);
        btnDeleteSession.resize(150, 50);
        btnDeleteSession.getStyleClass().add("NormalBtn");
        btnDeleteSession.setText("Delete Session");
        btnDeleteSession.setOnAction(e -> {
            if (listSessions.getSelectionModel().getSelectedIndex() >= 15){ // ok
                JOptionPane.showMessageDialog(null, "Session [" + listS.get(listSessions.getSelectionModel().getSelectedIndex()).nameSession + "] deleted sucessfully." );
                listS.remove(listSessions.getSelectionModel().getSelectedIndex());
            } else {
                JOptionPane.showMessageDialog(null, "You can't delete a default session.");
            }
        });

        Button btnResetAllSessions = new Button();
        btnResetAllSessions.relocate(250, 330);
        btnResetAllSessions.resize(150, 50);
        btnResetAllSessions.getStyleClass().add("NormalBtn");
        btnResetAllSessions.setText("Reset All Sessions");
        btnResetAllSessions.setOnAction(e -> {
            int sure = JOptionPane.showConfirmDialog(null, "Are you sure?", "Reset All",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (sure == 0){ // ok
                for (int i = 0; i < listS.size() ; i++){
                    session nS = new session(i);
                    nS.nameSession = listS.get(i).nameSession;
                    listS.set(i, nS);
                }
                JOptionPane.showMessageDialog(null, "All sessions were reseted sucessfully.");
            }
        });

        Label lblV = new Label();
        lblV.setText("Version 2.2");
        lblV.getStyleClass().add("NormalText");
        lblV.relocate(5,25);

        Button btnThemes = new Button();
        btnThemes.relocate(250, 430);
        btnThemes.resize(150, 50);
        btnThemes.getStyleClass().add("NormalBtn");
        btnThemes.setText("Themes");
        btnThemes.setOnAction(e -> {
            if (!sessionOpen){
                new themesWindow();
            }
        });

        pane.getChildren().addAll(btnEnter,btnCreateNewSession, btnDeleteSession, btnResetAllSessions, btnThemes, lblV);

    }

}
