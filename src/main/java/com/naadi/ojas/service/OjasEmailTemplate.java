package com.naadi.ojas.service;

public class OjasEmailTemplate {

    private OjasEmailTemplate() {
    }

    public static String buildEmail(
            String title,
            String greeting,
            String message,
            String buttonText,
            String buttonLink,
            String footerNote
    ) {
        String safeButton = "";

        if (buttonText != null && buttonLink != null && !buttonLink.isBlank()) {
            safeButton = """
                    <div style="margin:26px 0;">
                        <a href="%s"
                           style="display:inline-block;background:#004aad;color:#ffffff;
                                  text-decoration:none;padding:14px 24px;border-radius:999px;
                                  font-weight:800;font-family:Arial,sans-serif;">
                            %s
                        </a>
                    </div>
                    """.formatted(buttonLink, buttonText);
        }

        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background:#f4f8ff;font-family:Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f8ff;padding:28px 12px;">
                        <tr>
                            <td align="center">
                                <table width="100%%" cellpadding="0" cellspacing="0"
                                       style="max-width:620px;background:#ffffff;border-radius:26px;
                                              overflow:hidden;box-shadow:0 18px 45px rgba(0,74,173,0.12);">
                                    
                                    <tr>
                                        <td style="background:#b4dbff;padding:28px;text-align:center;">
                                            <h1 style="margin:0;color:#004aad;font-size:30px;">
                                                🎨 Ojas by Tejas
                                            </h1>
                                            <p style="margin:8px 0 0;color:#004aad;font-weight:700;">
                                                Trunkful of Colors, Brushful of Dreams
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding:32px;color:#004aad;">
                                            <h2 style="margin:0 0 16px;font-size:26px;color:#004aad;">
                                                %s
                                            </h2>
                
                                            <p style="font-size:16px;line-height:1.6;margin:0 0 16px;">
                                                %s
                                            </p>
                
                                            <div style="font-size:15px;line-height:1.7;color:#004aad;">
                                                %s
                                            </div>
                
                                            %s
                
                                            <p style="margin-top:24px;font-size:14px;line-height:1.6;color:#3366aa;">
                                                %s
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="background:#fff3c9;padding:22px;text-align:center;color:#004aad;">
                                            <p style="margin:0;font-size:14px;font-weight:700;">
                                                Keep creating. Keep coloring. Keep dreaming.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                title,
                greeting,
                message,
                safeButton,
                footerNote == null ? "" : footerNote
        );
    }
}