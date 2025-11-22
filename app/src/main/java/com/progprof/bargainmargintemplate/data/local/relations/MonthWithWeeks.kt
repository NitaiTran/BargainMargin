package com.progprof.bargainmargintemplate.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity

data class MonthWithWeeks(
    @Embedded val month: MonthEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "monthId"
    )
    val weeks: List<WeekEntity>
)