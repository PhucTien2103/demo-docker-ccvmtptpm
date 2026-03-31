package nhom13.vn.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "LeaveApprovals")
public class LeaveApproval implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "requestId")
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "managerId")
    private User manager; // Người duyệt

    private String decision; // Phê duyệt hoặc Từ chối
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date approvalTime;

    @Column(columnDefinition = "nvarchar(500)")
    private String note;

    public LeaveApproval() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public java.util.Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(java.util.Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}