package com.annztech.rewardsystem.modules.invite.controller;

import com.annztech.rewardsystem.common.dto.AppResponse;
import com.annztech.rewardsystem.common.service.LocalizationService;
import com.annztech.rewardsystem.external.email.service.EmailService;
import com.annztech.rewardsystem.modules.invite.constants.InviteConstants;
import com.annztech.rewardsystem.modules.invite.dto.InviteDTO;
import com.annztech.rewardsystem.modules.invite.dto.InviteUpdateDTO;
import com.annztech.rewardsystem.modules.invite.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/invites")
@Tag(name = "Invites", description = "User Invite Management")
public class InviteController extends LocalizationService {
    private final InviteService inviteService;
    private final EmailService zeptoMailService;
    public InviteController(InviteService inviteService, EmailService zeptoMailService) {
        this.inviteService = inviteService;
        this.zeptoMailService = zeptoMailService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getInvite(@PathVariable String id) {
        return AppResponse.success(getMessage(InviteConstants.INVITE_FETCHED), HttpStatus.OK, inviteService.getInviteDTOById(id));
    }

    @Operation(summary = "Get all invites")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetched all invites successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InviteDTO.class))
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<Object> getAllInvite() {
        return AppResponse.success(getMessage(InviteConstants.INVITES_FETCHED), HttpStatus.OK, inviteService.getAllInviteDTO());
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateInvite(@PathVariable String id, @RequestBody InviteUpdateDTO dto) {
        return AppResponse.success(getMessage(InviteConstants.INVITE_UPDATED), HttpStatus.OK, inviteService.updateInviteStatus(id, dto));
    }

    @PatchMapping("/{id}/resend")
    public ResponseEntity<Object> resendUpdate(@PathVariable String id){
        return AppResponse.success(getMessage(InviteConstants.INVITE_RESENT), HttpStatus.OK, inviteService.resendUpdateStatus(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteInvite(@PathVariable String id) {
        return AppResponse.success(getMessage(InviteConstants.INVITE_DELETED), HttpStatus.OK, inviteService.deleteInvite(id));
    }

}
