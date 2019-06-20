import javafx.scene.image.Image;

public class ImageView extends javafx.scene.image.ImageView {
    private int num;

    ImageView(Image image) {
        super(image);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
