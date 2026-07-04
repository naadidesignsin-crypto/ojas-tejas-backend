package com.naadi.ojas.controller;

import com.naadi.ojas.dto.WorkshopResponse;
import com.naadi.ojas.service.WorkshopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public List<WorkshopResponse> getPublishedWorkshops() {
        return workshopService.getPublishedWorkshops();
    }

    @GetMapping("/{workshopId}")
    public WorkshopResponse getPublishedWorkshop(
            @PathVariable Long workshopId
    ) {
        return workshopService.getPublishedWorkshop(workshopId);
    }
}