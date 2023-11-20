package com.project.Restaurant.Member;

import com.project.Restaurant.Place.Place;
import com.project.Restaurant.Post.Post;
import com.project.Restaurant.Reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;    //  유저아이디

    private String password;    //  유저비밀번호

    @Column(unique = true)
    private String memberNickName;  //  유저닉네임

    @Column(unique = true)
    private String email;   //  유저이메일

    private LocalDateTime signupDate; //  유저가입일

    @OneToMany(mappedBy = "member")
    private List<Place> placeList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Reservation> reservationList;

    private String authority; //  유저권한

    private Boolean memberActivation; //  유저활성화

}
