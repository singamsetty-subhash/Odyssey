package com.ecell.odyssey;

import java.time.LocalDateTime;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @Column(nullable = false, unique = true, length = 32)
    private String id;

    @Column(nullable = false, length = 128)
    private String teamName;

    @Column(nullable = false, length = 64)
    private String track;

    @Column(nullable = false, length = 64)
    private String stage;

    @Column(length = 2000)
    private String pitchSummary;

    @Column(length = 512)
    private String deckUrl;

    // Leader / Primary Person details
    @Column(nullable = false, length = 128)
    private String leaderName;

    @Column(nullable = false, length = 128)
    private String leaderEmail;

    @Column(nullable = false, length = 32)
    private String leaderPhone;

    @Column(nullable = false, length = 64)
    private String rollNumber;

    @Column(nullable = false, length = 64)
    private String dept;

    @Column(nullable = false, length = 32)
    private String year;

    @Column(nullable = false, length = 32)
    private String section;

    @Column(nullable = false, length = 128)
    private String college;

    // Payment details
    @Column(nullable = false)
    private int amountPaid;

    @Column(nullable = false, length = 128)
    private String utr;

    @Column(length = 512)
    private String paymentImagePath;

    // Team Size
    @Column(nullable = false)
    private int teamSize;

    // Additional Member 2 details
    @Column(length = 128)
    private String member2Name;

    @Column(length = 128)
    private String member2Email;

    @Column(length = 32)
    private String member2Phone;

    @Column(length = 64)
    private String member2RollNumber;

    @Column(length = 128)
    private String member2Role;

    // Additional Member 3 details
    @Column(length = 128)
    private String member3Name;

    @Column(length = 128)
    private String member3Email;

    @Column(length = 32)
    private String member3Phone;

    @Column(length = 64)
    private String member3RollNumber;

    @Column(length = 128)
    private String member3Role;

    // Additional Member 4 details
    @Column(length = 128)
    private String member4Name;

    @Column(length = 128)
    private String member4Email;

    @Column(length = 32)
    private String member4Phone;

    @Column(length = 64)
    private String member4RollNumber;

    @Column(length = 128)
    private String member4Role;

    // Additional Member 5 details
    @Column(length = 128)
    private String member5Name;

    @Column(length = 128)
    private String member5Email;

    @Column(length = 32)
    private String member5Phone;

    @Column(length = 64)
    private String member5RollNumber;

    @Column(length = 128)
    private String member5Role;

    // Individual Participant Role & Team Group Reference
    @Column(length = 64)
    private String participantRole;

    @Column(length = 32)
    private String groupId;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    public Registration() {
    }

    public Registration(
        String id,
        String teamName,
        String track,
        String stage,
        String pitchSummary,
        String deckUrl,
        String leaderName,
        String leaderEmail,
        String leaderPhone,
        String rollNumber,
        String dept,
        String year,
        String section,
        String college,
        int amountPaid,
        String utr,
        String paymentImagePath,
        int teamSize,
        String member2Name,
        String member2Email,
        String member2Phone,
        String member2RollNumber,
        String member2Role,
        String member3Name,
        String member3Email,
        String member3Phone,
        String member3RollNumber,
        String member3Role,
        String member4Name,
        String member4Email,
        String member4Phone,
        String member4RollNumber,
        String member4Role,
        LocalDateTime registeredAt
    ) {
        this(id, teamName, track, stage, pitchSummary, deckUrl, leaderName, leaderEmail, leaderPhone,
             rollNumber, dept, year, section, college, amountPaid, utr, paymentImagePath, teamSize,
             member2Name, member2Email, member2Phone, member2RollNumber, member2Role,
             member3Name, member3Email, member3Phone, member3RollNumber, member3Role,
             member4Name, member4Email, member4Phone, member4RollNumber, member4Role,
             "", "", "", "", "",
             "Leader", id, registeredAt);
    }

    public Registration(
        String id,
        String teamName,
        String track,
        String stage,
        String pitchSummary,
        String deckUrl,
        String leaderName,
        String leaderEmail,
        String leaderPhone,
        String rollNumber,
        String dept,
        String year,
        String section,
        String college,
        int amountPaid,
        String utr,
        String paymentImagePath,
        int teamSize,
        String member2Name,
        String member2Email,
        String member2Phone,
        String member2RollNumber,
        String member2Role,
        String member3Name,
        String member3Email,
        String member3Phone,
        String member3RollNumber,
        String member3Role,
        String member4Name,
        String member4Email,
        String member4Phone,
        String member4RollNumber,
        String member4Role,
        String member5Name,
        String member5Email,
        String member5Phone,
        String member5RollNumber,
        String member5Role,
        String participantRole,
        String groupId,
        LocalDateTime registeredAt
    ) {
        this.id = id;
        this.teamName = teamName;
        this.track = track;
        this.stage = stage;
        this.pitchSummary = pitchSummary;
        this.deckUrl = deckUrl;
        this.leaderName = leaderName;
        this.leaderEmail = leaderEmail;
        this.leaderPhone = leaderPhone;
        this.rollNumber = rollNumber;
        this.dept = dept;
        this.year = year;
        this.section = section;
        this.college = college;
        this.amountPaid = amountPaid;
        this.utr = utr;
        this.paymentImagePath = paymentImagePath;
        this.teamSize = teamSize;
        this.member2Name = member2Name;
        this.member2Email = member2Email;
        this.member2Phone = member2Phone;
        this.member2RollNumber = member2RollNumber;
        this.member2Role = member2Role;
        this.member3Name = member3Name;
        this.member3Email = member3Email;
        this.member3Phone = member3Phone;
        this.member3RollNumber = member3RollNumber;
        this.member3Role = member3Role;
        this.member4Name = member4Name;
        this.member4Email = member4Email;
        this.member4Phone = member4Phone;
        this.member4RollNumber = member4RollNumber;
        this.member4Role = member4Role;
        this.member5Name = member5Name;
        this.member5Email = member5Email;
        this.member5Phone = member5Phone;
        this.member5RollNumber = member5RollNumber;
        this.member5Role = member5Role;
        this.participantRole = participantRole != null && !participantRole.isBlank() ? participantRole : "Participant";
        this.groupId = groupId != null && !groupId.isBlank() ? groupId : id;
        this.registeredAt = registeredAt != null ? registeredAt : LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getPitchSummary() { return pitchSummary; }
    public void setPitchSummary(String pitchSummary) { this.pitchSummary = pitchSummary; }

    public String getDeckUrl() { return deckUrl; }
    public void setDeckUrl(String deckUrl) { this.deckUrl = deckUrl; }

    public String getLeaderName() { return leaderName; }
    public void setLeaderName(String leaderName) { this.leaderName = leaderName; }

    public String getLeaderEmail() { return leaderEmail; }
    public void setLeaderEmail(String leaderEmail) { this.leaderEmail = leaderEmail; }

    public String getLeaderPhone() { return leaderPhone; }
    public void setLeaderPhone(String leaderPhone) { this.leaderPhone = leaderPhone; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public int getAmountPaid() { return amountPaid; }
    public void setAmountPaid(int amountPaid) { this.amountPaid = amountPaid; }

    public String getUtr() { return utr; }
    public void setUtr(String utr) { this.utr = utr; }

    public String getPaymentImagePath() { return paymentImagePath; }
    public void setPaymentImagePath(String paymentImagePath) { this.paymentImagePath = paymentImagePath; }

    public int getTeamSize() { return teamSize; }
    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }

    public String getMember2Name() { return member2Name; }
    public void setMember2Name(String member2Name) { this.member2Name = member2Name; }

    public String getMember2Email() { return member2Email; }
    public void setMember2Email(String member2Email) { this.member2Email = member2Email; }

    public String getMember2Phone() { return member2Phone; }
    public void setMember2Phone(String member2Phone) { this.member2Phone = member2Phone; }

    public String getMember2RollNumber() { return member2RollNumber; }
    public void setMember2RollNumber(String member2RollNumber) { this.member2RollNumber = member2RollNumber; }

    public String getMember2Role() { return member2Role; }
    public void setMember2Role(String member2Role) { this.member2Role = member2Role; }

    public String getMember3Name() { return member3Name; }
    public void setMember3Name(String member3Name) { this.member3Name = member3Name; }

    public String getMember3Email() { return member3Email; }
    public void setMember3Email(String member3Email) { this.member3Email = member3Email; }

    public String getMember3Phone() { return member3Phone; }
    public void setMember3Phone(String member3Phone) { this.member3Phone = member3Phone; }

    public String getMember3RollNumber() { return member3RollNumber; }
    public void setMember3RollNumber(String member3RollNumber) { this.member3RollNumber = member3RollNumber; }

    public String getMember3Role() { return member3Role; }
    public void setMember3Role(String member3Role) { this.member3Role = member3Role; }

    public String getMember4Name() { return member4Name; }
    public void setMember4Name(String member4Name) { this.member4Name = member4Name; }

    public String getMember4Email() { return member4Email; }
    public void setMember4Email(String member4Email) { this.member4Email = member4Email; }

    public String getMember4Phone() { return member4Phone; }
    public void setMember4Phone(String member4Phone) { this.member4Phone = member4Phone; }

    public String getMember4RollNumber() { return member4RollNumber; }
    public void setMember4RollNumber(String member4RollNumber) { this.member4RollNumber = member4RollNumber; }

    public String getMember4Role() { return member4Role; }
    public void setMember4Role(String member4Role) { this.member4Role = member4Role; }

    public String getMember5Name() { return member5Name; }
    public void setMember5Name(String member5Name) { this.member5Name = member5Name; }

    public String getMember5Email() { return member5Email; }
    public void setMember5Email(String member5Email) { this.member5Email = member5Email; }

    public String getMember5Phone() { return member5Phone; }
    public void setMember5Phone(String member5Phone) { this.member5Phone = member5Phone; }

    public String getMember5RollNumber() { return member5RollNumber; }
    public void setMember5RollNumber(String member5RollNumber) { this.member5RollNumber = member5RollNumber; }

    public String getMember5Role() { return member5Role; }
    public void setMember5Role(String member5Role) { this.member5Role = member5Role; }

    public String getParticipantRole() { return participantRole; }
    public void setParticipantRole(String participantRole) { this.participantRole = participantRole; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    // Record-style aliases for backwards compatibility
    public String id() { return getId(); }
    public String teamName() { return getTeamName(); }
    public String track() { return getTrack(); }
    public String stage() { return getStage(); }
    public String pitchSummary() { return getPitchSummary(); }
    public String deckUrl() { return getDeckUrl(); }
    public String leaderName() { return getLeaderName(); }
    public String leaderEmail() { return getLeaderEmail(); }
    public String leaderPhone() { return getLeaderPhone(); }
    public String rollNumber() { return getRollNumber(); }
    public String dept() { return getDept(); }
    public String year() { return getYear(); }
    public String section() { return getSection(); }
    public String college() { return getCollege(); }
    public int amountPaid() { return getAmountPaid(); }
    public String utr() { return getUtr(); }
    public String paymentImagePath() { return getPaymentImagePath(); }
    public int teamSize() { return getTeamSize(); }
    public String member2Name() { return getMember2Name(); }
    public String member2Email() { return getMember2Email(); }
    public String member2Phone() { return getMember2Phone(); }
    public String member2RollNumber() { return getMember2RollNumber(); }
    public String member2Role() { return getMember2Role(); }
    public String member3Name() { return getMember3Name(); }
    public String member3Email() { return getMember3Email(); }
    public String member3Phone() { return getMember3Phone(); }
    public String member3RollNumber() { return getMember3RollNumber(); }
    public String member3Role() { return getMember3Role(); }
    public String member4Name() { return getMember4Name(); }
    public String member4Email() { return getMember4Email(); }
    public String member4Phone() { return getMember4Phone(); }
    public String member4RollNumber() { return getMember4RollNumber(); }
    public String member4Role() { return getMember4Role(); }
    public String member5Name() { return getMember5Name(); }
    public String member5Email() { return getMember5Email(); }
    public String member5Phone() { return getMember5Phone(); }
    public String member5RollNumber() { return getMember5RollNumber(); }
    public String member5Role() { return getMember5Role(); }
    public String participantRole() { return getParticipantRole(); }
    public String groupId() { return getGroupId(); }
    public LocalDateTime registeredAt() { return getRegisteredAt(); }

    public boolean isLeader() {
        if (participantRole != null && !participantRole.isBlank()) {
            return "Leader".equalsIgnoreCase(participantRole.trim());
        }
        return id == null || !id.contains("-M");
    }

    public boolean leader() {
        return isLeader();
    }

    public String getAllEmailsCsv() {
        List<String> emails = new ArrayList<>();
        if (leaderEmail != null && !leaderEmail.isBlank()) emails.add(leaderEmail.trim());
        if (member2Email != null && !member2Email.isBlank()) emails.add(member2Email.trim());
        if (member3Email != null && !member3Email.isBlank()) emails.add(member3Email.trim());
        if (member4Email != null && !member4Email.isBlank() && teamSize >= 4) emails.add(member4Email.trim());
        if (member5Email != null && !member5Email.isBlank() && teamSize >= 5) emails.add(member5Email.trim());
        return String.join(",", emails);
    }

    public List<String> getAllEmailsList() {
        List<String> emails = new ArrayList<>();
        if (leaderEmail != null && !leaderEmail.isBlank()) emails.add(leaderEmail.trim());
        if (member2Email != null && !member2Email.isBlank()) emails.add(member2Email.trim());
        if (member3Email != null && !member3Email.isBlank()) emails.add(member3Email.trim());
        if (member4Email != null && !member4Email.isBlank() && teamSize >= 4) emails.add(member4Email.trim());
        if (member5Email != null && !member5Email.isBlank() && teamSize >= 5) emails.add(member5Email.trim());
        return emails;
    }

    public String getGoogleCalendarUrl() {
        String title = "Odyssey 2026 — Flagship Entrepreneurship Summit (KITS E-Cell)";
        // Andhra Pradesh IST: Oct 29 09:00 AM IST to Oct 30 06:00 PM IST (UTC: 20261029T033000Z / 20261030T123000Z)
        String dates = "20261029T033000Z/20261030T123000Z";
        String location = "CV Raman Hall, KKR & KSR Institute of Technology and Sciences (KITS), Vinjanampadu, Guntur, Andhra Pradesh 522019, India";
        StringBuilder sb = new StringBuilder();
        sb.append("Odyssey 2026 Boarding Pass ID: ").append(id != null ? id : "ODY-2026").append("\n");
        sb.append("Lead: ").append(leaderName != null ? leaderName : "").append(" (Roll: ").append(rollNumber != null ? rollNumber : "").append(", Ph: ").append(leaderPhone != null ? leaderPhone : "").append(")\n");
        sb.append("College: ").append(college != null ? college : "KITS Guntur").append(" · Dept: ").append(dept != null ? dept : "").append("\n");
        sb.append("Group Size: ").append(teamSize).append(" Members\n\n");
        sb.append("Tournament Master Schedule (Andhra Pradesh IST):\n");
        sb.append("• Day 1 (Oct 29): 09:00 AM – 05:00 PM (Rounds 1–6: Problem Drop, Customer Hunt, Strategy Challenge)\n");
        sb.append("• Day 2 (Oct 30): 09:00 AM – 05:30 PM (Rounds 7–9: Crisis Simulation, Investor Pitch, Awards Ceremony)\n\n");
        sb.append("Official WhatsApp Group: https://chat.whatsapp.com/LTBBY7HvMAJF1hROCei2J0\n");
        sb.append("Organised by KITS E-Cell · https://kitsecellgnt.com/");

        try {
            return "https://calendar.google.com/calendar/render?action=TEMPLATE"
                    + "&text=" + URLEncoder.encode(title, StandardCharsets.UTF_8)
                    + "&dates=" + dates
                    + "&ctz=Asia/Kolkata"
                    + "&details=" + URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8)
                    + "&location=" + URLEncoder.encode(location, StandardCharsets.UTF_8)
                    + "&add=" + URLEncoder.encode(getAllEmailsCsv(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "https://calendar.google.com/calendar/render?action=TEMPLATE&text=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        }
    }

    public String googleCalendarUrl() {
        return getGoogleCalendarUrl();
    }
}
