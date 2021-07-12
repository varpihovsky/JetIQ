package com.varpihovsky.repo_data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.varpihovsky.repo_data.SubjectDetailsDTO
import com.varpihovsky.repo_data.SubjectTaskDTO

data class SubjectDetailsWithTasks(
    @Embedded val subjectDetailsDTO: SubjectDetailsDTO,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectDetailsId"
    )
    val subjectTasks: List<SubjectTaskDTO>
)
