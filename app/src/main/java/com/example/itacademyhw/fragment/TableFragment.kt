package com.example.itacademyhw.fragment

import android.os.Bundle
import com.example.itacademyhw.databinding.FragmentTableBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itacademyhw.UsersListAdapter
import com.example.itacademyhw.room.DBManager
import com.example.itacademyhw.room.UserRepository
import com.example.itacademyhw.room.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TableFragment : Fragment() {
    private var _binding: FragmentTableBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }
    private val adapter = UsersListAdapter() { onLongTap(it) }
    private val database: DBManager by lazy { DBManager.getDatabase(requireContext())}
    private val repository by lazy { UserRepository(database.userDao()) }

    companion object {
        private const val USER_DELETED_SUCCESS_MESSAGE = "User has been deleted."
        private const val JPG_EXTENSION = ".jpg"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTable()
    }

    private fun onLongTap(item: UserEntity) {
        lifecycleScope.launch {
            repository.delete(item)
            deleteImage(item)
            updateTable()
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), USER_DELETED_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteImage(item: UserEntity) {
        if (item.photoName.isNotEmpty()) {
            requireContext()
                .filesDir
                .listFiles()
                ?.filter { it.canRead() && it.isFile && it.name.endsWith(JPG_EXTENSION) }
                ?.find {
                    it.name == item.photoName
                }?.delete()
        }
    }

    private fun updateTable() {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            lifecycleScope.launch {
                repository.allUsers.collect { users ->
                    adapter.submitList(users)
                        withContext(Dispatchers.Main) {
                            recyclerView.adapter = adapter
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}