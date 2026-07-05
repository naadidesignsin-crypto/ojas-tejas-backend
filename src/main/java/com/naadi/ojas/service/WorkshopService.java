package com.naadi.ojas.service;

import com.naadi.ojas.dto.WorkshopDateRequest;
import com.naadi.ojas.dto.WorkshopDateResponse;
import com.naadi.ojas.dto.WorkshopRequest;
import com.naadi.ojas.dto.WorkshopResponse;
import com.naadi.ojas.entity.Workshop;
import com.naadi.ojas.entity.WorkshopDate;
import com.naadi.ojas.repository.WorkshopDateRepository;
import com.naadi.ojas.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopDateRepository workshopDateRepository;

    public WorkshopResponse createWorkshop(WorkshopRequest request) {
        Workshop workshop = new Workshop();

        workshop.setTitle(request.getTitle().trim());
        workshop.setDescription(request.getDescription().trim());
        workshop.setAgeGroup(clean(request.getAgeGroup()));
        workshop.setLevel(clean(request.getLevel()));
        workshop.setPriceLabel(clean(request.getPriceLabel()));
        workshop.setImageData(clean(request.getImageData()));
        workshop.setPublished(false);

        Workshop savedWorkshop = workshopRepository.save(workshop);

        return toResponse(savedWorkshop, true);
    }

    public WorkshopResponse updateWorkshop(Long workshopId, WorkshopRequest request) {
        Workshop workshop = getWorkshopOrThrow(workshopId);

        workshop.setTitle(request.getTitle().trim());
        workshop.setDescription(request.getDescription().trim());
        workshop.setAgeGroup(clean(request.getAgeGroup()));
        workshop.setLevel(clean(request.getLevel()));
        workshop.setPriceLabel(clean(request.getPriceLabel()));
        workshop.setImageData(clean(request.getImageData()));

        return toResponse(workshop, true);
    }

    @Transactional(readOnly = true)
    public List<WorkshopResponse> getAdminWorkshops() {
        return workshopRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(workshop -> toResponse(workshop, true))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WorkshopResponse> getPublishedWorkshops() {
        return workshopRepository.findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(workshop -> toResponse(workshop, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkshopResponse getPublishedWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findByIdAndPublishedTrue(workshopId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Workshop not found"
                ));

        return toResponse(workshop, false);
    }

    public WorkshopResponse publishWorkshop(Long workshopId) {
        Workshop workshop = getWorkshopOrThrow(workshopId);
        workshop.setPublished(true);

        return toResponse(workshop, true);
    }

    public WorkshopResponse unpublishWorkshop(Long workshopId) {
        Workshop workshop = getWorkshopOrThrow(workshopId);
        workshop.setPublished(false);

        return toResponse(workshop, true);
    }

    public WorkshopResponse addWorkshopDate(Long workshopId, WorkshopDateRequest request) {
        validateWorkshopTime(request.getStartTime(), request.getEndTime());

        Workshop workshop = getWorkshopOrThrow(workshopId);

        WorkshopDate workshopDate = new WorkshopDate();
        workshopDate.setWorkshop(workshop);
        workshopDate.setWorkshopDate(request.getWorkshopDate());
        workshopDate.setStartTime(request.getStartTime());
        workshopDate.setEndTime(request.getEndTime());
        workshopDate.setAvailableSeats(request.getSeats());
        workshopDate.setMode(clean(request.getMode()));
        workshopDate.setMeetingLink(clean(request.getMeetingLink()));

        workshopDateRepository.save(workshopDate);

        workshop.getDates().add(workshopDate);

        return toResponse(workshop, true);
    }

    public void deleteWorkshopDate(Long dateId) {
        WorkshopDate workshopDate = workshopDateRepository.findById(dateId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Workshop date not found"
                ));

        workshopDateRepository.delete(workshopDate);
    }

    private Workshop getWorkshopOrThrow(Long workshopId) {
        return workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Workshop not found"
                ));
    }

    private void validateWorkshopTime(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return;
        }

        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End time must be after start time"
            );
        }
    }

    private WorkshopResponse toResponse(Workshop workshop, boolean includeMeetingLink) {
        List<WorkshopDateResponse> dateResponses = workshop.getDates()
                .stream()
                .sorted(
                        Comparator.comparing(WorkshopDate::getWorkshopDate)
                                .thenComparing(WorkshopDate::getStartTime)
                )
                .map(date -> toDateResponse(date, includeMeetingLink))
                .toList();

        return WorkshopResponse.builder()
                .id(workshop.getId())
                .title(workshop.getTitle())
                .description(workshop.getDescription())
                .ageGroup(workshop.getAgeGroup())
                .level(workshop.getLevel())
                .priceLabel(workshop.getPriceLabel())
                .imageData(workshop.getImageData())
                .published(workshop.getPublished())
                .createdAt(workshop.getCreatedAt())
                .dates(dateResponses)
                .build();
    }

    private WorkshopDateResponse toDateResponse(
            WorkshopDate date,
            boolean includeMeetingLink
    ) {
        return WorkshopDateResponse.builder()
                .id(date.getId())
                .workshopDate(date.getWorkshopDate())
                .startTime(date.getStartTime())
                .endTime(date.getEndTime())
                .seats(date.getAvailableSeats())
                .mode(date.getMode())
                .meetingLink(includeMeetingLink ? date.getMeetingLink() : null)
                .build();
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}