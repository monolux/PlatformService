package com.monolux.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QRCodeUtil {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final MediaType MEDIA_TYPE = MediaType.IMAGE_PNG;

    // endregion

    // region ▒▒▒▒▒ User Define Methods ▒▒▒▒▒

    public static byte[] createQRCode(final String content, final int width, final int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, QRCodeUtil.MEDIA_TYPE.getSubtype(), pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    // endregion
}