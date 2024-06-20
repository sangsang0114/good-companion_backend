package org.sku.zero.application.redis;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.sku.zero.application.MailService;
import org.sku.zero.application.MemberService;
import org.sku.zero.domain.Member;
import org.sku.zero.domain.redis.NewShop;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewShopService {
    private final RedisTemplate<String, NewShop> redisTemplate;
    private final MemberService memberService;
    private final MailService mailService;

    public void saveNewShop(NewShop newShop) {
        redisTemplate.opsForValue().set("new_shop:"+newShop.getShopId(), newShop, Duration.ofDays(1));
    }

    public List<NewShop> listNewShops() {
        List<NewShop> newshops = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match("new_shop:*").count(100).build();

        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        while (cursor.hasNext()) {
            String key = cursor.next();
            NewShop newShop = redisTemplate.opsForValue().get(key);
            newshops.add(newShop);
        }
        return newshops;
    }

    public void sendNewsShopInfoMail(){
        List<Member> members = memberService.findMemberByEmailFlagIsTrue();
        List<String> emails = members.stream().map(Member::getEmail).toList();
        List<NewShop> newShops = listNewShops();
        if(newShops.isEmpty())
            return;

        emails.forEach(email -> {
            try {
                System.out.println(email);
                mailService.sendNewShopByBatchMail(email,newShops);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
