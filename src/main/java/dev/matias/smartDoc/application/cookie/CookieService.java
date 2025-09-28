package dev.matias.smartDoc.application.cookie;

import dev.matias.smartDoc.application.auth.utils.CookieOptions;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public ResponseCookie createCookie(String token, String name, CookieOptions opts) {
        return ResponseCookie.from(name, token)
                .httpOnly(opts.httpOnly)
                .secure(opts.secure)
                .sameSite(opts.sameSite)
                .path(opts.path)
                .maxAge(opts.maxAge)
                .build();
    }

}
