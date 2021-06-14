package com.varpihovsky.jetiq.back.dto.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.varpihovsky.jetiq.back.dto.SubjectDetailsDTO
import com.varpihovsky.jetiq.back.dto.SubjectTaskDTO

data class SubjectDetailsWithTasks(
    @Embedded val subjectDetailsDTO: SubjectDetailsDTO,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectDetailsId"
    )
    val subjectTasks: List<SubjectTaskDTO>
)
