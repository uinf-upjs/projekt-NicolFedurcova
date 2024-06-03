package sk.upjs.hackstock.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentLoginBinding
import sk.upjs.hackstock.ui.home.HomeFragment


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var loginAttempts = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val application = requireNotNull(this.activity).application
        val appRepository = (application as MainApplication).repository
        val factory = LoginViewModel.LoginViewModelFactory(appRepository)

        val loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.login(username, password)
        }

        loginViewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_loginFragment_to_fragment_home)
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                loginAttempts++
                if (loginAttempts >= 3) {
                    binding.loginButton.isEnabled = false // Disable login button
                    binding.loginButton.postDelayed({
                        binding.loginButton.isEnabled = true // Enable login button after 20 seconds
                    }, 20000)
                    loginAttempts=0
                }
            }
        })

        loginViewModel.warningMessage.observe(viewLifecycleOwner, Observer { message ->
            if (loginAttempts>0){
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                binding.warning.apply {
                    text = message
                    visibility = if (message.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }
        })

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}