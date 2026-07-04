package com.naadi.ojas.controller;

import com.naadi.ojas.dto.WorkshopDateRequest;
import com.naadi.ojas.dto.WorkshopRequest;
import com.naadi.ojas.dto.WorkshopResponse;
import com.naadi.ojas.service.WorkshopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/workshops")
@RequiredArgsConstructor
public class AdminWorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public List<WorkshopResponse> getAdminWorkshops() {
        return workshopService.getAdminWorkshops();
    }

    @PostMapping
    public WorkshopResponse createWorkshop(
            @Valid @RequestBody WorkshopRequest request
    ) {
        return workshopService.createWorkshop(request);
    }

    @PutMapping("/{workshopId}")
    public WorkshopResponse updateWorkshop(
            @PathVariable Long workshopId,
            @Valid @RequestBody WorkshopRequest request
    ) {
        return workshopService.updateWorkshop(workshopId, request);
    }

    @PatchMapping("/{workshopId}/publish")
    public WorkshopResponse publishWorkshop(
            @PathVariable Long workshopId
    ) {
        return workshopService.publishWorkshop(workshopId);
    }

    @PatchMapping("/{workshopId}/unpublish")
    public WorkshopResponse unpublishWorkshop(
            @PathVariable Long workshopId
    ) {
        return workshopService.unpublishWorkshop(workshopId);
    }

    @PostMapping("/{workshopId}/dates")
    public WorkshopResponse addWorkshopDate(
            @PathVariable Long workshopId,
            @Valid @RequestBody WorkshopDateRequest request
    ) {
        return workshopService.addWorkshopDate(workshopId, request);
    }

    @DeleteMapping("/dates/{dateId}")
    public Map<String, String> deleteWorkshopDate(
            @PathVariable Long dateId
    ) {
        workshopService.deleteWorkshopDate(dateId);

        return Map.of(
                "status", "success",
                "message", "Workshop date deleted"
        );
    }
}