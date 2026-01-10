package io.vrecon.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RecognitionResult {
    @JsonProperty("vehicle_found")
    private boolean vehicleFound;

    private String make;
    private String model;
    private String generation;
    private String color;
    private String side;
    private String angle;

    @JsonProperty("recognition_probability")
    private Double recognitionProbability;

    @JsonProperty("rect_area")
    private RectArea rectArea;

    @JsonProperty("damage_detected")
    private boolean damageDetected;

    @JsonProperty("damage_area")
    private List<RectArea> damageArea;

    @JsonProperty("detection_notes")
    private String detectionNotes;

    @JsonProperty("multiple_vehicles_in_image")
    private boolean multipleVehiclesInImage;

    public boolean isVehicleFound() {
        return vehicleFound;
    }

    public void setVehicleFound(boolean vehicleFound) {
        this.vehicleFound = vehicleFound;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public Double getRecognitionProbability() {
        return recognitionProbability;
    }

    public void setRecognitionProbability(Double recognitionProbability) {
        this.recognitionProbability = recognitionProbability;
    }

    public RectArea getRectArea() {
        return rectArea;
    }

    public void setRectArea(RectArea rectArea) {
        this.rectArea = rectArea;
    }

    public boolean isDamageDetected() {
        return damageDetected;
    }

    public void setDamageDetected(boolean damageDetected) {
        this.damageDetected = damageDetected;
    }

    public List<RectArea> getDamageArea() {
        return damageArea;
    }

    public void setDamageArea(List<RectArea> damageArea) {
        this.damageArea = damageArea;
    }

    public String getDetectionNotes() {
        return detectionNotes;
    }

    public void setDetectionNotes(String detectionNotes) {
        this.detectionNotes = detectionNotes;
    }

    public boolean isMultipleVehiclesInImage() {
        return multipleVehiclesInImage;
    }

    public void setMultipleVehiclesInImage(boolean multipleVehiclesInImage) {
        this.multipleVehiclesInImage = multipleVehiclesInImage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RecognitionResult{\n");
        sb.append("  vehicleFound=").append(vehicleFound).append("\n");
        if (vehicleFound) {
            sb.append("  make='").append(make).append("'\n");
            sb.append("  model='").append(model).append("'\n");
            sb.append("  generation='").append(generation).append("'\n");
            sb.append("  color='").append(color).append("'\n");
            sb.append("  side='").append(side).append("'\n");
            sb.append("  angle='").append(angle).append("'\n");
            sb.append("  recognitionProbability=").append(recognitionProbability).append("\n");
            if (rectArea != null) {
                sb.append("  rectArea=").append(rectArea).append("\n");
            }
            sb.append("  damageDetected=").append(damageDetected).append("\n");
            if (damageArea != null && !damageArea.isEmpty()) {
                sb.append("  damageArea=").append(damageArea).append("\n");
            }
            if (detectionNotes != null) {
                sb.append("  detectionNotes='").append(detectionNotes).append("'\n");
            }
            sb.append("  multipleVehiclesInImage=").append(multipleVehiclesInImage).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static class RectArea {
        private int x;
        private int y;
        private int width;
        private int height;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("RectArea{x=%d, y=%d, width=%d, height=%d}", x, y, width, height);
        }
    }
}
