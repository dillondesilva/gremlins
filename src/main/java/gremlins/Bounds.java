package gremlins;

public class Bounds {

    public float left;
    public float right;
    public float top;
    public float bottom;

    public Bounds(float posX, float posY) {
        this.left = posX;
        this.right = posX + 20;
        this.top = posY;
        this.bottom = posY + 20;
    }

    public void setBounds(float posX, float posY) {
        this.left = posX;
        this.right = posX + 20;
        this.top = posY;
        this.bottom = posY + 20; 
    }

    public void shiftBounds(float shiftX, float shiftY) {
        this.left += shiftX;
        this.right += shiftX;
        this.top += shiftY;
        this.bottom += shiftY;
    }

}
