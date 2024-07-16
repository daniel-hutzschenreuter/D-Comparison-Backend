package de.ptb.backend.model.dsi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DccName {
    String refType;
    String content;
    String lang;

    public DccName(String content, String lang){
        this.content = content;
        this.lang = lang;
    }

    public String toXMLString(){
        String langString = lang != null ?  " lang=\"" + lang + "\"" : "";

        String XMLString = "<dcc:name>\n" +
                "<dcc:content"+ langString + ">" + this.content + "</dcc:content>\n" +
                "</dcc:name>\n";

        return XMLString;
    }
}
