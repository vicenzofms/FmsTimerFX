package program;

import classes.Themes;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.Serializable;

public class themesWindow implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Stage window;
    private transient Image imgPreview;
    private transient ImageView iv;
    private transient Pane imgPane;

    private void close(){
        window.close();
    }


    public themesWindow(){
        Initialize();
    }

    public void Initialize(){
        System.out.println("Inicio da janela");
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        Pane p = new Pane();
        Scene s = new Scene(p, 450,300);



        HBox topBar = new HBox();
        topBar.relocate(0,0);
        //topBar.resize(550,30);
        topBar.getStyleClass().add("topBarDesign");
        topBar.setPadding(new Insets(10, 450, 10, 0));
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

        if (mainClass.myTheme.equals(Themes.DEFAULT_THEME)){
            s.getStylesheets().add("design/defaultTheme.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_GREEN)){
            s.getStylesheets().add("design/theme3.css");
            s.getStylesheets().removeAll("design/defaultTheme.css","design/theme2.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.DEFAULT_BLUE)){
            s.getStylesheets().add("design/theme2.css");
            s.getStylesheets().removeAll("design/theme3.css","design/defaultTheme.css","design/theme4.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.WHITE_RED)){
            s.getStylesheets().add("design/theme4.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/defaultTheme.css","design/theme5.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.ORANGE_BLUE)){
            s.getStylesheets().add("design/theme5.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/defaultTheme.css","design/theme6.css");
        } else if (mainClass.myTheme.equals(Themes.YELLOW_PURPLE)){
            s.getStylesheets().add("design/theme6.css");
            s.getStylesheets().removeAll("design/theme3.css","design/theme2.css","design/theme4.css","design/theme5.css","design/defaultTheme.css");
        }

        RadioButton rbDefault = new RadioButton("Default Theme");
        rbDefault.relocate(20, 30);
        rbDefault.getStyleClass().add("NormalText");
        RadioButton rbDefaultBlue = new RadioButton("Black and Blue");
        rbDefaultBlue.relocate(20, 70);
        rbDefaultBlue.getStyleClass().add("NormalText");
        RadioButton rbDefaultGreen = new RadioButton("Black and Green");
        rbDefaultGreen.relocate(20, 110);
        rbDefaultGreen.getStyleClass().add("NormalText");
        RadioButton rbWhiteRed = new RadioButton("Light Theme");
        rbWhiteRed.relocate(20, 150);
        rbWhiteRed.getStyleClass().add("NormalText");
        RadioButton rbOrangeBlue = new RadioButton("Turquoise theme");
        rbOrangeBlue.relocate(20, 190);
        rbOrangeBlue.getStyleClass().add("NormalText");
        RadioButton rbYellowPurple = new RadioButton("Red theme");
        rbYellowPurple.relocate(20, 230);
        rbYellowPurple.getStyleClass().add("NormalText");

        imgPreview = new Image("resources/img1.png");
        if(mainClass.myTheme == Themes.DEFAULT_THEME){rbDefault.setSelected(true);imgPreview = new Image("resources/img1.png");}
        else if(mainClass.myTheme == Themes.DEFAULT_BLUE){rbDefaultBlue.setSelected(true);imgPreview = new Image("resources/img2.png");}
        else if(mainClass.myTheme == Themes.DEFAULT_GREEN){rbDefaultGreen.setSelected(true);imgPreview = new Image("resources/img3.png");}
        else if(mainClass.myTheme == Themes.WHITE_RED){rbWhiteRed.setSelected(true);imgPreview = new Image("resources/img4.png");}
        else if(mainClass.myTheme == Themes.ORANGE_BLUE){rbOrangeBlue.setSelected(true);imgPreview = new Image("resources/img5.png");}
        else if(mainClass.myTheme == Themes.YELLOW_PURPLE){rbYellowPurple.setSelected(true);imgPreview = new Image("resources/img6.png");}

        ToggleGroup tg = new ToggleGroup();
        rbDefault.setToggleGroup(tg);
        rbDefaultBlue.setToggleGroup(tg);
        rbDefaultGreen.setToggleGroup(tg);
        rbWhiteRed.setToggleGroup(tg);
        rbOrangeBlue.setToggleGroup(tg);
        rbYellowPurple.setToggleGroup(tg);

        rbDefault.setOnAction(e -> {
            imgPreview = new Image("resources/img1.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });
        rbDefaultBlue.setOnAction(e -> {
            imgPreview = new Image("resources/img2.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });
        rbDefaultGreen.setOnAction(e -> {
            imgPreview = new Image("resources/img3.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });
        rbWhiteRed.setOnAction(e -> {
            imgPreview = new Image("resources/img4.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });
        rbOrangeBlue.setOnAction(e -> {
            imgPreview = new Image("resources/img5.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });
        rbYellowPurple.setOnAction(e -> {
            imgPreview = new Image("resources/img6.png");
            ImageView iv = new ImageView(imgPreview);
            iv.relocate(2,3);
            imgPane = new Pane(iv);
            imgPane.relocate(170,30);
            imgPane.setStyle("-fx-border-width: 10px");
            imgPane.setStyle("-fx-border-style: solid");
            imgPane.setStyle("-fx-border-insets: 3px");
            imgPane.setStyle("-fx-border-color: black");
            p.getChildren().add(imgPane);
        });

        Button btnApply = new Button("Apply Theme");
        btnApply.relocate(175,210);
        btnApply.getStyleClass().add("NormalBtn");
        btnApply.setOnAction(e -> {
            if (rbDefault.isSelected()){
                mainClass.switchTheme(Themes.DEFAULT_THEME);
            } else if (rbDefaultBlue.isSelected()){
                mainClass.switchTheme(Themes.DEFAULT_BLUE);
            } else if (rbDefaultGreen.isSelected()){
                mainClass.switchTheme(Themes.DEFAULT_GREEN);
            } else if (rbWhiteRed.isSelected()){
                mainClass.switchTheme(Themes.WHITE_RED);
            } else if (rbOrangeBlue.isSelected()){
                mainClass.switchTheme(Themes.ORANGE_BLUE);
            } else if (rbYellowPurple.isSelected()){
                mainClass.switchTheme(Themes.YELLOW_PURPLE);
            }

            close();
        });
        p.getChildren().add(btnApply);

        iv = new ImageView(imgPreview);
        iv.relocate(2,3);
        imgPane = new Pane(iv);
        imgPane.relocate(170,30);
        imgPane.setStyle("-fx-border-width: 10px");
        imgPane.setStyle("-fx-border-style: solid");
        imgPane.setStyle("-fx-border-insets: 3px");
        imgPane.setStyle("-fx-border-color: black");
        p.getChildren().addAll(rbDefault, rbDefaultBlue, rbDefaultGreen, rbOrangeBlue, rbWhiteRed, rbYellowPurple, imgPane);

        window.setScene(s);
        window.showAndWait();
    }

}
