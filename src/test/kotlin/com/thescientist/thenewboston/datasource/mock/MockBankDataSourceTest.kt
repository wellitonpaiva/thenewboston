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

    @Test
    internal fun `should add a new bank`() {
        val bank = Bank("2345", 1.0, 1)
        mockDataSource.createBank(bank)
        assertThat(mockDataSource.banks.size).isEqualTo(4)
    }

    @Test
    internal fun `should throw exception when bank already exists`() {
        val bank = Bank("1234", 1.0, 1)
        assertThrows<IllegalArgumentException> { mockDataSource.createBank(bank) }
    }

    @Test
    internal fun `should update an existing bank`() {
        val bank = Bank("1234", 1.0, 1)
        mockDataSource.updateBank(bank)
        assertThat(mockDataSource.retrieveBank(bank.accountNumber)).isEqualTo(bank)
    }

    @Test
    internal fun `should throw exception when update a not existing bank`() {
        val notExistingBank = Bank("444", 1.0, 1)
        assertThrows<NoSuchElementException> { mockDataSource.updateBank(notExistingBank) }
    }
}