package es.rudo.androidbaseproject.domain.models.configuration

open class BasicConfiguration(connectionType: Type) {

    open var type: Type = connectionType

    enum class Type {
        FIREBASE, BACK, MIX, USER_CONF
    }
}
