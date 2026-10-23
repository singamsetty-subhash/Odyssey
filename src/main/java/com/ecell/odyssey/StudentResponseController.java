package com.ecell.odyssey;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class StudentResponseController {

    private final StudentResponseRepository responseRepository;

    public StudentResponseController(StudentResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    @GetMapping({ "/student-response", "/response" })
    public String showResponsePage(
            @RequestParam(value = "submitted", required = false) String submitted,
            Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");

        List<StudentResponse> allResponses = responseRepository.findAllByOrderBySubmittedAtDesc();
        model.addAttribute("totalCount", allResponses.size());
        model.addAttribute("recentResponses", allResponses.stream().limit(12).toList());
        model.addAttribute("isSubmitted", submitted != null);
        return "student-response";
    }

    @PostMapping({ "/student-response", "/response" })
    public String handleResponseSubmission(
            @RequestParam(defaultValue = "") String teamName,
            @RequestParam(defaultValue = "") String leaderName,
            @RequestParam(defaultValue = "") String rollNumber,
            @RequestParam(defaultValue = "") String dept,
            @RequestParam(defaultValue = "") String otherDept,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String message,
            Model model,
            RedirectAttributes redirectAttributes) {

        String finalDept = dept.trim();
        if ("Other".equalsIgnoreCase(finalDept) && !otherDept.isBlank()) {
            finalDept = otherDept.trim();
        }

        boolean isValid = !teamName.isBlank() && !leaderName.isBlank() && !rollNumber.isBlank()
                && !finalDept.isBlank() && !email.isBlank() && !message.isBlank();

        if (!isValid) {
            model.addAttribute("eventName", "Odyssey");
            model.addAttribute("tagline", "Beyond the Horizon");
            model.addAttribute("errorMessage", "Please provide all required fields including Team Name, Leader Name, Roll Number, Department, Email, and your Event Message.");
            model.addAttribute("prevTeamName", teamName);
            model.addAttribute("prevLeaderName", leaderName);
            model.addAttribute("prevRollNumber", rollNumber);
            model.addAttribute("prevDept", dept);
            model.addAttribute("prevOtherDept", otherDept);
            model.addAttribute("prevEmail", email);
            model.addAttribute("prevMessage", message);

            List<StudentResponse> allResponses = responseRepository.findAllByOrderBySubmittedAtDesc();
            model.addAttribute("totalCount", allResponses.size());
            model.addAttribute("recentResponses", allResponses.stream().limit(12).toList());
            return "student-response";
        }

        StudentResponse responseObj = new StudentResponse(
                teamName.trim(),
                leaderName.trim(),
                rollNumber.trim().toUpperCase(),
                finalDept,
                email.trim(),
                message.trim(),
                LocalDateTime.now()
        );
        responseRepository.save(responseObj);

        redirectAttributes.addFlashAttribute("successMessage",
                "Thank you, Team " + teamName.trim() + "! Your message about Odyssey 2026 has been successfully recorded.");
        return "redirect:/student-response?submitted=true";
    }

    @GetMapping("/admin/responses")
    public String viewAdminResponses(HttpSession session, Model model) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login?redirect=/admin/responses";
        }
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        List<StudentResponse> list = responseRepository.findAllByOrderBySubmittedAtDesc();
        model.addAttribute("responses", list);
        model.addAttribute("totalCount", list.size());
        return "admin-responses";
    }

    @GetMapping("/admin/responses/export/csv")
    public void exportResponsesCsv(HttpSession session, HttpServletResponse response) throws IOException {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        List<StudentResponse> list = responseRepository.findAllByOrderBySubmittedAtDesc();
        String filename = "odyssey_student_responses_" + System.currentTimeMillis() + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // Write UTF-8 BOM for Microsoft Excel compatibility
        response.getOutputStream().write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });

        PrintWriter writer = new PrintWriter(response.getOutputStream(), true, StandardCharsets.UTF_8);
        writer.println("Response ID,Team Name,Leader Name,Roll Number,Department,Email,Message About Event,Submitted Timestamp");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (StudentResponse r : list) {
            String timeStr = r.getSubmittedAt() != null ? r.getSubmittedAt().format(dtf) : "";
            writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    escapeCsv(String.valueOf(r.getId())),
                    escapeCsv(r.getTeamName()),
                    escapeCsv(r.getLeaderName()),
                    escapeCsv(r.getRollNumber()),
                    escapeCsv(r.getDept()),
                    escapeCsv(r.getEmail()),
                    escapeCsv(r.getMessage()),
                    timeStr));
        }
        writer.flush();
    }

    @GetMapping("/api/student-responses")
    @ResponseBody
    public Object apiStudentResponses(HttpSession session) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return java.util.Collections.singletonMap("error", "Unauthorized. Please authenticate at /admin/login");
        }
        return responseRepository.findAllByOrderBySubmittedAtDesc();
    }

    private String escapeCsv(String input) {
        if (input == null) return "";
        return input.replace("\"", "\"\"").replace("\r\n", " ").replace("\n", " ");
    }
}
