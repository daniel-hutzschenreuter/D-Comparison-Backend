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
package de.ptb.backend.model;

public class DKCRErrorMessage {
    String errorMessage;

    public  DKCRErrorMessage(String message){
        this.errorMessage = message;
    }
    @Override
    public String toString() {
        return "DKCRResponseMessage{" +
                "Message='" + this.errorMessage +'\'' +
                '}';
    }
}
