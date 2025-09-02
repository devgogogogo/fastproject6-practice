package com.fastcampus.crash.model.entity;

import com.fastcampus.crash.model.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "\"user\"",
        indexes = {
                //회원가입 로직을 만들때 비즈니스 로직 상에서 이 유저네임에 대한 중복 확인 처리를 해줄 예정이지만,
                //데이터 베이스 레벨에서 유니크 인덱스로 만들어주면, 동일한 유저 네임을 가진 사용자가 중복되어 생성되는 일을 데이터베이스 레벨에서 원천 봉쇄할 수 있다는 장점이 있따.
                @Index(name = "user_username_idx", columnList = "username", unique = true)
        })
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private ZonedDateTime createdDateTime;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && role == that.role && Objects.equals(createdDateTime, that.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, name, email, role, createdDateTime);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals(Role.ADMIN)) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()),
                    new SimpleGrantedAuthority(Role.ADMIN.name()),
                    new SimpleGrantedAuthority("ROLE_" + Role.USER.name()), //이건 안넣어도 된다
                    new SimpleGrantedAuthority(Role.USER.name()));//이건 안넣어도 된다
        } else {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + Role.USER.name()),//이렇게 해야 .hasRole(Role.ADMIN.name())도 검증에 통과가 된다.
                    new SimpleGrantedAuthority(Role.USER.name())); //.hasAuthority(Role.ADMIN.name()) (O) , .hasRole(Role.ADMIN.name())(X))
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    /*
    로그인을 하면 redis에
    {
  "userId": 1,
  "username": "jayce",
  "password": "$2a$10$XTgwZs.SzRLutG6jAcGNi.bTVjD1NBWFRvXWyhMpgMlJOVoWBLpMu",
  "name": "Dev Jayce",
  "email": "jayce@crash.com",
  "role": "USER",
  "createdDateTime": 1756793518.712332,
  "enabled": true,
  "authorities": [
    {
      "authority": "ROLE_USER"
    },
    {
      "authority": "USER"
    }
  ],
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "accountNonExpired": true
} 이런식으로 데이터 세팅이 되어서 나오는데 우리가 사용하는 값도 아니고 레디스상에 데이터로 저장되길 원치 않기 떄문에 -->> @JsonIgnore 를 붙여주면 안나온다.
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public static UserEntity of(String username, String password, String name, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setRole(Role.USER);
        return userEntity;
    }

    @PrePersist
    private void prePersist() {
        createdDateTime = ZonedDateTime.now();
    }

}
