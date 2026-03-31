package nhom13.vn.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "LeaveRequests")
public class LeaveRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Temporal(TemporalType.DATE) // Lưu trữ ngày [5, 12]
    private java.util.Date startDate;

    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String reason;

    private String status; // Chờ duyệt, Đã duyệt, Từ chối

    @Transient
    private String reviewerComment;

    // Constructors, Getters, Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getStartDate() { return (Date) startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return (Date) endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReviewerComment() { return reviewerComment; }
    public void setReviewerComment(String reviewerComment) { this.reviewerComment = reviewerComment; }
}