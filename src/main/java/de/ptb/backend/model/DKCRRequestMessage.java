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
 * LAST MODIFIED:	15.08.23, 12:55
 */

package de.ptb.backend.model;

import lombok.Data;

import java.util.List;
@Data
public class DKCRRequestMessage {
    String pidReport;
    List<Participant> participantList;

    /**
     * This class packages the important information that comes from the payload so that it can be processed by other functions.
     * @param pidReport String
     * @param participantList List<Participant>
     */
    public DKCRRequestMessage(String pidReport, List<Participant> participantList) {
        this.pidReport = pidReport;
        this.participantList = participantList;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

}
