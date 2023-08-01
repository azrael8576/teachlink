package com.wei.amazingtalker_recruit.feature.teacherschedule.utilities

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Unit tests for [WeekDataHelper].
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
class WeekDataHelperTest {

    private lateinit var weekDataHelper: WeekDataHelper

    @Before
    fun setUp() {
        weekDataHelper = WeekDataHelper()
    }

    @Test
    fun `getQueryDateUtc should reset to start of day when resetToStartOfDay is true`(){
        // Arrange
        val queryDateLocal = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC)

        // Act
        val result = weekDataHelper.getQueryDateUtc(queryDateLocal, true)

        // Assert
        assertThat(result).isEqualTo(OffsetDateTime.of(2023, 8, 1, 0, 0, 0, 0, ZoneOffset.UTC))
    }

    @Test
    fun `getQueryDateUtc should keep original time when resetToStartOfDay is false`() {
        // Arrange
        val queryDateLocal = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC)

        // Act
        val result = weekDataHelper.getQueryDateUtc(queryDateLocal, false)

        // Assert
        assertThat(result).isEqualTo(queryDateLocal)
    }

    @Test
    fun `getWeekStart should return previous Monday when called on Tuesday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC) // Tuesday

        // Act
        val result = weekDataHelper.getWeekStart(localTime)

        // Assert
        assertThat(result).isEqualTo(OffsetDateTime.of(2023, 7, 31, 10, 30, 0, 0, ZoneOffset.UTC)) // Monday
    }

    @Test
    fun `getWeekStart should return same day when called on Monday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 7, 31, 10, 30, 0, 0, ZoneOffset.UTC) // Monday

        // Act
        val result = weekDataHelper.getWeekStart(localTime)

        // Assert
        assertThat(result).isEqualTo(localTime) // Same day
    }

    @Test
    fun `getWeekEnd should return next Sunday when called on Tuesday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC) // Tuesday

        // Act
        val result = weekDataHelper.getWeekEnd(localTime)

        // Assert
        assertThat(result).isEqualTo(OffsetDateTime.of(2023, 8, 6, 10, 30, 0, 0, ZoneOffset.UTC)) // Sunday
    }

    @Test
    fun `getWeekEnd should return same day when called on Sunday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 8, 6, 10, 30, 0, 0, ZoneOffset.UTC) // Sunday

        // Act
        val result = weekDataHelper.getWeekEnd(localTime)

        // Assert
        assertThat(result).isEqualTo(localTime) // Same day
    }

    @Test
    fun `getWeekDateText should format week date text correctly when given start and end dates`() {
        // Arrange
        val weekStart = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC)
        val weekEnd = OffsetDateTime.of(2023, 8, 6, 10, 30, 0, 0, ZoneOffset.UTC)

        // Act
        val result = weekDataHelper.getWeekDateText(weekStart, weekEnd)

        // Assert
        assertThat(result).isEqualTo("2023-08-01 - 08-06")
    }

    @Test
    fun `setDateTabs should set tabs for weekdays when called on Tuesday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 8, 1, 10, 30, 0, 0, ZoneOffset.UTC) // Tuesday

        // Act
        val result = weekDataHelper.setDateTabs(localTime)

        // Assert
        assertThat(result.size).isEqualTo(6)
        assertThat(localTime).isEqualTo(result[0])
        assertThat(localTime.plusDays(1)).isEqualTo(result[1])
    }

    @Test
    fun `setDateTabs should set tab for Sunday only when called on Sunday`() {
        // Arrange
        val localTime = OffsetDateTime.of(2023, 8, 6, 10, 30, 0, 0, ZoneOffset.UTC) // Sunday

        // Act
        val result = weekDataHelper.setDateTabs(localTime)

        // Assert
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(localTime)
    }

}


