package si.uni_lj.fe.tnuv.cppaplikacija;


import android.graphics.drawable.GradientDrawable;


// poseben razred za dinamično obrobo gumbov
public class CustomGradientDrawable extends GradientDrawable {
    private int strokeColor;
    private int fillColor;


    public CustomGradientDrawable(int fillColor, int strokeColor, int strokeWidth) {
        super(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { fillColor, fillColor });
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        setStroke(strokeWidth, strokeColor);
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setStroke(10, strokeColor);
    }


    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        setColor(fillColor);
    }


    public int getFillColor() {
        return fillColor;
    }
}
