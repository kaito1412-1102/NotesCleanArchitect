package com.example.database.sqlbuilder

import com.example.model.DeadlineTagFilter
import com.example.model.StatusFilter

class SqlBuilder {

    private var status = StatusFilter.ALL.toString()
    private var deadlineTag = StatusFilter.ALL.toString()

    fun withConditionStatus(type: StatusFilter): SqlBuilder {
        status = type.toString()
        return this
    }

    fun withConditionDeadlineFilter(type: DeadlineTagFilter): SqlBuilder {
        deadlineTag = type.toString()
        return this
    }

    fun build(): String {
        val haveCondition = if (status == StatusFilter.ALL.toString() && deadlineTag == DeadlineTagFilter.ALL.toString()) "" else "WHERE"
        val haveAndCondition = if (status != StatusFilter.ALL.toString() && deadlineTag != DeadlineTagFilter.ALL.toString()) "AND" else ""

        val sortCondition =
            if (haveCondition == "") "ORDER BY (CASE WHEN date(deadline_time / 1000, 'unixepoch') = date('now') THEN 0 ELSE 1 END), date(deadline_time / 1000, 'unixepoch') DESC"
            else ""

        val conditionStatusSql = if (status != StatusFilter.ALL.toString()) "status = \"$status\"" else ""
        val conditionDeadlineTagSql = when (deadlineTag) {
            DeadlineTagFilter.TODAY.toString() -> "date(deadline_time / 1000, 'unixepoch') = date('now')"
            DeadlineTagFilter.UPCOMING.toString() -> "date(deadline_time / 1000, 'unixepoch') > date('now')"
            DeadlineTagFilter.OVERDUE.toString() -> "date(deadline_time / 1000, 'unixepoch') < date('now')"
            else -> ""
        }
        return "SELECT * FROM NOTE $haveCondition $conditionStatusSql $haveAndCondition $conditionDeadlineTagSql $sortCondition"
    }
}