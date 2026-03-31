package nhom13.vn.entity;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "LeaveBalances")
public class LeaveBalance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private User user;

    private int totalDays;
    private int usedDays;
    private int remainingDays;

    @Column(nullable = false)
    private int lastResetYear;

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (lastResetYear <= 0) {
            lastResetYear = LocalDate.now().getYear();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(int usedDays) {
        this.usedDays = usedDays;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public int getLastResetYear() {
        return lastResetYear;
    }

    public void setLastResetYear(int lastResetYear) {
        this.lastResetYear = lastResetYear;
    }
}