package com.project.Restaurant.Member.consumer;

import com.project.Restaurant.Place.Customer.PlaceCustomer;
import com.project.Restaurant.Post.Post;
import com.project.Restaurant.PostComment.PostComment;
import com.project.Restaurant.Reservation.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;    //  유저아이디

    @Column(unique = true)
    private String nickname;  //  유저닉네임

    @Column(unique = true)
    private String email;   //  유저이메일

    @Column(columnDefinition = "TEXT")
    private String password;    //  유저비밀번호

    private LocalDateTime signupDate; //  유저가입일

    private Boolean memberActivation; //  유저활성화

    private String authority;   //  유저권한

    @OneToMany(mappedBy = "customer")
    private List<PlaceCustomer> placeCustomerList;

    @OneToMany(mappedBy = "customer")
    private List<Post> postList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Reservation> reservationList;

    private String provider;

    private String providerId;
}
