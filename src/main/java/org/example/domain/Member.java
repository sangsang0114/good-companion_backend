package org.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.enums.MemberRole;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Member extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "is_available")
    private Integer isAvailable;

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
