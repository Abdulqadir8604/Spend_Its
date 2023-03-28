package com.lamaq.spendits

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class Collection(
    var name: String = "",
    var createdTime: Date = Date(),
    private var notes: RealmList<Note> = RealmList()
) : RealmObject() {

    val totalAmount: Double
        get() = notes.sumOf { it.amt.toDouble() }

    fun aq_deleteFromRealm() {
        notes.forEach { it.deleteFromRealm() }
        deleteFromRealm()
    }
}
