package com.annztech.rewardsystem.modules.job.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.openApi.StandardApiResponses;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.modules.job.constants.JobConstant;
import com.annztech.rewardsystem.modules.job.dto.JobCreateDTO;
import com.annztech.rewardsystem.modules.job.dto.JobDTO;
import com.annztech.rewardsystem.modules.job.dto.JobUpdateDTO;
import com.annztech.rewardsystem.modules.job.service.JobService;
import com.annztech.rewardsystem.modules.job.service.JobStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/job-title")
@RestController
@Tag(name = "Internal Job Tile", description = "Internal Job Title Management")
public class JobController extends LocalizationService {
    private final JobService jobService;
    private final JobStateService jobStateService;
    public JobController(JobService jobService, JobStateService jobStateService) {
        this.jobService = jobService;
        this.jobStateService = jobStateService;
    }

    @GetMapping
    @Operation(
            summary = "Get all jon titles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns all job titles",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = JobDTO.class))
                    )
            ),
    })
    public ResponseEntity<Object> getAllJobs() {
        return AppResponse.success(getMessage(JobConstant.JOB_FETCHED), HttpStatus.OK, jobService.getAllJobDTO());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Job Title by ID",
            description = "Returns a Job title by its unique ID"
    )
    @Parameter(description = "Job Title ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> getJob(@PathVariable String id) {
        return AppResponse.success(getMessage(JobConstant.JOB_FETCHED), HttpStatus.OK, jobService.getJobDTOById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new job title")
    @StandardApiResponses
    public ResponseEntity<Object> createJob(@Valid @RequestBody JobCreateDTO job) {
        return AppResponse.success(getMessage(JobConstant.JOB_CREATED), HttpStatus.CREATED, jobService.createJobDTO(job));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a job title")
    @Parameter(description = "job title ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> updateJob(@PathVariable String id, @RequestBody JobUpdateDTO job) {
        return AppResponse.success(getMessage(JobConstant.JOB_UPDATED), HttpStatus.CREATED, jobService.updateJobDTO(id, job));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a job title")
    @Parameter(description = "job title ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @StandardApiResponses
    public ResponseEntity<Object> deleteJob(@PathVariable String id) {
        return AppResponse.success(getMessage(JobConstant.JOB_DELETED), HttpStatus.CREATED, jobService.deleteJobDTO(id));
    }

    @GetMapping("/search")
    @Operation(summary = "search by job names")
    @StandardApiResponses
    public ResponseEntity<Object> searchJob(@RequestParam("q") String query) {
        return AppResponse.success(getMessage(JobConstant.JOB_FETCHED), HttpStatus.OK, jobService.searchJobDTO(query));
    }

    @PatchMapping("/{jobId}/deactivate")
    public ResponseEntity<Object> deactivateInternalDepartment(@PathVariable String jobId){
        return AppResponse.success(getMessage(JobConstant.JOB_DEACTIVATED), HttpStatus.OK, jobStateService.deactivateJob(jobId));
    }

    @PatchMapping("/{jobId}/activate")
    public ResponseEntity<Object> activateInternalDepartment(@PathVariable String jobId){
        return AppResponse.success(getMessage(JobConstant.JOB_ACTIVATED), HttpStatus.OK, jobStateService.activateJob(jobId));
    }

}
