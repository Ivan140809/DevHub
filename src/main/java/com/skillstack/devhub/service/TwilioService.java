package com.skillstack.devhub.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TwilioService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.messaging-service-sid}")
    private String messagingServiceSid;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Map<String, String> pendingCodes = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendResetCode(String toPhone, String email) {
        String code = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        pendingCodes.put(email, code);

        Message.creator(
                new PhoneNumber(toPhone),
                messagingServiceSid,
                "DevHub - Tu codigo de verificacion es: " + code + ". Expira en 10 minutos."
        ).create();
    }

    public boolean verifyCode(String email, String userCode) {
        String stored = pendingCodes.get(email);
        if (stored != null && stored.equals(userCode)) {
            pendingCodes.remove(email);
            return true;
        }
        return false;
    }
}
