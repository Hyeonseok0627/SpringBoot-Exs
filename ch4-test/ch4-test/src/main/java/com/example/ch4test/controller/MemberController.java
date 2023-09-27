package com.example.ch4test.controller;


//@RequestMapping("/members")
//@Controller
//@RequiredArgsConstructor
//public class MemberController {
//
//    private final MemberService memberService;
//    private final PasswordEncoder passwordEncoder;
//
//    @GetMapping(value = "/new")
//    public String memberForm(Model model){
//        model.addAttribute("memberFormDto", new MemberFormDto());
//        return "member/memberForm";
//    }
//
//    @PostMapping(value = "/new")
//    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors()){
//            return "member/memberForm";
//        }
//
//        try {
//            Member member = Member.createMember(memberFormDto, passwordEncoder);
//            memberService.saveMember(member);
//        } catch (IllegalStateException e){
//            model.addAttribute("errorMessage", e.getMessage());
//            return "member/memberForm";
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping(value = "/login")
//    public String loginMember(){
//        return "/member/memberLoginForm";
//    }
//
//    @GetMapping(value = "/login/error")
//    public String loginError(Model model){
//        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
//        return "/member/memberLoginForm";
//    }
//
//}