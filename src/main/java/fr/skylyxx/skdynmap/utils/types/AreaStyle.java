package fr.skylyxx.skdynmap.utils.types;

public class AreaStyle {

    private String fillColor;
    private double fillOpacity;
    private String lineColor;
    private double lineOpacity;
    private int lineWeight;

    public AreaStyle(String fillColor, double fillOpacity, String lineColor, double lineOpacity, int lineWeight) {
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.lineColor = lineColor;
        this.lineOpacity = lineOpacity;
        this.lineWeight = lineWeight;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }
    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }
    public void setLineOpacity(double lineOpacity) {
        this.lineOpacity = lineOpacity;
    }
    public void setLineWeight(int lineWeight) {
        this.lineWeight = lineWeight;
    }

    public String getFillColor() {
        return fillColor;
    }
    public double getFillOpacity() {
        return fillOpacity;
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

    @Override
    public String toString() {
        return "AreaStyle{" +
                "fillColor='" + fillColor + '\'' +
                ", fillOpacity=" + fillOpacity +
                ", lineColor='" + lineColor + '\'' +
                ", lineOpacity=" + lineOpacity +
                ", lineWeight=" + lineWeight +
                '}';
    }

    public AreaStyle clone() {
        return new AreaStyle(getFillColor(),getFillOpacity(),getLineColor(),getLineOpacity(),getLineWeight());
    }
}
