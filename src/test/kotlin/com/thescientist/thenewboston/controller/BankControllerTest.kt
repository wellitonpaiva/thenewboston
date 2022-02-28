package com.thescientist.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.thescientist.thenewboston.model.Bank
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor (
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    private val baseUrl = "/api/banks"

    @Nested
    @DisplayName("getBanks()")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks{

        @Test
        internal fun `should return all banks`() {
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON)}
                    jsonPath("$[0].accountNumber") { value("1234") }
            }
        }
    }

    @Nested
    @DisplayName("getBank()")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        internal fun `should return the bank with the given account number`() {
            val accountNumber = "1234"
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType(MediaType.APPLICATION_JSON)) }
                    jsonPath("accountNumber") { value(accountNumber) }
                    jsonPath("trust") { value("3.14") }
                    jsonPath("transactionFee") { value("17") }
                }
        }

        @Test
        internal fun `should Not Found if the account number does not exist`() {
            val accountNumber = "not existing accountNumber"

            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        internal fun `should add new bank`() {
            val newBank = Bank("acc123", 31.415, 2)

            val jsonNewBank = objectMapper.writeValueAsString(newBank)
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = jsonNewBank
            }
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("accountNumber") { value("acc123") }
                    jsonPath("trust") { value("31.415") }
                    jsonPath("transactionFee") { value("2") }
                }

            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect { content { json(jsonNewBank) } }
        }

        @Test
        internal fun `should return BAD REQUEST if bank with given account number already exists`() {
            val invalidBank = Bank("1234", 1.0, 1)

            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        internal fun `should update an existing bank`() {
            val updateBank = Bank("1234", 31.415, 2)
            val jsonUpdateBank = objectMapper.writeValueAsString(updateBank)

            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUpdateBank
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(jsonUpdateBank)
                    }
                }
            mockMvc.get("$baseUrl/${updateBank.accountNumber}")
                .andExpect { content { json(jsonUpdateBank) } }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {

        @Test
        @DirtiesContext
        internal fun `should delete the bank with the given account number`() {
            val accountNumber = "1234"

            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNoContent() } }

            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        internal fun `should return NOT FOUND if no bank with given account number exists`() {
            val notExistingAccountNumber = "1122"

            mockMvc.delete("$baseUrl/$notExistingAccountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}