package com.ecell.odyssey;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper;
    private final String adminUsername;
    private final String adminPassword;

    public RegistrationController(RegistrationService registrationService, ObjectMapper objectMapper,
            @Value("${odyssey.admin.username:admin}") String adminUsername,
            @Value("${odyssey.admin.password:admin123}") String adminPassword) {
        this.registrationService = registrationService;
        this.objectMapper = objectMapper;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("teamSize", 3);
        model.addAttribute("amountPaid", 300);
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(
            @RequestParam(defaultValue = "") String teamName,
            @RequestParam(defaultValue = "") String leaderName,
            @RequestParam(defaultValue = "") String leaderEmail,
            @RequestParam(defaultValue = "") String leaderPhone,
            @RequestParam(defaultValue = "") String rollNumber,
            @RequestParam(defaultValue = "") String dept,
            @RequestParam(defaultValue = "") String otherDept,
            @RequestParam(defaultValue = "") String year,
            @RequestParam(defaultValue = "") String section,
            @RequestParam(defaultValue = "KKR and KSR institute of Technology and Sciences(KITS)") String college,
            @RequestParam(defaultValue = "3") int teamSize,
            @RequestParam(defaultValue = "") String utr,
            @RequestParam(value = "paymentImage", required = false) MultipartFile paymentImage,
            @RequestParam(defaultValue = "") String member2Name,
            @RequestParam(defaultValue = "") String member2Email,
            @RequestParam(defaultValue = "") String member2Phone,
            @RequestParam(defaultValue = "") String member2RollNumber,
            @RequestParam(defaultValue = "") String member3Name,
            @RequestParam(defaultValue = "") String member3Email,
            @RequestParam(defaultValue = "") String member3Phone,
            @RequestParam(defaultValue = "") String member3RollNumber,
            @RequestParam(defaultValue = "") String member4Name,
            @RequestParam(defaultValue = "") String member4Email,
            @RequestParam(defaultValue = "") String member4Phone,
            @RequestParam(defaultValue = "") String member4RollNumber,
            @RequestParam(defaultValue = "") String member5Name,
            @RequestParam(defaultValue = "") String member5Email,
            @RequestParam(defaultValue = "") String member5Phone,
            @RequestParam(defaultValue = "") String member5RollNumber,
            Model model,
            RedirectAttributes redirectAttributes) {

        int safeTeamSize = Math.max(3, Math.min(5, teamSize));

        String finalTeamName = (teamName != null && !teamName.isBlank()) ? teamName.trim() : (leaderName.trim() + "'s Group");
        String finalDept = dept.trim();
        if ("Other".equalsIgnoreCase(finalDept) && !otherDept.isBlank()) {
            finalDept = otherDept.trim();
        }

        // 1. Check for duplicate team name
        if (registrationService.isTeamNameTaken(finalTeamName)) {
            model.addAttribute("errorMessage",
                    "The Team Name '" + finalTeamName + "' is already registered in the database. Please choose a different unique Team Name.");
            model.addAttribute("eventName", "Odyssey");
            model.addAttribute("tagline", "Beyond the Horizon");
            preserveRegistrationAttributes(model, teamName, leaderName, leaderEmail, leaderPhone, rollNumber, dept,
                    otherDept, year, section, college, safeTeamSize, utr,
                    member2Name, member2Email, member2Phone, member2RollNumber,
                    member3Name, member3Email, member3Phone, member3RollNumber,
                    member4Name, member4Email, member4Phone, member4RollNumber,
                    member5Name, member5Email, member5Phone, member5RollNumber);
            return "register";
        }

        // 2. Validation for mandatory fields
        boolean deptValid = !dept.isBlank() && (!"Other".equalsIgnoreCase(dept.trim()) || !otherDept.isBlank());
        boolean leaderValid = !leaderName.isBlank() && !leaderEmail.isBlank() && !leaderPhone.isBlank() &&
                !rollNumber.isBlank() && deptValid && !year.isBlank() && !section.isBlank() && !utr.isBlank() &&
                !finalTeamName.isBlank();

        boolean member2Valid = !member2Name.isBlank() && !member2Email.isBlank() && !member2Phone.isBlank()
                && !member2RollNumber.isBlank();
        boolean member3Valid = !member3Name.isBlank() && !member3Email.isBlank() && !member3Phone.isBlank()
                && !member3RollNumber.isBlank();
        boolean member4Valid = (safeTeamSize < 4) || (!member4Name.isBlank() && !member4Email.isBlank()
                && !member4Phone.isBlank() && !member4RollNumber.isBlank());
        boolean member5Valid = (safeTeamSize < 5) || (!member5Name.isBlank() && !member5Email.isBlank()
                && !member5Phone.isBlank() && !member5RollNumber.isBlank());

        if (!leaderValid || !member2Valid || !member3Valid || !member4Valid || !member5Valid) {
            model.addAttribute("errorMessage",
                    "Please provide all required details including Team Name, Department, Leader, and all team members (minimum 3 persons per group required).");
            model.addAttribute("eventName", "Odyssey");
            model.addAttribute("tagline", "Beyond the Horizon");
            preserveRegistrationAttributes(model, teamName, leaderName, leaderEmail, leaderPhone, rollNumber, dept,
                    otherDept, year, section, college, safeTeamSize, utr,
                    member2Name, member2Email, member2Phone, member2RollNumber,
                    member3Name, member3Email, member3Phone, member3RollNumber,
                    member4Name, member4Email, member4Phone, member4RollNumber,
                    member5Name, member5Email, member5Phone, member5RollNumber);
            return "register";
        }

        // 3. Validation for names: must only contain letters, spaces, and dots (.)
        String nameRegex = "^[a-zA-Z\\s.]+$";
        boolean leaderNameValid = leaderName.trim().matches(nameRegex);
        boolean member2NameValid = member2Name.trim().matches(nameRegex);
        boolean member3NameValid = member3Name.trim().matches(nameRegex);
        boolean member4NameValid = (safeTeamSize < 4) || member4Name.trim().matches(nameRegex);
        boolean member5NameValid = (safeTeamSize < 5) || member5Name.trim().matches(nameRegex);

        if (!leaderNameValid || !member2NameValid || !member3NameValid || !member4NameValid || !member5NameValid) {
            model.addAttribute("errorMessage",
                    "Names can only contain letters, spaces, and dots (.) — no numbers or other special characters are permitted.");
            model.addAttribute("eventName", "Odyssey");
            model.addAttribute("tagline", "Beyond the Horizon");
            preserveRegistrationAttributes(model, teamName, leaderName, leaderEmail, leaderPhone, rollNumber, dept,
                    otherDept, year, section, college, safeTeamSize, utr,
                    member2Name, member2Email, member2Phone, member2RollNumber,
                    member3Name, member3Email, member3Phone, member3RollNumber,
                    member4Name, member4Email, member4Phone, member4RollNumber,
                    member5Name, member5Email, member5Phone, member5RollNumber);
            return "register";
        }

        // 4. Validation for phone numbers: must be strictly 10 digits
        String phoneRegex = "^[0-9]{10}$";
        boolean leaderPhoneValid = leaderPhone.trim().matches(phoneRegex);
        boolean member2PhoneValid = member2Phone.trim().matches(phoneRegex);
        boolean member3PhoneValid = member3Phone.trim().matches(phoneRegex);
        boolean member4PhoneValid = (safeTeamSize < 4) || member4Phone.trim().matches(phoneRegex);
        boolean member5PhoneValid = (safeTeamSize < 5) || member5Phone.trim().matches(phoneRegex);

        if (!leaderPhoneValid || !member2PhoneValid || !member3PhoneValid || !member4PhoneValid || !member5PhoneValid) {
            model.addAttribute("errorMessage",
                    "Phone numbers must be exactly 10 digits without country code or spaces.");
            model.addAttribute("eventName", "Odyssey");
            model.addAttribute("tagline", "Beyond the Horizon");
            preserveRegistrationAttributes(model, teamName, leaderName, leaderEmail, leaderPhone, rollNumber, dept,
                    otherDept, year, section, college, safeTeamSize, utr,
                    member2Name, member2Email, member2Phone, member2RollNumber,
                    member3Name, member3Email, member3Phone, member3RollNumber,
                    member4Name, member4Email, member4Phone, member4RollNumber,
                    member5Name, member5Email, member5Phone, member5RollNumber);
            return "register";
        }

        // Handle Payment Screenshot Upload (Stored directly in MySQL Database)
        byte[] paymentImageBytes = null;
        String paymentImageType = "image/png";
        String paymentImagePath = "";

        if (paymentImage != null && !paymentImage.isEmpty()) {
            try {
                paymentImageBytes = paymentImage.getBytes();
                String detectedType = paymentImage.getContentType();
                if (detectedType != null && !detectedType.isBlank()) {
                    paymentImageType = detectedType.trim();
                }
            } catch (IOException e) {
                paymentImageBytes = null;
            }

            // Also write a copy to disk if uploads directory is accessible
            try {
                String originalFilename = paymentImage.getOriginalFilename();
                String ext = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String safeRoll = rollNumber.replaceAll("[^a-zA-Z0-9]", "");
                String filename = "pay_" + System.currentTimeMillis() + "_" + safeRoll + ext;

                Path uploadDir = Paths.get("uploads", "payments").toAbsolutePath().normalize();
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path destination = uploadDir.resolve(filename);
                if (paymentImageBytes != null) {
                    Files.write(destination, paymentImageBytes);
                }
                paymentImagePath = "/uploads/payments/" + filename;
            } catch (Exception ignored) {
            }
        }

        int calculatedAmount = safeTeamSize * 100;
        String track = "General";
        String stage = "Registered";
        String pitchSummary = "";
        String deckUrl = "";

        Registration reg = registrationService.register(
                finalTeamName, track, stage, pitchSummary, deckUrl,
                leaderName, leaderEmail, leaderPhone, rollNumber, finalDept, year, section, college,
                calculatedAmount, utr, paymentImagePath, paymentImageBytes, paymentImageType, safeTeamSize,
                member2Name, member2Email, member2Phone, member2RollNumber, "",
                member3Name, member3Email, member3Phone, member3RollNumber, "",
                member4Name, member4Email, member4Phone, member4RollNumber, "",
                member5Name, member5Email, member5Phone, member5RollNumber, "");

        redirectAttributes.addFlashAttribute("successRegistration", reg);
        return "redirect:/register/pass/" + reg.id() + "?autoCal=1";
    }

    @GetMapping("/api/check-team-name")
    @ResponseBody
    public java.util.Map<String, Object> checkTeamName(@RequestParam(defaultValue = "") String name) {
        boolean taken = registrationService.isTeamNameTaken(name);
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("name", name != null ? name.trim() : "");
        response.put("taken", taken);
        response.put("message", taken ? "Team name is already registered" : "Team name is available");
        return response;
    }

    @GetMapping("/pass")
    public String showBoardingPassDirect(@RequestParam(value = "autoCal", required = false) String autoCal,
            Model model) {
        List<Registration> list = registrationService.getAll();
        if (!list.isEmpty()) {
            return "redirect:/register/pass/" + list.get(list.size() - 1).getId();
        }
        Registration demo = new Registration();
        demo.setId("ODY-2026-DEMO");
        demo.setTeamName("Odyssey Founders");
        demo.setTrack("AI & FinTech");
        demo.setStage("MVP Prototype");
        demo.setLeaderName("Shaik Alex");
        demo.setLeaderEmail("alex@kitsgnt.ac.in");
        demo.setLeaderPhone("9876543210");
        demo.setRollNumber("22JR1A0501");
        demo.setDept("CSE");
        demo.setYear("3rd Year");
        demo.setSection("A");
        demo.setCollege("KITS Guntur");
        demo.setAmountPaid(300);
        demo.setTeamSize(3);
        demo.setMember2Name("Venkata Sai");
        demo.setMember2Email("sai@kitsgnt.ac.in");
        demo.setMember2Phone("9876543211");
        demo.setMember2RollNumber("22JR1A0502");
        demo.setMember3Name("Priya Sharma");
        demo.setMember3Email("priya@kitsgnt.ac.in");
        demo.setMember3Phone("9876543212");
        demo.setMember3RollNumber("22JR1A0503");
        demo.setUtr("UPI987654321012");
        demo.setRegisteredAt(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC));
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("registration", demo);
        model.addAttribute("autoCal", "1".equals(autoCal) || "true".equalsIgnoreCase(autoCal));
        return "pass";
    }

    @GetMapping("/register/pass/{id}")
    public String showBoardingPass(@PathVariable String id,
            @RequestParam(value = "autoCal", required = false) String autoCal,
            Model model) {
        Optional<Registration> regOpt = registrationService.findById(id);
        if (regOpt.isEmpty()) {
            return "redirect:/register";
        }
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("registration", regOpt.get());
        model.addAttribute("autoCal", "1".equals(autoCal) || "true".equalsIgnoreCase(autoCal));
        return "pass";
    }

    @GetMapping({ "/find-ticket", "/find-my-ticket" })
    public String showFindTicketPage(Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        return "find-ticket";
    }

    @PostMapping({ "/find-ticket", "/find-my-ticket" })
    public String processFindTicket(
            @RequestParam(defaultValue = "") String teamName,
            @RequestParam(defaultValue = "") String leaderName,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String utr,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");

        if (teamName.isBlank() || leaderName.isBlank() || email.isBlank() || utr.isBlank()) {
            model.addAttribute("errorMessage", "Please provide all 4 verification details: Team Name, Leader Name, Email, and UTR.");
            model.addAttribute("prevTeamName", teamName);
            model.addAttribute("prevLeaderName", leaderName);
            model.addAttribute("prevEmail", email);
            model.addAttribute("prevUtr", utr);
            return "find-ticket";
        }

        Optional<Registration> regOpt = registrationService.verifyAndFindTicket(teamName, leaderName, email, utr);
        if (regOpt.isEmpty()) {
            model.addAttribute("errorMessage",
                    "Verification Failed: No registration found matching the provided Team Name, Leader Name, Email, and UTR. Please ensure all 4 credentials match your registration details.");
            model.addAttribute("prevTeamName", teamName);
            model.addAttribute("prevLeaderName", leaderName);
            model.addAttribute("prevEmail", email);
            model.addAttribute("prevUtr", utr);
            return "find-ticket";
        }

        Registration reg = regOpt.get();
        redirectAttributes.addFlashAttribute("verified", true);
        return "redirect:/register/pass/" + reg.getId() + "?verified=1";
    }

    @GetMapping({ "/faq", "/faqs" })
    public String showFaqPage(Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        return "faq";
    }

    @GetMapping("/admin/login")
    public String showAdminLoginPage(
            @RequestParam(value = "redirect", required = false) String redirect,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "timeout", required = false) String timeout,
            HttpSession session,
            Model model) {
        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");

        if (timeout != null) {
            if (session != null) {
                try {
                    session.removeAttribute("adminLoggedIn");
                    session.invalidate();
                } catch (IllegalStateException ignored) {
                }
            }
            model.addAttribute("timeoutMessage",
                    "Session Expired: You were automatically logged out due to 30 minutes of inactivity.");
            model.addAttribute("alreadyLoggedIn", false);
        } else {
            if (logout != null) {
                model.addAttribute("successMessage", "You have been logged out successfully.");
            }
            boolean isLoggedIn = session != null && Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"));
            model.addAttribute("alreadyLoggedIn", isLoggedIn);
        }

        model.addAttribute("redirectUrl",
                (redirect != null && !redirect.isBlank()) ? redirect : "/admin/registrations");
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String handleAdminLogin(
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String password,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            HttpSession session,
            Model model) {
        if (adminUsername.equals(username.trim()) && adminPassword.equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            if (redirectUrl != null && !redirectUrl.isBlank() && redirectUrl.startsWith("/admin")
                    && !redirectUrl.contains("login")) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/admin/registrations";
        }

        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        model.addAttribute("prevUsername", username);
        model.addAttribute("redirectUrl",
                (redirectUrl != null && !redirectUrl.isBlank()) ? redirectUrl : "/admin/registrations");
        return "admin-login";
    }

    @GetMapping("/admin/logout")
    public String handleAdminLogout(
            @RequestParam(value = "timeout", required = false) String timeout,
            HttpSession session) {
        if (session != null) {
            try {
                session.removeAttribute("adminLoggedIn");
                session.invalidate();
            } catch (IllegalStateException ignored) {
            }
        }
        if (timeout != null) {
            return "redirect:/admin/login?timeout=true";
        }
        return "redirect:/admin/login?logout=true";
    }

    @GetMapping({ "/admin", "/admin/registrations" })
    public String viewAllRegistrations(HttpSession session, Model model) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login?redirect=/admin/registrations";
        }
        List<Registration> list = registrationService.getAll();
        long totalParticipants = list.size();
        long totalTeams = list.stream()
                .map(r -> {
                    if (r.getGroupId() != null && !r.getGroupId().isBlank()) {
                        return r.getGroupId().trim();
                    }
                    if (r.getTeamName() != null && !r.getTeamName().isBlank()) {
                        return r.getTeamName().trim().toLowerCase();
                    }
                    if (r.getId() != null) {
                        return r.getId().replaceAll("-M\\d+$", "").trim();
                    }
                    return "";
                })
                .filter(s -> !s.isBlank())
                .distinct()
                .count();

        long totalAmountReceived = list.stream()
                .filter(Registration::isLeader)
                .mapToLong(r -> r.getAmountPaid() > 0 ? r.getAmountPaid() : (r.getTeamSize() > 0 ? r.getTeamSize() * 100L : 300L))
                .sum();

        model.addAttribute("eventName", "Odyssey");
        model.addAttribute("tagline", "Beyond the Horizon");
        model.addAttribute("registrations", list);
        model.addAttribute("totalCount", totalParticipants);
        model.addAttribute("totalTeams", totalTeams);
        model.addAttribute("totalParticipants", totalParticipants);
        model.addAttribute("totalAmountReceived", totalAmountReceived);
        model.addAttribute("totalRevenue", totalAmountReceived);
        model.addAttribute("deptCounts", registrationService.getDepartmentCounts());
        return "admin";
    }

    @PostMapping("/admin/registrations/delete")
    public String deleteRegistration(
            @RequestParam("id") String id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login?redirect=/admin/registrations";
        }

        boolean deleted = registrationService.deleteRegistration(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Record #" + id + " has been permanently deleted from the database.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to delete record #" + id + ": Record not found.");
        }
        return "redirect:/admin/registrations";
    }

    @PostMapping("/api/admin/registrations/delete")
    @ResponseBody
    public java.util.Map<String, Object> apiDeleteRegistration(
            @RequestParam("id") String id,
            HttpSession session) {
        java.util.Map<String, Object> res = new java.util.HashMap<>();
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            res.put("success", false);
            res.put("error", "Unauthorized. Please authenticate at /admin/login");
            return res;
        }

        boolean deleted = registrationService.deleteRegistration(id);
        res.put("success", deleted);
        res.put("message", deleted ? "Record #" + id + " permanently deleted from database." : "Record not found.");
        return res;
    }

    @GetMapping("/api/registrations")
    @ResponseBody
    public Object apiRegistrations(HttpSession session) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return java.util.Collections.singletonMap("error", "Unauthorized. Please authenticate at /admin/login");
        }
        return registrationService.getAll();
    }

    @GetMapping("/api/dept-counts")
    @ResponseBody
    public Object apiDeptCounts(HttpSession session) {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return java.util.Collections.singletonMap("error", "Unauthorized. Please authenticate at /admin/login");
        }
        return registrationService.getDepartmentCounts();
    }

    @GetMapping("/admin/export/csv")
    public void exportCsv(
            @RequestParam(value = "dept", required = false) String dept,
            HttpSession session,
            HttpServletResponse response) throws IOException {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        List<Registration> list = registrationService.getAll();
        if (dept != null && !dept.isBlank() && !dept.equalsIgnoreCase("ALL")) {
            String targetDept = dept.trim();
            list = list.stream()
                    .filter(r -> r.getDept() != null && r.getDept().equalsIgnoreCase(targetDept))
                    .collect(java.util.stream.Collectors.toList());
        }

        String safeDept = (dept == null || dept.isBlank() || dept.equalsIgnoreCase("ALL")) ? "ALL_DEPTS"
                : dept.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = "odyssey_registrations_" + safeDept + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        java.io.PrintWriter writer = response.getWriter();
        // UTF-8 Byte Order Mark so Microsoft Excel automatically parses UTF-8 correctly
        writer.write('\uFEFF');
        writer.println(
                "Pass ID,Roll Number,Student Name,Participant Role,Team Name,Department,Year,Section,College,Phone,Email,Group Size,Fee Paid (INR),UTR Reference,Payment Proof URL,Registration Date");

        for (Registration r : list) {
            writer.println(String.join(",",
                    escapeCsv(r.getId()),
                    escapeCsv(r.getRollNumber()),
                    escapeCsv(r.getLeaderName()),
                    escapeCsv(r.getParticipantRole() != null ? r.getParticipantRole() : "Member"),
                    escapeCsv(r.getTeamName()),
                    escapeCsv(r.getDept()),
                    escapeCsv(r.getYear()),
                    escapeCsv(r.getSection()),
                    escapeCsv(r.getCollege()),
                    escapeCsv(r.getLeaderPhone()),
                    escapeCsv(r.getLeaderEmail()),
                    String.valueOf(r.getTeamSize()),
                    r.isLeader() ? String.valueOf(r.getAmountPaid()) : "-",
                    escapeCsv(r.getUtr()),
                    escapeCsv(r.getPaymentImagePath()),
                    escapeCsv(r.getFormattedRegistrationDateTime())));
        }
        writer.flush();
    }

    @GetMapping("/admin/export/excel")
    public void exportExcel(
            @RequestParam(value = "dept", required = false) String dept,
            HttpSession session,
            HttpServletResponse response) throws IOException {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        List<Registration> list = registrationService.getAll();
        if (dept != null && !dept.isBlank() && !dept.equalsIgnoreCase("ALL")) {
            String targetDept = dept.trim();
            list = list.stream()
                    .filter(r -> r.getDept() != null && r.getDept().equalsIgnoreCase(targetDept))
                    .collect(java.util.stream.Collectors.toList());
        }

        String safeDept = (dept == null || dept.isBlank() || dept.equalsIgnoreCase("ALL")) ? "ALL_DEPTS"
                : dept.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = "odyssey_registrations_" + safeDept + ".xls";

        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        java.io.PrintWriter writer = response.getWriter();
        writer.write('\uFEFF');
        writer.println(
                "<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns=\"http://www.w3.org/TR/REC-html40\">");
        writer.println(
                "<head><meta charset=\"UTF-8\"><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>Registrations</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head>");
        writer.println(
                "<body><table border=\"1\" style=\"border-collapse: collapse; font-family: Calibri, sans-serif;\">");
        writer.println("<tr style=\"background-color: #021a30; color: #19d3e6; font-weight: bold;\">");
        writer.println(
                "<th>Pass ID</th><th>Roll No</th><th>Student Name</th><th>Role</th><th>Team Name</th><th>Dept</th><th>Year</th><th>Section</th><th>College</th><th>Phone</th><th>Email</th><th>Group Size</th><th>Fee Paid (INR)</th><th>UTR Reference</th><th>Payment Proof URL</th><th>Registration Date</th>");
        writer.println("</tr>");

        for (Registration r : list) {
            writer.println("<tr>");
            writer.println("<td>" + escapeHtml(r.getId()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getRollNumber()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getLeaderName()) + "</td>");
            writer.println(
                    "<td>" + escapeHtml(r.getParticipantRole() != null ? r.getParticipantRole() : "Member") + "</td>");
            writer.println("<td>" + escapeHtml(r.getTeamName()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getDept()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getYear()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getSection()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getCollege()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getLeaderPhone()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getLeaderEmail()) + "</td>");
            writer.println("<td>" + r.getTeamSize() + "</td>");
            writer.println("<td>" + (r.isLeader() ? r.getAmountPaid() : "-") + "</td>");
            writer.println("<td>" + escapeHtml(r.getUtr()) + "</td>");
            writer.println("<td>" + escapeHtml(r.getPaymentImagePath()) + "</td>");
            writer.println(
                    "<td>" + escapeHtml(r.getFormattedRegistrationDateTime()) + "</td>");
            writer.println("</tr>");
        }

        writer.println("</table></body></html>");
        writer.flush();
    }

    @GetMapping("/admin/export/json")
    public void exportJson(
            @RequestParam(value = "dept", required = false) String dept,
            HttpSession session,
            HttpServletResponse response) throws IOException {
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        List<Registration> list = registrationService.getAll();
        if (dept != null && !dept.isBlank() && !dept.equalsIgnoreCase("ALL")) {
            String targetDept = dept.trim();
            list = list.stream()
                    .filter(r -> r.getDept() != null && r.getDept().equalsIgnoreCase(targetDept))
                    .collect(java.util.stream.Collectors.toList());
        }

        String safeDept = (dept == null || dept.isBlank() || dept.equalsIgnoreCase("ALL")) ? "ALL_DEPTS"
                : dept.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = "odyssey_registrations_" + safeDept + ".json";

        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), list);
    }

    private void preserveRegistrationAttributes(Model model, String teamName, String leaderName, String leaderEmail,
            String leaderPhone, String rollNumber, String dept, String otherDept, String year, String section,
            String college, int teamSize, String utr, String member2Name, String member2Email, String member2Phone,
            String member2RollNumber, String member3Name, String member3Email, String member3Phone,
            String member3RollNumber, String member4Name, String member4Email, String member4Phone,
            String member4RollNumber, String member5Name, String member5Email, String member5Phone,
            String member5RollNumber) {
        model.addAttribute("prevTeamName", teamName);
        model.addAttribute("prevLeaderName", leaderName);
        model.addAttribute("prevLeaderEmail", leaderEmail);
        model.addAttribute("prevLeaderPhone", leaderPhone);
        model.addAttribute("prevRollNumber", rollNumber);
        model.addAttribute("prevDept", dept);
        model.addAttribute("prevOtherDept", otherDept);
        model.addAttribute("prevYear", year);
        model.addAttribute("prevSection", section);
        model.addAttribute("prevCollege", college);
        model.addAttribute("teamSize", teamSize);
        model.addAttribute("prevUtr", utr);

        model.addAttribute("prevMember2Name", member2Name);
        model.addAttribute("prevMember2Email", member2Email);
        model.addAttribute("prevMember2Phone", member2Phone);
        model.addAttribute("prevMember2RollNumber", member2RollNumber);

        model.addAttribute("prevMember3Name", member3Name);
        model.addAttribute("prevMember3Email", member3Email);
        model.addAttribute("prevMember3Phone", member3Phone);
        model.addAttribute("prevMember3RollNumber", member3RollNumber);

        model.addAttribute("prevMember4Name", member4Name);
        model.addAttribute("prevMember4Email", member4Email);
        model.addAttribute("prevMember4Phone", member4Phone);
        model.addAttribute("prevMember4RollNumber", member4RollNumber);

        model.addAttribute("prevMember5Name", member5Name);
        model.addAttribute("prevMember5Email", member5Email);
        model.addAttribute("prevMember5Phone", member5Phone);
        model.addAttribute("prevMember5RollNumber", member5RollNumber);
    }

    @GetMapping({"/register/payment-image/{id}", "/api/registrations/{id}/payment-image"})
    @ResponseBody
    public ResponseEntity<byte[]> getPaymentImageFromDatabase(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Registration> regOpt = registrationService.findById(id.trim());
        if (regOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Registration reg = regOpt.get();
        byte[] imageBytes = reg.getPaymentImageBytes();
        String contentType = reg.getPaymentImageType();

        // If this record is a member with no direct bytes, look up the group leader
        if ((imageBytes == null || imageBytes.length == 0) && reg.getGroupId() != null && !reg.getGroupId().isBlank() && !reg.getGroupId().equalsIgnoreCase(reg.getId())) {
            Optional<Registration> leaderOpt = registrationService.findById(reg.getGroupId());
            if (leaderOpt.isPresent()) {
                imageBytes = leaderOpt.get().getPaymentImageBytes();
                contentType = leaderOpt.get().getPaymentImageType();
            }
        }

        // Serve from MySQL Database
        if (imageBytes != null && imageBytes.length > 0) {
            if (contentType == null || contentType.isBlank()) {
                contentType = "image/png";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"payment_proof_" + reg.getId() + ".png\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);
        }

        // Fallback: If not stored as BLOB, try to serve from local disk if it exists
        if (reg.getPaymentImagePath() != null && reg.getPaymentImagePath().contains("/uploads/payments/")) {
            String fname = reg.getPaymentImagePath().substring(reg.getPaymentImagePath().lastIndexOf("/") + 1);
            try {
                Path localFile = Paths.get("uploads", "payments").resolve(fname).normalize();
                if (Files.exists(localFile) && Files.isReadable(localFile)) {
                    byte[] fileBytes = Files.readAllBytes(localFile);
                    String fileContentType = Files.probeContentType(localFile);
                    if (fileContentType == null) {
                        fileContentType = "image/png";
                    }
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fname + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
                            .contentType(MediaType.parseMediaType(fileContentType))
                            .body(fileBytes);
                }
            } catch (Exception ignored) {
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/uploads/payments/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> servePaymentImage(@PathVariable String filename) {
        // 1. Try serving from local disk first if present
        try {
            Path baseDir = Paths.get("uploads", "payments").toAbsolutePath().normalize();
            Path filePath = baseDir.resolve(filename).normalize();

            if (filePath.startsWith(baseDir) && Files.exists(filePath) && Files.isReadable(filePath)) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "image/png";
                }
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(Files.readAllBytes(filePath));
            }
        } catch (Exception ignored) {
        }

        // 2. If not on local disk (e.g. wiped by Railway restart), load directly from MySQL database!
        try {
            Optional<Registration> regOpt = registrationService.findByPaymentImagePathFilename(filename);
            if (regOpt.isPresent()) {
                Registration reg = regOpt.get();
                byte[] bytes = reg.getPaymentImageBytes();
                String type = reg.getPaymentImageType();

                if ((bytes == null || bytes.length == 0) && reg.getGroupId() != null && !reg.getGroupId().isBlank()) {
                    Optional<Registration> leaderOpt = registrationService.findById(reg.getGroupId());
                    if (leaderOpt.isPresent()) {
                        bytes = leaderOpt.get().getPaymentImageBytes();
                        type = leaderOpt.get().getPaymentImageType();
                    }
                }

                if (bytes != null && bytes.length > 0) {
                    if (type == null || type.isBlank()) {
                        type = "image/png";
                    }
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
                            .contentType(MediaType.parseMediaType(type))
                            .body(bytes);
                }
            }
        } catch (Exception ignored) {
        }

        return ResponseEntity.notFound().build();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ") + "\"";
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
