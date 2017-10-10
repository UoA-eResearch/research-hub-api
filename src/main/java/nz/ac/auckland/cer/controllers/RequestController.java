package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.VMConsultation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.POST, value = "/vmConsultation/create")
    String createVMConsultationRequest(@RequestBody VMConsultation vmConsultation) {
        // Create ServiceNow VM Consultation ticket
        String output = "";
        try {
            String autoCerDir = System.getenv("AUTOCER_DIR");
            String command = String.format("python3 %s/bin/create_vm_consultation_sn_ticket.py -req %s -res %s -d %s -t %s -l %s -c '%s'",
                    autoCerDir, vmConsultation.getRequestorUpi(), vmConsultation.getResearcherUpi(), vmConsultation.getDate(),
                    vmConsultation.getTime(), vmConsultation.getLocation(), vmConsultation.getComments());
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
