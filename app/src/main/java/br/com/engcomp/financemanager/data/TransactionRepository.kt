package br.com.engcomp.financemanager.data

import br.com.engcomp.financemanager.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionDao.getTransactionById(id)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }
}