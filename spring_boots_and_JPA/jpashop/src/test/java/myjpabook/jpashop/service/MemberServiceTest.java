package myjpabook.jpashop.service;

import myjpabook.jpashop.domain.Member;
import myjpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        Member member = new Member();
        member.setName("cheon");

        Long saveId = memberService.join(member);

        Assertions.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() {
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("cheon");
        member2.setName("cheon");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생해야함");
    }
}