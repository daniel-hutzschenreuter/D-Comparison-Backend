package de.ptb.backend.controller;

import de.ptb.backend.model.Participant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class JsonResponse {
    private String reportType;
    private String messageStatus;

    private Map<String, String> DKCReport;
    List<Participant> participants;

    public JsonResponse(String type, String status, Map<String, String> dkcrreport){
        reportType = type;
        messageStatus = status;
        DKCReport = dkcrreport;
    }
    @Override
    public String toString(){
        StringBuilder message = new StringBuilder("{ "+reportType+":\n\t{\n\t\tmessageStatus: "+ messageStatus +",\n\t\tDKCReport: \n\t\t{");
        int count=0;
        if(DKCReport != null){
            for(Map.Entry<String, String> entry : DKCReport.entrySet()){
                if(count==0){
                    message.append("\n\t\t\t").append(entry.getKey()).append(": ").append(entry.getValue());
                }else{
                    message.append(",\n\t\t\t").append(entry.getKey()).append(": ").append(entry.getValue());
                }
                count++;
            }
        }
        message.append("\n\t\t}\n\t}\n}");
        return message.toString();
    }

}
