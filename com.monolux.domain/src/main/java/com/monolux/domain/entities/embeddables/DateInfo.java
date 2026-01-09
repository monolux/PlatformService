package com.monolux.domain.entities.embeddables;

import com.monolux.domain.exceptions.DomainIllegalArgumentException;
import com.monolux.utils.ValidationUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.LocalDate;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Embeddable
public class DateInfo {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Transient
    private LocalDate date;

    @ToString.Include(name = "year", rank = 1)
    @Column(nullable = false)
    private Integer year;

    @ToString.Include(name = "month", rank = 2)
    @Column(nullable = false)
    private Byte month;

    @ToString.Include(name = "dayOfMonth", rank = 3)
    @Column(nullable = false)
    private Byte dayOfMonth;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Builder
    public DateInfo(@NonNull final Integer year,
                    @NonNull final Byte month,
                    @NonNull final Byte dayOfMonth) {
        if (!ValidationUtil.isValidYearMonthDay(year, month, dayOfMonth)) {
            throw new DomainIllegalArgumentException("year, month, dayOfMonth is not valid.");
        }

        this.date = LocalDate.of(year, month, dayOfMonth);

        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    // endregion
}