package com.waglewagle.server.domain.timeTable.controller;

import com.waglewagle.server.domain.timeTable.dto.TimeTableDTO;
import com.waglewagle.server.global.apiPayload.ApiResponse;
import com.waglewagle.server.global.apiPayload.code.GeneralSuccessCode;
import com.waglewagle.server.global.apiPayload.dto.ListResponseDTO;
import com.waglewagle.server.global.security.userdetails.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/festivals")
public class TimeTableController implements TimeTableControllerDocs {
    @GetMapping("/{festivalId}/timetables")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ApiResponse<ListResponseDTO<TimeTableDTO.TimeTableInfo>> getTimeTables(
            @PathVariable Long festivalId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onListSuccess(GeneralSuccessCode.OK, null);
    }
}
