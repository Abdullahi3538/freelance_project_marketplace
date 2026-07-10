    package Backend.entity.Auth;

    import Backend.Enmu.Role;
    import Backend.entity.Freelancer.FreelancerProfile;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "users")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 100)
        private String fullName;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private  Role role;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {
            createdAt = LocalDateTime.now();
        }

        @OneToOne(mappedBy = "user")
        @JsonIgnore
        private FreelancerProfile freelancerProfile;

    }
