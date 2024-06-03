package sk.upjs.hackstock.ui.settings

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.R
import sk.upjs.hackstock.entities.User
import java.text.SimpleDateFormat
import java.util.*

class ProfileSettingsFragment : Fragment() {

    private lateinit var viewModel: ProfileSettingsViewModel
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextDateOfBirth: EditText
    private lateinit var spinnerCountry: Spinner
    private lateinit var editTextCity: EditText
    private lateinit var editTextOccupation: EditText
    private lateinit var editTextAnnualIncome: EditText
    private lateinit var buttonSave: Button
    private var user: User? = null
    private lateinit var spinnerStatus: Spinner
    private lateinit var languageSwitch: Switch
    private val statuses = listOf("Married", "Single", "In a relationship", "Divorced", "Widowed")
    private val locale = Locale.getDefault()
    private val countries = Locale.getISOCountries().map { countryCode ->
        Locale("", countryCode).getDisplayCountry(Locale("en"))
    }.sorted()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val sharedPreferences = MainApplication.prefs
//        val language = sharedPreferences.getString("my_lang", "en")
//        if (language != null) {
//            setLocale(language)
//        }

        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        val application = requireNotNull(this.activity).application
        val appRepository = (application as MainApplication).repository

        viewModel = ViewModelProvider(this, ProfileSettingsViewModelFactory(appRepository)).get(ProfileSettingsViewModel::class.java)

        editTextName = root.findViewById(R.id.editTextName)
        editTextSurname = root.findViewById(R.id.editTextSurname)
        editTextDateOfBirth = root.findViewById(R.id.editTextDateOfBirth)
        spinnerCountry = root.findViewById(R.id.spinnerCountry)
        editTextCity = root.findViewById(R.id.editTextCity)
        spinnerStatus = root.findViewById(R.id.spinnerStatus)
        editTextOccupation = root.findViewById(R.id.editTextOccupation)
        editTextAnnualIncome = root.findViewById(R.id.editTextAnnualIncome)
        buttonSave = root.findViewById(R.id.buttonSave)
        //languageSwitch = root.findViewById(R.id.languageSwitch)

        editTextDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        buttonSave.setOnClickListener {
            saveUser()
            findNavController().navigate(R.id.navigation_home)
        }

//        languageSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                setLocale("en")
//            } else {
//                setLocale("sk")
//            }
//        }

        setupCountrySpinner()
        setupStatusSpinner()

        return root
    }

//    private fun setLocale(language: String) {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val config = resources.configuration
//        config.setLocale(locale)
//        val newContext = context?.createConfigurationContext(config)
//        // Update the context of the application with the new locale
//        // Save the selected language in shared preferences
//        val editor = MainApplication.prefs.edit()
//        editor.putString("my_lang", language)
//        editor.apply()
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmail = MainApplication.prefs.getString("user_email", "")
        if (userEmail != null) {
            lifecycleScope.launch {
                user = viewModel.getUserByEmail(userEmail)
                user?.let {
                    populateUserData(it)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupCountrySpinner() {

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = adapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCountry = parent.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
    }

    private fun setupStatusSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatus = parent.getItemAtPosition(position) as String
                // Do something with selectedStatus if needed
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when nothing is selected
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                editTextDateOfBirth.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveUser() {
        val name = editTextName.text.toString()
        val surname = editTextSurname.text.toString()
        val dateOfBirthStr = editTextDateOfBirth.text.toString()
        val country = spinnerCountry.selectedItem.toString()
        val city = editTextCity.text.toString()
        val status = spinnerStatus.selectedItem.toString()
        val occupation = editTextOccupation.text.toString()
        val annualIncome = editTextAnnualIncome.text.toString().toDoubleOrNull()

        if (name.isEmpty() || surname.isEmpty() || dateOfBirthStr.isEmpty() ||
            country.isEmpty() || city.isEmpty() || status.isEmpty() || occupation.isEmpty() || annualIncome == null) {
            Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val dateOfBirth = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateOfBirthStr)

        user?.let { user ->
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.updateUserDetails(
                    user.userId, name, surname, dateOfBirth, country, city, status, occupation, annualIncome
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun populateUserData(user: User) {
        editTextName.setText(user.name)
        editTextSurname.setText(user.surname)
        editTextDateOfBirth.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(user.dateOfBirth))
        val countryIndex = countries.indexOf(user.country)
        if (countryIndex >= 0) {
            spinnerCountry.setSelection(countryIndex)
        }
        editTextCity.setText(user.city)
        val statusIndex = statuses.indexOf(user.status)
        if (statusIndex >= 0) {
            spinnerStatus.setSelection(statusIndex)
        }
        editTextOccupation.setText(user.occupation)
        editTextAnnualIncome.setText(user.annualIncome.toString())
    }
}
