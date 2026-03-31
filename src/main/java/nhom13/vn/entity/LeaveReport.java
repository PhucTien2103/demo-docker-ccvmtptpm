package nhom13.vn.entity;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "LeaveReports")
public class LeaveReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "createdById")
    private User createdBy;

    @Column(columnDefinition = "nvarchar(255)")
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date reportDate;

    // Constructors, Getters, Setters
}