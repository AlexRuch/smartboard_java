package itmo.apkvt.ruchyov.smartboard.controller;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import itmo.apkvt.ruchyov.smartboard.service.ProjectService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*")
@RestController
public class SmartboardController {


    private final ProjectService projectService;

    @Autowired
    public SmartboardController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/api/project", method = RequestMethod.POST)
    public Project createProject(@RequestBody final String projectName) {
        return projectService.createProject(projectName);
    }

    @RequestMapping(value = "/api/project", method = RequestMethod.PUT)
    public List<Project> deleteProject(@RequestBody final String projectId) {
        projectService.deleteProject(Long.parseLong(projectId));
        return projectService.getAllProjects();
    }

    @RequestMapping(value = "/api/project/{projectId}", method = RequestMethod.GET)
    public Optional<Project> getProject(@PathVariable("projectId") final String projectId) {
        return projectService.getProject(Long.parseLong(projectId));
    }

    @RequestMapping(value = "/api/project/all", method = RequestMethod.GET)
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @RequestMapping(value = "/api/project/enable", method = RequestMethod.PUT)
    public void makeProjectEnabled(@RequestBody final String projectId) throws IOException {
        projectService.makeProjectEnabled(Long.parseLong(projectId));
        sendDataToClient();
    }

    @RequestMapping(value = "/api/project/update", method = RequestMethod.PUT)
    public void updateProjectName(@RequestParam("projectName") final String projectName,
                                  @RequestParam("projectId") final String projectId) {
        projectService.updateProject(projectName, Long.parseLong(projectId));
    }


    @RequestMapping(value = "/api/project/update/position", method = RequestMethod.PUT)
    public Project updateEntryPosition(@RequestBody final String payloadJSON) {
        JSONObject payload = new JSONObject(payloadJSON);
        return projectService.updateEntryPosition(payload.getLong("projectId")
                , payload.getLong("entryId")
                , payload.getString("changeType"));
    }

    @RequestMapping(value = "/api/entry/text", method = RequestMethod.POST)
    public Project createTextEntry(@RequestBody final String payloadJSON) {
        JSONObject payload = new JSONObject(payloadJSON);
        return projectService.addTextEntry(payload.getLong("projectId")
                , payload.getString("entryName")
                , payload.getString("entryText"));
    }

    @RequestMapping(value = "/api/entry/table", method = RequestMethod.POST)
    public Project createTableEntry(@RequestBody final String payloadJSON) {
        JSONObject payload = new JSONObject(payloadJSON);

        JSONArray jsonArray = payload.getJSONArray("tableRows");
        List<String> rowsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            rowsList.add(jsonArray.getString(i));
        }
        return projectService.addTableEntry(payload.getLong("projectId")
                , payload.getString("entryName")
                , payload.getString("tableCSS")
                , rowsList);
    }

    @RequestMapping(value = "/api/entry/image", method = RequestMethod.POST)
    public Project createImageEntry(@RequestParam("entryImage") final MultipartFile entryImage,
                                    @RequestParam("entryName") final String entryName,
                                    @RequestParam("projectId") final String projectId) throws IOException {
        return projectService.addImageEntry(Long.parseLong(projectId), entryName, entryImage);
    }

    @RequestMapping(value = "/api/entry/image", method = RequestMethod.PUT)
    public void updateImageEntry(@RequestParam("imageFile") final MultipartFile imageFile,
                                 @RequestParam("entryName") final String entryName,
                                 @RequestParam("projectId") final String projectId,
                                 @RequestParam("entryId") final String entryId) {

    }

    @RequestMapping(value = "/api/entry/text", method = RequestMethod.PUT)
    public void updateTextEntry(@RequestParam("entryText") final String entryText,
                                @RequestParam("entryName") final String entryName,
                                @RequestParam("projectId") final String projectId,
                                @RequestParam("entryId") final String entryId) {

    }

    @RequestMapping(value = "/api/entry/table", method = RequestMethod.PUT)
    public void updateTableEntry(@RequestParam("entryName") final String entryName,
                                 @RequestParam("tableCSS") final String tableCSS,
                                 @RequestParam("projectId") final String projectId,
                                 @RequestParam("entryId") final String entryId) {

    }

    @RequestMapping(value = "/api/entry", method = RequestMethod.PUT)
    public Project deleteEntry(@RequestBody final String entryId) {
        return projectService.deleteEntry(Long.parseLong(entryId));
    }

    @RequestMapping(value = "/api/entry/{entryId}", method = RequestMethod.GET)
    public Entry getEntry(@PathVariable("entryId") final String entryId) {
        return null;
    }

    private SseEmitter emitter;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @RequestMapping(value = "/live/update")
    public SseEmitter liveUpdate() throws IOException {
        System.out.println(emitters.size());
        emitter = new SseEmitter(0L);
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        sendDataToClient();
        return emitter;
    }


    private void sendDataToClient() throws IOException {
        System.out.println(projectService.getEnableProject());
        emitter.send(projectService.getEnableProject());
    }


}
