package com.blaqueyard.afro.controller;

import com.blaqueyard.afro.payload.UploadFileResponse;
import com.blaqueyard.afro.service.FileStorageService;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectFacesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import jdk.nashorn.internal.parser.JSONParser;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @CrossOrigin
    @RequestMapping(value = "recog", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String,String>> newFile(
            @RequestParam(value = "incomingFileName") String incomingFileName) throws IOException, ParseException {

        Map<String, String> response = new HashMap<String, String>();

        System.out.print("### ######################### ###\n");
        System.out.print("# ** hello  testing ibm AI   ** #\n");
        System.out.print("### ######################### ###\n");

        IamOptions options = new IamOptions.Builder()
                .apiKey("remove")
                .build();

        VisualRecognition service = new VisualRecognition("2018-03-19", options);

        DetectFacesOptions detectFacesOptions = new DetectFacesOptions.Builder()
                .imagesFile(new File(incomingFileName))
                .build();
        DetectedFaces result = service.detectFaces(detectFacesOptions).execute();
        System.out.println(result);

        String st = result.toString();

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(st);

        Long ip = (Long) json.get("images_processed");
        System.out.println(ip);

        // loop array
        JSONArray msg = (JSONArray) json.get("images");

        System.out.println("Array size :: "+msg.size());

        if(msg.size() == 1){
            // iterate via "New way to loop"
            System.out.println("\n==> Advance For Loop ..");
            for (Object temp : msg) {
//            System.out.println(temp);
                JSONParser parsertwo = new JSONParser();
                JSONObject jsontwo = (JSONObject) parsertwo.parse(temp.toString());

                JSONArray tmp = (JSONArray) jsontwo.get("faces");

                for (Object levelTwo : tmp){
                    System.out.println("l2 "+ levelTwo);

                    String ageObj = ((JSONObject)levelTwo).get("age").toString();
                    JSONObject obj;
                    obj = (JSONObject) parser.parse(ageObj);
                    String min_age = ((JSONObject)obj).get("min").toString();
                    String max_age = ((JSONObject)obj).get("min").toString();


                    String genderObj = ((JSONObject)levelTwo).get("gender").toString();
                    JSONObject ob;
                    ob = (JSONObject) parser.parse(genderObj);
                    String gender_label = ((JSONObject)ob).get("gender_label").toString();
                    String score = ((JSONObject)ob).get("score").toString();


                    response.put("code", "0");
                    response.put("msg", "success");
                    response.put("image", incomingFileName);
                    response.put("min-age", min_age);
                    response.put("max_age", max_age);
                    response.put("gender_label", gender_label);
                    response.put("score", score);

                }
//            response.put("res", (String) temp);
            }
        }else {
            //write to file
            try (FileWriter file = new FileWriter("fred.json")) {
                file.write(result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.print("###############################\n");
            System.out.print("###        end the AI       ###\n");
            System.out.print("###############################\n");

            response.put("code", "0");
            response.put("image", incomingFileName);
            response.put("mg", "success");

        }
        return ResponseEntity.ok().body(response);
    }

    }
