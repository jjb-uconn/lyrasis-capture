package us.jbec.lct.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.jbec.lct.models.ImageJob;
import us.jbec.lct.services.CloudCaptureDocumentService;
import us.jbec.lct.util.LCToolUtils;

import java.io.IOException;

/**
 * Controller for interacting with image jobs
 */
@RestController
public class JobController {

    Logger LOG = LoggerFactory.getLogger(JobController.class);

    private final CloudCaptureDocumentService cloudCaptureDocumentService;

    /**
     * Controller for interacting with image jobs
     * @param cloudCaptureDocumentService autowired parameter
     */
    public JobController(CloudCaptureDocumentService cloudCaptureDocumentService) {
        this.cloudCaptureDocumentService = cloudCaptureDocumentService;
    }

    /**
     * Retrieves an image job by uuid
     * @param uuid uuid of image job to retrieve
     * @return retrieved image job
     * @throws JsonProcessingException
     */
    @GetMapping(value = "/getJob")
    public @ResponseBody ImageJob getJob(@RequestParam String uuid) throws JsonProcessingException {
        LOG.info("Received request for job: {}", uuid);
        try {
            return cloudCaptureDocumentService.getImageJobByUuid(uuid);
        } catch (Exception e) {
            LOG.error("An error occurred while getting image job!", e);
            throw e;
        }
    }

    /**
     * Saves an image job
     * @param authentication authentication object
     * @param imageJob image job to save
     * @throws IOException
     */
    @PostMapping(value = "/sec/api/saveJob", consumes= { "application/json" })
    public @ResponseBody void saveJob(Authentication authentication,  @RequestBody ImageJob imageJob) throws IOException {
        var user = LCToolUtils.getUserFromAuthentication(authentication);
        LOG.info("Received request to save job.");
        try {
            cloudCaptureDocumentService.saveCloudCaptureDocument(user.getFirebaseIdentifier(), imageJob);
        } catch (Exception e) {
            LOG.error("An error occurred while saving image job!", e);
            throw e;
        }
    }

}
