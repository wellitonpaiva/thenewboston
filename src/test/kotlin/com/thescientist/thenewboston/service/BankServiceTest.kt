package com.thescientist.thenewboston.service

import com.thescientist.thenewboston.datasource.BankDataSource
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class BankServiceTest {

    private val dataSource: BankDataSource = mockk(relaxed = true)

    private val bankService = BankService(dataSource)

    @Test
    internal fun `should call its datasource to retrieve banks`() {
        bankService.getBanks()
        verify(exactly = 1) { dataSource.retrieveBanks() }
    }

    @Test
    internal fun `should call its datasource to find bank`() {
        val accountNumber = "1234"
        bankService.getBank(accountNumber)
        verify(exactly = 1) { dataSource.retrieveBank(accountNumber) }
    }
}