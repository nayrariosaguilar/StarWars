package webservice;
import com.example.starwars.R;

public class CharacterColor {
    private String color;
    private String character;

    public String getColor() {
        return color;
    }
    public String getCharacter() {
        return character;
    }
    // Setter Methods
    public void setColor(String color) {
        this.color = color;
    }


    public void setCharacter(String character) {
        this.character = character;
    }
    public int getSoundId() {
        switch (character) {
            case "C3PO":
                return R.raw.c3po;
            case "Chewaka":
                return R.raw.chewbacca;
            case "R2D2":
                return R.raw.r2d2;
            case "Darth Vader":
                return R.raw.darthvader;
            default:
                return 0;
        }
    }

}
