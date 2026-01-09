package com.monolux.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Date;

@Slf4j
public class RandomUtil {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    private static final int DEFAULT_PASSWORD_LENGTH = 10;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    public RandomUtil() {

    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    public String generateRandomPassword() {
        char[] charSetDigit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        char[] charSetUpperCase = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        char[] charSetLowerCase = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        char[] charSetSpecial = new char[] { '!', '@', '#', '$', '^' };
        char[] charSetAll = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '^' };
        StringBuilder sb = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        sb.append(this.generateRandomInner(charSetAll, RandomUtil.DEFAULT_PASSWORD_LENGTH - 4));

        // 숫자 삽입
        int idxRandom = sr.nextInt(sb.length());
        sb.insert(idxRandom, charSetDigit[sr.nextInt(charSetDigit.length)]);

        // 대문자 삽입
        idxRandom = sr.nextInt(sb.length());
        sb.insert(idxRandom, charSetUpperCase[sr.nextInt(charSetUpperCase.length)]);

        // 소문자 삽입
        idxRandom = sr.nextInt(sb.length());
        sb.insert(idxRandom, charSetLowerCase[sr.nextInt(charSetLowerCase.length)]);

        // 특수문자 삽입
        idxRandom = sr.nextInt(sb.length());
        sb.insert(idxRandom, charSetSpecial[sr.nextInt(charSetSpecial.length)]);

        return sb.toString();
    }

    public String generateRandomSecretKey(final int length) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };
        return this.generateRandomInner(charSet, length);
    }

    public String generateRandomNoString(final int length) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return this.generateRandomInner(charSet, length);
    }

    public String generateRandomInner(final char[] charSet, final int length) {
        if (charSet != null && charSet.length > 0) {
            StringBuilder sb = new StringBuilder();
            SecureRandom sr = new SecureRandom();
            sr.setSeed(new Date().getTime());

            for (int i = 0; i < length; i++) {
                sb.append(charSet[sr.nextInt(charSet.length)]);
            }

            return sb.toString();
        }

        return null;
    }

    // endregion
}