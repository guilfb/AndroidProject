package com.example.tp2

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.tp2.database.MyDatabase
import com.example.tp2.databinding.FragmentIdentityBinding
import com.example.tp2.model.User
import com.example.tp2.viewmodel.IdentityViewModel
import com.example.tp2.viewmodelfactory.IdentityViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class IdentityFragment : Fragment() {

    private lateinit var binding: FragmentIdentityBinding
    private lateinit var viewModel: IdentityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identity, container, false)

        val countries = arrayOf("Allemagne","Angleterre","Belgique","France","Irelande","Italy","Portugal","Spain")

        val application = requireNotNull(this.activity).application
        val sharedPreferences = application.getSharedPreferences("SP_USER", Context.MODE_PRIVATE)

        val id = sharedPreferences.getLong("ID",0L)

        val dataSource = MyDatabase.getInstance(application).userDao
        val viewModelFactory = IdentityViewModelFactory(dataSource, application, id)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(IdentityViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.apply {
            tiFirstname.hint = getString(R.string.firstname)
            tiLastname.hint = getString(R.string.lastname)
            tiEmail.hint = getString(R.string.email)
            tiPassword.hint = getString(R.string.password)
            tiPasswordConfirm.hint = getString(R.string.password)
            tiAddress.hint = getString(R.string.address)
            tiCity.hint = getString(R.string.city)
            btValidate.text = getString(R.string.validate)
        }

        binding.tiCountrySpinner.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, countries)
        binding.tiCountrySpinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val country = parent?.getItemAtPosition(position).toString()
                viewModel.countrySelected = country
            }
        }

        viewModel.user.observe(this, Observer { user ->
            user?.let {
                binding.tiCountrySpinner.setSelection(countries.indexOf(user.country))
            }
        })

        binding.tiBirthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.tiBirthday.text =  SpannableStringBuilder("$dayOfMonth/$monthOfYear/$year")
                viewModel.user.value?.birthdayDate = Date(year,monthOfYear,dayOfMonth).time
            }, year, month, day)
            dpd.show()
        }

        viewModel.navigateToOtherActivity.observe(this, Observer { message ->
            message?.let {
                if(message == "ok") {
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
                    editor.putBoolean("ISLOGGEDIN", true)
                    editor.apply()

                    this.findNavController().navigate(IdentityFragmentDirections.actionIdentityFragmentToApiListMovieFragment())
                } else {
                    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
                }
                viewModel.doneValidateNavigating()
            }
        })
        return binding.root
    }
}

@BindingAdapter("userDateOfBirth")
fun TextView.setUserDateOfBirth(item: User?) {
    val dateText: String
    dateText = if(item?.birthdayDate != null) {
        val date = Date(item!!.birthdayDate)
        val f = SimpleDateFormat("dd/MM/yy")
        f.format(date)
    } else {
        ""
    }
    text = dateText
}