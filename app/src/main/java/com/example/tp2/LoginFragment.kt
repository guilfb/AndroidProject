package com.example.tp2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.FragmentLoginBinding
import com.example.tp2.viewmodel.LoginViewModel
import com.example.tp2.viewmodelfactory.LoginViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = MyDatabase.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)

        val sharedPreferences = application.getSharedPreferences("SP_USER", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("ISLOGGEDIN",false)
        val id = sharedPreferences.getLong("ID",0L)
        val firstname = sharedPreferences.getString("FIRSTNAME","FirstName")
        val lastname = sharedPreferences.getString("LASTNAME","LastName")

        val email = sharedPreferences.getString("EMAIL","Email")
        val password = sharedPreferences.getString("PASSWORD","Password")

        // System.out.println("$id $firstname $lastname $email $password")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.apply {
            tiEmail.hint = getString(R.string.emailLogin)
            tiPassword.hint = getString(R.string.pwdLogin)
            btValidate.text = getString(R.string.login)
            btIdentity.text = getString(R.string.signup)
        }

        // SI LE USER S'EST DEJA LOGGE AUPARAVANT
        if(isLoggedIn) {
            this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToApiListMovieFragment())
        }

        // NAVIGATE TO LIST FRAGMENT --> CONNEXION
        viewModel.navigateToOtherActivity.observe(this, Observer { message ->
            message?.let {
                when {
                    message.contains("ok:") -> {
                        val editor = sharedPreferences.edit()
                        viewModel.user.value?.id?.let { editor.putLong("ID", it) }
                        viewModel.user.value?.firstname?.let { editor.putString("FIRSTNAME", it) }
                        viewModel.user.value?.lastname?.let { editor.putString("LASTNAME", it) }
                        viewModel.user.value?.birthdayDate?.let { editor.putLong("BIRTHDATE", it) }
                        viewModel.user.value?.gender?.let { editor.putString("GENDER", it) }
                        viewModel.user.value?.email?.let { editor.putString("EMAIL", it) }
                        viewModel.user.value?.password?.let { editor.putString("PASSWORD", it) }
                        viewModel.user.value?.address?.let { editor.putString("ADDRESS", it) }
                        viewModel.user.value?.city?.let { editor.putString("CITY", it) }
                        viewModel.user.value?.country?.let { editor.putString("COUNTRY", it) }

                        if(message == "ok:oui"){
                            editor.putBoolean("ISLOGGEDIN", true)
                        }

                        editor.apply()

                        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToApiListMovieFragment())
                    }
                    message == "admin" -> this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
                    else -> {
                        val msg = when (message) {
                            "ko" -> "Email ou mot de passe errone"
                            "mail" -> "Email non saisi"
                            "pwd" -> "Mot de passe non saisi"
                            else -> "Erreur inconnue"
                        }
                        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.doneValidateNavigating()
            }
        })

        // NAVIGATE TO SIGN UP FRAGMENT --> SIGN UP
        viewModel.navigateToIdentity.observe(this, Observer { user ->
            user?.let {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToIdentityFragment())
                viewModel.doneOnSignUp()
            }
        })

        return binding.root
    }
}