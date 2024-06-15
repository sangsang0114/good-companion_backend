package org.sku.zero.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.sku.zero.domain.enums.MemberRole;
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

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "email_flag")
    private Integer emailFlag;

    @Column(name = "fcm_flag")
    private Integer fcmFlag;

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

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateNotificationSetting(Integer emailFlag, Integer fcmFlag) {
        this.emailFlag = emailFlag;
        this.fcmFlag = fcmFlag;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
