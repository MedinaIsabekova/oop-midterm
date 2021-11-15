package sample;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };

    private Parent createContent() {
        VBox box = new VBox();
        box.setPrefSize(500, 350);
        Button btn1 = new Button("Open dialog");
        btn1.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Title");
            alert.setContentText("Content");
            alert.show();
        });

        Button btn2 = new Button("Open second dialog");
        btn2.setOnAction(e -> {
            CustomDialog dialog = new CustomDialog("Title", "Content");
            dialog.openDialog();
        });

        box.getChildren().addAll(btn1, btn2);

        return box;
    }

    private static class CustomDialog extends Stage {

        private ScaleTransition a = new ScaleTransition();
        private ScaleTransition b = new ScaleTransition();

        private SequentialTransition anim = new SequentialTransition(a, b);

        CustomDialog(String header, String content) {
            Pane root = new Pane();

            a.setFromX(0.01);
            a.setFromY(0.01);
            a.setToY(1.0);
            a.setDuration(Duration.seconds(0.33));
            a.setInterpolator(EXP_IN);
            a.setNode(root);

            b.setFromX(0.01);
            b.setToX(1.0);
            b.setDuration(Duration.seconds(0.33));
            b.setInterpolator(EXP_OUT);
            b.setNode(root);

            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);

            Rectangle bg = new Rectangle(350, 150, Color.WHITESMOKE);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1.5);

            Text headerText = new Text(header);
            headerText.setFont(Font.font(20));

            Text contentText = new Text(content);
            contentText.setFont(Font.font(16));

            VBox box = new VBox(10,
                    headerText,
                    new Separator(Orientation.HORIZONTAL),
                    contentText
            );
            box.setPadding(new Insets(15));

            Button btn = new Button("OK");
            btn.setTranslateX(bg.getWidth() - 50);
            btn.setTranslateY(bg.getHeight() - 50);
            btn.setOnAction(e -> closeDialog());

            root.getChildren().addAll(bg, box, btn);

            setScene(new Scene(root, null));
        }

        void openDialog() {
            show();

            anim.play();
        }

        void closeDialog() {
            anim.setOnFinished(e -> close());
            anim.setAutoReverse(true);
            anim.setCycleCount(2);
            anim.playFrom(Duration.seconds(0.66));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}