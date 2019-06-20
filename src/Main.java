import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    long startTime;
    int steps = 0;

    private void updatePane(GridPane pane, ImageView[][] imageView) {
        pane.getChildren().clear();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (imageView[i][j] == null) continue;
                pane.add(imageView[i][j], j, i);
            }
    }

    private void check(Stage stage, ImageView[][] image) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (image[i][j].getNum()==0 && (i != 3 || j != 3)) return;
                if (image[i][j] == null) continue;
                if (image[i][j].getNum() != (i * 4 + j + 1)) return;
            }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You win\nTime:" + String.format("%d sec", (System.nanoTime() - startTime) / 1000000000) + "\nSteps: " + steps);
        alert.showAndWait();
        stage.close();
    }

    private boolean findNum(ImageView[][] imageView, int num) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (imageView[i][j] == null) continue;
                if (imageView[i][j].getNum() == num) return true;
            }
        return false;
    }

    private boolean moveImage(ImageView[][] imageView, int i, int j, int x, int y) {
        if (i + x >= 0 && i + x < 4 && j + y >= 0 && j + y < 4 && imageView[i + x][j + y].getNum() == 0) {
            ImageView temp= imageView[i + x][j + y];
            imageView[i + x][j + y] = imageView[i][j];
            imageView[i][j]=temp;
            steps++;
            return true;
        }
        return false;
    }

    private boolean canBeSolved(ImageView[][] imageView) {
        int sum = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (imageView[i][j] == null) {
                    sum += i+1;
                    continue;
                }
                for (int q = i; q < 4; q++)
                    for (int m = j + 1; m < 4; m++)
                        if (imageView[q][m].getNum() < imageView[i][j].getNum()) sum++;
            }
        if (sum % 2 == 0) return true;
        return false;
    }

    private void generate(ImageView[][] imageView) {
        while (true) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    int num;
                    while (true) {
                        num = (int) (Math.random() * 16);
                        if (!findNum(imageView, num)) break;
                    }
                    if (num == 0) {
                        imageView[i][j] = new ImageView(null);
                        imageView[i][j].setNum(0);
                        continue;
                    }
                    Image image = new Image("img/" + num + ".png");
                    imageView[i][j] = new ImageView(image);
                    imageView[i][j].setNum(num);
                }

            if (canBeSolved(imageView)) break;
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    imageView[i][j] = null;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageView[][] imageView = new ImageView[4][4];
        GridPane pane = new GridPane();
        pane.setHgap(30);
        pane.setVgap(30);
        generate(imageView);
        updatePane(pane, imageView);
        Scene scene = new Scene(pane);
        scene.setOnKeyReleased(event -> {
            boolean find = false;
            for (int i = 0; i < 4; i++) {
                if (find) break;
                for (int j = 0; j < 4; j++) {
                    if (find) break;
                    switch (event.getCode()) {
                        case UP:
                            find = moveImage(imageView, i, j, -1, 0);
                            break;
                        case DOWN:
                            find = moveImage(imageView, i, j, 1, 0);
                            break;
                        case LEFT:
                            find = moveImage(imageView, i, j, 0, -1);
                            break;
                        case RIGHT:
                            find = moveImage(imageView, i, j, 0, 1);
                            break;
                    }
                }
            }
            updatePane(pane, imageView);
            check(primaryStage, imageView);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        startTime = System.nanoTime();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
