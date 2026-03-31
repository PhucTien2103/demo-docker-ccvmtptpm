package nhom13.vn.entity;
import java.io.Serializable;
import jakarta.persistence.*;
@Entity
@Table(name = "Notifications")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;

    private boolean isRead;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date sentTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public java.util.Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(java.util.Date sentTime) {
        this.sentTime = sentTime;
    }
}
