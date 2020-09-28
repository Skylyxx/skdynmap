package fr.skylyxx.skdynmap.utils;

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

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public double getLineOpacity() {
        return lineOpacity;
    }

    public void setLineOpacity(double lineOpacity) {
        this.lineOpacity = lineOpacity;
    }

    public int getLineWeight() {
        return lineWeight;
    }

    public void setLineWeight(int lineWeight) {
        this.lineWeight = lineWeight;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public double getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public String toString() {
        return "line-color=" + getLineColor() + " line-opacity=" + getLineOpacity() + " line-weight=" + getLineWeight() + " fill-color=" + getFillColor() + " fill-opacity=" + getFillOpacity();
    }
}
