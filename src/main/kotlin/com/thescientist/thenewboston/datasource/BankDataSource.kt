package com.thescientist.thenewboston.datasource

import com.thescientist.thenewboston.model.Bank

interface BankDataSource {

    fun retrieveBanks(): Collection<Bank>
    fun findBank(accountNumber: String): Bank?
}