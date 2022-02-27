package com.thescientist.thenewboston.datasource.mock

import com.thescientist.thenewboston.model.Bank
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

internal class MockBankDataSourceTest {

    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide some mock data`() {
        val banks = mockDataSource.retrieveBanks()
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch { it.trust != 0.0 }
        assertThat(banks).anyMatch { it.transactionFee != 0 }
    }

    @Test
    internal fun `should provide a single bank when exists`() {
        val accountNumber = "1234"
        val bank = mockDataSource.retrieveBank(accountNumber)
        assertThat(bank).isEqualTo(Bank("1234", 3.14, 17))
    }

    @Test
    internal fun `should throw exception when bank not exists`() {
        assertThrows<NoSuchElementException> { mockDataSource.retrieveBank("1") }
    }
}