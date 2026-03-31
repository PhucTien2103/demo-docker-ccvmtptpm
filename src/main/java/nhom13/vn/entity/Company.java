package nhom13.vn.entity;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "Companies")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "nvarchar(255) not null")
    private String name;

    @Column(columnDefinition = "nvarchar(500)")
    private String address;

    private String contactInfo;

    @OneToMany(mappedBy = "company")
    private List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
