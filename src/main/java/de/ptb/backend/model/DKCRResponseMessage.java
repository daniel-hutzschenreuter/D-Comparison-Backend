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
package de.ptb.backend.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class DKCRResponseMessage {
    String fileName;
    String base64String;

    /**
     * This class contains the information of a succesfull response of the server which will be attached as response in BackendController.java
     * @param fileName String
     * @param file File
     * @throws IOException
     */
    public DKCRResponseMessage(String fileName, File file) throws IOException {
        this.fileName = fileName+ ".xml";
        byte[] fileContent = Files.readAllBytes(file.toPath());
        this.base64String = Base64.getEncoder().encodeToString(fileContent);
    }

    public void setBase64String(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        this.base64String = Base64.getEncoder().encodeToString(fileContent);
    }

    @Override
    public String toString() {
        return "DKCRResponseMessage{" +
                "fileName='" + fileName +'\'' +
                ", base64String='" + base64String + '\'' +
                '}';
    }
}
