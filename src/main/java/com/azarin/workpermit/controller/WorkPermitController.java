/*
Обрабатывает HTTP-запросы от пользователя (браузера)
Состоит из:

@GetMapping("/permits") — список нарядов
@GetMapping("/permits/create") — форма создания
@PostMapping("/permits") — сохранение наряда и участников

Функции:
Принимает данные с формы
Запускает сохранение в сервисе
Возвращает HTML-страницы через Thymeleaf
 */
package com.azarin.workpermit.controller;

import com.azarin.workpermit.model.PermitParticipant;
import com.azarin.workpermit.model.WorkPermit;
import com.azarin.workpermit.repository.WorkPermitRepository;
import com.azarin.workpermit.service.PdfGeneratorService;
import com.azarin.workpermit.service.WorkPermitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WorkPermitController {

    private final WorkPermitService service;

    public WorkPermitController(WorkPermitService service) {
        this.service = service;
    }

    @GetMapping("/permits")
    public String listPermits(Model model) {
        model.addAttribute("permits", service.getAllPermits());
        return "permits/list";
    }
    @GetMapping("/permits/create")
    public String showCreateForm(Model model) {
        model.addAttribute("permit", new WorkPermit());
        model.addAttribute("participant", new PermitParticipant());
        return "permits/create";


    }
    @PostMapping("/permits")
    public String savePermit(
            @ModelAttribute("permit") WorkPermit permit,
            @RequestParam Map<String, String> formData
    ) {
        permit.setCreatedAt(LocalDateTime.now());

        // Собираем участников вручную
        List<PermitParticipant> participants = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String name = formData.get("participants[" + i + "].fullName");
            String pos = formData.get("participants[" + i + "].position");
            String role = formData.get("participants[" + i + "].role");

            if (name != null && !name.isBlank()) {
                PermitParticipant p = PermitParticipant.builder()
                        .fullName(name)
                        .position(pos)
                        .role(role)
                        .workPermit(permit)
                        .build();
                participants.add(p);
            }
        }
       // System.out.println("=== УЧАСТНИКИ ===");
        participants.forEach(p -> System.out.println(p.getFullName() + " | " + p.getRole()));


        permit.setParticipants(participants);
       // System.out.println("=== СОХРАНЯЕМ PERMIT ===");
       // System.out.println(permit.getNumber() + ", участников: " + permit.getParticipants().size());

        service.savePermit(permit);

        return "redirect:/permits";
    }
    @GetMapping("/permits/{id}")
    public String viewPermit(@PathVariable Long id, Model model) {
        WorkPermit permit = service.getPermitById(id)
                .orElseThrow(() -> new IllegalArgumentException("Наряд не найден, id: " + id));
        model.addAttribute("permit", permit);
        return "permits/view";
    }
    @GetMapping("/permits/{id}/print")
    public String printPermit(@PathVariable Long id, Model model) {
        WorkPermit permit = service.getPermitById(id)
                .orElseThrow(() -> new IllegalArgumentException("Наряд не найден, id: " + id));
        model.addAttribute("permit", permit);

        return switch (permit.getType()) {
            case "F01" -> "permits/printF01";
            case "F02" -> "permits/printF02";
            default -> "permits/printF01";
        };
    }
    @Autowired
    private WorkPermitRepository workPermitRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping("/permits/{id}/print/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        WorkPermit permit = workPermitRepository.findById(id).orElse(null);
        if (permit == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] pdf = pdfGeneratorService.generatePermitHeader(permit);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "work_permit_" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
