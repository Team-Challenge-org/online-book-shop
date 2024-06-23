package org.teamchallenge.bookshop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ScheduledTaskService {

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldRecords() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        entityManager.createQuery(
                        "DELETE FROM Cart c " +
                        "WHERE c.lastModified < :weekAgo AND c.isPermanent = false"
                )
                .setParameter("weekAgo", oneWeekAgo)
                .executeUpdate();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredTokens() {
        entityManager.createQuery(
                        "DELETE FROM Token t " +
                                "WHERE t.expiryDate < :now"
                )
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
}