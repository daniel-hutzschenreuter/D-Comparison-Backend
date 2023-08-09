/*
Copyright (c) 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
This source code and software is free software: you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation, version 3 of the License.
The software is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public License
along with this XSD.  If not, see http://www.gnu.org/licenses.
CONTACT: 		info@ptb.de
DEVELOPMENT:	https://d-si.ptb.de
AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
LAST MODIFIED:	2023-08-09
*/
package de.ptb.backend.model.formula;

import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EEqualsMC2 {
    SiConstant c;
    List<SiReal> massSiReal = new ArrayList<>();

    public EEqualsMC2(SiConstant speedOfLight, List<SiReal> siReals) {
        this.c = speedOfLight;
        this.massSiReal = siReals;

    }
    public  EEqualsMC2(SiConstant speedOfLight){
        this.c = speedOfLight;
    }

    public SiReal calculate(SiReal massSiReal){
        Double value = massSiReal.getValue() * this.c.getValue()*this.c.getValue();
        String unit = "\\joule";
        String dateTime = LocalDateTime.now().toString();
        Double uncertainty = massSiReal.getExpUnc().getUncertainty();
        int coverageFactor = massSiReal.getExpUnc().getCoverageFactor();
        Double coverageProbability = massSiReal.getExpUnc().getCoverageProbability();

        return new SiReal(value, unit, dateTime, new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability));
    }

    public List<SiReal> calculate(){
        List<SiReal> newSiReals = new ArrayList<>();
        for (SiReal mass: this.massSiReal){
            newSiReals.add(this.calculate(mass));
        }
        return newSiReals;
    }
}
