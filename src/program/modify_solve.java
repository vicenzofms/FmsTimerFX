package program;

import classes.Themes;
import classes.solve;
import classes.solveStates;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.Serializable;


public class modify_solve implements Serializable {

    private static final long serialVersionUID = 1L;


    private solve mySolve;
    private Stage window;
    private int ID;

    public modify_solve(solve s, int id, session se){
        mySolve = s;
        ID = id;
        initialize(se);
    }

    public void initialize(session s){
        window = new Stage();
        Pane p = new Pane();
        window.setResizable(false);



        Label lbltxt1 = new Label();
        lbltxt1.setText("Solve: " + ID);
        lbltxt1.getStyleClass().add("lblTitle");
        lbltxt1.setStyle("-fx-font-size: 36");
        lbltxt1.relocate(100, 20);
        lbltxt1.setAlignment(Pos.CENTER);
        p.getChildren().add(lbltxt1);

        ToggleGroup tg = new ToggleGroup();
        RadioButton rbNormal = new RadioButton();
        rbNormal.setText("Normal");
        rbNormal.setToggleGroup(tg);
        rbNormal.relocate(30,70);
        rbNormal.getStyleClass().add("NormalText");

        RadioButton rbPlus2 = new RadioButton();
        rbPlus2.setText("Plus 2");
        rbPlus2.setToggleGroup(tg);
        rbPlus2.relocate(120,70);
        rbPlus2.getStyleClass().add("NormalText");

        RadioButton rbDNF = new RadioButton();
        rbDNF.setText("DNF");
        rbDNF.setToggleGroup(tg);
        rbDNF.relocate(200,70);
        rbDNF.getStyleClass().add("NormalText");

        if (mySolve.getStateOfSolve() == solveStates.NORMAL){
            rbNormal.setSelected(true);
        } else if (mySolve.getStateOfSolve() == solveStates.PLUS2){
            rbPlus2.setSelected(true);
        } else {
            rbDNF.setSelected(true);
        }
        p.getChildren().addAll(rbDNF, rbNormal, rbPlus2);

        Label lblTime = new Label();
        lblTime.setText("Time: " + mySolve.timeFormated());
        lblTime.getStyleClass().add("ScrambleText");
        lblTime.setStyle("-fx-font-size: 36px");
        lblTime.relocate(10, 130);
        p.getChildren().add(lblTime);

        rbPlus2.setOnAction(e -> {
            if (mySolve.getStateOfSolve() != solveStates.PLUS2){
                mySolve.addSeconds(2);
                mySolve.setStateOfSolve(solveStates.PLUS2);
                lblTime.setText("Time: " + mySolve.timeFormated());
            }
        });
        rbNormal.setOnAction(e -> {
            if (mySolve.getStateOfSolve() == solveStates.PLUS2){
                mySolve.subSeconds(2);
                mySolve.setStateOfSolve(solveStates.NORMAL);
                lblTime.setText("Time: " + mySolve.timeFormated());
            } else if (mySolve.getStateOfSolve() == solveStates.DNF){
                mySolve.setStateOfSolve(solveStates.NORMAL);
                lblTime.setText("Time: " + mySolve.timeFormated());
            }
        });
        rbDNF.setOnAction(e -> {
            if (mySolve.getStateOfSolve() == solveStates.PLUS2){
                mySolve.subSeconds(2);
                mySolve.setStateOfSolve(solveStates.DNF);
                lblTime.setText("Time: " + mySolve.timeFormated());
            } else if (mySolve.getStateOfSolve() == solveStates.NORMAL){
                mySolve.setStateOfSolve(solveStates.DNF);
                lblTime.setText("Time: " + mySolve.timeFormated());
            }
        });

        Label lbltxt2 = new Label();
        lbltxt2.setText("Comment in Solve:");
        lbltxt2.getStyleClass().add("NormalText");
        lbltxt2.relocate(95,200);
        p.getChildren().add(lbltxt2);

        Label lbltxt3 = new Label();
        lbltxt3.setText("Scramble:");
        lbltxt3.getStyleClass().add("NormalText");
        lbltxt3.relocate(15,440);
        p.getChildren().add(lbltxt3);

        TextField txtScramble = new TextField();
        txtScramble.setPromptText("Scramble Here...");
        txtScramble.setText(mySolve.getScramble());
        txtScramble.getStyleClass().add("-fx-font-size: 11px;-fx-font-family: 'Gill Sans MT', Arial, sans-serif;");
        txtScramble.relocate(85,435);
        p.getChildren().add(txtScramble);

        TextArea tC = new TextArea();
        tC.setText(mySolve.getComment());
        tC.setPromptText("Write your comment here.");
        tC.getStyleClass().add("textAreaComment");
        tC.setPrefSize(290-6, 200-6);
        tC.relocate(3,3);
        Pane commentP = new Pane();
        commentP.getChildren().add(tC);
        commentP.relocate(7,220);
        p.getChildren().add(commentP);

        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.relocate(100,480);
        btnOK.getStyleClass().add("NormalBtn");
        btnOK.setPrefSize(100,40);
        btnOK.setOnAction(e -> {
            mySolve.setComment(tC.getText());
            mySolve.setScramble(txtScramble.getText());
            s.allSolvesList.set(ID-1, mySolve);
            s.allS.set(ID-1, mySolve);
            s.calculateAverages();
            window.close();
        });
        p.getChildren().add(btnOK);

        HBox topBar = new HBox();
        topBar.relocate(0,0);
        //topBar.resize(550,30);
        topBar.getStyleClass().add("topBarDesign");
        topBar.setPadding(new Insets(10, 300, 10, 0));
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
        p.getChildren().add(topBar);

        Scene sc = new Scene(p, 300,530);
        if (mainClass.myTheme.equals(Themes.DEFAULT_THEME)){
            sc.getStylesheets().add("design/defaultTheme.css");
            sc.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_GREEN)){
            sc.getStylesheets().add("design/theme3.css");
            sc.getStylesheets().removeAll("design/defaultTheme.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
            sc.getStylesheets().add("design/theme2.css");
            sc.getStylesheets().removeAll("design/theme3.css","design/defaultTheme.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
            sc.getStylesheets().add("design/theme4.css");
            sc.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/defaultTheme.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
            sc.getStylesheets().add("design/theme5.css");
            sc.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/defaultTheme.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){
            sc.getStylesheets().add("design/theme6.css");
            sc.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/defaultTheme.css");
        }
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(sc);
        window.showAndWait();
    }

}
