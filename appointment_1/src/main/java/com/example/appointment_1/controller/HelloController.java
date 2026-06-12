package com.example.appointment_1.controller;

import com.example.appointment_1.entity.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.example.appointment_1.entity.Appointment;
import com.example.appointment_1.repository.AppointmentRepository;
import com.example.appointment_1.dto.PageResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RestController

// ✅ MAIN APPOINTMENT ROUTE
@RequestMapping("/appointments")

@CrossOrigin(
    origins = "http://localhost:3000",
    methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE
    }
)

public class HelloController {

    @Autowired
    private AppointmentRepository repo;

    // =========================
    // ✅ CREATE APPOINTMENT
    // =========================

    @PostMapping
    public ResponseEntity<?> save(

            @Valid
            @RequestBody
            Appointment appointment
    ) {

        // 🚫 PREVENT DUPLICATE SLOT

        if (

            repo.existsByDoctorAndDateAndTime(

                appointment.getDoctor(),

                appointment.getDate(),

                appointment.getTime()
            )
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(

                        "Slot already booked for "

                        + appointment.getDoctor()

                        + ". Suggested slot: 11:00 AM"
                    );
        }

        // =========================
        // 🤖 AI PRIORITY LOGIC
        // =========================

        if (
            appointment.getSymptoms()
            != null
        ) {

            String symptoms =

                    appointment
                    .getSymptoms()
                    .toLowerCase();

            // 🚨 EMERGENCY

            if (

                symptoms.contains("chest")

                ||

                symptoms.contains("breathing")

                ||

                symptoms.contains("heart")
            ) {

                appointment.setPriority(
                        "Emergency"
                );

                appointment.setWaitTime(5);

            }

            // ✅ NORMAL

            else {

                appointment.setPriority(
                        "Normal"
                );

                appointment.setWaitTime(15);
            }
        }

        Appointment saved =
                repo.save(appointment);

        return ResponseEntity
                .status(201)
                .body(saved);
    }

    // =========================
    // ✅ GET ALL (PAGINATION)
    // =========================

    @GetMapping
    public ResponseEntity<PageResponse<Appointment>>
    getAll(

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            int page,
            

            @RequestParam(
                    name = "size",
                    defaultValue = "5"
            )
            int size,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = "id"
            )
            String sortBy,

            @RequestParam(
                    name = "direction",
                    defaultValue = "asc"
            )
            String direction
    ) {

        Sort sort =

                direction.equalsIgnoreCase(
                        "desc"
                )

                ?

                Sort.by(sortBy).descending()

                :

                Sort.by(sortBy).ascending();

        Page<Appointment> result =

                repo.findAll(

                        PageRequest.of(
                                page,
                                size,
                                sort
                        )
                );

        PageResponse<Appointment> response =

                new PageResponse<>(

                        result.getContent(),

                        result.getNumber(),

                        result.getTotalPages(),

                        result.getTotalElements()
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ✅ GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Appointment>
    getById(

            @PathVariable("id")
            Long id
    ) {

        return repo.findById(id)

                .map(ResponseEntity::ok)

                .orElse(

                    ResponseEntity
                    .notFound()
                    .build()
                );
    }

    // =========================
    // ✅ SEARCH BY NAME
    // =========================

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(

            @RequestParam("name")
            String name
    ) {

        return ResponseEntity.ok(

                repo.findByNameContainingIgnoreCase(
                        name
                )
        );
    }

    // =========================
    // ✅ FILTER BY DATE
    // =========================

    @GetMapping("/date")
    public ResponseEntity<?> getByDate(

            @RequestParam("date")
            String date
    ) {

        return ResponseEntity.ok(

                repo.findByDate(date)
        );
    }

    // =========================
    // ✅ FILTER BY DOCTOR
    // =========================

    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>>
    getByDoctor(

            @RequestParam("doctor")
            String doctor
    ) {

        return ResponseEntity.ok(

                repo.findByDoctor(doctor)
        );
    }

    // =========================
    // ✅ FILTER BY PRIORITY
    // =========================

    @GetMapping("/priority")
    public ResponseEntity<List<Appointment>>
    getByPriority(

            @RequestParam("priority")
            String priority
    ) {

        return ResponseEntity.ok(

                repo.findByPriority(priority)
        );
    }

    // =========================
    // ✅ UPDATE APPOINTMENT
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<?> update(

            @PathVariable("id")
            Long id,

            @Valid
            @RequestBody
            Appointment updated
    ) {

        Optional<Appointment> existing =

                repo.findById(id);

        if (existing.isPresent()) {

            Appointment appt =
                    existing.get();

            // 🚫 DUPLICATE SLOT CHECK

            boolean isDuplicate =

                    repo.existsByDoctorAndDateAndTime(

                            updated.getDoctor(),

                            updated.getDate(),

                            updated.getTime()
                    )

                    && !(

                        appt.getDoctor().equals(
                                updated.getDoctor()
                        )

                        &&

                        appt.getDate().equals(
                                updated.getDate()
                        )

                        &&

                        appt.getTime().equals(
                                updated.getTime()
                        )
                    );

            if (isDuplicate) {

                return ResponseEntity
                        .badRequest()
                        .body(

                            "Doctor already has appointment at this slot"
                        );
            }

            // ✅ UPDATE FIELDS

            appt.setName(updated.getName());

            appt.setDate(updated.getDate());

            appt.setTime(updated.getTime());

            appt.setDoctor(updated.getDoctor());

            appt.setSpecialization(
                    updated.getSpecialization()
            );

            appt.setSymptoms(
                    updated.getSymptoms()
            );

            appt.setPriority(
                    updated.getPriority()
            );

            appt.setWaitTime(
                    updated.getWaitTime()
            );

            return ResponseEntity.ok(

                    repo.save(appt)
            );
        }

        else {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    // =========================
    // ✅ DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(

            @PathVariable("id")
            Long id
    ) {

        if (!repo.existsById(id)) {

            return ResponseEntity
                    .status(404)
                    .body(
                            "Appointment not found"
                    );
        }

        repo.deleteById(id);

        return ResponseEntity.ok(
                "Deleted successfully"
        );
    }
}