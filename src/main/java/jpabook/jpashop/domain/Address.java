package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter // 값이라는거 자체는 변경이되면 안되도록 설계되어야 한다. 그래서 생성할때만 값이 설정되고 그 후로는 변경이 불가능하게 해야한다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

//    @Builder
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
