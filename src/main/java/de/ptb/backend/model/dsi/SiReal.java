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
LAST MODIFIED:	2023-08-08
*/
package de.ptb.backend.model.dsi;

public class SiReal {
    Double value;
    String unit;
    String dateTime;
    SiExpandedUnc expUnc;
    Double massDifference = 0.0;
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public SiExpandedUnc getExpUnc() {
        return expUnc;
    }

    public void setExpUnc(SiExpandedUnc expUnc) {
        this.expUnc = expUnc;
    }

    public Double getMassDifference() {
        return massDifference;
    }

    public SiReal(Double value, String unit, String dateTime, SiExpandedUnc expUnc) {
        this.value = value;
        this.unit = unit;
        this.dateTime = dateTime;
        this.expUnc = expUnc;
    }

    public void manipulateValue(Double manipulator){
        this.massDifference=manipulator;
        this.value+=manipulator;
    }

    @Override
    public String toString() {
        return "SiReal{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", expUnc=" + expUnc.toString() +
                '}';
    }
}
