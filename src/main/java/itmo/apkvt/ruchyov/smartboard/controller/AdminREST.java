package itmo.apkvt.ruchyov.smartboard.controller;

import itmo.apkvt.ruchyov.smartboard.entity.Entry;
import itmo.apkvt.ruchyov.smartboard.entity.Project;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AdminREST {

    @RequestMapping(value = "/api/project", method = RequestMethod.POST)
    public void createProject(@RequestBody final String projectName) {
    }

    @RequestMapping(value = "/api/project", method = RequestMethod.DELETE)
    public void deleteProject(@RequestBody final String projectId) {
    }

    @RequestMapping(value = "/api/project/{projectId}", method = RequestMethod.GET)
    public Project getProject(@PathVariable("projectId") final String projectId) {
        return null;
    }

    @RequestMapping(value = "/api/project/all", method = RequestMethod.GET)
    public List<Project> getAllProjects() {
        return null;
    }

    @RequestMapping(value = "/api/project/enable", method = RequestMethod.PUT)
    public void makeProjectEnabled(@RequestBody final String projectId) {
    }

    @RequestMapping(value = "/api/project/update", method = RequestMethod.PUT)
    public void updateProject(@RequestParam("projectName") final String projectName,
                              @RequestParam("projectId") final String projectId) {
    }

    @RequestMapping(value = "/api/entry/image", method = RequestMethod.POST)
    public void createImageEntry(@RequestParam("imageFile") final MultipartFile imageFile,
                                 @RequestParam("entryName") final String entryName,
                                 @RequestParam("projectId") final String projectId) {

    }

    @RequestMapping(value = "/api/entry/text", method = RequestMethod.POST)
    public void createTextEntry(@RequestParam("entryText") final String entryText,
                                @RequestParam("entryName") final String entryName,
                                @RequestParam("projectId") final String projectId) {

    }

    @RequestMapping(value = "/api/entry/table", method = RequestMethod.POST)
    public void createTableEntry(@RequestParam("entryName") final String entryName,
                                 @RequestParam("tableCSS") final String tableCSS,
                                 @RequestParam("projectId") final String projectId) {

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

    @RequestMapping(value = "/api/entry", method = RequestMethod.DELETE)
    public void deleteEntry(@RequestBody final String entryId) {

    }

    @RequestMapping(value = "/api/entry/{entryId}", method = RequestMethod.GET)
    public Entry getEntry(@PathVariable("entryId") final String entryId) {
        return null;
    }


}
