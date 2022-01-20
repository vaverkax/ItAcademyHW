package com.example.itacademyhw.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.itacademyhw.ImageItem
import com.example.itacademyhw.databinding.FragmentEditBinding
import com.example.itacademyhw.room.DBManager
import com.example.itacademyhw.room.UserRepository
import com.example.itacademyhw.room.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import androidx.core.app.ActivityCompat
import com.example.itacademyhw.R
import java.io.File


class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    private val database: DBManager by lazy { DBManager.getDatabase(requireContext())}
    private val repository by lazy { UserRepository(database.userDao()) }
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val fileName = UUID.randomUUID().toString()
        val isPhotoSaved = saveFileToInternalStorage(fileName, bitmap)
        if (isPhotoSaved) {
            loadImage(fileName)
            Toast.makeText(requireContext(), SUCCESS_SAVE_PHOTO_MESSAGE, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), FAILURE_SAVE_PHOTO_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }
    private var photoName: String? = null

    companion object {
        private const val SUCCESS_SAVE_PHOTO_MESSAGE = "Successfully saved photo"
        private const val FAILURE_SAVE_PHOTO_MESSAGE = "Failed to saved photo"
        private const val JPG_EXTENSION = ".jpg"
        private const val ERROR_MESSAGE = "Something went wrong!"
        private const val REQUEST_CODE = 200
        private const val IMAGE_QUALITY = 80
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            addBtn.setOnClickListener {
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                val imageName = photoName ?: ""
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.insert(UserEntity(firstName = firstName, lastName = lastName, photoName = imageName))
                    withContext(Dispatchers.Main) {
                        firstNameEditText.text.clear()
                        lastNameEditText.text.clear()
                        imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_launcher_foreground))
                        photoName = null
                    }
                }
            }
            imageView.setOnClickListener{
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    requestPermission()
                    return@setOnClickListener
                }
                takePhoto.launch(null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
    }

    private fun loadImage(fileName: String) {
        photoName?.let { deletePhoto(it) }
         val photo = requireContext()
            .filesDir
            .listFiles()
            ?.filter { it.canRead() && it.isFile && it.name.endsWith(JPG_EXTENSION) }
            ?.map { file ->
                val bytes = file.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                ImageItem.Internal(file.name, bmp)
            }
             ?.find {
                 it.fileName == "$fileName$JPG_EXTENSION"
             }
            if (photo != null) {
                binding.imageView.setImageBitmap(photo.bitmap)
                photoName = "$fileName$JPG_EXTENSION"
            }
    }

    private fun findFile(fileName: String): File? {
         return requireContext()
            .filesDir
            .listFiles()
            ?.filter { it.canRead() && it.isFile && it.name.endsWith(JPG_EXTENSION) }
            ?.find {
                it.name == fileName
            }
    }

    private fun deletePhoto(fileName: String) {
        try {
            findFile(fileName)?.delete()
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "$ERROR_MESSAGE$e", Toast.LENGTH_SHORT)
        }
    }

    private fun saveFileToInternalStorage(fileName: String, image: Bitmap): Boolean {
        return try {
            requireContext().openFileOutput("$fileName$JPG_EXTENSION", Context.MODE_PRIVATE).use { stream ->
                if (!image.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }
}