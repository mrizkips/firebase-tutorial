package com.mrizkips.firebasetutorial.fragments.auth

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrizkips.firebasetutorial.R
import com.mrizkips.firebasetutorial.databinding.FragmentRegisterBinding
import com.mrizkips.firebasetutorial.models.User
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            authRegister(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.ivRegisterPhotoProfile.setImageURI(it)
            photoUri = it
        }

        binding.ivRegisterPhotoProfile.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun authRegister(email: String, password: String) {
        // TODO 3: Lengkapi kode register user menggunakan email dan password dibawah ini

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.context, "User berhasil register", Toast.LENGTH_SHORT)
                        .show()
                    uploadPhoto {
                        requireView().findNavController()
                            .navigate(R.id.action_registerFragment_to_mainActivity)
                        requireActivity().finish()
                    }
                } else {
                    Toast.makeText(this.context, "Gagal register", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadPhoto(callback: () -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val uuid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("images/$uuid")

        imageRef.putFile(photoUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener {
                        saveAllUserData(
                            binding.etNama.text.toString(),
                            binding.etEmail.text.toString(),
                            it.result.toString()
                        ) {
                            callback()
                        }
                    }
                } else {
                    Toast.makeText(this.requireContext(), "Gagal upload", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveAllUserData(name: String, email: String, photoUrl: String, callback: () -> Unit) {
        // TODO 4: Lengkapi kode dibawah ini untuk menyimpan data user ke realtime database

        database.child("users").child(uid!!).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.requireContext(), "Berhasil simpan ke db", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this.requireContext(), "Gagal simpan ke db", Toast.LENGTH_SHORT).show()
                }
                callback()
            }
    }

}