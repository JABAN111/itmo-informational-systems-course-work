package lab.`is`.bank.dto

data class ClientDto(
    val passportID: String,
//    var email: String = ""
) {
    constructor() : this("")
}