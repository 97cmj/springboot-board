package com.cmj.myproject.domain;

//도메인이란 JPA 에서 사용하는 용어로서, 테이블과 매핑되는 객체를 의미한다.
//JPA 는 객체와 테이블을 매핑할 때, 어노테이션을 사용한다.

import com.cmj.myproject.dto.UserForm;
import lombok.Getter;

import javax.persistence.*;

//@Entity 어노테이션을 사용하여 User 클래스가 테이블과 매핑됨을 명시한다.
@Entity
@Getter
public class User {

    public User() {
    }


    public User(UserForm userForm) {
        this.email = userForm.getEmail();
        this.userName = userForm.getUserName();
        this.password = userForm.getPassword();
    }

    public static User createUser(UserForm userForm) {
        return new User(userForm);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    // @Id 어노테이션을 사용하여 테이블의 기본키임을 명시한다.
    // @GeneratedValue 어노테이션을 사용하여 기본키의 생성 전략을 명시한다.
    // @Column 어노테이션을 사용하여 테이블의 컬럼임을 명시한다.

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String userName;

    @Column(length = 50, nullable = false)
    private String password;

}
