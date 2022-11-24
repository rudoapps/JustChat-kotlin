package es.rudo.androidbaseproject.domain.models.configuration

import es.rudo.androidbaseproject.domain.models.configuration.BasicConfiguration
import es.rudo.firebasechat.helpers.Constants.DEFAULT_NODE_FIREBASE

class FirebaseConfiguration(projectName: String, node: String? = DEFAULT_NODE_FIREBASE) :
    BasicConfiguration(Type.FIREBASE) {

    val nodeFirebase = node
    val firebaseProjectName = projectName
}
