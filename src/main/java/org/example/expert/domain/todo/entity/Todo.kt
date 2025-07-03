package org.example.expert.domain.todo.entity

import com.querydsl.core.annotations.QueryProjection
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Entity
public open class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val title: String,
    @Column(nullable = false)
    val contents: String,

    val weather: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    var comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.PERSIST])
    var managers: MutableList<Manager> = mutableListOf(),

    ) : Timestamped() {


    constructor(_title: String, _contents: String, _weather: String, _user: User) : this(
        title = _title,
        contents = _contents,
        weather = _weather,
        user = _user,
    )

    @QueryProjection
    constructor(_id: Long, _title: String, _contents: String, _weather: String, _user: User) : this(
        id = _id,
        title = _title,
        contents = _contents,
        weather = _weather,
        user = _user
    )

}