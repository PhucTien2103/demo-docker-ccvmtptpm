package nhom13.vn.entity;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(columnDefinition = "nvarchar(200)")
    private String fullName;

    private String role; // Super Admin, Manager, Employee
    private int status; // Trạng thái tài khoản

    @ManyToOne
    @JoinColumn(name = "companyId") // Mỗi người dùng thuộc một công ty [5, 9]
    private Company company;

    @OneToMany(mappedBy = "user")
    private List<LeaveRequest> leaveRequests;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

    // Constructors, Getters, Setters [10]
    
    
}