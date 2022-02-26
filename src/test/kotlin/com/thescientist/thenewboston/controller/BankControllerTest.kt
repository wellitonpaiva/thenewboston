package com.thescientist.thenewboston.controller

import com.thescientist.thenewboston.model.Bank
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    internal fun `should return all banks`() {
        mockMvc.get("/api/banks")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON)}
                jsonPath("$[0].accountNumber") { value("1234") }
        }
    }

    @Test
    internal fun `should return the bank with the given account number`() {
        mockMvc.get("/api/banks/1234")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType(MediaType.APPLICATION_JSON)) }
                jsonPath("accountNumber") {value("1234")}
                jsonPath("trust") {value("3.14")}
                jsonPath("transactionFee") {value("17")}
            }
    }
}