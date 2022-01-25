package com.example.itacademyhw.presenter

import com.example.itacademyhw.model.DataStore
import com.example.itacademyhw.fragment.MainFragment
import kotlinx.coroutines.*

class MainPresenter(private val datastore: DataStore) {

    private var view: MainFragment? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val viewScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onAttachView(view: MainFragment) {
        this.view = view
    }

    fun onDetachView() {
        viewScope.coroutineContext.cancelChildren()
        this.view = null
    }

    fun destroy() {
        viewScope.cancel()
        scope.cancel()
    }

    private fun updateUI() {
        viewScope.launch {
            val items = withContext(Dispatchers.IO) {
                datastore.fetchData()
            }
            view?.showItem(items)
            view?.showToast("Data received")
        }
    }

    fun onViewClick() {
        view?.showToast("Button clicked")
        updateUI()
    }
}