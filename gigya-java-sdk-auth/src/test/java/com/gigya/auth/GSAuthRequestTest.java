package com.gigya.auth;

import com.gigya.socialize.GSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(PowerMockRunner.class)
public class GSAuthRequestTest {

    @Test
    public void test_signedRequest() {

        // Test partner private key.
        String PRIVATE_KEY = "MIIEoQIBAAKCAQEAy7d/1DUA+2hxI42XxsAsa6zcgOCFJCWfe0wMitRPnBatkx9ZUOGaZQ+UzQxSrMvjiC6C6jLNwYrsyQ6T+KtjlM44IGIdjflh+GM67czaFVWpkihvnWGsC9yV591vpV5qzOTop9QzjRZ52g2xZ4Uws/eNvGMJAx7cvHVQAGBPFXMufmqMNqYvP42hvuik+z+fcRO9l/7u+4YD5D8kROhiLGjdLgoveSojKUfFQDArvKgwlaPmm5uKw6wEzxB/dw1cOJ4mZ50n1USp2zO/OAPoXPZtFyCcHhZnX2aKip5sJ2WiBURSRXV9SeNhXpmbfR28BqB3kaLMG9011hhQJunAdQIDAQABAoH/Wx03jzIlvkx4Te6Gc95//6jQ9tN0uTYanorlETLkaGu8NpFO4V4GMC1H4G4ijCalHcOvyg/u9yGrxkE+7Xm6kFOrxtAbZ+SibPX05Wc6I1CtBzIOudV1ndiLM6lWxh/0pK+9X1MJu5DR7zUwo5xurA+M1TV2Um77S08s+P4aYPzWdUaVCKVAqd0JTmAVaL9rvpio85ukZRsAqPnqj61mfyW1WWwcuyz4mVZSCk6i7/AcLaXlobzhKzCclmzw8BBPalBkPm8Ftnze5bAFtkC4dTBGJ5vipfBvngGEfcUz1K4xg/VMFnV3+a9dDqRdOpm6ImUGaFKxxlbjk7y1pf4hAoGBANP2Rd8/6dKaZgKQdrdvXVd5ms6f1BmDseu4dP8oeZzg3kuvsg3LaTuNiSgOogGR7dy64wcQla7b2anfN5SEd+QZdM5C8RXPLy5cUPTXWJ4CEoq/XXhA50+KYEnZ2H7DDRs4sDDj/4Kk2l6n+UdbripNWZpH8jNtQVsX63n+PTBRAoGBAPYKrzSn+TTT2OvwCjWS9+a58v+hfVlfzH+K29xdD9WssTyKWVXgiI6uD0iBwk+hDy60KufcVBY+3OhKUHgb73cM/0u3ghzgDXhmKWX9ra1grtMK43g/86fqZPkmoJpw55BsGZRsET3dtGkE3f04/aQz+V9oNoRXVi2niPtOOwjlAoGAbTuneovHhl9HXt7wL0xXADONEhrQBW4XDPF8adqEAdLo/HxI+E7xpl0kZ95Soxh/SpeNVCC54ukt3RtJ22IBHy33QizBbYneOLgeiG9KHfPXdmV0V/qquhUH1kdMCNegM30dX7TAAqXOW6WZE6stsGM4YAffy5zsZ3OGSNI62mECgYEAhR04t3Ndh7BJ/zRKQbv29VSLXLSBPdZvrF1zICEVTRR2e3uaY7TsDM2tJRxXBX/s1+bQA8uXjsWJ/P8f1CvA/hcBFHT/JyItB4O2SCDcUx4o17NfaBKpf6J36Lh2UbheACwMFtn05hTJ47unYrXvvGQGOhEG1cjurhqjKNulYuUCgYAhkvsyR0PaXNtIAlhdFX2V54wvFOuWCvez7Mk5unsXCYFq5Uoar7CMBBl92H0z6+6HLhPn8k4H3SqNecAQlzzc3gr05F/AFkEivgLY7DsaYmj8aw/V1qi/Pbh4pXBvExL/B937okJfR8aOzNWwUEA/tLvUejrdbUSHu0bNsB4/cQ==";

        // Generate auth request.
        final GSAuthRequest request = new GSAuthRequest("ANiHp6OEaqFZ", PRIVATE_KEY, "accounts.getAccountInfo");
        request.setParam("UID", "cae35bb4103a4466b81fcefd6f5f6721");
        request.setAPIDomain("us1-st1.gigya.com");
        final GSResponse response = request.send();
        if (response.getErrorCode() == 0) {
            System.out.println(response.getData().toJsonString());
        } else {
            System.out.println(response.getResponseText());
        }

        // Assert.
        //assertEquals(0, response.getErrorCode());
    }
}
