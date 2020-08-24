package fr.skylyxx.skdynmap;

public class AreaStyle {

    String lineColor;
    double lineOpacity;
    int lineWeight;
    String fillColor;
    double fillOpacity;

    public AreaStyle(String lineColor, double lineOpacity, int lineWeight, String fillColor, double fillOpacity) {
        this.lineColor = lineColor;
        this.lineOpacity = lineOpacity;
        this.lineWeight = lineWeight;

        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
    }

    public String getLineColor() {
        return lineColor;
    }

    public double getLineOpacity() {
        return lineOpacity;
    }

    public int getLineWeight() {
        return lineWeight;
    }

    public String getFillColor() {
        return fillColor;
    }

    public double getFillOpacity() {
        return fillOpacity;
    }

    public String valuesToString() {
        return "line-color=" + getLineColor() + " line-opacity=" + getLineOpacity() + " line-weight=" + getLineWeight() + " fill-color=" + getFillColor() + " fill-opacity=" + getFillOpacity();
    }

}
