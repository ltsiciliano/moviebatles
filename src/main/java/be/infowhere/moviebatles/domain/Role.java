package be.infowhere.moviebatles.domain;

import javax.persistence.*;
import java.util.List;

@Table(name = "ROLES")
@Entity
public class Role {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long idRole;

    @Column(nullable = false)
    private String authority;

    @ManyToMany(mappedBy = "grantedRole")
    private List<User> users;

    public Role(String authority) {
        this.authority = authority;
    }

    public Role() {
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long id) {
        this.idRole = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
