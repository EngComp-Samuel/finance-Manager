package br.com.engcomp.financemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.engcomp.financemanager.data.FinanceDatabase
import br.com.engcomp.financemanager.data.TransactionRepository
import br.com.engcomp.financemanager.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel (application: Application) : AndroidViewModel(application){
    private val repository: TransactionRepository

    val allTransactions: Flow<List<Transaction>>

    init {
        val transactionDao = FinanceDatabase.getInstance(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        allTransactions = repository.allTransactions
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    fun getTransactionById(id: Long): Flow<Transaction> {
        return repository.getTransactionById(id)
    }



}