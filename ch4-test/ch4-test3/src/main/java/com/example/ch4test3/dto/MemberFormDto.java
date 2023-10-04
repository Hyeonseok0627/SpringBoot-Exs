package com.example.ch4test3.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
// 회원가입 시, 입력받은 데이터를 담을 용도로 사용, 박스로 비유
public class MemberFormDto {

    // 값이 필수라는 의미, 비워 두면 안됨.
    // message -> 만약, 값이 비웠을 경우, 서버에서, 해당 에러 메세지 확인용.
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    // 값이 필수라는 의미, 비워 두면 안됨. 공백 유무
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
}