package com.mrizkips.firebasetutorial.fragments.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mrizkips.firebasetutorial.MainActivity
import com.mrizkips.firebasetutorial.R
import com.mrizkips.firebasetutorial.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterNow.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            authLogin(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loggedIn()
        }
    }

    private fun authLogin(email: String, password: String) {
        // TODO 5: Lengkapi kode dibawah ini untuk melakukan login
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.requireContext(), "Berhasil login", Toast.LENGTH_SHORT).show()
                    loggedIn()
                } else {
                    Toast.makeText(this.requireContext(), "Gagal login", Toast.LENGTH_SHORT).show()
                    Log.w("auth", "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun loggedIn() {
        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        requireActivity().finish()
    }
}