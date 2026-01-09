package com.monolux.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialNoUtil {
    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    /**
     * 주민번호 뒷자리 첫번째 숫자 생성
     * @param yearOfBirth : 출생년도
     * @param isMale      : 성별
     * @param isForeigner : 내/외국인 여부
     * @return : 주민번호 뒷자리 첫번째 숫자
     */
    public static int getSocialNoDigit(final int yearOfBirth, final boolean isMale, final boolean isForeigner) {
        if (yearOfBirth >= 2000) {
            if (isMale) {
                if (isForeigner) {
                    return 7;   // 2000년도 출생 외국인 남성
                } else {
                    return 3;   // 2000년도 출생 내국인 남성
                }
            } else {
                if (isForeigner) {
                    return 8;   // 2000년도 출생 외국인 여성
                } else {
                    return 4;   // 2000년도 출생 내국인 남성
                }
            }
        } else if (yearOfBirth >= 1900) {
            if (isMale) {
                if (isForeigner) {
                    return 5;   // 1900 출생 외국인 남성
                } else {
                    return 1;   // 1900 출생 내국인 남성
                }
            } else {
                if (isForeigner) {
                    return 6;   // 1900 출생 외국인 여성
                } else {
                    return 2;   // 1900 출생 내국인 여성
                }
            }
        } else if (yearOfBirth >= 1800) {
            if (isMale) {
                if (isForeigner) {
                    throw new IllegalArgumentException();
                } else {
                    return 9;   // 1800 출생 내국인 남성
                }
            } else {
                if (isForeigner) {
                    throw new IllegalArgumentException();
                } else {
                    return 0;   // 1800 출생 내국인 여성
                }
            }
        }

        throw new IllegalArgumentException();
    }

    // endregion
}