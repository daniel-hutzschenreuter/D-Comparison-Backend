package de.ptb.backend.model.dsi;

public class SiExpandedUnc {
    Double uncertainty;
    int coverageFactor;
    Double coverageProbability;

    public Double getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(Double uncertainty) {
        this.uncertainty = uncertainty;
    }

    public int getCoverageFactor() {
        return coverageFactor;
    }

    public void setCoverageFactor(int coverageFactor) {
        this.coverageFactor = coverageFactor;
    }

    public Double getCoverageProbability() {
        return coverageProbability;
    }

    public void setCoverageProbability(Double coverageProbability) {
        this.coverageProbability = coverageProbability;
    }
    public SiExpandedUnc(Double uncertainty, int coverageFactor, Double coverageProbability){
        this.uncertainty = uncertainty;
        this.coverageFactor = coverageFactor;
        this.coverageProbability = coverageProbability;
    }

    @Override
    public String toString() {
        return "SiExpandedUnc{" +
                "uncertainty=" + uncertainty +
                ", coverageFactor=" + coverageFactor +
                ", coverageProbability=" + coverageProbability +
                '}';
    }
}
