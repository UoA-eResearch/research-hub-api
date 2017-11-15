package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.requests.DataConsultation;
import nz.ac.auckland.cer.model.requests.VMConsultation;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;


@RestController
public class RequestController {

    private static String exec(String command) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream);
        defaultExecutor.setStreamHandler(pumpStreamHandler);
        defaultExecutor.execute(commandline);
        return (byteArrayOutputStream.toString());
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/vmConsultation/create")
    String createVMConsultationRequest(@RequestBody VMConsultation vmConsultation) {
        // Create ServiceNow VM Consultation ticket
        String output = "";
        try {
            String autoCerDir = System.getenv("AUTOCER_DIR");
            String requestorUpi = "jdip004"; // TODO replace with upi from Shiboleth headers
            String command = String.format("python3 %s/bin/create_vm_consultation_sn_ticket.py -r %s -d %s -t %s -c '%s'",
                    autoCerDir, requestorUpi, vmConsultation.getDate(),
                    vmConsultation.getTime(), vmConsultation.getComments());
            output = exec(command);
        } catch (Exception e) {
            String cause = e.getMessage();
            if (cause.equals("bash: python3: command not found")) {
                System.out.println("Error: No python interpreter found.");
            }
        }

        return output;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/dataConsultation/create")
    String createDataConsultationRequest(@RequestBody DataConsultation dataConsultation) {
        // Create ServiceNow Data Consultation ticket
        String output = "";
        try {
            String autoCerDir = System.getenv("AUTOCER_DIR");
            String command = String.format("python3 %s/bin/create_data_consultation_sn_ticket.py --requestor_upi %s --researcher_upi %s --new_or_existing_storage %s --project_title \"%s\" --project_abstract \"%s\" --project_end_date %s --field_of_research \"%s\" --requirements \"%s\" --short_name %s --upis %s --access_rights %s --data_owner %s --data_contact %s --data_this_year %s --data_next_year %s",
                    autoCerDir, dataConsultation.getRequestorUpi(), dataConsultation.getResearcherUpi(),
                    dataConsultation.getNewOrExistingStorage(), dataConsultation.getProjectTitle(),
                    dataConsultation.getProjectAbstract(), dataConsultation.getProjectEndDate(),
                    dataConsultation.getFieldOfResearch(), dataConsultation.getRequirements(), dataConsultation.getShortName(),
                    String.join(",", dataConsultation.getUpis()), String.join(",", dataConsultation.getAccessRights()),
                    dataConsultation.getDataOwner(), dataConsultation.getDataContact(), dataConsultation.getDataThisYear(),
                    dataConsultation.getDataNextYear());
            output = exec(command);
        } catch (Exception e) {
            String cause = e.getMessage();
            if (cause.equals("bash: python3: command not found")) {
                System.out.println("Error: No python interpreter found.");
            }
        }

        return output;
    }

}
