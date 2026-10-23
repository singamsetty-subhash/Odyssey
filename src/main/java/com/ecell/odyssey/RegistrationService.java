package com.ecell.odyssey;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;

    public RegistrationService(RegistrationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Registration register(
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
        String member5Role
    ) {
        String groupId = generateUniqueId();
        int safeTeamSize = Math.max(3, Math.min(5, teamSize));
        int safeAmount = safeTeamSize * 100;
        String cleanTeamName = teamName != null && !teamName.isBlank() ? teamName.trim() : leaderName.trim() + "'s Group";
        String cleanTrack = track != null ? track.trim() : "General";
        String cleanStage = stage != null ? stage.trim() : "Registered";
        String cleanPitch = pitchSummary != null ? pitchSummary.trim() : "";
        String cleanDeck = deckUrl != null ? deckUrl.trim() : "";
        String cleanCollege = college != null && !college.isBlank() ? college.trim() : "KITS Singapur";
        String cleanUtr = utr != null ? utr.trim() : "";
        String cleanPaymentImg = paymentImagePath != null ? paymentImagePath.trim() : "";
        LocalDateTime now = LocalDateTime.now();

        // 1. Leader Registration Record
        Registration leaderReg = new Registration(
            groupId,
            cleanTeamName,
            cleanTrack,
            cleanStage,
            cleanPitch,
            cleanDeck,
            leaderName != null ? leaderName.trim() : "",
            leaderEmail != null ? leaderEmail.trim() : "",
            leaderPhone != null ? leaderPhone.trim() : "",
            rollNumber != null ? rollNumber.trim() : "",
            dept != null ? dept.trim() : "",
            year != null ? year.trim() : "",
            section != null ? section.trim() : "",
            cleanCollege,
            amountPaid > 0 ? amountPaid : safeAmount,
            cleanUtr,
            cleanPaymentImg,
            safeTeamSize,
            member2Name != null ? member2Name.trim() : "",
            member2Email != null ? member2Email.trim() : "",
            member2Phone != null ? member2Phone.trim() : "",
            member2RollNumber != null ? member2RollNumber.trim() : "",
            member2Role != null ? member2Role.trim() : "",
            member3Name != null ? member3Name.trim() : "",
            member3Email != null ? member3Email.trim() : "",
            member3Phone != null ? member3Phone.trim() : "",
            member3RollNumber != null ? member3RollNumber.trim() : "",
            member3Role != null ? member3Role.trim() : "",
            member4Name != null ? member4Name.trim() : "",
            member4Email != null ? member4Email.trim() : "",
            member4Phone != null ? member4Phone.trim() : "",
            member4RollNumber != null ? member4RollNumber.trim() : "",
            member4Role != null ? member4Role.trim() : "",
            member5Name != null ? member5Name.trim() : "",
            member5Email != null ? member5Email.trim() : "",
            member5Phone != null ? member5Phone.trim() : "",
            member5RollNumber != null ? member5RollNumber.trim() : "",
            member5Role != null ? member5Role.trim() : "",
            "Leader",
            groupId,
            now
        );
        Registration savedLead = repository.save(leaderReg);

        // 2. Persist Member 2 as Individual Database Record
        if (member2Name != null && !member2Name.isBlank()) {
            Registration m2Reg = new Registration(
                groupId + "-M2",
                cleanTeamName,
                cleanTrack,
                cleanStage,
                cleanPitch,
                cleanDeck,
                member2Name.trim(),
                member2Email != null ? member2Email.trim() : "",
                member2Phone != null ? member2Phone.trim() : "",
                member2RollNumber != null ? member2RollNumber.trim() : "",
                dept != null ? dept.trim() : "",
                year != null ? year.trim() : "",
                section != null ? section.trim() : "",
                cleanCollege,
                0,
                cleanUtr,
                cleanPaymentImg,
                safeTeamSize,
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "Member 2",
                groupId,
                now
            );
            repository.save(m2Reg);
        }

        // 3. Persist Member 3 as Individual Database Record
        if (member3Name != null && !member3Name.isBlank()) {
            Registration m3Reg = new Registration(
                groupId + "-M3",
                cleanTeamName,
                cleanTrack,
                cleanStage,
                cleanPitch,
                cleanDeck,
                member3Name.trim(),
                member3Email != null ? member3Email.trim() : "",
                member3Phone != null ? member3Phone.trim() : "",
                member3RollNumber != null ? member3RollNumber.trim() : "",
                dept != null ? dept.trim() : "",
                year != null ? year.trim() : "",
                section != null ? section.trim() : "",
                cleanCollege,
                0,
                cleanUtr,
                cleanPaymentImg,
                safeTeamSize,
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "Member 3",
                groupId,
                now
            );
            repository.save(m3Reg);
        }

        // 4. Persist Member 4 as Individual Database Record (if size >= 4)
        if (safeTeamSize >= 4 && member4Name != null && !member4Name.isBlank()) {
            Registration m4Reg = new Registration(
                groupId + "-M4",
                cleanTeamName,
                cleanTrack,
                cleanStage,
                cleanPitch,
                cleanDeck,
                member4Name.trim(),
                member4Email != null ? member4Email.trim() : "",
                member4Phone != null ? member4Phone.trim() : "",
                member4RollNumber != null ? member4RollNumber.trim() : "",
                dept != null ? dept.trim() : "",
                year != null ? year.trim() : "",
                section != null ? section.trim() : "",
                cleanCollege,
                0,
                cleanUtr,
                cleanPaymentImg,
                safeTeamSize,
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "Member 4",
                groupId,
                now
            );
            repository.save(m4Reg);
        }

        // 5. Persist Member 5 as Individual Database Record (if size >= 5)
        if (safeTeamSize >= 5 && member5Name != null && !member5Name.isBlank()) {
            Registration m5Reg = new Registration(
                groupId + "-M5",
                cleanTeamName,
                cleanTrack,
                cleanStage,
                cleanPitch,
                cleanDeck,
                member5Name.trim(),
                member5Email != null ? member5Email.trim() : "",
                member5Phone != null ? member5Phone.trim() : "",
                member5RollNumber != null ? member5RollNumber.trim() : "",
                dept != null ? dept.trim() : "",
                year != null ? year.trim() : "",
                section != null ? section.trim() : "",
                cleanCollege,
                0,
                cleanUtr,
                cleanPaymentImg,
                safeTeamSize,
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "Member 5",
                groupId,
                now
            );
            repository.save(m5Reg);
        }

        return savedLead;
    }

    private String generateUniqueId() {
        String id;
        do {
            id = "ODY-" + ThreadLocalRandom.current().nextInt(1000, 9999);
        } while (repository.existsById(id));
        return id;
    }

    public Optional<Registration> findById(String id) {
        return repository.findById(id);
    }

    public List<Registration> getAll() {
        List<Registration> list = repository.findAll();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Identify each team's latest registeredAt timestamp and primary team key
        // Group all members of the same team together using their clean team name or groupId.
        Map<String, LocalDateTime> teamRegisteredAtMap = new HashMap<>();
        for (Registration r : list) {
            String key = extractTeamGroupKey(r);
            LocalDateTime dt = r.getRegisteredAt() != null ? r.getRegisteredAt() : LocalDateTime.MIN;
            teamRegisteredAtMap.merge(key, dt, (oldVal, newVal) -> newVal.isAfter(oldVal) ? newVal : oldVal);
        }

        // 2. Sort registrations:
        // Primary: Team registration time DESC (newest teams appear first)
        // Secondary: Team key (keeps all team members strictly together)
        // Tertiary: Role priority (Leader = 1, Member 2 = 2, Member 3 = 3, Member 4 = 4, Member 5 = 5)
        // Quaternary: Pass ID ASC (deterministic tiebreaker)
        return list.stream()
            .sorted(Comparator
                .<Registration, LocalDateTime>comparing(
                    r -> teamRegisteredAtMap.getOrDefault(extractTeamGroupKey(r), LocalDateTime.MIN),
                    Comparator.reverseOrder()
                )
                .thenComparing(this::extractTeamGroupKey)
                .thenComparingInt(this::getParticipantRoleRank)
                .thenComparing(r -> r.getId() != null ? r.getId() : "")
            )
            .toList();
    }

    public String extractTeamGroupKey(Registration r) {
        if (r == null) return "";
        if (r.getTeamName() != null && !r.getTeamName().isBlank()) {
            return r.getTeamName().trim().toLowerCase();
        }
        if (r.getGroupId() != null && !r.getGroupId().isBlank()) {
            return r.getGroupId().trim();
        }
        if (r.getId() != null && !r.getId().isBlank()) {
            return r.getId().replaceAll("-M\\d+$", "").trim();
        }
        return "";
    }

    public int getParticipantRoleRank(Registration r) {
        if (r == null) return 99;
        String role = r.getParticipantRole();
        if (role != null) {
            String lower = role.trim().toLowerCase();
            if (lower.contains("leader") || lower.equals("lead")) return 1;
            if (lower.contains("member 2") || lower.endsWith("2")) return 2;
            if (lower.contains("member 3") || lower.endsWith("3")) return 3;
            if (lower.contains("member 4") || lower.endsWith("4")) return 4;
            if (lower.contains("member 5") || lower.endsWith("5")) return 5;
        }
        String id = r.getId();
        if (id != null) {
            if (!id.contains("-M")) return 1;
            if (id.endsWith("-M2")) return 2;
            if (id.endsWith("-M3")) return 3;
            if (id.endsWith("-M4")) return 4;
            if (id.endsWith("-M5")) return 5;
        }
        return 10;
    }

    public long getTotalCount() {
        return repository.count();
    }

    public boolean isTeamNameTaken(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return false;
        }
        return repository.existsByTeamNameIgnoreCase(teamName.trim());
    }

    public java.util.Map<String, Long> getDepartmentCounts() {
        List<Registration> list = repository.findAll();
        return list.stream()
            .map(r -> (r.getDept() != null && !r.getDept().isBlank()) ? r.getDept().trim() : "Other")
            .collect(java.util.stream.Collectors.groupingBy(
                d -> d,
                java.util.LinkedHashMap::new,
                java.util.stream.Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed()
                .thenComparing(java.util.Map.Entry::getKey))
            .collect(java.util.stream.Collectors.toMap(
                java.util.Map.Entry::getKey,
                java.util.Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));
    }

    public java.util.Optional<Registration> verifyAndFindTicket(String teamName, String leaderName, String email, String utr) {
        if (teamName == null || leaderName == null || email == null || utr == null) {
            return java.util.Optional.empty();
        }
        String cleanTeam = teamName.trim();
        String cleanLeader = leaderName.trim();
        String cleanEmail = email.trim();
        String cleanUtr = utr.trim();

        if (cleanTeam.isEmpty() || cleanLeader.isEmpty() || cleanEmail.isEmpty() || cleanUtr.isEmpty()) {
            return java.util.Optional.empty();
        }

        List<Registration> all = repository.findAll();
        return all.stream()
            .filter(r -> r.getTeamName() != null && r.getTeamName().trim().equalsIgnoreCase(cleanTeam))
            .filter(r -> r.getLeaderName() != null && r.getLeaderName().trim().equalsIgnoreCase(cleanLeader))
            .filter(r -> r.getLeaderEmail() != null && r.getLeaderEmail().trim().equalsIgnoreCase(cleanEmail))
            .filter(r -> r.getUtr() != null && r.getUtr().trim().equalsIgnoreCase(cleanUtr))
            .min((r1, r2) -> {
                boolean r1IsLead = !r1.getId().contains("-M");
                boolean r2IsLead = !r2.getId().contains("-M");
                if (r1IsLead && !r2IsLead) return -1;
                if (!r1IsLead && r2IsLead) return 1;
                return 0;
            });
    }

    @Transactional
    public boolean deleteRegistration(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        Optional<Registration> opt = repository.findById(id.trim());
        if (opt.isEmpty()) {
            return false;
        }
        Registration reg = opt.get();
        // If this record is the group leader (id matches groupId or is leader), delete all associated records
        String groupId = reg.getGroupId();
        if (groupId != null && !groupId.isBlank() && reg.getId().equalsIgnoreCase(groupId)) {
            List<Registration> groupRecords = repository.findAll().stream()
                .filter(r -> groupId.equalsIgnoreCase(r.getGroupId()) || groupId.equalsIgnoreCase(r.getId()))
                .toList();
            repository.deleteAll(groupRecords);
        } else {
            repository.delete(reg);
        }
        return true;
    }
}
