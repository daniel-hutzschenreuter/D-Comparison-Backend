/*
 * Copyright (c) 2022 - 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
 * This source code and software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 * The software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this XSD.  If not, see http://www.gnu.org/licenses.
 * CONTACT: 		info@ptb.de
 * DEVELOPMENT:	https://d-si.ptb.de
 * AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
 * LAST MODIFIED:	15.08.23, 15:41
 */

package de.ptb.backend.model.dsi;

import lombok.Data;

import java.util.List;

@Data
public class SiExpandedUnc {
    Double uncertainty;
    int coverageFactor;
    Double coverageProbability;
    String distribution;

    public String toXMLString(){
        // check if values are available
        String uncValueString = this.uncertainty != null ? "<si:uncertainty>" + this.uncertainty + "</si:uncertainty>\n" : "";
        String covFactorString = this.coverageFactor != -1 ? "<si:coverageFactor>" + this.coverageFactor + "</si:coverageFactor>\n" : "";
        String covProbabilityString = this.coverageProbability != null ? "<si:coverageProbability>" + this.coverageProbability + "</si:coverageProbability>\n" : "";
        String distributionString = this.distribution != null ? "<si:distribution>" + this.distribution + "</si:distribution>\n" : "";

        String XMLstring =
                "<si:expandedUnc>\n" +
                uncValueString +
                covFactorString +
                covProbabilityString +
                distributionString +
                "</si:expandedUnc>\n";
        return XMLstring;
    }
//    public void setUncertainty(Double uncertainty) {
//        this.uncertainty = uncertainty;
//    }
//
//    public void setCoverageFactor(int coverageFactor) {
//        this.coverageFactor = coverageFactor;
//    }
//
//    public void setCoverageProbability(Double coverageProbability) {
//        this.coverageProbability = coverageProbability;
//    }

    /**
     * A SiExpandedUnc is a part of an SiReal containing its uncertainty, coverageFactor and coveragePossibility
     * @param uncertainty Double
     * @param coverageFactor Int
     * @param coverageProbability Double
     */
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

//    public Double getUncertainty() {
//        return uncertainty;
//    }
//
//    public int getCoverageFactor() {
//        return coverageFactor;
//    }
//
//    public Double getCoverageProbability() {
//        return coverageProbability;
//    }
}
