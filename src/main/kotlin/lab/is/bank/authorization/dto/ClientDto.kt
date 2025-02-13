package lab.`is`.bank.authorization.dto

data class ClientDto(
    val passportID: String,
) {
    constructor() : this("")
}
